import processing.core.PImage;
import java.util.List;

public abstract class AnimatedEntity extends Entity{

    //Instance Variables
    private int animationPeriod;
    private int repeatCount;

    //Class Methods
    public AnimatedEntity(String id, Point position, List<PImage> images, int animationPeriod, int imageAdjust, int repeatCount) {
        super(id, position, images, imageAdjust);
        this.animationPeriod = animationPeriod;
        this.repeatCount = repeatCount;
    }

    //Accessors
    public int getAnimationPeriod() { return animationPeriod; }

    //Mutators
    public void nextImage() {
        int nextInd = getImageIndex() + 1;
        if (nextInd == getImages().size()) {
            nextInd = 0;
        }
        setImageIndex(nextInd);
    }

    //Others
    protected abstract void scheduleActions(World world, EventScheduler scheduler);
    public void animationpart(EventScheduler scheduler) {
        scheduler.scheduleEvent(this,
                Animation.createAnimationAction(this, repeatCount),
                animationPeriod);
    }
}
