
/*
Animation: a change/update in/to the image displayed by an Entity
 */

final class Animation extends Action {

    //Instance Variables
    private int repeatCount; //The number of changes (remaining) to be enacted

    //Class Methods
    public Animation(AnimatedEntity entity, int repeatCount) {
        super(entity);
        this.repeatCount = repeatCount;
    }
    public static Animation createAnimationAction(AnimatedEntity entity, int repeatCount) {
        return new Animation(entity, repeatCount);
    }


    public void executeAction(EventScheduler scheduler) {
        getEntity().nextImage();
        int nextRC = Math.max((repeatCount - 1), 0);
        if (repeatCount != 1)
        {
            scheduler.scheduleEvent(getEntity(),
                    Animation.createAnimationAction(getEntity(),
                            nextRC),
                    getEntity().getAnimationPeriod());
        }
    }
}
