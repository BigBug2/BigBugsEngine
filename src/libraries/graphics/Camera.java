package libraries.graphics;

import libraries.geometry.NewGeometry.Point;

public interface Camera extends Point {
    float getZoom();
    void setZoom(float v);

    boolean isVisible();
}
