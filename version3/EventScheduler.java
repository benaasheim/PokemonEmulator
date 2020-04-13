import java.util.*;

final class EventScheduler
{

   //Universal Class Values
   private static final double timeScale = 1.0;

   //Instance Variables
   private PriorityQueue<Event> eventQueue;
   private Map<Entity, List<Event>> pendingEvents;
   private boolean paused;

   //Class Methods
   public EventScheduler() {
      this.eventQueue = new PriorityQueue<>(new EventComparator());
      this.pendingEvents = new HashMap<>();
      this.paused = false;
   }

   //Mutators
   public void pause(){
      paused = !paused;
   }
   public void updateOnTime(long time) {
      if (!paused){
         while (!eventQueue.isEmpty() && eventQueue.peek().getTime() < time)
         {
            Event next = eventQueue.poll();
            removePendingEvent(next);
            next.getAction().executeAction(this);
         }
      }
   }
   public void unscheduleAllEvents(Entity entity) {
      List<Event> pending = pendingEvents.remove(entity);
      if (pending != null)
      {
         for (Event event : pending)
         {
            eventQueue.remove(event);
         }
      }
   }
   private void removePendingEvent(Event event) {
      List<Event> pending = pendingEvents.get(event.getEntity());
      if (pending != null)
      {
         pending.remove(event);
      }
   }
   public void scheduleEvent(Entity entity, Action action, long afterPeriod) {
      long waittime = (long)(afterPeriod * timeScale);
      long time = System.currentTimeMillis() +waittime;
      Event event = new Event(action, time, entity);
      eventQueue.add(event);
      // update list of pending events for the given entity
      List<Event> pending = pendingEvents.getOrDefault(entity,
              new LinkedList<>());
      pending.add(event);
      pendingEvents.put(entity, pending);
   }
}
