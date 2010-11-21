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

import cz.cuni.mff.ksi.jinfer.autoeditor.gui.component.AbstractComponent;
import cz.cuni.mff.ksi.jinfer.autoeditor.gui.topcomponent.AutoEditorTopComponent;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.GraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;
import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
 * @author rio
 */
public class AutoEditor {

  public static <T> void drawComponentAsync(final AbstractComponent component) {
    if (component.getVisualizer() == null) {
      throw new IllegalStateException("Visualizer has not been set");
    }
    drawInGUI(component);
  }

  public static <T> boolean drawComponentAndWaitForGUI(final AbstractComponent component) {
    drawComponentAsync(component);
    try {
      component.waitForGUIDone();
    } catch (final InterruptedException e) {
      return false;
    }
    return true;
  }

  public static void closeTab() {
    // Call GUI in a special thread. Required by NB.
    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

      @Override
      public void run() {
        // Pass this as argument so the thread will be able to wake us up.
        AutoEditorTopComponent.findInstance().close();
      }
    });
  }

  public static <T> Graph<State<T>, Step<T>> createGraph(final Automaton<T> automaton) {
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

    return graph;
  }

  public static List<GraphMousePlugin> getDefaultGraphMousePlugins() {
    final LinkedList<GraphMousePlugin> list = new LinkedList<GraphMousePlugin>();
    list.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0));
    list.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON1_MASK | MouseEvent.CTRL_MASK));
    return list;
  }

  private static void drawInGUI(final AbstractComponent component) {
    // Call GUI in a special thread. Required by NB.
    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

      @Override
      public void run() {
        // Pass this as argument so the thread will be able to wake us up.
        AutoEditorTopComponent.findInstance().drawAutomatonBasicVisualizationServer(component);
      }
    });
  }
}
