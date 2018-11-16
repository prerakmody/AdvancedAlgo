import com.sun.org.apache.xml.internal.serialize.OutputFormat;

import java.lang.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("ALL")
public class MyAlgo{
    private int numJobs;
    private int[][] jobs;
    HashMap<Quple<Integer, Integer, Integer, Double>, OurSchedule> memoization = new HashMap<>();

    // 1. Constructor
    public MyAlgo(ProblemInstance instance){
        numJobs = instance.getNumJobs();
        jobs    = instance.getJobs();
        sortJobs();
    }

    // 2. This function is called by the outside world
    public OurSchedule getSchedule(){
        OurSchedule s = new OurSchedule();
        int i=0;
        for(int[] job: jobs){ //on the sorted jobs
            s.add(new Job(i, new Double(job[0]), new Double(job[1])));
            i++;
        }
        return getSchedule(s, 0d, 0);
    }

    // 3. The private function
    private OurSchedule getSchedule(OurSchedule schedule, Double timePassed, int level){

        // Step3.0 - return if same schedule exists
        OurSchedule memoizationCandidate = memoization.get(new Quple(schedule.getLowestDueTimeJobId(), schedule.getHighestDueTimeJobId(), schedule.size(), timePassed));
        if(memoizationCandidate!=null){
//            System.out.println("hit memoization at level "+level+ " " +schedule.getLowestDueTimeJobId() + " "+ schedule.getHighestDueTimeJobId());
            return memoizationCandidate;
        }

        // Step3.1 - return if terminating conditions --> schedule.size == {0,1}
        int N = schedule.size();
        OurSchedule returnSchedule = new OurSchedule();
        if (N == 0){
            return returnSchedule;
        }
        if(N==1) {
            return schedule;
        }

        // Step3.2 - Calculate max{processingTime} for this schedule
        Job kJob = schedule.getK();
        int kId = schedule.getKIndex();
//        if (level == 0){
//            System.out.println("\n * Level 0 : kJob : " + kJob);
//        }

        // Step3.3 - loop over all delta [0,N-kID]
        Double minimumTardiness = Double.MAX_VALUE;
        for (int delta=0; delta <= N-kId; delta++){
            //concatenate, getsubset, gettardiness are in n. maybe switch to index based system instead of physical lists?
            // Step3.3.1 - Split into 3 branches
//            OurSchedule jobsBranch1 = null;
//            if(memoization.get(new Quple())){
//
//            } else{
            OurSchedule jobsBranch2 = schedule.getSubset(0,kId+delta);
            Double ck = timePassed+jobsBranch2.getProcessingTime();

            Quple quple1 = new Quple(0, kId+delta, kId+delta, timePassed);
            Quple quple3 = new Quple(kId+delta+1, N-1, N-kId-delta-1, ck);
            if(level==0) {
                System.out.println(delta+" "+quple1);
                System.out.println(quple3);
                System.out.println();
            }
            if(memoization.get(quple1)!=null || memoization.get(quple3)!=null){
                System.out.println("hitting memoization");
            }
            OurSchedule jobsBranch1 = memoization.get(quple1)!=null ? memoization.get(quple1) : schedule.getSubset(0, kId-1).concatenate(schedule.getSubset(kId+1,kId+delta));
            OurSchedule jobsBranch3 = memoization.get(quple3)!=null ? memoization.get(quple3) : schedule.getSubset(kId+delta+1, N);


            // Step 3.3.2 - Split into 3 branches
            OurSchedule scheduleBranch1 = getSchedule(jobsBranch1, timePassed, level+1);
            OurSchedule scheduleBranch3 = getSchedule(jobsBranch3, ck, level+1);

            //only if better

            if(memoization.get(quple1)==null) {
                memoization.put(quple1, scheduleBranch1);
            }
            if(memoization.get(quple3)==null){
                memoization.put(quple3, scheduleBranch3);
            }


            OurSchedule scheduleBranch2 = new OurSchedule();
            scheduleBranch2.add(kJob);

            OurSchedule candidateSchedule = scheduleBranch1.concatenate(scheduleBranch2).concatenate(scheduleBranch3);

            if (candidateSchedule.getTardiness(timePassed) < minimumTardiness){
                minimumTardiness = candidateSchedule.getTardiness(timePassed);
                returnSchedule   = candidateSchedule;
            }
        }

//        memoization.put(new Quple(returnSchedule.getLowestDueTimeJobId(), returnSchedule.getHighestDueTimeJobId(), returnSchedule.size(), timePassed), returnSchedule);
        return returnSchedule;
    }

    ////////////////////////////////////// HELPER FUNCTIONS //////////////////////////////////////

    private void sortJobs(){
        Arrays.sort(jobs,
         Comparator.comparingInt(a->a[1])
        );
    }
}