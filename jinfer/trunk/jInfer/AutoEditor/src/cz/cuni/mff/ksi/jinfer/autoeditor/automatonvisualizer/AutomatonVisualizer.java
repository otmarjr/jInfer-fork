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

import cz.cuni.mff.ksi.jinfer.autoeditor.vyhnanovska.LayoutFactory;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author rio
 * TODO rio comment
 */
public class AutomatonVisualizer<T> {

  final private Automaton<T> automaton;
  final private SymbolToString<T> symbolToString;
  final private BasicVisualizationServer<State<T>, Step<T>> bvs;
  /**
   * Variable used to pass data to the GUI and gets the result of user
   * interaction.
   */
  private VisualizationViewer<State<T>, Step<T>> visualizationViewer;

  public AutomatonVisualizer(final Automaton<T> automaton, final SymbolToString<T> symbolToString) {
    this.automaton = automaton;
    this.symbolToString = symbolToString;

    //if (!AutoEditorTopComponent.getAskUser()) {
    //  throw new IllegalStateException("User does not want to interact with the inferrence anymore.");
    //}

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

    final Layout<State<T>, Step<T>> layout = LayoutFactory.createLayout(automaton, graph);

    visualizationViewer = new VisualizationViewer<State<T>, Step<T>>(layout);
    //visualizationViewer.setPreferredSize(new Dimension(350,350)); //Sets the viewing area size

    visualizationViewer.getRenderContext().setVertexLabelTransformer(
            new ToStringLabeller<State<T>>());
    visualizationViewer.getRenderContext().setEdgeLabelTransformer(
            new Transformer<Step<T>, String>() {

              @Override
              public String transform(Step<T> i) {
                return symbolToString.toString(i.getAcceptSymbol());
              }
            });

    final PluggableGraphMouse gm = new PluggableGraphMouse();
    gm.add(new PickingUnlimitedGraphMousePlugin<State<T>, Step<T>>());
    gm.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0));
    gm.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON1_MASK | MouseEvent.CTRL_MASK));
    visualizationViewer.setGraphMouse(gm);

    bvs = visualizationViewer;
  }

  public BasicVisualizationServer<State<T>, Step<T>> getBVS() {
    return bvs;
  }
}
