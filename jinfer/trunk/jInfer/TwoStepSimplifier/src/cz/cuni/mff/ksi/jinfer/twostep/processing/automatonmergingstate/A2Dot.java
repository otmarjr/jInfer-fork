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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate;

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class A2Dot {
  public static <T> String convertToDot(final Automaton<T> automaton, final  SymbolToString<T> s2s) {
    final StringBuilder sb = new StringBuilder();
    sb.append("digraph finite_state_machine {\n");
    sb.append("node [shape = circle, fontname = \"SFTT1000\", fontsize = 10, width = 0.5, fixedsize = true];\n");
    sb.append("edge [fontname = \"SFTT1000\", fontsize = 10];\n");
    sb.append("graph [fontname = \"SFTT1000\", fontsize = 10];\n");
    sb.append("rankdir = LR;\n");
    final Deque<State<T>> queue = new ArrayDeque<State<T>>();
    queue.addAll(automaton.getDelta().keySet());
    
    while (!queue.isEmpty()) {
      final State<T> actual = queue.removeFirst();
      if (actual.equals(automaton.getInitialState())) {
        sb.append("0 [style = invis];\n");
        sb.append("\t 0 -> " + String.valueOf(actual.getName()) + ";\n");
      }
      sb.append(actual.getName() + " [label = \"" + actual.getName() + "|" + actual.getFinalCount() + "\"];\n");
      for (Step<T> step : automaton.getDelta().get(actual)) {
        sb.append("\t");
        sb.append(step.getSource().getName());
        sb.append(" -> ");
        sb.append(step.getDestination().getName());
        sb.append(" [label = \"" + s2s.toString(step.getAcceptSymbol()) + "|" + String.valueOf(step.getUseCount()) + "\"];\n");
      }
    }
    sb.append("}\n");
    return sb.toString();
  }
}
