import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class ComputeTardiness {
	public static ProblemInstance readInstance(String filename, int verbose){
		if (verbose == 1){
			System.out.println(" ------- ");
			System.out.println(" - Filename : " + filename);
			System.out.println(" ------- \n");
		}

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

	public static void writeResultsForTestData(String data_file_directory, String output_path){
		int regex = 1;

		// Prerak
		int greedyBool = 0;
		int lawlerApproxBool = 0; Double epsilon = 0.33d;

		// Niels
		int bestFirstBool = 0;
		int lawlerBool = 1;

		int justTest = 0;

        File aDirectory = new File(data_file_directory);
        String[] filesInDir = aDirectory.list();
        Arrays.sort(filesInDir);

        // Step1 - Regex
		String fileFilterRegex = "";
		String size = null;
        if (regex == 1){
			//        String[] regexes = new String[]{"#(30|60)", "RDD=0.[36]_TF=0.[36]"};
			size = "80";
			fileFilterRegex = ".*(?:#" + size + ").*";
			// fileFilterRegex = ".*(?:#10).*";
			// fileFilterRegex = ".*(?:random_RDD=0.2_TF=0.2_#30).*";
        	// String fileFilterRegex = ".*(?:#(?:30|60)|RDD=0\\.[46]_TF=0\\.[46]).*";
		}

		if (lawlerApproxBool == 1){
			output_path += "lawlerApprox_" + size + ".csv";
		}else if(lawlerBool == 1){
			output_path += "lawler_" + size + ".csv";
			// output_path += "lawler.csv";
		}
		else if(greedyBool == 1){
			output_path += "greedy_" + size + ".csv";
		}
		else if(bestFirstBool == 1){
			output_path += "bestFirst.csv";
		}


		// Step2 - Loop over files
        String result = "fileName;result;runtime;\n";
        for(String fileName: filesInDir){
            // filter files out of scope
            if(!fileName.matches(fileFilterRegex)){
                // System.out.println(fileName);
                continue;
            }
			// System.out.println(fileName);

            ProblemInstance instance = readInstance(data_file_directory+fileName, 0);
            long startBestFirst   = System.currentTimeMillis();
            long runtimeBestFirst = -1;
            long tardinessBestFirst = -1;

			// Step 2.1 - If only for test
			if (justTest == 1){
				long startLawler           = System.currentTimeMillis();
				MyAlgo lawlerFunc          = new MyAlgo(instance);
				OurSchedule lawlerSchedule = lawlerFunc.getSchedule();
				long lawlerRuntime         = System.currentTimeMillis()-startLawler;
				Double lawlerTardiness     = lawlerSchedule.getTardiness(0d);
				System.out.println(String.format("%s\t%s\t\t%s;\n", fileName, lawlerTardiness, lawlerRuntime));
			}else if(lawlerBool == 1){
				long startLawler           = System.currentTimeMillis();
				MyAlgo lawlerFunc          = new MyAlgo(instance);
				OurSchedule lawlerSchedule = lawlerFunc.getSchedule();
				long lawlerRuntime         = System.currentTimeMillis()-startLawler;
				Double lawlerTardiness     = lawlerSchedule.getTardiness(0d);
				String output              = String.format("%s;%s;%s;\n", fileName, lawlerTardiness, lawlerRuntime);
				System.out.println(output);
				result += String.format(output);

			}else if (lawlerApproxBool == 1){
				long startLawlerApprox           = System.currentTimeMillis();
				lawlerApprox lawlerApproxFunc    = new lawlerApprox(instance);
				OurSchedule lawlerApproxSchedule = lawlerApproxFunc.getSchedule(epsilon);
				long lawlerApproxRuntime         = System.currentTimeMillis() - startLawlerApprox;
				Double lawlerApproxTardiness     = lawlerApproxSchedule.getTardiness(0d);
				String output              = String.format("%s;%s;%s;\n", fileName, lawlerApproxTardiness, lawlerApproxRuntime);
				System.out.println(output);
				result += String.format(output);
			}else if (bestFirstBool == 1){
				try {
					BestFirst bestFirst        = new BestFirst(instance);
					Schedule bestFirstSchedule = bestFirst.getSchedule();
					tardinessBestFirst         = bestFirstSchedule.getTardiness();
					runtimeBestFirst           = System.currentTimeMillis()-startBestFirst;
					String output              = String.format("%s;%s;%s;\n", fileName, tardinessBestFirst, runtimeBestFirst);
					System.out.println(output);
					result += String.format(output);
				}
				catch(OutOfMemoryError e){
					//error values are preset at initialization
				}
			}else if (greedyBool == 1){
				long startGreedy           = System.currentTimeMillis();
				Greedy greedyFunc          = new Greedy(instance);
				Schedule greedySchedule    = greedyFunc.getSchedule();
				long greddyRuntime         = System.currentTimeMillis() - startGreedy;
				int greedyTardiness        = greedySchedule.getTardiness();
				String output              = String.format("%s;%s;%s;\n", fileName, greedyTardiness, greddyRuntime);
				System.out.println(output);
				result += String.format(output);
			}

			// Step 2.2 -

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
		int writeCsv = 1;
		if (writeCsv == 1){
			String data_file_directory = "/home/strider/Work/Netherlands/TUDelft/1_Courses/Sem1/AAlgo/Assignments/Part1/AAlgo_PA1/test/instances/";
			String output_path         = "/home/strider/Work/Netherlands/TUDelft/1_Courses/Sem1/AAlgo/Assignments/Part1/AAlgo_PA1/test/results/results/";
			writeResultsForTestData(data_file_directory, output_path);
		}else {
			Double epsilon = Double.valueOf(args[0]);
			ProblemInstance instance = readInstance(args[1], 1);

			Boolean lawlerBool = true;
			if (lawlerBool){
				long startLawler           = System.currentTimeMillis();
				MyAlgo lawlerFunc          = new MyAlgo(instance);
				OurSchedule lawlerSchedule = lawlerFunc.getSchedule();
				long lawlerRuntime         = System.currentTimeMillis()-startLawler;
				Double lawlerTardiness     = lawlerSchedule.getTardiness(0d);
				System.out.println(String.format("%s\t%s\t\t%s;\n", "", lawlerTardiness, lawlerRuntime));
			}
		}
	}
}
