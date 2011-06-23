/*
 * Copyright (C) 2011 sviro
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
package cz.cuni.mff.ksi.jinfer.functionalDependencies.repairer;

import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sviro
 */
public final class RepairFactory {

  public static List<Pair<Repair, Repair>> getValuePairs(List<Repair> repairs) {
    if (repairs == null) {
      return null;
    }

    List<Pair<Repair, Repair>> result = new ArrayList<Pair<Repair, Repair>>();

    for (Repair repair : repairs) {
      if (repair.hasValueRepair()) {
        for (Repair repair1 : repairs) {
          if (repair1.hasValueRepair() && !repair.equals(repair) && !result.contains(new Pair<Repair, Repair>(repair1, repair))) {
            result.add(new Pair<Repair, Repair>(repair, repair1));
          }
        }
      }
    }

    return result;
  }

  private RepairFactory() {
  }

  public static List<Repair> getReliabilityRepairs(final List<Repair> repairs) {
    if (repairs == null) {
      return null;
    }

    List<Repair> result = new ArrayList<Repair>();

    for (Repair repair : repairs) {
      if (repair.hasReliabilityRepair()) {
        result.add(repair);
      }
    }
    return result;
  }

  public static List<Repair> getValueRepairs(final List<Repair> repairs) {
    if (repairs == null) {
      return null;
    }

    List<Repair> result = new ArrayList<Repair>();

    for (Repair repair : repairs) {
      if (repair.hasValueRepair()) {
        result.add(repair);
      }
    }

    return result;
  }
}
