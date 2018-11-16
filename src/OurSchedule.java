import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;



public class OurSchedule extends ArrayList<Job> {
    Double totalTime = 0d;
//    Double tardiness = 0d;
    int longestJobIndex = 0;
    Job longestJob = null;
    Job highestDueTime = null;
    Job lowestDueTime = null;
//    int firstJobId = -1;
//    int lastJobId = -1;
//    Double startTime = 0d;

    public OurSchedule(){
        super();
//        this.startTime=0d;
//        this.totalTime=this.startTime;
    }

    public boolean add(Job job){
        if(job!=null) {
//            this.tardiness += Math.max(0, this.totalTime + job.processingTime - job.dueTime);
            this.totalTime         += job.processingTime;


            if (job.biggerK(this.longestJob)) {
                this.longestJobIndex = this.size();
                this.longestJob = job;
            }

            if(highestDueTime==null || job.dueTime>highestDueTime.dueTime){
                highestDueTime = job;
            }
            if(lowestDueTime==null || job.dueTime<lowestDueTime.dueTime){
                lowestDueTime = job;
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

    public int getLowestDueTimeJobId(){
        if(lowestDueTime==null){
            return -1;
        }
        return lowestDueTime.id;
    }

    public int getHighestDueTimeJobId(){
        if(highestDueTime==null){
            return -1;
        }
        return highestDueTime.id;
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

    public Double getProcessingTime(){
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

//    public void setTotalTime(int totalTime){
//        this.totalTime=totalTime;
//    }
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

//    public int hashCode(){
//        if(this.size()==0){
//            return -1;
//        }
//        return this.get(0).id*(int)Math.pow(this.lastJob.id,2)*(int)Math.pow(this.size(),3);
//    }
//
//    public boolean equals(Object other){
//        if(other instanceof OurSchedule){
//            OurSchedule otherSchedule = (OurSchedule) other;
//            //todo consider adding timestarted to constructor to allow full comparison
//            if(this.size()==otherSchedule.size() &&
//                    this.longestJob.equals(otherSchedule.longestJob) &&
//                    this.lastJob.equals(otherSchedule.lastJob)
//            ){
//                return true;
//            }
//            //remove this
////            return super.equals(otherSchedule);
////            for(int i=0; i<this.size(); i++){
////                if(!this.get(i).equals(otherSchedule.get(i))){
////                    return false;
////                }
////            }
////            return true;
//        }
//        return false;
//    }

    public void sortJobs(){
        Collections.sort(this,
                Comparator.comparingDouble(a->a.dueTime)
        );
    }
}
