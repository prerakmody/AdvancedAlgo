import java.lang.*;
import java.util.Arrays;
import java.util.Comparator;

//private class orderJobsByAscendingDeadline implements Comparator<Integer[]>{
//    public orderJobsByAscendingDeadline(){
//
//    }
//@Override
//public int compare(Integer[]o1,Integer[]o2){
//        Integer quantityOne=o1[1]; //0 - p(j), 1 - d(j)
//        Integer quantityTwo=o2[1];
//        // reverse sort on quantity
//        return quantityOne.compareTo(quantityTwo);
//        }
//}

public class MyAlgo{
    private int numJobs;
    private int[][] jobs;

    // 1. Constructor
    public MyAlgo(ProblemInstance instance){
        numJobs = instance.getNumJobs();
        jobs    = instance.getJobs();
        sortJobs();
    }

    // 2. This function is called by the outside world
    public Schedule getSchedule(){
        /*
        1. Get the k value for all jobs
        2. Loop over all delta in (0, numJobs - k)
            2.1. Using each delta, create a DP problem
        */
        return getSchedule(jobs);
    }

    // 3. The private function
    private Schedule getSchedule(int[][] jobSet){
        int k = getK(jobSet);
        int N = jobSet.length;
        Schedule s = null;

        if (N == 0){
            return s;
        }
        //todo add second if clause
        int minimumTardiness = Integer.MAX_VALUE;
        for (int delta=0; delta < N-k; delta++){

            int [][] jobsBranch1 = constructBranch1(jobSet, 0, k-1, delta, k); // jobSet[0, k-1] + jobset(k-1, k+delta] - jobs[k]
            int jobsBranch2      = constructBranch2(jobsBranch1, jobSet[k][1]);
            int [][] jobsBranch3 = constructBranch3(jobSet, k+delta, N); // jobSet[k+delta, N]

            Schedule scheduleBranch1 = getSchedule(jobsBranch1);
            Schedule scheduleBranch3 = getSchedule(jobsBranch3);

            int tardinessBranch1 = scheduleBranch1.getTardiness();
            int tardinessBranch2 = jobsBranch2;
            int tardinessBranch3 = scheduleBranch3.getTardiness();

            int totalTardiness = tardinessBranch1+tardinessBranch2+tardinessBranch3;

            if (totalTardiness<minimumTardiness) {
                minimumTardiness=totalTardiness;
                // todo check that jobId is indeed irrelevant
                for(int[] job : jobsBranch1){
                    s = new Schedule(s,0, job[0], job[1]);
                }
                s = new Schedule(s,0,jobSet[k][0],jobSet[k][1]);
                for(int[] job : jobsBranch3){
                    s = new Schedule(s, 0, job[0], job[1]);
                }
            }
        }
        return s;
    }

    ////////////////////////////////////// HELPER FUNCTIONS //////////////////////////////////////
    private int[][] constructBranch3(int[][] jobSet, int start_idx, int end_idx){
        /*k+delta+1..n*/
        int[][] newJobSet;
        for (int i=start_idx; i <= end_idx; i++){
            newJobSet[i] = jobSet[i];
        }

        return newJobSet;
    }

    private int constructBranch2(int [][] jobSet, int kDeadline){

        int sum_pj = 0;
        for (int i=0; i< jobSet.length; i++){
            sum_pj += jobSet[i][0];
        }

        return Math.min(0, sum_pj - kDeadline);
    }

    private int[][] constructBranch1(int[][] jobSet, int start_idx, int end_idx, int delta, int k){
        /*1..k-1,   k+1..k+delta**/
        int[][] newJobSet;
        for (int i=start_idx; i <= end_idx; i++){ //0...k-1
            newJobSet[i] = jobSet[i];
        }
        if (delta > 0){
            for (int i=end_idx+1; i <= k+delta; i++){ //k-1...k+delta
                newJobSet[i] = jobSet[i];
            }
        }

        return newJobSet;
    }

    private void sortJobs(){
        Arrays.sort(jobs,
         Comparator.comparingInt(a->a[1])
        );
    }

    private int getK(int [][] jobs){
        int numJobs = jobs.length;
        int jobLengthK       = -1;
        int jobIDK       = -1;
        for (int i=0; i < numJobs; i++){ //job[i][0]=jobLength, job[i][1]=jobDueTime
            if (jobs[i][0] > jobLengthK){
                jobLengthK = jobs[i][0];
                jobIDK     = i;
            }
        }

        return jobIDK;
    }

}