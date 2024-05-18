package game.actors;

import engine.managers.client.InputManager;
import game.GSource;
import libraries.geometry.NewGeometry.*;
import libraries.physics.Physics;

public class Radio extends Ability {
    private final GSource game;
    private static final float ROCKET_SIZE = 4;

    public Radio(Figure body, GSource game) {
        super("radio", body, (a) -> {
            return InputManager.getKeyState(' ').length >= 1;
        }, 0, 1, Physics.SECOND * 5);

        this.game = game;
        tie(game.getPlayer().location());
    }

    @Override
    public void use() {
        Point mouse = InputManager.getLastMouseLocation(game.camera);
        if (mouse != null) {
            game.OBJECTS.add(new Rocket(new Rect(mouse.getX() - ROCKET_SIZE / 2, mouse.getY() + ROCKET_SIZE / 2,
                    ROCKET_SIZE, ROCKET_SIZE), game));
        }
    }
}