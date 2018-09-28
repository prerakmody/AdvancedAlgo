import java.util.LinkedList;



public class OurSchedule extends LinkedList<Job> {

    int totalTime = 0;
    int tardiness = 0;
    int longestJobIndex = 0;
    Job longestJob = null;

    public OurSchedule(){
        super();
    }

    public boolean add(Job job){
        if(job!=null) {
            tardiness += Math.max(0, (this.totalTime + job.processingTime) - job.dueTime);
            totalTime += job.processingTime;
            if (longestJob==null || job.processingTime < longestJob.processingTime) {
                longestJobIndex = this.size();
                longestJob = job;
            }
        }

        return super.add(job);
    }

    public int getTardiness(){
        return this.tardiness;
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
}
