package engine.objects.renderable.actors;

import engine.objects.renderable.Renderable;
import libraries.geometry.NewGeometry.*;
import libraries.geometry.SGeometry.*;
import libraries.graphics.Graphics;

public abstract class Actor extends Renderable {
    private Figure body;
    private AnchorPoint location = new AnchorPoint(0, 0);

    public Actor(String name, Figure body) {
        super(name);
        setBody(body);
    }

    public void updateBody() {
        body.update();
    }

    public Figure getBody() {
        return body;
    }
    public void setBody(Figure body) {
        if (body == null) {
            return;
        }
        this.body = body;
        Point c = body.findCentre();
        if (c == null) {
            c = this.body.findCentre();
        }
        location.setX(c.getX());
        location.setY(c.getY());
        this.body.tie(location);
    }

    public SRect getHitBox() {
        return new SRect(body.findPackingDiagonal());
    }

    @Override
    public AnchorPoint location() {
        return location;
    }
    public void setLocation(AnchorPoint point) {
        location = point;
        body.tie(location);
    }

    public void tie(Point p) {
        location.tie(p);
    }
    public void untie() {
        location.tie(new GlobalPoint());
    }

    @Override
    public void render() {
        Graphics.draw(body, getTexture());
    }
}
