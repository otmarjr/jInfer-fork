/*
 * Copyright (C) 2011 anti
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.defective;

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class DefectiveAutomaton<T> extends Automaton<T> {

  private List<List<T>> removedInputStrings;

  public DefectiveAutomaton(boolean createInitialState) {
    super(createInitialState);
    this.removedInputStrings = new LinkedList<List<T>>();
  }

  public DefectiveAutomaton() {
    this.removedInputStrings = new LinkedList<List<T>>();
  }

  public DefectiveAutomaton(Automaton<T> anotherAutomaton) {
    super(anotherAutomaton);
    this.removedInputStrings = new LinkedList<List<T>>();
  }

  public List<List<T>> getRemovedInputStrings() {
    return this.removedInputStrings;
  }

  public void removeStep(Step<T> step) {
    this.delta.get(step.getSource()).remove(step);
    this.reverseDelta.get(step.getDestination()).remove(step);
  }

  private boolean shouldBeRemoved(State<T> state) {
    if (this.delta.get(state).isEmpty() && state.getFinalCount() == 0) {
      return true;
    }
    if (this.reverseDelta.get(state).isEmpty() && !state.equals(this.initialState)) {
      return true;
    }
    return false;
  }

  private void removeState(State<T> state) {
    if (this.reverseMergedStates.get(state) != null) {
      for (State<T> st : this.reverseMergedStates.get(state)) {
        this.nameMap.remove(st.getName());
        this.mergedStates.remove(st);
      }
      this.reverseMergedStates.get(state).clear();
    }
    for (Step<T> step : this.delta.get(state)) {
      this.reverseDelta.get(step.getDestination()).remove(step);
    }
    for (Step<T> step : this.reverseDelta.get(state)) {
      this.delta.get(step.getSource()).remove(step);
    }
    this.delta.remove(state);
    this.reverseDelta.remove(state);
    if (state.equals(this.initialState)) {
      this.initialState = this.createNewState();
      this.initialState.incFinalCount();
    }
  }

  public void minimize() {
    Set<State<T>> toremove = new HashSet<State<T>>();
    Set<Step<T>> toremovestep = new HashSet<Step<T>>();
    for (State<T> state : this.delta.keySet()) {
      for (Step<T> step : this.delta.get(state)) {
        if (step.getUseCount() == 0) {
          toremovestep.add(step);
        }
      }
    }
    for (Step<T> step : toremovestep) {
      removeStep(step);
    }
    for (State<T> state : this.delta.keySet()) {
      if (shouldBeRemoved(state)) {
        toremove.add(state);
      }
    }
    for (State<T> state : toremove) {
      removeState(state);
    }
  }

  public void tryRemoveInputString(List<T> string) {
    State<T> actual = this.initialState;
    for (T symbol : string) {
      Step<T> toTake = this.getOutStepOnSymbol(actual, symbol);
      assert toTake != null;
      assert toTake.getUseCount() > 0;
      toTake.setUseCount(toTake.getUseCount() - 1);
      toTake.removeInputString(string);
      actual = toTake.getDestination();
    }
    assert actual.getFinalCount() > 0;
    actual.setFinalCount(actual.getFinalCount() - 1);
    this.removedInputStrings.add(string);
  }

  public void undoRemoveInputString(List<T> string) {
    State<T> actual = this.initialState;
    for (T symbol : string) {
      Step<T> toTake = this.getOutStepOnSymbol(actual, symbol);
      assert toTake != null;
      assert toTake.getUseCount() >= 0;
      toTake.setUseCount(toTake.getUseCount() + 1);
      toTake.addInputString(string);
      actual = toTake.getDestination();
    }
    assert actual.getFinalCount() >= 0;
    actual.setFinalCount(actual.getFinalCount() + 1);
    this.removedInputStrings.remove(string);
  }
}
