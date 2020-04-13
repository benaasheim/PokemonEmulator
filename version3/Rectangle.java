import processing.core.PApplet;

public class Rectangle extends Shape{

    private double width;
    private double height;
    private Point topleft;

    public Rectangle(double width, double height, Point topleft, int r, int g, int b) {
        super(r, g, b);
        this.width = width;
        this.height = height;
        this.topleft = topleft;
    }

    public Rectangle(double width, double height, Point topleft) {
        super(255, 255, 255);
        this.width = width;
        this.height = height;
        this.topleft = topleft;
    }

    protected double getArea(){
        return (this.height * this.width);
    }
    protected double getPerimeter(){
        return 2 * (this.height + this.width);
    }
    protected Point getAnchor() {
        return topleft;
    }
    protected void translate(Point point) {
        topleft = point;
    }
    protected void addTo(PApplet screen) {
        fillIn(screen);
        screen.rect(topleft.x, topleft.y, (float)width, (float)height);
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double w) {
        this.width = w;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double h) {
        this.height = h;
    }

    public Point getTopLeft() {
        return topleft;
    }


    public boolean equals(Object other){
        if (other instanceof Rectangle){
            return (this.width == ((Rectangle)other).getWidth()) && (this.height == ((Rectangle)other).getHeight()) && (this.topleft.x == ((Rectangle)other).getTopLeft().x) && (this.topleft.y == ((Rectangle)other).getTopLeft().y);
        }
        return false;
    }
}
