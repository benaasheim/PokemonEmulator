import processing.core.PImage;
import java.util.List;

class Quake extends AtlanticQuake
{
    public static final String KEY = "quake";
    public static final int imageAdjust = 0;

    public Quake(String id, Point position,
                 List<PImage> images,
                 int actionPeriod, int animationPeriod, int repeatCount)
    {
        super(id, position, images, actionPeriod, animationPeriod, imageAdjust, repeatCount);
    }

    protected void ifAtlantis(World world) {
    }

    public int compareTo(Object other) {
        if (other instanceof Entity) {
            return getPosition().y - ((Entity)other).getPosition().y;
        }
        else {
            throw new IllegalArgumentException("Object of type (" + other.getClass() + ") not comparable to Entity");
        }
    }
}
