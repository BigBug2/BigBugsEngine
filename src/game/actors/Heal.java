package game.actors;

import engine.objects.renderable.actors.ActiveActor;
import engine.objects.renderable.actors.Creating;
import libraries.geometry.NewGeometry;
import libraries.physics.Physics;

public class Heal extends Creating {
    private static final int heal = 10;

    public Heal(NewGeometry.Figure body) {
        super("heal", body, Physics.FLESH, 1);

        fixedPlace = true;
    }
    @Override
    public void reflect(ActiveActor actor) {
        process(actor);
    }

    @Override
    public void process(ActiveActor actor) {
        if (actor instanceof Creating) {
            ((Creating) actor).addHealth(heal);
        }
    }
}
