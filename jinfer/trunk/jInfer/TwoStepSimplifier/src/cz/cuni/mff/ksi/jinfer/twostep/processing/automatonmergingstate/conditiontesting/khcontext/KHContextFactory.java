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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.khcontext;

import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.twostep.ModuleParameters;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTesterFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTester;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory for {@link KHContext}.
 *
 * Implements "parameters" capability {@see ModuleParameters}.
 * 
 * @author anti
 */
@ServiceProvider(service = MergeConditionTesterFactory.class)
public class KHContextFactory implements MergeConditionTesterFactory {

  private static final Logger LOG = Logger.getLogger(KHContextFactory.class);
  private int parameterK = -1;
  private int parameterH = -1;
  private static final int K_DEFAULT_VALUE = 2;
  private static final int H_DEFAULT_VALUE = 1;
  /**
   * Canonical name.
   */
  public static final String NAME = "TwoStepClusterProcessorAutomatonMergingStateMergeConditionTesterKHContext";
  /**
   * Name presented to user.
   */
  public static final String DISPLAY_NAME = "k,h-context";
  public static final String PROPERTIES_K = "k";
  public static final String PROPERTIES_K_DEFAULT = "2";
  public static final String PROPERTIES_H = "h";
  public static final String PROPERTIES_H_DEFAULT = "1";

  @Override
  public <T> MergeConditionTester<T> create() {
    LOG.debug("Creating new " + NAME);
    Properties p = RunningProject.getActiveProjectProps(NAME);
    try {
      return new KHContext<T>(
              Integer.parseInt(p.getProperty(PROPERTIES_K, PROPERTIES_K_DEFAULT)),
              Integer.parseInt(p.getProperty(PROPERTIES_H, PROPERTIES_H_DEFAULT)));
    } catch (NumberFormatException e) {
      LOG.error("Parameter k,h must be numbers, NumberFormatException. Creating " + NAME + " with default values.");
      return new KHContext<T>(
              Integer.parseInt(PROPERTIES_K_DEFAULT),
              Integer.parseInt(PROPERTIES_H_DEFAULT));
    }
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getModuleDescription() {
    return getDisplayName();
  }

  @Override
  public List<String> getCapabilities() {
    return Arrays.asList("parameters");
  }

  @Override
  public String getUserModuleDescription() {
    final StringBuilder sb = new StringBuilder(getDisplayName());
    sb.append(" finds all <i>k,h</i>-context of states being tested. If there are two"
            + " contexts that are equivalent (same symbol string), the states"
            + " are considered mergable (<i>k,h</i>-equivalent). Also <i>k - h</i> preceeding states in contexts are merged.");
    return sb.toString();
  }

  @Override
  public List<String> getParameterNames() {
    return Arrays.<String>asList("k", "h");
  }

  @Override
  public String getParameterDisplayDescription(final String parameterName) {
    if ("k".equals(parameterName)) {
      return "k in <i>k,h</i>-context. That is the number of transitions that have to"
              + "be same (by means of symbols) before state. Default value: 2.";
    }
    if ("h".equals(parameterName)) {
      return "h in <i>k,h</i>-context. Subsequent elements are the same already after <i>h</i> characters"
              + " Default value: 1.";
    }
    throw new IllegalArgumentException("Asking for a description of unknown parameter");
  }

  @Override
  public void setParameter(final String parameterName, final String newValue) {
    if ("k".equals(parameterName)) {
      this.parameterK = Integer.parseInt(newValue);
    }
    if ("h".equals(parameterName)) {
      this.parameterH = Integer.parseInt(newValue);
    }
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }

  @Override
  public String getParameterDefaultValue(final String parameterName) {
    if ("k".equals(parameterName)) {
      return String.valueOf(K_DEFAULT_VALUE);
    } else if ("h".equals(parameterName)) {
      return String.valueOf(H_DEFAULT_VALUE);
    } else {
      return "";
    }
  }
}
