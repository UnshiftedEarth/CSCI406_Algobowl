import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;


public class AlgoOutputGenerator {

	String fileName;
	int n;
	int m;
	ArrayList<Subset> subsets;
	ArrayList<Subset> cover;
	Map<Integer, ArrayList<Subset>> mappedSubsets = new TreeMap<Integer, ArrayList<Subset>>();

	public AlgoOutputGenerator(String inputFileName){
		fileName = inputFileName;
		subsets = new ArrayList<Subset>();
		cover = new ArrayList<Subset>();
		
		try {
			readFile();
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
		mapSubsets();
	}

	private void readFile() throws FileNotFoundException{
		FileReader inputFileReader = new FileReader(fileName);
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
		}
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
	
	private Subset getNextSubset() {
		//TODO
		return null;
	}
	
	private boolean checkIfCoverIsComplete() {
		//TODO
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

	public static void main(String[] args) {
		String file = "test_2.txt";
		AlgoOutputGenerator algo = new AlgoOutputGenerator(file);
//		algo.verifyFileRead();
		algo.verifyMapping();

	}

}
