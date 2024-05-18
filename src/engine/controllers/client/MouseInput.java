package engine.controllers.client;

import com.jogamp.newt.event.MouseListener;

import engine.controllers.view.View;
import libraries.geometry.NewGeometry.*;

import java.util.Stack;

public class MouseInput implements MouseListener {
    public static int sensitive = 10000;

    private static final float CLICK_INACCURACY = 1.1f;

    private static long clickTime;
    private static long lastMove;
    private static boolean pressed = false;

    private static GlobalPoint location = new GlobalPoint(0, 0);

    private static Stack<MouseEvent> ways = new Stack<>();

    @Override
    public void mouseClicked(com.jogamp.newt.event.MouseEvent e) {
        location = View.toMeters(new GlobalPoint(e.getX(), e.getY()));
        long time = System.nanoTime();
        if (ways.isEmpty() || time - clickTime > 10000000f &&
                (Math.abs(location.getX() - ways.get(ways.size() - 1).start.getX()) < CLICK_INACCURACY &&
                        Math.abs(location.getY() - ways.get(ways.size() - 1).start.getY()) < CLICK_INACCURACY)) {
            ways.add(new MouseEvent(location, location, true));
            ways.add(new MouseEvent(location, location, false));
            clickTime = time;
        }
    }

    @Override
    public void mouseEntered(com.jogamp.newt.event.MouseEvent e) {

    }

    @Override
    public void mouseExited(com.jogamp.newt.event.MouseEvent e) {

    }

    @Override
    public void mousePressed(com.jogamp.newt.event.MouseEvent e) {
        pressed = true;
        mouseMoved(e);
    }

    @Override
    public void mouseReleased(com.jogamp.newt.event.MouseEvent e) {
        pressed = false;
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(com.jogamp.newt.event.MouseEvent e) {
        if ((System.nanoTime() - lastMove) > 1000000000f / sensitive) {
            lastMove = System.nanoTime();

            GlobalPoint curiousLocation = View.toMeters(new GlobalPoint(e.getX(), e.getY()));
            if (!ways.isEmpty()) {
                ways.add(new MouseEvent(location, curiousLocation, pressed));
            } else {
                ways.add(new MouseEvent(curiousLocation, curiousLocation, pressed));
            }
            location = curiousLocation;
        }
    }

    @Override
    public void mouseDragged(com.jogamp.newt.event.MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseWheelMoved(com.jogamp.newt.event.MouseEvent e) {

    }

    public static MouseEvent[] flushLocations() {
        MouseEvent[] output;
        while (true) {
            try {
                output = new MouseEvent[ways.size()];
                ways.copyInto(output);
                ways.clear();
                break;
            } catch(ArrayIndexOutOfBoundsException e) {}
        }
        if (output.length >= 1) {
            ways.add(new MouseEvent(output[output.length - 1].start, output[output.length - 1].end, pressed));
        }
        return output;
    }

    public static boolean isPressed() {
        return pressed;
    }

    public static class MouseEvent {
        public GlobalPoint start, end;
        public boolean pressed;

        private MouseEvent(GlobalPoint start, GlobalPoint end, boolean state) {
            this.start = start;
            this.end = end;
            this.pressed = state;
        }

        public boolean isClick() {
            return pressed && start.equals(end);
        }
    }
}