import processing.core.PApplet;
import processing.core.PImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/*
ImageStore: to ideally keep track of the images used in our virtual world
 */

final class ImageStore {

    //Universal Class Values
    private static final int KEYED_RED_IDX = 2;
    private static final int KEYED_GREEN_IDX = 3;
    private static final int KEYED_BLUE_IDX = 4;
    private static final int COLOR_MASK = 0xffffff;
    private static final int KEYED_IMAGE_MIN = 5;
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private static final int DEFAULT_IMAGE_COLOR = 0x808080;

    //Instance Variables
    private Map<String, List<PImage>> images;
    private List<PImage> defaultImages;

    //Class Methods
   public ImageStore(int width, int height)
   {
      this.images = new HashMap<>();
      defaultImages = new LinkedList<>();
      defaultImages.add(createImageColored(width, height, DEFAULT_IMAGE_COLOR));
   }
   private static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, VirtualWorld.RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++)
        {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
   }

    private void loadImages(Scanner in, PApplet screen) {
       int lineNumber = 0;
       while (in.hasNextLine())
       {
          try
          {
//              System.out.println("LOADING IMAGES FILE LINE # " + lineNumber);
             this.processImageLine(in.nextLine(), screen);
          }
          catch (NumberFormatException e)
          {
             System.out.println(String.format("Image format error on line %d",
                lineNumber));
          }
          lineNumber++;
       }
    }
    private void processImageLine(String line, PApplet screen) {
       String[] attrs = line.split("\\s");
       if (attrs.length >= 2)
       {
          String key = attrs[0];
          PImage img = screen.loadImage(attrs[1]);
          if (img != null && img.width != -1)
          {
             List<PImage> imgs = this.getImages(key);
             imgs.add(img);

             if (attrs.length >= KEYED_IMAGE_MIN)
             {
                int r = Integer.parseInt(attrs[getIDX(1)]);
                int g = Integer.parseInt(attrs[getIDX(2)]);
                int b = Integer.parseInt(attrs[getIDX(3)]);
                setAlpha(img, screen.color(r, g, b), 0);
             }
          }
       }
    }
    public static int getIDX(int i){
        switch (i){
            case 1:
                return KEYED_RED_IDX;
            case 2:
                return KEYED_GREEN_IDX;
            case 3:
                return KEYED_BLUE_IDX;
            default:
                return 0;
        }
    }
    private List<PImage> getImages(String key) {
       List<PImage> imgs = this.images.get(key);
       if (imgs == null)
       {
          imgs = new LinkedList<>();
          this.images.put(key, imgs);
       }
       return imgs;
    }
    /*
          Called with color for which alpha should be set and alpha value.
          setAlpha(img, color(255, 255, 255), 0));
        */
    private void setAlpha(PImage img, int maskColor, int alpha) {
       int alphaValue = alpha << 24;
       int nonAlpha = maskColor & COLOR_MASK;
       img.format = PApplet.ARGB;
       img.loadPixels();
       for (int i = 0; i < img.pixels.length; i++)
       {
          if ((img.pixels[i] & COLOR_MASK) == nonAlpha)
          {
             img.pixels[i] = alphaValue | nonAlpha;
          }
       }
       img.updatePixels();
    }
    public void loadImages(String filename, PApplet screen) {
        try
        {
//            System.out.println("LOADING IMAGES FROM FILE: " + filename + " **(in imagestore)**");

            Scanner in = new Scanner(new File(filename));
            this.loadImages(in, screen);
//            System.out.println("IMAGES SUCCESSFULLY LOADED FROM FILE: " + filename + " **(in imagestore)**");

        }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
    }
    public Background createDefaultBackground() {
        return new Background(DEFAULT_IMAGE_NAME,
                getImageList(DEFAULT_IMAGE_NAME));
    }
    public List<PImage> getImageList(String key)
    {
        return getOrDefault(key, defaultImages);
    }
    public List<PImage> getOrDefault(String key, List<PImage> pImages) {
        List<PImage> pImage = images.get(key);
        if (pImage == null) {
            System.out.println("Key not found: " + key);
            return pImages;
        }
        else {
            return pImage;
        }
    }
    private Map<String, List<PImage>> getImages() {
        return images;
    }
    private List<PImage> getDefaultImages() {
        return defaultImages;
    }
}
