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
package cz.cuni.mff.ksi.jinfer.functionalDependencies;

import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.Repair;
import java.util.Map;
import org.w3c.dom.Node;

/**
 * Class gathering all information from repairing for statistical purpose.
 * @author sviro
 */
public class RepairStatistics {
  private int unreliableSize;
  private int newValueSize;
  private int valueChangesSize;
  
  private int startingRG = -1;
  private int rGCount;

  /**
   * Get number of nodes that were marked as unreliable.
   * @return Number of nodes that were marked as unreliable.
   */
  public int getUnreliableCount() {
    return unreliableSize;
  }

  /**
   * Get number of nodes that value was changed to newly generated one.
   * @return Number of nodes that value was changed to newly generated one. 
   */
  public int getNewValueCount() {
    return newValueSize;
  }

  /**
   * Get number of nodes that value was changed to value of another node.
   * @return Number of nodes that value was changed to value of another node.
   */
  public int getValueChangesCount() {
    return valueChangesSize;
  }

  /**
   * Collect data for statistics from repair.
   * @param repair Repair from which is data collected.
   */
  public void collectData(final Repair repair) {
    unreliableSize += repair.getUnreliableNodes().size();
    
    final Map<Node, NodeValue> valueNodes = repair.getValueNodes();
    for (Node node : valueNodes.keySet()) {
      if (repair.isNewValue(node)) {
        newValueSize++;
      } else {
        valueChangesSize++;
      }
    }
  }

  /**
   * Set the initial number of repair groups. 
   * @param repairGroupsSize Number of repair groups to be set.
   */
  public void setRepairGroup(final int repairGroupsSize) {
    if (startingRG == -1) {
      startingRG = repairGroupsSize;
    }
    
    rGCount++;
  }

  /**
   * Get initial number of repair groups.
   * @return Initial number of repair groups. 
   */
  public int getStartingRG() {
    return startingRG;
  }

  /**
   * Get numer of repair groups used in repair.
   * @return Numer of repair groups used in repair. 
   */
  public int getRGCount() {
    return rGCount;
  }
  
}