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

/**
 * TODO anti Comment!
 *
 * @author anti
 */
class StateHString {

  private int k;
  private int h;
  private Deque<State<String>> states;
  private Deque<Step<String>> steps;
  private String context;

  public StateHString(StateString another) {
    this.k = another.getK();
    this.h = another.getH();
    this.states = another.getStates();
    this.steps = another.getSteps();
    this.context = another.getContext();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final StateHString other = (StateHString) obj;
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
      int i = 0;
      while (itThis.hasNext()) {
        State<String> sThis = itThis.next();
        State<String> sOther = itOther.next();
        if (i >= this.h) {
          if (sThis != sOther && (sThis == null || !sThis.equals(sOther))) {
            return false;
          }
        }
        i++;
      }
    }

    if (this.steps.size() != other.getSteps().size()) {
      return false;
    }
    int i = 0;
    Iterator<Step<String>> itThis = this.steps.iterator();
    Iterator<Step<String>> itOther = other.getSteps().iterator();
    while (itThis.hasNext()) {
      Step<String> sThis = itThis.next();
      Step<String> sOther = itOther.next();
      if (i >= this.h) {
        if (sThis != sOther && (sThis == null || !sThis.equals(sOther))) {
          return false;
        }
      }
      i++;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 59 * hash + this.k;
    hash = 59 * hash + this.h;
    Iterator<State<String>> it = this.states.iterator();
    int i = 0;
    while (it.hasNext()) {
      if (i >= this.h) {
        hash = 59 * hash + it.next().hashCode();
      } else {
        it.next();
      }
    }
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
