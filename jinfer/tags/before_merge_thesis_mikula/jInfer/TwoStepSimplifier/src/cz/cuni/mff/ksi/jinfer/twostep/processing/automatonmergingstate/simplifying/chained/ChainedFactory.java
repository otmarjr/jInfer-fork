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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.chained;

import cz.cuni.mff.ksi.jinfer.base.utils.CollectionToString;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifierFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.defective.defectivemdl.DefectiveMDLFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.greedymdl.GreedyMDLFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory for {@link Chained}.
 *
 * @author anti
 */
@ServiceProvider(service = AutomatonSimplifierFactory.class)
public class ChainedFactory implements AutomatonSimplifierFactory {

  private static final Logger LOG = Logger.getLogger(ChainedFactory.class);
  /**
   * Canonical name.
   */
  public static final String NAME = "TwoStepClusterProcessorAutomatonMergingStateAutomatonSimplifierChained";
  /**
   * Name presented to user.
   */
  public static final String DISPLAY_NAME = "Chained";
  /**
   * Property name prefix for cleaners selected.
   */
  public static final String PROPERTIES_PREFIX = "chain";
  /**
   * Default cleaner to use in chain when none are selected.
   */
  public static final String PROPERTIES_SIMPLIFIER_DEFAULT = "TwoStepClusterProcessorAutomatonMergingStateAutomatonSimplifierNull";
  /**
   * Property name for count of cleaners user selected in a chain.
   */
  public static final String PROPERTIES_COUNT = "count";

  @Override
  public <T> AutomatonSimplifier<T> create() {
    LOG.debug("Creating new " + NAME);
    return new Chained<T>(getAutomatonSimplifierFactories());
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getModuleDescription() {
    final StringBuilder sb = new StringBuilder(getDisplayName());
    sb.append(CollectionToString.colToString(
            getAutomatonSimplifierFactories(),
            ", ",
            new CollectionToString.ToString<AutomatonSimplifierFactory>() {

              @Override
              public String toString(final AutomatonSimplifierFactory t) {
                return t.getModuleDescription();
              }
            }));
    return sb.toString();
  }

  @Override
  public String getUserModuleDescription() {
    final StringBuilder sb = new StringBuilder(getDisplayName());
    sb.append(" chains another existing simplifiers in a sequence pipelining output of");
    sb.append(" first to input of second and so on.");
    return sb.toString();
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.<String>emptyList();
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }

  private List<AutomatonSimplifierFactory> getAutomatonSimplifierFactories() {
    final Properties p = RunningProject.getActiveProjectProps(NAME);
    final List<AutomatonSimplifierFactory> result = new ArrayList<AutomatonSimplifierFactory>();

    final String _count = p.getProperty(PROPERTIES_COUNT, "notJebHojid4");
    if ("notJebHojid4".equals(_count)) {
      result.add(ModuleSelectionHelper.lookupImpl(AutomatonSimplifierFactory.class, GreedyMDLFactory.NAME));
      result.add(ModuleSelectionHelper.lookupImpl(AutomatonSimplifierFactory.class, DefectiveMDLFactory.NAME));
    } else {
      int count;
      try {
        count = Integer.valueOf(_count);
      } catch (NumberFormatException e) {
        count = 0;
      }
      for (int c = 0; c < count; c++) {
        final String name = p.getProperty(PROPERTIES_PREFIX + c);
        result.add(ModuleSelectionHelper.lookupImpl(AutomatonSimplifierFactory.class, name));
      }
    }
    return result;
  }
}
