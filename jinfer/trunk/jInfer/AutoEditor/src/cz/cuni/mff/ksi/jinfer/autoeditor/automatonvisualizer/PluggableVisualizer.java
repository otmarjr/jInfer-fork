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
import edu.uci.ics.jung.visualization.control.GraphMousePlugin;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author rio
 * TODO rio comment
 */
public final class PluggableVisualizer<T> extends Visualizer<T> {

  private final PluggableGraphMouse pluggableGraphMouse;

  public PluggableVisualizer(final Layout<State<T>, Step<T>> layout) {
    super(layout);
    setVertexLabelTransformer(new ToStringLabeller<State<T>>());
    setEdgeLabelTransformer(new ToStringLabeller<Step<T>>());
    pluggableGraphMouse = new PluggableGraphMouse();
  }

  public void addGraphMousePlugin(final GraphMousePlugin graphMousePlugin) {
    pluggableGraphMouse.add(graphMousePlugin);
    setGraphMouse(pluggableGraphMouse);
  }

  public void setVertexLabelTransformer(final Transformer<State<T>, String> vertexLabelTransformer) {
    getRenderContext().setVertexLabelTransformer(vertexLabelTransformer);
  }

  public void setEdgeLabelTransformer(final Transformer<Step<T>, String> edgeLabelTransformer) {
    getRenderContext().setEdgeLabelTransformer(edgeLabelTransformer);
  }
}
