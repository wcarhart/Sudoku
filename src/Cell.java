import java.util.ArrayList;

public class Cell {
	protected ArrayList<Integer> possibilities;
	protected int index;
	protected boolean fixed;

	/*
	 * Purpose: cell constructor, generic
	 * 
	 * @return a new cell with generic values
	 */
	public Cell() {
		possibilities = new ArrayList<Integer>();
		index = 0;
		fixed = false;
	}

	/*
	 * Purpose: cell constructor, with specified possibilities list at specific
	 * index
	 * 
	 * @param possibilities | specified possibilities list
	 * 
	 * @param index | index of new cell
	 * 
	 * @return a new cell with specific possibilities list at index "index"
	 */
	public Cell(ArrayList<Integer> possibilities, int index) {
		Toolbox box = new Toolbox();
		this.possibilities = box.cloneInteger(possibilities);
		this.fixed = false;
		this.index = index;
	}

	/*
	 * Purpose: cell constructor, with specified possibilities list at specific
	 * row + column
	 * 
	 * @return a new cell with specific possibilities list at index (9 * row) +
	 * column
	 */
	public Cell(ArrayList<Integer> possibilities, int row, int col) {
		Toolbox box = new Toolbox();
		this.possibilities = box.cloneInteger(possibilities);
		this.fixed = false;
		this.index = (9 * row) + col;
	}

	/*
	 * Purpose: cell constructor, with specified value at specific index
	 * 
	 * @param fixed | true if fixed, false if not fixed
	 * 
	 * @param value | specified value for possibilities list
	 * 
	 * @param index | index of new cell
	 * 
	 * @return a new cell with specific fixed value at index "index"
	 */
	public Cell(boolean fixed, int value, int index) {
		this.possibilities = new ArrayList<Integer>();
		this.possibilities.add(new Integer(value));
		this.fixed = fixed;
		this.index = index;
	}

	/*
	 * Purpose: cell constructor, with specified value at specific row + column
	 * 
	 * @param fixed | true if fixed, false if not fixed
	 * 
	 * @param value | specified value for possibilities list
	 * 
	 * @param row | row for calculating index of new cell
	 * 
	 * @param col | column for calculating index of new cell
	 * 
	 * @return a new cell with specific fixed value at index (9 * row) + column
	 */
	public Cell(boolean fixed, int value, int row, int col) {
		this.possibilities = new ArrayList<Integer>();
		this.possibilities.add(new Integer(value));
		this.fixed = fixed;
		this.index = (9 * row) + col;
	}
}
