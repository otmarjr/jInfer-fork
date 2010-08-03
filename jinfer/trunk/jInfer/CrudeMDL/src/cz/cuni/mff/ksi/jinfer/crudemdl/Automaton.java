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

package cz.cuni.mff.ksi.jinfer.crudemdl;

import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author anti
 */
public class Automaton<T> {
  private static final Logger LOG = Logger.getLogger(Automaton.class);
  private State<T> initialState;
  private List<State<T>> states;
  private Integer maxStateName;

  Automaton() {
    this.maxStateName= 1;
    this.initialState= new State<T>(0, 1, this);
    this.states= new LinkedList<State<T>>();
    this.states.add(this.initialState);
  }

  /**
   * @return the initialState
   */
  public State<T> getInitialState() {
    return initialState;
  }

  /**
   * @param initialState the initialState to set
   */
  public void setInitialState(final State<T> initialState) {
    this.initialState = initialState;
  }

  public void addNewStateCreated(final State<T> newState) {
    // TODO assert here about maxstate
    this.maxStateName++;
    this.getStates().add(newState);
  }

  /**
   * @return the states
   */
  public List<State<T>> getStates() {
    return this.states;
  }

  /**
   * @param states the states to set
   */
  public void setStates(final List<State<T>> states) {
    this.states = states;
  }

  /**
   * @return the maxStateName
   */
  public Integer getMaxStateName() {
    return this.maxStateName;
  }

  /**
   * @param maxStateName the maxStateName to set
   */
  public void setMaxStateName(final Integer maxStateName) {
    this.maxStateName = maxStateName;
  }

  public List<State<T>> mergeStates(final State<T> mainState, final State<T> mergedState) {
    return this.mergeStates(new Pair<State<T>, State<T>>(mainState, mergedState));
  }

  public List<State<T>> mergeStates(Pair<State<T>, State<T>> toMergePair) {
    List<State<T>> removedStates= new LinkedList<State<T>>();
    if (!this.states.contains(toMergePair.getFirst())) {
      LOG.fatal("neobsahuje uz mainstate:" + toMergePair.getFirst());
      return removedStates;
    }
    if (!this.states.contains(toMergePair.getSecond())) {
      LOG.fatal("neobsahuje uz mergedstate:" + toMergePair.getSecond());
      return removedStates;
    }
    Deque<Pair<State<T>, State<T>>> toMergeStates= new LinkedList<Pair<State<T>, State<T>>>();
    toMergeStates.add(toMergePair);

    while (!toMergeStates.isEmpty()) {
      Pair<State<T>, State<T>> mergePair= toMergeStates.removeFirst();
      State<T> mainState= mergePair.getFirst();
      State<T> mergedState= mergePair.getSecond();

      /* insteps */
      List<Step<T>> mergedStateInSteps= mergedState.getInSteps();
      for (Step<T> mergedStateInStep : mergedStateInSteps) {
        mergedStateInStep.setDestination(mainState);
      }

      /* outsteps */
      Map<T, Step<T>> mergedStateOutSteps= mergedState.getOutSteps();
      for (T symbol : mergedStateOutSteps.keySet()) {
        Step<T> mergedStateOutStep= mergedStateOutSteps.get(symbol);

        if (mainState.getOutSteps().containsKey(symbol)) {
          State<T> mainStateDestination= mainState.getOutSteps().get(symbol).getDestination();
          State<T> mergedStateDestination= mergedStateOutStep.getDestination();
          if (!mainStateDestination.equals(mergedStateDestination)) {
            toMergeStates.addLast(
                    new Pair<State<T>, State<T>>(mainStateDestination, mergedStateDestination)
                    );
          }
          mainState.getOutSteps().get(symbol).incUseCount(mergedStateOutStep.getUseCount());
        } else {
          mergedStateOutStep.setSource(mainState);
          mainState.addOutStep(mergedStateOutStep);
        }
      }

      /* finalCount */
      mainState.incFinalCount(mergedState.getFinalCount());

      removedStates.add(mergedState);
      this.states.remove(mergedState);
      LOG.error("removed:\n");
      LOG.error(mergedState);
      LOG.error("after:\n");
      LOG.error(this);
    }
    return removedStates;
  // TODO check loop
  }

  private Pair<State<T>, State<T>> hasIntersection(List<Pair<Step<T>, Step<T>>> contextsA, List<Pair<Step<T>, Step<T>>> contextsB) {
    for (Pair<Step<T>, Step<T>> contextA : contextsA) {
      for (Pair<Step<T>, Step<T>> contextB : contextsB) {
        if (
                contextA.getFirst().getAcceptSymbol().equals(contextB.getFirst().getAcceptSymbol())&&
                contextA.getSecond().getAcceptSymbol().equals(contextB.getSecond().getAcceptSymbol())
                ) {
          return new Pair<State<T>, State<T>>(contextA.getFirst().getDestination(), contextB.getFirst().getDestination());
        }
      }
    }
    return null;
  }

  public void make21context() {
    Deque<State<T>> toTestStates = new LinkedList<State<T>>();
    toTestStates.addAll(this.getStates());

    while (!toTestStates.isEmpty()) {
      State<T> toTestState= toTestStates.removeFirst();
      List<Pair<Step<T>, Step<T>>> testStateKHcontexts= toTestState.find21contexts();
      if (testStateKHcontexts.isEmpty()) {
        continue;
      }

      Deque<State<T>> anotherStates= new LinkedList<State<T>>();
      anotherStates.addAll(this.getStates());
      while (!anotherStates.isEmpty()) {
        State<T> anotherState= anotherStates.removeFirst();
 /*       if (anotherState.equals(toTestState)) {
          continue;
        }
  */      List<Pair<Step<T>, Step<T>>> anotherStateKHcontexts= anotherState.find21contexts();
        if (anotherStateKHcontexts.isEmpty()) {
          continue;
        }

        Pair<State<T>, State<T>> additionalMerge= this.hasIntersection(testStateKHcontexts, anotherStateKHcontexts);
        List<State<T>> removedStates= new LinkedList<State<T>>();
        if (additionalMerge != null)
        {
          if (additionalMerge.getFirst().equals(additionalMerge.getSecond())) {
            continue;
          }
          LOG.error("Equals:" + toTestState.getName() + " " + anotherState.getName());
          LOG.error("additionaly:" + additionalMerge.getFirst().getName() + " " + additionalMerge.getSecond().getName());
          removedStates.addAll( this.mergeStates(additionalMerge.getFirst(), additionalMerge.getSecond()) );
// TODO cez removed spravit
          if (this.states.contains(toTestState)&&this.states.contains(anotherState)) {
            removedStates.addAll( this.mergeStates(toTestState, anotherState) );
          }
          anotherStates.removeAll(removedStates);
          toTestStates.removeAll(removedStates);
          if (removedStates.contains(toTestState)) {
            break;
          }
          testStateKHcontexts= toTestState.find21contexts();
        }
      }
    }
  }


  @Override
  public String toString() {
//    return super.toString();
    StringBuilder sb = new StringBuilder("Automaton\n");
    for (State<T> state: this.getStates()) {
    sb.append(state);
    }
    return sb.toString();
  }

}
