package libraries.other;

public class Vectors2<Obj> {
    public Obj x, y;

    public Vectors2() {}
    public Vectors2(Obj x, Obj y) {
        this.x = x;
        this.y = y;
    }
    public Vectors2<Obj> copy() {
        return new Vectors2<Obj>(x, y);
    }

    public String toString(){
        if (x != null && y != null)
            return x.toString() + y.toString();
        return "";
    }
}