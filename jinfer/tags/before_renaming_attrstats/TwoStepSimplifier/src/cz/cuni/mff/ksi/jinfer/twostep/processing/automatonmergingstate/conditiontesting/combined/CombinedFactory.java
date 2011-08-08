/*
 * Copyright (C) 2011 anti
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.combined;

import cz.cuni.mff.ksi.jinfer.base.utils.CollectionToString;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTester;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTesterFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.khcontext.KHContextFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.skstrings.SKStringsFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
@ServiceProvider(service = MergeConditionTesterFactory.class)
public class CombinedFactory implements MergeConditionTesterFactory {

  private static final Logger LOG = Logger.getLogger(CombinedFactory.class);
  /**
   * Canonical name.
   */
  public static final String NAME = "TwoStepClusterProcessorAutomatonMergingStateMergeConditionTesterCombined";
  /**
   * Name presented to user.
   */
  public static final String DISPLAY_NAME = "Combined";
  public static final String PROPERTIES_PREFIX = "combination";
  public static final String PROPERTIES_COUNT = "count";
  public static final String PROPERTIES_TESTER_DEFAULT = "TwoStepClusterProcessorAutomatonMergingStateMergeConditionTesterNull";

  @Override
  public <T> MergeConditionTester<T> create() {
    LOG.debug("Creating new " + NAME);
    return new Combined<T>(getMergeConditionTesterFactories());
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }

  @Override
  public String getModuleDescription() {
    final StringBuilder sb = new StringBuilder(getDisplayName());
    sb.append(CollectionToString.colToString(
            getMergeConditionTesterFactories(),
            ", ",
            new CollectionToString.ToString<MergeConditionTesterFactory>() {

              @Override
              public String toString(final MergeConditionTesterFactory t) {
                return t.getModuleDescription();
              }
            }));
    return sb.toString();
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.<String>emptyList();
  }

  @Override
  public String getUserModuleDescription() {
    final StringBuilder sb = new StringBuilder(getModuleDescription());
    sb.append(" combines another existing condition tester, returns");
    sb.append(" union of alternatives given by all testers.");
    return sb.toString();
  }

  private List<MergeConditionTesterFactory> getMergeConditionTesterFactories() {
    final Properties p = RunningProject.getActiveProjectProps(NAME);
    final List<MergeConditionTesterFactory> result = new ArrayList<MergeConditionTesterFactory>();

    final String _count = p.getProperty(PROPERTIES_COUNT, "notJebHojid4");
    if ("notJebHojid4".equals(_count)) {
      result.add(ModuleSelectionHelper.lookupImpl(MergeConditionTesterFactory.class, KHContextFactory.NAME));
      result.add(ModuleSelectionHelper.lookupImpl(MergeConditionTesterFactory.class, SKStringsFactory.NAME));
    } else {
      int count;
      try {
        count = Integer.valueOf(_count);
      } catch (NumberFormatException e) {
        count = 0;
      }
      for (int c = 0; c < count; c++) {
        final String name = p.getProperty(PROPERTIES_PREFIX + c);
        result.add(ModuleSelectionHelper.lookupImpl(MergeConditionTesterFactory.class, name));
      }
    }
    return result;
  }
}
