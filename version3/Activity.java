
/*
Activity: an Action that affects another Entity
 */

final class Activity extends Action {

    //Instance Variables
    private World world;

    //Class Methods
    public Activity(ActiveEntity entity, World world) {
        super(entity);
        this.world = world;
    }
    public static Activity createActivityAction(ActiveEntity entity, World world) {
        return new Activity(entity, world);
    }

    //Others
    public void executeAction(EventScheduler scheduler) {
        ((ActiveEntity)getEntity()).executeActivity(scheduler, world);
    }
}
