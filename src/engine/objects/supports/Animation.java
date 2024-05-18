package engine.objects.supports;

import engine.objects.GameObject;
import engine.objects.renderable.Renderable;

public class Animation extends Support {
    private float state;

    private long speed;
    private final int from, to;

    public Animation(Renderable target, long speed, int from, int to) {
        super(target);

        this.speed = speed;

        this.from = from;
        this.to = to;

        state = from;
    }

    @Override
    public void process() {
        state = from + (state - from + (float) ((double) (getFrameTime()) / speed)) % (to - from + 1);
        ((Renderable) target).setTexture((int) state);
    }

    public long getTime() {
        return speed * (to - from);
    }
}
