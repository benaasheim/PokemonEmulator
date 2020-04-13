import processing.core.PImage;
import java.util.List;

/*
Background: an image and location to display if no entity is present.
Doesn't obstruct entity movement
 */

class Background {

   //For Parsing
   public static final String KEY = "background";
   public static final int NUM_PROPERTIES = 4;

   //Assumed Class Universals
   public static final int defaultImageIndex = 0;

   //Instance Variables
   private String id;
   private List<PImage> images;

   //Class Methods
   public Background(String id, List<PImage> images) {
      this.id = id;
      this.images = images;
   }

   public static boolean parse(String [] properties, ImageStore imageStore, World world) {
      if (properties.length == NUM_PROPERTIES)
      {

         String id = properties[Functions.ID];
         Point pt = new Point(Integer.parseInt(properties[Functions.COL]),
                 Integer.parseInt(properties[Functions.ROW]));
         List<PImage> images = imageStore.getImageList(id);


         Background background = new Background(id, images);

         world.setBackground(pt,
                 background);
      }
      return properties.length == NUM_PROPERTIES;
   }

   //Accessors
   public List<PImage> getImages() {
      return images;
   }
   public String getId() {
      return id;
   }
   public PImage getCurrentImage() {
      return images.get(defaultImageIndex);
   }
}
