package engine.objects.renderable.actors;

import engine.ESource;
import engine.objects.renderable.actors.sprites.Sprite;
import engine.objects.renderable.actors.sprites.Text;
import libraries.geometry.NewGeometry.*;
import libraries.geometry.SGeometry.*;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class Counter extends Actor {
    private Sprite counting;

    private Function<Integer, Integer> consumer;
    private Rect textBody;
    private int value = 0;

    public Counter(String name, Figure body, Sprite counting, Function<Integer, Integer> consumer) {
        super(name, body);

        Segment s = body.findPackingDiagonal();
        float width = (s.guidePoint.getX() - s.headPoint.getX() - 4 * ESource.FRAME_WIDTH) / 2,
                height = s.guidePoint.getY() - s.headPoint.getY() - 4 * ESource.FRAME_HEIGHT;

        counting.setBody(counting.getBody().packInto(new SRect(s.headPoint.getX() + ESource.FRAME_WIDTH * 2,
                s.guidePoint.getY() - ESource.FRAME_HEIGHT * 2, width, height)));
        this.counting = counting;

        s.headPoint.addX(ESource.FRAME_WIDTH * 2 + width); s.guidePoint.addX(-ESource.FRAME_WIDTH * 2);
        s.headPoint.addY(ESource.FRAME_HEIGHT * 2); s.guidePoint.addY(-ESource.FRAME_HEIGHT * 2);
        textBody = new Rect(s);

        this.consumer = consumer;
    }

    @Override
    public void process() {}

    public void render() {
        super.render();
        counting.render();

        value = consumer.apply(value);
        new Text(Integer.toString(value), textBody).render();
    }
}
