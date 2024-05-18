package game;

import engine.controllers.model.Loader;
import engine.controllers.view.View;
import engine.objects.renderable.actors.Counter;
import engine.objects.renderable.actors.sprites.Sprite;
import engine.objects.renderable.actors.sprites.Viewer;
import engine.objects.renderable.packages.ClientModule;

import libraries.geometry.NewGeometry.*;

public class GInterface extends ClientModule {
    private final int inventor, bufferInventor;

    public GInterface(GSource game) {
        super("interface");

        OBJECTS.add(new Sprite("player", new Rect(-View.mWindowWidth() / 2 + 2, View.mWindowHeight() / 2, 8, 8)));
        OBJECTS.add(new Viewer("jigger/way", new Rect(-View.mWindowWidth() / 2 + 10, View.mWindowHeight() / 2, 16, 2), game.getPlayer()::getHealth, 100));
        Sprite coinSprite = new Sprite("coin", new Rect(0, 0, 10, 5));
        OBJECTS.add(Loader.loadAnimations(coinSprite));
        OBJECTS.add(new Counter("button/common", new Rect(-View.mWindowWidth() / 2 + 10, View.mWindowHeight() / 2 - 2, 12, 6), coinSprite, game.getPlayer()::getPoints));
        inventor = OBJECTS.add(new Inventor(game, new Rect(20, View.mWindowHeight() / 2,
                View.mWindowWidth() / 2 - 22, 8), 3));
        bufferInventor = 0;
    }

    @Override
    public void process() {
        super.process();
    }

    public Inventor getPlayerInventor() {
        return (Inventor) OBJECTS.get(inventor);
    }
}
