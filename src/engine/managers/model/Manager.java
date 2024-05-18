package engine.managers.model;

import java.util.ArrayList;
import java.util.Stack;

public class Manager<OBJ> {
    private ArrayList<OBJ> objects = new ArrayList<>();
    private Stack<Integer> free = new Stack<>();
    private int lastID = 0;

    public OBJ get(int id) {
        return objects.get(id);
    }
    public ArrayList<OBJ> getAll() {
        return new ArrayList<>(objects);
    }
    public int getID(OBJ value) {
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) == value) {
                return i;
            }
        }
        return -1;
    }
    public int add(OBJ object) {
        int free = getFreeID();
        if (free >= objects.size()) {
            objects.add(object);
        } else {
            objects.set(free, object);
        }
        return free;
    }
    public boolean remove(int id) {
        return delete(id);
    }
    public boolean delete(int id) {
        if (objects.get(id) == null) {
            return false;
        }
        objects.set(id, null);
        free.add(id);
        return true;
    }

    private int getFreeID() {
        if (free.isEmpty()) {
            return lastID++;
        }
        return free.pop();
    }
}
