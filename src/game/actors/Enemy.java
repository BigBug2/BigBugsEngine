package game.actors;

import engine.controllers.model.Loader;
import engine.objects.renderable.actors.ActiveActor;
import engine.objects.renderable.actors.Creating;
import engine.objects.supports.AnimationsPack;
import game.GSource;
import libraries.geometry.NewGeometry.*;
import libraries.physics.Physics;

public class Enemy extends Creating {
    public static final int DAMAGE = 5;

    private final Player player;
    private final GSource game;

    public Enemy(Figure body, Player player, GSource game) {
        super("enemy", body, Physics.FLESH, 50);

        this.player = player;
        this.game = game;

        setMaxSpeed(9.5f);
    }
    @Override
    public void process() {
        super.process();

        push(Vector.getOpposite(new Vector(new GlobalPoint(location().getX() - player.location().getX(),
                location().getY() - player.location().getY()), 100)), location());
    }

    @Override
    public void process(ActiveActor actor) {
        if (actor instanceof Player) {
            ((Player) actor).addHealth(-DAMAGE);
        }
    }
    @Override
    public void destroy() {
        super.destroy();

        Coin coin = new Coin(getBody());
        game.OBJECTS.add(Loader.loadAnimations(coin));
        game.OBJECTS.add(coin);
    }
}
