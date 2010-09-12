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

package cz.cuni.mff.ksi.jinfer.crudemdl.moduleselection;

import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import java.util.Comparator;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class ModuleNameComparator {
  public static Comparator<NamedModule> getComparator() {
    return new Comparator<NamedModule>() {
      @Override
      public int compare(final NamedModule o1, final NamedModule o2) {
        final String s1 = o1.getName();
        final String s2 = o2.getName();
        return s1.compareTo(s2);
      }
    };
  }

}
