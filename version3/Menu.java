import processing.core.PApplet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;


public class Menu implements Loadable{

    //Instance Variables
    private int numBRows;
    private int numBCols;
    private Button[][] buttons;
    private List<Button> buttonList;
    private Cursor cursor;
    private List<Shape> frame;
    private Point position;
    private String exit;

    //Class Methods
    public Menu(Point point, int width, int height, int numBRows, int numBCols, Cursor cursor, String exit) {
        this.position = point;
        this.numBRows = numBRows;
        this.numBCols = numBCols;
        this.buttons = new Button[numBCols][numBRows];
        this.buttonList = new ArrayList<>();
        this.frame = List.of(new Rectangle(width, height, point));
        this.cursor = cursor;
        this.exit = exit;
    }
    public boolean processLine(String line, ImageStore imageStore) {
        try {
            String[] properties = line.split("\\s");
            if (properties.length > 0) {
                switch (properties[Functions.PROPERTY_KEY]) {
                    case Button.KEY:
                        return Button.parse(properties, this);
                }
            }
            return false;
        }
        catch (Exception e) {
            throw new LineProcessingError(e, line);
        }
    }

    //Accessors
    public Cursor getCursor() {
        System.out.println(cursor.getPos());
        return cursor;
    }
    public Button getButton(Point pos){
        return buttons[pos.x][pos.y];
    }
    public List<Button> getButtonList() {
        return buttonList;
    }
    public String exit() {
        return exit;
    }

    //Mutators
    public boolean moveCursor(char d){
        BiPredicate<Point, Menu> predicate;
        BiPredicate<Point, Menu> predicateb = (newpos, menuModel) -> (getButton(newpos) != null);
        if (d == 'u') {
            d = 'd';
        }
        else if (d == 'd') {
            d = 'u';
        }
        switch (d) {
            case 'd': predicate = (newpos, menuModel) -> (newpos.y < numBRows) && (getButton(newpos) != null);break;
            case 'u': predicate = (newpos, menuModel) -> (newpos.y >= 0) && (getButton(newpos) != null);break;
            case 'l': predicate = (newpos, menuModel) -> (newpos.x >= 0) && (getButton(newpos) != null);break;
            case 'r': predicate = (newpos, menuModel) -> (newpos.x < numBCols) && (getButton(newpos) != null);break;
            default: predicate = predicateb;
        }
        return moveCursorTo(d, predicate);
    }
    public boolean moveCursorTo(char d, BiPredicate<Point, Menu> horizoncheck){
        getButton(cursor.getPos()).Unhighlight();
        Point newpos = cursor.getPos().indir(d);
        if (horizoncheck.test(newpos, this)){
            cursor.setPos(newpos);
            getButton(cursor.getPos()).Highlight();
            return true;
        }
        getButton(cursor.getPos()).Highlight();
        return false;
    }
    public void tryAddButton(Button button){
        if (getButton(button.getPos()) == null){
            addButton(button);
            System.out.println("ADDED " + button.getFunctionkey());
        }
        else
        {
            throw new IllegalArgumentException("position occupied by " + getButton(button.getPos()).getFunctionkey() + " cannot add, " + button.getFunctionkey());
        }
    }
    public void addButton(Button button){
        buttonList.add(button);
        buttons[button.getPos().x][button.getPos().y] = button;
    }

    //Other
    public String pushButton(){
        Button button = getButton(cursor.getPos());
        if (button != null){
            return button.getFunctionkey();
        }
        else return "";
    }
    public void addTo(PApplet screen) {
        addFrameTo(screen);
        buttonList.stream().forEach(button -> button.addTo(screen));
    }
    public void addFrameTo(PApplet screen) {
        frame.stream().forEach(shape -> shape.addTo(screen));
    }
}
