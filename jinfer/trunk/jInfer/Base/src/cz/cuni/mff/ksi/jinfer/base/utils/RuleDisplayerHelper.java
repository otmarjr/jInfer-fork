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
import cz.cuni.mff.ksi.jinfer.base.options.RuleDisplayersPanel;
import org.openide.util.NbPreferences;

/**
 * Logic for getting selected Rule Displayer.
 *
 * @author sviro
 */
public final class RuleDisplayerHelper {

  private RuleDisplayerHelper() {
  }

  ;

  /**
   * Get selected Rule Displayer, if no Rule displayer module is installed return null.
   * @return Selected Rule Displayer, if no Rule displayer is installed, return null.
   */
  public static RuleDisplayer getRuleDisplayer() {
    String ruleDisplayName = NbPreferences.forModule(RuleDisplayersPanel.class).get(RuleDisplayer.RULE_DISPLAYER_PROPERTY, RuleDisplayersPanel.RULE_DISPLAYER_DEFAULT);
    try {
      return ModuleSelectionHelper.lookupImpl(RuleDisplayer.class, ruleDisplayName, ModuleSelectionHelper.Fallback.EXCEPTION);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  /**
   * Check if some Rule Displayer is set.
   * @return <tt>true</tt> is some Rule Displayer is set, otherwise return <tt>false</tt>.
   */
  public static boolean isRuleDisplayerSet() {
    if (getRuleDisplayer() != null) {
      return true;
    }
    return false;
  }
}
