import java.util.LinkedList;

public class OurSchedule extends LinkedList<Job> {
    public OurSchedule(){
        super();
    }


    public int getTardiness(){
        int tardiness=0;
        int totalTime = 0;
        for(Job job: this){
            tardiness+=Math.max(0, (totalTime+job.processingTime)-job.dueTime);
            totalTime+=job.processingTime;
        }
        return tardiness;
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
}
