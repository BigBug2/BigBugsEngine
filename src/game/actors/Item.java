package game.actors;

import engine.objects.renderable.actors.ActiveActor;
import engine.objects.renderable.actors.Actor;
import libraries.geometry.NewGeometry.*;
import libraries.physics.Physics;

public class Item extends ActiveActor {
    protected Item(String name, Figure body) {
        super(name, body, Physics.IRON);
        setMaxSpeed(0);
        fixedPlace = true;
    }

    // make ghosting (don't throw any object)
    @Override
    public void reflect(ActiveActor actor) {}

    @Override
    public void process(ActiveActor actor) {}

    public void pick() {
        destroy();
    }
}
