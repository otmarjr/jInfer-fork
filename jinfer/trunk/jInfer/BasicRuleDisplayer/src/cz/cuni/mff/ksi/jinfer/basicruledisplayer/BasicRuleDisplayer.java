/*
 *  Copyright (C) 2010 vektor
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
package cz.cuni.mff.ksi.jinfer.basicruledisplayer;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.List;
import org.openide.windows.WindowManager;

/**
 * Helper class for displaying a list of rules.
 * 
 * @author vektor
 */
public final class BasicRuleDisplayer {

  private BasicRuleDisplayer() {
  }

  /**
   * Display a list of rules. The list will be rendered as an image and put into
   * the Rule Displayer window, in a named panel.
   *
   * @param panelName Title of the panel where these rules will be displayed.
   * @param rules List of rules to display.
   * @param render Flag whether to actually do anything.
   */
  public static void showRulesAsync(final String panelName,
          final List<Element> rules, final boolean render) {
    if (!render || BaseUtils.isEmpty(rules)) {
      return;
    }
    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

      @Override
      public void run() {
        BasicRuleDisplayerTopComponent.findInstance().createNewPanel(panelName).setRules(rules);
      }
    });
  }
}
