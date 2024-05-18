package engine.other;

import libraries.geometry.NewGeometry.*;

public class GameCamera implements libraries.graphics.Camera {
    private float x, y;
    private float zoom = 1;

    private boolean visible = true;

    public GameCamera(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public GameCamera(float x, float y, float zoom) {
        this.x = x;
        this.y = y;

        this.zoom = zoom;
    }

    @Override
    public float getZoom() {
        return zoom;
    }

    @Override
    public void setZoom(float v) {
        zoom = v;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setX(float v) {
        this.x = v;
    }

    @Override
    public void setY(float v) {
        this.y = v;
    }

    @Override
    public void addX(float v) {
        x += v;
    }

    @Override
    public void addY(float v) {
        y += v;
    }

    public void setVisible(boolean v) {
        visible = v;
    }
    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public boolean equals(Point p) {
        return x == p.getX() && y == p.getY();
    }

    @Override
    public Point copy() {
        return new GameCamera(x, y, zoom);
    }
}
