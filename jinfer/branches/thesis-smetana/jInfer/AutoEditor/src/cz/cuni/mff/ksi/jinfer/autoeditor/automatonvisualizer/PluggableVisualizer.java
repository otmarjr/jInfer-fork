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

import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.GraphMousePlugin;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections15.Transformer;

/**
 * Extension of {@link Visualizer} that provides an easy way of adding JUNG
 * graph mouse plugins.
 *
 * @see GraphMousePlugin
 *
 * @author rio
 */
public class PluggableVisualizer<T> extends Visualizer<T> {

  private static final long serialVersionUID = 353474565;

  private final PluggableGraphMouse pluggableGraphMouse;
  private final Set<GraphMousePlugin> graphMousePlugins;

  /**
   * Constructs instance with specified {@link Layout}, default graph mouse
   * plugins and {@link ToStringLabeller}s as vertex and edge label {@link Transformer}s.
   *
   * @param layout Layout instance.
   */
  public PluggableVisualizer(final Layout<State<T>, Step<T>> layout) {
    super(layout);

    setVertexLabelTransformer(new ToStringLabeller<State<T>>());
    setEdgeLabelTransformer(new ToStringLabeller<Step<T>>());
    
    pluggableGraphMouse = new PluggableGraphMouse();
    graphMousePlugins = new HashSet<GraphMousePlugin>();
    for (final GraphMousePlugin graphMousePlugin : getDefaultGraphMousePlugins()) {
      pluggableGraphMouse.add(graphMousePlugin);
      graphMousePlugins.add(graphMousePlugin);
    }
    setGraphMouse(pluggableGraphMouse);
  }

  /**
   * Adds graph mouse plugin.
   *
   * @param graphMousePlugin
   */
  public void addGraphMousePlugin(final GraphMousePlugin graphMousePlugin) {
    pluggableGraphMouse.add(graphMousePlugin);
    setGraphMouse(pluggableGraphMouse);
  }

  /**
   * Removes graph mouse plugin.
   *
   * @param graphMousePlugin
   */
  public void removeGraphMousePlugin(final GraphMousePlugin graphMousePlugin) {
    graphMousePlugins.remove(graphMousePlugin);
    pluggableGraphMouse.remove(graphMousePlugin);
  }

  /**
   * Retrieves all used graph mouse plugins.
   *
   * @return Active graph mouse plugins.
   */
  public Set<GraphMousePlugin> getGraphMousePlugins() {
    return graphMousePlugins;
  }

  /**
   * Replaces vertex label transformer.
   *
   * @param vertexLabelTransformer
   */
  public void replaceVertexLabelTransformer(final Transformer<State<T>, String> vertexLabelTransformer) {
    setVertexLabelTransformer(vertexLabelTransformer);
  }

  /**
   * Replaces edge label transformer.
   *
   * @param edgeLabelTransformer
   */
  public void replaceEdgeLabelTransformer(final Transformer<Step<T>, String> edgeLabelTransformer) {
    setEdgeLabelTransformer(edgeLabelTransformer);
  }

  private void setVertexLabelTransformer(final Transformer<State<T>, String> vertexLabelTransformer) {
    getRenderContext().setVertexLabelTransformer(vertexLabelTransformer);
  }

  private void setEdgeLabelTransformer(final Transformer<Step<T>, String> edgeLabelTransformer) {
    getRenderContext().setEdgeLabelTransformer(edgeLabelTransformer);
  }

  private static List<GraphMousePlugin> getDefaultGraphMousePlugins() {
    final LinkedList<GraphMousePlugin> list = new LinkedList<GraphMousePlugin>();
    list.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, 1 / 1.1f, 1.1f));
    list.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON3_MASK));
    return list;
  }
}
