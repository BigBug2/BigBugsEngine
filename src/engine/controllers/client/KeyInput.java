package engine.controllers.client;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import libraries.other.Vectors2;

import java.util.ArrayList;

public class KeyInput implements KeyListener {
    private static ArrayList<Vectors2<Long>>[] keysStates = loadNewKeysStates();

    @Override
    public void keyPressed(KeyEvent e) {
        keysStates[e.getKeyCode()].add(new Vectors2<>(System.nanoTime(), -1l));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        while (true) {
            try {
                if (keysStates[e.getKeyCode()].isEmpty()) {
                    keysStates[e.getKeyCode()].add(new Vectors2<>(System.nanoTime(), -1l));
                }
                keysStates[e.getKeyCode()].get(keysStates[e.getKeyCode()].size() - 1).y = System.nanoTime();
                break;
            } catch(IndexOutOfBoundsException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static ArrayList<Vectors2<Long>>[] flush() {
        ArrayList<Vectors2<Long>>[] tmp;
        while (true) {
            try {
                tmp = keysStates.clone();
                break;
            } catch (ArrayIndexOutOfBoundsException e) {}
        }
        keysStates = loadNewKeysStates();
        return tmp;
    }

    private static ArrayList<Vectors2<Long>>[] loadNewKeysStates() {
        ArrayList<Vectors2<Long>>[] result = new ArrayList[256];
        for (int i = 0; i < result.length; i++) {
            result[i] = new ArrayList<>();
            if (keysStates != null && !keysStates[i].isEmpty() && keysStates[i].get(keysStates[i].size() - 1).y == -1) {
                result[i].add(new Vectors2<>(keysStates[i].get(keysStates[i].size() - 1).x, -1l));
            }
        }
        return result;
    }
}
