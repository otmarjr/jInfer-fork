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
package cz.cuni.mff.ksi.jinfer.base.interfaces;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.List;

/**
 * Interface of the Rule displayer used to show rule sets in some graphical way.
 *
 * @author sviro
 */
public interface RuleDisplayer extends NamedModule {

  String RULE_DISPLAYER_PROP = "rule.displayer";
  String RULE_DISPLAYER_DEFAULT = "NullRuleDisplayer";

  /**
   * Creates rule displayer window in which rules are displayed.
   *
   * @param panelName Name of the rule displayer window.
   * @param rules Rules to be displayed in rule displayer.
   */
  void createDisplayer(final String panelName, final List<Element> rules);
}
