import processing.core.PApplet;

public abstract class Shape {
    private int red;
    private int green;
    private int blue;

    public Shape(int r, int g, int b) {
        this.red = r;
        this.green = g;
        this.blue = b;
    }

    //Accessors
    public int red() {
        return red;
    }
    public int green() {
        return green;
    }
    public int blue() {
        return blue;
    }

    protected abstract double getArea();
    protected abstract double getPerimeter();
    protected abstract Point getAnchor();
    protected abstract void translate(Point point);
    protected abstract void addTo(PApplet screen);
    public void fillIn(PApplet screen) {
        screen.fill(red, green, blue);
    }
}
