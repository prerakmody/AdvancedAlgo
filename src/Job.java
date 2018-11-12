public class Job {
    int id;
    int processingTime;
    int dueTime;
    public Job(int id, int processingTime, int dueTime){
        this.id=id;
        this.processingTime=processingTime;
        this.dueTime=dueTime;
    }

    public int hashCode(){
        return id*processingTime*dueTime;
    }

    public String toString(){
        return "<id: "+this.id+", processingTime: "+this.processingTime+", dueTime: "+this.dueTime+">";
    }

    public boolean biggerK(Job other){
        //returns true if this is bigger k job than other
        if (other==null ||
                this.processingTime>other.processingTime ||
                (this.processingTime == other.processingTime && this.dueTime>other.dueTime)) {
            return true;
        }
        return false;
    }
}
