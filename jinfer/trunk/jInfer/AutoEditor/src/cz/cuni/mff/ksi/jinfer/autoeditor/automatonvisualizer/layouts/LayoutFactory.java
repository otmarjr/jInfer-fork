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

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.TransformerUtils;
import org.openide.util.Exceptions;

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
    return cz.cuni.mff.ksi.jinfer.autoeditor.vyhnanovska.LayoutFactory.createLayout(automaton, createGraph(automaton));
  }

  /**
   * Creates {@link Layout} using Graphviz tool. Graphviz must be installed.
   *
   * TODO rio understand and comment
   *
   * @param <T> Type parameter of specified automaton.
   * @param automaton Automaton to create layout from.
   * @param edgeLabelTransformer TODO rio comment
   * @return Graphviz layout.
   */
  public static <T> Layout<State<T>, Step<T>> createGraphvizLayout(final Automaton<T> automaton, final Transformer<Step<T>, String> edgeLabelTransformer) {
    final Map<State<T>, Point2D> positions= new HashMap<State<T>, Point2D>();

    ProcessBuilder p = new ProcessBuilder(Arrays.asList(
            "/usr/bin/dot",
            "-Tplain"));
    try {
      Process k = p.start();
      k.getOutputStream().write(
              (new AutomatonToDot<T>()).convertToDot(automaton, edgeLabelTransformer).getBytes()
              );
      k.getOutputStream().flush();
      BufferedReader b = new BufferedReader(new InputStreamReader( k.getInputStream() ) );
      k.getOutputStream().close();

      Scanner s = new Scanner(b);
      s.next();
      s.next();
      double width= s.nextDouble();
      double height= s.nextDouble();
      double windowW= 500;
      double windowH= 300;

      while (s.hasNext()) {
        if (s.next().equals("node")) {
          int nodeName = s.nextInt();
          double x= s.nextDouble();
          double y= s.nextDouble();
          for (State<T> state : automaton.getDelta().keySet()) {
            if (state.getName() == nodeName) {
              positions.put(state, new Point(
                      (int) (windowW * x / width),
                      (int) (windowH * y / height)
                      ));
              break;
            }
          }
        }
      }
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
    Transformer<State<T>, Point2D> trans= TransformerUtils.mapTransformer(positions);

    return new StaticLayout<State<T>, Step<T>>(createGraph(automaton), trans);
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
