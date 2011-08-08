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

package cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.graphviz;

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import java.util.ArrayDeque;
import java.util.Deque;
import org.apache.commons.collections15.Transformer;

/**
 * Converts {@link Automaton} to dot language string representation used as input to graphviz.
 *
 * @author anti
 */
public final class AutomatonToDot {

  private AutomatonToDot() {};

  public static <T> String convertToDot(final Automaton<T> automaton, final Transformer<Step<T>, String> edgeLabelTransformer) {
    final StringBuilder sb = new StringBuilder();
    sb.append("digraph finite_state_machine {\n");
    sb.append("\trankdir=LR;\n");
    sb.append("\tnodesep=\"50\";");
    sb.append("\tsplines=\"line\";");
    sb.append("\tranksep=\"100\";");
    sb.append("\tedge [label = \"\", dir = none, arrowhead=none, arrowtail=none];");
    sb.append("\tnode [shape = none, label = \"\", width = 0, height = 0];\n");
    final Deque<State<T>> queue = new ArrayDeque<State<T>>();
    queue.addAll(automaton.getDelta().keySet());
    
    while (!queue.isEmpty()) {
      final State<T> actual = queue.removeFirst();
      sb.append(actual.getName());
      sb.append(";\n");
      for (Step<T> step : automaton.getDelta().get(actual)) {
        sb.append("\t");
        sb.append(step.getSource().getName());
        sb.append(" -> ");
        sb.append(step.getDestination().getName());
        sb.append("];\n");
      }
    }
    sb.append("\n}");
    return sb.toString();
  }
}
