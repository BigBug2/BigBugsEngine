package engine.managers.model;


import engine.objects.GameObject;
import engine.objects.renderable.packages.GameModule;

public class GameManager extends Manager<GameObject> {
    @Override
    public int add(GameObject module) {
        int id = super.add(module);
        module.setId(id, this);
        return id;
    }
}
