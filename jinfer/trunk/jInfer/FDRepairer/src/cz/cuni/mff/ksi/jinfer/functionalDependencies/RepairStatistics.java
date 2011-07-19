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
 *
 * @author sviro
 */
public class RepairStatistics {
  private int unreliableSize;
  private int newValueSize;
  private int valueChangesSize;
  
  private int startingRG = -1;
  private int RGCount;

  public int getUnreliableCount() {
    return unreliableSize;
  }

  public int getNewValueCount() {
    return newValueSize;
  }

  public int getValueChangesCount() {
    return valueChangesSize;
  }

  public void collectData(Repair repair) {
    unreliableSize += repair.getUnreliableNodes().size();
    
    Map<Node, NodeValue> valueNodes = repair.getValueNodes();
    for (Node node : valueNodes.keySet()) {
      if (repair.isNewValue(node)) {
        newValueSize++;
      } else {
        valueChangesSize++;
      }
    }
  }

  public void setRepairGroup(int repairGroupsSize) {
    if (startingRG == -1) {
      startingRG = repairGroupsSize;
    }
    
    RGCount++;
  }

  public int getStartingRG() {
    return startingRG;
  }

  public int getRGCount() {
    return RGCount;
  }
  
}
