import processing.core.PImage;
import java.util.List;

/*
Player: the users avatar and gamepiece for moving around in the world
 */

final class Player extends Person {

    //For Parsing
    public static final String KEY = "player";
    public static final int NUM_PROPERTIES = 4;

    //Instance Variables
    private Point nextPosition;
    private boolean input;

    //Class Methods
    public Player(String id, Point position, List<PImage> u, List<PImage> d, List<PImage> l, List<PImage> r) {
        super(id, position, d, u, d, l, r);
        this.nextPosition = position;
        this.input = true;
    }
    public static boolean parse(String [] properties, ImageStore imageStore, World world) {
        System.out.println("HERE!");
        if (NUM_PROPERTIES == properties.length)
        {


            String id = properties[Functions.ID];
            Point pt = new Point(Integer.parseInt(properties[Functions.COL]),
                    Integer.parseInt(properties[Functions.ROW]));
            if (VirtualWorld.lastLoc != null) {
                pt = VirtualWorld.lastLoc;
            }
            List<PImage> images = imageStore.getImageList(KEY);



            Player entity = createPlayer(id,
                    pt,
                    images);

            world.setPlayer(entity);
        }
        return NUM_PROPERTIES == properties.length;
    }
    public static Player createPlayer(String id, Point position, List<PImage> images) {
        return new Player(id, position,
                images.subList(0, 4), images.subList(4, 8), images.subList(8, 12), images.subList(12, 16));
    }
    public int compareTo(Object other) {
        if (other instanceof Entity) {
            return this.getPosition().y - ((Entity)other).getPosition().y;
        }
        else {
            throw new IllegalArgumentException("Object of type (" + other.getClass() + ") not comparable to Entity");
        }
    }

    //Accessors
    public Point getNextPosition() {
        return nextPosition;
    }
    //Mutators
    public void setNextPosition(Point position) {
        nextPosition = position;
    }
    public void enableInput() {
        input = true;
    }
    public void disableInput() {
        input = false;
    }
    public void toggleInput() {
        input = !input;
    }

    //Others
    public void saveLast() {
        VirtualWorld.lastLoc = getPosition();
    }
    public void scheduleActions(World world, EventScheduler scheduler, char d) {
        if (input) {
            setNextPosition(getPosition().indir(d));
            disableInput();
            Object occupant = world.getOccupancyCell(getNextPosition());
            if (occupant instanceof Atlantis) {
                saveLast();
            }
                scheduleActions(world, scheduler);
        }
        else {
            System.out.println("HANDSTIIIEED");
        }
    }
    public void scheduleActions(World world, EventScheduler scheduler) {
        animationpart(scheduler);
        activitypart(scheduler, world);
    }
    public Point get_final_destination(World world, EventScheduler scheduler) {
        return nextPosition;
    }
    public Point getNextDestination(Point final_destination, World world) {
        if (getPosition().adjacent(final_destination)) {
            return final_destination;
        }
        else {
            throw new Error("No Clear Path");
        }
    }
    public void task(Point point, World world, EventScheduler scheduler) {
        if (world.isOccupied(point)) {
            Entity occupant = world.getOccupancyCell(point);
            if (occupant instanceof Atlantis) {
                saveLast();
                ((Atlantis)occupant).scheduleActions(world, scheduler);
            }
            else {
                nextPosition = this.getPosition();
            }
        }
        else if (world.withinBounds(nextPosition) && world.getBackgroundCell(nextPosition) instanceof Trap) {
            double chance = Math.random()*4;
            if (chance > 3){
                saveLast();
                scheduler.pause();
                disableInput();
                VirtualWorld.setPokemon(Pokemon.newZigzagoon());
            }
        }
        world.moveEntity(this, nextPosition);
        enableInput();
    }
    public void else1(Point point, World world) {
        throw new Error("Somehow next and final don't match");
    }
    public void else2(Point point, World world, EventScheduler scheduler) {
        turn_to(point);
        enableInput();
    }
    public void else3() {
        throw new Error("next equals current");
    }
    public void afterAll(World world, EventScheduler scheduler) {
    }
}
