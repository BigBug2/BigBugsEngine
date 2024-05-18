package engine.controllers.model;

import libraries.geometry.NewGeometry.*;
import libraries.geometry.SGeometry.*;

import engine.objects.renderable.actors.ActiveActor;
import libraries.other.Triple;

import java.util.ArrayList;

public class CollisionsController {
    private static ArrayList<Triple<ActiveActor, Figure, SRect>> actors = new ArrayList<>();

    public static void add(ActiveActor actor) {
        actors.add(new Triple<>(actor, actor.getBody(), new SRect(actor.getBody().findPackingDiagonal())));
    }

    static void process() {
        for (var t : actors) {
            t.f.move();
        }
        for (int i = 0; i < actors.size(); i++) {
            for (int j = i + 1; j < actors.size(); j++) {
                if (actors.get(i).t.colliding(actors.get(j).t) &&
                        FiguresTools.areColliding(actors.get(i).s, actors.get(j).s)) {
                    actors.get(i).f.reflect(actors.get(j).f);
                    actors.get(j).f.reflect(actors.get(i).f);
                }
            }
        }

        actors.clear();
    }
    /*
    private static void process(ActiveActor actor1, ActiveActor actor2) {
        if (actor1 instanceof Bullet && actor2 instanceof Player || actor1 instanceof Player && actor2 instanceof Bullet) {
            return;
        }
        Triple<Line, GlobalPoint, Float> actor1Depth, actor2Depth;
        Vector movementForce1 = actor1.getMovementForce(), movementForce2 = actor2.getMovementForce();
        if (actor1.getBody() instanceof Shape) {
            actor1Depth = getDepth((Shape) actor1.getBody(),
                    actor2.getBody(), movementForce1.coordinate());
            if (actor2.getBody() instanceof Shape) {
                actor2Depth = getDepth((Shape) actor2.getBody(),
                        actor1.getBody(), movementForce1.coordinate());
            } else {
                actor2Depth = getDepth((Circle) actor2.getBody(),
                        (Shape) actor1.getBody(), movementForce1.coordinate());
            }
        } else {
            if (actor2.getBody() instanceof Shape) {
                actor1Depth = getDepth((Circle) actor1.getBody(),
                        (Shape) actor2.getBody(), movementForce1.coordinate());
                actor2Depth = getDepth((Shape) actor2.getBody(), actor1.getBody(),
                        movementForce1.coordinate());
            } else {
                actor1Depth = getDepth((Circle) actor1.getBody(), (Circle) actor2.getBody(), movementForce1.coordinate());
                actor2Depth = getDepth((Circle) actor2.getBody(), (Circle) actor1.getBody(), movementForce2.coordinate());
            }
        }
        reflect(actor1, actor2, actor1Depth, movementForce1.coordinate());
        reflect(actor1, actor2, actor2Depth, movementForce2.coordinate());

        actor1.move(new Vector(Vector.getOpposite(movementForce1).coordinate(), actor1Depth.t));
        actor2.move(new Vector(Vector.getOpposite(movementForce2).coordinate(), actor2Depth.t));

        //actor1.push(new Vector(new GlobalPoint(0, 1), actor1.getMass() * Physics.g), actor1.location());
        //actor2.push(new Vector(new GlobalPoint(0, 1), actor2.getMass() * Physics.g), actor2.location());
    }

    private static Triple<Line, GlobalPoint, Float> getDepth(Shape s, Figure f, Point movementForce) {
        Triple<Line, GlobalPoint, Float> result = new Triple<>(null, null, -1f);
        for (int i = 0; i < s.countOfEdges(); i++) {
            ArrayList<Pair<Segment, GlobalPoint>> collisions;
            collisions = FiguresTools.findPlaces(f instanceof Shape ? (Shape) f : ((Circle) f).toShape(),
                    new Ray(s.getVertex(i), new GlobalPoint(s.getVertex(i).getX() - movementForce.getX(),
                            s.getVertex(i).getY() - movementForce.getY())));
            Triple<Line, GlobalPoint, Float> tmp = new Triple<>(null, null, -1f);
            for (var collision : collisions) {
                float tmpDepth = new Segment(s.getVertex(i), collision.s).length();
                if (tmpDepth < tmp.t || tmp.t == -1) {
                    tmp.f = collision.f;
                    tmp.s = collision.s;
                    tmp.t = tmpDepth;
                }
            }
            if (tmp.t > result.t) {
                result = tmp;
            }
        }
        return result;
    }
    private static Triple<Line, GlobalPoint, Float> getDepth(Circle c, Shape s, Point movementForce) {
        Ray ray = new Ray(c.centre, new GlobalPoint(c.centre.getX() + movementForce.getX(),
                c.centre.getY() + movementForce.getY()));

        Triple<Line, GlobalPoint, Float> result = new Triple<>(null, null, -1f);
        for (Pair<Segment, GlobalPoint> collision : FiguresTools.findPlaces(s, ray)) {
            float tmp = c.radius - new Segment(c.centre, collision.s).length();
            if (tmp < result.t || result.t == -1) {
                result.t = tmp;
                result.f = collision.f;
                result.s = collision.s;
            }
        }
        return result;
    }
    private static Triple<Line, GlobalPoint, Float> getDepth(Circle c1, Circle c2, Point movementForce) {
        ArrayList<GlobalPoint> collisions = FiguresTools.findIntersections(c1,c2);
        Line line = collisions.isEmpty() ? null : new Line(collisions.get(0), collisions.get(collisions.size() - 1));
        return new Triple<>(line, new GlobalPoint(new Segment(new Ray(c1.centre, c2.centre), c1.radius).guidePoint),
                Math.max(c1.radius + c2.radius - new Segment(c1.centre, c2.centre).length(), 0));
    }

    private static void reflect(ActiveActor actor1, ActiveActor actor2, Triple<Line, GlobalPoint, Float> depth, Point mCoord) {
        if (depth.t != -1) {
            actor1.move(Vector.getOpposite(new Vector(mCoord, depth.t)));
            GlobalPoint globalCoord = LineTools.findCommonPoint(new Line(LineTools.createPerpendicular(depth.f, actor1.location())),
                    LineTools.createParallel(depth.f, new GlobalPoint(actor1.location().getX() + mCoord.getX(),
                            actor1.location().getY() + mCoord.getY())));
            if (globalCoord != null) {
                Vector punch = new Vector(new GlobalPoint(globalCoord.getX() - actor1.location().getX(),
                        globalCoord.getY() - actor1.location().getY()));
                actor2.push(punch, depth.s);
                actor1.push(Vector.getOpposite(punch), depth.s);
            }
            //actor1.push(Vector.getOpposite(actor1.getMovementForce()), actor1.location());
        }
    }
    */
}
