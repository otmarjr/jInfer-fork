/*
 *  Copyright (C) 2011 vektor
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
package cz.cuni.mff.ksi.jinfer.base.utils;

import cz.cuni.mff.ksi.jinfer.base.interfaces.IDSetSearch;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import org.openide.windows.WindowManager;

/**
 * Logic for displaying panel for ID/IDREF attributes search.
 *
 * @author vektor
 */
public final class IDSetSearchHelper {

  private IDSetSearchHelper() {
  }

  /**
   * Shows an ISS panel of provided name, based on the provided grammar.
   *
   * @param panelName Title of the panel.
   * @param grammar Grammar to work on.
   */
  public static void showISSPanelAsync(final String panelName,
          final List<Element> grammar) {
    if (BaseUtils.isEmpty(grammar)) {
      return;
    }
    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

      @Override
      public void run() {
        ModuleSelectionHelper.lookupImpls(IDSetSearch.class).get(0).showIDSetPanel(panelName, grammar);
      }
    });
  }

  /**
   * Generates a graph representation of the specified grammar in GraphViz
   * language and writes it to the specified file.
   *
   * @param grammar Grammar to create graph representation from.
   * @param file File to write GV representation to.
   */
  public static void generateGraphVizInput(final List<Element> grammar, final File file) {
    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

      @Override
      public void run() {
        final String gvInput = ModuleSelectionHelper.lookupImpls(IDSetSearch.class).get(0).getGraphVizInput(grammar);
        PrintWriter pw = null;
        try {
          pw = new PrintWriter(file);
          pw.write(gvInput);
        } catch (final IOException e) {
          throw new RuntimeException(e);
        } finally {
          pw.close();
        }

      }
    });
  }
}
