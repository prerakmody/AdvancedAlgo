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

    private ArrayList<Integer> getReducedDeltas(OurSchedule input_schedule, Double input_dueTime, int kId, Double timePassed, int level){
        ArrayList<Integer> deltas = new ArrayList<>();
        OurSchedule schedule = input_schedule.clone();
        //Job kJob = input_kJob.clone();
        Double d_k = input_dueTime;
        //zeroth part

        //first part
        while(true){
            Double dk_prime = Double.MIN_VALUE;
            while(true) {

                dk_prime = timePassed + schedule.getSubsetWithDeadlineLEQ(d_k).getProcessingTime();
//                if(printCounter<5) {
//                    System.out.println("dk prima " + dk_prime);
//                    System.out.println(d_k);
//
//                }
                printCounter++;
                if (dk_prime > d_k) {
                    d_k = dk_prime;
                }
                if(dk_prime<=d_k){
                    break;
                }
            }
            //second part
//            if(level==5){
//                System.out.println("--------------");
//                System.out.println(schedule.findIndex(schedule.findClosestNonTardyJob(d_k)));
//                System.out.println(kId);
//            }
            deltas.add(schedule.findIndex(schedule.findClosestNonTardyJob(d_k))-kId);
            OurSchedule S_double_prime = schedule.getSubsetWithDeadlineGthan(d_k);
            if(S_double_prime.size()!=0){
                d_k = S_double_prime.lowestDueTime.dueTime;

            } else{
                return deltas;
            }

        }

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
//        if (level == 0){
//            System.out.println("\n * Level 0 : kJob : " + kJob);
//        }

        // Step3.3 - loop over all delta [0,N-kID]
        ArrayList<Integer> reducedDeltas = new ArrayList<>();
        if(true) {
            reducedDeltas = getReducedDeltas(schedule, (double) kJob.dueTime, kId, timePassed, level);
//            if(!printedOnce){
//                System.out.println("===========================");
//                System.out.println(schedule);
//                System.out.println(timePassed);
//                System.out.println(kJob.dueTime + " " + Integer.toString(kId));
//                System.out.println(reducedDeltas);
//
//                printedOnce=true;
//            }

        } else {
            for (int delta=0; delta <= N-kId; delta++){
                reducedDeltas.add(delta);
            }
        }

//        System.out.println(reducedDeltas.toString());

        Double minimumTardiness = Double.MAX_VALUE;
//        for (int delta=0; delta <= N-kId; delta++){
        for (int delta: reducedDeltas){
            //concatenate, getsubset, gettardiness are in n. maybe switch to index based system instead of physical lists?
            // Step3.3.1 - Split into 3 branches
//            OurSchedule jobsBranch1 = null;
//            if(memoization.get(new Quple())){
//
//            } else{
            OurSchedule jobsBranch2 = schedule.getSubset(0,kId+delta);
            Double ck = timePassed+jobsBranch2.getProcessingTime();

//            Quple quple1 = new Quple(0, kId+delta, kId+delta, timePassed);
//            Quple quple3 = new Quple(kId+delta+1, N-1, N-kId-delta-1, ck);
//            if(level==0) {
//                System.out.println(delta+" "+quple1);
//                System.out.println(quple3);
//                System.out.println();
//            }
//            if(memoization.get(quple1)!=null || memoization.get(quple3)!=null){
//                System.out.println("hitting memoization");
//            }
            OurSchedule jobsBranch1 = schedule.getSubset(0, kId-1).concatenate(schedule.getSubset(kId+1,kId+delta));
            OurSchedule jobsBranch3 = schedule.getSubset(kId+delta+1, N);


            // Step 3.3.2 - Split into 3 branches
            OurSchedule scheduleBranch1 = getSchedule(jobsBranch1, timePassed, level+1);
            OurSchedule scheduleBranch3 = getSchedule(jobsBranch3, ck, level+1);

            //only if better

//            if(memoization.get(quple1)==null) {
//                memoization.put(quple1, scheduleBranch1);
//            }
//            if(memoization.get(quple3)==null){
//                memoization.put(quple3, scheduleBranch3);
//            }


            OurSchedule scheduleBranch2 = new OurSchedule();
            scheduleBranch2.add(kJob);

            OurSchedule candidateSchedule = scheduleBranch1.concatenate(scheduleBranch2).concatenate(scheduleBranch3);

            if (candidateSchedule.getTardiness(timePassed) < minimumTardiness){
                minimumTardiness = candidateSchedule.getTardiness(timePassed);
                returnSchedule   = candidateSchedule;
            }
        }

//        memoization.put(new Quple(returnSchedule.getLowestDueTimeJobId(), returnSchedule.getHighestDueTimeJobId(), returnSchedule.size(), timePassed), returnSchedule);
        memoization.put(new Tuple(schedule, timePassed), returnSchedule);
        return returnSchedule;
    }

    ////////////////////////////////////// HELPER FUNCTIONS //////////////////////////////////////

    private void sortJobs(){
        Arrays.sort(jobs,
         Comparator.comparingInt(a->a[1])
        );
    }
}