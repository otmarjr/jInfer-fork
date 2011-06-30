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

import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.repairer.Repair;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 *
 * @author sviro
 */
public class RepairGroupModel {
  
  private TreeNode tree = null;
  private List<RepairGroup> repairGroups;

  public RepairGroupModel(List<RepairGroup> repairGroups) {
    if (BaseUtils.isEmpty(repairGroups)) {
      throw new IllegalArgumentException("Repair groups must not be empyt.");
    }
    
    this.repairGroups = repairGroups;
  }

  TreeNode getTree() {
    if (tree == null) {
      tree = new DefaultMutableTreeNode("");
      
      for (RepairGroup repairGroup : repairGroups) {
        final DefaultMutableTreeNode repairGroupNode = new DefaultMutableTreeNode("repairGroup - " + repairGroup.getWeight());
        
        for (Repair repair : repairGroup.getRepairs()) {
          final RepairTreeNode repairNode = new RepairTreeNode(repair);
          repairGroupNode.add(repairNode);
        }
        ((DefaultMutableTreeNode) tree).add(repairGroupNode);
      }
    }
    
    return tree;
  }
  
}
