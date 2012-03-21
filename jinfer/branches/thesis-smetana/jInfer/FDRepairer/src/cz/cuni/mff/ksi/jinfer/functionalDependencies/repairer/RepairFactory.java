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

import cz.cuni.mff.ksi.jinfer.base.interfaces.Pair;
import cz.cuni.mff.ksi.jinfer.base.objects.ImmutablePair;
import java.util.ArrayList;
import java.util.List;

/**
 * Support class for Repair implementations.
 * @author sviro
 */
public final class RepairFactory {

  /**
   * Get pairs of all value repairs.
   * @param repairs List of repairs from which are pairs created.
   * @return List of pairs of all value repairs.
   * @throws InterruptedException 
   */
  public static List<Pair<RepairImpl, RepairImpl>> getValuePairs(final List<RepairImpl> repairs) throws InterruptedException {
    if (repairs == null) {
      return null;
    }

    final List<Pair<RepairImpl, RepairImpl>> result = new ArrayList<Pair<RepairImpl, RepairImpl>>();

    for (int i = 0; i < repairs.size() - 1; i++) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      final RepairImpl repair = repairs.get(i);
      if (repair.hasValueRepair()) {
        for (int j = 1 + i; j < repairs.size(); j++) {
          final RepairImpl repair1 = repairs.get(j);
          if (repair1.hasValueRepair()) {
            result.add(new ImmutablePair<RepairImpl, RepairImpl>(repair, repair1));
          }
        }
      }
    }

    return result;
  }

  private RepairFactory() {
  }

  /**
   * Get all repairs that are marking nodes as unreliable.
   * @param repairs Repairs from which are selected.
   * @return List of all repairs that are marking nodes as unreliable.
   */
  public static List<RepairImpl> getReliabilityRepairs(final List<RepairImpl> repairs) {
    if (repairs == null) {
      return null;
    }

    final List<RepairImpl> result = new ArrayList<RepairImpl>();

    for (RepairImpl repair : repairs) {
      if (repair.hasReliabilityRepair()) {
        result.add(repair);
      }
    }
    return result;
  }

  /**
   * Get all repairs that modifies node values.
   * @param repairs Repairs from which are selected.
   * @return List of all repairs that modifies node values.
   */
  public static List<RepairImpl> getValueRepairs(final List<RepairImpl> repairs) {
    if (repairs == null) {
      return null;
    }

    final List<RepairImpl> result = new ArrayList<RepairImpl>();

    for (RepairImpl repair : repairs) {
      if (repair.hasValueRepair()) {
        result.add(repair);
      }
    }

    return result;
  }
}
