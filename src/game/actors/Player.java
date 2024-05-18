package game.actors;

import engine.ESource;
import engine.controllers.model.Loader;
import engine.managers.client.InputManager;
import engine.objects.renderable.actors.ActiveActor;
import engine.objects.renderable.actors.Creating;
import engine.objects.renderable.actors.sprites.Sprite;
import engine.objects.renderable.packages.Pack;
import engine.objects.supports.AnimationsPack;
import game.GSource;
import libraries.geometry.NewGeometry.*;
import libraries.geometry.SGeometry;
import libraries.graphics.Graphics;
import libraries.physics.Physics;
import java.util.ArrayList;

public class Player extends Creating {
    private int points;
    private final GSource game;

    private final Sprite pointer;
    private final AnimationsPack pointerAnimations;

    public Player(Figure body, GSource game) {
        super("player", body, Physics.FLESH, 100);
        this.game = game;

        Segment s = getBody().findPackingDiagonal();
        float width = (s.guidePoint.getX() - s.headPoint.getX()), height = (s.guidePoint.getY() - s.headPoint.getY());
        pointer = new Sprite("green_way", new Rect(location().getX() - width / 2 - ESource.FRAME_WIDTH, location().getY(), width + 2 * ESource.FRAME_WIDTH,
                height / 2 + ESource.FRAME_HEIGHT));
        pointer.tie(location());
        pointerAnimations = Loader.loadAnimations(pointer);

        setMaxSpeed(10);
    }

    @Override
    public void process(ActiveActor actor) {
        if (actor instanceof Creating) {
            ((Creating) actor).addHealth(-10);
        } else if (actor instanceof Item) {
            if (actor instanceof Coin) {
                points += 1;
                ((Coin) actor).pick();
            }
        }
    }

    @Override
    public void process() {
        super.process();

        push(new Vector(new GlobalPoint(-1, 0), InputManager.getKeyState((int) 'A').length * getMass() * Physics.g * 2), location());
        push(new Vector(new GlobalPoint(1, 0), InputManager.getKeyState((int) 'D').length  * getMass() * Physics.g * 2), location());
        push(new Vector(new GlobalPoint(0, -1), InputManager.getKeyState((int) 'S').length  * getMass() * Physics.g * 2), location());
        push(new Vector(new GlobalPoint(0, 1), InputManager.getKeyState((int) 'W').length  * getMass() * Physics.g * 2), location());
    }

    @Override
    public void render() {
        if (isRenderable()) {
            pointerAnimations.process();
            pointer.render();
            super.render();
        }
    }
    public int getPoints() {
        return points;
    }
    public int getPoints(int old) {
        return points;
    }

    public void addPoints(int v) {
        points += v;
    }

    @Override
    public void destroy() {
        setProcessable(false);
        setRenderable(false);
    }
}
