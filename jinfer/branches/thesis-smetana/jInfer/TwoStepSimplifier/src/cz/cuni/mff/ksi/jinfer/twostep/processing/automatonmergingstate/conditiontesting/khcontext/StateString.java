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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.khcontext;

import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
class StateString {

  private int k;
  private int h;
  private Deque<State<String>> states;
  private Deque<Step<String>> steps;
  private String context;

  public StateString(State<String> firstState, int k, int h) {
    this.context = "";
    this.states = new LinkedList<State<String>>();
    this.states.addFirst(firstState);
    this.steps = new LinkedList<Step<String>>();
    this.k = k;
    this.h = h;
  }

  public StateString(int k, int h, Deque<State<String>> states, Deque<Step<String>> steps, String context) {
    this.k = k;
    this.h = h;
    this.states = states;
    this.steps = steps;
    this.context = context;
  }

  public State<String> getFirstState() {
    return this.states.getFirst();
  }

  public StateString clonePreceed(Step<String> inStep) {
    Deque<State<String>> newStates = new LinkedList<State<String>>();
    Deque<Step<String>> newSteps = new LinkedList<Step<String>>();
    newStates.addAll(this.states);
    newStates.addFirst(inStep.getSource());
    if (newStates.size() > k + 1) {
      newStates.removeLast();
    }
    newSteps.addAll(this.steps);
    newSteps.addFirst(inStep);
    if (newSteps.size() > k) {
      newSteps.removeLast();
    }

    String newContext = inStep.getAcceptSymbol() + this.context;
    if (newContext.length() > k) {
      newContext = newContext.substring(0, k);
    }
    return new StateString(k, h, newStates, newSteps, newContext);
  }

  public boolean isLenghtK() {
    return this.steps.size() == this.k;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final StateString other = (StateString) obj;
    if (this.k != other.getK()) {
      return false;
    }
    if (this.h != other.getH()) {
      return false;
    }
    if (this.states.size() != other.getStates().size()) {
      return false;
    }
    {
      Iterator<State<String>> itThis = this.states.iterator();
      Iterator<State<String>> itOther = other.getStates().iterator();
      while (itThis.hasNext()) {
        State<String> sThis = itThis.next();
        State<String> sOther = itOther.next();
        if (sThis != sOther && (sThis == null || !sThis.equals(sOther))) {
          return false;
        }
      }
    }

    if (this.steps.size() != other.getSteps().size()) {
      return false;
    }
    Iterator<Step<String>> itThis = this.steps.iterator();
    Iterator<Step<String>> itOther = other.getSteps().iterator();
    while (itThis.hasNext()) {
      Step<String> sThis = itThis.next();
      Step<String> sOther = itOther.next();
      if (sThis != sOther && (sThis == null || !sThis.equals(sOther))) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 59 * hash + this.k;
    hash = 59 * hash + this.h;
    hash = 59 * hash + (this.states != null ? this.states.hashCode() : 0);
    hash = 59 * hash + (this.steps != null ? this.steps.hashCode() : 0);
    return hash;
  }

  public int getH() {
    return h;
  }

  public int getK() {
    return k;
  }

  public Deque<State<String>> getStates() {
    return states;
  }

  public Deque<Step<String>> getSteps() {
    return steps;
  }

  public String getContext() {
    return context;
  }
}
