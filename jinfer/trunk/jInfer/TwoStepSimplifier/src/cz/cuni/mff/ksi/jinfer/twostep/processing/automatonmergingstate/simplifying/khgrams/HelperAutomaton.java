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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.khgrams;

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author anti
 */
class HelperAutomaton extends Automaton<String> {

  protected int k;
  protected int h;
  protected Map<String, List<Deque<State<String>>>> contextMap;

  private void init(int k, int h) {
    this.k = k;
    this.h = h;
    this.contextMap = new HashMap<String, List<Deque<State<String>>>>();
  }

  public HelperAutomaton(int k, int h) {
    init(k, h);
  }

  public HelperAutomaton(boolean createInitialState, int k, int h) {
    super(createInitialState);
    init(k, h);
  }

  public HelperAutomaton(Automaton<String> anotherAutomaton, int k, int h) {
    super(anotherAutomaton);
    init(k, h);
  }

  public void kcontextMe() throws InterruptedException {
    List<State<String>> finalStates = new LinkedList<State<String>>();
    for (State<String> state : reverseDelta.keySet()) {
      if (state.getFinalCount() > 0) {
        if (Thread.interrupted()) {
          throw new InterruptedException();
        }
        finalStates.add(state);
      }
    }

    for (State<String> fS : finalStates) {
      State<String> currentState = fS;
      String currentContext = "";
      Deque<State<String>> currentStates = new LinkedList<State<String>>();
      while (!currentState.equals(this.initialState)) {
        if (Thread.interrupted()) {
          throw new InterruptedException();
        }
        Step<String> inStep = reverseDelta.get(currentState).iterator().next();
        currentContext = inStep.getAcceptSymbol() + currentContext;
        currentStates.addFirst(currentState);
        if (currentContext.length() > k) {
          currentStates.removeLast();
          currentContext = currentContext.substring(0, currentContext.length() - 1);
        }
        if (currentContext.length() == k) {
          if (!contextMap.containsKey(currentContext)) {
            contextMap.put(currentContext, new LinkedList<Deque<State<String>>>());
          }
          Deque<State<String>> copy = new LinkedList<State<String>>();
          copy.addLast(inStep.getSource());
          for (State<String> x : currentStates) {
            copy.addLast(x);
            if (Thread.interrupted()) {
              throw new InterruptedException();
            }
          }
          contextMap.get(currentContext).add(copy);
        }
        currentState = inStep.getSource();
      }
    }

    for (String key : contextMap.keySet()) {
      if (!contextMap.get(key).isEmpty()) {
        Deque<State<String>> first = contextMap.get(key).get(0);
        contextMap.get(key).remove(0);
        for (Deque<State<String>> merg : contextMap.get(key)) {
          Iterator<State<String>> mergIt = merg.iterator();
          Iterator<State<String>> firstIt = first.iterator();
          int i = 0;
          while (firstIt.hasNext()) {
            if (Thread.interrupted()) {
              throw new InterruptedException();
            }
            if (i >= this.h) {
              mergeStates(firstIt.next(), mergIt.next());
            } else {
              firstIt.next();
              mergIt.next();
            }
            i++;
          }
        }
      }
    }
  }
}
