import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;



public class OurSchedule extends ArrayList<Job> {

    int totalTime = 0;
    int tardiness = 0;
    int longestJobIndex = 0;
    Job longestJob = null;

    public OurSchedule(){
        super();
    }

    public boolean add(Job job){
        if(job!=null) {
            if (longestJob==null || job.processingTime >= longestJob.processingTime) {
                longestJobIndex = this.size();
                longestJob = job;
            }
        }

        return super.add(job);
    }

    public int getTardiness(int timePassed){
        // Terminating Condition - 1
        if(this.size()==0){
            return 0;
        }

        // Terminating Condition - 2 is satisfied here (if this.size() == 1)
        int totalTardiness = 0;
        int finger = timePassed;
        for(Job job: this){
            totalTardiness += Math.max(0, finger + job.processingTime - job.dueTime);
            finger         += job.processingTime;
        }
        return totalTardiness;
    }

    public int getProcessingTime(){
        int time = 0;
        for(Job job: this){
            time+=job.processingTime;
        }
        return time;
    }

    public OurSchedule getSubset(int startIndex, int endIndex){
        OurSchedule newSchedule = new OurSchedule();
        int i = 0;
        for(Job job: this){
            if(startIndex<=i && i<=endIndex){
                newSchedule.add(job);
            }
            i++;
        }
        return newSchedule;
    }

    public void setTotalTime(int totalTime){
        this.totalTime=totalTime;
    }
    public OurSchedule concatenate(OurSchedule otherSchedule){
//        int timepassed = this.totalTime<otherSchedule.totalTime? this.totalTime: otherSchedule.totalTime;
        OurSchedule newSchedule = new OurSchedule();

        for(Job job: this){
            newSchedule.add(job);
        }
        for(Job job: otherSchedule){
            newSchedule.add(job);
        }
        return newSchedule;
    }

    public Job getK(){
        return longestJob;
    }

    public int getKIndex(){
        return this.longestJobIndex;
    }

    public int getProcessingTimeSum(){
        int result = 0;
        for(Job job: this){
            result+=job.processingTime;
        }
        return result;
    }

//    public int hashCode(){
//        if(this.isEmpty()){
//            return 0;
//        }
//        return this.get(0).id*this.longestJobIndex*this.size();
//    }

    public String toString(){
        String result = "[\n";
        for(Job job : this){
            result+=job.toString()+",\n";
        }
        result+="]";
        return result;
    }

    public void sortJobs(){
        Collections.sort(this,
                Comparator.comparingInt(a->a.dueTime)
        );
    }
}
