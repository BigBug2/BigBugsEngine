package libraries.geometry;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;

@RunWith(Enclosed.class)
public class GeometryTest extends NewGeometry {
    @Test
    public void commonTest() {
        // test 1
        {
            GlobalPoint location = new GlobalPoint(5, 5);

            Vector move = new Vector(new GlobalPoint(8, -5));
            Point mCoord = move.coordinate();

            Segment place = new Segment(0, 0, 5, 4);
            System.out.println(LineTools.findPerpendicularPoint(place, location));
            Point globalCoord = LineTools.findCommonPoint(new Line(LineTools.findPerpendicularPoint(place, location), location),
                    LineTools.createParallel(place, new GlobalPoint(location.getX() + mCoord.getX(), location.getY() + mCoord.getY())));

            Vector punch = Vector.getMultiplied(new Vector(new GlobalPoint(globalCoord.getX() - location.getX(), globalCoord.getY() - location.getY())), -2);
            System.out.println(Vector.sum(new Vector[]{move, punch}).coordinate());
        }
    }

    public static class LineTest {
        @Test
        public void belongsTest() {
            Line line; Point belongsP, unbelongsP;

            // test 1
            line = new Line(new GlobalPoint(6, 5), Line.HORIZONTAL);
            belongsP = new GlobalPoint(15, 5);
            unbelongsP = new GlobalPoint(12, 7);
            Assert.assertTrue(line.belongs(belongsP));
            Assert.assertFalse(line.belongs(unbelongsP));

            // test 2
            line = new Line(new GlobalPoint(3, 2), Line.VERTICAL);
            belongsP = new GlobalPoint(3, 12);
            unbelongsP = new GlobalPoint(2, 5);
            Assert.assertTrue(line.belongs(belongsP));
            Assert.assertFalse(line.belongs(unbelongsP));

            // test 3
            line = new Line(new GlobalPoint(-3, 12), new GlobalPoint(6, 18));
            belongsP = new GlobalPoint(12, 22);
            unbelongsP = new GlobalPoint(2, 18);
            Assert.assertTrue(line.belongs(belongsP));
            Assert.assertFalse(line.belongs(unbelongsP));
        }
    }
    public static class RayTest {
        @Test
        public void belongsTest() {
            Ray ray; Point belongsP, unbelongsP;

            // test 1
            ray = new Ray(new GlobalPoint(6, 5), new GlobalPoint(13, 5));
            belongsP = new GlobalPoint(15, 5);
            unbelongsP = new GlobalPoint(2, 5);
            Assert.assertTrue(ray.belongs(belongsP));
            Assert.assertFalse(ray.belongs(unbelongsP));

            // test 2
            ray = new Ray(new GlobalPoint(13, 5), new GlobalPoint(6, 5));
            belongsP = new GlobalPoint(2, 5);
            unbelongsP = new GlobalPoint(15, 5);
            Assert.assertTrue(ray.belongs(belongsP));
            Assert.assertFalse(ray.belongs(unbelongsP));

            // test 3
            ray = new Ray(new GlobalPoint(3, 2), new GlobalPoint(3, 9));
            belongsP = new GlobalPoint(3, 12);
            unbelongsP = new GlobalPoint(3, 1);
            Assert.assertTrue(ray.belongs(belongsP));
            Assert.assertFalse(ray.belongs(unbelongsP));

            // test 4
            ray = new Ray(new GlobalPoint(-3, 12), new GlobalPoint(6, 18));
            belongsP = new GlobalPoint(12, 22);
            unbelongsP = new GlobalPoint(2, 18);
            Assert.assertTrue(ray.belongs(belongsP));
            Assert.assertFalse(ray.belongs(unbelongsP));
        }
    }
    public static class SegmentTest {
        @Test
        public void constructorTest() {
            Ray ray;

            // test 1: horizontal
            ray = new Ray(new GlobalPoint(), new GlobalPoint(5, 0));
            Assert.assertTrue(new Segment(ray, 5).guidePoint.equals(new GlobalPoint(5, 0)));

            // test 2: horizontal
            ray = new Ray(new GlobalPoint(), new GlobalPoint(5, 0));
            Assert.assertTrue(new Segment(ray, 10).guidePoint.equals(new GlobalPoint(10, 0)));

            // test 3: horizontal
            ray = new Ray(new GlobalPoint(5, 0), new GlobalPoint(10, 0));
            Assert.assertTrue(new Segment(ray, 10).guidePoint.equals(new GlobalPoint(15, 0)));

            // test 1: vertical
            ray = new Ray(new GlobalPoint(), new GlobalPoint(0, 5));
            Assert.assertTrue(new Segment(ray, 5).guidePoint.equals(new GlobalPoint(0, 5)));

            // test 2: vertical
            ray = new Ray(new GlobalPoint(), new GlobalPoint(0, 5));
            Assert.assertTrue(new Segment(ray, 10).guidePoint.equals(new GlobalPoint(0, 10)));

            // test 3: vertical
            ray = new Ray(new GlobalPoint(0, 5), new GlobalPoint(0, 10));
            Assert.assertTrue(new Segment(ray, 10).guidePoint.equals(new GlobalPoint(0, 15)));
        }

        @Test
        public void belongsTest() {
            Segment segment; Point belongsP, unbelongsP;

            // test 1
            segment = new Segment(new GlobalPoint(-3, 12), new GlobalPoint(6, 18));
            belongsP = new GlobalPoint(0, 14);
            unbelongsP = new GlobalPoint(12, 22);
            Assert.assertTrue(segment.belongs(belongsP));
            Assert.assertFalse(segment.belongs(unbelongsP));

            // test 2
            segment = new Segment(new GlobalPoint(-3, 12), new GlobalPoint(6, 18));
            belongsP = new GlobalPoint(6, 18);
            Assert.assertTrue(segment.belongs(belongsP));

            // test 3
            segment = new Segment(new GlobalPoint(-3, 12), new GlobalPoint(6, 18));
            belongsP = new GlobalPoint(-3, 12);
            Assert.assertTrue(segment.belongs(belongsP));
            /*
            line = new Line(new GlobalPoint(6, 5), Line.HORIZONTAL);
            belongsP = new GlobalPoint(15, 5);
            unbelongsP = new GlobalPoint(12, 7);
            Assert.assertTrue(line.belongs(belongsP));
            Assert.assertFalse(line.belongs(unbelongsP));*/
        }
    }

    public static class VectorTest {
        @Test
        public void sumTest() {
            Vector[] vectors;

            //test 1
            vectors = new Vector[]{new Vector(new GlobalPoint(7, -4)),
                    Vector.getOpposite(new Vector(new GlobalPoint(0, -4)))};
            Assert.assertTrue(Vector.sum(vectors).equals(new Vector(new GlobalPoint(7, 0))));
            //test 2
            vectors = new Vector[]{new Vector(new GlobalPoint(7, -4)),
                    Vector.getOpposite(new Vector(LineTools.findPerpendicularPoint(new Line(new GlobalPoint(0, -4), Line.HORIZONTAL), new GlobalPoint(0, 0))))};
            Assert.assertTrue(Vector.sum(vectors).equals(new Vector(new GlobalPoint(7, 0))));
        }
    }

    public static class CircleTest {
        @Test
        public void toShape() {
            Circle circle;
            Shape result;

            // test 1
            circle = new Circle(new GlobalPoint(0, 0), 15);
            result = circle.toShape();
            result.print();
        }
    }

    public static class ShapeTest {
        // rewrite
        @Test
        public void inTest() {
            Shape shape;

            // Test 1
            shape = new Shape(new ArrayList<>(Arrays.asList(new Point[]{
                    new GlobalPoint(0, 0),
                    new GlobalPoint(5, 0),
                    new GlobalPoint(5, 5),
                    new GlobalPoint(0, 5)
            })));
            for (Segment s : shape.edges) {
                System.out.println(s.function);
            }
            Assert.assertTrue(shape.in(new GlobalPoint(2, 2)));
            Assert.assertFalse(shape.in(new GlobalPoint(-1, 2)));

            // Test 2
            shape = new Shape(new ArrayList<>(Arrays.asList(new Point[]{
                    new GlobalPoint(0, 0),
                    new GlobalPoint(100, 0),
                    new GlobalPoint(0, 100)
            })));
            Assert.assertTrue(shape.in(new GlobalPoint(10, 10)));
            Assert.assertFalse(shape.in(new GlobalPoint(90, 90)));
            // Test 3
        }

        // rewrite
        @Test
        public void calculateAreaTest() {
            Shape shape;

            // Test 1
            shape = new Shape(new ArrayList<>(Arrays.asList(new Point[]{
                    new GlobalPoint(0, 0),
                    new GlobalPoint(5, 0),
                    new GlobalPoint(5, 5),
                    new GlobalPoint(0, 5)
            })));
            System.out.println(shape.calculateArea());
            Assert.assertTrue(shape.calculateArea() == 25);

            // Test 2
            shape = new Shape(new ArrayList<>(Arrays.asList(new Point[]{
                    new GlobalPoint(10, 5),
                    new GlobalPoint(5, 17),
                    new GlobalPoint(13, 21),
                    new GlobalPoint(20, 16),
                    new GlobalPoint(22, 12),
                    new GlobalPoint(14, 11),
                    new GlobalPoint(10, 5)
            })));
            Assert.assertTrue((shape.calculateArea() - 130.5) < 1);
        }
    }

    public static class TriangleTest {
        @Test
        public void calculateAreaTest() {
            Triangle triangle;

            // Test 1
            triangle = new Triangle(new GlobalPoint(0, 0), new GlobalPoint(5, 0), new GlobalPoint(0, 5));
            Assert.assertTrue(triangle.calculateArea() == 12.5f);
        }
    }

    public static class RectTest {
        @Test
        public void calculateAreaTest() {
            Rect rect;

            // Test 1
            rect = new Rect(new Segment(new GlobalPoint(0, 0), new GlobalPoint(5, 5)));
            Assert.assertTrue(rect.calculateArea() == 25);

            // Test 2
            rect = new Rect(new Segment(new GlobalPoint(0, 0), new GlobalPoint(10, 5)));
            Assert.assertTrue(rect.calculateArea() == 50);
        }
    }

    public static class LineToolsTest {
        @Test
        public void findCommonPoint() {
            Line l1, l2;

            // test 1
            l1 = new Line(new GlobalPoint(3, 2), new GlobalPoint(3, 9));
            l2 = new Line(new GlobalPoint(6, 5), new GlobalPoint(15, 5));
            Assert.assertTrue(LineTools.findCommonPoint(l1, l2).equals(new GlobalPoint(3, 5)));

            // test 2
            l1 = new Line(new GlobalPoint(3, 2), new GlobalPoint(3, 9));
            l2 = new Segment(new GlobalPoint(-3, 12), new GlobalPoint(6, 18));
            Assert.assertTrue(LineTools.findCommonPoint(l1, l2).equals(new GlobalPoint(3, 16)));

            // test 3
            l1 = new Segment(new GlobalPoint(3, 2), new GlobalPoint(3, 9));
            l2 = new Line(new GlobalPoint(-3, 12), new GlobalPoint(6, 18));
            Assert.assertNull(LineTools.findCommonPoint(l1, l2));

            // test 4
            l1 = new Segment(new GlobalPoint(0, 0), new GlobalPoint(20, 0));
            l2 = new Segment(new GlobalPoint(2, 0), new GlobalPoint(18, 0));
            Assert.assertNotNull(LineTools.findCommonPoint(l1, l2));

            // test 5
            l1 = new Line(new GlobalPoint(0, 5), new GlobalPoint(20, 5));
            l2 = new Line(new GlobalPoint(2, 0), new GlobalPoint(18, 0));
            Assert.assertNull(LineTools.findCommonPoint(l1, l2));

            // test 6
            l1 = new Segment(new GlobalPoint(0, 5), new GlobalPoint(18, 0));
            l2 = new Line(new GlobalPoint(2, 0), new GlobalPoint(18, 0));
            Assert.assertTrue(LineTools.findCommonPoint(l1, l2).equals(new GlobalPoint(18, 0)));

            // test 7
            l1 = new Line(new GlobalPoint(0, 5), new GlobalPoint(20, 5));
            l2 = new Line(new GlobalPoint(2, 5), new GlobalPoint(18, 5));
            Assert.assertNotNull(LineTools.findCommonPoint(l1, l2));

            // test 8
            l1 = new Line(new GlobalPoint(5, 0), new GlobalPoint(5, 10));
            l2 = new Line(new GlobalPoint(-2, 0), new GlobalPoint(12, 14));
            Assert.assertTrue(LineTools.findCommonPoint(l1, l2).equals(new GlobalPoint(5, 7)));

            // test 9
            l1 = new Line(new GlobalPoint(5, 0), new GlobalPoint(5, 20));
            l2 = new Line(new GlobalPoint(2, 0), new GlobalPoint(2, 30));
            Assert.assertNull(LineTools.findCommonPoint(l1, l2));

            // test 10
            l1 = new Line(new GlobalPoint(15, -7.5f), new GlobalPoint(15, -18.333336f));
            l2 = new Line(new GlobalPoint(10, 5), new GlobalPoint(10, -16.333336f));
            Assert.assertNull(LineTools.findCommonPoint(l1, l2));

            // test 11
            l1 = new Segment(new GlobalPoint(15, -10), new GlobalPoint(15, 30f));
            l2 = new Line(new GlobalPoint(0, 5), Line.HORIZONTAL);
            Assert.assertTrue(LineTools.findCommonPoint(l1, l2).equals(new GlobalPoint(15, 5)));
        }

        @Test
        public void findPerpendicularsPoint() {
            Line line;
            GlobalPoint p;

            // test 1
            line = new Line(new GlobalPoint(0, 0), Line.VERTICAL);
            p = new GlobalPoint(2, 2);
            Assert.assertTrue(LineTools.findPerpendicularPoint(line, p).equals(new GlobalPoint(0, 2)));

            // test 2
            line = new Segment(new GlobalPoint(0, 0), new GlobalPoint(5, 5));
            p = new GlobalPoint(3, 1);
            Assert.assertTrue(LineTools.findPerpendicularPoint(line, p).equals(new GlobalPoint(2, 2)));
        }
    }

    public static class FiguresToolsTest {
        @Test
        public void cut() {
            Shape shape;
            Line line;
            ArrayList<Shape> result;

            // test 1
            shape = new Rect(0, 5, 5, 5);
            line = new Line(0, 0, 5, 5);
            result = FiguresTools.split(shape, line);
            System.out.println("Result");
            for (Shape s : result) {
                s.print();
            }
        }

        @Test
        public void cutDifficult() {
            Assert.assertTrue(false);
        }
    }
}



//package Geometry;
//
//import staticlibraries.SimplestClasses.Vectors2;
//import staticlibraries.SimplestClasses.*;
//import TestsClasses.*;
//import org.junit.Assert;
//import org.junit.Test;
//
//import java.util.ArrayList;
//
//class GeometryTest extends Geometry{
//    public static class LineTest{
//        @Test
//        public void findPointOfCollision(){
//            TestCase<Line, Point> testCase = new TestCase<>();
//
//            // Horizontal and vertical segments;
//            testCase.init(new Line[] {new Segment(0, 2, 10, 2), new Segment(2, 0, 2, 10)},
//                    new Point(2, 2));
//            testCase.checkWithException(Line.findCommonPoint(testCase.arguments[0], testCase.arguments[1]));
//            // Horizontal and horizontal segments
//            testCase.init(new Line[] {new Segment(0, 2, 10, 2), new Segment(9, 3, 15, 3)},
//                    null);
//            testCase.checkWithException(Line.findCommonPoint(testCase.arguments[0], testCase.arguments[1]));
//            // Horizontal and simple segments
//            testCase.init(new Line[] {new Segment(0, 2, 10, 2), new Segment(0, 2, -15, 3)},
//                    new Point(0, 2));
//            testCase.checkWithException(Line.findCommonPoint(testCase.arguments[0], testCase.arguments[1]));
//            // Horizontal and vertical segments
//            testCase.init(new Line[] {new Segment(0, 2, 10, 2), new Segment(0, 5, 0, 0)},
//                    new Point(0, 2));
//            testCase.checkWithException(Line.findCommonPoint(testCase.arguments[0], testCase.arguments[1]));
//            // Horizontal ray and vertical segment
//            testCase.init(new Line[] {new Ray(0, 2, 10, 2), new Segment(15, 3, 15, 0)},
//                    new Point(15, 10));
//            testCase.checkWithException(Line.findCommonPoint(testCase.arguments[0], testCase.arguments[1]));
//            // Horizontal ray and simple straight
//            testCase.init(new Line[] {new Ray(0, 2, 10, 2), new Straight(3, 3, 0, 0)},
//                    new Point(1, 1));
//            testCase.checkWithException(Line.findCommonPoint(testCase.arguments[0], testCase.arguments[1]));
//
//            // Perpendicular segments
//            testCase.init(new Line[] {new Segment(0, 3, 3, 0), new Segment(3, 3, 0, 0)},
//                    new Point(1, 1));
//            testCase.checkWithException(Line.findCommonPoint(testCase.arguments[0], testCase.arguments[1]));
//
//            // Simple segment and ray
//            testCase.init(new Line[] {new Segment(4, 3, 12, 4), new Ray(1, 10, 6, 5)},
//                    new Point(7, 3));
//            testCase.checkWithException(Line.findCommonPoint(testCase.arguments[0], testCase.arguments[1]));
//            // Simple segment and straight
//            testCase.init(new Line[] {new Segment(4, 3, 12, 4), new Straight(1, 10, 6, 5)},
//                    new Point(7, 3));
//            testCase.checkWithException(Line.findCommonPoint(testCase.arguments[0], testCase.arguments[1]));
//            // Simple segments
//            testCase.init(new Line[] {new Segment(4, 3, 12, 4), new Segment(1, 10, 6, 5)},
//                    new Point(7, 3));
//            testCase.checkWithException(Line.findCommonPoint(testCase.arguments[0], testCase.arguments[1]));
//            // Simple segment and vertical ray
//            testCase.init(new Line[] {new Segment(4, 3, 12, 4), new Ray(10, 12, 10, 5)},
//                    new Point(10, 3));
//            testCase.checkWithException(Line.findCommonPoint(testCase.arguments[0], testCase.arguments[1]));
//        }
//
//        @Test
//        public void rotate(){
//            TestCase<Line, Line> testCase = new TestCase<>();
//            Line line = new Straight(10, 2, 10, 5);
//            Line rotatedLine = new Straight(10, 2, 15, 2);
//            line.rotate(90);
//            Assert.assertEquals(Math.abs(line.function) < 1f, true);
//
//            line = new Ray(10, 2, 10, 5);
//            rotatedLine = new Ray(10, 2, 15, 2);
//            line.rotate(90);
//            Assert.assertEquals(line.function < 1f, true);
//
//            line = new Segment(10, 2, 10, 5);
//            rotatedLine = new Segment(10, 2, 13, 2);
//            line.rotate(90);
//            Assert.assertEquals(line.function < 1f, true);
//        }
//    }
//    public static class SegmentTest{
//        @Test
//        public void belongs(){
//            BiTestCase<Segment, Point, Boolean> biTestCase = new BiTestCase();
//            // Simple line
//            Segment segment = new Segment(6, 3, 12, 9);
//            Point point = new Point(5, 2);
//            biTestCase.init(segment, point, false);
//            biTestCase.checkWithException(segment.belongs(point));
//
//            // Simple line
//            segment = new Segment(6, 3, 12, 9);
//            point = new Point(9, 6);
//            biTestCase.init(segment, point, true);
//            biTestCase.checkWithException(segment.belongs(point));
//
//            // Simple line
//            segment = new Segment(6, 3, 12, 9);
//            point = new Point(12, 9);
//            biTestCase.init(segment, point, true);
//            biTestCase.checkWithException(segment.belongs(point));
//
//            // Vertical line
//            segment = new Segment(6, 3, 6, 9);
//            point = new Point(6, 2);
//            biTestCase.init(segment, point, false);
//            biTestCase.checkWithException(segment.belongs(point));
//
//            // Vertical line
//            segment = new Segment(6, 3, 6, 9);
//            point = new Point(6, 5);
//            biTestCase.init(segment, point, true);
//            biTestCase.checkWithException(segment.belongs(point));
//
//            // Horizontal line
//            segment = new Segment(6, 3, 12, 3);
//            point = new Point(12, 4);
//            biTestCase.init(segment, point, false);
//            biTestCase.checkWithException(segment.belongs(point));
//
//            // Horizontal line
//            segment = new Segment(6, 3, 12, 3);
//            point = new Point(8, 5);
//            biTestCase.init(segment, point, false);
//            biTestCase.checkWithException(segment.belongs(point));
//        }
//    }
//    public static class RayTest{
//        @Test
//        public void belongs(){
//            BiTestCase<Ray, Point, Boolean> biTestCase = new BiTestCase();
//            // Simple line
//            Ray ray = new Ray(6, 3, 12, 9);
//            Point point = new Point(5, 2);
//            biTestCase.init(ray, point, false);
//            biTestCase.checkWithException(ray.belongs(point));
//
//            // Simple line
//            ray = new Ray(6, 3, 12, 9);
//            point = new Point(9, 6);
//            biTestCase.init(ray, point, true);
//            biTestCase.checkWithException(ray.belongs(point));
//
//            // Simple line
//            ray = new Ray(6, 3, 12, 9);
//            point = new Point(13, 10);
//            biTestCase.init(ray, point, true);
//            biTestCase.checkWithException(ray.belongs(point));
//
//            // Vertical line
//            ray = new Ray(6, 3, 6, 9);
//            point = new Point(6, 2);
//            biTestCase.init(ray, point, false);
//            biTestCase.checkWithException(ray.belongs(point));
//
//            // Vertical line
//            ray = new Ray(6, 3, 6, 9);
//            point = new Point(6, 5);
//            biTestCase.init(ray, point, true);
//            biTestCase.checkWithException(ray.belongs(point));
//
//            // Horizontal line
//            ray = new Ray(6, 3, 12, 3);
//            point = new Point(13, 3);
//            biTestCase.init(ray, point, true);
//            biTestCase.checkWithException(ray.belongs(point));
//
//            // Horizontal line
//            ray = new Ray(6, 3, 12, 3);
//            point = new Point(8, 5);
//            biTestCase.init(ray, point, false);
//            biTestCase.checkWithException(ray.belongs(point));
//        }
//    }
//    public static class StraightTest{
//        @Test
//        public void belongs(){
//            BiTestCase<Straight, Point, Boolean> biTestCase = new BiTestCase();
//            // Simple line
//            Straight straight = new Straight(6, 3, 12, 9);
//            Point point = new Point(5, 2);
//            biTestCase.init(straight, point, true);
//            biTestCase.checkWithException(straight.belongs(point));
//
//            // Simple line
//            straight = new Straight(6, 3, 12, 9);
//            point = new Point(9, 6);
//            biTestCase.init(straight, point, true);
//            biTestCase.checkWithException(straight.belongs(point));
//
//            // Simple line
//            straight = new Straight(6, 3, 12, 9);
//            point = new Point(13, 10);
//            biTestCase.init(straight, point, true);
//            biTestCase.checkWithException(straight.belongs(point));
//
//            // Vertical line
//            straight = new Straight(6, 3, 6, 9);
//            point = new Point(6, 2);
//            biTestCase.init(straight, point, true);
//            biTestCase.checkWithException(straight.belongs(point));
//
//            // Vertical line
//            straight = new Straight(6, 3, 6, 9);
//            point = new Point(6, 5);
//            biTestCase.init(straight, point, true);
//            biTestCase.checkWithException(straight.belongs(point));
//
//            // Horizontal line
//            straight = new Straight(6, 3, 12, 3);
//            point = new Point(12, 4);
//            biTestCase.init(straight, point, true);
//            biTestCase.checkWithException(straight.belongs(point));
//
//            // Horizontal line
//            straight = new Straight(6, 3, 12, 3);
//            point = new Point(8, 5);
//            biTestCase.init(straight, point, false);
//            biTestCase.checkWithException(straight.belongs(point));
//        }
//    }
//
//    public static class CircleTest{
//        @Test
//        public void findPointOfIntersection(){
//            BiTestCase<Circle, Line, Vectors2<Point>> testCase = new BiTestCase<>();
//            Circle commonCircle = new Circle(new Point(0, 0), 10);
//
//            testCase.init(commonCircle,
//                    new Straight(-10, -4, -4, 10),
//                    new Vectors2<>(new Point(-9, -4), new Point(9, -4)));
//            testCase.checkWithException(commonCircle.findPointsOfIntersection(new Straight(-10, -4, -4, 10)));
//
//            testCase.init(commonCircle,
//                    new Segment(10, -4, 10, 8),
//                    new Vectors2<>(new Point(10, 0), new Point(10, 0)));
//            testCase.checkWithException(commonCircle.findPointsOfIntersection(new Segment(10, -4, 10, 8)));
//
//            testCase.init(commonCircle,
//                    new Segment(3, 13, 9, -11),
//                    new Vectors2<>(new Point(4, 9), new Point(7, 8)));
//            testCase.checkWithException(commonCircle.findPointsOfIntersection(new Segment(3, 13, 9, -11)));
//
//            testCase.init(commonCircle,
//                    new Ray(4, -2, 6, 7),
//                    new Vectors2<>(null, new Point(6, 7)));
//            testCase.checkWithException(commonCircle.findPointsOfIntersection(new Ray(4, -2, 6, 7)));
//
//            testCase.init(commonCircle,
//                    new Ray(-3, -1, -7, 9),
//                    new Vectors2<>(null, new Point(-6, 7)));
//            testCase.checkWithException(commonCircle.findPointsOfIntersection(new Ray(-3, -1, -7, 9)));
//
//            testCase.init(commonCircle,
//                    new Segment(-2, -8, -6, -17),
//                    new Vectors2<>(null, null));
//            testCase.checkWithException(commonCircle.findPointsOfIntersection(new Segment(-2, -8, -6, -17)));
//
//            testCase.init(commonCircle,
//                    new Ray(-1, 12, 2, 15),
//                    new Vectors2<>(null, null));
//            testCase.checkWithException(commonCircle.findPointsOfIntersection(new Ray(-1, 12, 2, 15)));
//
//            testCase.init(commonCircle,
//                    new Straight(1, 15, 12, 2),
//                    new Vectors2<>(null, null));
//            testCase.checkWithException(commonCircle.findPointsOfIntersection(new Straight(1, 15, 12, 2)));
//        }
//    }
//    public static class ShapeTest {
//        @Test
//        public void inTest(){
//            ArrayList<Point> edges = new ArrayList<>();
//            edges.add(new Point(0, 2));
//            edges.add(new Point(5, 5));
//            edges.add(new Point(7, 3));
//            Assert.assertTrue(Shape.in(new Shape(edges)));
//        }
//
//        @Test
//        public void cutTest(){
//            ArrayList<Point> edges = new ArrayList<>();
//            edges.add(new Point(4, 24)); edges.add(new Point(10, 27));
//            edges.add(new Point(10, 21));
//            ArrayList<Shape> shapes = new Shape(edges).cut(new Segment(10, 27, 10, 21));
//            for (int i = 0; i < shapes.size(); i++) {
//                shapes.get(i).print();
//                System.out.println();
//            }
//        }
//        @Test
//        public void isConvexTest(){
//            ArrayList<Point> edges = new ArrayList<>();
//            edges.add(new Point(-4, 7)); edges.add(new Point(-1, 4));
//            edges.add(new Point(4, 6)); edges.add(new Point(5, 10));
//            edges.add(new Point(2, 13)); edges.add(new Point(-3, 12));
//            Assert.assertTrue(Shape.isConvex(new Shape(edges)));
//
//            edges = new ArrayList<>();
//            edges.add(new Point(-4, 7)); edges.add(new Point(-1, 4));
//            edges.add(new Point(1, 8)); edges.add(new Point(5, 10));
//            edges.add(new Point(2, 13)); edges.add(new Point(3, 12));
//            Assert.assertFalse(Shape.isConvex(new Shape(edges)));
//        }
//        @Test
//        public void calculateSTest(){
//            ArrayList<Point> edges = new ArrayList<>();
//            edges.add(new Point(-4, 7)); edges.add(new Point(-1, 4));
//            edges.add(new Point(4, 6)); edges.add(new Point(5, 10));
//            edges.add(new Point(2, 13)); edges.add(new Point(-3, 12));
//            Assert.assertTrue(Math.abs(new Shape(edges).calculateS() - 67) < 1);
//
//            edges = new ArrayList<>();
//            edges.add(new Point(-4, 7)); edges.add(new Point(-1, 4));
//            edges.add(new Point(1, 8)); edges.add(new Point(5, 10));
//            edges.add(new Point(2, 13)); edges.add(new Point(-3, 12));
//            Assert.assertTrue(Math.abs(new Shape(edges).calculateS() - 60.5) < 1);
//        }
//    }
//
//    public static class TriangleTest{
//        @Test
//        public void calculateSTest(){
//            Triangle triangle = new Triangle(new Point(6, 1), new Point(12, 1), new Point(6, 7));
//            Assert.assertTrue(Math.abs(triangle.calculateS() - 18) < 1);
//
//        }
//    }
//}
//
//
//
//
//
//
//
//
//
///*
//package Geometry;
//
//import org.junit.Test;
//import org.junit.experimental.runners.Enclosed;
//import org.junit.runner.RunWith;
//
//import static org.junit.Assert.*;
//
//@RunWith(Enclosed.class)
//public class GeometryTest extends Geometry {
//   public static class testLine {
//        private final TestCase<Segment>[] testsCases = new TestCase[]{
//                //Parallel
//                new TestCase<Segment>(new Segment(1, 0, 1, 10), new Segment(5, 0, 5, 10),
//                        null),
//                new TestCase<Segment>(new Segment(5, 3, 20, 3), new Segment(0, 3, 30, 3),
//                        null),
//                new TestCase<Segment>(new Segment(5, 100, 20, 5), new Segment(10, 100, 30, 5),
//                        null),
//                //Perpendiculars
//                new TestCase<Segment>(new Segment(0, 3, 3, 0), new Segment(3, 3, 0, 0),
//                        new Point(1, 1)),
//                new TestCase<Segment>(new Segment(-10, -10, 20, 20), new Segment(10, -10, -20, 20),
//                        new Point(0, 0)),
//                //SimpleLines
//                new TestCase<Segment>(new Segment(-10, -10, 15, 20), new Segment(5, 1, 20, 30),
//                        new Point(14, 19)),
//                new TestCase<Segment>(new Segment(0, 10, 50, 10), new Segment(25, 5, 50, 15),
//                        new Point(37, 10)),
//                new TestCase<Segment>(new Segment(0, -10, -50, -10), new Segment(-25, -5, -25, -15),
//                        new Point(-25, -10)),
//                new TestCase<Segment>(new Segment(10, -5, 30, 15), new Segment(15, -5, 25, 10),
//                        new Point(25, 10)),
//                new TestCase<Segment>(new Segment(10, 10, 50, 30), new Segment(10, 10, 40, 30),
//                        new Point(10, 10)),
//                new TestCase<Segment>(new Segment(0, 10, 50, 10), new Segment(25, 5, 25, 15),
//                        new Point(25, 10)),
//                //uncolliding
//                new TestCase<Segment>(new Segment(0, 0, 50, 0), new Segment(0, 5, 50, 5), null),
//                new TestCase<Segment>(new Segment(0, 0, 50, 20), new Segment(0, 5, 50, 21),
//
//                        null),
//                new TestCase<Segment>(new Segment(1, 1, 3, 7), new Segment(2, 2, 4, 6),
//                        null)
//        };
//        private TestCase<Segment>[] perpendiculars = new TestCase[]{
//                new TestCase<Segment>(new Segment(0, 3, 3, 0), new Segment(3, 3, 0, 0),
//                        new Point(1, 1)),
//                new TestCase<Segment>(new Segment(-10, -10, 20, 20), new Segment(10, -10, -20, 20),
//                        new Point(0, 0)),
//        };
//        @Test
//        public void createPerpendicular() {
//            for (TestCase<Segment> testCase : perpendiculars) {
//                if (isUncorrectedPerpendicular(new Segment(testCase.line1.createPerpendicular(testCase.line2.getStart())),
//                        testCase.line2.getStart(), testCase.line2)) {
//                    testCase.line1.print();
//                    // new Segment(testCase.line1.createPerpendicular(testCase.line2.getStart())).print();
//                    System.out.println("Start");
//                    breakRuntime();
//                }
//                if (isUncorrectedPerpendicular(new Segment(testCase.line1.createPerpendicular(testCase.line2.getEnd())),
//                        testCase.line2.getEnd(), testCase.line2)) {
//                    testCase.line1.print();
//                    // new Segment(testCase.line1.createPerpendicular(testCase.line2.getEnd())).print();
//                    System.out.println("End");
//                    breakRuntime();
//                }
//            }
//        }
//        private boolean isUncorrectedPerpendicular(Line testLine, Point point, Segment perpendicular) {
//            return !((testLine.belongs(perpendicular.getHeadPoint()) || testLine.belongs(perpendicular.getEnd()))
//                    && (approximatelyEqual(perpendicular.getHeadPoint(), point) || approximatelyEqual(perpendicular.getEnd(), point)));
//        }
//
//        @Test
//        public void findCommonPoint() {
//            for (TestCase<Segment> testCase : testsCases)
//                if (!approximatelyEqual(Line.findPointOfCollision(testCase.line1, testCase.line2),
//                        testCase.pointOfColliding))
//                    breakRuntime();
//        }
//        private Geometry.Line createSegment(float p1x, float p1y, float p2x, float p2y){
//            return new Geometry.Segment(new Point((int) p1x, (int) p1y), new Point((int) p2x, (int) p2y));
//        }
//
//        class TestCase<Obj>{
//            public  Obj line1, line2;
//            Point pointOfColliding;
//
//            TestCase(Obj line1, Obj line2, Point pointOfColliding){
//                this.line1 = line1;
//                this.line2 = line2;
//                this.pointOfColliding = pointOfColliding;
//            }
//        }
//    }
//
//    public static boolean approximatelyEqual(Point verifiable, Point right){
//        if (verifiable != null && right != null) System.out.println("test: " + right.x + " " + right.y + " verifiable: " + verifiable.x + " " + verifiable.y);
//        if (right == null && verifiable == null || right != null && verifiable != null &&
//                right.x == verifiable.x && right.y == verifiable.y)
//            return true;
//        return false;
//    }
//    public static void breakRuntime(){
//        assertNotNull(null);
//    }
//    public static void breakRuntime(int i){
//       System.out.println(i);
//       breakRuntime();
//    }
//*/