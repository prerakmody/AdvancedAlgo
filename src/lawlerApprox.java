import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class lawlerApprox {

    private int numJobs;
    private int[][] jobs;
    HashMap<Tuple<OurSchedule, Integer>, OurSchedule> memoization = new HashMap<Tuple<OurSchedule, Integer>, OurSchedule>();

    // 1. Constructor
    public lawlerApprox(ProblemInstance instance){
        numJobs = instance.getNumJobs();
        jobs    = instance.getJobs();
        sortJobsEDD();


    }

    // 2. This function is called by the outside world
    public OurSchedule getSchedule(Double epsilon){
        OurSchedule scaledSchedule = new OurSchedule();
        OurSchedule schedule = new OurSchedule();
        int i = 0;
        for(int[] job: jobs){ //on the sorted jobs
            Double processingTime = new Double(job[0]);
            Double dueTime = new Double(job[1]);
            schedule.add(new Job(i, processingTime, dueTime));
            scaledSchedule.add(new Job(i, processingTime, dueTime));
            i++;
        }
        Double Tmax = schedule.getMostTardyJob(0);

        if(Tmax == 0){
            return schedule;
        }
        int n = schedule.size();
        Double K = (2*epsilon*Tmax)/(n*(n+1));

        for(Job job: scaledSchedule){
            job.processingTime = Math.floor(job.processingTime/K);
            job.dueTime = job.dueTime/K;
        }
        OurSchedule approximateScaledSchedule = this.getSchedule(scaledSchedule,0d,0);
        OurSchedule resultSchedule = new OurSchedule();
        for(Job scaledJob: approximateScaledSchedule){
            for(Job job: schedule){
                if(scaledJob.id==job.id){
                    resultSchedule.add(job);
                    break;
                }
            }
        }
        return resultSchedule;
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
        int rand = (int)(Math.random() * 1000);
        int count = 0;
        for (int delta: reducedDeltas){
            count += 1;
            OurSchedule jobsBranch1 = schedule.getSubset(0, kId-1).concatenate(schedule.getSubset(kId+1,kId+delta));
            OurSchedule jobsBranch2 = schedule.getSubset(0,kId+delta);
            Double ck = timePassed+jobsBranch2.getProcessingTime();
            OurSchedule jobsBranch3 = schedule.getSubset(kId+delta+1, N);

            // [NIELS] Does this have any effect?
            // Step 3.3.2 - Split into 3 branches
            OurSchedule scheduleBranch1 = getSchedule(jobsBranch1, timePassed, level+1);
            OurSchedule scheduleBranch3 = getSchedule(jobsBranch3, ck, level+1);

            OurSchedule scheduleBranch2 = new OurSchedule();
            scheduleBranch2.add(kJob);

            OurSchedule candidateSchedule = scheduleBranch1.concatenate(scheduleBranch2).concatenate(scheduleBranch3);

            Double candidateTardiness = candidateSchedule.getTardiness(timePassed);
            if (level == -1){
                System.out.println(" -> Level 0 (" + Integer.toString(count)+ "/" + Integer.toString(reducedDeltas.size()) + ") || candidateTardiness = " + candidateTardiness
                        + " || (branch1.size)=" + scheduleBranch1.size() + " || (branch2.size)=" + scheduleBranch2.size() + " || (branch3.size)=" + scheduleBranch3.size());
                System.out.println("\n\n");
            }
            if (level == -1){
                System.out.println(" ---> Level 1 (" + Integer.toString(rand) + ")|| delta : " + Integer.toString(delta) +
                        " ||  candidateTardiness = " + candidateTardiness + "(schedule.size=" + Integer.toString(candidateSchedule.size()) + ")");
            }
            if (candidateTardiness < minimumTardiness){
                minimumTardiness = candidateTardiness;
                returnSchedule   = candidateSchedule;
                if (minimumTardiness == 0d){
                    break;
                }
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

    private void sortJobsEDD(){
        Arrays.sort(jobs,
                Comparator.comparingInt(a->a[1])
        );
    }
}
