package engine.managers.client;

import engine.controllers.client.KeyInput;
import engine.controllers.client.MouseInput;
import engine.controllers.view.View;

import libraries.graphics.Camera;
import libraries.other.Vectors2;
import libraries.geometry.NewGeometry.*;

import java.util.ArrayList;
import java.util.Arrays;

public class InputManager {
    private static MouseInput.MouseEvent[] mouseLocations;

    private static ArrayList<Vectors2<Long>>[] keysStates;

    public static void update() {
        mouseLocations = MouseInput.flushLocations();
        keysStates = KeyInput.flush();
    }

    public static MouseInput.MouseEvent[] getMouseLocations(Camera camera) {
        MouseInput.MouseEvent[] arr = Arrays.copyOf(mouseLocations, mouseLocations.length);
        for (var m : arr) {
            m.start.setX((m.start.getX() - camera.getX()) / camera.getZoom());
            m.start.setY((m.start.getY() - camera.getY()) / camera.getZoom());

            m.end.setX((m.end.getX() - camera.getX()) / camera.getZoom());
            m.end.setY((m.end.getY() - camera.getY()) / camera.getZoom());
        }
        return arr;
    }

    public static Point getLastMouseLocation(Camera camera) {
        if (mouseLocations.length == 0) {
            return null;
        }
        Point tmp = mouseLocations[mouseLocations.length - 1].end.copy();
        tmp.setX((tmp.getX() - camera.getX()) / camera.getZoom());
        tmp.setY((tmp.getY() - camera.getY()) / camera.getZoom());
        return tmp;
    }

    public static Vectors2<Long>[] getKeyState(int key) {
        Vectors2<Long>[] arr = new Vectors2[keysStates[key].size()];
        keysStates[key].toArray(arr);
        return arr;
    }

    public static class Keys {
        public static final int TAB = 9, ENTER = 13, SHIFT = 15, CTRL = 17, CAPS_LOCK = 20, ESC = 27;
        public static final int F1 = 97, F2 = 98, F3 = 99;
        public static final int LEFT = 149, UP = 150, RIGHT = 151, DOWN = 152;

        public int getKeyState(char c) {
            return getKeyState(c);
        }
    }
}

