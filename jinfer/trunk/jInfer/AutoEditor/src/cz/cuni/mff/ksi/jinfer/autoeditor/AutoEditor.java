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

import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.AutomatonVisualizer;
import cz.cuni.mff.ksi.jinfer.autoeditor.gui.topcomponent.AutoEditorTopComponent;
import cz.cuni.mff.ksi.jinfer.autoeditor.gui.component.examples.OneButtonComponent;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import java.util.ArrayList;
import java.util.List;
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
 * @param <T>
 * @author rio
 */
public class AutoEditor<T> {

  public AutoEditor() {
  }

  /**
   * Draws automaton and waits until user picks some states and clicks
   * 'continue' button.
   *
   * @param automaton automaton to be drawn
   * @return returns picked states in a list
   */
  public static <T> List<State<T>> drawAutomatonToPickStates(final OneButtonComponent panel, final AutomatonVisualizer<T> visualizer) {
    panel.getAutomatonDrawPanel().add(visualizer.getBVS());

    // Call GUI in a special thread. Required by NB.
    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

      @Override
      public void run() {
        // Pass this as argument so the thread will be able to wake us up.
        AutoEditorTopComponent.findInstance().drawAutomatonBasicVisualizationServer(panel);
      }
    });

    try {
      // Sleep on this.
      panel.waitForGUIDone();
    } catch (InterruptedException e) {
      return null;
    }

    /* AutoEditorTopComponent wakes us up. Get the result and return it.
     * VisualizationViewer should give us the information about picked vertices.
     */
    final Set<State<T>> pickedSet = visualizer.getBVS().getPickedVertexState().getPicked();
    List<State<T>> lst = new ArrayList<State<T>>(pickedSet);
    return lst;
  }
}
