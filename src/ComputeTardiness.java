import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ComputeTardiness {	
	public static ProblemInstance readInstance(String filename){
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

		long start = System.currentTimeMillis();

		Greedy greedy = new Greedy(instance);
		Schedule greedySchedule = greedy.getSchedule();
		System.out.println(greedySchedule.getTardiness());

		System.out.println("greedy runtime: "+(System.currentTimeMillis()-start));
		start = System.currentTimeMillis();

		try {
			BestFirst bestFirst = new BestFirst(instance);
			Schedule bestFirstSchedule = bestFirst.getSchedule();
			System.out.println(bestFirstSchedule.getTardiness());
		}
		catch(OutOfMemoryError e){
			System.out.println("best first oom error");
		}
        System.out.println("best first runtime: "+(System.currentTimeMillis()-start));
        start = System.currentTimeMillis();

		MyAlgo myfunc = new MyAlgo(instance);
		OurSchedule myFuncSchedule = myfunc.getSchedule();
		System.out.println(myFuncSchedule.getTardiness());
        System.out.println("myAlgo runtime: "+(System.currentTimeMillis()-start));
        start = System.currentTimeMillis();
	}
}
