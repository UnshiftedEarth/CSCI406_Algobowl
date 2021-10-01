import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class AlgoOutputGenerator {

	String inputFileName;
	int n;
	int m;
	ArrayList<Subset> subsets; // temp storage as file is read
	ArrayList<Subset> cover; // solution set of of subsets
	HashSet<Integer> universal; // remaining ints to be covered (starts with 1 to n)
	Map<Integer, ArrayList<Subset>> mappedSubsets = new TreeMap<Integer, ArrayList<Subset>>(); //should this be a hashset also?

	public AlgoOutputGenerator(String file){
		inputFileName = file;
		subsets = new ArrayList<Subset>();
		cover = new ArrayList<Subset>();
		
		try {
			readFile();
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
		// populate universal set
		universal = new HashSet<>();
		for (int i = 1; i <= n; i++) {// O(n)
			universal.add(i);
		}
		mapSubsets();

	}

	public void generateCover() {
		while (!coverIsComplete()) {
			ArrayList<Subset> nextList = getListOfFewestSubsets();
			Subset s = chooseSubsetLeastWeight(nextList);
			cover.add(s);
			// remove ints from universal set
			for (int i : s.set) { // O(n) complexity
				if (universal.contains(i))
					universal.remove(i);
			}
		}
		//TODO: create output file
		System.out.println("Cover is complete!"); //temp confirmation
	}
	private void readFile() throws FileNotFoundException{
		FileReader inputFileReader = new FileReader(inputFileName);
		Scanner inputScanner = new Scanner(inputFileReader);
		n = Integer.parseInt(inputScanner.nextLine());
		m = Integer.parseInt(inputScanner.nextLine());
	
		//read subsets
		for (int i = 1; i <= m; i++) {
			String nextSubsetString = inputScanner.nextLine();
			Scanner subsetScanner = new Scanner(nextSubsetString);
		
			ArrayList<Integer> nextSubset = new ArrayList<Integer>();
			while (subsetScanner.hasNextInt()) {
				nextSubset.add(subsetScanner.nextInt());
			}
			int w = Integer.parseInt(inputScanner.nextLine());
			subsets.add(new Subset(w, nextSubset));

			subsetScanner.close();
		}

		inputScanner.close();
	}
	
	// basic algorithm outline:
	// 1. map subsets based on integers contained within the subset
	// 2. find integer with fewest subsets (not including subsets already added to cover)
	// 3. choose subset with least weight (or some other criteria) that has not been added to cover yet
	// 4. add subset to cover
	// 5. check if cover is complete
	// 		6. if cover is not complete, repeat from step 2
	// 		7. if cover is complete, return cover
	
	private void mapSubsets() {
		for (Integer x = 1; x <= n; x++) {
			ArrayList<Subset> listOfSubsetsWithX = new ArrayList<Subset>();
			for (Subset s: subsets) {
				if (s.set.contains(x)) {
					listOfSubsetsWithX.add(s);
				}
			}
			mappedSubsets.put(x, listOfSubsetsWithX);
		}
	}
	
	private ArrayList<Subset> getListOfFewestSubsets() {
		//initialize with first subset in map (avoid returning null)
		int next = universal.iterator().next();
		int xWithFewestSubsets = next;
		int fewestSubsets = mappedSubsets.get(next).size(); // 1 is integer key, not index of map
		
		// find int x with fewest number of subsets that include x
		// skip ints that are already covered (loop through universal instead of all ints)
		for (int i: universal) { 
			ArrayList<Subset> list = mappedSubsets.get(i);
			
			// don't count subsets that are already included in cover
			// this could be constant time if we just check for size
			// not counting subsets that are already included will give a better solution, at the expense of efficiency
			int listSize = 0;
			for (Subset s: list) { 
				if (!cover.contains(s)) listSize++;
			}
			if (listSize < fewestSubsets) {
				fewestSubsets = listSize;
				xWithFewestSubsets = i;
			}
		}
		return mappedSubsets.get(xWithFewestSubsets);
	}
	
	// three choices for choosing a subset: 
	// 1. choose subset with least weight
	// 2. choose subset with largest size (to fill cover more quickly)
	// 3. choose subset with greatest size/weight ratio
	private Subset chooseSubsetLeastWeight(ArrayList<Subset> arrayList) {
		//initialize with first subset
		int chosenWeight = arrayList.get(0).weight;
		Subset chosen = arrayList.get(0);
		
		for (Subset s: arrayList) {
			if (s.weight < chosenWeight) {
				chosen = s;
				chosenWeight = s.weight;
			}
		}
		return chosen;
	}

	//TODO
	private Subset chooseSubsetGreatestSize(ArrayList<Subset> arrayList) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//TODO
	private Subset chooseSubsetGreatestSizeWeightRatio(ArrayList<Subset> arrayList) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// Since we remove the ints from universal set in generateCover()
	// we only need to check if the set is empty.
	private boolean coverIsComplete() {
		if (universal.isEmpty()) return true;
		return false;
	}
	
	public void verifyFileRead() {
		System.out.println("n: " + this.n);
		System.out.println("m: " + this.m);	
		System.out.println("Total subsets: " + subsets.size());
		
		for (int i = 1; i <= subsets.size(); i++) {
			System.out.println("Subset " + i + ": " + subsets.get(i-1).toString());
		}
	
	}
	
	public void verifyMapping() {
		for (Integer x = 1; x <= n; x++) {
			String printSubsetsWithX = x.toString() + ": ";
			ArrayList<Subset> subsetsWithX = mappedSubsets.get(x);
			for (Subset s: subsetsWithX) {
				printSubsetsWithX += s.toString();
				printSubsetsWithX += ' ';
			}
			System.out.println(printSubsetsWithX);
		}
	}


	public void outputVerification(String filename) {
		FileReader file;
		try {
			file = new FileReader(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		Scanner scanner = new Scanner(file);
		int minWeight = Integer.parseInt(scanner.nextLine());
		String[] s = scanner.nextLine().split(" ");
		
		// populate universal set
		HashSet<Integer> universal = new HashSet<>();
		for (int i = 1; i <= n; i++) // O(n)
			universal.add(i);
		// create summation variable
		int sum = 0;
		
		// loop through the subset ids
		for (String index : s) { // O(m)
			Subset set = subsets.get(Integer.parseInt(index) - 1);
			sum += set.weight;
			// If empty, skip the inner loop, continue calculating the sum
			if (universal.isEmpty())
				continue;

			// loop through each item in the subset and remove from universal set
			for (int i : set.set) { // O(n)
				if (universal.contains(i)) // constant time
					// remove the number from the universal set
					universal.remove(i); // constant time
					
			}
		}
		// Complexity: O(m*n + n)

		// check if the universal set is empty and if minimum weight is correct
		if (universal.isEmpty() && sum == minWeight) 
			System.out.println("File " + filename + " is valid :)");
		else 
			System.out.println("ERROR: FILE " + filename + " IS NOT VALID");

		scanner.close();
	}

	//TODO @Amber
	private String generateOutputFile() {
		String outputFileName = "output_" + inputFileName;
		// TODO Auto-generated method stub

		//return name of output file
		return outputFileName;
	}
	
	public static void main(String[] args) {
		String[] inputFileList = {
				"test_1.txt",
				"test_2.txt",
				"test_3.txt",
				"test_4.txt"
		};
		
		for (String file: inputFileList) {
			AlgoOutputGenerator algo = new AlgoOutputGenerator(file);
			System.out.println(file + " was read.");
			
			algo.generateCover();
			System.out.println(file + " cover was created.");
			
			String outputFileName = algo.generateOutputFile();
			System.out.println(file + " output file was created as " + outputFileName);
			
//			algo.outputVerification(outputFileName);
			
		}
	}


}
