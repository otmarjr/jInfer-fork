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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.skstrings;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTester;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
public class SKStrings<T> implements MergeConditionTester<T> {

    private int k;
    private double s;
    private String strategy;
    // Despite the definition in Ramans work, his example program implementation
    // does not consider k-strings having length smaller than k but ending
    // in an accepting state. This flag is used to indicate if this class
    // should behave accordingly to the definition (true) or skstr program (false)
    private boolean considerKStringsShorterThanKEndingInAcceptingState;

    public SKStrings(final int k, final double s, final String strategy, final boolean considerShorterKStrings) {
        this(k, s, strategy);
        this.considerKStringsShorterThanKEndingInAcceptingState = considerShorterKStrings;
    }

    /**
     * Setting k,h. It has to be k >= h, or exception thrown (cannot merge more
     * than k states on path, when only k of them were examined).
     *
     * @param k
     * @param h
     */
    public SKStrings(final int k, final double s, final String strategy) {
        this.k = k;
        this.s = s;
        this.strategy = strategy;
        if (!((k > 0) && (s >= 0) && (s <= 1))) {
            throw new IllegalArgumentException("Parameter k must be greater than 0. Parameter s must satisfy: 0 <= s <= 1.");
        }
    }

    public SKBucket<T> findSKStrings(final int _k, final State<T> state, final Map<State<T>, Set<Step<T>>> delta) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        final SKBucket<T> result = new SKBucket<T>();
        int sum = 0;
        Map<T, Integer> symbolsCounts = new HashMap<>();

        for (Step<T> step : delta.get(state)) {
            sum += step.getUseCount();
            
            Integer currentSymbolCount = symbolsCounts.get(step.getAcceptSymbol());

            if (currentSymbolCount != null) {
                symbolsCounts.put(step.getAcceptSymbol(), currentSymbolCount + step.getUseCount());
            } else {
                symbolsCounts.put(step.getAcceptSymbol(), step.getUseCount());
            }
        }

        if (_k > 1) {
            for (Step<T> step : delta.get(state)) {
                if (step.getDestination().getFinalCount() == 0) {
                    SKBucket<T> fromHim = findSKStrings(_k - 1, step.getDestination(), delta);
                    fromHim.preceede(step, (double) symbolsCounts.get(step.getAcceptSymbol()) / (double) sum);
                    //fromHim.preceede(step, (double) step.getUseCount() / (double) sum);
                    result.addAll(fromHim);
                } else {
                    if (considerKStringsShorterThanKEndingInAcceptingState) {
                        SKBucket<T> fromHim = new SKBucket<T>();
                        result.add(step, (double) symbolsCounts.get(step.getAcceptSymbol()) / (double) sum);
                        //result.add(step, (double) step.getUseCount() / (double) sum);
                        fromHim.preceede(step, (double) symbolsCounts.get(step.getAcceptSymbol()) / (double) sum);
                        //fromHim.preceede(step, (double) step.getUseCount() / (double) sum);
                        result.addAll(fromHim);
                    }
                }
            }
            return result;
        } else if (_k == 1) {
            for (Step<T> step : delta.get(state)) {
                result.add(step, (double) symbolsCounts.get(step.getAcceptSymbol()) / (double) sum);
                // result.add(step, (double) step.getUseCount() / (double) sum);
            }
            return result;
        }
        throw new IllegalStateException("This is impossible.");
    }

    public boolean isANDStrategy() {
        return "AND".equals(this.strategy);
    }

    @Override
    public List<List<List<State<T>>>> getMergableStates(final Automaton<T> automaton) throws InterruptedException {
        final Map<State<T>, Set<Step<T>>> delta = automaton.getDelta();
        final List<List<List<State<T>>>> alternatives = new ArrayList<List<List<State<T>>>>();

        final Map<State<T>, SKBucket<T>> stateStrings = new HashMap<State<T>, SKBucket<T>>();
        for (State<T> state1 : delta.keySet()) {
            stateStrings.put(state1, this.findSKStrings(k, state1, delta));
        }

        if ("AND".equals(this.strategy)) {
            for (State<T> state1 : delta.keySet()) {
                for (State<T> state2 : delta.keySet()) {
                    if (state1.equals(state2)) {
                        continue;
                    }
                    SKBucket<T> state1strings = stateStrings.get(state1);
                    SKBucket<T> state2strings = stateStrings.get(state2);

                    if (state1strings.getMostProbable(this.s).areSubset(state2strings)
                            && state2strings.getMostProbable(this.s).areSubset(state1strings)) {
                        List<State<T>> mergePair = new ArrayList<State<T>>();
                        mergePair.add(state1);
                        mergePair.add(state2);
                        List<List<State<T>>> ret = new ArrayList<List<State<T>>>();
                        ret.add(mergePair);

                        if (!alternatives.stream().anyMatch(l2 -> l2.stream().anyMatch(l
                                -> (l.get(0).equals(state1) && l.get(1).equals(state2))
                                || (l.get(0).equals(state2) && l.get(1).equals(state1))
                        ))) {
                            alternatives.add(ret);
                        }
                    }
                }
            }
        } else if ("OR".equals(this.strategy)) {
            for (State<T> state1 : delta.keySet()) {
                for (State<T> state2 : delta.keySet()) {
                    if (state1.equals(state2)) {
                        continue;
                    }
                    SKBucket<T> state1strings = stateStrings.get(state1);
                    SKBucket<T> state2strings = stateStrings.get(state2);
                    if (state1strings.getMostProbable(this.s).areSubset(state2strings) || state2strings.getMostProbable(this.s).areSubset(state1strings)) {
                        List<State<T>> mergePair = new ArrayList<State<T>>();
                        mergePair.add(state1);
                        mergePair.add(state2);
                        List<List<State<T>>> ret = new ArrayList<List<State<T>>>();
                        ret.add(mergePair);
                        alternatives.add(ret);
                    }
                }
            }
        } else if ("LAX".equals(this.strategy)) {
            for (State<T> state1 : delta.keySet()) {
                for (State<T> state2 : delta.keySet()) {
                    if (state1.equals(state2)) {
                        continue;
                    }
                    SKBucket<T> state1strings = stateStrings.get(state1);
                    SKBucket<T> state2strings = stateStrings.get(state2);
                    SKBucket<T> state1tops = state1strings.getMostProbable(this.s);
                    SKBucket<T> state2tops = state2strings.getMostProbable(this.s);
                    Iterator<SKString<T>> s1it = state1tops.getSKStrings().iterator();
                    Iterator<SKString<T>> s2it = state2tops.getSKStrings().iterator();
                    if (state1tops.getSKStrings().size() == state2tops.getSKStrings().size()) {
                        boolean same = true;
                        while (s1it.hasNext() && same) {
                            if (!s1it.next().equals(s2it.next())) {
                                same = false;
                            }
                        }
                        if (same) {
                            List<State<T>> mergePair = new ArrayList<State<T>>();
                            mergePair.add(state1);
                            mergePair.add(state2);
                            List<List<State<T>>> ret = new ArrayList<List<State<T>>>();
                            ret.add(mergePair);
                            alternatives.add(ret);
                        }
                    }
                }
            }
        }
        return alternatives;
    }

    protected void doRamansBasedSKStrings(Automaton<T> automaton) throws InterruptedException {
        boolean merging = true;

        Set<State<T>> skStringsDisplayedStates = new HashSet<>();

        State<T> state1Merge = null;
        State<T> state2Merge = null;

        boolean restart = false;

        while (merging) {
            merging = false;
            final Map<State<T>, Set<Step<T>>> delta = automaton.getDelta();

            List<State<T>> sortedStates = delta.keySet().stream().sorted((sx, sy) -> Integer.compare(sx.getName(), sy.getName())).collect(Collectors.toList());

            if (restart) {
                state1Merge = sortedStates.get(0);
                state2Merge = null;
                skStringsDisplayedStates.clear();;
            }
            
            final Map<State<T>, SKBucket<T>> stateStrings = new HashMap<>();
            
            for (State<T> st : delta.keySet()) {
                stateStrings.put(st, this.findSKStrings(k, st, delta));
            }
            for (State<T> state1 : sortedStates) {
                restart = false;
                if (state1Merge != null) {
                    if (state1.getName() < state1Merge.getName()) {
                        continue;
                    } else {
                        state1Merge = null;
                    }
                }

                for (State<T> state2 : sortedStates) {
                    if (state2.getName() <= state1.getName()) {
                        continue;
                    }

                    if (state2Merge != null) {
                        if (state2.getName() < state2Merge.getName()) {
                            continue;
                        } else {
                            state2Merge = null;
                        }
                    }

                    SKBucket<T> state1strings = stateStrings.get(state1);
                    SKBucket<T> state2strings = stateStrings.get(state2);

                    System.out.println(String.format("%d-equiv(%04d,%04d)?", k, state1.getName(), state2.getName()));

                    if (!skStringsDisplayedStates.contains(state1)) {
                        System.out.println(String.format("Strings from state %d", state1.getName()));
                        state1strings.getSKStrings().stream().sorted((sx, sy) -> Double.compare(sy.getProbability(), sx.getProbability()))
                                .forEach(s -> {
                                    int intPart = (int) (s.getProbability() * 100);
                                    int fracPart = (int) ((s.getProbability() * 100 % intPart) * 1000);
                                    System.out.println(String.format(Locale.US, "%3d.%-3d:  %s", intPart, fracPart,
                                                    s.getStr().stream().map(st -> st.getAcceptSymbol().toString()).collect(Collectors.joining(":")) + ":*UNDELIMITED*"));
                                });

                        skStringsDisplayedStates.add(state1);
                    }

                    if (state1strings.getMostProbable(this.s).areSubset(state2strings)
                            && state2strings.getMostProbable(this.s).areSubset(state1strings)) {
                        state1Merge = state1;
                        state2Merge = state2;

                        if (!skStringsDisplayedStates.contains(state2)) {
                            System.out.println(String.format("Strings from state %d", state2.getName()));
                            state2strings.getSKStrings().stream().sorted((sx, sy) -> Double.compare(sy.getProbability(), sx.getProbability()))
                                    .forEach(s -> {
                                        int intPart = (int) (s.getProbability() * 100);
                                        int fracPart = (int) ((s.getProbability() * 100 % intPart) * 1000);
                                        System.out.println(String.format(Locale.US, "%3d.%-3d:  %s", intPart, fracPart,
                                                        s.getStr().stream().map(st -> st.getAcceptSymbol().toString()).collect(Collectors.joining(":")) + ":*UNDELIMITED*"));
                                    });

                            skStringsDisplayedStates.add(state2);
                        }

                        merging = true; // Inefficient, but quick hack to avoid concurrent modification exceptions.
                        break;
                    }
                }
                if (merging) {
                    break;
                }
            }
            if (merging) {
                System.out.println(String.format("\nMerging %d & %d\n", state1Merge.getName(), state2Merge.getName()));
                if (areSkDistinguishable(state1Merge, state2Merge, automaton)) {
                    restart = true;
                    automaton.mergeStates(state1Merge, state2Merge);
                } else {
                    automaton.mergeStates(state1Merge, state2Merge);
                }

            }
        }
    }

    private boolean areSkDistinguishable(State<T> p, State<T> q, Automaton<T> aut) throws InterruptedException {
        List<SKString<T>> skstringsP = this.findSKStrings(k, p, aut.getDelta()).getSKStrings();
        List<SKString<T>> skstringsQ = this.findSKStrings(k, q, aut.getDelta()).getSKStrings();

        if (skstringsP.size() != skstringsQ.size()) {
            return true;
        }

        for (int i = 0; i < skstringsP.size(); i++) {
            SKString<T> skStrP = skstringsP.get(i);
            SKString<T> skStrQ = skstringsQ.get(i);
            if (skStrP.distinguishableFrom(skStrQ)) {
                return true;
            }
        }

        return false;
    }

    public void mergeStates(Automaton<T> automaton) throws InterruptedException {
        if (this.isANDStrategy()) {
            this.doRamansBasedSKStrings(automaton);
        } else {
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
}
