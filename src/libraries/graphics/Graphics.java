package libraries.graphics;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import libraries.geometry.NewGeometry.*;
import libraries.geometry.SGeometry.SRect;

public class Graphics {
    static Camera camera = new ZeroCamera();

    private static float tileSize = 10;

    public static void setTileSize(float v) {
        tileSize = v;
    }

    public static void draw(Figure figure, Texture texture) {
        draw(figure instanceof Shape ? (Shape) figure : ((Circle) figure).toShape(), texture);
    }
    public static void fill(Figure figure, Texture texture) {
        fill(figure instanceof Shape ? (Shape) figure : ((Circle) figure).toShape(), texture);
    }

    public static void draw(Shape shape, Texture texture) {
        if (!camera.isVisible()) {
            return;
        }
        GL2 gl = Renderer.getGLContext();

        Segment diagonal = shape.findPackingDiagonal();

        float width = diagonal.guidePoint.getX() - diagonal.headPoint.getX();
        float height = diagonal.guidePoint.getY() - diagonal.headPoint.getY();

        if (!Renderer.onScreen(new SRect((diagonal.headPoint.getX() - camera.getX()) * camera.getZoom(), (diagonal.headPoint.getY() - camera.getY()) * camera.getZoom(),
                (width * camera.getZoom()), (height * camera.getZoom())))) {

            /*System.out.println(new SRect((diagonal.headPoint.getX() - camera.getX()) * camera.getZoom(),
                    (diagonal.headPoint.getY() - camera.getY()) * camera.getZoom(),
                    ((diagonal.guidePoint.getX() - diagonal.headPoint.getX()) * camera.getZoom()),
                    ((diagonal.guidePoint.getY() - diagonal.headPoint.getY()) * camera.getZoom())));
            *///return;
        }

        if (texture != null) {
            gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject());
        }

        if (shape.countOfEdges() >= 4) {
            gl.glBegin(GL2.GL_POLYGON);
        } else if (shape.countOfEdges() == 3){
            gl.glBegin(GL2.GL_TRIANGLES);
        }
        for (int i = 0; i < shape.countOfEdges(); i++) {
            float x = shape.getVertex(i).getX(), y = shape.getVertex(i).getY();
            gl.glTexCoord2d((x - diagonal.headPoint.getX()) / width, 1 - (y - diagonal.headPoint.getY()) / height);
            gl.glVertex2f((x - camera.getX()) * camera.getZoom(), (y - camera.getY()) * camera.getZoom());
        }
        gl.glEnd();

        gl.glFlush();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
    }
    public static void fill(Shape shape, Texture texture) {
        if (!camera.isVisible()) {
            return;
        }
        GL2 gl = Renderer.getGLContext();

        Segment diagonal = shape.findPackingDiagonal();
        /*if (!Renderer.onScreen(new SRect((diagonal.headPoint.getX() - camera.getX()) * camera.getZoom(),
                (diagonal.headPoint.getY() - camera.getY()) * camera.getZoom(),
                ((diagonal.guidePoint.getX() - diagonal.headPoint.getX()) * camera.getZoom()),
                ((diagonal.guidePoint.getY() - diagonal.headPoint.getY()) * camera.getZoom())))) {
            return;
        }*/

        if (texture != null) {
            gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject());
        }
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);

        gl.glMatrixMode(gl.GL_MODELVIEW);

        if (shape.countOfEdges() >= 4) {
            gl.glBegin(GL2.GL_POLYGON);
        } else if (shape.countOfEdges() == 3){
            gl.glBegin(GL2.GL_TRIANGLES);
        }
        for (int i = 0; i < shape.countOfEdges(); i++) {
            float x = shape.getVertex(i).getX(), y = shape.getVertex(i).getY();
            //System.out.println((x + " " + camera.getX()) + " " + camera.getZoom());
            gl.glTexCoord2d((x - diagonal.headPoint.getX()) / tileSize, 1 - (y - diagonal.headPoint.getY()) / tileSize);
            gl.glVertex2f((x - camera.getX()) * camera.getZoom(), (y - camera.getY()) * camera.getZoom());
        }
        gl.glEnd();
        gl.glFlush();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);

    }

    public static void drawRect(SRect rect, Texture texture) {
        if (!camera.isVisible()) {
            return;
        }
        GL2 gl = Renderer.getGLContext();

        SRect drawable = new SRect((rect.x - camera.getX()) * camera.getZoom(),
                (rect.y - camera.getY()) * camera.getZoom(), rect.width * camera.getZoom(),
                rect.height * camera.getZoom());

        /*if (!Renderer.onScreen(drawable)) {
            return;
        }*/

        if (texture != null) {
            gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject());
        }

        gl.glTranslatef(drawable.x, drawable.y, 0);

        gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2d(0, 1);
            gl.glVertex2f(0, 0);

            gl.glTexCoord2d(1, 1);
            gl.glVertex2f(drawable.width, 0);

            gl.glTexCoord2d(1, 0);
            gl.glVertex2f(drawable.width, -drawable.height);

            gl.glTexCoord2d(0, 0);
            gl.glVertex2f(0, -drawable.height);
        gl.glEnd();
        gl.glFlush();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);

        gl.glTranslatef(-(rect.x - camera.getX()) * camera.getZoom(),
                -(rect.y - camera.getY()) * camera.getZoom(), 0);
    }
    public static void fillRect(SRect rect, Texture texture) {
        if (!camera.isVisible()) {
            return;
        }
        GL2 gl = Renderer.getGLContext();

        SRect drawable = new SRect((rect.x - camera.getX()) * camera.getZoom(),
                (rect.y - camera.getY()) * camera.getZoom(), rect.width * camera.getZoom(),
                rect.height * camera.getZoom());

        /*if (!Renderer.onScreen(drawable)) {
            return;
        }*/

        if (texture != null) {
            gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject());
        }
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);

        gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2d(0, 0);
            gl.glVertex2f(drawable.x, drawable.y);

            gl.glTexCoord2d(drawable.width / tileSize, 0);
            gl.glVertex2f(drawable.x + drawable.width, drawable.y);

            gl.glTexCoord2d(drawable.width / tileSize, drawable.height / tileSize);
            gl.glVertex2f(drawable.x + drawable.width, drawable.y - drawable.height);

            gl.glTexCoord2d(0, drawable.height / tileSize);
            gl.glVertex2f(drawable.x,drawable.y - drawable.height);
        gl.glEnd();

        gl.glFlush();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
    }

    public static void drawTile(TileSet.Tile tile) {
        if (!camera.isVisible()) {
            return;
        }
        GL2 gl = Renderer.getGLContext();

        SRect drawable = new SRect((tile.body.x - camera.getX()) * camera.getZoom(),
                (tile.body.y - camera.getY()) * camera.getZoom(), tile.body.width * camera.getZoom(),
                tile.body.height * camera.getZoom());

        /*if (!Renderer.onScreen(drawable)) {
            return;
        }*/

        if (tile.texture != null) {
            gl.glBindTexture(GL2.GL_TEXTURE_2D, tile.texture.getTextureObject());
        }

        gl.glTranslatef(drawable.x, drawable.y, 0);

        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2d(tile.minX, tile.minY);
        gl.glVertex2f(0, 0);

        gl.glTexCoord2d(tile.maxX, tile.minY);
        gl.glVertex2f(drawable.width, 0);

        gl.glTexCoord2d(tile.maxX, tile.maxY);
        gl.glVertex2f(drawable.width, -drawable.height);

        gl.glTexCoord2d(tile.minX, tile.maxY);
        gl.glVertex2f(0, -drawable.height);
        gl.glEnd();
        gl.glFlush();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);

        gl.glTranslatef(-(tile.body.x - camera.getX()) * camera.getZoom(),
                -(tile.body.y - camera.getY()) * camera.getZoom(), 0);
    }

    public static void setCamera(Camera newCamera) {
        camera = newCamera;
    }

    private static class ZeroCamera implements Camera {
        @Override
        public float getZoom() {
            return 1;
        }

        @Override
        public void setZoom(float v) {}

        @Override
        public boolean isVisible() {
            return true;
        }

        @Override
        public float getX() {
            return 0;
        }

        @Override
        public float getY() {
            return 0;
        }

        @Override
        public void setX(float v) {}

        @Override
        public void setY(float v) {}

        @Override
        public void addX(float v) {}

        @Override
        public void addY(float v) {}

        @Override
        public boolean equals(Point p) {
            return false;
        }

        @Override
        public Point copy() {
            return new ZeroCamera();
        }
    }
}
/*
public static void fill (Shape shape, Texture texture) {
        GL2 gl = EventListener.gl;

        if (texture != null) {
            gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject());
        }
        ArrayList<Shape> tiles = new ArrayList<>();

        Segment diagonal = shape.findPackingDiagonal();
        float length = diagonal.guidePoint.getX() - diagonal.headPoint.getX();

        gl.glBegin(GL2.GL_QUADS);
        for (int i = 0; i < shape.getCountOfEdges(); i++) {
            float x = shape.getEdge(i).getX(), y = shape.getEdge(i).getY();
            gl.glTexCoord2f((x - diagonal.headPoint.getX()) / length, (y - diagonal.headPoint.getY()) / length);
            gl.glVertex2f(x, y);
        }
        gl.glEnd();

        gl.glFlush();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
    }
 */