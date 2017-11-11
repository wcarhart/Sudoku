# Sudoku

### Purpose: 
Solve a standard game of Sudoku

### Background: 
Sudoku is a number game on a 9x9 board. The objective is to number each cell such that each row, column, and 3x3 subgrid only has the numbers [1, 9] once. Here's an example:

#### Prompt:

	* * * | 5 * 6 | * * *  
	* * 4 | * * * | 8 * *  
	* 9 * | 1 * 2 | * * * 
	---------------------
	9 * 8 | * * * | 3 * 2 
	* * * | * 9 * | * * * 
	1 * 2 | * * * | 4 * 7 
	---------------------
	* 2 * | 3 * 4 | * 8 *
	* * 7 | * * * | 9 * *
	* * * | 9 * 5 | * * *

#### Solution:

	2 8 1 | 5 4 6 | 7 3 9 
	6 5 4 | 7 3 9 | 8 2 1 
	7 9 3 | 1 8 2 | 5 6 4
	--------------------- 
	9 7 8 | 4 6 1 | 3 5 2 
	4 3 5 | 2 9 7 | 6 1 8 
	1 6 2 | 8 5 3 | 4 9 7
	--------------------- 
	5 2 9 | 3 7 4 | 1 8 6 
	3 1 7 | 6 2 8 | 9 4 5 
	8 4 6 | 9 1 5 | 2 7 3 



### Input + Output: 
Input is passed in as a .txt file, in the following format (for the example above):

	1 4 5
	1 6 6
	2 3 4
	...
	row column value
	...

Output can either be displayed on the command line or in a file, depending on the specified settings.

### Usage:
To manually type the name of the input file and have output written to the command line, do not include any command line arguments:
	
	java Solver.java
	
To read input from and write output to files, specify files in command line arguments:

	java Solver.java inputFilename outputFilename
	
"Nodes generated" in the output refers to the number of recursive nodes visited in the tree during solving. In general, the harder the Sudoku problem, the more nodes that will be created.

	


