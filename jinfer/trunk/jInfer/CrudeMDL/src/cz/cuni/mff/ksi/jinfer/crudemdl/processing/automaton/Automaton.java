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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton;

import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.mergecondition.MergeCondidionTester;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.log4j.Logger;

/**
 * Class representing deterministic finite automaton.
 * Can simplify itself when given mergingCondition
 * 
 * @author anti
 */
public class Automaton<T> {
  private static final Logger LOG = Logger.getLogger(Automaton.class);
  // TODO anti Comment the fields
  protected State<T> initialState;
  protected final Map<State<T>, Set<Step<T>>> delta;
  protected final Map<State<T>, Set<Step<T>>> reverseDelta;
  protected int newStateName;

  /**
   * Constructor which doesn't create initialState
   */
  public Automaton() {
    this.newStateName= 1;
    this.delta= new TreeMap<State<T>, Set<Step<T>>>();
    this.reverseDelta= new TreeMap<State<T>, Set<Step<T>>>();
  }

  /**
   * @param createInitialState - true= create initial state, false- as default constructor
   */
  public Automaton(final boolean createInitialState) {
    this();
    if (createInitialState) {
      this.initialState= this.createNewState();
    }
  }

  protected State<T> createNewState() {
    final State<T> newState= new State<T>(0, this.newStateName, this);
    this.newStateName++;
    this.delta.put(newState, new HashSet<Step<T>>());
    this.reverseDelta.put(newState, new HashSet<Step<T>>());
    return newState;
  }

  private Step<T> getOutStepOnSymbol(final State<T> state, final T symbol) {
    final Set<Step<T>> steps= this.delta.get(state);
    for (Step<T> step : steps) {
      if (step.getAcceptSymbol().equals(symbol)) {
        return step;
      }
    }
    return null;
  }

  private Step<T> createNewStep(final T onSymbol, final State<T> source, final State<T> destination) {
    final Step<T> newStep= new Step<T>(onSymbol, source, destination, 1);
    this.delta.get(source).add(newStep);
    this.reverseDelta.get(destination).add(newStep);
    return newStep;
  }

  /**
   * Given symbolString, iterates through it and follows steps in automaton. When there
   * isn't a step to follow, new state and step is created. Resulting in tree-formed automaton.
   * 
   * @param symbolString - list of symbols (one word from accepting language)
   */
  public void buildPTAOnSymbol(final List<T> symbolString) {
    State<T> xState= this.getInitialState();
    for (T symbol : symbolString) {
      final Step<T> xStep= this.getOutStepOnSymbol(xState, symbol);
      if (xStep != null) {
        xStep.incUseCount();
        xState= xStep.getDestination();
      } else {
        final State<T> newState= this.createNewState();
        final Step<T> newStep= this.createNewStep(symbol, xState, newState);
        assert newStep.getDestination().equals(newState);
        xState= newStep.getDestination();
      }
    }
    xState.incFinalCount();
  }

  private void collapseStepsAfterMerge(final State<T> mainState) {
    final Map<State<T>, Map<T, Step<T>>> inBuckets= new HashMap<State<T>, Map<T, Step<T>>>();

    final Set<Step<T>> inSteps= new HashSet<Step<T>>(this.reverseDelta.get(mainState));
    for (Step<T> inStep : inSteps) {
      if (inBuckets.containsKey(inStep.getSource())) {
        if (inBuckets.get(inStep.getSource()).containsKey(inStep.getAcceptSymbol())) {
          inBuckets.get(inStep.getSource()).get(inStep.getAcceptSymbol()).incUseCount(inStep.getUseCount());
          this.delta.get(inStep.getSource()).remove(inStep);
          this.reverseDelta.get(inStep.getDestination()).remove(inStep);
        } else {
          inBuckets.get(inStep.getSource()).put(inStep.getAcceptSymbol(), inStep);
        }
      } else {
        inBuckets.put(inStep.getSource(), new HashMap<T, Step<T>>());
        inBuckets.get(inStep.getSource()).put(inStep.getAcceptSymbol(), inStep);
      }
    }

    final Map<State<T>, Map<T, Step<T>>> outBuckets= new HashMap<State<T>, Map<T, Step<T>>>();
    final Set<Step<T>> outSteps= new HashSet<Step<T>>(this.delta.get(mainState));
    for (Step<T> outStep : outSteps) {
      if (outBuckets.containsKey(outStep.getDestination())) {
        if (outBuckets.get(outStep.getDestination()).containsKey(outStep.getAcceptSymbol())) {
          outBuckets.get(outStep.getDestination()).get(outStep.getAcceptSymbol()).incUseCount(outStep.getUseCount());
          this.delta.get(outStep.getSource()).remove(outStep);
          this.reverseDelta.get(outStep.getDestination()).remove(outStep);
        } else {
          outBuckets.get(outStep.getDestination()).put(outStep.getAcceptSymbol(), outStep);
        }
      } else {
        outBuckets.put(outStep.getDestination(), new HashMap<T, Step<T>>());
        outBuckets.get(outStep.getDestination()).put(outStep.getAcceptSymbol(), outStep);
      }
    }
  }

  private void mergeStates(final State<T> mainState, final State<T> mergedState) {
    if (mergedState.equals(mainState)) {
      return;
    }

    /* insteps */
    final Set<Step<T>> mergedStateInSteps= this.reverseDelta.get(mergedState);
    for (Step<T> mergedStateInStep : mergedStateInSteps) {
      mergedStateInStep.setDestination(mainState);
    }
    this.reverseDelta.remove(mergedState);
    this.reverseDelta.get(mainState).addAll(mergedStateInSteps);

    /* outsteps */
    final Set<Step<T>> mergedStateOutSteps= this.delta.get(mergedState);
    for (Step<T> mergedStateOutStep : mergedStateOutSteps) {
        mergedStateOutStep.setSource(mainState);
    }
    this.delta.remove(mergedState);
    this.delta.get(mainState).addAll(mergedStateOutSteps);


    /* finalCount */
    mainState.incFinalCount(mergedState.getFinalCount());
    LOG.debug("after merge");
    LOG.debug(this);
    this.collapseStepsAfterMerge(mainState);
    LOG.debug("after collapse");
    LOG.debug(this);
  }

  /**
   * Simplify by merging states. Condition to merge states is tested in provided mergedConditionTester
   *
   * @param mergeCondidionTester
   * @throws InterruptedException
   */
  public void simplify(MergeCondidionTester<T> mergeCondidionTester) throws InterruptedException {
    boolean search= true;
    while (search) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      search= false;
      List<List<Pair<State<T>, State<T>>>> mergableStates=null;
      boolean found= false;
      for (State<T> mainState : this.delta.keySet()) {
        for (State<T> mergedState : this.delta.keySet()) {
          mergableStates= mergeCondidionTester.getMergableStates(mainState, mergedState, this );
          if (!mergableStates.isEmpty()) {
            LOG.debug("Equivalent states: " + mainState.toString() + " " + mergedState.toString() + "\n");
            found= true;
            break;
          }
        }
        if (found) {
          break; // get out of searching when found already
        }
      }
      if (found) {
        Map<State<T>, State<T>> mergedOutStates= new HashMap<State<T>, State<T>>();
        for (Pair<State<T>, State<T>> mergePair : mergableStates.get(0)) {
          if (mergedOutStates.containsKey(mergePair.getFirst())) {
            LOG.debug("State " + mergePair.getFirst() +
                    " was merged out previously to " + mergedOutStates.get(mergePair.getFirst()) +
                    "  Merging states: " + mergePair.getFirst() + " " + mergePair.getSecond() + "\n");
            this.mergeStates(mergedOutStates.get(mergePair.getFirst()), mergePair.getSecond());
            mergedOutStates.put(mergePair.getSecond(),  mergedOutStates.get(mergePair.getFirst()));
          } else {
            LOG.debug("Merging states: " + mergePair.getFirst() + " " + mergePair.getSecond() + "\n");
            this.mergeStates(mergePair.getFirst(), mergePair.getSecond());
            mergedOutStates.put(mergePair.getSecond(), mergePair.getFirst());
          }
        }
        search= true;
      }
    }
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Automaton\n");
    for (State<T> state: this.delta.keySet()) {
      sb.append(state);
      sb.append("outSteps:\n");
      for (Step<T> step : this.delta.get(state)) {
        sb.append(step);
      }
    }
    sb.append("reversed:\n");
    for (State<T> state: this.reverseDelta.keySet()) {
      sb.append(state);
      sb.append("inSteps:\n");
      for (Step<T> step : this.reverseDelta.get(state)) {
        sb.append(step);
      }
    }
    return sb.toString();
  }

  /**
   * @return the initialState
   */
  public State<T> getInitialState() {
    return initialState;
  }

  /**
   * @return the delta
   */
  public Map<State<T>, Set<Step<T>>> getDelta() {
    return Collections.unmodifiableMap(delta);
  }

  /**
   * @return the reverseDelta
   */
  public Map<State<T>, Set<Step<T>>> getReverseDelta() {
    return Collections.unmodifiableMap(reverseDelta);
  }

  /**
   * @return the newStateName
   */
  protected Integer getNewStateName() {
    return newStateName;
  }
}