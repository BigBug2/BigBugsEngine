package libraries.graphics;

import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import engine.managers.client.InputManager;
import libraries.geometry.NewGeometry;
import libraries.geometry.SGeometry.SRect;

public abstract class Renderer implements GLEventListener {
    protected static GL2 gl = null;
    protected static GLProfile profile;
    protected static GLWindow window;

    protected static SRect body = new SRect(-50, 28, 100, 56);
    protected static float meter;

    @Override
    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2();

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        gl.glEnable(GL2.GL_TEXTURE_2D);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        meter = width / body.width;

        body.height = height / meter;
        body.y = body.height / 2;

        gl.glOrtho(body.x, body.width / 2, -body.height / 2, body.y, -1, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    public static GLProfile getProfile(){
        return profile;
    }
    public static GL2 getGLContext() {
        return gl;
    }

    public static float mWindowHeight() {
        return windowHeight() / meter;
    }
    public static float mWindowWidth() {
        return windowWidth() / meter;
    }
    public static int windowHeight() {
        return window.getHeight();
    }
    public static int windowWidth() {
        return window.getWidth();
    }

    public static void setFullScreen(boolean v) {
        window.setFullscreen(v);
    }

    public static void resizeWindow(int width, int height) {
        window.setSize(width, height);
    }

    public static boolean onScreen(SRect rect) {
        return body.colliding(rect);
    }

    public static float toMeters(int pixels) {
        return pixels / meter;
    }
    public static NewGeometry.GlobalPoint toMeters(NewGeometry.Point p) {
        return new NewGeometry.GlobalPoint(toMeters((int) p.getX()) - mWindowWidth() / 2, toMeters((int) (windowHeight() / 2 - p.getY())));
    }

    protected static void init(GLEventListener view, MouseListener mouse, KeyListener key) {
        GLProfile.initSingleton();
        profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);

        window = GLWindow.create(caps);
        window.setSize(1080, 720);
        window.setFullscreen(false);
        body.width = 100f;
        meter = window.getHeight() ;
        body.height = 0;

        window.setResizable(true);

        window.addGLEventListener(view);
        window.addMouseListener(mouse);
        window.addKeyListener(key);

        window.setVisible(true);
    }
}
/*

    public static class View extends EventListener {
        private static GLProfile profile;
        private static GLWindow window;

        private static boolean fullScreen = true;
        private static int windowWidth = 720, windowHeight = 480;

        public static float meter;

        public View() {
            if (profile == null) {
                init();
            }
        }

        @Override
        public void init(GLAutoDrawable drawable) {
            gl = drawable.getGL().getGL2();

            gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

            gl.glEnable(GL2.GL_TEXTURE_2D);
        }

        @Override
        public void dispose(GLAutoDrawable drawable) {

        }

        @Override
        public void display(GLAutoDrawable drawable) {
            gl = drawable.getGL().getGL2();

            gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

            for (GameModule gameModule : modelsManager.getAll()) {
                gameModule.render();
            }
        }

        @Override
        public void reshape(GLAutoDrawable drawable, int i, int i1, int i2, int i3) {
            GL2 gl = drawable.getGL().getGL2();

            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();

            float unitsWide = getMetersScreenWidth();
            float unitsTall = getWindowHeight() / (getWindowWidth()
                    / unitsWide);

            gl.glOrtho(-unitsWide / 2, unitsWide / 2, -unitsTall / 2, unitsTall / 2, -1, 1);
            gl.glMatrixMode(GL2.GL_MODELVIEW);
        }

        private static void init() {
            GLProfile.initSingleton();
            profile = GLProfile.get(GLProfile.GL2);
            GLCapabilities caps = new GLCapabilities(profile);

            meter = windowWidth / 100f;

            window = GLWindow.create(caps);
            window.setSize(windowWidth, windowHeight);
            window.setResizable(true);

            window.addGLEventListener(new View());
            window.addMouseListener(new MouseInput());
            window.addKeyListener(new KeyInput());

            window.setFullscreen(false);
                    window.setVisible(true);
                    }

public static void render() {
        if (window == null) {
        return;
        }

        window.display();
        }

public static GLProfile getProfile(){
        return profile;
        }

public static float getMetersScreenHeight() {
        return getWindowHeight() / meter;
        }
public static float getMetersScreenWidth() {
        return getWindowWidth() / meter;
        }
public static int getWindowHeight() {
        return window.getHeight();
        }
public static int getWindowWidth() {
        return window.getWidth();
        }

public static float toMeters(int pixels) {
        return pixels / meter;
        }

public static GlobalPoint toMeters(Point p) {
        return new GlobalPoint(toMeters((int) p.getX()) - getMetersScreenWidth() / 2, toMeters((int) (getWindowHeight() / 2 - p.getY())));
        }

public static void resizeWindow(int width, int height) {
        window.setSize(width, height);
        }

        }
 */