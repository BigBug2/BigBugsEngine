package game;

import engine.objects.GameObject;
import engine.objects.renderable.Renderable;
import engine.objects.renderable.actors.Button;
import engine.objects.renderable.actors.sprites.Text;
import game.actors.Pistol;
import game.actors.Player;
import engine.objects.renderable.actors.sprites.Sprite;
import engine.objects.renderable.packages.ClientModule;
import game.actors.Radio;
import game.actors.Shotgun;
import libraries.geometry.NewGeometry.*;
import libraries.geometry.SGeometry;
import libraries.graphics.Graphics;

public class Inventor extends ClientModule {
    public final int count;
    protected final Cell[] cells;
    protected int active = -1;

    private final Player player;

    public Inventor(GSource game, Figure body, int count) {
        super("inventor");
        this.count = count;

        this.player = game.getPlayer();

        Segment d = body.findPackingDiagonal();
        float width = (d.guidePoint.getX() - d.headPoint.getX()) / this.count, height = d.guidePoint.getY() - d.headPoint.getY();

        cells = new Cell[count];
        for (int i = 0; i < this.count; i++) {
            cells[i] = new Cell(new Rect(d.headPoint.getX() + width * i, d.guidePoint.getY(), width, height), -1, this);
        }
        cells[0].setTarget(OBJECTS.add(new Pistol(new Rect(player.location().getX(), player.location().getY(),
                2, 0.5f), player, game)));
        cells[1].setTarget(OBJECTS.add(new Radio(new Rect(player.location().getX() + 1, player.location().getY() + 1,
                0.5f, 2f), game)));
        cells[2].setTarget(OBJECTS.add(new Shotgun(new Rect(player.location().getX(), player.location().getY(),
                2, 0.5f), game)));
    }
    public boolean add(GameObject object) {
        for (int i = 0; i < cells.length; i++) {
            if (cells[i].target == -1) {
                cells[i].target = OBJECTS.add(object);
                return true;
            }
        }
        return false;
    }

    @Override
    public void process() {
        for (Cell cell : cells) {
            cell.process();
        }
        if (active != -1) {
            OBJECTS.get(active).process();
        }
    }
    @Override
    public void render() {
        for (Cell cell : cells) {
            cell.render();
        }
        if (active != -1) {
            GameObject act = OBJECTS.get(active);
            if (act instanceof Renderable) {
                ((Renderable) act).render();
            }
        }
    }

    @Override
    public AnchorPoint location() {
        return null;
    }

    public class Cell extends Button {
        private int target = -1;

        private Sprite sprite;
        private Figure innerBody;

        public Cell(Figure body, int target, ClientModule menu) {
            super("button/common", body, (e, a) -> {
                if (a == Button.ACTIVE) {
                    active = target;
                }
            }, menu);

            this.target = target;

            Segment d = body.findPackingDiagonal();
            float width = d.guidePoint.getX() - d.headPoint.getX(), height = d.guidePoint.getY() - d.headPoint.getY();
            innerBody = body.packInto(new SGeometry.SRect(d.headPoint.getX() + width * 0.1f, d.guidePoint.getY() - height * 0.1f,
                    width * 0.8f, height * 0.8f));
        }

        @Override
        public void render() {
            if (target != -1 && target == active) {
                setTexture(ACTIVE);
            }
            super.render();

            if (target != -1) {
                GameObject object = OBJECTS.get(target);
                if (object instanceof Renderable) {
                    Graphics.draw(innerBody, ((Renderable) object).getTexture());
                    if (getState() == AFFECTED) {
                        Segment d = innerBody.findPackingDiagonal();
                        d.guidePoint.setY(d.headPoint.getY() + (d.guidePoint.getY() - d.headPoint.getY()) / 3);
                        new Text(((Renderable) object).getName(), new Rect(d)).render();
                    }
                }
            }
        }

        void setTarget(int target) {
            this.target = target;
            function = (e, a) -> {
                if (a == Button.ACTIVE) {
                    active = target;
                }
            };
        }
    }
}
