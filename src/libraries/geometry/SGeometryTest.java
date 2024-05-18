package libraries.geometry;

import org.junit.Assert;
import org.junit.Test;

public class SGeometryTest extends SGeometry {
    public static class SRectTest {
        @Test
        public void inTest() {
            NewGeometry.Point point;
            SRect hitBox;

            // Test 1
            hitBox = new SRect(0, 5, 5, 5);
            point = new NewGeometry.GlobalPoint(2, 2);
            Assert.assertTrue(hitBox.in(point));

            // Test 2
            point = new NewGeometry.GlobalPoint(5, 5);
            Assert.assertTrue(hitBox.in(point));

            // Test 3
            point = new NewGeometry.GlobalPoint(0, 0);
            Assert.assertTrue(hitBox.in(point));

            // Test 4
            point = new NewGeometry.GlobalPoint(6, 2);
            Assert.assertFalse(hitBox.in(point));

            // Test 5
            point = new NewGeometry.GlobalPoint(2, 6);
            Assert.assertFalse(hitBox.in(point));

            // Test 6
            point = new NewGeometry.GlobalPoint(2, -1);
            Assert.assertFalse(hitBox.in(point));

            // Test 7
            hitBox = new SRect(-50, 0, 50, 50);
            point = new NewGeometry.GlobalPoint(1, 49);
            Assert.assertFalse(hitBox.in(point));
        }

        @Test
        public void collidingTest() {
            SRect r1, r2;

            // Test 1
            r1 = new SRect(0, 20, 50, 20);
            r2 = new SRect(25, 20, 50, 20);
            Assert.assertTrue(r1.colliding(r2));

            // Test 2
            r1 = new SRect(0, 20, 50, 20);
            r2 = new SRect(0, 10, 20, 30);
            Assert.assertTrue(r1.colliding(r2));

            // Test 3
            r1 = new SRect(0, 0, 40, 20);
            r2 = new SRect(10, -10, 20, 20);
            Assert.assertTrue(r1.colliding(r2));

            // Test 4
            r1 = new SRect(0, 20, 20, 20);
            r2 = new SRect(10, 30, 20, 20);
            Assert.assertTrue(r1.colliding(r2));

            // Test 5
            r1 = new SRect(0, 20, 20, 20);
            r2 = new SRect(-10, 10, 10, 2);
            Assert.assertTrue(r1.colliding(r2));

            // Test 6
            r1 = new SRect(0, 20, 20, 20);
            r2 = new SRect(-10, 10, 5, 2);
            Assert.assertFalse(r1.colliding(r2));

            // Test 7
            r1 = new SRect(0, 20, 20, 20);
            r2 = new SRect(10, 10, 5, 5);
            Assert.assertTrue(r1.colliding(r2));

            // Test 8
            r1 = new SRect(0, 20, 20, 20);
            r2 = new SRect(5, 15, 5, 5);
            Assert.assertTrue(r1.colliding(r2));

            // Test 9
            r1 = new SRect(0, 20, 30, 20);
            r2 = new SRect(0, 15, 5, 5);
            Assert.assertTrue(r1.colliding(r2));
        }
    }
}
