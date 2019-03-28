package lcs;

import java.util.Set;
import java.util.HashSet;

public class LCS {

	/**
	 * memoCheck is used to verify the state of your tabulation after performing
	 * bottom-up and top-down DP. Make sure to set it after calling either one of
	 * topDownLCS or bottomUpLCS to pass the tests!
	 */
	public static int[][] memoCheck;

	// -----------------------------------------------
	// Shared Helper Methods
	// -----------------------------------------------

	// SHARED HELPER METHODS!
	
	/**
	 * The collectSolution method is used to recurse through the table to find the 
	 * LCS for the strings checking.
	 * 
	 * @param rStr	The String found along the table's rows
	 * @param r		The index of the row string in the table		   
	 * @param cStr	The String found along the table's cols
	 * @param c		The index of the col string in the table
	 * @param memo 	The table we are finding the LCS for
	 * @return A set of solutions for the table and parameters provided. 
	 */
	private static Set<String> collectSolution(String rStr, int r, String cStr, int c, int[][] memo) {
		// Base Case
		if (r == 0 || c == 0) {
			Set<String> soln = new HashSet<String>();
			soln.add("");
			return soln;
		}

		// Recursive Case: Matched Letter

		System.out.println(r);
		System.out.println(c);

		if (rStr.charAt(r) == cStr.charAt(c)) {
			return updatedLCS(rStr.charAt(r), collectSolution(rStr, r - 1, cStr, c - 1, memo));
		}

		// Recursive case: Mismatched Letters
		Set<String> newLCS = new HashSet<>();
		if (memo[r][c - 1] >= memo[r - 1][c]) {
			newLCS.addAll(collectSolution(rStr, r, cStr, c - 1, memo));
		}
		if (memo[r][c - 1] <= memo[r - 1][c]) {
			newLCS.addAll(collectSolution(rStr, r - 1, cStr, c, memo));
		}
		return newLCS;
	}

	/**
	 * UpdatedLCS takes the previous set of strings and adds a new character. We employ this 
	 * method to append the new character to the existing set of strings. 
	 * 
	 * @param adding	The character from the substring that we are adding to the set
	 * @param LCS  		The already existing set of strings that contains result
	 * @return			The modified set of strings that includes the new character. 
	 */
	private static Set<String> updatedLCS(char adding, Set<String> LCS) {
		Set<String> copyLCS = new HashSet<String>();

		for (String LC : LCS) {
			copyLCS.add(LC + Character.toString(adding));
		}
		return copyLCS;
	}

	// -----------------------------------------------
	// Bottom-Up LCS
	// -----------------------------------------------

	/**
	 * Bottom-up dynamic programming approach to the LCS problem, which solves
	 * larger and larger subproblems iterative using a tabular memoization
	 * structure.
	 * 
	 * @param rStr The String found along the table's rows
	 * @param cStr The String found along the table's cols
	 * @return The longest common subsequence between rStr and cStr + [Side Effect]
	 *         sets memoCheck to refer to table
	 */
	public static Set<String> bottomUpLCS(String rStr, String cStr) {
		rStr = "0" + rStr;
		cStr = "0" + cStr;
		memoCheck = bottomUpTableFill(rStr, cStr, memoCheck);
		return collectSolution(rStr, rStr.length() - 1, cStr, cStr.length() - 1, memoCheck);
	}

	// BOTTOM-UP HELPER METHODS!
	
	/**
	 * BottomUpTableFilling method fills the table using recurrence. Every coordinate in the table
	 * is assigned a value. 
	 * 
	 * @param rStr The String found along the table's rows
	 * @param cStr The String found along the table's cols
	 * @param memo The table that we are filling out the values for 
	 * @return A complete table filled with values related to the length of LCS.
	 */
	private static int[][] bottomUpTableFill(String rStr, String cStr, int[][] memo) {
		memo = new int[rStr.length()][cStr.length()];

		// start at [1,1] - relies on the parameter to start as (1,1)

		for (int row = 0; row < rStr.length(); row++) {
			for (int col = 0; col < cStr.length(); col++) {
				if (row == 0 && col == 0) {
					memo[row][col] = 0;
				} else {
					if (rStr.charAt(row) == cStr.charAt(col)) {
						memo[row][col] = 1 + memo[row - 1][col - 1];
					} else {
						if (row == 0) {
							memo[row][col] = memo[row][col - 1];
						} else if (col == 0) {
							memo[row][col] = memo[row - 1][col];
						} else {
							memo[row][col] = Math.max(memo[row - 1][col], memo[row][col - 1]);
						}
					}
				}
			}
		}

		return memo;
	}

	// -----------------------------------------------
	// Top-Down LCS
	// -----------------------------------------------

	/**
	 * Top-down dynamic programming approach to the LCS problem, which solves
	 * smaller and smaller subproblems recursively using a tabular memoization
	 * structure.
	 * 
	 * @param rStr The String found along the table's rows
	 * @param cStr The String found along the table's cols
	 * @return The longest common subsequence between rStr and cStr + [Side Effect]
	 *         sets memoCheck to refer to table
	 */
	public static Set<String> topDownLCS(String rStr, String cStr) {
		rStr = "0" + rStr;
		cStr = "0" + cStr;
		boolean[][] haveVisited = new boolean[rStr.length()][cStr.length()];
		memoCheck = new int[rStr.length()][cStr.length()];
		topDownTableFill(rStr, rStr.length() - 1, cStr, cStr.length() - 1, memoCheck, haveVisited);
		return collectSolution(rStr, rStr.length() - 1, cStr, cStr.length() - 1, memoCheck);

	}

	// TOP-DOWN HELPER METHODS!

	/**
	 * TopDownTableFill fills in the table with values using a 
	 * recursive algorithm with memoization.
	 * 
	 * @param rStr 		The String found along the table's rows
	 * @param rIndex	The index of the current row
	 * @param cStr   	The String found along the table's cols
	 * @param cIndex 	The index of the current col
	 * @param memo		The table that we are filling in 
	 * @param visited	A table of the visited values, aids in memoization
	 * @return The integer value at the current rIndex and cIndex. 
	 */
	private static int topDownTableFill(String rStr, int rIndex, String cStr, int cIndex, int[][] memo,
			boolean[][] visited) {

		if (rIndex == 0 || cIndex == 0) {
			memo[rIndex][cIndex] = 0;
			return 0;
		}
		// exception happening because the string length is 0
		if (visited[rIndex][cIndex] != true) {
			if (rStr.charAt(rIndex) == cStr.charAt(cIndex)) {
				memo[rIndex][cIndex] = topDownTableFill(rStr, rIndex - 1, cStr, cIndex - 1, memo, visited) + 1;

			} else {
				memo[rIndex][cIndex] = Math.max(topDownTableFill(rStr, rIndex - 1, cStr, cIndex, memo, visited),
						topDownTableFill(rStr, rIndex, cStr, cIndex - 1, memo, visited));

			}
			visited[rIndex][cIndex] = true;
		}
		return memo[rIndex][cIndex];
	}
}
