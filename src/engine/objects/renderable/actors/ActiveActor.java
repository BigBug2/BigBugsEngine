package engine.objects.renderable.actors;

import java.util.ArrayList;

import engine.controllers.model.CollisionsController;
import libraries.geometry.NewGeometry.*;
import libraries.other.Pair;
import libraries.other.Triple;
import libraries.physics.Physical;
import libraries.physics.Physics;


public abstract class ActiveActor extends Actor {
    // force
    private Vector R = new Vector();

    // variables of movement
    protected boolean fixedPlace = false;
    private Vector speed = new Vector();
    private float maxSpeed = Float.POSITIVE_INFINITY;

    // physical values
    private final Physics.Material material;
    private float s, m;


    public ActiveActor(String name, Figure body, Physics.Material material) {
        super(name, body);

        this.material = material;

        s = body.calculateArea();
        m = s * material.p;

    }

    public void update() {
        super.update();
        s = getBody().calculateArea();
        m = s * material.p;
    }
    @Override
    public void process() {
        CollisionsController.add(this);
    }

    public void reflect(ActiveActor actor) {
        Shape s1 = getBody() instanceof Shape ? (Shape) getBody() : ((Circle) getBody()).toShape(36),
                s2 = actor.getBody() instanceof Shape ? (Shape) actor.getBody() : ((Circle) actor.getBody()).toShape(36);

        // direction coord
        Point dCoord = actor.getSpeedVector().coordinate();

        // place, vertex, depth of immersion
        Triple<Segment, Point, Float> max = new Triple<>(null, null, -1f);
        for (Point vertex : s2.getVertexes()) {
            Pair<Segment, Float> min = new Pair<>(null, Float.MAX_VALUE);
            for (var p : FiguresTools.findPlaces(s1, new Ray(vertex,
                    new GlobalPoint(vertex.getX() + dCoord.getX(), vertex.getY() + dCoord.getY())))) {
                float depth = new Segment(p.s, vertex).length();
                if (depth < min.s) {
                    min.f = p.f;
                    min.s = depth;
                }
            }

            if (min.f != null && min.s > max.t) {
                max.f = min.f;
                max.s = vertex;
                max.t = min.s;
            }
        }
        Vector mForce = actor.getMovementForce(max.t);
        Point mCoord = mForce.coordinate();
        if (max.f != null) {

            Point coord = LineTools.findCommonPoint(new Line(LineTools.createPerpendicular(max.f, actor.location())),
                    LineTools.createParallel(max.f, new GlobalPoint(actor.location().getX() + mCoord.getX(), actor.location().getY() + mCoord.getY())));

            if (coord != null) {
                System.out.println(getName() + " hit " + actor.getName() + " using " + new Vector(new GlobalPoint(coord.getX() - location().getX(), coord.getY() - location().getY())).mod + "Н");
                actor.push(Vector.getMultiplied(new Vector(new GlobalPoint(coord.getX() - location().getX(), coord.getY() - location().getY())), -2), actor.location());
            }

            actor.move(new Vector(Vector.getOpposite(mForce).coordinate(), max.t));
        }

        process(actor);
    }

    public abstract void process(ActiveActor actor);

    public void move() {
        if (fixedPlace) {
            return;
        }
        float timeS = (float) ((double) getFrameTime() / Physics.SECOND);
        R = Vector.sum(R, new Vector());
        Vector tmp = Vector.sum(speed, Vector.getMultiplied(R, 1f / m * timeS));
        if (tmp.mod != 0) {
            speed = new Vector(new Segment(new Ray(new GlobalPoint(), tmp.coordinate()), Math.min(maxSpeed,
                    tmp.mod)).guidePoint);
        }
        Point coord = speed.coordinate();
        if (coord.getX() != 0 || coord.getY() != 0) {
            location().addX(coord.getX() * timeS);
            location().addY(coord.getY() * timeS);

            updateBody();
        }
        // is falling slower then force or is rising
        //R = new Vector(new GlobalPoint(0, -m * Physics.g));

        R = new Vector();
    }

    public void push(Vector force, Point from) {
        R = Vector.sum(R, force);
    }


    public Vector getR() {
        return R;
    }

    public Vector getSpeedVector() {
        return speed;
    }

    public void setMaxSpeed(float v) {
        maxSpeed = v;
    }

    public Vector getMovementForce(float s) {
        if (fixedPlace) {
            return new Vector();
        }
        return Vector.sum(R, new Vector(speed.coordinate(), getMass() * speed.mod * speed.mod / (2 * s)));
    }

    public Vector getRotationMoment() {
        return null;
    }

    public float getMass() {
        return m;
    }
    public float getS() {
        return s;
    }

    protected void move(Vector direction) {
        if (fixedPlace) {
            return;
        }

        Point coord = direction.coordinate();
        location().addX(coord.getX());
        location().addY(coord.getY());
    }
}