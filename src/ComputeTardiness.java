import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ComputeTardiness {	
	public static ProblemInstance readInstance(String filename){
		System.out.println(" ------- ");
		System.out.println(" - Filename : " + filename);
		System.out.println(" ------- \n");
		ProblemInstance instance = null;
		
		try {
			int numJobs = 0;
			int[][] jobs = null;
			
			Scanner sc = new Scanner(new BufferedReader(new FileReader(filename)));
			if(sc.hasNextInt()){
				numJobs = sc.nextInt();
				jobs = new int[numJobs][2];
				int nextJobID = 0;
			
				while (sc.hasNextInt() && nextJobID < numJobs) {
					jobs[nextJobID][0] = sc.nextInt();
					jobs[nextJobID][1] = sc.nextInt();
					nextJobID++;
				}
			}
			sc.close();
			
			instance = new ProblemInstance(numJobs, jobs);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return instance;
	}

	// reads a problem, and outputs the result of both greedy and best-first
    public static void main (String args[]) {
		ProblemInstance instance = readInstance(args[0]);

		long start1   = System.currentTimeMillis();
		Greedy greedy = new Greedy(instance);
		Schedule greedySchedule = greedy.getSchedule();
		long end1     = System.currentTimeMillis()-start1;
//		try {
//			BestFirst bestFirst = new BestFirst(instance);
//			Schedule bestFirstSchedule = bestFirst.getSchedule();
//			System.out.println(bestFirstSchedule.getTardiness());
//		}
//		catch(OutOfMemoryError e){
//			System.out.println("best first oom error");
//		}
//        System.out.println("best first runtime: "+(System.currentTimeMillis()-start));
//        start = System.currentTimeMillis();

		long start = System.currentTimeMillis();
		MyAlgo myfunc = new MyAlgo(instance);
		OurSchedule myFuncSchedule = myfunc.getSchedule();
		System.out.println("\n -------------------------------- ");
		System.out.println("1. myAlgo min.Tard : " + Integer.toString(myFuncSchedule.getTardiness(0)));
        System.out.println("1. myAlgo runtime : " + (System.currentTimeMillis()-start));

		System.out.println("\n -------------------------------- ");
		System.out.println("1. Greedy min.Tard : " + Integer.toString(greedySchedule.getTardiness()));
		System.out.println("1. Greedy runtime : " + end1);
		System.out.println(" -------------------------------- ");
	}
}
