package engine.controllers.view;


import com.jogamp.opengl.GLAutoDrawable;
import engine.ESource;
import engine.controllers.client.KeyInput;
import engine.controllers.client.MouseInput;
import engine.managers.model.Manager;
import engine.objects.GameObject;
import engine.objects.renderable.Renderable;
import libraries.graphics.Renderer;

import java.util.Stack;

public class View extends Renderer {
    @Override
    public void display(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2();

        gl.glClearColor( 0f, 0f, 0f, 0f );
        gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);

        for (GameObject obj : ESource.MODULES.getAll()) {
            if (obj instanceof Renderable r) {
                if (r.isRenderable()) {
                    r.render();
                }
            }
        }
    }

    public static void render() {
        if (window == null) {
            return;
        }

        window.display();
    }

    public static void init() {
        Renderer.init(new View(), new MouseInput(), new KeyInput());
    }
}