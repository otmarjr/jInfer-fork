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
import java.awt.GridBagConstraints;
import javax.swing.JPanel;

/**
 *
 * @author rio
 * TODO rio comment
 */
public abstract class AbstractComponent<T> extends JPanel {

  private final Object monitor;
  private Visualizer<T> visualizer = null;
  private boolean interrupted = false;

  protected AbstractComponent() {
    monitor = new Object();
  }

  abstract protected JPanel getAutomatonDrawPanel();

  public void setVisualizer(final Visualizer<T> visualizer) {
    this.visualizer = visualizer;

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.weightx = 1.0;
    constraints.weighty = 1.0;
    constraints.fill = GridBagConstraints.BOTH;
    final JPanel panel = getAutomatonDrawPanel();
    panel.removeAll();
    panel.add(visualizer, constraints);
  }

  public Visualizer<T> getVisualizer() {
    return visualizer;
  }

  public void waitForGUIDone() throws InterruptedException {
    synchronized (monitor) {
      monitor.wait();
    }
  }

  protected void GUIDone() {
    synchronized (monitor) {
      monitor.notifyAll();
    }
  }

  public void GUIInterrupt() {
    interrupted = true;
    GUIDone();
  }

  public boolean GUIInterrupted() {
    return interrupted;
  }
}
