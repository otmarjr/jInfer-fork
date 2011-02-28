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
package cz.cuni.mff.ksi.jinfer.twostep.cleaning.nestedconcatenation;

import cz.cuni.mff.ksi.jinfer.twostep.cleaning.RegularExpressionCleaner;
import cz.cuni.mff.ksi.jinfer.twostep.cleaning.RegularExpressionCleanerFactory;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory for {@link NestedConcatenation}.
 *
 * @author anti
 */
@ServiceProvider(service = RegularExpressionCleanerFactory.class)
public class NestedConcatenationFactory implements RegularExpressionCleanerFactory {

  private static final Logger LOG = Logger.getLogger(NestedConcatenationFactory.class);
  /**
   * Canonical name.
   */
  public static final String NAME = "TwoStepRegularExpressionCleanerNestedConcatenation";
  /**
   * Name presented to user.
   */
  public static final String DISPLAY_NAME = "Nested Concatenation";

  @Override
  public <T> RegularExpressionCleaner<T> create() {
    LOG.debug("Creating new " + NAME);
    return new NestedConcatenation<T>();
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
    return Collections.<String>emptyList();
  }

  @Override
  public String getUserModuleDescription() {
    final StringBuilder sb = new StringBuilder(getModuleDescription());
    sb.append(" replaces nested concatenations is resulting regular expressions.");
    sb.append(" For example expression: <p align=\"center\"><i>(a, (b, c))</i></p> will");
    sb.append(" be transformed to nicer expression: <p align=\"center\"><i>(a, b, c)</i></p>");
    return sb.toString();
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }
}
