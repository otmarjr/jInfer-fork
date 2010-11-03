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
package cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.statespickingvisualizer;

import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.AutomatonVisualizer;
import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.graphmouseplugins.VerticesPickingGraphMousePlugin;
import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.GridLayout;
import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.LayoutHolder;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.GraphMousePlugin;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author rio
 * TODO rio comment
 */
public class StatesPickingVisualizer<T> extends AutomatonVisualizer<T> {

  /**
   * Variable used to pass data to the GUI and gets the result of user
   * interaction.
   */
  final private BasicVisualizationServer<State<T>, Step<T>> bvs;

  public StatesPickingVisualizer(final LayoutHolder<T> layoutHolder) {
    final VisualizationViewer<State<T>, Step<T>> visualizationViewer = new VisualizationViewer<State<T>, Step<T>>(layoutHolder.getLayout());
    //visualizationViewer.setPreferredSize(new Dimension(350,350)); //Sets the viewing area size

    visualizationViewer.getRenderContext().setVertexLabelTransformer(layoutHolder.getVertexLabelTransformer());
    visualizationViewer.getRenderContext().setEdgeLabelTransformer(layoutHolder.getEdgeLabelTransformer());

    final PluggableGraphMouse gm = new PluggableGraphMouse();
    for (final GraphMousePlugin plugin : getDefaultGraphMousePlugins()) {
      gm.add(plugin);
    }
    gm.add(new VerticesPickingGraphMousePlugin<State<T>, Step<T>>());
    visualizationViewer.setGraphMouse(gm);

    bvs = visualizationViewer;
  }

  // default layout- remove maybe
  public StatesPickingVisualizer(final Automaton<T> automaton, final Transformer<Step<T>, String> edgeLabelTransformer) {
    this(new GridLayout<T>(automaton, edgeLabelTransformer));
  }

  @Override
  public BasicVisualizationServer<State<T>, Step<T>> getBasicVisualizationServer() {
    return bvs;
  }
}
