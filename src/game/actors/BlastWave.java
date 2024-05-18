package game.actors;

import engine.controllers.model.Loader;
import engine.objects.renderable.actors.ActiveActor;
import engine.objects.renderable.actors.Creating;
import engine.objects.supports.AnimationsPack;
import libraries.geometry.NewGeometry.*;
import libraries.geometry.SGeometry.*;
import libraries.physics.Physics;

public class BlastWave extends ActiveActor {
    private static final float EXPANSION_COEFFICIENT = 1.007f;

    private int damage = 50;

    private final AnimationsPack animations;
    private final long targetTime;

    public BlastWave(Figure body) {
        super("fire", body, Physics.EMPTY);
        fixedPlace = true;

        animations = Loader.loadAnimations(this);
        targetTime = System.nanoTime() + animations.getPlayingTime();
    }

    @Override
    public void process(ActiveActor actor) {
        if (System.nanoTime() > targetTime) {
            destroy();
            return;
        }
        if (actor instanceof Creating) {
            ((Creating) actor).addHealth(-damage);
        }
        damage /= EXPANSION_COEFFICIENT;


        Segment s = getBody().findPackingDiagonal();
        float width = (s.guidePoint.getX() - s.headPoint.getX()), height = (s.guidePoint.getY() - s.headPoint.getY());
        setBody(getBody().packInto(new SRect(s.headPoint.getX() - (width * EXPANSION_COEFFICIENT - width) / 2,
                s.guidePoint.getY() + (height * EXPANSION_COEFFICIENT - height) / 2,
                width * EXPANSION_COEFFICIENT, height * EXPANSION_COEFFICIENT)));
    }

    @Override
    public void render() {
        animations.process();
        super.render();
    }
}
