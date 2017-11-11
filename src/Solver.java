import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Solver {
	public static int nodes;

	public static void main(String[] args) {
		String inputFilename = null, outputFilename = null;
		nodes = 1;

		// parsing user input
		switch (args.length) {
		case 0:
			Scanner in = new Scanner(System.in);
			System.out.println("Please enter the name of the input file for board initialization: ");
			inputFilename = in.next();
			break;
		case 2:
			inputFilename = args[0];
			outputFilename = args[1];
			break;
		default:
			System.out.println("Incorrect usage");
			System.out.println("\tUsage: java Solver input_filename");
			System.exit(1);
			break;
		}

		// solve game
		Board board = new Board(inputFilename);
		board = solve(board);
		if (outputFilename != null) {
			printToFile(board, outputFilename);
		} else if (board != null) {
			board.printWithUnknowns();
			System.out.println("Node generated = " + nodes);
		} else {
			System.err.println("Inconceivable game board");
			System.exit(1);
		}
	}

	/*
	 * Purpose: write output to a file
	 * 
	 * @param b | the board to be printed
	 * 
	 * @param filename | the name of the output file
	 * 
	 * @return void
	 */
	public static void printToFile(Board b, String filename) {
		FileWriter fw;
		PrintWriter pw;

		try {
			// clear file
			fw = new FileWriter(filename, false);
			pw = new PrintWriter(fw, false);

			if (b == null) {
				pw.println("Infeasible");
			} else {
				for (int i = 0; i < 81; i++) {
					pw.print(b.getValueAt(i).intValue() + " ");
					if ((i + 1) % 9 == 0) {
						pw.println();
					}
				}
			}
			
			pw.println("Nodes generated = " + nodes);

			pw.flush();
			pw.close();
			fw.close();
		} catch (IOException e) {
			System.err.println("Error in I/O when writing output to " + filename);
			System.exit(1);
		}
	}

	/*
	 * Purpose: recursively solve a given board, using the following steps: 1.
	 * Check if board is inconceivable --> if true, return null, 2. Check if
	 * board is the solution --> if true, return the board, 3. Calculate the
	 * next best cell on which to branch, 4. For each of the cells in the next
	 * best move's possibilities list: a. Try each possibility, b. Percolate
	 * updates throughout game board, c. Recursively call solve() on resulting
	 * board
	 * 
	 * @param b | the board that will be solved
	 * 
	 * @return the solved board
	 */
	public static Board solve(Board b) {
		if (b.isInconceivable()) {
			return null;
		}
		if (b.isSolution()) {
			return b;
		}

		Board copy, solution;
		Cell c = b.findBestMove();
		for (Integer i : c.possibilities) {
			nodes++;
			copy = b.clone();
			copy.update(c, i.intValue());
			solution = solve(copy);
			if (solution != null) {
				return solution;
			}
		}

		return null;
	}
}
