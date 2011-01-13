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

package cz.cuni.mff.ksi.jinfer.autoeditor.gui.component;

import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.Visualizer;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import java.awt.GridBagConstraints;
import javax.swing.JPanel;

/**
 * Base class representing panel with {@link Automaton} that can be drawn
 * by AutoEditor.
 * @author rio
 */
public abstract class AbstractComponent<T> extends JPanel {

  private final Object monitor;
  private Visualizer<T> visualizer = null;
  private boolean interrupted = false;

  protected AbstractComponent() {
    monitor = new Object();
  }

  /**
   * Has to be overridden to return instance of {@link JPanel} to place drawn
   * {@link Automaton} on.
   * @return instance of {@link JPanel} to place drawn {@link Automaton} on.
   */
  abstract protected JPanel getAutomatonDrawPanel();

  public void setVisualizer(final Visualizer<T> visualizer) {
    this.visualizer = visualizer;

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.weightx = 1.0;
    constraints.weighty = 1.0;
    constraints.fill = GridBagConstraints.BOTH;
    final JPanel panel = getAutomatonDrawPanel();
    visualizer.setPreferredSize(panel.getSize());
    panel.removeAll();
    panel.add(new GraphZoomScrollPane(visualizer), constraints);
  }

  /**
   * Getter.
   */
  public Visualizer<T> getVisualizer() {
    return visualizer;
  }

  /**
   * Thread that calls this method is suspended until method <code>GUIDone()</code>
   * is called on this instance. Used to wait for user interaction when
   * {@link Automaton} is shown in AutoEditor.
   * @throws InterruptedException
   */
  public void waitForGUIDone() throws InterruptedException {
    synchronized (monitor) {
      monitor.wait();
    }
  }

  /**
   * Call to wake up all threads that are suspended in call of method
   * <code>waitForGUIDone()</code> on this instance.
   */
  protected void GUIDone() {
    synchronized (monitor) {
      monitor.notifyAll();
    }
  }

  /**
   * Signals AutoEditor that waiting for any user interaction (on this instance)
   * has to be interrupted and inference has to interrupted as well.
   */
  public void GUIInterrupt() {
    interrupted = true;
    GUIDone();
  }

  /**
   * Checks if <code>GUIInterrupt()</code> was called on this instance.
   */
  public boolean GUIInterrupted() {
    return interrupted;
  }
}
