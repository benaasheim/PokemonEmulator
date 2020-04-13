import processing.core.PImage;
import java.util.List;

/*
Atlantis: Used to Transport the Player to a different World
 */

final class Atlantis extends AtlanticQuake {

    //For Parsing
    public static final String KEY = "atlantis";
    private static final int COL = 2;
    private static final int ROW = 3;
    private static final int EXIT_CODE = 4;
    private static final int ENTRY_POINT = 5;
    public static final int NUM_PROPERTIES = 6;

    //Assumed Universal Class values
    private static final int ACTION_PERIOD = 350;
    private static final int ANIMATION_PERIOD = 10;
    private static final int ANIMATION_REPEAT_COUNT = 6;
    private static final int imageAdjust = 8;

    //Instance Variables
    private Point point; //The entryPoint in the next World
    private String exitcode; //The key of the next World in WorldStore

    //Class Methods
    public Atlantis(String id, Point position, List<PImage> images, String exitcode, Point point) {
        super(id, position, images, ACTION_PERIOD, ANIMATION_PERIOD, imageAdjust, ANIMATION_REPEAT_COUNT);
        this.exitcode = exitcode;
        this.point = point;
    }

    protected static boolean parse(String [] properties, ImageStore imageStore, World world) {
        if (properties.length == NUM_PROPERTIES)
        {


            String id = properties[Functions.ID];
            Point pt = new Point(Integer.parseInt(properties[COL]),
                    Integer.parseInt(properties[ROW]));
            List<PImage> images = imageStore.getImageList(KEY);

            String exitcode = properties[EXIT_CODE];
            Point exitpoint = Point.fromString(properties[ENTRY_POINT]);


            Atlantis entity = Atlantis.createAtlantis(id,
                    pt,
                    images,
                    exitcode,
                    exitpoint);

            world.tryAddEntity(entity);


        }
        return properties.length == NUM_PROPERTIES;
    }
    public static Atlantis createAtlantis(String id, Point position, List<PImage> images, String exitcode, Point point1) {
        return new Atlantis(id, position, images, exitcode, point1);
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
    public String getExitcode(){ return exitcode; }

    //Others
    @Override
    protected void ifAtlantis(World world) {
        System.out.println(getExitcode());
        VirtualWorld.lastLoc = point;
        VirtualWorld.nextWorld = getExitcode();
    }
}
