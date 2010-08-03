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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author anti
 */
public class State<T> {
  private Map< T, Step<T>> outSteps;
  private List<Step<T>> inSteps;
  private Integer finalCount;
  private Integer name;
  private Automaton<T> parentAutomaton;

  State(final Integer finalCount, final Integer name, final Automaton<T> parentAutomaton) {
    this.inSteps= new LinkedList<Step<T>>();
    this.outSteps= new HashMap<T, Step<T>>();
    this.finalCount= finalCount;
    this.name= name;
    this.parentAutomaton= parentAutomaton;
  }

  /**
   * @return the outSteps
   */
  public Map<T, Step<T>> getOutSteps() {
    return this.outSteps;
  }

  /**
   * @param outSteps the outSteps to set
   */
  public void setOutSteps(Map<T, Step<T>> outSteps) {
    this.outSteps = outSteps;
  }

  /**
   * @return the inSteps
   */
  public List<Step<T>> getInSteps() {
    return this.inSteps;
  }

  /**
   * @param inSteps the inSteps to set
   */
  public void setInSteps(List<Step<T>> inSteps) {
    this.inSteps = inSteps;
  }

  /**
   * @return the finalCount
   */
  public Integer getFinalCount() {
    return finalCount;
  }

  /**
   * @param finalCount the finalCount to set
   */
  public void setFinalCount(final Integer finalCount) {
    this.finalCount = finalCount;
  }

  public void incFinalCount() {
    this.incFinalCount(1);
  }

  public void incFinalCount(final Integer i) {
    this.setFinalCount(this.getFinalCount() + i);
  }

  public void addInStep(Step<T> inStep) {
    this.inSteps.add(inStep);
  }

  public void addInStepAll(Collection<Step<T>> inSteps) {
    this.inSteps.addAll(inSteps);
  }

  public void addOutStep(Step<T> outStep) {
    this.outSteps.put(outStep.getAcceptSymbol(), outStep);
  }

  public State<T> buildPTAOnSymbol(final T symbol) {
    if (this.outSteps.containsKey(symbol)) {
      final Step<T> outStep= this.outSteps.get(symbol);
      outStep.incUseCount();
      return outStep.getDestination();
    } else {
      State<T> newState= new State<T>(0, this.getParentAutomaton().getMaxStateName() + 1, this.getParentAutomaton());
      this.getParentAutomaton().addNewStateCreated(newState);
      Step<T> newOutStep= new Step<T>(symbol, this, newState, 1);
      this.addOutStep(newOutStep);
      newState.addInStep(newOutStep);
      return newState;
    }
  }

  public List<Pair<Step<T>, Step<T>>> find21contexts() {
    List<Pair<Step<T>, Step<T>>> contexts= new LinkedList<Pair<Step<T>, Step<T>>>();
    for (Step<T> inStep: this.inSteps) {
      for (Step<T> secondInStep: inStep.getSource().getInSteps()) {
        contexts.add(new Pair<Step<T>, Step<T>>(
                secondInStep,
                inStep
                ));
      }
    }
    return contexts;
  }

  @Override
  public String toString() {
  //  return super.toString();
    StringBuilder sb = new StringBuilder("[");
    sb.append(this.getName());
    sb.append("|");
    sb.append(this.finalCount);
    sb.append("] steps:\n");
    for (T symbol : this.outSteps.keySet()) {
      sb.append("on ");
      sb.append(this.outSteps.get(symbol));
      sb.append(" -> ");
      sb.append(this.outSteps.get(symbol).getDestination().getName());
      sb.append("\n");
    }
    sb.append("\n");
    Set<State<T>> outStates= new HashSet<State<T>>();
    for (Step<T> step : this.outSteps.values()) {
      outStates.add(step.getDestination());
    }
    return sb.toString();
  }

  /**
   * @return the name
   */
  public Integer getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(Integer name) {
    this.name = name;
  }

  /**
   * @return the myAutomaton
   */
  public Automaton<T> getParentAutomaton() {
    return parentAutomaton;
  }

  /**
   * @param parentAutomaton the myAutomaton to set
   */
  public void setParentAutomaton(Automaton<T> parentAutomaton) {
    this.parentAutomaton = parentAutomaton;
  }
}
