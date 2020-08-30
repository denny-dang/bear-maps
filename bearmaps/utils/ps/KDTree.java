package bearmaps.utils.ps;
import java.util.List;

public class KDTree implements PointSet {
    KDTreeNode root;

    /* Constructs a KDTree using POINTS. You can assume POINTS contains at least one
       Point object. */
    public KDTree(List<Point> points) {
        root = null;
        for (Point p : points) {
            root = insert(root, p, true);
        }
    }

    /*

    You might find this insert helper method useful when constructing your KDTree!
    Think of what arguments you might want insert to take in. If you need
    inspiration, take a look at how we do BST insertion!
*/
    private KDTreeNode insert(KDTreeNode source, Point p, boolean b) {
        if (source == null) {
            KDTreeNode output = new KDTreeNode(p);
            if (b) {
                output.trait = "x";
            }
            else {
                output.trait = "y";
            }
            return output;
        }
        else if (compare(p, source.point(), b) >= 0) {
            source.right = insert(source.right(), p, !b);
            return source;
        }
        else {
            source.left = insert(source.left(), p, !b);
            return source;
        }
    }

    private int compare(Point p1, Point p2, boolean b) {
        if (b) {
            return Double.compare(p1.getX(), p2.getX());
        }
        else {
            return Double.compare(p1.getY(), p2.getY());
        }
    }



    /* Returns the closest Point to the inputted X and Y coordinates. This method
       should run in O(log N) time on average, where N is the number of POINTS. */
    public Point nearest(double x, double y) {
        KDTreeNode output = nearNode(root, new Point(x, y), root);
        return output.point();
    }

    public KDTreeNode nearNode(KDTreeNode n, Point goal, KDTreeNode best) {
        KDTreeNode goodSide;
        KDTreeNode badSide;
        if (n == null) {
            return best;
        }
        else if (Point.distance(n.point(), goal) < Point.distance(best.point(), goal)) {
            best = n;
        }
        if (decide(n, goal) >= 0) {
            goodSide = n.right();
            badSide = n.left();
        }
        else {
            goodSide = n.left();
            badSide = n.right();
        }
        best = nearNode(goodSide, goal, best);
        if (Point.distance(trial(n, goal), goal) < Point.distance(best.point(), goal)) {
            best = nearNode(badSide, goal, best);
        }
        return best;
    }

    public double decide(KDTreeNode pos, Point goal) {
        if (pos.trait.equals("x")) {
            return goal.getX() - pos.point.getX();
        }
        else {
            return goal.getY() - pos.point.getY();
        }
    }

    public Point trial(KDTreeNode pos, Point goal) {
        if (pos.trait.equals("x")) {
            return new Point(pos.point.getX(), goal.getY());
        }
        else {
            return new Point(goal.getX(), pos.point.getY());
        }
    }

    private class KDTreeNode {

        private Point point;
        private KDTreeNode left;
        private KDTreeNode right;
        private String trait;

        // If you want to add any more instance variables, put them here!

        KDTreeNode(Point p) {
            this.point = p;
            this.trait = null;
        }

        KDTreeNode(Point p, KDTreeNode left, KDTreeNode right) {
            this.point = p;
            this.left = left;
            this.right = right;
            this.trait = null;
        }

        Point point() {
            return point;
        }

        KDTreeNode left() {
            return left;
        }

        KDTreeNode right() {
            return right;
        }

        // If you want to add any more methods, put them here!

    }
}
