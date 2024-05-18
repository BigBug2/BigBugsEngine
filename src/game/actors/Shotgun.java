package game.actors;

import engine.controllers.model.Loader;
import engine.managers.client.InputManager;
import game.GSource;
import engine.objects.renderable.packages.GameModule;
import engine.objects.supports.AnimationsPack;
import libraries.geometry.NewGeometry.*;
import libraries.physics.Physics;

import java.util.Random;

public class Shotgun extends Ability {
    public static final int FIRE = 1;
    private static final int FRACTION = 10;

    private final AnimationsPack animation;

    private final GameModule game;

    public Shotgun(Figure body, GSource game) {
        super("gun", body, (a) -> {
            return InputManager.getKeyState(' ').length >= 1;
        }, Physics.SECOND / 2, 4, Physics.SECOND * 4);

        tie(game.getPlayer().location());
        animation = Loader.loadAnimations(this);

        this.game = game;
    }
    @Override
    public void process() {
        super.process();
        animation.process();
    }

    @Override
    public void use() {
        Point mouse = InputManager.getLastMouseLocation(game.camera);
        Ray direction = new Ray(location().copy(), mouse);
        for (int angle = -45, count = 0; count < FRACTION && angle <= 45; angle += Math.abs(new Random().nextInt()) % 10 + 5, count++) {
            Line tmp = direction.copy(); tmp.rotate(angle);
            Point offset = new Vector(new GlobalPoint(tmp.guidePoint.getX() - location().getX(), tmp.guidePoint.getY() - location().getY()), 2).coordinate();

            game.OBJECTS.add(new Bullet(new Rect(location().getX() + offset.getX() - 1, location().getY() + offset.getY(), 0.5f, 0.2f),
                    new Vector(new GlobalPoint(tmp.guidePoint.getX() - location().getX(), tmp.guidePoint.getY() - location().getY()), 1000)));
        }
    }
}

/*

 */