package libraries.geometry;

import libraries.other.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class NewGeometry {
    private static boolean testMode = false;

    public static final float INACCURACY = 0.1f;
    public static final float INACCURACY2 = INACCURACY / 10;

    public interface Point{
        float getX();
        float getY();

        void setX(float v);
        void setY(float v);

        void addX(float v);
        void addY(float v);

        boolean equals(Point p);

        Point copy();
    }
    public static class GlobalPoint implements Point{
        public float x, y;

        public GlobalPoint(){
            x = 0;
            y = 0;
        }
        public GlobalPoint(Point p) {
            this(p.getX(), p.getY());
        }
        public GlobalPoint(int x, int y){
            this.x = x;
            this.y = y;
        }
        public GlobalPoint(float x, float y){
            this.x = x;
            this.y = y;
        }

        public float getX(){
            return x;
        }
        public float getY(){
            return y;
        }

        public void setX(float v){
            x = v;
        }
        public void setY(float v){
            y = v;
        }

        public void addX(float v) {
            x += v;
        }
        public void addY(float v) {
            y += v;
        }

        public boolean equals(Point point){
            return getX() == point.getX() && getY() == point.getY();
        }

        public GlobalPoint copy(){
            return new GlobalPoint(x, y);
        }

        void print() {
            System.out.println(getX() + " "  + getY());
        }

        public String toString() {
            return getX() + " " + getY();
        }
    }
    public static class AnchorPoint extends GlobalPoint{
        private Point zero;

        public AnchorPoint(Point zeroCoordinate, float x, float y){
            super(x, y);
            zero = zeroCoordinate;
        }
        public AnchorPoint(float x, float y){
            super(x, y);
            zero = new GlobalPoint(0, 0);
        }
        public AnchorPoint(Point p){
            this(p.getX(), p.getY());
        }

        // return coordinate in the global space
        @Override
        public float getX(){
            return zero.getX() + x;
        }
        @Override
        public float getY(){
            return zero.getY() + y;
        }

        @Override
        public void setX(float v){
            x = v - zero.getX();
        }
        @Override
        public void setY(float v){
            y = v - zero.getY();
        }

        public void tie(Point p) {
            float oldX = getX(), oldY = getY();
            zero = p;
            setX(oldX);
            setY(oldY);
        }
    }

    public static class Line{
        public static final float HORIZONTAL = 0f, VERTICAL = Float.POSITIVE_INFINITY;

        public Point headPoint, guidePoint;
        protected float function;

        public Line(int x1, int y1, int x2, int y2){
            this(new GlobalPoint(x1, y1), new GlobalPoint(x2, y2));
        }
        public Line(Line line){
            this(line.headPoint, line.guidePoint);
        }
        public Line(Point aHeadPoint, Point aGuidePoint){
            headPoint = aHeadPoint;
            guidePoint = aGuidePoint;
            function = calculateFunction(aHeadPoint, aGuidePoint);
        }
        public Line(Point aHeadPoint, float aFunction){
            headPoint = aHeadPoint;
            function = aFunction;
            if (isVertical(this))
                guidePoint = new GlobalPoint(headPoint.getX(), headPoint.getY() + 20);
            else
                guidePoint = new GlobalPoint(headPoint.getX() + 20, calculateYByX(headPoint.getX() + 20));
        }

        public void update(){
            function = calculateFunction(headPoint, guidePoint);
        }
        public void reinitialize(Point aHeadPoint, Point aGuidePoint){
            headPoint = aHeadPoint;
            guidePoint = aGuidePoint;
        }

        public float getFunction(){
            return function;
        }

        public boolean equals(Line line){
            return isParallel(line) && belongs(line.headPoint);
        }
        public boolean isParallel(Line l) {
            if (isVertical(this)) {
                return isVertical(l);
            } else if (isHorizontal(this)) {
                return isHorizontal(l);
            } else {
                return Math.abs((headPoint.getX() - guidePoint.getX()) / (headPoint.getY() - guidePoint.getY())) ==
                        Math.abs((l.headPoint.getX() - l.guidePoint.getX()) / (l.headPoint.getY() - l.guidePoint.getY()));
            }
        }
        public boolean belongs(Point point){
            if (isVertical(this))
                return Math.abs(point.getX() - headPoint.getX()) < INACCURACY2;
            return Math.abs(point.getY() - calculateYByX(point.getX())) < INACCURACY2;
        }

        public Line copy(){
            return new Line(headPoint, guidePoint);
        }

        public void rotate(float angle){
            NewGeometry.PointTools.rotate(guidePoint, headPoint, angle);
            reinitialize(headPoint, guidePoint);
        }

        public static boolean isHorizontal(Line line){
            if (line.headPoint == null || line.guidePoint == null) {
                return Math.abs(line.function) == HORIZONTAL;
            }
            return line.headPoint.getY() == line.guidePoint.getY();
        }
        public static boolean isVertical(Line line){
            if (line.headPoint == null || line.guidePoint == null) {
                return Math.abs(line.function) == VERTICAL || Float.isNaN(line.function);
            }
            return line.headPoint.getX() == line.guidePoint.getX();
        }

        public void print() {
            System.out.println(headPoint.getX() + " " + headPoint.getY());
        }

        public static float calculateFunction(Point p1, Point p2){
            if (p1.getX() == p2.getX()) {
                return VERTICAL;
            } else if (p1.getY() == p2.getY()) {
                return HORIZONTAL;
            }
            return (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
        }

        protected float calculateXByY(float y){
            if (isHorizontal(this)) throw new UncertainFunction("uncertain answer: line is horizontal");
            if (isVertical(this))
                return headPoint.getX();
            return headPoint.getX() + (y - headPoint.getY()) / function;
        }
        protected float calculateYByX(float x){
            if (isVertical(this)) throw new RuntimeException("uncertain answer: line is vertical");
            if (isHorizontal(this))
                return headPoint.getY();
            return headPoint.getY() + (x - headPoint.getX()) * function;
        }
    }
    public static class Ray extends Line{
        protected boolean direction;

        public Ray(Point aHeadPoint, Point aGuidePoint){
            super(aHeadPoint, aGuidePoint);
            direction = NewGeometry.PointTools.findDirectionTo(headPoint, guidePoint);
        }

        @Override
        public void reinitialize(Point aHeadPoint, Point aGuidePoint){
            super.reinitialize(aHeadPoint, aGuidePoint);
            direction = NewGeometry.PointTools.findDirectionTo(headPoint, guidePoint);
        }

        @Override
        public boolean belongs(Point point){
            return super.belongs(point) &&
                    (direction == NewGeometry.PointTools.findDirectionTo(headPoint, point) || point.equals(headPoint));
        }

        @Override
        public Line copy(){
            return new Ray(headPoint.copy(), guidePoint.copy());
        }

        public boolean getDirection() {
            return direction;
        }
    }
    public static class Segment extends Ray{
        public Segment(float x1, float y1, float x2, float y2) {
            this(new GlobalPoint(x1, y1), new GlobalPoint(x2, y2));
        }

        public Segment(Point start, Point end){
            super(start, end);
        }
        // Exception Horizontal and Vertical lines
        public Segment(Ray ray, float length){
            super(ray.headPoint, ray.guidePoint.copy());
            float l = length();

            Point hp = headPoint.copy(), gp = guidePoint.copy();
            gp.addX(-hp.getX()); gp.addY(-hp.getY());
            hp.setX(0); hp.setY(0);

            guidePoint.setX(headPoint.getX() + gp.getX() * length / l);
            guidePoint.setY(headPoint.getY() + gp.getY() * length / l);
        }

        @Override
        public void reinitialize(Point start, Point end){
            super.reinitialize(start, end);
            this.guidePoint = end;
        }

        public GlobalPoint middle(){
            GlobalPoint p = new GlobalPoint();
            p.x = (headPoint.getX() + guidePoint.getX()) / 2;
            p.y = (headPoint.getY() + guidePoint.getY()) / 2;
            return p;
        }

        @Override
        public boolean belongs(Point point){
            return super.belongs(point) &&
                    (NewGeometry.PointTools.findDirectionTo(guidePoint, point) == !direction || point.equals(guidePoint));
        }
        public Line copy(){
            return new Segment(headPoint.copy(), guidePoint.copy());
        }

        public float length(){
            return (float) Math.sqrt((headPoint.getX() - guidePoint.getX()) * (headPoint.getX() - guidePoint.getX()) +
                    (headPoint.getY() - guidePoint.getY()) * (headPoint.getY() - guidePoint.getY()));
        }

        public void print(){
            System.out.println("Segment: ");
            System.out.println(headPoint.getX() + "  " + headPoint.getY());
            System.out.println(guidePoint.getX() + " " + guidePoint.getY());
        }
    }

    public static class Vector {
        public float mod;

        private final Point guide;

        public Vector() {
            guide = new GlobalPoint();

            this.mod = 0;
        }
        public Vector(Point guidePoint) {
            guide = guidePoint;

            mod = (float) Math.sqrt(guide.getX() * guide.getX() + guide.getY() * guide.getY());
        }

        public Vector(Point guidePoint, float mod) {
            guide = guidePoint;

            this.mod = 1;
            multiple(this, mod);
        }

        public Point coordinate() {
            if (mod == 0) {
                return new GlobalPoint(0, 0);
            }
            return new Segment(new Ray(new GlobalPoint(0, 0), guide), Math.abs(mod)).guidePoint;
        }

        public Vector copy() {
            return new Vector(guide.copy(), mod);
        }

        public boolean equals(Object object) {
            if (object instanceof Vector) {
                Vector v = (Vector) object;
                return v.mod == mod && v.coordinate().equals(coordinate());
            }
            return false;
        }

        public static Vector sum(Vector v1, Vector v2) {
            Point l1 = v1.coordinate(), l2 = v2.coordinate();
            return new Vector(new GlobalPoint(l1.getX() + l2.getX(),
                    l1.getY() + l2.getY()));
        }
        public static Vector sum(ArrayList<Vector> vectors) {
            GlobalPoint coord = new GlobalPoint();
            for (Vector v : vectors) {
                Point p = v.coordinate();
                coord.addX(p.getX());
                coord.addY(p.getY());
            }

            return new Vector(coord);
        }
        public static Vector sum(Vector[] vectors) {
            GlobalPoint coord = new GlobalPoint();
            for (Vector v : vectors) {
                Point p = v.coordinate();
                coord.addX(p.getX());
                coord.addY(p.getY());
            }

            return new Vector(coord);
        }

        public static void multiple(Vector vector, float k) {
            vector.mod *= Math.abs(k);

            vector.guide.setX(vector.guide.getX() * k);
            vector.guide.setY(vector.guide.getY() * k);
        }

        public static Vector getMultiplied(Vector vector, float k) {
            Vector tmp = vector.copy();
            multiple(tmp, k);
            return tmp;
        }

        public static Vector getOpposite(Vector vector) {
            return getMultiplied(vector, -1);
        }


    }

    public static class Curve {
        ArrayList<Segment> edges;

        ArrayList<Point> vertexes;

        public Curve() {
            vertexes = new ArrayList<>();
            edges = new ArrayList<>();
        }

        public Curve(ArrayList<Point> vertexes) {
            this.vertexes = vertexes;
            edges = Figure.toListOfSegments(vertexes);
        }
    }

    public static abstract class Figure{
        public Figure() {}

        public abstract void update();

        public abstract boolean in(Point point);

        public abstract boolean belongs(Point point);

        public abstract void tie(Point p);

        public abstract Point findCentre();

        public abstract float calculateArea();

        public abstract float calculateP();

        public abstract Segment findPackingDiagonal();

        public abstract Figure packInto(SGeometry.SRect rect);

        public static ArrayList<Segment> toListOfSegments(ArrayList<Point> points){
            ArrayList<Segment> lines = new ArrayList<>();
            for (int i = 0; i < points.size() - 1; i++)
                lines.add(new Segment(points.get(i), points.get(i + 1)));
            return lines;
        }
    }
    public static class Circle extends Figure{
        public GlobalPoint centre;
        public float radius;

        public Circle(GlobalPoint centre, float radius){
            if (centre == null) {
                throw new IllegalArgumentException();
            }
            this.centre = centre;
            this.radius = radius;
        }

        @Override
        public void update() {}

        @Override
        public void tie(Point point) {
            centre = new AnchorPoint(point, centre.getX() - point.getX(), centre.getY() - point.getY());
        }

        @Override
        public Figure packInto(SGeometry.SRect rect) {
            float radius = Math.min(rect.width, rect.height) / 2;
            return new Circle(centre, radius);
        }

        public Shape toShape() {
            return toShape(12);
        }
        public Shape toShape(int countOf) {
            ArrayList<Point> edges = new ArrayList<>(countOf);
            float angle = 0;
            for (int i = 1; i <= countOf; i++) {
                angle += 360f / countOf;
                edges.add(new GlobalPoint((float) (centre.getX() + Math.sin(Math.toRadians(angle)) * radius),
                            (float) (centre.getY() + Math.cos(Math.toRadians(angle)) * radius)));
            }

            return new Shape(edges);
        }

        public float getRadius() {
            return radius;
        }

        public void setRadius(float newValue) {
            radius = newValue;
        }

        @Override
        public GlobalPoint findCentre() {
            return centre;
        }

        @Override
        public float calculateArea() {
            return radius  * radius * 3.14f;
        }

        @Override
        public float calculateP() {
            return 2 * radius * 3.14f;
        }

        @Override
        public boolean in(Point point) {
            return !LineTools.isBiggerThen(new Segment(point, centre), radius);
        }

        @Override
        public boolean belongs(Point point){
            return Math.pow(point.getX() - centre.getX(), 2) + Math.pow(point.getY() - centre.getY(), 2) <= radius * radius + INACCURACY2;
        }

        @Override
        public Segment findPackingDiagonal() {
            return new Segment(new GlobalPoint(centre.getX() - radius, centre.getY() - radius),
                    new GlobalPoint(centre.getX() + radius, centre.getY() + radius));
        }
    }

    public static class Shape extends Figure {
        private static final float AREA_CALCULATION_ACCURACY = 0.1f;
        protected ArrayList<Point> vertexes;
        protected ArrayList<Segment> edges;

        public Shape(ArrayList<Point> vertexes) {
            if (vertexes.size() <= 2)
                throw new RuntimeException("Can't be created shape with less then 3 edges");
            this.vertexes = vertexes;
            this.edges = toListOfSegments(vertexes);
            this.edges.add(new Segment(vertexes.get(vertexes.size() - 1), vertexes.get(0)));
        }

        public void update() {
            for (Segment s  : edges) {
                s.update();
            }
        }

        public Point getVertex(int index) {
            return vertexes.get(index);
        }
        public ArrayList<Point> getVertexes() {
            return new ArrayList<>(vertexes);
        }
        public ArrayList<Point> getVertexes(int fromEdge, int toEdge) {
            ArrayList<Point> result = new ArrayList<>(Math.abs(toEdge - fromEdge));
            if (toEdge > fromEdge) {
                for (int i = fromEdge; i <= toEdge; i++) {
                    result.add(vertexes.get(i));
                }
            } else {
                for (int i = fromEdge; i > toEdge; i--) {
                    result.add(vertexes.get(i));
                }
            }

            return result;
        }

        public Segment getEdge(int index) {
            return edges.get(index);
        }

        public int countOfEdges() {
            return vertexes.size();
        }

        @Override
        public void tie(Point p) {
            vertexes.replaceAll(point -> new AnchorPoint(p, point.getX() - p.getX(), point.getY() - p.getY()));
            for (int i = 0; i < edges.size(); i++) {
                edges.get(i).headPoint = vertexes.get(i);
                edges.get(i).guidePoint = vertexes.get((i + 1) % vertexes.size());
            }
        }

        public void rotate(float angle) {
            for (Segment s : edges) {
                s.rotate(angle);
            }
        }

        @Override
        public Figure packInto(SGeometry.SRect rect) {
            Segment d = findPackingDiagonal();
            float wK = rect.width / (d.guidePoint.getX() - d.headPoint.getX()),
                    hK = rect.height / (d.guidePoint.getY() - d.headPoint.getY());

            ArrayList<Point> newVertexes = new ArrayList<>(countOfEdges());
            for (Point vertex : vertexes) {
                newVertexes.add(new GlobalPoint(rect.x + (vertex.getX() - d.headPoint.getX()) * wK,
                        rect.y - (vertex.getY() - d.headPoint.getY()) * hK));
            }

            return new Shape(newVertexes);
        }

        @Override
        public GlobalPoint findCentre() {
            GlobalPoint centre = new GlobalPoint(0, 0);

            float a;
            float b = 0;

            for (int i = 0; i + 1 < vertexes.size(); i++) {
                a = vertexes.get(i).getX() * vertexes.get(i + 1).getY() - vertexes.get(i + 1).getX() * vertexes.get(i).getY();
                b += a;

                centre.x += (vertexes.get(i).getX() + vertexes.get(i + 1).getX()) * a;
                centre.y += (vertexes.get(i).getY() + vertexes.get(i + 1).getY()) * a;
            }
            a = vertexes.get(vertexes.size() - 1).getX() * vertexes.get(0).getY() - vertexes.get(0).getX() * vertexes.get(vertexes.size() - 1).getY();
            b += a;

            centre.x += (vertexes.get(vertexes.size() - 1).getX() + vertexes.get(0).getX()) * a;
            centre.y += (vertexes.get(vertexes.size() - 1).getY() + vertexes.get(0).getY()) * a;

            b *= 3;
            centre.x /= b;
            centre.y /= b;
            System.out.println("632: result: " + centre);
            return centre;
        }

        // calculate area by Pick's theorem
        public float calculateArea() {
            float INACCURACY = 0.01f;
            Shape s = simpler(this);

            Segment diagonal = s.findPackingDiagonal();

            int inner = 0, border = 0;
            for (float x = diagonal.headPoint.getX(); x <= diagonal.guidePoint.getX(); x = Math.round((x + INACCURACY) * 100) / 100f) {
                Line line = new Line(new GlobalPoint(x, 0), Line.VERTICAL);
                ArrayList<Triple<Point, Integer, Boolean>> collisions = new ArrayList<>();
                for (int i = 0; i < s.edges.size(); i++) {
                    if (Line.isVertical(s.edges.get(i)) && s.edges.get(i).headPoint.getX() == x) {
                        Point nearer = NewGeometry.PointTools.getNearestToZero(s.edges.get(i).headPoint, s.edges.get(i).guidePoint);
                        Point farther = NewGeometry.PointTools.getFarthestTo(new GlobalPoint(), s.edges.get(i).headPoint, s.edges.get(i).guidePoint);
                        collisions.add(new Triple<>(nearer, i, true));
                        collisions.add(new Triple<>(farther, i, true));
                    } else {
                        Point collision = LineTools.findCommonPoint(line, s.edges.get(i));
                        if (collision != null) {
                            if (collisions.isEmpty()) {
                                collisions.add(new Triple<>(collision, i, false));
                            } else if (!collision.equals(collisions.get(collisions.size() - 1).f)) {
                                var last = collisions.get(collisions.size() - 1);
                                if (collisions.get(collisions.size() - 1).t &&
                                        s.in(new GlobalPoint(x, (collision.getY() + last.f.getY()) / 2))) {
                                    collisions.add(new Triple<>(last.f, last.s, false));
                                }
                                collisions.add(new Triple<>(collision, i, false));
                            }
                        }
                    }
                }
                collisions.sort(new Comparator<Triple<Point, Integer, Boolean>>() {
                    @Override
                    public int compare(Triple<Point, Integer, Boolean> t1, Triple<Point, Integer, Boolean> t2) {
                        return PointTools.compareTo(t1.f, t2.f);
                    }
                });

                for (int i = 0; i + 1 < collisions.size(); i += 2) {
                    int countOf = (int) (Math.abs(collisions.get(i + 1).f.getY() - collisions.get(i).f.getY()) / INACCURACY);
                    if (countOf == 0) {
                        i--;
                        continue;
                    }
                    if (collisions.get(i).t) {
                        border += countOf + 1;
                    } else {
                        inner += countOf + 1;
                        if (collisions.get(i).f.getY() % INACCURACY < 0.1) {
                            border++;
                            inner--;
                        } if (collisions.get(i + 1).f.getY() % INACCURACY < 0.1) {
                            border++;
                            inner--;
                        }
                    }
                }
            }
            return (inner + border / 2f - 1) * INACCURACY * INACCURACY;
        }

        @Override
        public float calculateP() {
            float P = 0;
            for (Segment segment : edges)
                P += segment.length();
            return P;
        }

        @Override
        public boolean in(Point point) {
            Line rays = new Line(point, Line.HORIZONTAL);
            int left = 0, right = 0;

            Point lastCollision = null;
            for (Segment s : edges) {
                Point collision = LineTools.findCommonPoint(rays, s);
                if (collision != null && (lastCollision == null || collision.getX() != lastCollision.getX())) {
                    lastCollision = collision;
                    if (collision.getX() == point.getX())
                        return true;
                    else if (collision.getX() > point.getX())
                        right++;
                    else
                        left++;
                }
            }
            return left % 2 == 1 && right % 2 == 1;
        }

        @Override
        public boolean belongs(Point point) {
            for (Segment segment : edges)
                if (segment.belongs(point))
                    return true;
            return false;
        }

        public static Shape simpler(Shape s){
            ArrayList<Point> newEdges = new ArrayList<>(s.vertexes.size());
            for (Point p : s.vertexes){
                newEdges.add(new GlobalPoint((float) (Math.floor(p.getX() / INACCURACY) * INACCURACY),
                        (float) (Math.floor(p.getY() / INACCURACY) * INACCURACY)));
            }
            return new Shape(newEdges);
        }

        // return point with minimal position(minimal x and minimal y) and
        // maximal position(maximal x and maximal y)
        @Override
        public Segment findPackingDiagonal() {
            float minX = vertexes.get(0).getX(), maxX = vertexes.get(0).getX();
            float minY = vertexes.get(0).getY(), maxY = vertexes.get(0).getY();
            for (Point p : vertexes) {
                minX = Math.min(p.getX(), minX);
                maxX = Math.max(p.getX(), maxX);
                minY = Math.min(p.getY(), minY);
                maxY = Math.max(p.getY(), maxY);
            }
            return new Segment(new GlobalPoint(minX, minY), new GlobalPoint(maxX, maxY));
        }

        public void print() {
            System.out.println("Shape: ");
            for (Point e : vertexes) {
                System.out.println(e.getX() + " " + e.getY());
            }
            System.out.println();
        }
    }
    public static class Triangle extends Shape {
        Triangle(ArrayList<Point> edges){
            super(edges);
        }
        Triangle(Point edge1, Point edge2,Point edge3){
            this(toListOfEdges(edge1, edge2, edge3));
        }

        @Override
        public GlobalPoint findCentre(){
            return LineTools.findCommonPoint(new Line(vertexes.get(0), edges.get(1).middle()),
                    new Line(vertexes.get(1), edges.get(2).middle()));
        }
        @Override
        public float calculateArea(){
            float a = edges.get(0).length(), b = edges.get(1).length(), c = edges.get(2).length();
            double p = (a + b + c) / 2;
            return (float) Math.sqrt(p * (p - a) * (p - b) * (p - c));
        }

        private static ArrayList<Point> toListOfEdges(Point edge1, Point edge2, Point edge3){
            ArrayList<Point> edges = new ArrayList<>();
            edges.add(edge1); edges.add(edge2);
            edges.add(edge3);
            return edges;
        }
    }
    public static class Rect extends Shape {
        public Rect(Segment diagonal) {
            super(wrapUp(diagonal));
        }

        public Rect(float x, float y, float width, float height) {
            super(new ArrayList<>(Arrays.asList(new Point[]{
                    new GlobalPoint(x, y),
                    new GlobalPoint(x + width, y),
                    new GlobalPoint(x + width, y - height),
                    new GlobalPoint(x, y - height)
            })));
        }

        @Override
        public GlobalPoint findCentre() {
            return LineTools.findCommonPoint(new Segment(vertexes.get(0), vertexes.get(2)),
                    new Segment(vertexes.get(1), vertexes.get(3)));
        }

        @Override
        public float calculateArea() {
            return new Segment(vertexes.get(0), vertexes.get(1)).length() *
                    new Segment(vertexes.get(1), vertexes.get(2)).length();
        }

        private static ArrayList<Point> wrapUp(Segment diagonal) {
            ArrayList<Point> vertexes = new ArrayList<>(4);
            vertexes.add(new GlobalPoint(diagonal.headPoint.getX(), diagonal.headPoint.getY()));
            vertexes.add(new GlobalPoint(diagonal.guidePoint.getX(), diagonal.headPoint.getY()));
            vertexes.add(new GlobalPoint(diagonal.guidePoint.getX(), diagonal.guidePoint.getY()));
            vertexes.add(new GlobalPoint(diagonal.headPoint.getX(), diagonal.guidePoint.getY()));
            return vertexes;
        }
    }

    public static class PointTools implements Comparator<Point>{
        @Override
        public int compare(Point p1, Point p2) {
            return compareTo(p1, p2);
        }

        public static int compareTo(Point p1, Point p2) {
            if (p1.getX() < p2.getX())
                return -1;
            else if (p1.getX() == p2.getX() && p1.getY() < p2.getY())
                return -1;
            return 1;
        }

        public static void rotate(Point rotating, Point axe, double angle) {
            double a = Math.toRadians(angle);
            rotating.setX((float) (Math.cos(a) * (rotating.getX() - axe.getX()) -
                    Math.sin(a) * (rotating.getY() - axe.getY()) + axe.getX()));
            rotating.setY((float) (Math.cos(a) * (rotating.getY() - axe.getY()) +
                    Math.sin(a) * (rotating.getX() - axe.getX()) + axe.getY()));
        }

        public static Point getNearestTo(Point head, Point p1, Point p2) {
            float distanceByX1 = Math.abs(head.getX() - p1.getX());
            float distanceByX2 = Math.abs(head.getX() - p2.getX());
            if (distanceByX1 < distanceByX2)
                return p1;
            else if (distanceByX1 == distanceByX2 && Math.abs(head.getY() - p1.getY()) < Math.abs(head.getY() - p2.getY()))
                return p1;
            return p2;
        }
        public static Point getNearestToZero(Point p1, Point p2) {
            if (p1.getX() < p2.getX())
                return p1;
            else if (p1.getX() == p2.getX() && p1.getY() < p2.getY())
                return p1;
            return p2;
        }

        public static Point getFarthestTo(Point head, Point p1, Point p2) {
            float distanceByX1 = Math.abs(head.getX() - p1.getX());
            float distanceByX2 = Math.abs(head.getX() - p2.getX());
            if (distanceByX1 > distanceByX2)
                return p1;
            else if (distanceByX1 == distanceByX2 && Math.abs(head.getY() - p1.getY()) > Math.abs(head.getY() - p2.getY()))
                return p1;
            return p2;
        }

        private static boolean findDirectionTo(Point from, Point to) {
            return to.getX() > from.getX() || to.getX() == from.getX() &&
                    to.getY() > from.getY();
        }
    }

    public static class LineTools {
        public static boolean isBigger(Segment s1, Segment s2) {
            return Math.pow(s1.headPoint.getX() - s1.guidePoint.getX(), 2) +
                    Math.pow(s1.headPoint.getY() - s1.guidePoint.getY(), 2) >
                    Math.pow(s2.headPoint.getX() - s2.guidePoint.getX(), 2) +
                            Math.pow(s2.headPoint.getY() - s2.guidePoint.getY(), 2);
        }
        public static boolean isBiggerThen(Segment segment, float length) {
            return Math.pow(segment.headPoint.getX() - segment.guidePoint.getX(), 2) +
                    Math.pow(segment.headPoint.getY() - segment.guidePoint.getY(), 2) > length * length;
        }

        public static int findDirection(Line from, Point to) {
            if (Line.isVertical(from)) {
                if (from.headPoint.getY() > to.getY())
                    return 1;
                else if (from.headPoint.getY() == to.getY())
                    return 0;
            } else if (Line.isHorizontal(from)) {
                if (from.headPoint.getX() > to.getX())
                    return 1;
                else if (from.headPoint.getX() == to.getX())
                    return 0;
            } else {
                float x = from.calculateXByY(to.getY());
                if (x > to.getX())
                    return 1;
                else if (x == to.getX())
                    return 0;
            }
            return -1;
        }

        public static boolean areColliding(Line l1, Line l2) {
            return LineTools.findCommonPoint(l1, l2) != null;
        }

        public static GlobalPoint findCommonPoint(Line l1, Line l2) {
            GlobalPoint commonPoint;
            if (l1.isParallel(l2)) {
                if (Line.isVertical(l1)) {
                    float minY = Math.max(Math.min(l1.headPoint.getY(), l1.guidePoint.getY()),
                            Math.min(l2.headPoint.getY(), l2.guidePoint.getY()));
                    float maxY = Math.min(Math.max(l1.headPoint.getY(), l1.guidePoint.getY()),
                            Math.max(l2.headPoint.getY(), l2.guidePoint.getY()));
                    commonPoint = new GlobalPoint(l1.headPoint.getX(), (minY + maxY) / 2);
                } else {
                    float minX = Math.max(Math.min(l1.headPoint.getX(), l1.guidePoint.getX()),
                            Math.min(l2.headPoint.getX(), l2.guidePoint.getX()));
                    float maxX = Math.min(Math.max(l1.headPoint.getX(), l1.guidePoint.getX()),
                            Math.max(l2.headPoint.getX(), l2.guidePoint.getX()));
                    float x = (minX + maxX) / 2;
                    commonPoint = new GlobalPoint(x, l1.calculateYByX(x));
                }
            } else {
                float x = findXForCommonPoint(l1, l2);
                if (Line.isVertical(l1) && Line.isVertical(l2)) {
                    return null;
                } else if (Line.isVertical(l1)) {
                    commonPoint = new GlobalPoint(x, l2.calculateYByX(x));
                } else {
                    commonPoint = new GlobalPoint(x, l1.calculateYByX(x));
                }
            }

            if (testMode)
                commonPoint.print();

            return l1.belongs(commonPoint) && l2.belongs(commonPoint) ? commonPoint : null;
        }

        public static Line createParallel(Line l, Point from) {
            return new Line(from, l.getFunction());
        }

        public static Segment createPerpendicular(Line l, GlobalPoint from) {
            return new Segment(from, findPerpendicularPoint(l, from));
        }
        public static GlobalPoint findPerpendicularPoint(Line l, GlobalPoint p) {
            Circle circle = new Circle(p, new Segment(l.headPoint, p).length());
            ArrayList<GlobalPoint> lateralVertexes = FiguresTools.findIntersections(circle, new Line(l));

            return new Segment(lateralVertexes.get(0), lateralVertexes.get(lateralVertexes.size() - 1)).middle();
        }

        private static float findXForCommonPoint(Line l1, Line l2) {
            if (l1.function == l2.function) {
                return Float.NEGATIVE_INFINITY;
            }

            if (Line.isVertical(l1))
                return l1.headPoint.getX();
            if (Line.isVertical(l2))
                return l2.headPoint.getX();
            if (Line.isHorizontal(l1))
                return l2.calculateXByY(l1.headPoint.getY());
            if (Line.isHorizontal(l2))
                return l1.calculateXByY(l2.headPoint.getY());

            return ((l2.headPoint.getY() - l1.headPoint.getY()) +
                    (l1.headPoint.getX() * l1.function - l2.headPoint.getX() * l2.function)) / (l1.function - l2.function);
        }
        private static float findYForCommonPoint(Line l1, Line l2) {
            if (l1.function == l2.function)
                return Float.NEGATIVE_INFINITY;

            if (Line.isHorizontal(l1))
                return l1.headPoint.getY();
            if (Line.isHorizontal(l2))
                return l2.headPoint.getY();
            if (Line.isVertical(l1))
                return l2.calculateYByX(l1.headPoint.getX());
            if (Line.isVertical(l2))
                return l1.calculateYByX(l2.headPoint.getX());

            return (-l2.headPoint.getY() * l1.function + l2.function *
                    (l1.headPoint.getY() + l2.headPoint.getX() * l1.function - l1.headPoint.getX() * l1.function))
                    / (l2.function - l1.function);
        }
    }

    public static class FiguresTools{
        // copy point when start or end equals vertex of the shape
        public static ArrayList<Shape> split(Shape s, Line l) {
            ArrayList<Shape> result = new ArrayList<>();

            ArrayList<Pair<Point, Integer>> collisions = new ArrayList<>();
            Point lastCollision, collision = null;
            for (int i = 0; i < s.edges.size(); i++) {
                lastCollision = collision;
                collision = LineTools.findCommonPoint(s.edges.get(i), l);
                if (collision != null) {
                    if (lastCollision == null || !collision.equals(lastCollision)) {
                        collisions.add(new Pair<>(collision, i));
                    }
                }
            }
            if (collisions.size() <= 1) {
                result.add(s);
                return result;
            }

            collisions.sort((p1, p2) -> PointTools.compareTo(p1.f, p2.f));
            int start = 0;
            ArrayList<Point> edges = new ArrayList<>();
            // don't equals to next point of then shape
            if (!collisions.get(0).f.equals(s.getVertex(collisions.get(0).s))) {
                edges.add(collisions.get(0).f);
            }
            for (int i = 0, j = collisions.get(0).s + 1; i <= s.edges.size(); i++, j = i % s.edges.size()) {
                edges.add(s.getVertex(j));
                int index = 0;
                for (; index < collisions.size(); index++) {
                    if (collisions.get(index).s == j) {
                        break;
                    }
                }
                // there is a collision
                if (index < collisions.size()) {
                    // don't equals to last written point
                    if (!edges.get(edges.size() - 1).equals(collisions.get(index).f)) {
                        edges.add(collisions.get(index).f);
                    }

                    if (index > start) {
                        for (int t = index - 1; t > start; t--) {
                            edges.add(collisions.get(t).f);
                            if (collisions.get(t).s < collisions.get(t - 1).s) {
                                edges.addAll(s.getVertexes(collisions.get(t).s + 1, collisions.get(t - 1).s));
                            } else {
                                edges.addAll(s.getVertexes(collisions.get(t).s, collisions.get(t - 1).s - 1));
                            }
                            edges.add(collisions.get(t + 1).f);
                        }
                    } else {
                        for (int t = index + 1; t < start; t++) {
                            edges.add(collisions.get(t).f);
                            if (collisions.get(t).s < collisions.get(t - 1).s) {
                                edges.addAll(s.getVertexes(collisions.get(t).s + 1, collisions.get(t + 1).s));
                            } else {
                                edges.addAll(s.getVertexes(collisions.get(t).s, collisions.get(t + 1).s - 1));
                            }
                            edges.add(collisions.get(t + 1).f);
                        }
                    }
                    if (edges.get(edges.size() - 1).equals(edges.get(0))) {
                        edges.remove(edges.size() - 1);
                    }
                    if (edges.size() > 2) {
                        result.add(new Shape(edges));
                        edges = new ArrayList<>();
                    }
                    start = index;
                }
            }
            if (edges.size() > 2) {
                result.add(new Shape(edges));
            }
            return result;
        }

        public static ArrayList<Shape> split(Shape s, Curve curve) {
            ArrayList<Shape> result = new ArrayList<>();
            if (curve.vertexes.size() < 2) {
                result.add(s);
                return result;
            }

            BiGrav<Point, Segment> grav = new BiGrav<>();
            // basic filling common grav;
            grav.addVertex(curve.vertexes.get(0));
            for (int i = 1; i < curve.vertexes.size(); i++) {
                grav.addVertex(curve.vertexes.get(i));
                grav.addEdge(i - 1, i, curve.edges.get(i - 1));
            }
            // to continue filling
            grav.addVertex(s.vertexes.get(0));
            for (int i = 1; i < s.vertexes.size(); i++) {
                grav.addVertex(s.vertexes.get(i));
                grav.addEdge(i - 1, i, s.edges.get(i - 1));
            }

            ArrayList<Integer> collisions = new ArrayList<>();

            // processing collisions, creating bridges
            for (int i = 0; i < grav.size(); i++) {
                for (int ie = 0; ie < grav.get(i).edges.size(); ie++) {
                    for (int j = i + 1; j < grav.size(); j++) {
                        for (int je = 0; je < grav.get(j).edges.size(); je++) {
                            Point collision = LineTools.findCommonPoint(grav.get(i).edges.get(ie).value,
                                    grav.get(j).edges.get(je).value);
                            if (collision != null) {
                                int vertex;

                                if (collision.equals(grav.get(i).value)) {
                                    vertex = i;
                                    grav.put(j, grav.get(j).edges.get(je).to, vertex,
                                            new Segment(grav.get(j).value, grav.get(vertex).value),
                                            new Segment(grav.get(vertex).value, grav.get(grav.get(j).edges.get(je).to).value));
                                } else if (collision.equals(grav.get(grav.get(i).edges.get(ie).to).value)) {
                                    vertex = grav.get(i).edges.get(ie).to;
                                    grav.put(j, grav.get(j).edges.get(je).to, vertex,
                                            new Segment(grav.get(j).value, grav.get(vertex).value),
                                            new Segment(grav.get(vertex).value, grav.get(grav.get(j).edges.get(je).to).value));

                                } else if (collision.equals(grav.get(j).value)) {
                                    vertex = j;
                                    grav.put(i, grav.get(i).edges.get(ie).to, vertex,
                                            new Segment(grav.get(i).value, grav.get(vertex).value),
                                            new Segment(grav.get(vertex).value, grav.get(grav.get(i).edges.get(ie).to).value));
                                } else if (collision.equals(grav.get(grav.get(j).edges.get(je).to).value)) {
                                    vertex = grav.get(j).edges.get(je).to;
                                    grav.put(i, grav.get(i).edges.get(ie).to, vertex,
                                            new Segment(grav.get(i).value, grav.get(vertex).value),
                                            new Segment(grav.get(vertex).value, grav.get(grav.get(i).edges.get(ie).to).value));
                                } else {
                                    vertex = grav.size();
                                    grav.addVertex(collision);

                                    grav.put(i, grav.get(i).edges.get(ie).to, vertex,
                                            new Segment(grav.get(i).value, grav.get(vertex).value),
                                            new Segment(grav.get(vertex).value, grav.get(grav.get(i).edges.get(ie).to).value));

                                    grav.put(j, grav.get(j).edges.get(je).to, vertex,
                                            new Segment(grav.get(j).value, grav.get(vertex).value),
                                            new Segment(grav.get(vertex).value, grav.get(grav.get(j).edges.get(je).to).value));
                                }

                                collisions.add(vertex);
                            }
                        }
                    }
                }
            }

            if (collisions.size() % 2 == 1) {
                grav.get(collisions.get(collisions.size() - 1)).edges = new ArrayList<>();
            }

            ArrayList<Integer> oldCycles = new ArrayList<>();
            for (int collision : collisions) {
                ArrayList<ArrayList<Integer>> cycles = grav.findCycles(collision);
                for (var cycle : cycles) {
                    if (!oldCycles.contains(cycle.get(0))) {
                        ArrayList<Point> edges = new ArrayList<>(cycle.size());
                        ArrayList<Point> brunches = new ArrayList<>();

                        for (int i = 0; i < cycle.size(); i++) {
                            edges.add(grav.get(cycle.get(i)).value);
                            // is bridge
                            if (grav.get(cycle.get(i)).edges.size() > 1) {
                                if (i + 1 < cycle.size()) {
                                    oldCycles.add(cycle.get(i + 1));
                                }
                                if (i - 1 >= 0) {
                                    oldCycles.add(cycle.get(i - 1));
                                }
                                for (int j = 0; j < grav.get(cycle.get(i)).edges.size(); j++) {
                                    if (i + 1 == cycle.size() || grav.get(cycle.get(i)).edges.get(j).to != cycle.get(i + 1)) {
                                        brunches.add(grav.get(grav.get(cycle.get(i)).edges.get(j).to).value);
                                    }
                                }
                            }
                        }

                        Shape shape = new Shape(edges);
                        SGeometry.SRect hitBox = new SGeometry.SRect(shape.findPackingDiagonal());
                        boolean isRight = true;
                        for (Point brunch : brunches) {
                            if (hitBox.in(brunch) && shape.in(brunch)) {
                                isRight = false;
                                break;
                            }
                        }
                        if (isRight) {
                            result.add(shape);
                        }
                    }
                }
            }

            return result;
        }

        public static ArrayList<GlobalPoint> findIntersections(Figure f1, Figure f2) {
            if (f1 instanceof Shape && f2 instanceof Shape){
                return findIntersections((Shape) f1, (Shape) f2);
            }
            if (f1 instanceof Circle && f2 instanceof Shape) {
                return findIntersections((Circle) f1, (Shape) f2);
            }
            if (f1 instanceof Shape && f2 instanceof Circle) {
                return findIntersections((Circle) f2, (Shape) f1);
            }
            if (f1 instanceof Circle && f2 instanceof Circle)
                return findIntersections((Circle) f1, (Circle) f2);
            throw new RuntimeException("undefined class of figure");
        }
        public static ArrayList<GlobalPoint> findIntersections(Shape s1, Shape s2) {
            ArrayList<GlobalPoint> points = new ArrayList<>();
            for (Segment seg1 : s1.edges){
                for (Segment seg2 : s2.edges){
                    GlobalPoint p = LineTools.findCommonPoint(seg1, seg2);
                    if (p != null)
                        points.add(p);
                }
            }
            return points;
        }
        public static ArrayList<GlobalPoint> findIntersections(Circle c, Shape shape) {
            ArrayList<GlobalPoint> points = new ArrayList<>();
            for (Segment s : shape.edges){
                ArrayList<GlobalPoint> tmp = FiguresTools.findIntersections(c, s);
                for (GlobalPoint p : tmp) {
                    if (p != null)
                        points.add(p);
                }
            }
            return points;
        }
        public static ArrayList<GlobalPoint> findIntersections(Circle c1, Circle c2) {
            ArrayList<GlobalPoint> points = new ArrayList();
            Segment segment = new Segment(c1.centre, c2.centre);
            float length = segment.length();
            if (length == c1.radius + c2.radius){
                segment = new Segment(new Ray(c1.centre, c2.centre), c1.radius);
                if (segment.headPoint != c1.centre)
                    points.add(new GlobalPoint(segment.headPoint));
                else
                    points.add(new GlobalPoint(segment.guidePoint));
            }
            else if (length < c1.radius + c2.radius){
                Point intersectionCentre;
                Line arcBase;
                float distance = c2.radius - (c1.radius - Math.abs(length - c2.radius)) / 2;
                segment = new Segment(new Ray(c1.centre, c2.centre), c1.radius);
                intersectionCentre = segment.headPoint != c1.centre ? segment.headPoint : segment.guidePoint;
                if (Line.isHorizontal(segment))
                    arcBase = new Line(intersectionCentre, Line.VERTICAL);
                else
                    arcBase = LineTools.createParallel(LineTools.createPerpendicular(segment,
                            new GlobalPoint(segment.headPoint.getX() + 1, segment.guidePoint.getY())), intersectionCentre);
                return FiguresTools.findIntersections(c1, arcBase);
            }
            return points;
        }

        public static ArrayList<Triple<Segment, Segment, GlobalPoint>> findPlaces(Shape s1, Shape s2) {
            ArrayList<Triple<Segment, Segment, GlobalPoint>> numbers = new ArrayList<>();
            for (int i = 0; i < s1.countOfEdges(); i++) {
                for (int j = 0; j < s2.countOfEdges(); j++) {
                    GlobalPoint p = LineTools.findCommonPoint(s1.edges.get(i), s2.edges.get(j));
                    if (p != null) {
                        numbers.add(new Triple<>(s1.edges.get(i), s2.edges.get(j), p));
                    }
                }
            }

            return numbers;
        }
        public static ArrayList<Pair<Segment, GlobalPoint>> findPlaces(Shape s, Circle c) {
            ArrayList<Pair<Segment, GlobalPoint>> places = new ArrayList<>();
            for (int i = 0; i < s.edges.size(); i++) {
                ArrayList<GlobalPoint> ps = findIntersections(c, s.edges.get(i));
                if (!ps.isEmpty()) {
                    for (GlobalPoint p : ps) {
                        places.add(new Pair<>(s.edges.get(i), p));
                    }
                }
            }
            return places;
        }

        public static boolean areColliding(Figure f, Line l) {
            if (f instanceof Shape) {
                for (Segment s : ((Shape) f).edges) {
                    if (LineTools.areColliding(s, l)) {
                        return true;
                    }
                }
                return false;
            } else if (f instanceof Circle) {
                Circle c = (Circle) f;
                if (l.headPoint.equals(l.guidePoint)) {
                    return new Segment(l.headPoint, c.findCentre()).length() == c.radius;
                }
                return LineTools.createPerpendicular(l, c.centre).length() <= c.radius;
            } else {
                throw new RuntimeException("undefined implements of Figure");
            }
        }
        public static boolean areColliding(Figure f1, Figure f2) {
            if (f1 instanceof Shape && f2 instanceof Shape){
                return areColliding((Shape) f1, (Shape) f2);
            }
            if (f1 instanceof Circle && f2 instanceof Shape) {
                return areColliding((Circle) f1, (Shape) f2);
            }
            if (f1 instanceof Shape && f2 instanceof Circle) {
                return areColliding((Circle) f2, (Shape) f1);
            }
            if (f1 instanceof Circle && f2 instanceof Circle)
                return areColliding((Circle) f1, (Circle) f2);
            throw new RuntimeException("undefined class of figure");
        }
        public static boolean areColliding(Shape s1, Shape s2) {
            for (Segment seg1 : s1.edges){
                for (Segment seg2 : s2.edges){
                    if (LineTools.areColliding(seg1, seg2))
                        return true;
                }
            }
            return false;
        }
        public static boolean areColliding(Circle c, Shape shape) {
            for (Segment s : shape.edges){
                if (!FiguresTools.findIntersections(c, s).isEmpty())
                    return true;
            }
            return false;
        }
        public static boolean areColliding(Circle c1, Circle c2) {
            Segment segment = new Segment(c1.centre, c2.centre);
            float length = segment.length();
            if (length > c1.radius + c2.radius)
                return false;
            return true;
        }

        public static ArrayList<GlobalPoint> findIntersections(Figure f, Line line) {
            if (f instanceof Shape) {
                return findIntersections((Shape) f, line);
            } else {
                return findIntersections((Circle) f, line);
            }
        }
        public static ArrayList<GlobalPoint> findIntersections(Circle c, Line line) {
            float sqF = line.function * line.function;
            ArrayList<GlobalPoint> points = new ArrayList<>();
            GlobalPoint p1, p2;
            if (Line.isVertical(line)) {
                Vectors2<Float> results = MT.solveQuadraticEquation(1, -2 * c.centre.getY(),
                        (float) (c.centre.getY() * c.centre.getY() + Math.pow(line.headPoint.getX() - c.centre.getX(), 2) - c.radius * c.radius));
                p1 = new GlobalPoint(line.calculateXByY(results.x), results.x);
                p2 = new GlobalPoint(line.calculateXByY(results.y), results.y);
            } else {
                // the system with equations: (X - Xcenter) ^ 2 + (Y - Ycentre) ^ 2 = r ^ 2 and
                //                            Ystart + (X - Xstart) * function = Y
                Vectors2<Float> results = MT.solveQuadraticEquation(sqF + 1,
                        -2 * (c.centre.getX() + line.headPoint.getX() * sqF - line.headPoint.getY() * sqF + c.centre.getY() * line.function),
                        c.radius * c.radius - MT.squared(c.centre.getX()) - MT.squared(c.centre.getY()) - MT.squared(line.headPoint.getX() *
                                line.function) - MT.squared(line.headPoint.getY()) + 2 * (line.headPoint.getX() * line.headPoint.getY() * line.function +
                                c.centre.getY() * line.headPoint.getY()));
                p1 = new GlobalPoint(results.x, line.calculateYByX(results.x));
                p2 = new GlobalPoint(results.y, line.calculateYByX(results.y));
            }
            if (line.belongs(p1) && c.belongs(p1)) {
                points.add(p1);
            }
            if (!p2.equals(p1)) {
                points.add(p2);
            }
            return points;
        }
        public static ArrayList<GlobalPoint> findIntersections(Shape s, Line line) {
            ArrayList<GlobalPoint> points = new ArrayList<>();
            for (Segment segment : s.edges){
                GlobalPoint intersetionPoint = LineTools.findCommonPoint(segment, line);
                if (intersetionPoint != null)
                    points.add(intersetionPoint);
            }
            return points;
        }

        public static ArrayList<Pair<Segment, GlobalPoint>> findPlaces(Shape s, Line l) {
            ArrayList<Pair<Segment, GlobalPoint>> places = new ArrayList<>();
            for (Segment seg : s.edges) {
                GlobalPoint collision = LineTools.findCommonPoint(seg, l);
                if (collision != null) {
                    places.add(new Pair<>(seg, collision));
                }
            }
            return places;
        }
    }
}

/*
isCollidingForSegment
    return (line.headPoint.x >= calculateX(line.headPoint.y, this))
                        == (line.guidePoint.x <= calculateX(line.guidePoint.y, this))
                        && (line.getY(false) >= calculateY(line.headPoint.x, this))
                        == (line.getY(true) <= calculateY(line.guidePoint.x, this));
old classes
public static class Rect extends Figure{
        private Line diagonal;
        private ArrayList<Point> edges;
        Rect(){};
        Rect(Segment diagonal, boolean linked){
            init(diagonal);
        }
        Rect(ArrayList<Point> edges){
            init(edges);
        }
        Rect(Polygon polygon){
            init(polygon);
        }
        public void init(Segment diagonal){
            this.diagonal = diagonal;
            updateS();
        }
        public void init(ArrayList<Point> positions){
            Point minPosition = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
            Point maxPosition = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);

            for (Point point : positions){
                minPosition.x = Math.min(minPosition.x, point.x);
                minPosition.y = Math.min(minPosition.y, point.y);
                maxPosition.x = Math.max(maxPosition.x, point.x);
                maxPosition.y = Math.max(maxPosition.y, point.y);
            }
            diagonal = new Segment(minPosition, maxPosition);
            updateS();
        }
        public void init(Polygon polygon){
            ArrayList<Point> figuresPositions = polygon.getEdges();
            init(figuresPositions);
            updateS();
        }

        @Override
        public ArrayList<Segment> getBorder(){
            ArrayList<Point> edges = getEdges();
            ArrayList<Segment> border = new ArrayList<Segment>(4);
            for (int i = 0; i < edges.size(); i++)
                border.add(new Segment(edges.get(i), edges.get(i + 1)));
            border.add(new Segment(edges.get(0), edges.get(edges.size() - 1)));
            return border;
        }
        @Override
        public ArrayList<Point> getEdges(){
            ArrayList<Point> edges = new ArrayList<>(4);
            edges.add(new Point(diagonal.getX(false), diagonal.getY(true))); // left, upper edge
            edges.add(new Point(diagonal.getX(true), diagonal.getY(true))); // right, upper edge
            edges.add(new Point(diagonal.getX(true), diagonal.getY(false))); // right, down edge
            edges.add(new Point(diagonal.getX(false), diagonal.getY(false))); // left, down edge
            return edges;
        }

        @Override
        public void updateS(){
            S = Math.abs((int)((diagonal.start.x - diagonal.end.x)
                    * (diagonal.start.y - diagonal.end.y)));
        }
        @Override
        public float getS(){
            return S;
        }

        @Override // TO-DO
        public ArrayList<Point> findPointsOfIntersection(ArrayList<Segment> anotherFigure){
            ArrayList<Point> pointsOfColliding = new ArrayList<>();
            return pointsOfColliding;
        }//TO-DO
        @Override
        public boolean isCollidingWith(ArrayList<Segment> anotherFigure){
            for (Segment line : anotherFigure)
                if (isCollidingWith(line))
                    return true;
            return false;
        }
        @Override
        public boolean isCollidingWith(Line line){
            if (Line.isHorizontal(line)) // line is parallel for downside of rect
                return line.start.y >= diagonal.getY(false) && line.end.y >= diagonal.getY(true)
                        && (line.start.x >= diagonal.getX(false) == (line.end.x <= diagonal.getX(true)));
            Point point = new Point();
            point.y = Math.min(diagonal.getY(true), line.getY(true));
            point.x = Line.calculateX(point.y, line);
            return point.x >= diagonal.getX(false) && point.x <= diagonal.getX(true);
        }
    }
 */