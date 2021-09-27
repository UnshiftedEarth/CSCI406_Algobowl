import java.util.ArrayList;

public class Subset {

	public int weight;
	public ArrayList<Integer> set = new ArrayList<Integer>();	
	
	public Subset(int weight, ArrayList<Integer> set) {
		this.weight = weight;
		this.set = set;
	}
	
	public String toString() {
		String setString = new String();
		for (Integer x: set) {
			setString += x;
			setString += ',';
		}
		return setString.substring(0, setString.length()-1);
	}
}
