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

package cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer;

import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.SymbolToString;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class AutomatonToDot<T> {
  public String convertToDot(Automaton<T> automaton, SymbolToString<T> symbolToString) {
    StringBuilder sb = new StringBuilder();
    sb.append("digraph finite_state_machine {\n"
            + "\trankdir=LR;\n"
            + "\tnode [shape = circle];\n");
    List<State<T>> finiteStates= new LinkedList<State<T>>();
    Deque<State<T>> queue = new ArrayDeque<State<T>>();
    Set<State<T>> visited= new HashSet<State<T>>();
    queue.addLast(automaton.getInitialState());
    visited.add(automaton.getInitialState());
    while (!queue.isEmpty()) {
      State<T> actual = queue.removeFirst();
      if (actual.getFinalCount() > 0) {
        finiteStates.add(actual);
      }
      for (Step<T> step : automaton.getDelta().get(actual)) {
        sb.append("\t");
        sb.append(step.getSource().getName());
        sb.append(" -> ");
        sb.append(step.getDestination().getName());
        sb.append(" [ label = \"");
        sb.append(symbolToString.toString(step.getAcceptSymbol()));
        sb.append("|");
        sb.append(step.getUseCount());
        sb.append("\" ];\n");
        if (!visited.contains(step.getDestination())) {
          queue.addLast(step.getDestination());
          visited.add(step.getDestination());
        }
      }
    }
    sb.append("\n}");
    return sb.toString();
  }
}
