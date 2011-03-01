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
package cz.cuni.mff.ksi.jinfer.twostep.cleaning.chained;

import cz.cuni.mff.ksi.jinfer.base.utils.CollectionToString;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.twostep.cleaning.RegularExpressionCleaner;
import cz.cuni.mff.ksi.jinfer.twostep.cleaning.RegularExpressionCleanerFactory;
import cz.cuni.mff.ksi.jinfer.twostep.cleaning.emptychildren.EmptyChildrenFactory;
import cz.cuni.mff.ksi.jinfer.twostep.cleaning.nestedconcatenation.NestedConcatenationFactory;
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
@ServiceProvider(service = RegularExpressionCleanerFactory.class)
public class ChainedFactory implements RegularExpressionCleanerFactory {

  private static final Logger LOG = Logger.getLogger(ChainedFactory.class);
  /** 
   * Canonical name.
   */
  public static final String NAME = "TwoStepRegularExpressionCleanerChained";
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
  public static final String PROPERTIES_CLEANER_DEFAULT = "TwoStepRegularExpressionCleanerNull";
  /**
   * Property name for count of cleaners user selected in a chain.
   */
  public static final String PROPERTIES_COUNT = "count";

  @Override
  public <T> RegularExpressionCleaner<T> create() {
    LOG.debug("Creating new " + NAME);
    return new Chained<T>(getRegularexpressionCleanerFactories());
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getModuleDescription() {
    final StringBuilder sb = new StringBuilder(getDisplayName());
    sb.append(CollectionToString.colToString(
            getRegularexpressionCleanerFactories(),
            ", ",
            new CollectionToString.ToString<RegularExpressionCleanerFactory>() {

              @Override
              public String toString(final RegularExpressionCleanerFactory t) {
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
    sb.append(" chains another existing cleaners in a sequence pipelining output of");
    sb.append(" first cleaner to input of second cleaner and so on.");
    return sb.toString();
  }

  private List<RegularExpressionCleanerFactory> getRegularexpressionCleanerFactories() {
    final Properties p = RunningProject.getActiveProjectProps(NAME);
    final List<RegularExpressionCleanerFactory> result = new ArrayList<RegularExpressionCleanerFactory>();

    final String _count = p.getProperty(PROPERTIES_COUNT, "notJebHojid4");
    if ("notJebHojid4".equals(_count)) {
      result.add(ModuleSelectionHelper.lookupImpl(RegularExpressionCleanerFactory.class, EmptyChildrenFactory.NAME));
      result.add(ModuleSelectionHelper.lookupImpl(RegularExpressionCleanerFactory.class, NestedConcatenationFactory.NAME));
    } else {
      int count;
      try {
        count = Integer.valueOf(_count);
      } catch (NumberFormatException e) {
        count = 0;
      }
      for (int c = 0; c < count; c++) {
        final String name = p.getProperty(PROPERTIES_PREFIX + c);
        result.add(ModuleSelectionHelper.lookupImpl(RegularExpressionCleanerFactory.class, name));
      }
    }
    return result;
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }
}
