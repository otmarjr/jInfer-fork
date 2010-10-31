/*
 *  Copyright (C) 2010 rio
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

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.GraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author rio
 * TODO rio comment
 */
public abstract class AutomatonVisualizer<T> {

  public abstract BasicVisualizationServer<State<T>, Step<T>> getBasicVisualizationServer();

  protected static <T> Graph<State<T>, Step<T>> createGraph(final Automaton<T> automaton) {
    final DirectedSparseMultigraph<State<T>, Step<T>> graph = new DirectedSparseMultigraph<State<T>, Step<T>>();
    final Map<State<T>, Set<Step<T>>> automatonDelta = automaton.getDelta();

    // Get vertices = states of automaton
    for (final Entry<State<T>, Set<Step<T>>> entry : automatonDelta.entrySet()) {
      graph.addVertex(entry.getKey());
    }

    // Get edges of automaton
    for (Entry<State<T>, Set<Step<T>>> entry : automatonDelta.entrySet()) {
      for (Step<T> step : entry.getValue()) {
        graph.addEdge(step, step.getSource(), step.getDestination());
      }
    }

    return graph;
  }

  protected static List<GraphMousePlugin> getDefaultGraphMousePlugins() {
    final LinkedList<GraphMousePlugin> list = new LinkedList<GraphMousePlugin>();
    list.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0));
    list.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON1_MASK | MouseEvent.CTRL_MASK));
    return list;
  }
}
