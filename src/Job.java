public class Job {
    int id;
    int processingTime;
    int dueTime;
    public Job(int id, int processingTime, int dueTime){
        this.id=id;
        this.processingTime=processingTime;
        this.dueTime=dueTime;

    }


    public String toString(){
        return "<id: "+this.id+", processingTime: "+this.processingTime+", dueTime: "+this.dueTime+">";
    }
}
