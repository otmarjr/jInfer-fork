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

import javax.swing.JPanel;

/**
 *
 * @author rio
 * TODO rio comment
 */
public abstract class AutoEditorComponent extends JPanel {

  private final Object monitor;

  protected AutoEditorComponent() {
    monitor = new Object();
  }

  abstract public JPanel getAutomatonDrawPanel();

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
}
