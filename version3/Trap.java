import processing.core.PImage;
import java.util.List;

/*
Trap: a background object that can trigger
 */

public class Trap extends Background {
    private static final int NUM_PROPERTIES = 4;
    private static final int OVERLAY_IND = 1;

    public static final String KEY = "trap";

    public Trap(String id, List<PImage> images) {
        super(id, images);
    }
    public PImage getOverlay() { return getImages().get(OVERLAY_IND); }

    public static boolean parse(String [] properties, ImageStore imageStore, World world) {
        if (properties.length == NUM_PROPERTIES)
        {

            String id = properties[Functions.ID];
            Point pt = new Point(Integer.parseInt(properties[Functions.COL]),
                    Integer.parseInt(properties[Functions.ROW]));
            List<PImage> images = imageStore.getImageList(id);

            Trap trap = new Trap(id, images);

            world.setBackground(pt, trap);
        }
        return properties.length == NUM_PROPERTIES;
    }
}
