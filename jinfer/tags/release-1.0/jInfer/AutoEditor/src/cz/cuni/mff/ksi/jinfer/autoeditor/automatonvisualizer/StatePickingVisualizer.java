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

import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.graphmouseplugins.VertexPickingGraphMousePlugin;
import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.transformers.VertexShapeTransformer;
import cz.cuni.mff.ksi.jinfer.autoeditor.gui.component.AbstractComponent;
import cz.cuni.mff.ksi.jinfer.autoeditor.gui.component.StatePickingComponent;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import org.apache.commons.collections15.Transformer;

/**
 * Visualizer with support of picking of one vertex and straight line edges
 * with custom label.
 *
 * @author rio
 */
public class StatePickingVisualizer<T> extends PluggableVisualizer<T> {

  private static final long serialVersionUID = 8564336;

  /**
   * Constructs instance with specified {@link Layout}, edge label {@link Transformer}
   * and {@link AbstractComponent} which will be this visualizer displayed at.
   *
   * @param layout Layout of automaton.
   * @param edgeLabelTransformer Transformer of edge labels.
   * @param component Component which will be this visualizer displayed at. When
   *                  a state is picked, <code>guiDone()</code> method will be
   *                  called on this component.
   * @param superinitalState Superinitial state. Cannot be picked.
   * @param superfinalState Superfinal state. Cannot be picked.
   */
  public StatePickingVisualizer(final Layout<State<T>, Step<T>> layout, final Transformer<Step<T>, String> edgeLabelTransformer, final StatePickingComponent<T> component, final State<T> superinitialState, final State<T> superfinalState) {
    super(layout);

    replaceEdgeLabelTransformer(edgeLabelTransformer);
    getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<State<T>, Step<T>>());
    getRenderContext().setVertexShapeTransformer(new VertexShapeTransformer<T>(superinitialState, superfinalState));

    addGraphMousePlugin(new VertexPickingGraphMousePlugin<T>(component));
  }  
}
