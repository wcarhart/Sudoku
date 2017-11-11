import java.util.ArrayList;

public class Toolbox {
	public Toolbox() {}
	
	// have to have different methods for each type b/c of type erasure
	public ArrayList<Integer> cloneInteger(ArrayList<Integer> toClone) {
		ArrayList<Integer> toReturn = new ArrayList<Integer>(toClone.size());
		for (Integer i : toClone) {
			toReturn.add(i);
		}
		return toReturn;
	}
	
	public ArrayList<Cell> cloneCell(ArrayList<Cell> toClone) {
		ArrayList<Cell> toReturn = new ArrayList<Cell>(toClone.size());
		for (Cell c : toClone) {
			toReturn.add(new Cell(cloneInteger(c.possibilities), c.index));
		}
		return toReturn;
	}
}
