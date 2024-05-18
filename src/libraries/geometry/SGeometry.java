package libraries.geometry;

import libraries.geometry.NewGeometry.*;

public class SGeometry {
    public static class SRect {
        public float x, y;
        public float width, height;

        public SRect(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        public SRect(NewGeometry.Segment diagonal) {
            x = Math.min(diagonal.headPoint.getX(), diagonal.guidePoint.getX());
            y = Math.max(diagonal.headPoint.getY(), diagonal.guidePoint.getY());

            width = Math.abs(diagonal.guidePoint.getX() - diagonal.headPoint.getX());
            height = Math.abs(diagonal.guidePoint.getY() - diagonal.headPoint.getY());
        }

        public boolean in(Point p) {
            return p.getX() >= x && p.getX() <= x + width &&
                    p.getY() >= y - height && p.getY() <= y;
        }
        public boolean colliding(Segment s) {
            return colliding(new SRect(s));
        }

        public boolean colliding(SRect other) {
            return other.x <= x + width && other.x + other.width >= x &&
                    other.y - other.height <= y && other.y >= y - height;
        }

        public String toString() {
            return x + " " + y + "\nwidth" + width + " height:" + height;
        }
    }
}
