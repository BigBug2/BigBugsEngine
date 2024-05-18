package engine.objects.renderable;

import com.jogamp.opengl.util.texture.Texture;
import engine.managers.view.TexturesManager;
import engine.objects.GameObject;
import libraries.geometry.NewGeometry.*;

public abstract class Renderable extends GameObject {
    private final String name;
    private int texture;

    protected boolean renderable = true;

    public Renderable(String name) {
        this.name = name;
    }

    public boolean isRenderable() {
        return renderable;
    }
    public void setRenderable(boolean v) {
        renderable = v;
    }

    public String getName() {
        return name;
    }

    public Texture getTexture() {
        return TexturesManager.getTextures(name)[texture];
    }
    public void setTexture(int texture) {
        this.texture = texture;
    }

    public abstract AnchorPoint location();

    public abstract void render();
}
