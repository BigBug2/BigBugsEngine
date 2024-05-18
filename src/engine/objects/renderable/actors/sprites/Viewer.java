package engine.objects.renderable.actors.sprites;

import engine.ESource;
import engine.managers.view.TexturesManager;
import libraries.geometry.NewGeometry.*;
import libraries.geometry.SGeometry.*;
import libraries.graphics.Graphics;

import java.util.Comparator;
import java.util.function.Function;


public class Viewer extends Sprite {
    private final int maxValue;

    private String spaceName;

    private final Function<Void, Integer> function;

    public Viewer(String wayName, Figure body, Function<Void, Integer> function, int maxValue) {
        super("background", body);

        spaceName = wayName;

        this.function = function;

        this.maxValue = maxValue;
    }

    @Override
    public void render() {
        super.render();

        Segment s = getBody().findPackingDiagonal();
        float frameWidth = (s.guidePoint.getX() - s.headPoint.getX()) * ESource.FRAME_WIDTH / 2;
        float frameHeight = (s.guidePoint.getY() - s.headPoint.getY()) * ESource.FRAME_HEIGHT / 2;

        SRect space = new SRect(s.headPoint.getX() + frameWidth, s.guidePoint.getY() - frameHeight,
                s.guidePoint.getX() - s.headPoint.getX() - 2 * frameWidth, s.guidePoint.getY() - s.headPoint.getY() - 2 * frameHeight);

        space.width = Math.max(Math.min(space.width * ((float) function.apply(null) / maxValue), space.width), 0);
        Graphics.fillRect(space, TexturesManager.getTextures(spaceName)[0]);
    }
}
