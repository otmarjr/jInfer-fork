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

import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.Visualizer;
import cz.cuni.mff.ksi.jinfer.autoeditor.gui.component.AbstractComponent;
import cz.cuni.mff.ksi.jinfer.autoeditor.gui.topcomponent.AutoEditorTopComponent;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import org.openide.windows.WindowManager;

/**
 *
 * Contains static function for displaying visualized {@link Automaton} in NB GUI.
 *
 * @author rio
 */
public final class AutoEditor {

  private AutoEditor() {
  }

  /**
   * Asynchronously draws component in the AutoEditor tab.
   *
   * This function is asynchronous, which means that the drawing is not done
   * in a thread which this function is executed in. So this function can return
   * before the drawing is done.
   *
   * @param component Component with initialized instance of {@link Visualizer}.
   */
  public static void drawComponentAsync(final AbstractComponent component) {
    if (component.getVisualizer() == null) {
      throw new IllegalStateException("Visualizer has not been set");
    }
    drawInGUI(component);
  }

  /**
   * Draws component in the AutoEditor tab.
   *
   * This function is synchronous. It returns when drawn component signals it.
   *
   * @param component Component with initialized instance of {@link Visualizer}.
   * @return Value of <code>true</code> if the component signaled return,
   * <code>false</code> if waiting was interrupted by another thread.
   * @throws InterruptedException If the AutoEditor tab was closed.
   */
  public static boolean drawComponentAndWaitForGUI(final AbstractComponent component) throws InterruptedException {
    drawComponentAsync(component);
    component.waitForGuiDone();

    if (component.guiInterrupted()) {
      throw new InterruptedException();
    }

    return true;
  }

  /**
   * Closes the AutoEditor tab.
   */
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

  private static void drawInGUI(final AbstractComponent component) {
    // Call GUI in a special thread. Required by NB.
    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

      @Override
      public void run() {
        // Pass this as argument so the thread will be able to wake us up.
        AutoEditorTopComponent.findInstance().drawComponent(component);
      }
    });
  }
}
