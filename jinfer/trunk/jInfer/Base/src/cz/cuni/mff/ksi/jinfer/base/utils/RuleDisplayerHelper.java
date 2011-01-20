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
import org.openide.util.NbPreferences;

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
  public static RuleDisplayer getRuleDisplayer() {
    final String ruleDisplayName = NbPreferences.forModule(RuleDisplayer.class).get(RuleDisplayer.RULE_DISPLAYER_PROPERTY, "TreeRuleDisplayer");
    return ModuleSelectionHelper.lookupImpl(RuleDisplayer.class, ruleDisplayName);
  }
}
