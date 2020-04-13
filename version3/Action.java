
/*
Action: ideally what our various entities might do in our virutal world
 */

public abstract class Action {

    //Instance Variables
    private AnimatedEntity entity;

    //Class Methods
    public Action(AnimatedEntity entity){
        this.entity = entity;
    }

    //Accessors
    public AnimatedEntity getEntity(){return entity;}

    //Others
    public abstract void executeAction(EventScheduler scheduler);
}
