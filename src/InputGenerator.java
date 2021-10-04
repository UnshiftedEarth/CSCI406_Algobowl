import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;


public class InputGenerator {
	private static int seed;
	private static int numSets;
	private static int numElements;
	private static Random rand;
	private static Subset sets[];
	static BufferedWriter writer;
	private static int whichFunc;
	//
	public static int Uniform(int a, int b) {
		return (int) (a+(b-a)*rand.nextDouble());	
	}
	
	//simple function to read in the users constraints
	public static void setConditions() {
		Scanner in=new Scanner(System.in);
		System.out.println("Enter the pRNG seed: ");	
		seed = in.nextInt();
		System.out.println("Enter the number of items: ");
		numElements = in.nextInt();
		System.out.println("Enter the number of sets: ");
		numSets= in.nextInt();	
		System.out.println("Enter 1 if you want a random set or 2 if you want a set with a guaranteed solution: ");
		whichFunc= in.nextInt();	
		rand = new Random(seed);
		sets = new Subset[numSets+1];
	}
	
	//this function will create the determined amount of sets and fill them with a random amount of 
	//elements, results from this function are not guaranteed to have a solution
	public static void createSetsRandom() {
		for(int i=0; i<numSets; i++) {
			ArrayList<Integer> tempSet = new ArrayList<Integer>();	
			int setSize = Uniform(1,numElements);
			for(int j=0;j<setSize;j++) {
				int num = Uniform(1,numElements+1);
				//makes sure the random value is not already in the set, if it is it will reroll
				if(!tempSet.contains(num))
					tempSet.add(num);
				else
					j -= 1;
			}
			sets[i] = new Subset(Uniform(1,1000), tempSet);
		}
	}
	//this function is less random then createSetsRandom but it is guaranteed to have a solution
	//this functions adds every element once before adding them randomly ensuring a solution
	public static void createSets() {
		//the counter variable will be used to keep track of the elements that have not been used yet
		int counter = 1;
		//goes through every set and assigns a random amount of elements to it
		for(int i=0; i<numSets; i++) {
			ArrayList<Integer> tempSet = new ArrayList<Integer>();	
			int setSize = Uniform(1,numElements);
			for(int j=0;j<setSize;j++) {
				//this if statement will place every element once in a subset, 
				//after which it will be randomized
				if(counter <= numElements) {
					tempSet.add(counter);
					counter++;
				}
				else {
					int num = Uniform(1,numElements+1);
					//makes sure the random value is not already in the set, if it is it will reroll
					if(!tempSet.contains(num))
						tempSet.add(num);
					else
						j -= 1;
				}
			}
			sets[i] = new Subset(Uniform(1,1000), tempSet);
		}
		//This if conditions guarantees every element is in at least one set
		if (counter<=numElements) {
			ArrayList<Integer> tempSet = new ArrayList<Integer>();	
			for(int i=counter; i <=numElements;i++) {
				tempSet.add(i);
			}
			sets[numSets] = new Subset(Uniform(1,100), tempSet);
			numSets += 1;
		}
	}
	
	
	public static void printSets() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("inputFile.txt", true));
		writer.append(String.valueOf(numElements));
		writer.newLine();
		writer.append(String.valueOf(numSets));
		for (int i=0; i<numSets; i++) {
			//System.out.println(sets[i].toString());
			writer.newLine();
			writer.append(sets[i].toString());
			writer.newLine();
			writer.write(String.valueOf(sets[i].weight));
		}
		writer.close();
	}
	public static void main(String[] args) {		
		setConditions();
		if(whichFunc == 1)
			createSetsRandom();
		else
			createSets();
		try {
			printSets();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
