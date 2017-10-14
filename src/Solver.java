import java.util.Scanner;

public class Solver {
	public static void main(String[] args) {
		// variables
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
		board.update();
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
		if (b.isInconceivable()) {
			b.printWithUnknowns();
			return null;
		}
		if (b.isSolution()) {
			return b;
		}
		// recursively build tree
		// 1. select cell with least number of possibilities + most filled
		// row/col/subgrid
		// 2. try each number of possibilities by adding them to branch of recursive tree
		
		Cell c = b.findBestMove();
		int count = -1;
		for (Integer i : c.possibilities) {
			count++;
			return solve(b.clone(c, c.possibilities.get(count)));
		}
		
		return null;
	}
}
