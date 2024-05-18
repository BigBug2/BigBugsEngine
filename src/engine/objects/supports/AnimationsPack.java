package engine.objects.supports;

import engine.objects.GameObject;
import engine.objects.renderable.Renderable;

public class AnimationsPack extends Support {
    private int headAnimation = 0;

    private int animation = 0;
    private long swapTime = System.nanoTime();

    private Animation[] animations;

    public AnimationsPack(Renderable target, long speed, int[] indexes) {
        super(target);

        animations = new Animation[indexes.length / 2];
        for (int i = 0; i < indexes.length; i += 2) {
            animations[i / 2] = new Animation(target, speed, indexes[i], indexes[i + 1]);
        }
    }

    @Override
    public void process() {
        if (System.nanoTime() - swapTime >= animations[animation].getTime()) {
            startNewAnimation(headAnimation);
        }
        animations[animation].update();
        animations[animation].process();
    }
    public long getPlayingTime() {
        return animations[animation].getTime();
    }

    public void startNewAnimation(int animation) {
        if (animation >= animations.length || animation < 0) {
            throw new IllegalArgumentException();
        }
        this.animation = animation;
        swapTime = System.nanoTime();
    }

    public void setHeadAnimation(int animation) {
        if (animation >= animations.length || animation < 0) {
            throw new IllegalArgumentException();
        }
        headAnimation = animation;
    }
}
