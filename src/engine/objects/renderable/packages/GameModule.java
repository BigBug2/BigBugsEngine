package engine.objects.renderable.packages;

import engine.objects.GameObject;
import engine.objects.renderable.actors.ActiveActor;
import engine.objects.renderable.actors.Actor;
import engine.objects.supports.Support;
import engine.other.GameCamera;
import libraries.geometry.NewGeometry.*;
import libraries.graphics.Graphics;

public abstract class GameModule extends Pack {
    public final GameCamera camera = new GameCamera(0, 0);

    private long sleep = 0;

    public GameModule(String name) {
        super(name);
    }

    @Override
    public void update() {
        super.update();

        sleep -= getFrameTime();
    }

    @Override
    public void processAll() {
        for (GameObject obj : OBJECTS.getAll()) {
            if (obj != null) {
                obj.update();
                if (obj.isProcessable()) {
                    if (obj instanceof ActiveActor) {
                        if (new Segment(camera, ((Actor) obj).location()).length() < 1000){
                            obj.process();
                        }
                    } else {
                        obj.process();
                    }
                }
            }
        }
    }

    public void render() {
        Graphics.setCamera(camera);
        super.render();
    }

    @Override
    public AnchorPoint location() {
        return new AnchorPoint(camera);
    }

    public void sleep(long ms) {
        sleep = ms;
    }

    @Override
    public boolean isProcessable() {
        return super.isProcessable() && sleep <= 0;
    }
}
