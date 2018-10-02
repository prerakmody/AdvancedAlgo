public class Tuple<X, Y> {
    public final X x;
    public final Y y;
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public int hashCode(){
        return x.hashCode()*y.hashCode();
    }

    public boolean equals(Object other){
        if(other instanceof Tuple){
            Tuple otherTuple= (Tuple) other;
            return this.x.equals(otherTuple.x) && this.y.equals(otherTuple.y);
        }
        return false;
    }
}
