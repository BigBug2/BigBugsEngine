package libraries.physics;

import libraries.geometry.NewGeometry.*;

public interface Physical {
    float getS();
    float getMass();

    Vector getR();
    Vector getSpeedVector();
    Vector getRotationMoment();

    void move();
    void move(Vector direction);

    void push(Vector force, GlobalPoint from);
}
