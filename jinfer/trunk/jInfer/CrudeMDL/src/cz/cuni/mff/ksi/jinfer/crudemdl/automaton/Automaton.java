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

package cz.cuni.mff.ksi.jinfer.crudemdl.automaton;

import com.sun.org.apache.xml.internal.serializer.ToTextStream;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import java.util.AbstractList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.log4j.Logger;

/**
 * TODO anti Comment!
 * @author anti
 */
public class Automaton<T> {
  private static final Logger LOG = Logger.getLogger(Automaton.class);
  // TODO anti Comment the fields
  protected State<T> initialState;
  protected final Map<State<T>, Set<Step<T>>> delta;
  protected final Map<State<T>, Set<Step<T>>> reverseDelta;
  protected int newStateName;

  public Automaton() {
    this.newStateName= 1;
    this.delta= new TreeMap<State<T>, Set<Step<T>>>();
    this.reverseDelta= new TreeMap<State<T>, Set<Step<T>>>();
  }

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

  private Step<T> getInStepOnSymbol(final State<T> state, final T symbol) {
    final Set<Step<T>> steps= this.reverseDelta.get(state);
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
    for (Step<T> inStep : this.reverseDelta.get(mainState)) {
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
    for (Step<T> outStep : this.delta.get(mainState)) {
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

  public void mergeStates(final State<T> mainState, final State<T> mergedState) {
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
    LOG.error("after merge");
    LOG.error(this);
    this.collapseStepsAfterMerge(mainState);
    LOG.error("after collapse");
    LOG.error(this);
  }

  private List<KHContext<T>> find21Contexts(final State<T> state) {
    final List<KHContext<T>> result= new LinkedList<KHContext<T>>();

    for (Step<T> inStep : this.reverseDelta.get(state)) {
      for (Step<T> secondInStep : this.reverseDelta.get(inStep.getSource())) {
        final KHContext<T> context= new KHContext<T>(2, 1);
        context.addStateLast(secondInStep.getSource());
        context.addStepLast(secondInStep);
        context.addStateLast(inStep.getSource());
        context.addStepLast(inStep);
        context.addStateLast(state);
        result.add(context);
      }
    }
    return result;
  }

  private List<Pair<State<T>, State<T>>> getEquivalentKHContexts(final List<KHContext<T>> kHContextsA, final List<KHContext<T>> kHContextsB) {
    List<Pair<State<T>, State<T>>> result= new LinkedList<Pair<State<T>, State<T>>>();
    for (KHContext<T> contextA : kHContextsA) {
      for (KHContext<T> contextB : kHContextsB) {
        result= contextA.getMergeableStates(contextB);
        if (!result.isEmpty()) {
          return result;
        }
      }
    }
    return result;
  }

  public void make21context() {
    boolean searchAgain= true;
    while (searchAgain) {
      boolean found= false;
      final Iterator<State<T>> statesIterator= this.delta.keySet().iterator();
      List<Pair<KHContext<T>, KHContext<T>>> equivalentContexts= null;
      List<Pair<State<T>, State<T>>> mergableStates=null;
      while ((!found)&&(statesIterator.hasNext())) {
        final State<T> toTestState= statesIterator.next();
        final List<KHContext<T>> kHContexts= this.find21Contexts(toTestState);
        if (kHContexts.isEmpty()) {
          continue;
        }
        
        final Iterator<State<T>> anotherIterator= this.delta.keySet().iterator();
        while ((!found)&&(anotherIterator.hasNext())) {
          final State<T> anotherState= anotherIterator.next();
          if (anotherState.equals(toTestState)) {
            continue;
          }
          final List<KHContext<T>> anotherKHContexts= this.find21Contexts(anotherState);
          if (anotherKHContexts.isEmpty()) {
            continue;
          }

            mergableStates= this.getEquivalentKHContexts(kHContexts, anotherKHContexts);
            if (!mergableStates.isEmpty()) {
              LOG.error("Equivalent states: " + toTestState.getName() + " " + anotherState.getName() + "\n");
              found= true;
            }
        }
      }
      if (found) {
        for (Pair<State<T>, State<T>> mergePair : mergableStates) {
          LOG.error("Merging states: " + mergePair.getFirst() + " " + mergePair.getSecond() + "\n");
          this.mergeStates(mergePair.getFirst(), mergePair.getSecond());
        }
        searchAgain= true;
      } else {
        searchAgain= false;
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
  protected State<T> getInitialState() {
    return initialState;
  }

  /**
   * @return the delta
   */
  protected Map<State<T>, Set<Step<T>>> getDelta() {
    return Collections.unmodifiableMap(delta);
  }

  /**
   * @return the reverseDelta
   */
  protected Map<State<T>, Set<Step<T>>> getReverseDelta() {
    return Collections.unmodifiableMap(reverseDelta);
  }

  /**
   * @return the newStateName
   */
  protected Integer getNewStateName() {
    return newStateName;
  }
}