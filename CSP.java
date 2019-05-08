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
		List<PartialState> state = new ArrayList<PartialState>();
		for (int i = 0; i < nMeetings; i++) {
			state.add(new PartialState(i));
		}
		ArrayList<UnaryDateConstraint> unaryConstraints = new ArrayList<UnaryDateConstraint>();
		ArrayList<BinaryDateConstraint> binaryConstraints = new ArrayList<BinaryDateConstraint>();
		for (DateConstraint constraint : constraints) {
			if (constraint.arity() == 1) {
				unaryConstraints.add((UnaryDateConstraint) constraint);
				state.get(constraint.L_VAL).unaryConst.add((UnaryDateConstraint) constraint);

			} else {
				binaryConstraints.add((BinaryDateConstraint) constraint);
				state.get(constraint.L_VAL).binaryConst.add((BinaryDateConstraint) constraint);
				state.get(((BinaryDateConstraint) constraint).R_VAL).binaryConst.add((BinaryDateConstraint) constraint);
			}
		}
		ArrayList<LocalDate> assignVals = new ArrayList<LocalDate>();
		for (int i = 0; i < nMeetings; i++) {
			assignVals.add(null);
		}

	}

	private static List<LocalDate> backtracking(ArrayList<LocalDate> assignment, List<PartialState> state) {
		if (completed(assignment)) {
			return assignment;
		}
		PartialState unassignedVar = findUnassigned(state);
		
		for (LocalDate date : unassignedVar.domain) {
			if (checkConstraints(unassignedVar, date, assignment)) {
				unassignedVar.assignment = date;
				assignment.set(unassignedVar.index, date);
				List<LocalDate> result = backtracking(assignment, state);
				if (result != null) {
					return result;
				}
				unassignedVar.assignment = null;
			}

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

	private static PartialState findUnassigned(List<PartialState> state) {
		for (PartialState party : state) {
			if (party.equals(null)) {
				return state.get(party.index);
			}
		}
		return null;
	}

	private static boolean checkConstraints(PartialState party, LocalDate assigned, List<LocalDate> assignment) {

		for (UnaryDateConstraint unaryConstraints : party.unaryConst) {
			if (!checkDates(unaryConstraints.OP, unaryConstraints.R_VAL, assigned)) {
				return false;
			}
		}
		for (BinaryDateConstraint binaryConstraint : party.binaryConst) {
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

	private class PartialState {
		ArrayList<BinaryDateConstraint> binaryConst = new ArrayList<BinaryDateConstraint>();
		ArrayList<UnaryDateConstraint> unaryConst = new ArrayList<UnaryDateConstraint>();
		int index;
		LocalDate assignment = null;
		List<LocalDate> domain = new ArrayList<LocalDate>();

		PartialState(int index) {
			this.index = index;
		}

	}

}
