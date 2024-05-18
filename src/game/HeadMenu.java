package game;

import engine.ESource;
import engine.controllers.view.View;
import engine.objects.renderable.actors.Actor;
import engine.objects.renderable.actors.Regulator;
import engine.objects.renderable.actors.sprites.Background;
import engine.objects.renderable.actors.Button;
import engine.objects.renderable.actors.sprites.Sprite;
import engine.objects.renderable.actors.sprites.Text;
import engine.objects.renderable.packages.ClientModule;
import engine.objects.renderable.packages.Desk;
import libraries.geometry.NewGeometry.*;
import libraries.geometry.SGeometry;
import libraries.other.Vectors2;

import java.util.ArrayList;
import java.util.Arrays;

public class HeadMenu extends ClientModule {

    public HeadMenu() {
        super("head_menu");

        OBJECTS.add(new Background("background", this));
        OBJECTS.add(new Sprite("logo",  new Rect(-30, 30, 60, 15)));

        ((Button) OBJECTS.get(OBJECTS.add(new Button("button/common", new Rect(-20, 10, 40, 10), (p, s) -> {
            if (s == Button.ACTIVE) {
                ESource.MODULES.add(new GSource());
                setRenderable(false);
                setProcessable(false);
            }
        }, this)))).setSprite(new Text("START", new Rect(0, 0, 5, 5)));
        ((Button) OBJECTS.get(OBJECTS.add(new Button("button/common", new Rect(-20, 0, 40, 10), (p, s) -> {
            if (s == Button.ACTIVE) {

            }
        }, this)))).setSprite(new Text("SETTINGS", new Rect(0, 0, 5, 5)));
        ((Button) OBJECTS.get(OBJECTS.add(new Button("button/common", new Rect(-20, -10, 40, 10), (p, s) -> {
            if (s == Button.ACTIVE) {
                ESource.close();
            }
        }, this)))).setSprite(new Text("QUITE", new Rect(0, 0, 5, 5)));
    }
}
