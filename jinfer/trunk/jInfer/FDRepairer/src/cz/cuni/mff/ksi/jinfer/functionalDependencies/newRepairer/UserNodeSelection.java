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
import java.util.Collection;

/**
 * Class representing the previous repair candidate selected by the user.
 * @author sviro
 */
public class UserNodeSelection {

  private final Collection<String> nodePaths;
  private final boolean isValueRepair;
  private final FD fD;

  /**
   * Constructor creating previous selection.
   * @param pickedRepair Repair candidate that was picked by the user.
   */
  public UserNodeSelection(final RepairCandidate pickedRepair) {
    this.fD = pickedRepair.getFD();
    this.nodePaths = pickedRepair.getNodePaths();
    this.isValueRepair = pickedRepair.hasValueRepair();
  }

  /**
   * Check if the canidate pick by this selection repairs the same functional dependency
   * as provided candidate.
   * @param repair Repair candidate to be checked.
   * @return true if this selection repairs the same functional dependency as provided candidate.
   */
  public boolean repairsSameFD(final RepairCandidate repair) {
    return fD.equals(repair.getFD());
  }

  /**
   * Check if the canidate pick by this selection use the same repair operation
   * as provided candidate.
   * @param repair Repair candidate to be checked.
   * @return true if this selection repairs use the same repair operation as provided candidate.
   */
  public boolean isUsingSameOperation(final RepairCandidate repair) {
    return isValueRepair == repair.hasValueRepair();
  }
  
  /**
   * Get number of nodes this selection modifies.
   * @return Number of nodes this selection modifies. 
   */
  public int getNodeSize() {
    return nodePaths.size();
  }

  /**
   * Get paths of all nodes modified by this selection.
   * @return Collection of paths of all nodes modified by this selection. 
   */
  public Collection<String> getNodePaths() {
    return nodePaths;
  }

  /**
   * Check if this selection contains subset of paths modifying the same nodes as provided candidate.
   * @param repair Repair candidate to be checked.
   * @param thresholdT Threshold t modifying required amount of the size of subset.
   * @return true if if this selection contains subset of paths modifying the same nodes as provided candidate.
   */
  public boolean existSubset(final RepairCandidate repair, final double thresholdT) {
    final int elementsSize = (int) Math.ceil(getNodeSize() * thresholdT);
    int containingPathsCount = 0; 
    for (String path : repair.getNodePaths()) {
      if (containingPathsCount == elementsSize) {
        return true;
      }
      
      if (nodePaths.contains(path)) {
        containingPathsCount++;
      }
    }
    return false;
  }
  
  
}
