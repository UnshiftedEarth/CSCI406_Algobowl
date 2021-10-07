import java.util.ArrayList;
import java.util.HashSet;

public class Subset {

	public int weight;
	public HashSet<Integer> set;	
	
	public Subset(int weight, HashSet<Integer> set) {
		this.weight = weight;
		this.set = set;
	}
	
	public String toString() {
		String setString = new String();
		for (Integer x: set) {
			setString += x;
			setString += ' ';
		}
		return setString.substring(0, setString.length()-1);
	}
}
