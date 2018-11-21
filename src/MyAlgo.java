import com.sun.org.apache.xml.internal.serialize.OutputFormat;

import java.lang.*;
import java.util.*;

@SuppressWarnings("ALL")
public class MyAlgo{
    private int numJobs;
    private int[][] jobs;

    boolean printedOnce = false;
    int printCounter = 0;
    HashMap<Tuple<OurSchedule, Integer>, OurSchedule> memoization = new HashMap<Tuple<OurSchedule, Integer>, OurSchedule>();

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
        OurSchedule memoizationCandidate = memoization.get(new Tuple(schedule, timePassed));
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

        // Step3.3 - loop over all delta [0,N-kID]
        ArrayList<Integer> reducedDeltas = new ArrayList<>();
        if(true) {
            reducedDeltas = getReducedDeltas(schedule, (double) kJob.dueTime, kId, timePassed, level);
            if (level == -1){
                System.out.println("===== DELTA SITUATION (level=0) =====");
                System.out.println(" - Total Jobs : " + Integer.toString(N));
                Double reduction = 1 - reducedDeltas.size()/Double.valueOf(N-kId);
                System.out.println(" - Delta Len : " + Integer.toString(reducedDeltas.size()) + " || N-kId : " + Integer.toString(N-kId) + " || Reduction % in loops : " + reduction);
                System.out.println();
            }
        } else {
            for (int delta=0; delta <= N-kId; delta++){
                reducedDeltas.add(delta);
            }
        }
        Double minimumTardiness = Double.MAX_VALUE;
        int method = 0;
        for (int delta: reducedDeltas){
            OurSchedule jobsBranch1 = schedule.getSubset(0, kId-1).concatenate(schedule.getSubset(kId+1,kId+delta));
            OurSchedule jobsBranch2 = schedule.getSubset(0,kId+delta);
            Double ck = timePassed+jobsBranch2.getProcessingTime();
            OurSchedule jobsBranch3 = schedule.getSubset(kId+delta+1, N);

            // [NIELS] Does this have any effect?
            // Step 3.3.2 - Split into 3 branches
            OurSchedule scheduleBranch1  = new OurSchedule();
            OurSchedule scheduleBranch3  = new OurSchedule();
            if (method == 1){
                scheduleBranch1 = getSchedule(jobsBranch1, timePassed, level+1);
                scheduleBranch3 = getSchedule(jobsBranch3, ck, level+1);
            }else{
                // Method2 - Check if optimal schedule exists, if not, calculate and memoize it.
                OurSchedule candidateBranch1 = memoization.get(new Tuple(jobsBranch1, timePassed));

                if(candidateBranch1!=null){
                    // System.out.println("Found match for branch1");
                    scheduleBranch1 = candidateBranch1;
                }else{
                    scheduleBranch1 = getSchedule(jobsBranch1, timePassed, level+1);
                    memoization.put(new Tuple(jobsBranch1, timePassed), scheduleBranch1);
                }

                // Method2 - Check if optimal schedule exists, if not, calculate and memoize it.
                OurSchedule candidateBranch3 = memoization.get(new Tuple(jobsBranch3, ck));
                if(candidateBranch3!=null){
                    // System.out.println("Found match for branch3");
                    scheduleBranch3 = candidateBranch3;
                }else{
                    scheduleBranch3 = getSchedule(jobsBranch3, ck, level+1);
                    memoization.put(new Tuple(jobsBranch3, ck), scheduleBranch3);
                }

            }

            // Step 3.3.2 - Split into 3 branches
            //OurSchedule scheduleBranch1 = getSchedule(jobsBranch1, timePassed, level+1);
            //OurSchedule scheduleBranch3 = getSchedule(jobsBranch3, ck, level+1);

            OurSchedule scheduleBranch2 = new OurSchedule();
            scheduleBranch2.add(kJob);

            OurSchedule candidateSchedule = scheduleBranch1.concatenate(scheduleBranch2).concatenate(scheduleBranch3);

            Double candidateTardiness = candidateSchedule.getTardiness(timePassed);
            if (candidateTardiness < minimumTardiness){
                minimumTardiness = candidateTardiness;
                returnSchedule   = candidateSchedule;
            }
        }

        memoization.put(new Tuple(schedule, timePassed), returnSchedule);
        return returnSchedule;
    }

    ////////////////////////////////////// HELPER FUNCTIONS //////////////////////////////////////

    private ArrayList<Integer> getReducedDeltas(OurSchedule input_schedule, Double input_dueTime, int kId, Double timePassed, int level){
        ArrayList<Integer> deltas = new ArrayList<>();
        OurSchedule schedule = input_schedule.clone();
        Double d_k = input_dueTime;

        //Loop1
        while(true){
            Double dk_prime = Double.MIN_VALUE;

            // Loop2 : Updating the due date of Big-Brother (job with highest processing time)
            while(true) {
                // dk_prime = timePassed + schedule.getSubsetWithDeadlineLEQ(d_k).getProcessingTime();
                dk_prime = timePassed + schedule.getSubsetTimeWithDeadlineLEQ(d_k);
                if (dk_prime > d_k) {
                    d_k = dk_prime;
                }
                if(dk_prime<=d_k){
                    break;
                }
            }

            // Post-Loop-Break
            deltas.add(schedule.findIndex(schedule.findClosestNonTardyJob(d_k))-kId);
            OurSchedule S_double_prime = schedule.getSubsetWithDeadlineGthan(d_k);
            if(S_double_prime.size()!=0){
                d_k = S_double_prime.lowestDueTime.dueTime;
            } else{
                return deltas;
            }
        }
    }

    private void sortJobs(){
        Arrays.sort(jobs,
         Comparator.comparingInt(a->a[1])
        );
    }
}