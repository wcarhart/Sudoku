import java.util.ArrayList;

public class Toolbox {
	public Toolbox() { }

	public ArrayList<Integer> clone(ArrayList<Integer> toClone) {
		ArrayList<Integer> toReturn = new ArrayList<Integer>(toClone.size());
		for (Integer i : toClone) {
			toReturn.add(i);
		}
		return toReturn;
	}
}
