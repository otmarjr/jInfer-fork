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

import cz.cuni.mff.ksi.jinfer.base.interfaces.AttributeStatistics;
import cz.cuni.mff.ksi.jinfer.base.interfaces.RuleDisplayer;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.List;
import org.openide.util.NbPreferences;
import org.openide.windows.WindowManager;

/**
 * Logic for displaying attribute statistics window.
 *
 * @author vektor
 */
public final class AttributeStatsHelper {

  private AttributeStatsHelper() {
  }

  /**
   * Calculates and displays attribute statistics for the provided grammar in
   * a panel with provided title.
   *
   * @param panelName Title of the panel where this statistics will be displayed.
   * @param grammar Grammar to calculate statistics from.
   * @param render Flag whether to do anything at all.
   */
  public static void showStatisticsAsync(final String panelName,
          final List<Element> grammar, final boolean render) {
    if (!render || BaseUtils.isEmpty(grammar)) {
      return;
    }
    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

      @Override
      public void run() {
        ModuleSelectionHelper.lookupImpls(AttributeStatistics.class).get(0).showStatistics(panelName, grammar);
      }
    });
  }
}
