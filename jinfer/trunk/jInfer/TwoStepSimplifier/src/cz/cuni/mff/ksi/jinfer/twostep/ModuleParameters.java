/*
 *  Copyright (C) 2010 anti
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
package cz.cuni.mff.ksi.jinfer.twostep;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTester;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifier;
import java.util.List;

/**
 * Interface giving access to parameters of module, if it supports "parameters"
 * capability.
 *
 * It is used in {@link MergeConditionTester} and {@link AutomatonSimplifier}.
 * Implementations of the former can have parameters to be set by the implementation
 * of latter one.
 * It is so, because {@link MergeConditionTester} cannot have project-wide configuration.
 *
 * @author anti
 */
public interface ModuleParameters {

  /**
   * List of parameters names - as used in code, as presented to user in dialogs.
   * @return
   */
  List<String> getParameterNames();

  /**
   * Short description for user explaining parameter purpose - what can be tuned using parameter.
   * @param parameterName name of parameter (as obtained by previous calling {@link getParameterNames}
   * @return description to be presented to user
   */
  String getParameterDisplayDescription(final String parameterName);

  /**
   * What is default value of parameter if no other is set?
   *
   * @param parameterName name of parameter (as obtained by previous calling {@link getParameterNames}
   * @return default value
   */
  String getParameterDefaultValue(final String parameterName);

  /**
   * Set parameter value to this submodule.
   *
   * @param parameterName name of parameter (as obtained by previous calling {@link getParameterNames}
   * @param newValue value to set
   */
  void setParameter(final String parameterName, final int newValue);
}
