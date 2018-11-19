public class Job {
    int id;
    Double processingTime;
    Double dueTime;
    public Job(int id, Double processingTime, Double dueTime){
        this.id=id;
        this.processingTime=processingTime;
        this.dueTime=dueTime;
    }

    public int hashCode(){
        return id;
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

    public boolean equals(Object other){
        if(other instanceof Job){
            Job otherJob = (Job) other;
            return this.id == otherJob.id;
        }
        return false;
    }

    public Job clone(){
        return new Job(this.id, this.processingTime, this.dueTime);
    }
}
