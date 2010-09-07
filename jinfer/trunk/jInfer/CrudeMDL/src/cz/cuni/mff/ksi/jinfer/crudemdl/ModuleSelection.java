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
package cz.cuni.mff.ksi.jinfer.crudemdl;

import cz.cuni.mff.ksi.jinfer.base.interfaces.ModuleName;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.Clusterer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.openide.util.Lookup;

/**
 * Class providing methods for lookup modules important for inference.
 * @author anti
 */
public final class ModuleSelection {

  private static Comparator<ModuleName> getComparator() {
    return new Comparator<ModuleName>() {

      @Override
      public int compare(final ModuleName o1, final ModuleName o2) {
        final String s1 = o1.getModuleName();
        final String s2 = o2.getModuleName();
        return s1.compareTo(s2);
      }
    };
  }


  private ModuleSelection() {
  }

  /**
   * 
   * @param name
   * @return
   */
  public static Clusterer<? extends Object> lookupClusterer(final String name) {
    final List<Clusterer<? extends Object>> clusterers = lookupClusterers();
    
    Clusterer<? extends Object> result = null;
    
    for (Clusterer<? extends Object> clusterer : clusterers) {
      if (result == null) {
        result = clusterer;
      }
      
      if (clusterer.getModuleName().equals(name)) {
        return clusterer;
      }
    }

    return result;
  }

  /**
   * 
   * @return
   */
  public static List<String> lookupClustererNames() {
    final List<String> list = new ArrayList<String>();
    for (Clusterer<? extends Object> clusterer : lookupClusterers()) {
      list.add(clusterer.getModuleName());
    }
    return list;
  }
 
  private static List<Clusterer<?>> lookupClusterers() {
    final Lookup lkp = Lookup.getDefault();
    final List<Clusterer<?>> result = new ArrayList<Clusterer<?>>();
    result.addAll( (Collection<? extends Clusterer<?>>) lkp.lookupAll( cz.cuni.mff.ksi.jinfer.crudemdl.clustering.Clusterer.class ));
    Collections.sort(result, getComparator());
    return result;
  }
}
