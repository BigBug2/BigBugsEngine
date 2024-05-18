package game.actors;

import engine.objects.renderable.actors.ActiveActor;
import engine.objects.renderable.actors.Creating;
import libraries.geometry.NewGeometry;
import libraries.physics.Physics;

public class Spike extends Creating {
    private static final int DAMAGE = 10;
    private long sleep = Physics.SECOND;

    public Spike(NewGeometry.Figure body) {
        super(Physics.IRON.name, body, Physics.IRON, 30);

        fixedPlace = true;
    }

    @Override
    public void process(ActiveActor actor) {
        if (sleep > 0) {
            sleep -= getFrameTime();
        } else {
            if (actor instanceof Creating) {
                ((Creating) actor).addHealth(-DAMAGE);
            }
        }
    }
}
