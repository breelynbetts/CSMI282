package csp;
import java.time.LocalDate;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
/**
 * CSP: Calendar Satisfaction Problem Solver Provides a solution for scheduling
 * some n meetings in a given period of time and according to some unary and
 * binary constraints on the dates of each meeting.
 */
public class CSP {
    /**
     * Public interface for the CSP solver in which the number of meetings, range of
     * allowable dates for each meeting, and constraints on meeting times are
     * specified.
     * 
     * @param nMeetings   The number of meetings that must be scheduled, indexed
     *                    from 0 to n-1
     * @param rangeStart  The start date (inclusive) of the domains of each of the n
     *                    meeting-variables
     * @param rangeEnd    The end date (inclusive) of the domains of each of the n
     *                    meeting-variables
     * @param constraints Date constraints on the meeting times (unary and binary
     *                    for this assignment)
     * @return A list of dates that satisfies each of the constraints for each of
     *         the n meetings, indexed by the variable they satisfy, or null if no
     *         solution exists.
     */
    public static List<LocalDate> solve(int nMeetings, LocalDate rangeStart, LocalDate rangeEnd,
            Set<DateConstraint> constraints) {
        
       List<PartialState> state = fillDomains(nMeetings, rangeStart, rangeEnd);
        ArrayList<LocalDate> assignVals = new ArrayList<LocalDate>();
        for (int i = 0; i < nMeetings; i++) {
            state.add(new PartialState(i));
            assignVals.add(null);
        }
        
        //ArrayList<UnaryDateConstraint> unaryConstraints = new ArrayList<UnaryDateConstraint>();
        //ArrayList<BinaryDateConstraint> binaryConstraints = new ArrayList<BinaryDateConstraint>();
        for (DateConstraint constraint : constraints) {
            if (constraint.arity() == 1) {
                //unaryConstraints.add((UnaryDateConstraint) constraint);
                state.get(constraint.L_VAL).unaryConst.add((UnaryDateConstraint) constraint);
            } else {
                //binaryConstraints.add((BinaryDateConstraint) constraint);
                state.get(constraint.L_VAL).binaryConst.add((BinaryDateConstraint) constraint);
                state.get(((BinaryDateConstraint) constraint).R_VAL).binaryConst.add((BinaryDateConstraint) constraint);
            }
        }
        
        return backtracking(assignVals, state);
    }
    
    private static List<LocalDate> backtracking(ArrayList<LocalDate> assignment, List<PartialState> state) {
        if (completed(assignment)) {
            return assignment;
        }
        int unassignedIndex = findUnassigned(assignment);
        PartialState unassigned = state.get(unassignedIndex);
//        for(int i = 0; i < state.size(); i++) {
//        	System.out.println(state.get(i).domain.toString());
//        }
        for (LocalDate domain : unassigned.domain) {
            assignment.set(unassigned.index, domain);
            if (checkConstraints(unassigned, domain, assignment)) {
                List<LocalDate> result = backtracking(assignment, state);
                if (result != null) {
                    return result;
                }
                
            }
            assignment.set(unassigned.index, null);
        }
        return null;
    }
    
    private static boolean completed(ArrayList<LocalDate> assign) {
        for (LocalDate date : assign) {
            if (date == null) {
                return false;
            }
        }
        return true;
    }
    
     static int findUnassigned(List<LocalDate> assignment) {
        for (int i =0; i<assignment.size(); i++) {
            if (assignment.get(i) == null) {

                return i;
            }
        }
        return assignment.size();
    }
    private static boolean checkConstraints(PartialState party, LocalDate assigned, List<LocalDate> assignment) {
        for (UnaryDateConstraint unaryConstraints : party.unaryConst) {
        	if(assigned == null ||  unaryConstraints.R_VAL == null) {
        		continue;
        	}
            if (!checkDates(unaryConstraints.OP, unaryConstraints.R_VAL, assigned)) {
                return false;
            }
        }
        for (BinaryDateConstraint binaryConstraint : party.binaryConst) {
        	if(assigned == null ||  party == null || assignment == null) {
        		continue;
        	}
            if (!checkBinaryConstraint(binaryConstraint, assigned, party, assignment)) {
                return false;
            }
        }
        return true;
    }
    private static boolean checkDates(String operand, LocalDate day1, LocalDate day2) {
    	
        switch (operand) {
        case "==":
            if (day1.isEqual(day2))
                return true;
            break;
        case "!=":
            if (!day1.isEqual(day2))
                return true;
            break;
        case ">":
            if (day1.isAfter(day2))
                return true;
            break;
        case "<":
            if (day1.isBefore(day2))
                return true;
            break;
        case ">=":
            if (day1.isAfter(day2) || day1.isEqual(day2))
                return true;
            break;
        case "<=":
            if (day1.isBefore(day2) || day1.isEqual(day2))
                return true;
            break;
        }
        return false;
    }
    
    private static List<PartialState> fillDomains(int nMeetings, LocalDate rangeStart, LocalDate rangeEnd){
    	List<PartialState> states = new ArrayList<>(nMeetings);
    	for(int i = 0; i < nMeetings; i++) {
    		List<LocalDate> currentDomains = new ArrayList<>();
    		LocalDate currentLocalDate = rangeStart;
    		while(!currentLocalDate.equals(rangeEnd)) {
    			currentDomains.add(currentLocalDate);
    			currentLocalDate = currentLocalDate.plusDays(1);
    		}
    		currentDomains.add(rangeEnd);
    		PartialState currentPartialState = new PartialState(i);
    		currentPartialState.domain  = currentDomains;
    		states.add(currentPartialState );
    	}
    	return states;
    }
    
    private static boolean checkBinaryConstraint(BinaryDateConstraint constraint, LocalDate date, PartialState party,
            List<LocalDate> assignment) {
        if (party.index == constraint.L_VAL) {
            if (assignment.get(constraint.R_VAL) == null) {
                return true;
            }
            checkDates(constraint.OP, date, assignment.get(constraint.R_VAL));
        } else {
            if (assignment.get(constraint.L_VAL) == null) {
                return true;
            }
            checkDates(constraint.OP, assignment.get(constraint.L_VAL), date);
        }
        return true;
    }
    // change to variable
    private static class PartialState {
        ArrayList<BinaryDateConstraint> binaryConst;
        ArrayList<UnaryDateConstraint> unaryConst;
        int index;
        LocalDate assignment = null;
        List<LocalDate> domain = new ArrayList<LocalDate>();
        PartialState(int index) {
        	 this.domain = new ArrayList<LocalDate>();
             this.binaryConst = new ArrayList<BinaryDateConstraint>();
             this.unaryConst = new ArrayList<UnaryDateConstraint>();
             this.index = index;
        }
    }
}