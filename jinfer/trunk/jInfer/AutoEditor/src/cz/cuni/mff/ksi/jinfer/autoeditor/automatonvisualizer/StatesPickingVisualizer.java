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

import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.graphmouseplugins.VerticesPickingGraphMousePlugin;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import java.awt.Point;
import javax.swing.UIManager;
import org.apache.commons.collections15.Transformer;

/**
 * Visualizer with support of vertex picking and straight line edges with custom
 * label.
 *
 * @author rio
 */
public class StatesPickingVisualizer<T> extends PluggableVisualizer<T> {

  private static final long serialVersionUID = 3523135;

  private static int legendFirstStateXCoordinate = 120;
  private static int legendStateYCoordinate = 25;
  private static int legendStateXCoordinateStep = 130;

  /**
   * Constructs instance with specified {@link Layout} and edge label {@link Transformer}.

   * @param layout Layout of automaton.
   * @param edgeLabelTransformer Transformer of edge labels.
   */
  public StatesPickingVisualizer(final Layout<State<T>, Step<T>> layout, final Transformer<Step<T>, String> edgeLabelTransformer) {
    super(layout);

    replaceEdgeLabelTransformer(edgeLabelTransformer);
    getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<State<T>, Step<T>>());

    addGraphMousePlugin(new VerticesPickingGraphMousePlugin<State<T>, Step<T>>());
  }
  
  @Override
  public Visualizer<T> createLegend() {
    final State<T> regularState = new State<T>(0, 1);
    final State<T> pickedState = new State<T>(0, 2);
    final State<T> finalState = new State<T>(1, 3);
    
    final SparseGraph<State<T>, Step<T>> legendGraph = new SparseGraph<State<T>, Step<T>>();
    legendGraph.addVertex(regularState);
    legendGraph.addVertex(pickedState);
    legendGraph.addVertex(finalState);

    final Layout<State<T>, Step<T>> layout = new StaticLayout<State<T>, Step<T>>(legendGraph);

    layout.setLocation(regularState, new Point(legendFirstStateXCoordinate, legendStateYCoordinate));
    layout.setLocation(pickedState, new Point(legendFirstStateXCoordinate + 1 * legendStateXCoordinateStep, legendStateYCoordinate));
    layout.setLocation(finalState, new Point(legendFirstStateXCoordinate + 2 * legendStateXCoordinateStep, legendStateYCoordinate));

    final Visualizer<T> visualizer = new Visualizer<T>(layout);
    visualizer.setPreferredSize(legendDimension);
    visualizer.getRenderer().getVertexLabelRenderer().setPosition(Position.W);
    visualizer.getPickedVertexState().pick(pickedState, true);
    visualizer.setBackground(UIManager.getLookAndFeelDefaults().getColor("Panel.background"));
    visualizer.getRenderContext().setVertexLabelTransformer(new Transformer<State<T>, String>() {

      @Override
      public String transform(State<T> state) {
        switch (state.getName()) {
          case 1:
            return "Regular state";
          case 2:
            return "Picked state";
          case 3:
            return "Final state";
          default:
            throw new IllegalArgumentException("Unknown state name");
        }
      }

    });

    return visualizer;
  }
}
