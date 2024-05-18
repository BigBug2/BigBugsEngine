package engine.objects.renderable.packages;

import engine.objects.renderable.actors.Actor;
import engine.objects.renderable.actors.sprites.Sprite;
import libraries.geometry.NewGeometry.*;
import libraries.geometry.SGeometry.*;

import java.util.ArrayList;

public class Desk extends ClientModule {
    private int background;

    public Desk(String name, SRect body, ArrayList<Actor> actors) {
        super(name);

        background = OBJECTS.add(new Sprite("background", new Rect(body.x, body.y, body.width, body.height)));

        SRect space = new SRect(body.x + 0.1f, body.y - 0.1f,
                body.width - 0.2f, body.height - 0.2f);

        for (Actor actor : actors) {
            SRect actorBox = actor.getHitBox();

            SRect box = new SRect(space.x + space.width * actorBox.x, space.y + space.height * actorBox.y,
                    space.width * actorBox.width, space.height * actorBox.height);

            actor.setBody(actor.getBody().packInto(box));

            OBJECTS.add(actor);
        }
    }

    @Override
    public void process() {
        super.process();
    }

    public Sprite getBackground() {
        return (Sprite) OBJECTS.get(background);
    }
}