package libraries.graphics;

import com.jogamp.opengl.util.texture.Texture;
import libraries.geometry.SGeometry.SRect;

public class TileSet {
    private final Texture texture;
    private final int tileWidth, tileHeight;

    public TileSet(Texture texture, int tileWidth, int tileHeight) {
        this.texture = texture;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public Tile getTile(SRect body, int num) {
        Tile tile = new Tile();
        tile.texture = texture;

        tile.body = body;

        float onString = (float) texture.getImageWidth() / tileWidth;
        float onRow = (float) texture.getImageHeight() / tileHeight;

        tile.minX = (num % onString) / onString;
        tile.minY = (int) (num / onString) / onRow;

        tile.maxX = tile.minX + (float) tileWidth / texture.getWidth();
        tile.maxY = tile.minY + (float) tileHeight / texture.getHeight();

        return tile;
    }

    public static class Tile {
        public Texture texture;

        public SRect body;
        public float minX, maxY, maxX, minY;
    }
}
