package engine.objects.renderable.actors;

import engine.objects.renderable.actors.sprites.Viewer;
import libraries.geometry.NewGeometry.*;
import libraries.physics.Physics;

public abstract class Creating extends ActiveActor {
    protected int health, maxHealth;

    private final Viewer healthViewer;
    private long healthUpdateTime = System.nanoTime();

    private static final long healthViewingTime = 3 * Physics.SECOND;

    public Creating(String name, Figure body, Physics.Material material, int health) {
        this(name, body, material, health, health);
    }
    public Creating(String name, Figure body, Physics.Material material, int health, int maxHealth) {
        super(name, body, material);

        this.health = health;
        this.maxHealth = maxHealth;

        Segment d = body.findPackingDiagonal();
        float width = d.guidePoint.getX() - d.headPoint.getX(), height =
                d.guidePoint.getY() - d.headPoint.getY();
        healthViewer = new Viewer("jigger/way", new Rect(location().getX() - width / 2, location().getY() + (5 * height / 8), width, height / 8),
                this::getHealth, maxHealth);
        healthViewer.tie(location());
    }

    @Override
    public void process() {
        super.process();

        healthViewer.tie(location());

        if (health <= 0) {
            destroy();
        }
    }
    @Override
    public void render() {
        super.render();
        if (System.nanoTime() - healthUpdateTime < healthViewingTime) {
            healthViewer.render();
        }
    }

    @Override
    public void setBody(Figure body) {
        super.setBody(body);

        if (healthViewer != null) {
            healthViewer.tie(location());
        }
    }

    public void addHealth(int v) {
        health = Math.max(Math.min(health + v, maxHealth), 0);
        healthUpdateTime = System.nanoTime();
    }

    public int getHealth() {
        return health;
    }

    public Integer getHealth(Void unused) {
        return health;
    }
}
