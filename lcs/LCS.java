package lcs;

import java.util.Set;
import java.util.HashSet;

public class LCS {
    
    /**
     * memoCheck is used to verify the state of your tabulation after
     * performing bottom-up and top-down DP. Make sure to set it after
     * calling either one of topDownLCS or bottomUpLCS to pass the tests!
     */
    public static int[][] memoCheck;
    
    // -----------------------------------------------
    // Shared Helper Methods
    // -----------------------------------------------
    
    // [!] TODO: Add your shared helper methods here!
    
    private static Set<String> collectSolution (String rStr, int r, String cStr, int c, int[][] memo ) {
    	Set<String> soln  = new HashSet<String>() ; 
    	// Base Case 
    	if ( r == 0 || c == 0 ) {
    		return soln; 
    	}
    	
    	//Recursive Case: Matched Letters
    	return soln;
    }
    
    private Set<String> executeLCS () {
    	throw new UnsupportedOperationException();
    }

    // -----------------------------------------------
    // Bottom-Up LCS
    // -----------------------------------------------
    
    /**
     * Bottom-up dynamic programming approach to the LCS problem, which
     * solves larger and larger subproblems iterative using a tabular
     * memoization structure.
     * @param rStr The String found along the table's rows
     * @param cStr The String found along the table's cols
     * @return The longest common subsequence between rStr and cStr +
     *         [Side Effect] sets memoCheck to refer to table
     */
    public static Set<String> bottomUpLCS (String rStr, String cStr) {
        throw new UnsupportedOperationException();
    }
    
    // [!] TODO: Add any bottom-up specific helpers here!
    private static void bottomUpTableFill (String rStr, String cStr, int row, int col) {
    	int[][] bottomUpTable; 
    	
    	throw new UnsupportedOperationException();
    }
    
    // -----------------------------------------------
    // Top-Down LCS
    // -----------------------------------------------
    
    /**
     * Top-down dynamic programming approach to the LCS problem, which
     * solves smaller and smaller subproblems recursively using a tabular
     * memoization structure.
     * @param rStr The String found along the table's rows
     * @param cStr The String found along the table's cols
     * @return The longest common subsequence between rStr and cStr +
     *         [Side Effect] sets memoCheck to refer to table  
     */
    public static Set<String> topDownLCS (String rStr, String cStr) {
        throw new UnsupportedOperationException();
    }
    
    // [!] TODO: Add any top-down specific helpers here!
    private static void topDownTableFill (String rStr, String cStr, int row, int col) {
    	throw new UnsupportedOperationException();
    }
    
}
