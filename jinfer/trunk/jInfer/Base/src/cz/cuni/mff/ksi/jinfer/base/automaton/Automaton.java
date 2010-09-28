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

package cz.cuni.mff.ksi.jinfer.base.automaton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * Class representing deterministic finite automaton.
 * Can simplify itself when given mergingCondition
 *
 * Automaton can be non-deterministic for algorithmic reasons.
 * Of course, there is no constraint that each state has to have transition on
 * each symbol. But what more, there can be multiple transitions from one step
 * on same symbol, pointing to different states (real non-determinism) or to same
 * state (just non-canonical form).
 *
 * When merging occurs, this deviations can appear. Non-canonical form, when
 * there are two-or-more steps on same symbol between two states, is solved
 * (and collapsed to only one step) automatically by merging procedure.
 * Non-deterministic form is not solved by automaton itself. The merging algorithm
 * has to deal with this opportunity is it gives bad order of merging states.
 * But it can let it non-deterministic by design.
 * 
 * @author anti
 */
public class Automaton<T> {
  private static final Logger LOG = Logger.getLogger(Automaton.class);
  /**
   * Initial state of automaton, entry point. State in which automaton is
   * when starting reading input. Has to be in delta function keySet.
   *
   * Has no incoming steps.
   */
  protected State<T> initialState;
  /**
   * Transition function of automaton. Maps states to their outgoing steps. KeySet
   * is a set of all states of automaton.
   *
   * State is removed by delta.remove(state) and reverseDelta.remove(state)
   */
  protected final Map<State<T>, Set<Step<T>>> delta;
  /**
   * Transition function of automaton, mathematically the same as delta. For
   * programmatic reasons we have the reverse map, state mapping to ist incoming
   * steps.
   * Loops are therefore findable by using delta and reverseDelta so be careful
   * when counting loops - may count twice.
   */
  protected final Map<State<T>, Set<Step<T>>> reverseDelta;
  /**
   * New state name is an integer that is assigned to any new state created by
   * createNewState. It is always incremented in createNewState so no two states
   * share same name. Numbers freed by state removing are not used again, sequence
   * only grows.
   */
  protected int newStateName;
  /**
   * TODO anti Comment
   */
  protected final Map<State<T>, State<T>> mergedStates;
  /**
   * TODO anti Comment
   */
  protected final Map<State<T>, Set<State<T>>> reverseMergedStates;

  /**
   * Constructor which doesn't create initialState
   */
  public Automaton() {
    this.newStateName= 1;
    this.delta= new HashMap<State<T>, Set<Step<T>>>();
    this.reverseDelta= new HashMap<State<T>, Set<Step<T>>>();
    this.mergedStates= new HashMap<State<T>, State<T>>();
    this.reverseMergedStates= new HashMap<State<T>, Set<State<T>>>();
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

  public Automaton(final Automaton<T> anotherAutomaton) {
    this(false);
    
    AutomatonCloner<T, T> cloner= new AutomatonClonerImpl<T, T>();

    cloner.convertAutomaton(anotherAutomaton, this,
            new
              AutomatonSymbolConverter<T, T>() {
                @Override
                public T convertSymbol(T symbol) {
                  return symbol;
                }
              }
    );
  }

  /**
   * Creates new state and return it. This is preferred way to extend automaton
   * states. It initializes delta map, and revesre delta map with empty hashsets
   * of steps, increments newstatename.
   *
   * Process of extending automaton is therefore:
   * 1. create state using this function
   * 2. create steps giving them proper sources,destinations
   * 3. put step instances correctly in delta map and reverseDelta map.
   *
   * @return
   */
  protected final State<T> createNewState() {
    final State<T> newState= new State<T>(0, this.newStateName);
    this.newStateName++;
    this.delta.put(newState, new HashSet<Step<T>>());
    this.reverseDelta.put(newState, new HashSet<Step<T>>());
    this.reverseMergedStates.put(newState, new HashSet<State<T>>());
    return newState;
  }

  /**
   * Returns first step from state, which accepts given symbol. If there are
   * multiple such steps, return only one found.
   *
   * @param state
   * @param symbol
   * @return
   */
  protected Step<T> getOutStepOnSymbol(final State<T> state, final T symbol) {
    final Set<Step<T>> steps= this.delta.get(state);
    for (Step<T> step : steps) {
      if (step.getAcceptSymbol().equals(symbol)) {
        return step;
      }
    }
    return null;
  }

  /**
   * Preferred way to create new steps. Gives 1 as useCount to step.
   *
   * @param onSymbol
   * @param source
   * @param destination
   * @return
   */
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

  private State<T> getRealState(final State<T> state) {
    if (this.delta.containsKey(state)) {
      return state;
    } else {
      /* this is union-find-set with path shortening along the way */
      final State<T> realState= this.getRealState(this.mergedStates.get(state));
      this.mergedStates.put(state, realState);
      this.reverseMergedStates.get(realState).add(state);
      return realState;
    }
  }

  public void mergeStates(final State<T> _mainState, final State<T> _mergedState) {
    final State<T> mainState= this.getRealState(_mainState);
    final State<T> mergedState= this.getRealState(_mergedState);
    LOG.debug("mergeStates: Got to merge states: " + _mainState + " + " + _mergedState + "\n");
    LOG.debug("mergeStates: Real states merging: " +  mainState + " + " +  mergedState + "\n");
    if (mergedState.equals(mainState)) {
      LOG.debug("mergeStates: States equal, doing nothing\n");
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
    this.mergedStates.put(mergedState, mainState);
    this.reverseMergedStates.get(mainState).add(mergedState);
    LOG.debug("after merge");
    LOG.debug(this);
    this.collapseStepsAfterMerge(mainState);
    LOG.debug("after collapse");
    LOG.debug(this);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Automaton\n");
    Comparator<State<T>> stateComparator= new Comparator<State<T>>() {
      @Override
      public int compare(State<T> o1, State<T> o2) {
        return o1.getName() - o2.getName();
      }
    };

    List<State<T>> deltaKeys= new ArrayList<State<T>>();
    deltaKeys.addAll(this.delta.keySet());
    Collections.sort(deltaKeys, stateComparator);
    for (State<T> state: deltaKeys) {
      sb.append(state);
      for (State<T> mergedState : this.reverseMergedStates.get(state)) {
        sb.append(" + ");
        sb.append(mergedState);
      }
      sb.append(" outSteps:\n");
      for (Step<T> step : this.delta.get(state)) {
        sb.append(step);
      }
    }

    List<State<T>> reverseDeltaKeys= new ArrayList<State<T>>();
    reverseDeltaKeys.addAll(this.reverseDelta.keySet());
    Collections.sort(reverseDeltaKeys, stateComparator);
    sb.append("reversed:\n");
    for (State<T> state: reverseDeltaKeys) {
      sb.append(state);
      for (State<T> mergedState: this.reverseMergedStates.get(state)) {
        sb.append(" + ");
        sb.append(mergedState);
      }
      sb.append(" inSteps:\n");
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
   * @return the delta, unmodifiable!
   */
  public Map<State<T>, Set<Step<T>>> getDelta() {
    return Collections.unmodifiableMap(delta);
  }

  /**
   * @return the reverseDelta, unmodifiable!
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

  /**
   * @return the mergedStates
   */
  public Map<State<T>, State<T>> getMergedStates() {
    return Collections.unmodifiableMap(mergedStates);
  }

  /**
   * @return the reverseMergedStates
   */
  public Map<State<T>, Set<State<T>>> getReverseMergedStates() {
    return Collections.unmodifiableMap(reverseMergedStates);
  }
}