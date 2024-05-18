package game.actors;

import engine.controllers.model.CollisionsController;
import engine.controllers.model.Loader;
import engine.objects.renderable.actors.Actor;
import engine.objects.supports.AnimationsPack;
import game.GSource;
import libraries.geometry.NewGeometry.*;

public class Rocket extends Actor {
    private long targetTime;
    private final AnimationsPack animations;

    private GSource game;

    public Rocket(Figure body, GSource game) {
        super("rocket", body);

        this.game = game;

        animations = Loader.loadAnimations(this);
        targetTime = System.nanoTime() + animations.getPlayingTime();
    }

    @Override
    public void process() {
        if (System.nanoTime() >= targetTime) {
            game.OBJECTS.add(new BlastWave(new Circle(location(), 2)));
            destroy();
        }
    }
    @Override
    public void render() {
        animations.process();
        super.render();
    }
}
