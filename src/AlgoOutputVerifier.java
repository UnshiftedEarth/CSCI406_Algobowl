import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class AlgoOutputVerifier {

    File inputFolder;
    File outputFolder;
	int n;
	int m;
	ArrayList<Subset> subsets;

    public AlgoOutputVerifier(String input, String output) {
        inputFolder = new File(input);
        outputFolder = new File(output);
		subsets = new ArrayList<>();
    }

	// read in the input that matches the output
    public void readInput(File file) {
		Scanner inputScanner;
		try {
			inputScanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		n = Integer.parseInt(inputScanner.nextLine());
		m = Integer.parseInt(inputScanner.nextLine());
		subsets.clear();
	
		//read subsets
		for (int i = 1; i <= m; i++) {
			String nextSubsetString = inputScanner.nextLine();
			Scanner subsetScanner = new Scanner(nextSubsetString);
		
			HashSet<Integer> nextSubset = new HashSet<Integer>();
			while (subsetScanner.hasNextInt()) {
				nextSubset.add(subsetScanner.nextInt());
			}
			int w = Integer.parseInt(inputScanner.nextLine());
			subsets.add(new Subset(w, nextSubset));

			subsetScanner.close();
		}

		inputScanner.close();

    }

    public boolean outputVerification(File file) {
		Scanner scanner;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		int minWeight = Integer.parseInt(scanner.nextLine());
		String[] s = scanner.nextLine().split(" ");
		
		// populate universal set
		HashSet<Integer> universalVerify = new HashSet<>();
		for (int i = 1; i <= n; i++) // O(n)
			universalVerify.add(i);
		// create summation variable
		int sum = 0;
		
		// loop through the subset ids
		for (String index : s) { // O(m)
			Subset set = subsets.get(Integer.parseInt(index) - 1);
			sum += set.weight;
			// If empty, skip the inner loop, continue calculating the sum
			if (universalVerify.isEmpty())
				continue;

			// loop through each item in the subset and remove from universal set
			for (int i : set.set) { // O(n)
				if (universalVerify.contains(i)) // constant time
					// remove the number from the universal set
					universalVerify.remove(i); // constant time
					
			}
		}
		scanner.close();
		// Complexity: O(m*n + n)

		// check if the universal set is empty and if minimum weight is correct
		if (universalVerify.isEmpty() && (sum == minWeight)) 
			return true;
		else 
			return false;
	}

	// Returns the matching input file to the output file. Looks by number
	public File findInputFile(File outputFile) {
		String outputNumber = outputFile.getName().replaceAll("[^0-9]", "");
		File input = new File("");
		boolean found = false;

		// search the input file directory
		for (File fileEntry : inputFolder.listFiles()) {
			String inputNumber = fileEntry.getName().replaceAll("[^0-9]", ""); // keep only numbers
			
			if (inputNumber.equals(outputNumber)) {
				input = fileEntry;
				found = true;
				break;
			}

		}

		if (!found) {
			System.out.println("Error no input found for matching output");
			return null;
		}
		return input;
	}

	// Loops through the outputs folder and verifies all outputs
    public void verfiyAllOutputFiles() {
		if (outputFolder.listFiles().length != inputFolder.listFiles().length) {
			System.out.println("\nError: input/output file directories do not have the same number of files");
			return;
		}

        System.out.println("");
		for (File fileEntry : outputFolder.listFiles()) {
			// find the associated input to the output
			File input = findInputFile(fileEntry);
			if (input == null)
				return;

			readInput(input);
			boolean output = outputVerification(fileEntry);

			if (output) 
				System.out.println("File: "+fileEntry.getName()+" passed verification\n");
			else 
				System.out.println("ERROR File: "+fileEntry.getName()+" FAILED VERIFICATION\n");

		}
	}
    
    public static void main(String[] args) {
		AlgoOutputVerifier algo = new AlgoOutputVerifier("inputs_folder", "outputs_folder");
		algo.verfiyAllOutputFiles();
	}
}
