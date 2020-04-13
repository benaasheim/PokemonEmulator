import processing.core.PApplet;
import java.util.List;

public class Button {

    //For Parsing
    public static final String KEY = "button";
    private static final int LABEL = 1;
    private static final int PCOL = 4;
    private static final int PROW = 5;
    private static final int WIDTH = 6;
    private static final int HEIGHT = 7;
    private static final int FUNC = 8;
    private static final int NUM_PROPERTIES = 9;

    //Universal Class Values
    private static final String textHighlightMark = "» ";

    //Instance Variables
    private Point topLeft;
    private String functionkey;
    private Point pos;
    private String label;
    private boolean ishighlighted;
    private int width;
    private int height;
    private List<Shape> frame;

    //Class Methods
    public Button(Point topLeft, int width, int height, String functionkey, Point pos, String label){
        this.topLeft = topLeft;
        this.functionkey = functionkey;
        this.pos = pos;
        this.width = width;
        this.height = height;
        this.label = label;
        this.ishighlighted = false;
        this.frame = List.of(new Rectangle(width, height, topLeft));
    }
    public static boolean parse(String [] properties, Menu menu) {
        if (properties.length == NUM_PROPERTIES)
        {
            Point TL = new Point(Integer.parseInt(properties[Functions.COL]),
                    Integer.parseInt(properties[Functions.ROW]));
            Point P = new Point(Integer.parseInt(properties[PCOL]),
                    Integer.parseInt(properties[PROW]));
            String funckey = properties[FUNC];
            String lable = properties[LABEL];
            Button button = new Button(TL, Integer.parseInt(properties[WIDTH]),
                    Integer.parseInt(properties[HEIGHT]),
                    funckey, P, lable);
            menu.tryAddButton(button);
        }
        return properties.length == NUM_PROPERTIES;
    }
    public static Button createButton(Point topLeft, int width, int height, String functionkey, Point pos, String label) {
        return new Button(topLeft, width, height, functionkey, pos, label);
    }

    //Accessors
    public String getLabel() {
        if (isIshighlighted()) {
            return textHighlightMark + label;
        }
        else return label;
    }
    public boolean isIshighlighted(){
        return ishighlighted;
    }
    public String getFunctionkey() {
        return functionkey;
    }
    public Point getPos() {
        return pos;
    }

    //Mutators
    public void Highlight() {
        ishighlighted = true;
    }
    public void Unhighlight() {
        ishighlighted = false;
    }

    //Adding to screen
    public void addTo(PApplet screen) {
        addFrameTo(screen);
        addTextTo(screen);
    }
    public void addFrameTo(PApplet screen) {
        frame.stream().forEach(shape -> shape.addTo(screen));
    }
    public void addTextTo(PApplet screen) {
        screen.fill(0);
        screen.text(getLabel(), topLeft.x, topLeft.y, topLeft.x+width, topLeft.y+height);
    }
}
