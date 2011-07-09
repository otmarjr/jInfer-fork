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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.skstrings;

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
 * Factory for {@link SKStrings}.
 *
 * Implements "parameters" capability {@see ModuleParameters}.
 * 
 * @author anti
 */
@ServiceProvider(service = MergeConditionTesterFactory.class)
public class SKStringsFactory implements MergeConditionTesterFactory {

  private static final Logger LOG = Logger.getLogger(SKStringsFactory.class);
  private int parameterK = -1;
  private double parameterS = -1.0;
  private String parameterStrategy = "OR";
  private static final int K_DEFAULT_VALUE = 2;
  private static final double S_DEFAULT_VALUE = 1.0;
  private static final String STRATEGY_DEFAULT_VALUE = "OR";
  /**
   * Canonical name.
   */
  public static final String NAME = "TwoStepClusterProcessorAutomatonMergingStateMergeConditionTesterSKStrings";
  /**
   * Name presented to user.
   */
  public static final String DISPLAY_NAME = "s,k-strings";
  public static final String PROPERTIES_S = "s";
  public static final String PROPERTIES_S_DEFAULT = "0.5";
  public static final String PROPERTIES_K = "k";
  public static final String PROPERTIES_K_DEFAULT = "2";
  public static final String PROPERTIES_STRATEGY = "strategy";
  public static final String PROPERTIES_STRATEGY_DEFAULT = "OR";

  @Override
  public <T> MergeConditionTester<T> create() {
    LOG.debug("Creating new " + NAME);
    Properties p = RunningProject.getActiveProjectProps(NAME);
    try {
      return new SKStrings<T> (
              Integer.parseInt(p.getProperty(PROPERTIES_K, PROPERTIES_K_DEFAULT)),
              Double.parseDouble(p.getProperty(PROPERTIES_S, PROPERTIES_S_DEFAULT)),
              p.getProperty(PROPERTIES_STRATEGY, PROPERTIES_STRATEGY_DEFAULT)
              );
    } catch (NumberFormatException e) {
      LOG.error("Parameters s,k must be numbers. NumberFormatException. Creating " + NAME + " with default values.");
      return new SKStrings<T>(
              Integer.parseInt(PROPERTIES_K_DEFAULT),
              Double.parseDouble(PROPERTIES_S_DEFAULT),
              PROPERTIES_STRATEGY_DEFAULT);
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
    sb.append(" finds all <i>s,k</i>-strings of states being tested. States"
            + " are considered mergable (<i>s,k</i>-strings equivalent) when <i>s</i> percent of"
            + "<i>k</i>-strings (tails up to lenght <i>k</i>) are same (<b>AND</b> strategy), or are subset"
            + " of another state <i>k</i>-tails (<b>OR</b> strategy).");
    return sb.toString();
  }

  @Override
  public List<String> getParameterNames() {
    return Arrays.<String>asList("k", "s", "strategy");
  }

  @Override
  public String getParameterDisplayDescription(final String parameterName) {
    if ("k".equals(parameterName)) {
      return "k in <i>s,k</i>-strings. That is the number of transitions that have to"
              + "be same (by means of symbols) after state (<i>k</i>-tail). Default value: 2.";
    }
    if ("s".equals(parameterName)) {
      return "s in <i>s,k</i>-strings. States are equivalent when <i>s</i> percent of all <i>k</i>-tails are equivalent."
              + " Default value: 1.0";
    }
    if ("strategy".equals(parameterName)) {
      return "Strategy: <b>OR</b> - <i>s</i> percent of one state <i>k</i>-tails are subset of another state <i>k</i>-tails. <br>"
              + " Default value: OR.";
      // TODO anti comment
    }
    throw new IllegalArgumentException("Asking for a description of unknown parameter");
  }

  @Override
  public void setParameter(final String parameterName, final String newValue) {
    if ("k".equals(parameterName)) {
      this.parameterK = Integer.parseInt(newValue);
    }
    if ("s".equals(parameterName)) {
      this.parameterS = Double.parseDouble(newValue);
    }
    if ("strategy".equals(parameterName)) {
      this.parameterStrategy = newValue;
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
    } else if ("s".equals(parameterName)) {
      return String.valueOf(S_DEFAULT_VALUE);
    } else if ("strategy".equals(parameterName)) {
      return String.valueOf(STRATEGY_DEFAULT_VALUE);
    } else {
      return "";
    }
  }
}
