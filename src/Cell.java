import java.util.ArrayList;

public class Cell {
	protected ArrayList<Integer> possibilities;
	protected int index;
	protected boolean fixed;

	public Cell() {
		possibilities = new ArrayList<Integer>();
		index = 0;
		fixed = false;
	}

	public Cell(ArrayList<Integer> possibilities, int index) {
		Toolbox box = new Toolbox();
		this.possibilities = box.cloneInteger(possibilities);
		this.fixed = false;
		this.index = index;
	}
	
	public Cell(ArrayList<Integer> possibilities, int row, int col) {
		Toolbox box = new Toolbox();
		this.possibilities = box.cloneInteger(possibilities);
		this.fixed = false;
		this.index = (9 * row) + col;
	}

	public Cell(boolean fixed, int value, int index) {
		this.possibilities = new ArrayList<Integer>();
		this.possibilities.add(new Integer(value));
		this.fixed = fixed;
		this.index = index;
	}
	
	public Cell(boolean fixed, int value, int row, int col) {
		this.possibilities = new ArrayList<Integer>();
		this.possibilities.add(new Integer(value));
		this.fixed = fixed;
		this.index = (9 * row) + col;
	}
}
