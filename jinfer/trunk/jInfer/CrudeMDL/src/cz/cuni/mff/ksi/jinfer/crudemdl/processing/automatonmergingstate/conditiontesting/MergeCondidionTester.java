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

import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import java.util.List;

/**
 * Interface for implementing various testers of merge state condition. If state
 * are equivalent given a condition, should return a list of pairs of states.
 *
 * Each pair will be merged so, that second state is removed and first state remains
 * in automata.
 * Pairs are processed in order of list.
 *
 * Automaton can withstand request for merging state already removed in previous
 * merge. But not by subsequent calls to getMergableStates.
 *
 * For example this sequence is legal:
 * List<Pair<State, State>>: [1, 2] [2, 3] [3, 4]
 * Will merge state 2 into state 1. Then state 3 to state 2 - but state 2 is already
 * removed but automaton is clever and knows that, so it merges state 3 to state 1.
 * Then it merges state 4 to state 1 too.
 *
 * But when called a while later, it is illegal to return sequence:
 * List<Pair<State, State>>: [57, 2]
 *
 * Because state 2 doesn't exist anymore and automaton lost information about
 * previous merges.
 *
 * It is currently illegal to return a sequence where request to merge
 * an already merged state is not exactly after his merge request. Example:
 * List<Pair<State, State>>: [1, 2] [9, 8] [3, 2]
 *                                            ^^^
 * At the time, this is not possible, automaton is not clever enough to remember
 * more than one action backwards. Although this is on my TODO list.
 * Also already merged out state cannot be said to be merged out again, it can
 * be only as a first item in pair. So this is illegal too:
 * List<Pair<State, State>>: [1, 2] [3, 2]
 *                                     ^^^
 * Has to be:
 * List<Pair<State, State>>: [1, 2] [2, 3]
 *
 * TODO anti rework comment when automaton is clever enough
 * TODO anti COMMENT IS OBSOLETE! rework!
 *
 * Method actually returns not a single list of pairs, but list of list of pairs.
 * It's intended to return more alternative lists in one run. When some heuristic
 * wants to decide, it asks for alternatives different methods and then does sth.
 *
 * Actually automaton select only the first one and don't look any further.
 *
 * TODO anti remove automaton comment when this will be refactored to outside
 * optimizer
 * 
 * @author anti
 */
public interface MergeCondidionTester<T> {
  List<List<Pair<State<T>, State<T>>>> getMergableStates(State<T> mainState, State<T> mergedState, Automaton<T> automaton);
}
