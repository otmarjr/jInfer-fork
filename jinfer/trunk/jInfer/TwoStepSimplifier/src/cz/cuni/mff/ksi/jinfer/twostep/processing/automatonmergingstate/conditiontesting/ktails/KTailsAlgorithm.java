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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.ktails;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTester;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * k,h-context equivalence criterion implementation.
 *
 * It first finds all contexts of two states, then compares each pair. Returns
 * list of alternative merge lists of pairs.
 *
 * Not using k-grams algorithm.
 *
 * @author anti
 */
public class KTailsAlgorithm<T> implements MergeConditionTester<T> {

    private int k;

    /**
     * Setting k,h. It has to be k >= h, or exception thrown (cannot merge more
     * than k states on path, when only k of them were examined).
     *
     * @param k
     * @param h
     */
    public KTailsAlgorithm(final int k) {
        this.k = k;
        if (!(k > 0)) {
            throw new IllegalArgumentException("Parameter k must be greater than 0.");
        }
    }

    public KTBucket<T> findKTails(final int _k, final State<T> state, final Map<State<T>, Set<Step<T>>> delta) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        final KTBucket<T> result = new KTBucket<T>(_k);
        int sum = 0;
        for (Step<T> step : delta.get(state)) {
            sum += step.getUseCount();
        }

        if (_k > 1) {
            for (Step<T> step : delta.get(state)) {
                if (step.getDestination().isFinal()) {
                    result.add(step, (double) step.getUseCount() / (double) sum); // Must check if there are k-1 items!
                }

                KTBucket<T> fromHim = findKTails(_k - 1, step.getDestination(), delta);
                fromHim.preceede(step, (double) step.getUseCount() / (double) sum); // Must check if there are k-1 items!
                result.addAll(fromHim);
            }
            return result;
        } else if (_k == 1) {
            for (Step<T> step : delta.get(state)) {
                if (step.getDestination().isFinal()) {
                    result.add(step, (double) step.getUseCount() / (double) sum);
                }
            }
            return result;
        }
        throw new IllegalStateException("This is impossible.");
    }

    @Override
    public List<List<List<State<T>>>> getMergableStates(final Automaton<T> automaton) throws InterruptedException {
        final Map<State<T>, Set<Step<T>>> delta = automaton.getDelta();
        final List<List<List<State<T>>>> alternatives = new ArrayList<List<List<State<T>>>>();

        final Map<State<T>, KTBucket<T>> stateStrings = new HashMap<State<T>, KTBucket<T>>();
        for (State<T> state1 : delta.keySet()) {
            stateStrings.put(state1, this.findKTails(k, state1, delta));
        }

        for (State<T> state1 : delta.keySet()) {
            for (State<T> state2 : delta.keySet()) {
                if (state1.equals(state2)) {
                    continue;
                }
                KTBucket<T> state1strings = stateStrings.get(state1);
                KTBucket<T> state2strings = stateStrings.get(state2);
                if (state1strings.getKTails().equals(state2strings.getKTails())) {
                    List<State<T>> mergePair = new ArrayList<State<T>>();
                    mergePair.add(state1);
                    mergePair.add(state2);
                    List<List<State<T>>> ret = new ArrayList<List<State<T>>>();
                    ret.add(mergePair);
                    alternatives.add(ret);
                }
            }
        }

        return alternatives;
    }

    public void mergeStates(Automaton<T> automaton) throws InterruptedException {
        boolean search = true;
        boolean found = false;
        List<List<List<State<T>>>> result = Collections.emptyList();
        while (search) {
            search = false;
            for (State<T> state1 : automaton.getDelta().keySet()) {
                for (State<T> state2 : automaton.getDelta().keySet()) {
                    result = this.getMergableStates(automaton);
                    if (!result.isEmpty()) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    break;
                }
            }
            for (List<List<State<T>>> alt : result) {
                for (List<State<T>> merg : alt) {
                    automaton.mergeStates(merg);
                    search = true;
                    found = false;
                }
            }
        }
    }
}
