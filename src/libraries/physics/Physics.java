package libraries.physics;

public class Physics {
    public static final float g = 10f;
    public static long SECOND = 1000000000;

    public static final Material WATER = new Material("water", 1, 0.01f);
    public static final Material SOIL = new Material("soil", 1, 0.8f);
    public static final Material FLESH = new Material("flesh", 0.7f, 0.2f);
    public static final Material IRON = new Material("iron", 8.7f, 0.1f);
    public static final Material EMPTY = new Material("empty", 0f, 0f);

    public static class Material {
        public final String name;
        public final float p;
        public final float elasticity;

        public Material(String aName, float p, float elasticity) {
            name = aName;
            this.p = p;

            this.elasticity = elasticity;
        }
    }
}