import processing.core.PImage;
import java.util.*;


public class World implements Loadable{

    //Instance Variables
    private int numRows;
    private int numCols;
    private Background background[][];
    private Entity occupancy[][];
    private Set<Entity> entities;
    private Set<PolyEntity> polies;
    private Player player;

    public World(int numRows, int numCols, Background defaultBackground)
    {
        System.out.println(numRows + " " + numCols);
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();
        this.player = null;
        this.polies = new HashSet<>();

        for (int row = 0; row < numRows; row++)
        {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }
    public boolean processLine(String line, ImageStore imageStore) {
        try {
            String[] properties = line.split("\\s");
            if (properties.length > 0)
            {
                switch (properties[Functions.PROPERTY_KEY])
                {
                    case Background.KEY: return Background.parse(properties, imageStore, this);
                    case Trap.KEY: return Trap.parse(properties, imageStore, this);
                    case BoundaryGuardian.KEY: return BoundaryGuardian.parse(properties, imageStore, this);
                    case Obstacle.KEY: return Obstacle.parse(properties, imageStore, this);
                    case Ledge.KEY: return Ledge.parse(properties, imageStore, this);
                    case Atlantis.KEY: return Atlantis.parse(properties, imageStore, this);
                    case Tree.KEY: return Tree.parse(properties, imageStore, this);
                    case House.KEYA: case House.KEYB: return House.parse(properties, imageStore, this);
                    case BasicTalker.KEY: return BasicTalker.parse(properties, imageStore, this);
                    case Player.KEY: return Player.parse(properties, imageStore, this);
                    case Battler.KEY: return Battler.parse(properties, imageStore, this);
                }
            }
            return false;
        }
        catch (Exception e) {
            throw new LineProcessingError(e, line);
        }
    }

    //Accessors
    public Player getPlayer() {
        return player;
    }
    public Optional<PImage> getBackgroundOverlay(Point pos) {
        if (this.withinBounds(pos))
        {
            if (this.getBackgroundCell(pos) instanceof Trap){
                return Optional.of(((Trap)this.getBackgroundCell(pos)).getOverlay());
            }
            else {
                return Optional.empty();
            }
        }
        else
        {
            return Optional.empty();
        }
    }
    public int getNumRows() {
        return numRows;
    }
    public int getNumCols() {
        return numCols;
    }
    public Set<Entity> getEntities() {
        return entities;
    }
    protected Entity getOccupancyCell(Point pos)
    {
        return occupancy[pos.y][pos.x];
    }
    public Background getBackgroundCell(Point pos)
    {
        return background[pos.y][pos.x];
    }
    public Optional<PImage> getBackgroundImage(Point pos) {
        if (this.withinBounds(pos))
        {
            return Optional.of(getBackgroundCell(pos).getCurrentImage());
        }
        else
        {
            return Optional.empty();
        }
    }
    //Mutators
    public void setPlayer(Player p) {
        if (player != null) {
            throw new IllegalArgumentException();
        }
        else {
            player = p;
            tryAddEntity(player);
        }
    }
    public void tryAddPoly(PolyEntity poly) {
        poly.getComponents().stream().forEach(this::tryAddEntity);
    }
    public void scheduleActions(EventScheduler scheduler) {
        for (Entity entity : entities)
        {
            //Only start actions for entities that include action (not those with just animations)
            if (entity instanceof BoundaryGuardian) {
                if (((BoundaryGuardian) entity).getActionPeriod() > 0) {
                    ((BoundaryGuardian) entity).scheduleActions(this, scheduler);
                }
            }
        }
    }
    protected void setOccupancyCell(Point pos, Entity entity)
    {
        occupancy[pos.y][pos.x] = entity;
    }
    protected void setBackgroundCell(Point pos, Background bgnd)
    {
        background[pos.y][pos.x] = bgnd;
    }
    public void setBackground(Point pos, Background background) {
        if (withinBounds(pos))
        {
            setBackgroundCell(pos, background);
        }
    }
    public void pressA(EventScheduler scheduler) {
        Point point = player.getPosition().indir(player.getDir());
        if (isOccupied(point)) {
            Entity entity = getOccupancyCell(point);
            if (entity instanceof Person) {
                ((Person)entity).scheduleActions(this, scheduler);
            }
        }
    }
    //boolean checks
    public boolean isOccupied(Point pos) {
        return withinBounds(pos) &&
                getOccupancyCell(pos) != null;
    }
    public boolean withinBounds(Point pos) {
        return pos.y >= 0 && pos.y < numRows &&
                pos.x >= 0 && pos.x < numCols;
    }
    //actual changes
    public void tryAddEntity(Entity entity) {
        if (isOccupied(entity.getPosition()))
        {
            throw new IllegalArgumentException("position " + entity.getPosition() + " occupied by " + getOccupancyCell(entity.getPosition()).getId());
        }
        addEntity(entity);
    }
    public void addEntity(Entity entity) {
        if (withinBounds(entity.getPosition()))
        {
            setOccupancyCell(entity.getPosition(), entity);
            entities.add(entity);
        }
        else {
            System.out.println("OUTTA BOUNDS");
        }
    }
    protected void removeEntity(Entity entity)
    {
        removeEntityAt(entity.getPosition());
    }
    protected void removeEntityAt(Point pos) {
        if (withinBounds(pos) && getOccupancyCell(pos) != null) {
            entities.remove(getOccupancyCell(pos));
            setOccupancyCell(pos, null);
        }
    }
    public boolean moveEntity(Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (withinBounds(pos) && !pos.equals(oldPos))
        {
            setOccupancyCell(oldPos, null);
            removeEntityAt(pos);
            setOccupancyCell(pos, entity);
            entity.updatePosition(pos);
            return true;
        }
        return false;
    }
    //locators
    private static Optional<Entity> nearestEntity(List<Entity> entities, Point pos) {
        if (entities.isEmpty())
        {
            return Optional.empty();
        }
        else
        {
            Entity nearest = entities.get(0);
            int nearestDistance = pos.distanceSquared(nearest.getPosition());
            for (Entity other : entities)
            {
                int otherDistance = pos.distanceSquared(other.getPosition());

                if (otherDistance < nearestDistance)
                {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }
            return Optional.of(nearest);
        }
    }
    public Optional<Entity> findNearest(Point pos, Class classs) {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : entities)
        {
            if (entity.getClass() == classs)
            {
                ofType.add(entity);
            }
        }
        return nearestEntity(ofType, pos);
    }
}
