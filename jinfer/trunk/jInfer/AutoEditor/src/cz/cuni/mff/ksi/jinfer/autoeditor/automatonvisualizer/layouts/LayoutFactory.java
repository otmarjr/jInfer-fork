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
package cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts;

import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.LayoutF;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.collections15.Transformer;

/**
 * Factory of custom JUNG {@link Layout}s.
 *
 * @author rio
 */
public final class LayoutFactory {

  /**
   * Creates {@link Layout} written by Julia Vyhnanovska as a part of her master thesis.
   *
   * @param <T> Type parameter of specified automaton.
   * @param automaton Automaton to create layout from.
   * @return Grid layout.
   */
  public static <T> Layout<State<T>, Step<T>> createVyhnanovskaGridLayout(final Automaton<T> automaton) {
    return (new cz.cuni.mff.ksi.jinfer.autoeditor.vyhnanovska.LayoutFactory()).createLayout(automaton, createGraph(automaton), null);
  }

  /**
   * Creates {@link Layout} using Graphviz tool. Graphviz must be installed.
   *
   * TODO rio understand and comment
   *
   * @param <T> Type parameter of specified automaton.
   * @param automaton Automaton to create layout from.
   * @param edgeLabelTransformer Transformation of edge to string.
   * @return Graphviz layout.
   */
  public static <T> Layout<State<T>, Step<T>> createGraphvizLayout(final Automaton<T> automaton, final Transformer<Step<T>, String> edgeLabelTransformer) {
    return (new cz.cuni.mff.ksi.jinfer.autoeditor.graphviz.GraphvizLayoutFactory()).createLayout(automaton, createGraph(automaton), edgeLabelTransformer);
  }

  /**
   * Creates {@link Layout} which user selected in preferences.
   *
   * TODO rio understand and comment
   *
   * @param <T> Type parameter of specified automaton.
   * @param automaton Automaton to create layout from.
   * @param edgeLabelTransformer TODO rio comment
   * @return
   */
  public static <T> Layout<State<T>, Step<T>> createUserLayout(final Automaton<T> automaton, final Transformer<Step<T>, String> edgeLabelTransformer) {
    Properties p = RunningProject.getActiveProjectProps("GraphRenderer");

    LayoutF f= ModuleSelectionHelper.lookupImpl(LayoutF.class, p.getProperty("user-layout"));
    return f.createLayout(automaton, createGraph(automaton), edgeLabelTransformer);
  }

  /**
   * Creates JUNG representation of {@link Automaton}.
   *
   * @param <T> Type parameter of specified automaton.
   * @param automaton Automaton instance.
   * @return Automaton as JUNG graph.
   */
  private static <T> Graph<State<T>, Step<T>> createGraph(final Automaton<T> automaton) {
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
}
