package engine.controllers.model;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import engine.controllers.view.View;
import engine.objects.renderable.Renderable;
import engine.objects.supports.AnimationsPack;


public class Loader {
    public static final String imageType = ".png";

    public static AnimationsPack loadAnimations(Renderable target) {
        try {
            try {
                Scanner scanner = new Scanner(new File(Loader.class.getResource("/res/textures/"
                        + target.getName() + "/animation.txt").toURI()));
                long speed = scanner.nextLong();

                int[] indexes = new int[scanner.nextInt() * 2];
                for (int i = 0; i < indexes.length; i++) {
                    indexes[i] = scanner.nextInt();
                }

                scanner.close();

                return new AnimationsPack(target, speed, indexes);

            } catch (URISyntaxException e) {
                System.out.println("Cannot create URI");
                throw new RuntimeException(e);
            }
        } catch (FileNotFoundException e) {
            System.out.println("There is not file: " + "/res/textures" + target.getName() + "/animation.txt.txt");
            return null;
        }
    }
    public static Texture[] loadAllTextures(String packageName) {
        ArrayList<Texture> textures = new ArrayList<>();
        Texture tmp;

        for (int i = 1;; i++) {
            tmp = loadTexture(packageName + "/type" + i + imageType);
            if (tmp == null) {
                break;
            } else {
                textures.add(tmp);
            }
        }

        Texture[] array = new Texture[textures.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = textures.get(i);
        }
        return array;
    }
    public static Texture loadTexture(String path) {
        URL url = Loader.class.getResource("/res/textures/" + path);
        if (url == null) {
            return null;
        }

        BufferedImage image = null;
        try {
            image = ImageIO.read(url);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        if (image != null) {
            image.flush();
            GL2 gl = View.getGLContext();
            return AWTTextureIO.newTexture(View.getProfile(), image, true);
        }
        return null;
    }
}