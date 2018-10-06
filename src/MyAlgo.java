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
    HashMap<OurSchedule, OurSchedule> memoization = new HashMap<OurSchedule, OurSchedule>();

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
            s.add(new Job(i, job[0], job[1]));
            i++;
        }
        return getSchedule(s, 0, 0);
    }

    // 3. The private function
    private OurSchedule getSchedule(OurSchedule schedule, int timePassed, int level){

        // Step3.0 - return if same schedule exists
//        if(k!=null) {
//            Quple candidateQuple = new Quple(i, j, timePassed, k.id);
//            if (k != null && memoization.containsKey(candidateQuple)) {
//                return memoization.get(candidateQuple);
//            }
//        }
        OurSchedule memoizationCandidate = memoization.get(schedule);
        if(memoizationCandidate!=null){
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
        if (level == 0){
            System.out.println("\n * Level 0 : kJob : " + kJob);
        }

        // Step3.3 - loop over all delta [0,N-kID]
        int minimumTardiness = Integer.MAX_VALUE;
        for (int delta=0; delta <= N-kId; delta++){

            // Step3.3.1 - Split into 3 branches
            OurSchedule jobsBranch1 = schedule.getSubset(0, kId-1).concatenate(schedule.getSubset(kId+1,kId+delta));
            OurSchedule jobsBranch2 = schedule.getSubset(0,kId+delta);
            int ck = timePassed+jobsBranch2.getProcessingTime();
            OurSchedule jobsBranch3 = schedule.getSubset(kId+delta+1, N);

            // Step 3.3.2 - Split into 3 branches
//            OurSchedule scheduleBranch1 = new OurSchedule();
//            if(!jobsBranch1.isEmpty()){
            OurSchedule scheduleBranch1 = getSchedule(jobsBranch1, timePassed, level+1);
//            }
//            OurSchedule scheduleBranch3 = new OurSchedule();
//            if(!jobsBranch3.isEmpty()){
            OurSchedule scheduleBranch3 = getSchedule(jobsBranch3, ck, level+1);
//            }

            OurSchedule scheduleBranch2 = new OurSchedule();
            scheduleBranch2.add(kJob);

            OurSchedule candidateSchedule = scheduleBranch1.concatenate(scheduleBranch2).concatenate(scheduleBranch3);
            Integer[] deltas = new Integer[]{94,95};
            List<Integer> deltasToFilter = Arrays.asList(deltas);
//            if(level==1 && scheduleBranch1.getTardiness(timePassed)==74857){
            if(level==0 && Arrays.asList(deltas).contains(delta)) {
                System.out.println(scheduleBranch1);
                System.out.println(scheduleBranch2);
                System.out.println(scheduleBranch3);
                System.out.println(candidateSchedule);
                OurSchedule printSchedule = candidateSchedule;
                System.out.println(ck);
                System.out.println(kJob.processingTime);
                System.out.println(scheduleBranch1.getTardiness(0));
                System.out.println(scheduleBranch3.getTardiness(0));
                System.out.println(" \n--> Level : " + Integer.toString(level) + " || Delta : " + Integer.toString(delta));
                System.out.println(" ----> getTardiness(timePassed) : " + printSchedule.getTardiness(timePassed));
//                String tardString = "[" + Integer.toString(tardinessBranch1) + ", " + Integer.toString(tardinessBranch2) + ", " + Integer.toString(tardinessBranch3) + " ]";
                // System.out.println(" ----> totalTardiness : " + Integer.toString(totalTardiness) + " " + tardString);
            }

            //if (totalTardiness < minimumTardiness) { //this is an ERROR!
            if (candidateSchedule.getTardiness(timePassed) < minimumTardiness){
                minimumTardiness = candidateSchedule.getTardiness(timePassed);
                returnSchedule   = candidateSchedule;
            }
        }

        memoization.put(schedule, returnSchedule);
        return returnSchedule;
    }

    ////////////////////////////////////// HELPER FUNCTIONS //////////////////////////////////////

    private void sortJobs(){
        Arrays.sort(jobs,
         Comparator.comparingInt(a->a[1])
        );
    }
}