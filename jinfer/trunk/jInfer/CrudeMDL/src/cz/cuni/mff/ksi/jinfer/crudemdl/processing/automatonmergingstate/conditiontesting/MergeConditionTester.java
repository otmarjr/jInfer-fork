/*
 *  Copyright (C) 2010 anti
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.conditiontesting;

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import java.util.List;

/**
 * Interface for implementing various testers of merge state condition. If state
 * are equivalent given a condition, should return a list of list of list of states.
 *
 * First list: represents merge alternatives - that is, calling procedure can decide
 * which alternative from the first list it will implement and merge states specified
 * in
 * Second list: which contains merge sequences (lists). Merge sequence is
 * list of states that are to be merged into one state. All merge sequences in this
 * list have to be processed and appropriate states merged.
 *
 * For example 2,1-context tester will find states A and B have same context and
 * have to be merged. It creates a List{A, B} - that is merge sequence (all
 * states in sequence will be merged into one of them). But as 2,1-context method
 * requires, also one preceeding states in context have to be merged, say states
 * C and D. So it will create List{C, D}, merge sequence. Then, to enforce the states
 * are merged in order C+D, then A+B, a second list:
 * List{List{C,D}, List{A,B}} is created. That is a complete merge alternative.
 * If tester founds another alternative, with different contexts, it creates
 * another alternative, say List{List{E,F}, List{A,B}}.
 * And returns List{alternatives}.
 *
 * Each merge sequence will be merged so, that second state is removed and first
 * state remains in automata.
 *
 * Automaton can withstand request for merging a state, that has been already
 * removed in previous merge (thorough whole automata lifetime).
 *
 * For example this alternative is legal:
 * List<List<State>>: {{1, 2}, {2, 3}, {3, 4}}
 * Will merge state 2 into state 1. Then state 3 to state 2 - state 2 is already
 * removed, but automaton is clever and knows what to do, so it merges state 3 to state 1.
 * Then it merges state 4 to state 1 too.
 *
 * When called later, it is legal to return:
 * List<Pair<State, State>>: [57, 2]
 * State 2 doesn't exist anymore and automaton is clever enough to merge state
 * 57 into state 1.
 *
 * @author anti
 */
public interface MergeConditionTester<T> {
  List<List<List<State<T>>>> getMergableStates(final State<T> state1, final State<T> state2, final Automaton<T> automaton);
}
