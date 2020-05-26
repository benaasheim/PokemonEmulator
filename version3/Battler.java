import processing.core.PImage;
import java.util.*;

/*
Battler: A Boundary Guardian who challenges the player to a battle if they have not been beaten already.
 */

public class Battler extends BoundaryGuardian {

    //For Parsing
    public static final String KEY = "battler";
    private static final int POINT1 = 4;
    private static final int POINT2 = 5;
    private static final int POINT3 = 6;
    private static final int DIALOGUE = 7;
    private static final int DELETE_KEY = 8;
    private static final int IMAGE_KEY = 9;
    private static final int POKEMON_1_NAME = 10;
    private static final int POKEMON_2_NAME = 11;
    private static final int DEFEATED_DIALOGUE = 12;
    private static final int NUM_PROPERTIES = 13;

    //Instance Variables
    private Trainer trainer;

    //Class Methods
    public Battler(String id, Point position, List<PImage> u, List<PImage> d, List<PImage> l, List<PImage> r, List<Point> boundary, String dialogue, Trainer trainer) {
        super(id, position, u, d, l, r, boundary, dialogue);
        this.trainer = trainer;
    }

    public static boolean parse(String[] properties, ImageStore imageStore, World world) {
        if (NUM_PROPERTIES == properties.length) {

            String id = properties[Functions.ID];
            Point pt = new Point(Integer.parseInt(properties[Functions.COL]),
                    Integer.parseInt(properties[Functions.ROW]));
            List<PImage> images = imageStore.getImageList(properties[IMAGE_KEY]);


            String dialogue = properties[DIALOGUE];
            Point pt1 = Point.fromString(properties[POINT1]);
            Point pt2 = Point.fromString(properties[POINT2]);
            Point pt3 = Point.fromString(properties[POINT3]);
            List<Point> boundary = new ArrayList<>();
            boundary.add(pt1);
            boundary.add(pt2);
            boundary.add(pt3);
            Pokemon pokemon1 = Pokemon.pokemonfromname(properties[POKEMON_1_NAME]);
            Pokemon pokemon2 = Pokemon.pokemonfromname(properties[POKEMON_2_NAME]);

            Trainer trainer = new Trainer(id);
            trainer.addPokemon(pokemon1);
            trainer.addPokemon(pokemon2);
            String defeated_dialogue = properties[DEFEATED_DIALOGUE];
            System.out.println(trainer.getRoster());
            Battler entity = Battler.createBattler(id,
                    pt,
                    images,
                    boundary,
                    dialogue,
                    trainer);

            if (!(VirtualWorld.gameState.hasprogress(properties[DELETE_KEY]))) {
                System.out.println("HERETOO " + properties[DELETE_KEY]);
                world.tryAddEntity(entity);
            }
            else {
                // if Battler has been defeated replaced with stationary Basic Talker with different dialogue
                BasicTalker talker = BasicTalker.createBasicTalker(id, pt, images, defeated_dialogue);
                world.tryAddEntity(talker);
            }
        }

        return NUM_PROPERTIES == properties.length;
    }

    public static Battler createBattler(String id, Point position, List<PImage> images, List<Point> boundary, String dialogue, Trainer trainer) {
        return new Battler(id, position, images.subList(0, 4), images.subList(4, 8), images.subList(8, 12), images.subList(12, 16), boundary, dialogue, trainer);
    }

    public int compareTo(Object other) {
        if (other instanceof Entity) {
            return this.getPosition().y - ((Entity) other).getPosition().y;
        } else {
            throw new IllegalArgumentException("Object of type (" + other.getClass() + ") not comparable to Entity");
        }
    }


    //Move Functions
    public void perfom_task(Point point, World world, EventScheduler scheduler) {
        FlipPlayer(point, world);
        world.getPlayer().saveLast();
        Speak();
    }
    public void perfom_task_2(World world, EventScheduler scheduler) {
        world.getPlayer().saveLast();
        scheduler.pause();
        world.getPlayer().disableInput();
        VirtualWorld.setTrainer(trainer);
        walkingback =! walkingback;
    }

    public void task(Point point, World world, EventScheduler scheduler) {
        if (!walkingback) {
            perfom_task(point, world, scheduler);
            perfom_task_2(world, scheduler);
        }
        else {
            world.moveEntity(this, point);
            world.getPlayer().enableInput();
            walkingback =! walkingback;
        }
    }
}
