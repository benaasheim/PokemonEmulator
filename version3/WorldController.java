
/*
WorldController: Contains the specific emulated-"button"-functions from Controller if currently viewing the World
 */

public class WorldController extends Controller{
    public static final String emptyReturn = "";

    //Instance Variables
    private World world;
    private EventScheduler scheduler;
    private View view;

    //Class Methods
    public WorldController(World world, EventScheduler scheduler, View view) {
        this.world = world;
        this.scheduler = scheduler;
        this.view = view;
    }
    @Override
    protected void Arrowkey(char c, int dy, int dx) {
        world.getPlayer().scheduleActions(world, scheduler, c);
        if (view.sss(c)) {
            view.shiftView(dx, dy);
        }
    }
    @Override
    protected String start() {
        scheduler.pause();
        return "start";
    }
    @Override
    protected String aButton() {
        world.pressA(scheduler);
        return emptyReturn;
    }

    @Override
    protected String bButton() {
        return "";
    }

    @Override
    protected void heal() {
        VirtualWorld.gameState.heal(world.getPlayer().getPosition());
    }
}
