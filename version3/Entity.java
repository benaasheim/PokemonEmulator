import processing.core.PApplet;
import processing.core.PImage;
import java.util.List;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */

public abstract class Entity implements Comparable{

    //Instance Variables
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int imageAdjust;

    //Class Methods
    public Entity(String id, Point position, List<PImage> images, int imageAdjust){
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.imageAdjust = imageAdjust;
    }

    //Accessors
    public String getId() {
        return id;
    }
    public Point getPosition() {
        return position;
    }
    public List<PImage> getImages() {
        return images;
    }
    public int getImageIndex() {
        return imageIndex;
    }
    public PImage getCurrentImage() {
        return images.get(imageIndex);
    }
    public int getImageAdjust() { return imageAdjust; }

    //Mutators
    public void updatePosition(Point pos)
    {
        position = pos;
    }
    public void setImageIndex(int imageInd) {
        imageIndex = imageInd;
    }
    public void setImageAdjust(int I) {
        imageAdjust = I;
    }
    public void setImages(List<PImage> list) {
        images = list;
    }

    //Others
    public void draw(PApplet screen, int tileWidth, int tileHeight) {
        screen.image(getCurrentImage(),
                position.x * tileWidth,
                (position.y * tileHeight)-imageAdjust);
    }
    public void draw(PApplet screen, int tileWidth, int tileHeight, Viewport viewport) {
        Point viewPoint = viewport.worldToViewport(position.x, position.y);
        screen.image(getCurrentImage(),
                viewPoint.x * tileWidth,
                (viewPoint.y * tileHeight)-imageAdjust);
    }
}
