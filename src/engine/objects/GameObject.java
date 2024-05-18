package engine.objects;

import engine.Exceptions;
import engine.managers.model.Manager;

public abstract class GameObject {
    private int id = -1;
    private Manager<GameObject> manager;

    private long frameTime;
    private long lastUpdating = System.nanoTime();

    protected boolean processable = true;

    public abstract void process();

    public void destroy() {
        if (getId() == -1) {
            class InitializationException extends RuntimeException{
                InitializationException() {
                    super("don't initialized id of object to destroy it");
                }
            };
            throw new InitializationException();
        }
        manager.delete(getId());
    }

    public void update() {
        long curious = System.nanoTime();
        frameTime = curious - lastUpdating;
        lastUpdating = curious;
    }

    public long getFrameTime() {
        return frameTime;
    }

    public int getId() {
        if (id == -1) {
            throw new Exceptions.NoValidID();
        }
        return id;
    }
    public void setId(int id, Manager<GameObject> manager) {
        if (this.id == -1) {
            this.id = id;
            this.manager = manager;
        } else {
            throw new IllegalArgumentException("cannot rewrite id because" +
                    " he was already initialized");
        }
    }

    public boolean isProcessable() {
        return processable;
    }
    public void setProcessable(boolean v) {
        processable = v;
    }
}
