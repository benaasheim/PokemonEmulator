
/*
* Controller: Takes the keystroke from VirtualWorld's keyPressed()
* after it is translated by Options
* and executes the appropriate function call as it corresponds to the button on the gameboy(advance) original console
* Subclasses handle the difference in-menu vs in-world
*/

public abstract class Controller {
    public static final char upChar = 'u', downChar = 'd', leftChar = 'l', rightChar = 'r';
    public static final String UP = "UP", DOWN = "DOWN", LEFT = "LEFT", RIGHT = "RIGHT", START = "START", HEAL = "HEAL", ABUTTON = "ABUTTON", BBUTTON = "BBUTTON";

    protected abstract void Arrowkey(char c, int dy, int dx);
    protected abstract String start();
    protected abstract void heal();
    protected abstract String aButton();
    protected abstract String bButton();

    public String executeCommandSpec(String keyCode) {
        String retstr = "";
        switch (keyCode)
        {
            case UP: Arrowkey(upChar, -1, 0);break;
            case DOWN: Arrowkey(downChar, 1, 0);break;
            case LEFT: Arrowkey(leftChar, 0, -1);break;
            case RIGHT: Arrowkey(rightChar, 0, 1);break;
            case START: retstr = start();
            case HEAL: heal();break;
            case ABUTTON: retstr = aButton();break;
            case BBUTTON: retstr = bButton();break;
        }
        return retstr;
    }
}
