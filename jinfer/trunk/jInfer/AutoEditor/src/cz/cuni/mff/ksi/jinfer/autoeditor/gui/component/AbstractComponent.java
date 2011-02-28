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
import java.awt.Dimension;
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
  private Dimension visualizerPanelSize = null;

  protected AbstractComponent() {
    super();
    monitor = new Object();
  }

  /**
   * Has to be overridden to return instance of {@link JPanel} to place drawn
   * {@link Automaton} on.
   * @return instance of {@link JPanel} to place drawn {@link Automaton} on.
   */
  abstract protected JPanel getAutomatonDrawPanel();

  /**
   * Setter for {@link Visualizer}. It is defined protected because extensions
   * of this class may want to declare this setter for an argument type that extends
   * {@link Visualizer} class.
   * @param visualizer
   */
  protected void setVisualizer(final Visualizer<T> visualizer) {
    this.visualizer = visualizer;

    final GridBagConstraints constraints = new GridBagConstraints();
    constraints.weightx = 1.0;
    constraints.weighty = 1.0;
    constraints.fill = GridBagConstraints.BOTH;
    final JPanel panel = getAutomatonDrawPanel();
    if (visualizerPanelSize == null) {
      visualizerPanelSize = panel.getSize();
    }
    visualizer.setPreferredSize(visualizerPanelSize);

    panel.removeAll();
    panel.add(visualizer, constraints);
    panel.validate();
  }

  /**
   * Getter.
   */
  public Visualizer<T> getVisualizer() {
    return visualizer;
  }

  /**
   * Thread that calls this method is suspended until method <code>guiDone()</code>
   * is called on this instance. Used to wait for user interaction when
   * {@link Automaton} is shown in AutoEditor.
   * @throws InterruptedException
   */
  public void waitForGuiDone() throws InterruptedException {
    synchronized (monitor) {
      monitor.wait();
    }
  }

  /**
   * Call to wake up all threads that are suspended in call of method
   * <code>waitForGUIDone()</code> on this instance.
   */
  public void guiDone() {
    synchronized (monitor) {
      monitor.notifyAll();
    }
  }

  /**
   * Signals AutoEditor that waiting for any user interaction (on this instance)
   * has to be interrupted and inference has to interrupted as well.
   */
  public void guiInterrupt() {
    interrupted = true;
    guiDone();
  }

  /**
   * Checks if <code>guiInterrupt()</code> was called on this instance.
   */
  public boolean guiInterrupted() {
    return interrupted;
  }
}
