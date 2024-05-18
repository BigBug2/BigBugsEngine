package game.actors;

import engine.objects.renderable.actors.Actor;
import libraries.geometry.NewGeometry.*;

import java.util.function.Function;

public abstract class Ability extends Actor {
    private final Function<Integer, Boolean> isUsed;
    private int countOfUsed = 0;
    private long startUsing;
    private final long usingTime;


    private final int clip;

    private long startReloading;
    private final long reloadTime;

    public Ability(String name, Figure body, Function<Integer, Boolean> isUsed, long usingTime, int clip, long reloadTime) {
        super(name, body);

        this.isUsed = isUsed;

        this.usingTime = usingTime;

        this.clip = clip;
        this.reloadTime = reloadTime;
    }

    @Override
    public void process() {
        if (System.nanoTime() - startReloading > reloadTime && System.nanoTime() - startUsing > usingTime && isUsed.apply(countOfUsed)) {
            if (countOfUsed > clip) {
                reload();
            } else {
                startUsing = System.nanoTime();
                countOfUsed++;
                use();
            }
        }
    }
    public abstract void use();

    public void reload() {
        startReloading = System.nanoTime();
        countOfUsed = 0;
    }
}
