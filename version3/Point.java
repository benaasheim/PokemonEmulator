import java.util.function.Function;

final class Point {
   //Universal Class Values
   private static final char u = 'u', d = 'd', l = 'l', r = 'r';

   //Instance Variables
   public final int x;
   public final int y;

   public Point(int x, int y)
   {
      this.x = x;
      this.y = y;
   }

   public boolean adjacent(Point p1) {
       return (p1.x == x && Math.abs(p1.y - y) == 1) ||
          (p1.y == y && Math.abs(p1.x - x) == 1);
    }
   public int distanceSquared(Point p1) {
      return (p1.x - x) * (p1.x - x) + (p1.y - y) * (p1.y - y);
   }
   public double euclideanDistanceTo(Point point) {
      return Math.sqrt((double)distanceSquared(point));
   }
   public String toString()
   {
      return "(" + x + "," + y + ")";
   }
   public static Point fromString(String string) {
      if (string.equals("null")) {
         return null;
      }
      else {
         String s = (String)string.subSequence(1, string.length()-1);
         String[] newstring = s.split(",");
         return new Point(Integer.parseInt(newstring[0]),  Integer.parseInt(newstring[1]));
      }
   }
   public boolean equals(Object other) {
      return other instanceof Point &&
         ((Point)other).x == x &&
         ((Point)other).y == y;
   }
   public int hashCode() {
      int result = 17;
      result = result * 31 + x;
      result = result * 31 + y;
      return result;
   }
   public Point indir(char c) {
      switch (c) {
         case u: return above();
         case d: return below();
         case l: return toleft();
         case r: return toright();
         default: return this;
      }
   }
   public char fromdir(Point point) {
      if (point.equals(above())) {
         return u;
      }
      if (point.equals(below())) {
         return d;
      }
      if (point.equals(toleft())) {
         return l;
      }
      if (point.equals(toright())) {
         return r;
      }
      return ' ';
   }
   public Point above() {
      return new Point(x, y - 1);
   }
   public Point below() {
      return new Point(x, y + 1);
   }
   public Point toright() {
      return new Point(x+1, y);
   }
   public Point toleft() {
      return new Point(x-1, y);
   }
   public Point translateBy(Point point) {
      return new Point(x + point.x, y + point.y);
   }
   public Point otherSideOf(Point point) {
      int dx = point.x - x;
      int dy = point.y - y;
      return new Point(x-dx, y-dy);
   }
}
