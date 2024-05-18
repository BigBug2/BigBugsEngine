package engine.objects.renderable.actors;

import engine.ESource;
import engine.managers.view.TexturesManager;
import engine.objects.renderable.packages.ClientModule;
import libraries.geometry.NewGeometry.*;
import libraries.geometry.SGeometry.*;
import libraries.graphics.Graphics;
import libraries.other.Vectors2;

import java.util.function.BiConsumer;

public class Regulator extends Actor {
    public static final int COMMON = 0, VERTICAL = 1, HORIZONTAL = 2;

    private final int type;

    private Button jigger;

    GlobalPoint from, to;

    public Regulator(String name, Figure body, int type, BiConsumer<Float, Float> function, ClientModule menu) {
        super(name, body);

        this.type = type;

        Segment s = body.findPackingDiagonal();
        float frameWidth = (s.guidePoint.getX() - s.headPoint.getX()) * ESource.FRAME_WIDTH/ 2;
        float frameHeight = (s.guidePoint.getY() - s.headPoint.getY()) * ESource.FRAME_HEIGHT / 2;

        this.from = new GlobalPoint(s.headPoint.getX() + frameWidth,
                s.headPoint.getY() + frameHeight);
        this.to = new GlobalPoint(s.guidePoint.getX() - frameWidth,
                s.guidePoint.getY() - frameHeight);
        if (type == VERTICAL) {
            from.setX((from.getX() + to.getX()) / 2);
            to.setX(from.x);
        } else if (type == HORIZONTAL){
            from.setY((from.getY() + to.getY()) / 2);
            to.setY(from.getY());
        }

        if (from.getX() > to.getX() || from.getY() > to.getY()) {
            throw new IllegalArgumentException("from cannot be bigger then to point");
        }

        jigger = new Button("button/common", new Circle(new GlobalPoint(from.getX(), to.getY()), 4), (e, state) -> {
            if (state > 1) {
                Point location = jigger.location();
                location.setX(Math.max(Math.min((e.end.getX()), to.getX()), from.getX()));
                location.setY(Math.max(Math.min((e.end.getY()), to.getY()), from.getY()));

                function.accept((location.getX() - from.getX()) / (to.getX() - from.getX()),
                        (location.getY() - from.getY()) / (to.getY() - from.getY()));
            }
        }, menu);
    }

    @Override
    public void process() {
        jigger.process();
    }

    @Override
    public void render() {
        super.render();
        if (type == COMMON) {
            Graphics.drawRect(new SRect(from.getX(), to.getY(), jigger.location().getX() -
                    from.getX(), to.getY() - jigger.location().getY()), TexturesManager.getTextures("jigger/way")[0]);
        } else if (type == VERTICAL) {
            Segment s = jigger.getBody().findPackingDiagonal();
            Graphics.drawRect(new SRect(to.getX() - (s.guidePoint.getX() - s.headPoint.getX()) / 2, to.getY(), s.guidePoint.getX() - s.headPoint.getX(),
                    to.getY() - jigger.location().getY()), TexturesManager.getTextures("jigger/way")[0]);
        } else if (type == HORIZONTAL) {
            Segment s = jigger.getBody().findPackingDiagonal();
            Graphics.drawRect(new SRect(from.getX(), from.getY() + (s.guidePoint.getY() - s.headPoint.getY()) / 2, jigger.location().getX() - from.getX(),
                    s.guidePoint.getY() - s.headPoint.getY()), TexturesManager.getTextures("jigger/way")[0]);
        }
        jigger.render();
    }
}