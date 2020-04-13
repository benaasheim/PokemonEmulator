
/*
Cursor: Helps keep track of which button to highlight/moveto/click when dealing with menus
 */

public class Cursor {

    //Instance Variables
    private Point pos;

    //Class Methods
    public Cursor(Point pos){
        this.pos = pos;
    }
    public Cursor() {
        this.pos = new Point(0, 0);
    }

    //Accessors
    public Point getPos() {
        return pos;
    }

    //Mutators
    public void setPos(Point pos) {
        this.pos = pos;
    }
}
