import java.lang.reflect.Array;
import java.util.*;


public class InputGenerator {
	private int seed, numSets, numElements;
	private Random rand;
	private int elements[];
	private Subset sets[];
	
	public void setConditions() {
		Scanner in=new Scanner(System.in);
		System.out.println("Enter the pRNG seed: ");	
		seed = in.nextInt();
		System.out.println("Enter the number of itmes: ");
		numElements = in.nextInt();
		System.out.println("Enter the number of sets: ");
		numSets= in.nextInt();	
		
		rand = new Random(seed);
		elements = new int[numElements-1];
		sets = new Subset[numSets-1];
	}
	public void setElements() {
		for(int i=0; i<numElements;i++) {
			elements[i] = i;
		}
	}
	public void createSets() {
		for(int i=1; i<numSets; i++) {
			int setSize = Uniform(1,numElements);
			for(int j=0;j<setSize;j++) {
				sets[i].set.add(Uniform(1,numElements));
			}
		}
	}
	
	public int Uniform(int a, int b) {
		return (a+(b-a)*rand.nextInt());	
	}
}
