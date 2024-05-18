package engine.objects.renderable.actors.sprites;

import engine.controllers.view.View;
import engine.objects.renderable.packages.GameModule;
import libraries.geometry.NewGeometry;

public class Background extends Sprite {
    private final GameModule pack;

    public Background(String name, GameModule pack) {
        super(name, new NewGeometry.Rect(pack.camera.getX() - View.mWindowWidth() / 2, pack.camera.getY() + View.mWindowHeight() / 2,
                View.mWindowWidth(), View.mWindowHeight()));

        this.pack = pack;
    }

    public void reshape() {
        setBody(new NewGeometry.Rect(pack.camera.getX() - View.mWindowWidth() / 2, pack.camera.getY() + View.mWindowHeight() / 2,
                View.mWindowWidth(), View.mWindowHeight()));
    }
}