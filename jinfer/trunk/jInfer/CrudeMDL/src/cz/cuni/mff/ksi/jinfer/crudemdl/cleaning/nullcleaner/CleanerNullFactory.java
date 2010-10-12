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

package cz.cuni.mff.ksi.jinfer.crudemdl.cleaning.nullcleaner;

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
public class CleanerNullFactory implements RegularExpressionCleanerFactory {

  @Override
  public <T> RegularExpressionCleaner<T> create() {
    return new CleanerNull<T>();
  }

  @Override
  public String getName() {
    return "RegularExpressionCleanerNull";
  }

  @Override
  public String getModuleDescription() {
    return "Null cleaner (does nothing)";
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.<String>emptyList();
  }

  @Override
  public String getDisplayModuleDescription() {
    StringBuilder sb = new StringBuilder(getModuleDescription());
    sb.append(" is not editing regular expressions. It just passes input unchanged.");
    sb.append(" This is useful for seeing the real output from cluster processing algorithm");
    sb.append(" to perfrom comparison of algorithm performance. And is it useful");
    sb.append(" for debugging purposes of course.");
    return sb.toString();
  }

}
