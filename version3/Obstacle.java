import java.util.List;
import processing.core.PImage;


/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */


class Obstacle extends Entity {

    public static final String KEY = "obstacle";
    public static final int NUM_PROPERTIES = 4;


    //Universal Class Values

    public Obstacle(String id, Point position,
                    List<PImage> images,
                    int actionPeriod, int animationPeriod)
    {
        super(id, position, images, 0);

    }


    public static Obstacle createObstacle(String id, Point position, List<PImage> images)
    {
        return new Obstacle(id, position, images, 0, 0);
    }


    public int compareTo(Object other) {
        if (other instanceof Entity) {
            return this.getPosition().y - ((Entity)other).getPosition().y;
        }
        else {
            throw new IllegalArgumentException("Object of type (" + other.getClass() + ") not comparable to Entity");
        }
    }
    public static boolean parse(String [] properties, ImageStore imageStore, World world) {
        if (properties.length == NUM_PROPERTIES)
        {
            Point pt = new Point(
                    Integer.parseInt(properties[Functions.COL]),
                    Integer.parseInt(properties[Functions.ROW]));
            Obstacle entity = Obstacle.createObstacle(properties[Functions.ID],
                    pt, imageStore.getImageList(KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == NUM_PROPERTIES;
    }


}
