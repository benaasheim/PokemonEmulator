import processing.core.PImage;
import java.util.*;

/*
BoundaryGuardian: Used to stop player from crossing farther than supposed to if a certain checkpoint has not been reached
 */

public class BoundaryGuardian extends Person {

    //For Parsing
    public static final String KEY = "boundguard";
    private static final int POINT1 = 4;
    private static final int POINT2 = 5;
    private static final int POINT3 = 6;
    private static final int DIALOGUE = 7;
    private static final int DELETE_KEY = 8;
    private static final int IMAGE_KEY = 9;
    private static final int NUM_PROPERTIES = 10;

    //Instance Variables
    private PathingStrategy strategy;
    private List<Point> boundary;
    protected boolean walkingback;
    private Point originalposition;
    private String dialogue;

    //Class Methods
    public BoundaryGuardian(String id, Point position, List<PImage> u, List<PImage> d, List<PImage> l, List<PImage> r, List<Point> boundary, String dialogue) {
        super(id, position, d, u, d, l, r);
        this.strategy = new AStarPathingStrategy();
        this.boundary = boundary;
        this.originalposition = position;
        this.walkingback = false;
        this.dialogue = dialogue;
    }
    public static boolean parse(String [] properties, ImageStore imageStore, World world) {
        if (NUM_PROPERTIES == properties.length)
        {

            String id = properties[Functions.ID];
            Point pt = new Point(Integer.parseInt(properties[Functions.COL]),
                    Integer.parseInt(properties[Functions.ROW]));
            List<PImage> images = imageStore.getImageList(properties[IMAGE_KEY]);


            String dialogue = properties[DIALOGUE];
            Point pt1 = Point.fromString(properties[POINT1]);
            Point pt2 = Point.fromString(properties[POINT2]);
            Point pt3 = Point.fromString(properties[POINT3]);
            List<Point> boundary = new ArrayList<>();
            boundary.add(pt1); boundary.add(pt2); boundary.add(pt3);


            BoundaryGuardian entity = BoundaryGuardian.createBoundaryGuardian(id,
                    pt,
                    images,
                    boundary,
                    dialogue);

            if (!(VirtualWorld.gameState.hasprogress(properties[DELETE_KEY]))) {
                System.out.println("HERETOO " + properties[DELETE_KEY]);
                world.tryAddEntity(entity);
            }
        }

        return NUM_PROPERTIES == properties.length;
    }
    public static BoundaryGuardian createBoundaryGuardian(String id, Point position, List<PImage> images, List<Point> boundary, String dialogue) {
        return new BoundaryGuardian(id, position, images.subList(0, 4), images.subList(4, 8), images.subList(8, 12), images.subList(12, 16), boundary, dialogue);
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
    public String getdialogue() {
        return dialogue;
    }
    public Optional<Entity> getTarget(World world) {
        return world.findNearest(getPosition(), getTargetClass());
    }
    public Class getTargetClass(){
        return Player.class;
    }

    //Move Functions
    public void Speak() {
        VirtualWorld.nextMenu = dialogue;
        VirtualWorld.gameState.addProgress(getId());
    }
    public void scheduleActions(World world, EventScheduler scheduler) {
        activitypart(scheduler, world);
    }
    public Point get_final_destination(World world, EventScheduler scheduler) {
        Optional<Entity> Target = getTarget(world);
        if (Target.isPresent()) {
            if (!walkingback) {
                Entity player = Target.get();
                if (in_boundary(player)) {
                    if (player instanceof Player) {
                        freezPlayer((Player)player, scheduler);
                        animationpart(scheduler);
                        return player.getPosition();
                    }
                    else {
                        return getPosition();
                    }
                }
                else {
                    return getPosition();
                }
            }
            else {
                animationpart(scheduler);
                return originalposition;
            }
        }
        else {
            return getPosition();
        }
    }
    public boolean in_boundary(Entity entity) {
        for (Point point : boundary) {
            if (point.equals(entity.getPosition())){
                return true;
            }
        }
        return false;
    }
    public void freezPlayer(Player player, EventScheduler scheduler) {
        scheduler.pause();
        player.disableInput();
        player.setImageIndex(0);
        scheduler.unscheduleAllEvents(player);
        scheduler.pause();
    }
    public Point getNextDestination(Point final_destination, World world) {
        if (getPosition().adjacent(final_destination)) {
            return final_destination;
        }
        else {
            List<Point> path = new LinkedList<>();
            if (findGoal(getPosition(), final_destination, world, path)) {
                return path.get(0);
            }
            else {
                throw new Error("No Clear Path");
            }
        }
    }
    public void task(Point point, World world, EventScheduler scheduler) {
        if (!walkingback) {
            perfom_task(point, world, scheduler);
        }
        else {
            world.moveEntity(this, point);
            world.getPlayer().enableInput();
        }
        walkingback =! walkingback;
    }
    public void FlipPlayer(Point point, World world) {
        Entity entity = world.getOccupancyCell(point);
        System.out.println(dialogue);
        Point newpoint = getPosition().otherSideOf(point);
        world.moveEntity(entity, newpoint);
    }
    public void else1(Point point, World world) {
        world.moveEntity(this, point);
    }
    public void else2(Point point, World world, EventScheduler scheduler) {
        turn_to(point);
    }
    public void else3() {
    }
    public void afterAll(World world, EventScheduler scheduler) {
        scheduleActions(world, scheduler);
    }
    private boolean findGoal(Point pos, Point goal, World world, List<Point> path) {
        List<Point> points;

        points = strategy.computePath(pos, goal,
                p ->  world.withinBounds(p) && !world.isOccupied(p),
                Point::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS);

        if (points.size() == 0)
        {
            return false;
        }
        path.addAll(points);
        return true;
    }
    public void perfom_task(Point point, World world, EventScheduler scheduler) {
        FlipPlayer(point, world);
        world.getPlayer().saveLast();
        Speak();
    }
}
