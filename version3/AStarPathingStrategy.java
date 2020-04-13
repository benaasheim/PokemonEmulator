import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy implements PathingStrategy {

                           //1. Choose/know starting and ending points of the path
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {
        List<Point> path = new LinkedList<>();
        Map<Point, Point> Priors = new HashMap<>();
        Map<Point, Integer> Gvalues = new HashMap<>();
        Map<Point, Integer> Hvalues = new HashMap<>();
        Map<Point, Integer> Fvalues = new HashMap<>();

        Comparator<Point> pointComparator = Comparator.comparingInt(Fvalues::get);
        List<Point> OpenList = new LinkedList<>();
        List<Point> ClosedList = new LinkedList<>();
        Point cur;

        //2. Add start node to the open list and mark it as the current node
        cur = start;
        OpenList.add(cur);
        Gvalues.put(cur, 0);
        int h = calcH(cur, end);
        Hvalues.put(cur, h);
        Fvalues.put(cur, h);
        boolean failed = false;



        //6. go to step 3
        //7. repeat until a path to the end is found
        while (!withinReach.test(cur, end) || failed) {
            int cur_dist = Gvalues.get(cur);


            //3. Analyse all valid adjacent nodes that are not on the closed list
            List<Point> valid_adjacent_nodes = potentialNeighbors.apply(cur).filter(canPassThrough).filter(point -> !ClosedList.contains(point)).collect(Collectors.toList());





            Point finalCur = cur;
            valid_adjacent_nodes.stream()

                        .filter(point -> !OpenList.contains(point)).forEach(point -> analyze(point, end, finalCur, cur_dist, OpenList, Gvalues, Hvalues, Fvalues, Priors));




            //4. Move Current Node to the closed list
            ClosedList.add(cur);

            //5. Choose a node from the open list with the smallest f value and make it the current node
            Collections.sort(OpenList, pointComparator);
            if (OpenList.size() > 0) {
                cur = OpenList.remove(0);
            }
            else {
                System.out.println(":E ~~~~~~~~~~~~~\nOpenList: " + OpenList);
                System.out.println("ClosedList: " + ClosedList);
                return path;
            }
        }
        while (Priors.get(cur) != null) {
            path.add(cur);
            cur = Priors.get(cur);
        }
        Collections.reverse(path);
        return path;
    }

    private void analyze(Point point, Point endpoint, Point cur, int distA, List OpenList, Map<Point, Integer> Gvalues, Map<Point, Integer> Hvalues, Map<Point, Integer> Fvalues, Map<Point, Point> Priors) {
        //a. add to Open list if not already in it
        if (!OpenList.contains(point)) {
            OpenList.add(point);
            Gvalues.put(point, distA + 1);
            Hvalues.put(point, calcH(point, endpoint));
            Fvalues.put(point, calcH(point, endpoint) + distA + 1);
            Priors.put(point, cur);
        }
        //b. determine distance from start node
        int distance_from_start_node = distA + 1;

        //c. if calculated gvalue is better than previously calculated g value,
        if (Gvalues.get(point) > distance_from_start_node) {
            //replace the old g value with the new one and...
            Gvalues.remove(point);
            Gvalues.put(point, distance_from_start_node);
            //i. if necessary estimate distance of adjacent node to the end point
            int h = Hvalues.get(point);
            //ii. add g and h to get an f value
            int f = distance_from_start_node + h;
            Fvalues.remove(point);
            Fvalues.put(point, f);
            //iii. mark the adjacent node's prior vertex as the current node
            Priors.remove(point);
            Priors.put(point, cur);
        }
    }
    private int calcH(Point p1, Point p2) {
        return Math.abs(p1.y - p2.y) + Math.abs(p1.x - p2.x);
    }
}
