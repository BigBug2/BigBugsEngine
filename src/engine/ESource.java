package engine;

import engine.controllers.model.Loop;
import engine.controllers.view.View;
import engine.managers.model.GameManager;
import game.HeadMenu;

public class ESource {
    public static final float FRAME_WIDTH = 0.1f, FRAME_HEIGHT = 0.3f;

    public static final GameManager MODULES = new GameManager();

    public static void init() {
        View.init();
        Loop.init();

        MODULES.add(new HeadMenu());

        Loop.startGameLoop();
    }

    public static void close() {
        System.exit(0);
    }
}
