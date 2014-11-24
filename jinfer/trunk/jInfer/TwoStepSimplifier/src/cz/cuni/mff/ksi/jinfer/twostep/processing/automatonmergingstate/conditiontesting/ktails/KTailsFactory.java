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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.ktails;

import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTesterFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTester;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory for {@link KTailsAlgorithm}.
 *

 * 
 * @author anti
 */
@ServiceProvider(service = MergeConditionTesterFactory.class)
public class KTailsFactory implements MergeConditionTesterFactory {

  private static final Logger LOG = Logger.getLogger(KTailsFactory.class);
  private static final int K_DEFAULT_VALUE = 2;
  /**
   * Canonical name.
   */
  public static final String NAME = "TwoStepClusterProcessorAutomatonMergingStateMergeConditionTesterKTails";
  /**
   * Name presented to user.
   */
  public static final String DISPLAY_NAME = "k-tails";
  public static final String PROPERTIES_K = "k";
  public static final String PROPERTIES_K_DEFAULT = "2";

  @Override
  public <T> MergeConditionTester<T> create() {
    LOG.debug("Creating new " + NAME);
    Properties p = RunningProject.getActiveProjectProps(NAME);
    try {
      return new KTailsAlgorithm<T>(
              Integer.parseInt(p.getProperty(PROPERTIES_K, PROPERTIES_K_DEFAULT)));
    } catch (NumberFormatException e) {
      LOG.error("Parameters k must be numbers. NumberFormatException. Creating " + NAME + " with default values.");
      return new KTailsAlgorithm<T>(
              Integer.parseInt(PROPERTIES_K_DEFAULT));
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
    sb.append(" finds all <i>s,k</i>-tails of states being tested. States"
            + " are considered mergable (<i>k</i>-tails equivalent) when "
            + "<i>k</i>-tails (ending in an accepting state) are same.");
    return sb.toString();
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }
}
