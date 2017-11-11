import java.lang.reflect.Array;
import java.util.ArrayList;
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
		board = solve(board, 0);
		if (board != null) {
			board.printWithUnknowns();
			// board.printWithFirst();
			// board.printVerbose();
		} else {
			System.err.println("Inconceivable game board");
			System.exit(1);
		}
	}

	public static Board solve(Board b, int depth) {
		// base cases
		// TODO currently, only can be one or the other (will never get to
		// recursive call) -- need to revise isSolution() and isInconceivable()
		// to consider non-fixed values
		if (b.isInconceivable()) {
			//b.printWithUnknowns();
			//System.out.println("Nodes generated: " + depth);
			return null;
		}
		// TODO debug
		if (b.isSolution()) {
			//b.printWithUnknowns();
			//System.out.println("Nodes generated: " + depth);
			return b;
		}
		// recursively build tree
		// 1. select cell with least number of possibilities + most filled
		// row/col/subgrid
		// 2. try each number of possibilities by adding them to branch of
		// recursive tree

		Board copy, solution;
		Cell c = b.findBestMove();
		//ArrayList<Integer> p = new Toolbox().cloneInteger(c.possibilities);
		for (Integer i : c.possibilities) {
			System.out.println("Branching on cell #" + c.index);
			System.out.println("Cell #" + c.index + " has possibilities " + c.possibilities);
			System.out.println("I'm branching on " + i);
			System.out.println("I'm at depth " + depth);
			System.out.println();
			copy = b.clone();
			copy.update(c, i.intValue());
			solution = solve(copy, depth + 1);
			if (solution != null) {
				return solution;
			}
		}

		return null;
	}
}
