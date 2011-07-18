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
package cz.cuni.mff.ksi.jinfer.functionalDependencies.newRepairer;

import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.FD;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author sviro
 */
public class RepairGroup {
  
  private final List<RepairCandidate> repairs;
  private final FD functionalDependency;
  private double weight = -1;
  
  public RepairGroup(final FD functionalDependency) {
    repairs = new ArrayList<RepairCandidate>();
    this.functionalDependency = functionalDependency;
  }
  
  public void addRepair(final RepairCandidate repair) {
    repairs.add(repair);
  }
  
  public void addRepairs(final Collection<RepairCandidate> repairs) {
    this.repairs.addAll(repairs);
    
    for (RepairCandidate repair : repairs) {
      repair.addToRepairGroup(this);
    }
  }

  public List<RepairCandidate> getRepairs() {
    if (!repairs.isEmpty()) {
      Collections.sort(repairs, new Comparator<RepairCandidate>() {

        @Override
        public int compare(RepairCandidate o1, RepairCandidate o2) {
          double result = o1.getWeight() - o2.getWeight();
          if (result < 0) {
            return -1;
          }
          
          if (result > 0) {
            return 1;
          }
          return 0;
        }
      });
    }
    return repairs;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }

  public double getWeight() {
    if (weight == -1) {
      weight = computeWeight();
    }
    return weight;
  }

  private double computeWeight() {
    double result = 0d;
    
    for (RepairCandidate repair : repairs) {
      result += repair.getWeight();
    }
    
    return result;     
  }
  
  public RepairCandidate getMinimalRepair() {
    if (!repairs.isEmpty()) {
      return getRepairs().get(0);
    }
    
    return null;
  }

  public FD getFunctionalDependency() {
    return functionalDependency;
  }
  
  
}
