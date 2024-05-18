package engine.objects.renderable.actors.sprites;

import engine.managers.view.TexturesManager;
import libraries.geometry.NewGeometry.*;
import libraries.geometry.SGeometry;
import libraries.graphics.Graphics;
import libraries.graphics.TileSet;

public class Text extends Sprite {
    private static TileSet symbols = null;
    private int countOfStrings;

    private String text;

    public Text(String name, Figure body) {
        super(name, body);

        text = name;

        countOfStrings = 1;
        for (int i = 0; i < text.length(); i++) {
            if (name.charAt(i) == '\n') {
                countOfStrings++;
            }
        }
    }

    @Override
    public void render() {
        if (symbols == null) {
            symbols = new TileSet(
                    TexturesManager.getTextures("text")[0], 20, 20);
        }

        SGeometry.SRect rect = new SGeometry.SRect(getBody().findPackingDiagonal());

        float mTileWidth = rect.width / ((float) text.length() / countOfStrings);
        float y = rect.y;

        float j = -text.length() / (countOfStrings * 2f);
        for (int i = 0; i < text.length(); i++, j++) {
            if (text.charAt(i) == '\n') {
                y -= rect.height / countOfStrings;
                j = (float) -text.length() / countOfStrings / 2 - 1;
            } else {
                Graphics.drawTile(symbols.getTile(new SGeometry.SRect(location().getX() + mTileWidth * j, y, mTileWidth, rect.height),
                        text.charAt(i) >= 'a' && text.charAt(i) <= 'z' ? Character.toUpperCase(text.charAt(i)) : text.charAt(i)));
            }
        }
    }

    public void setText(String text) {
        this.text = text;
    }
}
