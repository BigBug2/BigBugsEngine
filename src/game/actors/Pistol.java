package game.actors;

import engine.controllers.model.Loader;
import engine.managers.client.InputManager;
import engine.objects.renderable.packages.GameModule;
import engine.objects.supports.AnimationsPack;
import libraries.geometry.NewGeometry.*;
import libraries.physics.Physics;

public class Pistol extends Ability {
    public static final int FIRE = 1;

    private final AnimationsPack animation;

    private final GameModule module;

    public Pistol(Figure body, Player player, GameModule module) {
        super("gun", body, (a) -> {
            return InputManager.getKeyState(' ').length >= 1;
        }, Physics.SECOND / 10, 10, Physics.SECOND * 2);

        tie(player.location());
        animation = Loader.loadAnimations(this);

        this.module = module;
    }
    @Override
    public void process() {
        super.process();
        animation.process();
    }

    @Override
    public void use() {
        Point mouse = InputManager.getLastMouseLocation(module.camera);
        if (mouse != null) {
            Point offset = new Vector(new GlobalPoint(mouse.getX() - location().getX(), mouse.getY() - location().getY()), 2).coordinate();
            module.OBJECTS.add(new Bullet(new Rect(location().getX() + offset.getX() - 1, location().getY() + offset.getY(), 0.5f, 0.2f),
                    new Vector(new GlobalPoint(mouse.getX() - location().getX(), mouse.getY() - location().getY()), 1000)));
        }
        animation.startNewAnimation(FIRE);
    }
}
