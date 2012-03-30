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

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * @author anti
 */
class KHContextHelperAutomaton extends Automaton<String> {

  protected int k;
  protected int h;
  protected Map<String, Set<StateHString>> contextMap;

  public Map<String, Set<StateHString>> getContextMap() {
    return contextMap;
  }

  private void init(int k, int h) {
    this.k = k;
    this.h = h;
    this.contextMap = new HashMap<String, Set<StateHString>>();
  }

  public KHContextHelperAutomaton(int k, int h) {
    init(k, h);
  }

  public KHContextHelperAutomaton(boolean createInitialState, int k, int h) {
    super(createInitialState);
    init(k, h);
  }

  public KHContextHelperAutomaton(Automaton<String> anotherAutomaton, int k, int h) {
    super(anotherAutomaton);
    init(k, h);
  }

  public void kcontextMe() throws InterruptedException {
    Deque<StateString> todo = new LinkedList<StateString>();
    Set<StateString> visited = new HashSet<StateString>();

    for (State<String> state : reverseDelta.keySet()) {
      if (state.getFinalCount() > 0) {
        if (Thread.interrupted()) {
          throw new InterruptedException();
        }
        todo.addLast(new StateString(state, this.k, this.h));
      }
    }

    while (!todo.isEmpty()) {
      StateString actual = todo.removeFirst();
      visited.add(actual);

      State<String> firstState = actual.getFirstState();
      for (Step<String> inStep : reverseDelta.get(firstState)) {
        if (Thread.interrupted()) {
          throw new InterruptedException();
        }

        StateString toAdd = actual.clonePreceed(inStep);
        if (!visited.contains(toAdd)) {
          todo.add(toAdd);
        }
      }

      if (actual.isLenghtK()) {
        if (!contextMap.containsKey(actual.getContext())) {
          contextMap.put(actual.getContext(), new HashSet<StateHString>());
        }
        contextMap.get(actual.getContext()).add(new StateHString(actual));
      }
    }

  }
}
