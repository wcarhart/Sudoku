import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class Board {
	protected ArrayList<Cell> grid;

	// debugged
	public Board(String filename) {
		this.grid = new ArrayList<Cell>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));
			String line;
			int row, col, val;
			ArrayList<Cell> temp = new ArrayList<Cell>();
			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line);
				row = Integer.parseInt(st.nextToken());
				col = Integer.parseInt(st.nextToken());
				val = Integer.parseInt(st.nextToken());
				temp.add(new Cell(true, val, row - 1, col - 1));
			}
			balance(temp);
		} catch (IOException e) {
			System.err.println("I/O error when reading from " + filename);
			System.exit(1);
		}
		try {
			br.close();
		} catch (IOException e) {
			System.err.println("Error when closing file " + filename);
			System.exit(1);
		}
	}

	public Board(ArrayList<Cell> grid) {
		Toolbox box = new Toolbox();
		this.grid = box.cloneCell(grid);
	}

	// debugged
	public void balance(ArrayList<Cell> temp) {
		boolean val;
		for (int i = 0; i < 81; i++) {
			val = false;
			for (Cell c : temp) {
				if (c.index == i) {
					this.grid.add(c);
					val = true;
					break;
				}
			}
			if (!val) {
				ArrayList<Integer> possibilities = new ArrayList<Integer>();
				for (int j = 0; j < 9; j++) {
					possibilities.add(new Integer(j + 1));
				}
				this.grid.add(new Cell(possibilities, i));
			}
		}
	}

	// TODO: debug this method
	public Board clone() {
		return new Board(this.grid);
	}

	// TODO: debug this methodS
	public boolean isInconceivable() {
		boolean toReturn = false;
		for (int i = 0; i < 81; i++) {
			if (this.grid.get(i).possibilities.size() == 0) {
				toReturn = true;
			}
		}
		return toReturn;
	}

	// debugged
	// TODO: currently only works if each cell's possibility list is of size one
	public boolean validRows() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		Cell c;
		for (int i = 0; i < 81; i += 9) { // for each row
			for (int j = 0; j < 9; j++) { // for each index in row
				if ((c = this.grid.get(i + j)).fixed || c.possibilities.size() == 1) {
					list.add(c.possibilities.get(0));
				}
			}
			Set<Integer> set = new HashSet<Integer>(list);
			if (set.size() < list.size()) {
				return false;
			}
			list = new ArrayList<Integer>();
		}
		return true;
	}

	// debugged
	// TODO: currently only works if each cell's possibility list is of size one
	public boolean validCols() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		Cell c;
		for (int i = 0; i < 9; i++) {
			for (int j = i; j < 81; j += 9) {
				if ((c = this.grid.get(j)).fixed || c.possibilities.size() == 1) {
					list.add(c.possibilities.get(0));
				}
			}
			Set<Integer> set = new HashSet<Integer>(list);
			if (set.size() < list.size()) {
				return false;
			}
			list = new ArrayList<Integer>();
		}
		return true;
	}

	// debugged
	// TODO: currently only works if each cell's possibility list is of size one
	public boolean validSubgrids() {
		ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < 9; i++) {
			list.add(new ArrayList<Integer>());
		}
		Cell c;

		for (int i = 0; i < 81; i++) {
			c = this.grid.get(i);
			switch (i % 9) {
			case 0:
				if (c.fixed || c.possibilities.size() == 1) {
					list.get(0).add(c.possibilities.get(0));
				}
				break;
			case 1:
				if (c.fixed || c.possibilities.size() == 1) {
					list.get(1).add(c.possibilities.get(0));
				}
				break;
			case 2:
				if (c.fixed || c.possibilities.size() == 1) {
					list.get(2).add(c.possibilities.get(0));
				}
				break;
			case 3:
				if (c.fixed || c.possibilities.size() == 1) {
					list.get(3).add(c.possibilities.get(0));
				}
				break;
			case 4:
				if (c.fixed || c.possibilities.size() == 1) {
					list.get(4).add(c.possibilities.get(0));
				}
				break;
			case 5:
				if (c.fixed || c.possibilities.size() == 1) {
					list.get(5).add(c.possibilities.get(0));
				}
				break;
			case 6:
				if (c.fixed || c.possibilities.size() == 1) {
					list.get(6).add(c.possibilities.get(0));
				}
				break;
			case 7:
				if (c.fixed || c.possibilities.size() == 1) {
					list.get(7).add(c.possibilities.get(0));
				}
				break;
			case 8:
				if (c.fixed || c.possibilities.size() == 1) {
					list.get(8).add(c.possibilities.get(0));
				}
				break;
			default:
				System.err.println("ERROR: could not compute subgrid validity");
				System.exit(1);
			}
		}
		for (int i = 0; i < 9; i++) {
			Set<Integer> set = new HashSet<Integer>(list.get(i));
			if (set.size() < list.get(i).size()) {
				return false;
			}
		}
		return true;
	}

	// TODO: debug this method
	public boolean isSolution() {
		boolean toReturn = true;
		for (int i = 0; i < 81; i++) {
			if (this.grid.get(i).possibilities.size() != 1) {
				toReturn = false;
				break;
			}
		}
		return toReturn;
	}

	// TODO: finish tie-breaker heuristic
	public Cell findBestMove() {
		ArrayList<Cell> potential = new ArrayList<Cell>();
		int min = 9, newMin;

		// two step selection process
		// step 1: smallest of possibilities lists
		for (Cell c : this.grid) {
			if ((newMin = c.possibilities.size()) < min) {
				min = newMin;
			}
		}
		for (Cell c : this.grid) {
			if (c.possibilities.size() == min) {
				potential.add(c);
			}
		}

		// step 2: (tie-breaker) most known in subgrid/row/col
		if (potential.size() > 1) {
			// check subgrid

		}

		return potential.get(0);
	}

	// debugged
	public void update(boolean update, Cell cell, int index) {
		if (update) {
			this.grid.get(cell.index).possibilities.clear();
			this.grid.get(cell.index).possibilities.add(new Integer(index));
		}
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (Cell c : this.grid) {
			boolean continueCondition = true;
			while (continueCondition) {
				continueCondition = false;
				if (c.fixed) {
					temp.add(c.possibilities.get(0));
				}
				if (removeFromCol(c, temp)) {
					continueCondition = true;
				}
				if (removeFromRow(c, temp)) {
					continueCondition = true;
				}
				if (removeFromSubgrid(c, temp)) {
					continueCondition = true;
				}
				temp = new ArrayList<Integer>();
			}
		}
	}

	// debugged
	public boolean removeFromCol(Cell c, ArrayList<Integer> list) {
		boolean toReturn = false;
		for (int i = (c.index % 9); i < 81; i += 9) {
			if (i != c.index) {
				for (Integer k : list) {
					if (this.grid.get(i).possibilities.contains(k) && !this.grid.get(i).fixed) {
						this.grid.get(i).possibilities.remove(k);
						toReturn = true;
					}
				}
			}
		}
		return toReturn;
	}

	// debugged
	public boolean removeFromRow(Cell c, ArrayList<Integer> list) {
		boolean toReturn = false;
		for (int i = ((int) (c.index / 9)) * 9; i < (((int) (c.index / 9)) * 9) + 9; i++) {
			if (i != c.index) {
				for (Integer k : list) {
					if (this.grid.get(i).possibilities.contains(k) && !this.grid.get(i).fixed) {
						this.grid.get(i).possibilities.remove(k);
						toReturn = true;
					}
				}
			}
		}
		return toReturn;
	}

	// debugged
	public boolean removeFromSubgrid(Cell c, ArrayList<Integer> list) {
		boolean toReturn = false;
		int index = findSubgrid(c.index);
		for (int i = index; i < index + 3; i++) {
			for (int j = 0; j < 3; j++) {
				int counter = i + (j * 9);
				if (counter != c.index) {
					for (Integer k : list) {
						if (this.grid.get(counter).possibilities.contains(k) && !this.grid.get(counter).fixed) {
							this.grid.get(counter).possibilities.remove(k);
							toReturn = true;
						}
					}
				}
			}
		}
		return toReturn;
	}

	// debugged
	public int findSubgrid(int index) {
		int col = index % 9;
		int row = (int) index / 9;
		int toReturn = -1;
		switch (row) {
		case 0:
		case 1:
		case 2:
			if (col >= 0 && col < 3) {
				toReturn = 0;
			} else if (col >= 3 && col < 6) {
				toReturn = 3;
			} else if (col >= 6) {
				toReturn = 6;
			}
			break;
		case 3:
		case 4:
		case 5:
			if (col >= 0 && col < 3) {
				toReturn = 27;
			} else if (col >= 3 && col < 6) {
				toReturn = 30;
			} else if (col >= 6) {
				toReturn = 33;
			}
			break;
		case 6:
		case 7:
		case 8:
			if (col >= 0 && col < 3) {
				toReturn = 54;
			} else if (col >= 3 && col < 6) {
				toReturn = 57;
			} else if (col >= 6) {
				toReturn = 60;
			}
			break;
		default:
			System.err.println("ERROR: out of game bounds index when finding subgrid");
			System.exit(1);
			break;
		}
		return toReturn;
	}

	// debugged
	public void printWithFirst() {
		int count = 0;
		for (int i = 0; i < 81; i += 9) {
			count++;
			System.out.print(this.grid.get(i).possibilities.get(0) + " ");
			System.out.print(this.grid.get(i + 1).possibilities.get(0) + " ");
			System.out.print(this.grid.get(i + 2).possibilities.get(0) + " | ");
			System.out.print(this.grid.get(i + 3).possibilities.get(0) + " ");
			System.out.print(this.grid.get(i + 4).possibilities.get(0) + " ");
			System.out.print(this.grid.get(i + 5).possibilities.get(0) + " | ");
			System.out.print(this.grid.get(i + 6).possibilities.get(0) + " ");
			System.out.print(this.grid.get(i + 7).possibilities.get(0) + " ");
			System.out.print(this.grid.get(i + 8).possibilities.get(0));
			if (count % 3 == 0 && count < 7) {
				System.out.print("\n---------------------\n");
			} else {
				System.out.print("\n");
			}
		}
	}

	// debugged
	public void printWithUnknowns() {
		int count = 0;
		for (int i = 0; i < 81; i += 9) {
			count++;
			System.out.print((this.grid.get(i).possibilities.size() == 1)
					? (this.grid.get(i).possibilities.get(0) + " ") : "* ");
			System.out.print((this.grid.get(i + 1).possibilities.size() == 1)
					? (this.grid.get(i + 1).possibilities.get(0) + " ") : "* ");
			System.out.print((this.grid.get(i + 2).possibilities.size() == 1)
					? (this.grid.get(i + 2).possibilities.get(0) + " | ") : "* | ");
			System.out.print((this.grid.get(i + 3).possibilities.size() == 1)
					? (this.grid.get(i + 3).possibilities.get(0) + " ") : "* ");
			System.out.print((this.grid.get(i + 4).possibilities.size() == 1)
					? (this.grid.get(i + 4).possibilities.get(0) + " ") : "* ");
			System.out.print((this.grid.get(i + 5).possibilities.size() == 1)
					? (this.grid.get(i + 5).possibilities.get(0) + " | ") : "* | ");
			System.out.print((this.grid.get(i + 6).possibilities.size() == 1)
					? (this.grid.get(i + 6).possibilities.get(0) + " ") : "* ");
			System.out.print((this.grid.get(i + 7).possibilities.size() == 1)
					? (this.grid.get(i + 7).possibilities.get(0) + " ") : "* ");
			System.out.print((this.grid.get(i + 8).possibilities.size() == 1)
					? (this.grid.get(i + 8).possibilities.get(0)) : "*");
			if (count % 3 == 0 && count < 7) {
				System.out.print("\n---------------------\n");
			} else {
				System.out.print("\n");
			}
		}
	}

	// debugged
	public void printVerbose() {
		int count = 0;
		for (int i = 0; i < 81; i += 9) {
			count++;
			System.out.print(Arrays.toString(this.grid.get(i).possibilities.toArray()));
			System.out.print(" ");
			System.out.print(Arrays.toString(this.grid.get(i + 1).possibilities.toArray()));
			System.out.print(" ");
			System.out.print(Arrays.toString(this.grid.get(i + 2).possibilities.toArray()));
			System.out.print(" | ");
			System.out.print(Arrays.toString(this.grid.get(i + 3).possibilities.toArray()));
			System.out.print(" ");
			System.out.print(Arrays.toString(this.grid.get(i + 4).possibilities.toArray()));
			System.out.print(" ");
			System.out.print(Arrays.toString(this.grid.get(i + 5).possibilities.toArray()));
			System.out.print(" | ");
			System.out.print(Arrays.toString(this.grid.get(i + 6).possibilities.toArray()));
			System.out.print(" ");
			System.out.print(Arrays.toString(this.grid.get(i + 7).possibilities.toArray()));
			System.out.print(" ");
			System.out.print(Arrays.toString(this.grid.get(i + 8).possibilities.toArray()));
			System.out.print(" | ");

			if (count % 3 == 0 && count < 7) {
				System.out.print("\n---------------------\n");
			} else {
				System.out.print("\n");
			}
		}
	}
}
