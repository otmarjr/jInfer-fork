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

package cz.cuni.mff.ksi.jinfer.autoeditor;

import cz.cuni.mff.ksi.jinfer.autoeditor.gui.AutoEditorTopComponent;
import cz.cuni.mff.ksi.jinfer.autoeditor.vyhnanovska.AutomatonLayoutTransformer;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.collections15.Transformer;
import org.openide.windows.WindowManager;

/**
 * Provides a way to draw automata in a GUI component and return a result of
 * user interaction with the drawing.
 *
 * How does it work?
 * - AutoEditor is called in a particular thread (AutoEditor thread). It
 *   prepares a visualization of a given automaton and runs an
 *   AutoEditorTopComponent function in a special thread (GUI thread, running
 *   in special thread is required by NB). Arguments passed to the
 *   AutoEditorTopComponent function are:
 *   1) reference to an instance of visualization of the automaton
 *   2) reference to this (this instance of AutoEditor).
 *   Then AutoEditor thread sleeps on its own instance.
 * - GUI thread stores the instance of AutoEditor in an instance of
 *   AutoEditorTopComponent class and draws what is be drawn. AutoEditor thread
 *   still sleeps.
 * - User interaction (i.e. button click) is handled in some NB thread
 *   (maybe it is the GUI thread - it is not important) which can access
 *   the same instance of AutoEditorTopComponent, so it can access
 *   the instance of AutoEditor, so it wakes AutoEditor thread up.
 * - AutoEditor thread gets the result of user interaction from the instance
 *   of visualization (which was passed to GUI thread) and returns it to the
 *   caller of AutoEditor. So the call of AutoEditor can be considered
 *   as synchronous.
 *
 * @param <T>
 * @author rio
 */
public class AutoEditor<T> {
  private SymbolToString<T> symbolToString;

  public AutoEditor(SymbolToString<T> symbolToString) {
    this.symbolToString= symbolToString;
  }

  public AutoEditor() {
  }

  /**
   * Variable used to pass data to the GUI and gets the result of user
   * interaction.
   */
  private VisualizationViewer<State<T>, Step<T>> visualizationViewer;

  /**
   * Draws automaton and waits until user picks some states and clicks
   * 'continue' button.
   *
   * @param automaton automaton to be drawn
   * @return returns picked states in a list
   */
  public List<State<T>> drawAutomatonToPickStates(final Automaton<T> automaton) {
    if (!AutoEditorTopComponent.getAskUser()) {
      throw new IllegalStateException("User does not want to interact with the inferrence anymore.");
    }

    final DirectedSparseGraph<State<T>, Step<T>> graph = new DirectedSparseGraph<State<T>, Step<T>>();
    final Map<State<T>, Set<Step<T>>> automatonDelta = automaton.getDelta();

    // Get vertices = states of automaton
    for (Entry<State<T>, Set<Step<T>>> entry : automatonDelta.entrySet()) {
      graph.addVertex(entry.getKey());
    }

    // Get edges of automaton
    for (Entry<State<T>, Set<Step<T>>> entry : automatonDelta.entrySet()) {
      for (Step<T> step : entry.getValue()) {
        graph.addEdge(step, step.getSource(), step.getDestination());
      }
    }


    // TODO rio find suitable layout
    //final Layout<State<T>, Step<T>> layout = new ISOMLayout<State<T>, Step<T>>(graph);

    // TODO rio toto je kod Julie
    final int	MIN_X_SIZE	= 7;
	final int	MIN_Y_SIZE	= 3;
	final int	SQUARE_SIZE	= 100;
    final AutomatonLayoutTransformer<T> automatonLayoutTransformer = new AutomatonLayoutTransformer<T>(MIN_X_SIZE, MIN_Y_SIZE, SQUARE_SIZE, graph, automaton);
    final Dimension dimension = automatonLayoutTransformer.getDimension();
	final Layout<State<T>, Step<T>> layout = new StaticLayout<State<T>, Step<T>>(graph, automatonLayoutTransformer, dimension);

    //layout.setSize(new Dimension(300,300)); // sets the initial size of the space

    visualizationViewer = new VisualizationViewer<State<T>, Step<T>>(layout);
    //visualizationViewer.setPreferredSize(new Dimension(350,350)); //Sets the viewing area size

    visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<State<T>>());
    visualizationViewer.getRenderContext().setEdgeLabelTransformer(
            new Transformer<Step<T>, String>() {
              @Override
              public String transform(Step<T> i) {
                return symbolToString.toString(i.getAcceptSymbol());
              }
    });

    final PluggableGraphMouse gm = new PluggableGraphMouse();
    gm.add(new PickingUnlimitedGraphMousePlugin<State<T>, Step<T>>());
    visualizationViewer.setGraphMouse(gm);

    // Call GUI in a special thread. Required by NB.
    synchronized(this) {
      WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

        @Override
        public void run() {
          // Pass this as argument so the thread will be able to wake us up.
          AutoEditorTopComponent.findInstance().drawAutomatonBasicVisualizationServer(AutoEditor.this, visualizationViewer, "Please select two states to be merged together.");
        }
      });

      try {
        // Sleep on this.
        this.wait();
      } catch (InterruptedException e) {
        return null;
      }
    }

    /* AutoEditorTopComponent wakes us up. Get the result and return it.
     * VisualizationViewer should give us the information about picked vertices.
     */
    final Set<State<T>> pickedSet = visualizationViewer.getPickedVertexState().getPicked();
    List<State<T>> lst = new ArrayList<State<T>>(pickedSet);
    return lst;
  }
}
