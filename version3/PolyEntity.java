import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;

public abstract class PolyEntity {
    private int width;
    private int height;
    private List<Entity> components;

    public PolyEntity(int width, int height) {
        this.width = width;
        this.height = height;
        this.components = new ArrayList<>();
    }

    public List<Entity> getComponents() {
        return components;
    }
    public Point getPosition() {
        return (components.get(0)).getPosition();
    }
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }


    public void addComponent(Entity obstacle) {
        components.add(obstacle);
    }

    public void assembleComponents(ImageStore imageStore, Point point, String oID, String image_key) {
        int image_key_inc = 0;

        for (int row = 0; row < getHeight(); row++)
        {
            for (int col = 0; col < getWidth(); col++)
            {
                image_key_inc++;




                String id = oID+"_"+col+","+row;
                Point pt = new Point(point.x + col, point.y + row);
                List<PImage> images = imageStore.getImageList(image_key+""+image_key_inc);

                Obstacle component = Obstacle.createObstacle(
                        id,
                        pt,
                        images);

                addComponent(component);
            }
        }
    }
}
