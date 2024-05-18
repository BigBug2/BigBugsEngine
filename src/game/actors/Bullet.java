package game.actors;

import engine.objects.renderable.actors.ActiveActor;
import engine.objects.renderable.actors.Creating;
import libraries.geometry.NewGeometry.*;
import libraries.physics.Physics;

public class Bullet extends ActiveActor {
    int health = 3;

    private final long time;

    private final Vector force;

    public Bullet(Figure body, Vector force) {
        super("iron", body, Physics.IRON);

        this.force = force;
        time = System.nanoTime();

        setMaxSpeed(20);
    }

    @Override
    public void process() {
        super.process();
        push(force, location());
    }

    @Override
    public void reflect(ActiveActor actor) {
        if (System.nanoTime() - time > Physics.SECOND * 0.1) {
            super.reflect(actor);
        }
    }

    @Override
    public void process(ActiveActor actor) {
        if (actor instanceof Creating) {
            ((Creating) actor).addHealth(-10);
        }
        health -= 1;
        if (health <= 0) {
           destroy();
        }
    }
}
