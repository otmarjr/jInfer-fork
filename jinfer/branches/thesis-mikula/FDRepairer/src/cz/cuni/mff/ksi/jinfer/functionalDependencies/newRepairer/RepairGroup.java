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
 * Class representing repair group from the thesis algorithm.
 * @author sviro
 */
public class RepairGroup {
  
  private final List<RepairCandidate> repairs;
  private final FD functionalDependency;
  private double weight = -1;
  
  /**
   * Constructor of repair group. The parameter defines functional dependency for 
   * which is this group defines.
   * @param functionalDependency Functional dependency for which is this group defined.
   */
  public RepairGroup(final FD functionalDependency) {
    repairs = new ArrayList<RepairCandidate>();
    this.functionalDependency = functionalDependency;
  }
  
  /**
   * Add repair candidate to this group.
   * @param repair Repair candidates to this group.
   */
  public void addRepair(final RepairCandidate repair) {
    repairs.add(repair);
  }
  
  /**
   * Add repair candidates to this group.
   * @param repairs Collection of repair candidates to this group. 
   */
  public void addRepairs(final Collection<RepairCandidate> repairs) {
    this.repairs.addAll(repairs);
    
    for (RepairCandidate repair : repairs) {
      repair.addToRepairGroup(this);
    }
  }

  /**
   * Get list of all repair candidates contained in this group.
   * @return List of all repair candidates contained in this group. 
   */
  public List<RepairCandidate> getRepairs() {
    if (!repairs.isEmpty()) {
      Collections.sort(repairs, new Comparator<RepairCandidate>() {

        @Override
        public int compare(final RepairCandidate o1, final RepairCandidate o2) {
          final double result = o1.getWeight() - o2.getWeight();
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

  /**
   * Set weight of this repair group.
   * @param weight Weight to be set. 
   */
  public void setWeight(final double weight) {
    this.weight = weight;
  }

  /**
   * Get weight of this repair group.
   * @return Weight of this repair group 
   */
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
  
  /**
   * Get repair candidate with the lowest weight.
   * @return Repair candidate with the lowest weight. 
   */
  public RepairCandidate getMinimalRepair() {
    if (!repairs.isEmpty()) {
      return getRepairs().get(0);
    }
    
    return null;
  }

  /**
   * Get functional dependency this repair groups is defined for.
   * @return Functional dependency this repair groups is defined for. 
   */
  public FD getFunctionalDependency() {
    return functionalDependency;
  }
  
  
}
