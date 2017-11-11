import java.util.ArrayList;

public class Toolbox {

	/*
	 * Purpose: generic constructor
	 * 
	 * @return new instance of Toolbox
	 */
	public Toolbox() {
	}

	// have to have different methods for each type because of type erasure

	/*
	 * Purpose: to clone an ArrayList of type Integer
	 * 
	 * @param toClone | the ArrayList to clone
	 * 
	 * @return a new clone of the specified ArrayList
	 */
	public ArrayList<Integer> cloneInteger(ArrayList<Integer> toClone) {
		ArrayList<Integer> toReturn = new ArrayList<Integer>(toClone.size());
		for (Integer i : toClone) {
			toReturn.add(i);
		}
		return toReturn;
	}

	/*
	 * Purpose: to clone an ArrayList of type Cell
	 * 
	 * @param toClone | the ArrayList to clone
	 * 
	 * @param a new clone of the specified ArrayList
	 */
	public ArrayList<Cell> cloneCell(ArrayList<Cell> toClone) {
		ArrayList<Cell> toReturn = new ArrayList<Cell>(toClone.size());
		for (Cell c : toClone) {
			toReturn.add(new Cell(cloneInteger(c.possibilities), c.index));
		}
		return toReturn;
	}
}
