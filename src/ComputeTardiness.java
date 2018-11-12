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
		String data_file_directory="/home/niels/projects/AdvancedAlgo/test-set/evenLessInstances/";
		String output_path="/home/niels/projects/AdvancedAlgo/outputBestFirst.csv";
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
//			result += String.format("%s;%s;%s;\n", file, greedySchedule.getTardiness(), runtimeGreedy);
			long startBestFirst   = System.currentTimeMillis();
//            int bestFirstTardiness = -1; //-1 implies error
			try {
				BestFirst bestFirst = new BestFirst(instance);
				Schedule bestFirstSchedule = bestFirst.getSchedule();

                long runtimeBestFirst = System.currentTimeMillis()-startBestFirst;
                result += String.format("%s;%s;%s;\n", file, bestFirstSchedule.getTardiness(), runtimeBestFirst);
			}
			catch(OutOfMemoryError e){
                result += String.format("%s;%s;%s;\n", file, -1, -1);
			}
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
		Double epsilon = Double.valueOf(args[0]);
		ProblemInstance instance = readInstance(args[1]);


		long startApproximate   = System.currentTimeMillis();
		MyAlgo2 myfunc = new MyAlgo2(instance);
		OurSchedule myFuncSchedule = myfunc.getSchedule(epsilon);
		long runtimeApproximate = System.currentTimeMillis()-startApproximate;

		long startOptimal = System.currentTimeMillis();
		MyAlgo myOptimalFunc = new MyAlgo(instance);
		OurSchedule myOptSchedule = myOptimalFunc.getSchedule();
		long runtimeOptimal = System.currentTimeMillis()-startOptimal;


		System.out.println(myOptSchedule.getTardiness(0)+ " " + myFuncSchedule.getTardiness(0));

		System.out.print(myFuncSchedule.getTardiness(0));
		System.out.println("\n -------------------------------- ");
		System.out.println("1. approximate min.Tard : " + Integer.toString(myFuncSchedule.getTardiness(0)));
        System.out.println("1. approximate runtime : " + (runtimeApproximate));

		System.out.println("2. exact min.Tard : " + Integer.toString(myOptSchedule.getTardiness(0)));
		System.out.println("2. exact runtime : " + (runtimeOptimal));

//		System.out.println("\n -------------------------------- ");
//		System.out.println("1. Greedy min.Tard : " + Integer.toString(greedySchedule.getTardiness()));
//		System.out.println("1. Greedy runtime : " + end1);
//		System.out.println(" -------------------------------- ");


	}
}
