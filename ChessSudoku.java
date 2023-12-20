package finalproject;

import java.util.*;
import java.io.*;


public class ChessSudoku
{
	/* SIZE is the size parameter of the Sudoku puzzle, and N is the square of the size.  For 
	 * a standard Sudoku puzzle, SIZE is 3 and N is 9. 
	 */
	public int SIZE, N;

	/* The grid contains all the numbers in the Sudoku puzzle.  Numbers which have
	 * not yet been revealed are stored as 0. 
	 */
	public int grid[][];

	/* Booleans indicating whether of not one or more of the chess rules should be 
	 * applied to this Sudoku. 
	 */
	public boolean knightRule;
	public boolean kingRule;
	public boolean queenRule;

	
	// Field that stores the same Sudoku puzzle solved in all possible ways
	public HashSet<ChessSudoku> solutions = new HashSet<ChessSudoku>();


	/* The solve() method should remove all the unknown characters ('x') in the grid
	 * and replace them with the numbers in the correct range that satisfy the constraints
	 * of the Sudoku puzzle. If true is provided as input, the method should finds ALL
	 * possible solutions and store them in the field named solutions. */
	public void solve(boolean allSolutions) {
 
		if (!allSolutions)						// if false
			sudokuSolver(0,0, 0);
		else	// if true
			{
			sudokuSolver(0,0,1);
		}
	}
 
	// Checks row by row, column by column, once reached the end of a row, start on new row
	private boolean sudokuSolver (int row, int col, int value)
	{
		// Check if reached the end of the grid
		if (row == N -1 && col ==N) {
 
			// If allSolutions set to true
			if (value == 1) {
				// perform a deep copy of current grid
				ChessSudoku solution = new ChessSudoku(SIZE);
				for (int i = 0; i < N; i++) {
					for (int j = 0; j < N; j++) {
						solution.grid[i][j] = grid[i][j];
					}
				}
				solutions.add(solution);
				return false;
			}
			// if allSolutions set to false
			else
				return true;
		}
 
		// Check if reached the end of the row
		if (col == N)
		{
			row ++;
			col = 0;
		}
 
		// Check if the square is an empty one
		if (grid[row][col] !=0)
 
			// If not empty go to next
			return sudokuSolver(row, col+1, value);
 
		// Start filling in some numbers
		for (int num = 1; num <= N; num++)
		{
			// Check if valid move
			if (isValid(row, col, num))
			{
				// if valid, fill it in
				grid[row][col]=num;
 
				// continue for next square
				if (sudokuSolver(row, col+1, value))
					return true;
			}
			// if not valid then reset
			grid[row][col]=0;
		}
		// Did not find a solution
		return false;
	}
 
	private boolean isValid ( int row, int col, int num){
 
			// Check row + column
			for (int i = 0; i <= N-1; i++) {
 
				// Check row
				if (grid[row][i] == num) {
					return false;
				}
 
				// Check Column
				if (grid[i][col]==num){
					return false;
				}
 
				// Check subgrid
				if (grid[SIZE * (row/SIZE) + i/SIZE][SIZE * (col/SIZE) + i % SIZE] == num)
					return false;
				}
 
			if (this.kingRule) // Need to check 4 positions if in bounds: top left, top right, bot left, bot right
			{
				// x_axis[0] represents the top left, 1 represents bot left, 2 top right, 3 bot right
				int[] x_axis = {-1,1,-1,1};
				int[] y_axis = {-1,-1,1,1};
 
				// Tests for those 4 positions
				for (int i = 0; i<4;i++){
					int m = x_axis[i] + row;
					int n = y_axis[i] + col;
 
					// Make sure that it is within the grid
					if ((0<= m) && (m<this.N) && ((0<=n)&& (n<this.N))){
 
						// If within the grid, check if same num
						if (grid[m][n] == num){
							return false;
						}
 
					}
				}
			}
 
			if (this.knightRule) // Need to check 8 positions if in bounds
			{
				// Same as king rule's above, 8 positions
				int[] x_axis = {-2, -2, -1, 1};
				int[] y_axis = {-1, 1, 2, 2};
 
				for (int i = 0; i < 4; i++) {
 
					// Check position
					int m = x_axis[i] + row;
					int n = y_axis[i] + col;
 
					// Check if within grid
					if ((0 <= m) && (m < this.N) && ((0 <= n) && (n < this.N))) {
 
						if (grid[m][n] == num) {
							return false;
						}
					}
 
					// Check other positions
					int j = x_axis[i] + col;
					int l = y_axis[i] + row;
 
					// Check if within grid
					if ((0 <= j) && (j < this.N) && ((0 <= l) && (l < this.N))) {
 
						if (grid[l][j] == num) {
							return false;
						}
					}
				}
			}
 
			if (this.queenRule)
			{
				if (num == N) {
 
					// Checks \\ diagonal
					int result = row - col;
 
					// The \\ diagonals can be separated into 3 cases
					// First case would be the main diagonal
					if (result == 0)
					{
						for (int j = 0; j<N;j++) {
							if (grid[j][j] == num)
								return false;
						}
					}
 
					// Second case would be above the main diagonal
					else if(result <= -1)
					{
						for (int j = N-1; result + j >=0; j--){
							if (grid[result+j][j]==num)
									return false;
						}
					}
 
					// Third case would be below the main diagonal
					else
					{
						for (int j = 0; result+j<N; j++)
						{
							if (grid[result+j][j] == num)
								return false;
						}
					}
 
					// Checks // diagonal
					int result2 = row + col;
					// Separated into two cases
					// First case would be above the main // diagonal + main diagonal
					if (result2<N){
						for (int j = 0; (result2-j)>=0 && (result2-j)<N; j++)
						{
							if (grid[j][result2-j] == num)
								return false;
						}
					}
					// Below the main diagonal
					else{
						for (int j=N-1; (result2-j)>=0 && (result2-j)<N;j--){
							if (grid[j][result2-j] == num)
								return false;
						}
					}
					}
				}
 
			return true;
		}
	

	/*****************************************************************************/
	/* NOTE: YOU SHOULD NOT HAVE TO MODIFY ANY OF THE METHODS BELOW THIS LINE. */
	/*****************************************************************************/

	/* Default constructor.  This will initialize all positions to the default 0
	 * value.  Use the read() function to load the Sudoku puzzle from a file or
	 * the standard input. */
	public ChessSudoku( int size ) {
		SIZE = size;
		N = size*size;

		grid = new int[N][N];
		for( int i = 0; i < N; i++ ) 
			for( int j = 0; j < N; j++ ) 
				grid[i][j] = 0;
	}


	/* readInteger is a helper function for the reading of the input file.  It reads
	 * words until it finds one that represents an integer. For convenience, it will also
	 * recognize the string "x" as equivalent to "0". */
	static int readInteger( InputStream in ) throws Exception {
		int result = 0;
		boolean success = false;

		while( !success ) {
			String word = readWord( in );

			try {
				result = Integer.parseInt( word );
				success = true;
			} catch( Exception e ) {
				// Convert 'x' words into 0's
				if( word.compareTo("x") == 0 ) {
					result = 0;
					success = true;
				}
				// Ignore all other words that are not integers
			}
		}

		return result;
	}


	/* readWord is a helper function that reads a word separated by white space. */
	static String readWord( InputStream in ) throws Exception {
		StringBuffer result = new StringBuffer();
		int currentChar = in.read();
		String whiteSpace = " \t\r\n";
		// Ignore any leading white space
		while( whiteSpace.indexOf(currentChar) > -1 ) {
			currentChar = in.read();
		}

		// Read all characters until you reach white space
		while( whiteSpace.indexOf(currentChar) == -1 ) {
			result.append( (char) currentChar );
			currentChar = in.read();
		}
		return result.toString();
	}


	/* This function reads a Sudoku puzzle from the input stream in.  The Sudoku
	 * grid is filled in one row at at time, from left to right.  All non-valid
	 * characters are ignored by this function and may be used in the Sudoku file
	 * to increase its legibility. */
	public void read( InputStream in ) throws Exception {
		for( int i = 0; i < N; i++ ) {
			for( int j = 0; j < N; j++ ) {
				grid[i][j] = readInteger( in );
			}
		}
	}


	/* Helper function for the printing of Sudoku puzzle.  This function will print
	 * out text, preceded by enough ' ' characters to make sure that the printint out
	 * takes at least width characters.  */
	void printFixedWidth( String text, int width ) {
		for( int i = 0; i < width - text.length(); i++ )
			System.out.print( " " );
		System.out.print( text );
	}


	/* The print() function outputs the Sudoku grid to the standard output, using
	 * a bit of extra formatting to make the result clearly readable. */
	public void print() {
		// Compute the number of digits necessary to print out each number in the Sudoku puzzle
		int digits = (int) Math.floor(Math.log(N) / Math.log(10)) + 1;

		// Create a dashed line to separate the boxes 
		int lineLength = (digits + 1) * N + 2 * SIZE - 3;
		StringBuffer line = new StringBuffer();
		for( int lineInit = 0; lineInit < lineLength; lineInit++ )
			line.append('-');

		// Go through the grid, printing out its values separated by spaces
		for( int i = 0; i < N; i++ ) {
			for( int j = 0; j < N; j++ ) {
				printFixedWidth( String.valueOf( grid[i][j] ), digits );
				// Print the vertical lines between boxes 
				if( (j < N-1) && ((j+1) % SIZE == 0) )
					System.out.print( " |" );
				System.out.print( " " );
			}
			System.out.println();

			// Print the horizontal line between boxes
			if( (i < N-1) && ((i+1) % SIZE == 0) )
				System.out.println( line.toString() );
		}
	}


	/* The main function reads in a Sudoku puzzle from the standard input, 
	 * unless a file name is provided as a run-time argument, in which case the
	 * Sudoku puzzle is loaded from that file.  It then solves the puzzle, and
	 * outputs the completed puzzle to the standard output. */
	public static void main( String args[] ) throws Exception {
		InputStream in = new FileInputStream("veryEasy3x3.txt");

		// The first number in all Sudoku files must represent the size of the puzzle.  See
		// the example files for the file format.
		int puzzleSize = readInteger( in );
		if( puzzleSize > 100 || puzzleSize < 1 ) {
			System.out.println("Error: The Sudoku puzzle size must be between 1 and 100.");
			System.exit(-1);
		}

		ChessSudoku s = new ChessSudoku( puzzleSize );
		
		// You can modify these to add rules to your sudoku
		s.knightRule = false;
		s.kingRule = false;
		s.queenRule = false;
		
		// read the rest of the Sudoku puzzle
		s.read( in );

		System.out.println("Before the solve:");
		s.print();
		System.out.println();

		// Solve the puzzle by finding one solution.
		s.solve(false);

		// Print out the (hopefully completed!) puzzle
		System.out.println("After the solve:");
		s.print();
	}
}

