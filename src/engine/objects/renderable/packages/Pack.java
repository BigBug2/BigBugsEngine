package engine.objects.renderable.packages;


import engine.managers.model.GameManager;
import engine.objects.GameObject;
import engine.objects.renderable.Renderable;

public abstract class Pack extends Renderable {
    public final GameManager OBJECTS = new GameManager();

    private int state = 0;

    public Pack(String name) {
        super(name);
    }

    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }

    @Override
    public void render() {
        for (GameObject obj : OBJECTS.getAll()) {
            if (obj instanceof Renderable) {
                ((Renderable) obj).render();
            }
        }
    }

    public boolean delete(int id) {
        return OBJECTS.delete(id);
    }

    protected void processAll() {
        for (GameObject obj : OBJECTS.getAll()) {
            if (obj != null) {
                obj.update();
                if (obj.isProcessable()) {
                    obj.process();
                }
            }
        }
    }
}
