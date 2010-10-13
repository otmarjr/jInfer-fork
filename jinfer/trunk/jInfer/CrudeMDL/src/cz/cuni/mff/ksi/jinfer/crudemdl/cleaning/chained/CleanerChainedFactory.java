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

package cz.cuni.mff.ksi.jinfer.crudemdl.cleaning.chained;

import cz.cuni.mff.ksi.jinfer.crudemdl.cleaning.RegularExpressionCleaner;
import cz.cuni.mff.ksi.jinfer.crudemdl.cleaning.RegularExpressionCleanerFactory;
import java.util.Collections;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
@ServiceProvider(service = RegularExpressionCleanerFactory.class)
public class CleanerChainedFactory implements RegularExpressionCleanerFactory {
  public static final String NAME = "RegularExpressionCleanerChained";
  public static final String DISPLAY_NAME = "Chaining another existing cleaners cleaner";
  public static final String PROPERTIES_PREFIX = "chain";

  @Override
  public <T> RegularExpressionCleaner<T> create() {
    return new CleanerChained<T>();
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getModuleDescription() {
    return "Chaining another existing cleaners cleaner";
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.<String>emptyList();
  }

  @Override
  public String getDisplayModuleDescription() {
    StringBuilder sb = new StringBuilder(getModuleDescription());
    sb.append(" chains another existing cleaners in a row.");
    return sb.toString();
  }

}
