import processing.core.PApplet;

public class Triangle extends Shape{

    //Instance Variables
    private Point a;
    private Point b;
    private Point c;


    public Triangle(Point a, Point b, Point c, int r, int g, int B) {
        super(r, g, B);
        this.a = a;
        this.b = b;
        this.c = c;
    }
    public Triangle(Point a, Point b, Point c) {
        super(255, 255, 255);
        this.a = a;
        this.b = b;
        this.c = c;
    }



    protected double getArea(){
        double z = 2;
        return (((double)a.x * (double)b.y + (double)b.x * (double)c.y + (double)c.x * (double)a.y) - ((double)a.y * (double)b.x + (double)b.y * (double)c.x + (double)c.y * (double)a.x)) / z;
    }
    protected double getPerimeter(){
        return a.euclideanDistanceTo(b) + b.euclideanDistanceTo(c) + a.euclideanDistanceTo(c);
    }
    protected Point getAnchor() {
        return a;
    }
    public void translate(Point p){
        int shiftx = p.x - a.x;
        int shifty = p.y - a.y;
        Point pb = new Point(b.x + shiftx, b.y + shifty);
        Point pc = new Point(c.x + shiftx, c.y + shifty);
        this.a = p;
        this.b = pb;
        this.c = pc;
    }
    protected void addTo(PApplet screen) {
        fillIn(screen);
        screen.triangle(a.x, a.y, b.x, b.y, c.x, c.y);
    }

    public Point getVertexA(){
        return a;
    };

    public Point getVertexB() {
        return b;
    }

    public Point getVertexC() {
        return c;
    }

    public boolean equals(Object other){
        if (other instanceof Triangle){
            return a.equals(((Triangle)other).getVertexA()) &&
                    b.equals(((Triangle)other).getVertexB()) &&
                    c.equals(((Triangle)other).getVertexC());
        }
        return false;
    }
}
