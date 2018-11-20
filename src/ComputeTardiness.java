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
		String data_file_directory="/home/niels/projects/AdvancedAlgo/test-set/instances/";
		String output_path="/home/niels/projects/AdvancedAlgo/output.csv";
//		String[] methods = new String[]{"bestfirst", "lawler"};
//		int[][] rdds_and_tfs = new int[][]{new int[]{30,30}, new int[]{60,60}};
//		int[] jobcounts = new int[]{30,60};

        File aDirectory = new File(data_file_directory);
        String[] filesInDir = aDirectory.list();
        Arrays.sort(filesInDir);
//
//        String[] regexes = new String[]{"#(30|60)", "RDD=0.[36]_TF=0.[36]"};
        String fileFilterRegex = ".*(?:#80).*";
//        String fileFilterRegex = ".*(?:#(?:30|60)|RDD=0\\.[46]_TF=0\\.[46]).*";

        String result = "fileName;resultBestFirst;resultLawler;runtimeBestFirst;runtimeLawler;\n";
        for(String fileName: filesInDir){
            // filter files out of scope
            if(!fileName.matches(fileFilterRegex)){
                System.out.println(fileName);
                continue;
            }

            ProblemInstance instance = readInstance(data_file_directory+fileName);
            long startBestFirst   = System.currentTimeMillis();
            long runtimeBestFirst = -1;
            long tardinessBestFirst = -1;

//            try {
//                BestFirst bestFirst = new BestFirst(instance);
//                Schedule bestFirstSchedule = bestFirst.getSchedule();
//
//                tardinessBestFirst = bestFirstSchedule.getTardiness();
//                runtimeBestFirst = System.currentTimeMillis()-startBestFirst;
//            }
//            catch(OutOfMemoryError e){
//                    //error values are preset at initialization
//            }


            long startLawler = System.currentTimeMillis();
            MyAlgo lawlerFunc = new MyAlgo(instance);
            OurSchedule lawlerSchedule = lawlerFunc.getSchedule();
            long lawlerRuntime = System.currentTimeMillis()-startLawler;
            Double lawlerTardiness = lawlerSchedule.getTardiness(0d);

            System.out.println(String.format("%s;%s;%s;%s;%s;\n", fileName, tardinessBestFirst, lawlerTardiness, runtimeBestFirst, lawlerRuntime));
            result += String.format("%s;%s;%s;%s;%s;\n", fileName, tardinessBestFirst, lawlerTardiness, runtimeBestFirst, lawlerRuntime);
        }

        try(PrintWriter out = new PrintWriter(output_path)){
            out.print(result);
        }
        catch(FileNotFoundException e){
            System.out.println("be sure to have the destination be a valid path");
        }




//		for(String method: methods){
//            for(int[] rdd_and_tf: rdds_and_tfs ){
//                int rdd = rdd_and_tf[0];
//                int tf = rdd_and_tf[1];
//                String data_file_path = data_file_directory+"random_RDD="+rdd+"_TF="+tf+"";
//            }
//		}
//
//		// create a file that is really a directory
//
//
//		// get a listing of all files in the directory
//
//		String result = "fileName;resultMyAlgo;runtimeMyAlgo;\n";
//		for(String file: filesInDir){
//			ProblemInstance instance = readInstance(data_file_directory+file);
//
////			long startGreedy   = System.currentTimeMillis();
////			Greedy greedy = new Greedy(instance);
////			Schedule greedySchedule = greedy.getSchedule();
////			long runtimeGreedy     = System.currentTimeMillis()-startGreedy;
////			result += String.format("%s;%s;%s;\n", file, greedySchedule.getTardiness(), runtimeGreedy);
//			long startBestFirst   = System.currentTimeMillis();
////            int bestFirstTardiness = -1; //-1 implies error
//			try {
//				BestFirst bestFirst = new BestFirst(instance);
//				Schedule bestFirstSchedule = bestFirst.getSchedule();
//
//                long runtimeBestFirst = System.currentTimeMillis()-startBestFirst;
//                result += String.format("%s;%s;%s;\n", file, bestFirstSchedule.getTardiness(), runtimeBestFirst);
//			}
//			catch(OutOfMemoryError e){
//                result += String.format("%s;%s;%s;\n", file, -1, -1);
//			}
//		}



	}

	// reads a problem, and outputs the result of both greedy and best-first
    public static void main (String args[]) {
		int writeCsv = 0;
		if (writeCsv == 1){
			writeResultsForTestData();
		}else{
			Double epsilon = Double.valueOf(args[0]);
			ProblemInstance instance = readInstance(args[1]);

			//
//
//		long startApproximateOptimized   = System.currentTimeMillis();
//		lawlerApprox lawlerApprox = new lawlerApprox(instance);
//		OurSchedule optimizedSchedule = lawlerApprox.getSchedule(epsilon);
//		long optimizedTardinessTime = System.currentTimeMillis()-startApproximateOptimized;
//
//		long startApproximate   = System.currentTimeMillis();
//		MyAlgo2 myfunc = new MyAlgo2(instance);
//		OurSchedule myFuncSchedule = myfunc.getSchedule(epsilon);
//		long runtimeApproximate = System.currentTimeMillis()-startApproximate;
//////
			long startOptimal = System.currentTimeMillis();
			MyAlgo myOptimalFunc = new MyAlgo(instance);
			OurSchedule myOptSchedule = myOptimalFunc.getSchedule();
			long runtimeOptimal = System.currentTimeMillis()-startOptimal;
//
//
//		System.out.println(myOptSchedule.getTardiness(0)+ " " + myFuncSchedule.getTardiness(0));
//
//		System.out.print(myFuncSchedule.getTardiness(0d));
			System.out.println("\n -------------------------------- ");
//		System.out.println("1. approximate min.Tard : " + myFuncSchedule.getTardiness(0d));
//        System.out.println("1. approximate runtime : " + (runtimeApproximate));
//
			System.out.println("2. exact min.Tard : " + myOptSchedule.getTardiness(0d));
			System.out.println("2. exact runtime : " + (runtimeOptimal));

//		System.out.println("3. lawlerApprox min.Tard : " + optimizedSchedule.getTardiness(0d));
//		System.out.println("3. lawlerApprox runtime : " + optimizedTardinessTime);

//		System.out.println("\n -------------------------------- ");
//		System.out.println("1. Greedy min.Tard : " + Integer.toString(greedySchedule.getTardiness()));
//		System.out.println("1. Greedy runtime : " + end1);
//		System.out.println(" -------------------------------- ");
		}





	}
}
