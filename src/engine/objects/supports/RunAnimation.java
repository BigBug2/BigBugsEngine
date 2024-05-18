package engine.objects.supports;

import engine.objects.GameObject;
import libraries.geometry.NewGeometry.*;
import libraries.geometry.NewGeometry.Point;


public class RunAnimation extends Support {
    // configuration variables
    private final Point point;
    private final Point to;
    private final float speed;

    // test direction variable
    private boolean direction;

    public RunAnimation(Point p, Point to, float speed) {
        super(new OPoint(p));

        point = p;
        this.to = to;
        this.speed = speed;

        direction = new Ray(point, to).getDirection();
    }

    @Override
    public void process() {
        if (new Ray(point, to).getDirection() != direction) {
            setProcessable(false);
            return;
        }
        Point target = new Segment(new Ray(point, to), speed * getFrameTime()).guidePoint;
        point.setX(target.getX());
        point.setY(target.getY());
    }

    private static class OPoint extends GameObject{
        public Point p;

        public OPoint(Point p) {
            this.p = p;
        }

        @Override
        public void process() {}
    }
}
