package pathfinder.informed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Comparator;

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
    	
    	Comparator<SearchTreeNode> compareCosts = new Comparator<SearchTreeNode>() {
    		@Override
    		public int compare(SearchTreeNode node1, SearchTreeNode node2) {
        		if (problem.getCost(node1.state) > problem.getCost(node2.state)) {
        			return 1;
        		}
        		if (problem.getCost(node1.state) < problem.getCost(node2.state)) {
        			return -1;
        		}
        		return 0;
        	};	
    	};
        
        PriorityQueue<SearchTreeNode> frontier = new PriorityQueue<SearchTreeNode>(compareCosts);
        
        frontier.add(new SearchTreeNode(problem.INITIAL_STATE, null, null));
        int heuristic = 0;
        
        boolean foundKey = false;
        
        while (!frontier.isEmpty() && (!foundKey)) {
        	SearchTreeNode current = frontier.poll();
        	
        	heuristic += problem.getCost(current.state);
        	
        	if (problem.isKey(current.state)) {
        		foundKey = true; 
        	}
        	
        	if(problem.isGoal(current.state) && foundKey) {
        		return getPath(current);
        	}
        	
        	Map<String, MazeState> transitions = problem.getTransitions(current.state);
        	for (Map.Entry<String, MazeState> transition : transitions.entrySet()) {
        		frontier.add(new SearchTreeNode(transition.getValue(), transition.getKey(), current));
        	}
        }
        return null;
        
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
    
}

/**
 * SearchTreeNode that is used in the Search algorithm to construct the Search
 * tree.
 */
class SearchTreeNode {
    
    MazeState state;
    String action;
    SearchTreeNode parent;
    
    /**
     * Constructs a new SearchTreeNode to be used in the Search Tree.
     * 
     * @param state The MazeState (row, col) that this node represents.
     * @param action The action that *led to* this state / node.
     * @param parent Reference to parent SearchTreeNode in the Search Tree.
     */
    SearchTreeNode (MazeState state, String action, SearchTreeNode parent) {
        this.state = state;
        this.action = action;
        this.parent = parent;
    }
    
    // Helper Methods
    //-----------------------------------------------------------------------------
    
    
}