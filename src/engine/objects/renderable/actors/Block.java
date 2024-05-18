package engine.objects.renderable.actors;

import engine.objects.renderable.packages.Pack;
import libraries.geometry.NewGeometry.*;
import libraries.graphics.Graphics;
import libraries.physics.Physics;

public class Block extends ActiveActor {

    public Block(Figure body, Physics.Material material) {
        super(material.name, body, material);
    }

    @Override
    public void process() {
        super.process();
    }
    @Override
    public void render() {
        Graphics.fill(getBody(), getTexture());
    }

    @Override
    public void process(ActiveActor actor) {
        //System.out.println(getLocation());
    }

    public void setStatic(boolean v) {
        fixedPlace = v;
    }
}
