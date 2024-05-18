package libraries.other;

public class MT {
    public static Vectors2<Float> solveQuadraticEquation(float a, float b, float c){
        double sqD = Math.sqrt(b * b - 4 * a * c);
        return new Vectors2<>((float) (-b - sqD) / (2 * a), (float) (-b + sqD) / (2 * a));
    }

    public static float pow(float value, int degree){
        for (; degree > 1; degree--){
            value *= value;
        }
        return value;
    }
    public static int pow(int value, int degree){
        for (; degree > 1; degree--){
            value *= value;
        }
        return value;
    }
    public static float squared(float value){
        return value * value;
    }

    public static int ceil(float value) {
        if (value % 1 != 0)
            return (int) (value + 1);
        return (int) value;
    }
}
