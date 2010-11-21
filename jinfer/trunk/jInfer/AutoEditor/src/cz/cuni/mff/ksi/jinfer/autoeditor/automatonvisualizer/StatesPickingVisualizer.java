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

import cz.cuni.mff.ksi.jinfer.autoeditor.AutoEditor;
import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.graphmouseplugins.VerticesPickingGraphMousePlugin;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.GraphMousePlugin;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author rio
 * TODO rio comment
 *
 * TODO mozno vyrobit class Visualizer<T> ktora bude len VisualizationViewer<State<T>, Step<T>> a to iste s Layoutom - skusit
 */
public class StatesPickingVisualizer<T> extends Visualizer<T> {

  public StatesPickingVisualizer(final Layout<State<T>, Step<T>> layout, final Transformer<Step<T>, String> edgeLabelTransformer) {
    super(layout);
    //setPreferredSize(new Dimension(350,350)); //Sets the viewing area size

    getRenderContext().setVertexLabelTransformer(new ToStringLabeller<State<T>>());
    getRenderContext().setEdgeLabelTransformer(edgeLabelTransformer);

    getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<State<T>, Step<T>>());

    final PluggableGraphMouse gm = new PluggableGraphMouse();
    for (final GraphMousePlugin plugin : AutoEditor.getDefaultGraphMousePlugins()) {
      gm.add(plugin);
    }
    gm.add(new VerticesPickingGraphMousePlugin<State<T>, Step<T>>());
    setGraphMouse(gm);
  }
  
}
