import java.util.Scanner;

public class Solver {
	public static void main(String[] args) {
		String filename = null;

		// parsing user input
		switch (args.length) {
		case 0:
			Scanner in = new Scanner(System.in);
			System.out.println("Please enter the name of the input file for board initialization: ");
			filename = in.next();
			break;
		case 1:
			filename = args[0];
			break;
		default:
			System.out.println("Incorrect usage");
			System.out.println("\tUsage: java Solver input_filename");
			System.exit(1);
			break;
		}

		// create new game and solve
		Board board = new Board(filename);
		//board.update(false, null, 0);
		//board.printVerbose();
		board = solve(board);
		if (board != null) {
			board.printWithUnknowns();
			// board.printWithFirst();
			// board.printVerbose();
		} else {
			System.err.println("Inconceivable game board");
			System.exit(1);
		}
	}

	public static Board solve(Board b) {
		// base cases
		// TODO currently, only can be one or the other (will never get to
		// recursive call) -- need to revise isSolution() and isInconceivable()
		// to consider non-fixed values
		if (b.isInconceivable()) {
			b.printWithUnknowns();
			return null;
		}
		// TODO debug
		if (b.isSolution()) {
			return b;
		}
		// recursively build tree
		// 1. select cell with least number of possibilities + most filled
		// row/col/subgrid
		// 2. try each number of possibilities by adding them to branch of
		// recursive tree

		Board copy, solution;
		Cell c = b.findBestMove();
		for (Integer i : c.possibilities) {
			copy = b.clone();
			//copy.update(true, c, c.possibilities.indexOf(i));
			copy.update(c, i.intValue());
			solution = solve(copy);
			if (solution != null) {
				return solution;
			}
		}

		return null;
	}
}
