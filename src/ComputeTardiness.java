import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

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

	public static void writeResultsForTestData(){
		String data_file_directory="/home/niels/projects/AdvancedAlgo/test-set/lessInstances/";
		String output_path="/home/niels/projects/AdvancedAlgo/outputMyAlgo.csv";
		// create a file that is really a directory
		File aDirectory = new File(data_file_directory);

		// get a listing of all files in the directory
		String[] filesInDir = aDirectory.list();
        Arrays.sort(filesInDir);
		String result = "fileName;resultMyAlgo;runtimeMyAlgo;\n";
		for(String file: filesInDir){
			ProblemInstance instance = readInstance(data_file_directory+file);

//			long startGreedy   = System.currentTimeMillis();
//			Greedy greedy = new Greedy(instance);
//			Schedule greedySchedule = greedy.getSchedule();
//			long runtimeGreedy     = System.currentTimeMillis()-startGreedy;
//			long startBestFirst   = System.currentTimeMillis();
//            int bestFirstTardiness = -1; //-1 implies error
//			try {
//				BestFirst bestFirst = new BestFirst(instance);
//				Schedule bestFirstSchedule = bestFirst.getSchedule();
////				System.out.println(bestFirstSchedule.getTardiness());
//                bestFirstTardiness = bestFirstSchedule.getTardiness();
//			}
//			catch(OutOfMemoryError e){
////				System.out.println("best first oom error");
//			}
////			int bestFirstTardiness = bestFirstSchedule==null? -1, bestFirstSchedule.getTardiness();
//			long runtimeBestFirst = System.currentTimeMillis()-startBestFirst;
////			System.out.println("best first runtime: "+(System.currentTimeMillis()-startBestFirst));
////			start = System.currentTimeMillis();

			long startMyFunc = System.currentTimeMillis();
			MyAlgo myfunc = new MyAlgo(instance);
			OurSchedule myFuncSchedule = myfunc.getSchedule();
			long runtimeMyFunc = System.currentTimeMillis()-startMyFunc;
			result += String.format("%s;%s;%s;\n", file, myFuncSchedule.getTardiness(0), runtimeMyFunc);
		}

        try(PrintWriter out = new PrintWriter(output_path)){
            out.print(result);
        }
        catch(FileNotFoundException e){
            System.out.println("be sure to have the destination be a valid path");
        }

	}

	// reads a problem, and outputs the result of both greedy and best-first
    public static void main (String args[]) {
//	    writeResultsForTestData();
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
