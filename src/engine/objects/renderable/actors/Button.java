package engine.objects.renderable.actors;

import engine.ESource;
import engine.controllers.client.MouseInput.MouseEvent;
import engine.objects.renderable.actors.sprites.Sprite;
import engine.objects.renderable.actors.sprites.Text;
import engine.objects.renderable.packages.ClientModule;
import libraries.geometry.NewGeometry.*;
import libraries.geometry.SGeometry.*;

import java.util.function.BiConsumer;

public class Button extends Actor {
    public static final int ACTIVE = 2, AFFECTED = 1, INACTIVE = 0;

    protected BiConsumer<MouseEvent, Integer> function;

    private final ClientModule menu;

    private int state = 0;

    private Sprite sprite = null;

    public Button(String name, Figure body, BiConsumer<MouseEvent, Integer> function, ClientModule menu) {
        super(name, body);

        this.menu = menu;

        this.function = function;
    }

    @Override
    public void process() {

        SRect active = getHitBox();
        Figure body = getBody();

        state = 0;
        MouseEvent[] events = menu.getMouseEvents();
        for (MouseEvent e : events) {
            if (active.in(e.end) && body.in(e.end)) {
                if (e.pressed) {
                    state = 2;
                    function.accept(e, state);
                } else {
                    state = Math.max(1, state);
                }
            }
        }
        for (MouseEvent e : events) {
            Segment s = new Segment(e.start, e.end);
            if (active.colliding(s) &&
                    FiguresTools.areColliding(body, s)) {
                if (e.pressed) {
                    state = 2;
                    function.accept(e, state);
                } else {
                    state = Math.max(1, state);
                }
            }
        }

        setTexture(state);
    }

    @Override
    public void render() {
        super.render();

        if (sprite != null) {
            sprite.render();
        }
    }

    public void setSprite(Sprite sprite) {
        Segment d = getBody().findPackingDiagonal();
        float frameWidth = (d.guidePoint.getX() - d.headPoint.getX()) * ESource.FRAME_WIDTH/ 2;
        float frameHeight = (d.guidePoint.getY() - d.headPoint.getY()) * ESource.FRAME_HEIGHT / 2;
        sprite.setBody(sprite.getBody().packInto(new SRect(d.headPoint.getX() + frameWidth,
                d.guidePoint.getY() - frameHeight, d.guidePoint.getX() - d.headPoint.getX() - frameWidth * 2,
                d.guidePoint.getY() - d.headPoint.getY() - frameHeight * 2)));

        this.sprite = sprite;
    }
}