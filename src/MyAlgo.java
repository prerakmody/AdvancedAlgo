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
    public OurSchedule getSchedule(){
        /*
        1. Get the k value for all jobs
        2. Loop over all delta in (0, numJobs - k)
            2.1. Using each delta, create a DP problem
        */

//        int jobID = -1;
//        int jobLength = -1;
//        int jobDueTime = -1;
        OurSchedule s = new OurSchedule();
        int i=0;
        for(int[] job: jobs){
            s.add(new Job(i, job[0], job[1]));
//            jobID = i;
//            jobLength = jobs[i][0];
//            jobDueTime = jobs[i][1];
            i++;
        }



        return getSchedule(s, 0);
    }

    // 3. The private function
    private OurSchedule getSchedule(OurSchedule schedule, int timePassed){

//        System.out.println(schedule);
        int N = schedule.size();
        OurSchedule returnSchedule = new OurSchedule();

        if (N == 0){
            return returnSchedule;
        }

        if(schedule.size()==1){
            return schedule;
        }

        Job kJob = getK(schedule);
//        int kId = kJob.id;
        int kId = schedule.indexOf(kJob);
        //todo add second if clause
        int minimumTardiness = Integer.MAX_VALUE;
        for (int delta=0; delta < N-kId; delta++){

            OurSchedule jobsBranch1 = schedule.getSubset(0, kId-1).concatenate(schedule.getSubset(kId+1,kId+delta));
            OurSchedule jobsBranch2 = schedule.getSubset(0,kId+delta);
            OurSchedule jobsBranch3 = schedule.getSubset(kId+delta+1, N);
//            int [][] jobsBranch1 = constructBranch1(jobSet, 0, k-1, delta, k); // jobSet[0, k-1] + jobset(k-1, k+delta] - jobs[k]
//            int jobsBranch2      = constructBranch2(jobsBranch1, jobSet[k][1]);
//            int [][] jobsBranch3 = constructBranch3(jobSet, k+delta+1, N); // jobSet[k+delta, N]

            int ck = jobsBranch2.getProcessingTime();//todo with or without timepassed


            OurSchedule scheduleBranch1 = getSchedule(jobsBranch1, timePassed);
            OurSchedule scheduleBranch3 = getSchedule(jobsBranch3, ck);

            int tardinessBranch1 = scheduleBranch1.getTardiness();
            int tardinessBranch2 = Math.max(0, ck-kJob.dueTime);
            int tardinessBranch3 = scheduleBranch3.getTardiness();

            int totalTardiness = tardinessBranch1+tardinessBranch2+tardinessBranch3;
            OurSchedule scheduleBranch2 = new OurSchedule();
            scheduleBranch2.add(kJob);
            OurSchedule candidateSchedule = scheduleBranch1.concatenate(scheduleBranch2).concatenate(scheduleBranch3);
//            System.out.println(totalTardiness);
//            System.out.println(candidateSchedule.getTardiness());
//            System.out.println("kId: "+kId);
//            System.out.println(scheduleBranch1);
//            System.out.println(scheduleBranch2);
//            System.out.println(scheduleBranch3);
//            System.out.println(schedule);
//            System.out.println(candidateSchedule);

//            System.out.println(totalTardiness);

            if (totalTardiness<minimumTardiness) {
                minimumTardiness=candidateSchedule.getTardiness();
                // todo check that jobId is indeed irrelevant
                returnSchedule = candidateSchedule;
            }
        }
        return returnSchedule;
    }

    ////////////////////////////////////// HELPER FUNCTIONS //////////////////////////////////////

    private void sortJobs(){
        Arrays.sort(jobs,
         Comparator.comparingInt(a->a[1])
        );
    }

    private Job getK(OurSchedule schedule){
        Job bestK     = null;
        int bestKLength = -1;
        for(Job job : schedule) {
            if (job.processingTime > bestKLength) {
                bestKLength = job.processingTime;
                bestK = job;
            }
        }

        return bestK;
    }

}