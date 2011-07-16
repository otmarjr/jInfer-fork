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
import cz.cuni.mff.ksi.jinfer.functionalDependencies.repairer.Repair;
import java.util.Collection;

/**
 *
 * @author sviro
 */
public class UserNodeSelection {

  private final Collection<String> nodePaths;
  private final boolean isValueRepair;
  private final FD fD;

  public UserNodeSelection(Repair pickedRepair) {
    this.fD = pickedRepair.getFD();
    this.nodePaths = pickedRepair.getNodePaths();
    this.isValueRepair = pickedRepair.hasValueRepair();
  }

  public boolean repairsSameFD(Repair repair) {
    return fD.equals(repair.getFD());
  }

  public boolean isUsingSameOperation(final Repair repair) {
    return isValueRepair == repair.hasValueRepair();
  }
  
  public int getNodeSize() {
    return nodePaths.size();
  }

  public Collection<String> getNodePaths() {
    return nodePaths;
  }

  public boolean existSubset(Repair repair, double thresholdT) {
    int elementsSize = (int) Math.ceil(getNodeSize() * thresholdT);
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
