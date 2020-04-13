
/*
Event: A time-identified occurence in our world
 */

final class Event {

   //Instance Variables
   private Action action;
   private long time;
   private Entity entity;

   //Class Methods
   public Event(Action action, long time, Entity entity) {
      this.action = action;
      this.time = time;
      this.entity = entity;
   }

   //Accessors
   public Action getAction() {
      return action;
   }
   public long getTime() {
      return time;
   }
   public Entity getEntity() {
      return entity;
   }
}
