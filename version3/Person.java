import processing.core.PImage;
import java.util.List;

public abstract class Person extends ActiveEntity{

    //Assumed Universal Class values
    public static final int ANIMATION_PERIOD = 100;
    public static final int ANIMATION_REPEAT_COUNT = 2;
    public static final int ACTION_PERIOD = 200;
    public static final int IMAGE_ADJUST = 10;

    //Instance Variables
    private List<PImage> u, d, l, r;
    private char dir;

    //Class Methods
    public Person(String id, Point position,
                  List<PImage> images, List<PImage> u, List<PImage> d, List<PImage> l, List<PImage> r) {
        super(id, position, images, ACTION_PERIOD, ANIMATION_PERIOD, IMAGE_ADJUST, ANIMATION_REPEAT_COUNT);
        this.u = u;
        this.d = d;
        this.l = l;
        this.r = r;
        this.dir = 'd';
        turncheck();
    }

    public char getDir() {
        return this.dir;
    }

    public void setDir(char n) {
        this.dir = n;
    }

    public void turncheck() {
        List<List<PImage>> lists = List.of(super.getImages(), u, d, l, r);
        for (List<PImage> list : lists) {
            if (list.size() != 4) {
                throw new Error("! " + lists.indexOf(list) + ", is len" + list.size() );
            }
        }
    }

    public void turn(char dir){
        setDir(dir);
        List<PImage> imageList;
        switch (dir) {
            case 'd': imageList = this.d;
            break;
            case 'u': imageList = this.u;
            break;
            case 'l': imageList = this.l;
            break;
            case 'r': imageList = this.r;
            break;
            default:
                imageList = this.getImages();
        }
        this.setImages(imageList);
    }
    public void turn_to(Point point) {

        if (getPosition().adjacent(point)) {
            char dd = getPosition().fromdir(point);
            turn(dd);
        }
        else {
            throw new Error("Cannot turn to non-adjacent");
        }
    }


    public void executeActivity(EventScheduler scheduler, World world) {
        Point final_destination = get_final_destination(world, scheduler);
        if (final_destination != getPosition()) {
            Point next_destination = getNextDestination(final_destination, world);
            if (facing(next_destination)) {
                if (next_destination.equals(final_destination)) {
                    task(next_destination, world, scheduler);
                }
                else {
                    else1(next_destination, world);
                }
            }
            else {
                else2(next_destination, world, scheduler);
            }
        }
        else {
            else3();
        }
        afterAll(world, scheduler);
    }
    public abstract Point get_final_destination(World world, EventScheduler scheduler);
    public abstract Point getNextDestination(Point final_destination, World world);
    public boolean facing(Point point) {
        return getPosition().indir(getDir()).equals(point);
    }
    public abstract void task(Point point, World world, EventScheduler scheduler);
    public abstract void else1(Point point, World world);
    public abstract void else2(Point point, World world, EventScheduler scheduler);
    public abstract void else3();
    public abstract void afterAll(World world, EventScheduler scheduler);
}
