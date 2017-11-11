import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Board {
	protected ArrayList<Cell> grid;

	/*
	 * Purpose: return value at a specific cell, because grid is protected
	 * 
	 * @param index | the index at which to grab the value
	 * 
	 * @return a new Integer representing the requested value
	 */
	public Integer getValueAt(int index) {
		return new Integer(this.grid.get(index).possibilities.get(0));
	}

	/*
	 * Purpose: construct a new board based on an input file
	 * 
	 * @param filename | the name of the input file
	 * 
	 * @return a new board constructed from the input file
	 */
	public Board(String filename) {

		// start whole board as 81 cells with full possibilities lists
		this.grid = new ArrayList<Cell>(81);
		ArrayList<Integer> possibilities = new ArrayList<>();
		for (int j = 0; j < 9; j++) {
			possibilities.add(new Integer(j + 1));
		}
		for (int i = 0; i < 81; i++) {
			this.grid.add(new Cell(possibilities, i));
		}

		// read in initial values from file, put in board and update one at
		// a time
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));
			String line;
			int row, col, val;
			ArrayList<Cell> temp = new ArrayList<Cell>();
			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line);
				if (st.hasMoreTokens()) {
					row = Integer.parseInt(st.nextToken());
					col = Integer.parseInt(st.nextToken());
					val = Integer.parseInt(st.nextToken());
					temp.add(new Cell(true, val, row - 1, col - 1));
				}
			}
			for (Cell c : temp) {
				this.update(c, c.possibilities.get(0));
			}
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

	/*
	 * Purpose: construct a new board from an existing grid of cells
	 * 
	 * @param grid | the original grid that will be cloned when creating the new
	 * board
	 * 
	 * @return a new board constructed from the existing grid of cells
	 */
	public Board(ArrayList<Cell> grid) {
		Toolbox box = new Toolbox();
		this.grid = box.cloneCell(grid);
	}

	/*
	 * Purpose: clone an existing board
	 * 
	 * @return a clone of an existing board
	 */
	public Board clone() {
		return new Board(this.grid);
	}

	/*
	 * Purpose: to determine if a given board is inconceivable, i.e. the game
	 * cannot continue. We know a given board is inconceivable if any of the
	 * possibilities lists are reduced to a size of zero.
	 * 
	 * @return true if the board is inconceivable, false if the board is
	 * conceivable
	 */
	public boolean isInconceivable() {
		boolean toReturn = false;
		for (int i = 0; i < 81; i++) {
			if (this.grid.get(i).possibilities.size() == 0) {
				toReturn = true;
				break;
			}
		}
		return toReturn;
	}

	/*
	 * Purpose: to determine if a given board is the solution, i.e. the game has
	 * been won. We know a given board is the solution if all the possibilities
	 * lists have a size of one.
	 * 
	 * @return true if the board is the solution, false if the board is not the
	 * solution
	 */
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

	/*
	 * Purpose: to find the next best cell on which to branch recursively.
	 * Strategy is two-fold: (1) compile a list of all cells who have the
	 * smallest possibilities list, and then, (2) implement a heuristic to break
	 * ties. Current heuristic: random selection (could be improved).
	 * 
	 * @return the cell for the computed next best move
	 */
	public Cell findBestMove() {
		ArrayList<Cell> potential = new ArrayList<Cell>();
		int min = 9, newMin;

		// two step selection process
		// step 1: select smallest of possibilities lists
		for (Cell c : this.grid) {
			if ((newMin = c.possibilities.size()) < min && newMin > 1) {
				min = newMin;
			}
		}
		for (Cell c : this.grid) {
			if (c.possibilities.size() == min) {
				potential.add(c);
			}
		}

		// step 2: select a potential cell randomly
		int selection = (int) (potential.size() * Math.random());
		return potential.get(selection);
	}

	/*
	 * Purpose: to change a specific value in the game board and then percolate
	 * updates through. Process is as follows: 1. Create a queue of cells that
	 * need to be processed, 2. While the queue is not empty: a. Dequeue cell at
	 * from of queue, b. Remove that cell's value from all other cells in its
	 * row, column, and subgrid, c. If, while removing, another cell's
	 * possibilities list drops to size one, add that cell to the queue.
	 * 
	 * @param cell | the cell whose value will be changed before updates are
	 * attempted
	 * 
	 * @param value | the value to which to change cell's value
	 * 
	 * @return void
	 */
	public void update(Cell cell, int value) {
		Toolbox box = new Toolbox();

		// update new value at cell's index
		Cell newCell = new Cell(true, value, cell.index);
		this.grid.set(cell.index, newCell);

		// use queue of cells to be processed
		ArrayList<Cell> toBeProcessed = new ArrayList<Cell>();
		toBeProcessed.add(newCell);
		ArrayList<Cell> changed = new ArrayList<Cell>();
		while (!toBeProcessed.isEmpty()) {
			cell = toBeProcessed.remove(0);

			// check if board has become inconceivable
			if (cell.possibilities.size() == 0) {
				break;
			}

			// remove from column, check if need to continue updates
			changed = box.cloneCell(removeFromCol(cell, cell.possibilities.get(0)));
			if (changed.size() != 0) {
				for (Cell c : changed) {
					toBeProcessed.add(c);
				}
			}
			// remove from row, check if need to continue updates
			changed = box.cloneCell(removeFromRow(cell, cell.possibilities.get(0)));
			if (changed.size() != 0) {
				for (Cell c : changed) {
					toBeProcessed.add(c);
				}
			}
			// remove from subgrid, check if need to continue updates
			changed = box.cloneCell(removeFromSubgrid(cell, cell.possibilities.get(0)));
			if (changed.size() != 0) {
				for (Cell c : changed) {
					toBeProcessed.add(c);
				}
			}
		}
	}

	/*
	 * Purpose: to remove a specific value from a specific column
	 * 
	 * @param c | the cell whose column will be checked and edited
	 * 
	 * @param value | the value that will be removed from c's column
	 * 
	 * @return a list of cells whose possibilities lists were reduced to size
	 * one during the removal process
	 */
	public ArrayList<Cell> removeFromCol(Cell c, int value) {
		ArrayList<Cell> toReturn = new ArrayList<Cell>();
		Cell temp;
		for (int i = (c.index % 9); i < 81; i += 9) {
			if (i != c.index) {
				if ((temp = this.grid.get(i)).possibilities.contains(value)) {
					temp.possibilities.remove(Integer.valueOf(value));
					if (temp.possibilities.size() == 1) {
						toReturn.add(temp);
					}
				}
			}
		}
		return toReturn;
	}

	/*
	 * Purpose: to remove a specific value from a specific row
	 * 
	 * @param c | the cell whose row will be checked and edited
	 * 
	 * @param value | the value that will be removed from c's row
	 * 
	 * @return a list of cells whose possibilities lists were reduced to size
	 * one during the removal process
	 */
	public ArrayList<Cell> removeFromRow(Cell c, int value) {
		ArrayList<Cell> toReturn = new ArrayList<Cell>();
		Cell temp;
		for (int i = ((int) (c.index / 9)) * 9; i < (((int) (c.index / 9)) * 9) + 9; i++) {
			if (i != c.index) {
				if ((temp = this.grid.get(i)).possibilities.contains(value)) {
					temp.possibilities.remove(Integer.valueOf(value));
					if (temp.possibilities.size() == 1) {
						toReturn.add(temp);
					}
				}
			}
		}

		return toReturn;
	}

	/*
	 * Purpose: to remove a specific value from a specific subgrid
	 * 
	 * @param c | the cell whose subgrid will be checked and edited
	 * 
	 * @param value | the value that will be removed from c's subgrid
	 * 
	 * @return a list of cells whose possibilities lists were reduced to size
	 * one during the removal process
	 */
	public ArrayList<Cell> removeFromSubgrid(Cell c, int value) {
		ArrayList<Cell> toReturn = new ArrayList<Cell>();
		Cell temp;

		int index = findSubgrid(c.index);
		for (int i = index; i < index + 3; i++) {
			for (int j = 0; j < 3; j++) {
				int counter = i + (j * 9);
				if (counter != c.index) {
					if ((temp = this.grid.get(counter)).possibilities.contains(value)) {
						temp.possibilities.remove(Integer.valueOf(value));
						if (temp.possibilities.size() == 1) {
							toReturn.add(temp);
						}
					}
				}
			}
		}

		return toReturn;
	}

	/*
	 * Purpose: to find specific subgrid of a given cell
	 * 
	 * @param index | the index of the cell whose subgrid will be returned
	 * 
	 * @return the index of the top-left cell in the subgrid in which the the
	 * cell with index "index" was found
	 * 
	 * For example: index = 2 --> return 0 index = 10 --> return 0 index = 8 -->
	 * return 6
	 */
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

	/*
	 * Purpose: print board with first item in each cell's possibilities list
	 * 
	 * @return void
	 */
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

	/*
	 * Purpose: print board with * for cells with possibilities list of size
	 * greater than one
	 * 
	 * @return void
	 */
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

	/*
	 * Purpose: print board, showing entire possibilities list for each cell
	 * 
	 * @return void
	 */
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

			if (count % 3 == 0 && count < 7) {
				System.out.print("\n---------------------\n");
			} else {
				System.out.print("\n");
			}
		}
	}
}
