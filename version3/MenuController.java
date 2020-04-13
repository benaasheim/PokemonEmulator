
/*
MenuController: Contains the specific emulated-"button"-functions from Controller if currently viewing a menu
 */

public class MenuController extends Controller{

    //Instance Variables
    private Menu menu;

    //Class Methods
    public MenuController(Menu menu) {
        this.menu = menu;
    }

    @Override
    protected void Arrowkey(char c, int dy, int dx) {
        menu.moveCursor(c);
    }
    @Override
    protected String start() {
        return "";
    }
    @Override
    protected String aButton() {
        return menu.pushButton();
    }

    @Override
    protected String bButton() {
        return menu.exit();
    }

    @Override
    protected void heal() {
    }
}
