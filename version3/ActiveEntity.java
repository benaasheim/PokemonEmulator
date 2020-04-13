import processing.core.PImage;
import java.util.List;

/*
ActiveEntity: An Entity Capable of acting on other Entities/Objects
 */

public abstract class ActiveEntity extends AnimatedEntity {

    //Instance Variables
    private int actionPeriod;

    //Class Methods
    public ActiveEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int imageAdjust, int repeatCount) {
        super(id, position, images, animationPeriod, imageAdjust, repeatCount);
        this.actionPeriod = actionPeriod;
    }

    //Accessors
    public int getActionPeriod() {
        return actionPeriod;
    }

    //Others
    protected abstract void executeActivity(EventScheduler scheduler, World world);
    public void RnU(EventScheduler scheduler, World world){
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }
    public void AtS(EventScheduler scheduler, World world) {
        world.addEntity(this);
        scheduleActions(world, scheduler);
    }
    protected abstract void scheduleActions(World world, EventScheduler scheduler);
    public void activitypart(EventScheduler scheduler, World world) {
        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world),
                actionPeriod);
    }
}
