package engine.managers.view;

import com.jogamp.opengl.util.texture.Texture;
import engine.controllers.view.View;
import libraries.other.Pair;
import engine.controllers.model.Loader;

import java.util.HashMap;

public class TexturesManager {
    private static HashMap<String, Pair<Texture[], Long>> textures = new HashMap<>();

    // time, equaling a thousand seconds, when texture will be deleted;
    private static final long DELETE_TIME = 1000000000000l;

    private static long oldTextureTime = 0;

    public static Texture[] getTextures(String name) {
        Texture[] ts;
        Pair<Texture[], Long> p = textures.get(name);
        if (p == null) {
            ts = Loader.loadAllTextures(name);
            textures.put(name, new Pair<>(ts, System.nanoTime()));
        } else {
            ts = p.f;
        }
        textures.get(name).s = System.nanoTime();
        return ts;
    }

    public static void clearOld() {
        long currentTime = System.nanoTime();
        if (currentTime - oldTextureTime >= DELETE_TIME) {
            oldTextureTime = Long.MAX_VALUE;
            textures.forEach((key, value) -> {
                if (currentTime - value.s >= DELETE_TIME) {
                    for (Texture t : textures.get(key).f) {
                        t.destroy(View.getGLContext());
                    }
                    textures.remove(key, value);
                } else if (value.s < oldTextureTime) {
                    oldTextureTime = value.s;
                }
            });
        }
    }
}