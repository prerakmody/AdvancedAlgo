import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;



public class OurSchedule extends ArrayList<Job> {
    Double totalTime = 0d;
    int longestJobIndex = 0;
    Job longestJob = null;
    Job highestDueTime = null;
    Job lowestDueTime = null;
    //    Double tardiness = 0d;

    public OurSchedule(){
        super();
    }

    /* ------------------------------------------------------------------------------
        These functions are called in getSchedule()
     ------------------------------------------------------------------------------ */

    public boolean add(Job job){
        if(job!=null) {
//            this.tardiness += Math.max(0, this.totalTime + job.processingTime - job.dueTime);
            this.totalTime         += job.processingTime;

            // To find kJob with longest processing time
            if (job.biggerK(this.longestJob)) {
                this.longestJobIndex = this.size();
                this.longestJob = job;
            }

            // To find job with highest/lowest dueTime ([NIELS] do we have to do this at every stage???)
            if(highestDueTime==null || job.dueTime>highestDueTime.dueTime){
                highestDueTime = job;
            }
            if(lowestDueTime==null || job.dueTime<lowestDueTime.dueTime){
                lowestDueTime = job;
            }
        }

        return super.add(job);
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

    // [NIELS] Lets optimize this
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

    public Double getProcessingTime(){
//        int time = 0;
//        for(Job job: this){
//            time+=job.processingTime;
//        }
        return totalTime;
    }

    public Job getK(){
        return longestJob;
    }

    public int getKIndex(){
        return this.longestJobIndex;
    }

    /* ------------------------------------------------------------------------------
        These functions are called in getReducedDeltas()
     ------------------------------------------------------------------------------ */

    public OurSchedule getSubsetWithDeadlineLEQ(double timestamp){
        OurSchedule result = new OurSchedule();
        for(Job job:this){
            if(job.dueTime<=timestamp){
                result.add(job); //[NIELS] We could optimize this to not do job comarisons and directly return processingTime
            }
        }
        return result;
    }

    public Double getSubsetTimeWithDeadlineLEQ(double timestamp){
        Double result = 0D;
        for(Job job:this){
            if(job.dueTime<=timestamp){
                result += job.processingTime; //[NIELS] We could optimize this to not do job comarisons and directly return processingTime
            }
        }
        return result;
    }

    public OurSchedule getSubsetWithDeadlineGthan(double timestamp){
        OurSchedule result = new OurSchedule();
        for(Job job:this){
            if(job.dueTime>timestamp){
                result.add(job);
            }
        }
        return result;
    }

    public Job findClosestNonTardyJob(Double timestamp){
        //todo might be an edge case due to result vs job duetime
        Job result = null;
        for(Job job: this) {
            if(job.dueTime<=timestamp){
                if(result==null || result.dueTime<=job.dueTime){
                    result = job;
                }
            }
        }
        return result;
    }

    public int findIndex(Job job){
        int i = 0;
        for(Job otherJob: this){
            if(job.equals(otherJob)){
                break;
            }
            i++;
        }
        return i;
    }
    /* ---------------------------------------
        UNCATEGORIZED
     --------------------------------------- */
    public String toString(){
        String result = "[\n";
        for(Job job : this){
            result+=job.toString()+",\n";
        }
        result+="]";
        return result;
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

    public OurSchedule clone() {
        OurSchedule result = new OurSchedule();
        for(Job job: this){
            result.add(job.clone());
        }
        return result;
    }

    public int getProcessingTimeSum(){
        int result = 0;
        for(Job job: this){
            result+=job.processingTime;
        }
        return result;
    }

    public void sortJobs(){
        Collections.sort(this,
                Comparator.comparingDouble(a->a.dueTime)
        );
    }

    // UNUSED FUNCTIONS

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
}
