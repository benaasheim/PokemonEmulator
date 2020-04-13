import processing.core.PImage;
import java.util.List;

/*
AtlanticQuake: A superclass for two entities that remove themselves after performing their action
 */

public abstract class AtlanticQuake extends ActiveEntity {

    //Class Methods
    public AtlanticQuake(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int imageAjust, int repeatCount) {
        super(id, position, images, actionPeriod, animationPeriod, imageAjust, repeatCount);
    }


    protected void scheduleActions(World world, EventScheduler scheduler) {
        animationpart(scheduler);
        activitypart(scheduler, world);
    }
    protected void executeActivity(EventScheduler scheduler, World world) {
        RnU(scheduler, world);
        ifAtlantis(world);
    }
    protected abstract void ifAtlantis(World world);
}
