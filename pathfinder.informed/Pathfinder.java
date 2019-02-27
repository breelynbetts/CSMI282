// Breelyn Betts and KeAnna Anglin
package pathfinder.informed;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashSet;

/**
 * Maze Pathfinding algorithm that implements a basic, uninformed, breadth-first tree search.
 */
public class Pathfinder {
	
    /**
     * Given a MazeProblem, which specifies the actions and transitions available in the
     * search, returns a solution to the problem as a sequence of actions that leads from
     * the initial to a goal state.
     * 
     * @param problem A MazeProblem that specifies the maze, actions, transitions.
     * @return An ArrayList of Strings representing actions that lead from the initial to
     * the goal state, of the format: ["R", "R", "L", ...]
     */
	
    public static ArrayList<String> solve (MazeProblem problem) {   
    	if( problem.KEY_STATE.size() == 0 ) {
    		return null;
    	} 
    	if (problem.GOAL_STATE.size() == 0 ) {
    		return null;
    	}
    	ArrayList<String> path = new ArrayList<>();
    	ArrayList<String> result;
    	MazeState keyState = null;
    	
    	for (MazeState hashKey : problem.KEY_STATE) {
    		keyState = hashKey;
    	}
    	result = solve(problem, problem.INITIAL_STATE, problem.KEY_STATE);
    	
    	if (result == null ) { return null; } 
    	path.addAll(result);
    	
    	result = solve(problem, keyState, problem.GOAL_STATE);
    	if (result == null) { return null; } 
    	path.addAll(result);
    
        return path;
    }
    
    /**
     * Given a leaf node in the search tree (a goal), returns a solution by traversing
     * up the search tree, collecting actions along the way, until reaching the root
     * 
     * @param last SearchTreeNode to start the upward traversal at (a goal node)
     * @return ArrayList sequence of actions; solution of format ["U", "R", "U", ...]
     */
    private static ArrayList<String> getPath (SearchTreeNode last) {
        ArrayList<String> result = new ArrayList<>();
        for (SearchTreeNode current = last; current.parent != null; current = current.parent) {
            result.add(current.action);
        }
        Collections.reverse(result);
        return result;
    }

    // Helper Methods
    //------------------------------------------------------------------------------
    
    private static int getHeuristic(MazeState current, HashSet<MazeState> goalState) {
    	int currCost = 0;
    	int lowestCost = Integer.MAX_VALUE;
    	for (MazeState goal: goalState) {
    		currCost = Math.abs(current.col - goal.col ) + Math.abs(current.row - goal.row);
    		if (currCost < lowestCost) {
    			lowestCost = currCost;
    		}
    	}
    	return currCost;
    }
    
    private static int getHistory(SearchTreeNode parent, MazeState current, MazeProblem problem) {
    	return parent.history + problem.getCost(current);
    }
    
    /**
     *  Uses a Lambda Expression to compare the costs between two SearchTreeNodes
     */

    private static Comparator<SearchTreeNode> compareCosts = (stn1, stn2) -> {
		return stn1.evaluate() - stn2.evaluate();
	};
	
	/** 
	 * Passing in a current state, the goal of this method is to find the optimal path from 
	 * an initial state to a certain goal state. 
	 * 
	 * @param p The MazeProblem in which the method is looking for the solution 
	 * @param s The initial MazeState that we are trying to find a path to 
	 * @param dests a HashSet of MazeStates that provide the goals that we are trying to get to
	 * @return An ArrayList of Strings representing actions that lead from the initial to
     * the goal state, of the format: ["R", "R", "L", ...]
	 */
	
    private static ArrayList<String> solve(MazeProblem p, MazeState s, HashSet<MazeState> dests) {
    	PriorityQueue<SearchTreeNode> frontier = new PriorityQueue<>(compareCosts);
        HashSet<MazeState> graveyard = new HashSet<>();
        SearchTreeNode curr = new SearchTreeNode(s, null, null, 0, 0 );
        
        frontier.add(curr);
        
        while(!frontier.isEmpty()) {
        	
        	curr = frontier.poll();
        	graveyard.add(curr.state);

        	if (dests == p.KEY_STATE) {
        		if (p.isKey(curr.state)) {
        			return getPath(curr);
        		}
        	} else if (dests == p.GOAL_STATE) {
        		if ( p.isGoal(curr.state)) {
        			return getPath(curr);
        		}
        	}
        	Map<String, MazeState> transitions = p.getTransitions(curr.state);
        	for (Map.Entry<String, MazeState> transition : transitions.entrySet()) {
        		SearchTreeNode child = new SearchTreeNode(transition.getValue(), transition.getKey(), curr, 
        				getHistory(curr, transition.getValue(), p), getHeuristic(curr.state, dests) );
        		if (!graveyard.contains(child.state)) {
        			frontier.add(child);
        		}
        	}  
      } return null;   
    } 
}

/**
 * SearchTreeNode that is used in the Search algorithm to construct the Search
 * tree.
 */
class SearchTreeNode {
    
    MazeState state;
    String action;
    SearchTreeNode parent;
    int history;
    int heuristic; 
    
    /**
     * Constructs a new SearchTreeNode to be used in the Search Tree.
     * 
     * @param state The MazeState (row, col) that this node represents.
     * @param action The action that *led to* this state / node.
     * @param parent Reference to parent SearchTreeNode in the Search Tree.
     * @param history The accumulated previous cost of moving the SearchTreeNode to that state 
     * @param heuristic The current cost of moving the SearchTreeNode to that state
     */
    SearchTreeNode (MazeState state, String action, SearchTreeNode parent, int history, int heuristic) {
        this.state = state;
        this.action = action;
        this.parent = parent;
        this.history = history;
        this.heuristic = heuristic;
    }
    
    public int evaluate() {
    	return history + heuristic; 
    }
    
}
