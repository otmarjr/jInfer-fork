/*
 *  Copyright (C) 2011 sviro
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

import cz.cuni.mff.ksi.jinfer.base.interfaces.RuleDisplayer;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.List;
import org.openide.util.NbPreferences;
import org.openide.windows.WindowManager;

/**
 * Logic for getting selected Rule Displayer.
 *
 * @author sviro
 */
public final class RuleDisplayerHelper {

  private RuleDisplayerHelper() {
  }

  /**
   * Get selected Rule Displayer. If no Rule Displayer is selected, return first registered.
   * @return Selected Rule Displayer.
   */
  private static RuleDisplayer getRuleDisplayer() {
    final String ruleDisplayName = NbPreferences.forModule(RuleDisplayer.class).get(RuleDisplayer.RULE_DISPLAYER_PROP, RuleDisplayer.RULE_DISPLAYER_DEFAULT);
    return ModuleSelectionHelper.lookupImpl(RuleDisplayer.class, ruleDisplayName);
  }


  /**
   * Display a list of rules. The list will be rendered into
   * the Rule Displayer window, in a named panel.
   *
   * @param panelName Title of the panel where these rules will be displayed.
   * @param rules List of rules to display.
   * @param render Flag whether to actually do anything.
   */
  public static void showRulesAsync(final String panelName, final List<Element> rules, final boolean render) {
    if (!render || BaseUtils.isEmpty(rules)) {
      return;
    }
    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

      @Override
      public void run() {
        getRuleDisplayer().createDisplayer(panelName, rules);
      }
    });
  }
}
