package engine.objects.supports;

import engine.objects.GameObject;

public abstract class Support extends GameObject {
    protected final GameObject target;

    public Support(GameObject target) {
        this.target = target;
    }
}
