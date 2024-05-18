package game;

import engine.controllers.view.View;
import engine.objects.renderable.actors.Block;
import game.actors.Player;
import engine.objects.renderable.packages.GameModule;
import game.actors.Enemy;
import libraries.geometry.NewGeometry.*;
import libraries.physics.Physics;

import java.util.ArrayList;
import java.util.Arrays;

public class GSource extends GameModule {
    int player, anInterface;

    public GSource() {
        super("gsource");

        ((Block) OBJECTS.get(OBJECTS.add(new Block(new Rect(-View.mWindowWidth() / 2, View.mWindowHeight() / 2, View.mWindowWidth(), View.mWindowHeight()),
                Physics.EMPTY)))).setStatic(true);

        player = OBJECTS.add(new Player(new Rect(0, 0, 2, 2), this));

        OBJECTS.add(new Enemy(new Rect(30, 20, 2, 2), (Player) OBJECTS.get(player), this));

        anInterface = OBJECTS.add(new GInterface(this));

        OBJECTS.add(new Factory(this));
    }

    @Override
    public void process() {
        processAll();
    }

    public Player getPlayer() {
        return (Player) OBJECTS.get(player);
    }
    public GInterface getInterface() {
        return (GInterface) OBJECTS.get(anInterface);
    }

    //it represents a zoned world, where the coordinate means the numbers of the area horizontally and vertically
    public class World extends Shape {
        // in meters
        private static final float DISTRICT_SIZE = 50;
        // in districts
        private static final int PROCESSING_ZONE_SIZE = 4;

        public World(ArrayList<Point> vertexes) {
            super(vertexes);
        }
        public World() {
            this(new ArrayList<>(Arrays.asList(new Point[]{new GlobalPoint(0, 0), new GlobalPoint(1, 0),
                    new GlobalPoint(1, 1), new GlobalPoint(0, 1)})));
        }

        public void process() {
            // creating processing zone
            Rect processingZone = new Rect((int) (getPlayer().location().getX() / DISTRICT_SIZE) - PROCESSING_ZONE_SIZE / 2,
                    (int) (getPlayer().location().getY() / DISTRICT_SIZE) - PROCESSING_ZONE_SIZE / 2,
                    PROCESSING_ZONE_SIZE, PROCESSING_ZONE_SIZE);
        }
    }
}
