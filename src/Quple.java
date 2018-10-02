public class Quple<W, X, Y, Z> {
    public final W w;
    public final X x;
    public final Y y;
    public final Z z;
    public Quple(W w, X x, Y y, Z z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int hashCode(){
        return w.hashCode()*x.hashCode()*y.hashCode()*z.hashCode();
    }

    public boolean equals(Object other){
        if(other instanceof Quple){
            Quple otherQuple= (Quple) other;
            return this.x.equals(otherQuple.x) && this.y.equals(otherQuple.y) && w.equals(otherQuple.w) && z.equals(otherQuple.z);
        }
        return false;
    }
}
