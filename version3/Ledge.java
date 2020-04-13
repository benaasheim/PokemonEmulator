import processing.core.PImage;

import java.util.List;

public class Ledge extends ActiveEntity {
    public static final String KEY = "ledge";
    public static final int NUM_PROPERTIES = 5;
    public static final int COL = 2;
    public static final int ROW = 3;
    public static final int ACTION_PERIOD = 50;
    public static final int ANIMATION_PERIOD = 1;
    public static final int ANIMATION_REPEAT_COUNT = 6;
    public static final int imageAdjust = 0;
    private char dir;

    public Ledge(String id, Point position,
                    List<PImage> images,
                    int actionPeriod, int animationPeriod, char dir)
    {
        super(id, position, images, actionPeriod, animationPeriod, imageAdjust, animationPeriod);
        this.dir = dir;
    }
    public static Ledge createLedge(String id, Point position, List<PImage> images, char exitcode) {
        return new Ledge(id, position, images, ACTION_PERIOD, ANIMATION_PERIOD, exitcode);
    }

    protected int getFRC() {
        return ANIMATION_REPEAT_COUNT;
    }


    public char getDir(){ return this.dir; }

    protected void executeActivity(EventScheduler scheduler, World world) {
//        model.removeEntity(((WorldModel)model).getPlayer());
//        LQuake quake = LQuake.createLQuake(this.ONE(), imageStore.getImageList(this.dirToImages()), ((WorldModel)model).getPlayer());
    }

    @Override
    protected void scheduleActions(World world, EventScheduler scheduler) {

    }

    public String dirToImages() {
        return "HEY";
    }
    public Point ONE() {
        switch (this.dir) {
            case 'l':
            case 'r':
                return this.getPosition().indir('l');
            case 'd':
                return this.getPosition().indir('d');
            default:
                throw new IllegalArgumentException();
        }
    }
    public int compareTo(Object other) {
        if (other instanceof Entity) {
            return this.getPosition().y - ((Entity)other).getPosition().y;
        }
        else {
            throw new IllegalArgumentException("Object of type (" + other.getClass() + ") not comparable to Entity");
        }
    }
    protected static boolean parse(String [] properties, ImageStore imageStore, World world) {
        if (properties.length == NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[COL]),
                    Integer.parseInt(properties[ROW]));
            Ledge entity = Ledge.createLedge(properties[Functions.ID],
                    pt, imageStore.getImageList(KEY),
                    properties[NUM_PROPERTIES-1].charAt(0));
            world.tryAddEntity(entity);
        }

        return properties.length == NUM_PROPERTIES;
    }

}
