import processing.core.PImage;
import java.util.*;

public class BasicTalker extends Person {

    //For Parsing
    public static final String KEY = "talk";
    private static final int DIALOGUE = 4;
    private static final int IMAGE_KEY = 5;
    private static final int NUM_PROPERTIES = 6;

    //Instance Variables
    private String dialogue;

    //Class Methods
    public BasicTalker(String id, Point position, List<PImage> u, List<PImage> d, List<PImage> l, List<PImage> r, String dialogue) {
        super(id, position, d, u, d, l, r);
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


            BasicTalker entity = BasicTalker.createBasicTalker(id,
                    pt,
                    images,
                    dialogue);


            world.tryAddEntity(entity);
        }
        return NUM_PROPERTIES == properties.length;
    }
    public static BasicTalker createBasicTalker(String id, Point position, List<PImage> images, String dialogue) {
        return new BasicTalker(id, position, images.subList(0, 4), images.subList(4, 8), images.subList(8, 12), images.subList(12, 16), dialogue);
    }
    public int compareTo(Object other) {
        if (other instanceof Entity) {
            return getPosition().y - ((Entity)other).getPosition().y;
        }
        else {
            throw new IllegalArgumentException("Object of type (" + other.getClass() + ") not comparable to Entity");
        }
    }

    //Accessors
    public Class getTargetClass(){
        return Player.class;
    }
    public Optional<Entity> getTarget(World world) {
        return world.findNearest(getPosition(), getTargetClass());
    }

    //Move Functions
    public void Speak() {
        VirtualWorld.nextMenu = dialogue;
        VirtualWorld.gameState.addProgress(getId());
        VirtualWorld.gameState.getTrainer().addPokemon(Pokemon.newCinderpiller());
    }
    public Point get_final_destination(World world, EventScheduler scheduler) {
        Optional<Entity> Target = getTarget(world);
        if (Target.isPresent()) {
            Entity player = Target.get();
            Point playpos = player.getPosition();
            if (getPosition().adjacent(playpos)) {
                return playpos;
            }
            else {
                throw new Error("Cannot respond to non-adjacent target");
            }
        }
        else {
            throw new Error("Cannot respond to absent target");
        }
    }
    public Point getNextDestination(Point final_destination, World world) {
        return final_destination;
    }
    public void task(Point point, World world, EventScheduler scheduler) {
        VirtualWorld.lastLoc = world.getPlayer().getPosition();
        Speak();
    }
    public void else1(Point point, World world) {
        throw new Error("Next destination not equal to final");
    }
    public void else2(Point point, World world, EventScheduler scheduler) {
        turn_to(point);
        scheduleActions(world, scheduler);
    }
    public void else3() {
        throw new Error("Final same as current");
    }
    public void scheduleActions(World world, EventScheduler scheduler) {
        world.getPlayer().disableInput();
        animationpart(scheduler);
        activitypart(scheduler, world);
    }
    public void afterAll(World world, EventScheduler scheduler) {
    }
}
