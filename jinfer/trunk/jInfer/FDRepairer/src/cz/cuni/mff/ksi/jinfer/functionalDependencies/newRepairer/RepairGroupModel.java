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
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * Class representing repair group in the repair picker.
 * @author sviro
 */
public class RepairGroupModel {
  
  private TreeNode tree = null;
  final private List<RepairGroup> repairGroups;

  /**
   * Constructor creating model of the repair groups for repair picker.
   * @param repairGroups Repair groups this model represent.
   */
  public RepairGroupModel(final List<RepairGroup> repairGroups) {
    if (BaseUtils.isEmpty(repairGroups)) {
      throw new IllegalArgumentException("Repair groups must not be empyt.");
    }
    
    this.repairGroups = repairGroups;
  }

  /**
   * Get node tree representing repair groups in the repair picker.
   * @return Node tree representing repair groups in the repair picker. 
   */
  public TreeNode getTree() {
    if (tree == null) {
      tree = new DefaultMutableTreeNode("");
      
      for (RepairGroup repairGroup : repairGroups) {
        final DefaultMutableTreeNode repairGroupNode = new DefaultMutableTreeNode("repairGroup - " + repairGroup.getWeight());
        
        for (RepairCandidate repair : repairGroup.getRepairs()) {
          final RepairTreeNode repairNode = new RepairTreeNode(repair);
          repairGroupNode.add(repairNode);
        }
        ((DefaultMutableTreeNode) tree).add(repairGroupNode);
      }
    }
    
    return tree;
  }
  
}
