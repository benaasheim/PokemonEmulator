import processing.core.PApplet;

class Circle extends Shape{

    //Instance Variables
    private double radius;
    private Point center;

    //Class Methods
    public Circle(double radius, Point center, int r, int g, int b) {
        super(r, g, b);
        this.radius = radius;
        this.center = center;
    }
    public Circle(double radius, Point center) {
        super(255, 255, 255);
        this.radius = radius;
        this.center = center;
    }
    public boolean equals(Object other){
        if (other instanceof Circle){
            return (radius == ((Circle)other).getRadius()) && (center.x == ((Circle)other).getCenter().x) && (center.y == ((Circle)other).getCenter().y);
        }
        return false;
    }

    //Accessors
    protected double getArea() {
        return radius * radius * Math.PI;
    }
    protected double getPerimeter() {
        return 2 * Math.PI * radius;
    }
    protected Point getAnchor() {
        return center;
    }
    public double getRadius() {
        return radius;
    }
    public Point getCenter() {
        return center;
    }

    //Mutators
    protected void translate(Point point) {
        center = point;
    }
    protected void addTo(PApplet screen) {
        fillIn(screen);
        screen.circle(center.x, center.y, (float)radius);
    }
}
