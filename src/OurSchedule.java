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
            this.tardiness += Math.max(0, this.totalTime + job.processingTime - job.dueTime);
            this.totalTime         += job.processingTime;


            if (job.biggerK(this.longestJob)) {
                this.longestJobIndex = this.size();
                this.longestJob = job;
            }
        }

        return super.add(job);
    }

    public Double getTardiness(Double timePassed){
        // Terminating Condition - 1 and 2 are satisfied here
        Double totalTardiness = 0d;
        Double finger = timePassed;
        for(Job job: this){
            totalTardiness += Math.max(0, finger + job.processingTime - job.dueTime);
            finger         += job.processingTime;
        }
        return totalTardiness;
    }

    public Double getMostTardyJob(int timePassed){
        Double maxTardiness = Double.MIN_VALUE;
        int finger = timePassed;
        for(Job job: this){
            Double tardinessCandidate = Math.max(0, finger + job.processingTime - job.dueTime);
            if(tardinessCandidate > maxTardiness){
                maxTardiness = tardinessCandidate;
            }
            finger         += job.processingTime;
        }
        return maxTardiness;
    }

    public int getProcessingTime(){
//        int time = 0;
//        for(Job job: this){
//            time+=job.processingTime;
//        }
        return totalTime;
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
                Comparator.comparingDouble(a->a.dueTime)
        );
    }
}
