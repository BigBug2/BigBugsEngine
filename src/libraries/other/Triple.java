package libraries.other;

public class Triple<T1, T2, T3> {
    public T1 f;
    public T2 s;
    public T3 t;

    public Triple(){};

    public Triple(T1 f, T2 s, T3 t) {
        this.f = f;
        this.s = s;
        this.t = t;
    }
}
