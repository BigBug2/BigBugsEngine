package game;

import engine.controllers.view.View;
import engine.objects.supports.Support;
import game.actors.Enemy;
import game.actors.Heal;
import game.actors.Spike;
import libraries.geometry.NewGeometry.*;
import libraries.physics.Physics;

import java.util.Random;

public class Factory extends Support {
    private static final int ENEMY = 1, SPY = 2, HEAL = 3;
    private static final float WIDTH = 2, HEIGHT = 2;
    private long time;
    private GSource game;

    private Random random = new Random();

    public Factory(GSource target) {
        super(target);

        game = target;
    }

    @Override
    public void process() {
        time -= getFrameTime();
        if (time < 0) {
            int chance = Math.abs(random.nextInt()) % 100;
            if (chance > 30) {
                if (chance < 85) {
                    Point p = getRandomBorderPosition();
                    Rect body = new Rect(p.getX(), p.getY(), WIDTH, HEIGHT);
                    game.OBJECTS.add(new Enemy(body, game.getPlayer(), game));
                } else {
                    Point p = getRandomPosition();
                    Rect body = new Rect(p.getX(), p.getY(), WIDTH, HEIGHT);
                    if (chance < 90) {
                        game.OBJECTS.add(new Heal(body));
                    } else {
                        game.OBJECTS.add(new Spike(body));
                    }
                }
            }
            time = (long) (Math.abs(new Random().nextInt() % 200 / 100f) * Physics.SECOND);
        }
    }

    public Point getRandomBorderPosition() {
        float position = Math.abs(random.nextInt()) % (int) (100 * 2 * (View.mWindowWidth() - WIDTH + View.mWindowHeight() - HEIGHT)) / 100f;
        Rect view = new Rect(-View.mWindowWidth() / 2 + WIDTH, View.mWindowHeight() / 2 - HEIGHT,
                View.mWindowWidth() - WIDTH * 2, View.mWindowHeight() - HEIGHT * 2);

        for (int side = 0;; side = (side + 1) % 4) {
            float sideLength = view.getEdge(side).length();
            if (sideLength > position) {
                return new Segment(new Ray(view.getEdge(side).headPoint, view.getEdge(side).guidePoint), position).guidePoint;
            }
            position -= sideLength;
        }
    }

    public GlobalPoint getRandomPosition() {
        return new GlobalPoint(random.nextInt() % ((View.mWindowWidth() / 2 - WIDTH * 2) * 100) / 100f,
                random.nextInt() % ((View.mWindowHeight() / 2 - HEIGHT * 2) * 100) / 100f);
    }
}
