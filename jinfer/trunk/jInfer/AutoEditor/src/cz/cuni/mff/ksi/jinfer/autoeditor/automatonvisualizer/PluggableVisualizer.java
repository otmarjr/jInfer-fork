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

import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.AbstractLayout;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.GraphMousePlugin;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.util.List;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author rio
 * TODO rio comment
 */
public class PluggableVisualizer<T> extends AbstractVisualizer<T> {

  final VisualizationViewer<State<T>, Step<T>> vv;
  final PluggableGraphMouse pluggableGraphMouse;

  public PluggableVisualizer(final Layout<State<T>, Step<T>> layout) {
    this(layout, new ToStringLabeller<State<T>>(), new ToStringLabeller<Step<T>>());
  }

  public PluggableVisualizer(final Layout<State<T>, Step<T>> layout, final List<GraphMousePlugin> graphMousePlugins) {
    this(layout, new ToStringLabeller<State<T>>(), new ToStringLabeller<Step<T>>(), graphMousePlugins);
  }

  public PluggableVisualizer(final Layout<State<T>, Step<T>> layout, final Transformer<State<T>, String> vertexLabelTransformer, final Transformer<Step<T>, String> edgeLabelTransformer) {
    this(layout, vertexLabelTransformer, edgeLabelTransformer, null);
  }

  public PluggableVisualizer(final Layout<State<T>, Step<T>> layout, final Transformer<State<T>, String> vertexLabelTransformer, final Transformer<Step<T>, String> edgeLabelTransformer, final List<GraphMousePlugin> graphMousePlugins) {
    vv = new VisualizationViewer<State<T>, Step<T>>(layout);
    vv.getRenderContext().setVertexLabelTransformer(vertexLabelTransformer);
    vv.getRenderContext().setEdgeLabelTransformer(edgeLabelTransformer);

    pluggableGraphMouse = new PluggableGraphMouse();
    if (graphMousePlugins != null) {
      for (final GraphMousePlugin graphMousePlugin : graphMousePlugins) {
        pluggableGraphMouse.add(graphMousePlugin);
      }
    }
  }

  public PluggableVisualizer(final AbstractLayout<T> layoutHolder) {
    this(layoutHolder.getLayout(), layoutHolder.getVertexLabelTransformer(), layoutHolder.getEdgeLabelTransformer());
  }

  public PluggableVisualizer(final AbstractLayout<T> layoutHolder, final List<GraphMousePlugin> graphMousePlugins) {
    this(layoutHolder.getLayout(), layoutHolder.getVertexLabelTransformer(), layoutHolder.getEdgeLabelTransformer(), graphMousePlugins);
  }

  public void addGraphMousePlugin(final GraphMousePlugin graphMousePlugin) {
    pluggableGraphMouse.add(graphMousePlugin);
  }

  @Override
  public BasicVisualizationServer<State<T>, Step<T>> getBasicVisualizationServer() {
    vv.setGraphMouse(pluggableGraphMouse);
    return vv;
  }

  
}
