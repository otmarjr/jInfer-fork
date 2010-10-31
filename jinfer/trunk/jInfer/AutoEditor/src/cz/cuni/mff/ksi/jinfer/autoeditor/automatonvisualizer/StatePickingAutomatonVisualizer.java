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

import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.LayoutFactory;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.GraphMousePlugin;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author rio
 * TODO rio comment
 */
public class StatePickingAutomatonVisualizer<T> extends AutomatonVisualizer<T> {

  /**
   * Variable used to pass data to the GUI and gets the result of user
   * interaction.
   */
  final private BasicVisualizationServer<State<T>, Step<T>> bvs;

  public StatePickingAutomatonVisualizer(final Automaton<T> automaton, final SymbolToString<T> symbolToString) {
    final Graph<State<T>, Step<T>> graph = createGraph(automaton);
    final Layout<State<T>, Step<T>> layout = LayoutFactory.createGridLayout(automaton, graph);
    final VisualizationViewer<State<T>, Step<T>> visualizationViewer = new VisualizationViewer<State<T>, Step<T>>(layout);
    //visualizationViewer.setPreferredSize(new Dimension(350,350)); //Sets the viewing area size

    visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<State<T>>());
    visualizationViewer.getRenderContext().setEdgeLabelTransformer(makeTransformer(symbolToString));

    final PluggableGraphMouse gm = new PluggableGraphMouse();
    for (final GraphMousePlugin plugin : getDefaultGraphMousePlugins()) {
      gm.add(plugin);
    }
    gm.add(new PickingUnlimitedGraphMousePlugin<State<T>, Step<T>>());
    visualizationViewer.setGraphMouse(gm);

    bvs = visualizationViewer;
}

  @Override
  public BasicVisualizationServer<State<T>, Step<T>> getBasicVisualizationServer() {
    return bvs;
  }

  // TODO rio remove this and use Transformer instead of SymbolToString
  private static <T> Transformer<Step<T>, String> makeTransformer(final SymbolToString<T> symbolToString) {
    return new Transformer<Step<T>, String>() {

      @Override
      public String transform(Step<T> step) {
        return symbolToString.toString(step.getAcceptSymbol());
      }
    };
  }
}
