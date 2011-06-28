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

import cz.cuni.mff.ksi.jinfer.functionalDependencies.repairer.Repair;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author sviro
 */
public class RepairTreeNode extends DefaultMutableTreeNode {

  private final Repair repair;
  
  RepairTreeNode(final Repair repair) {
    this.repair = repair;
  }

  public Repair getRepair() {
    return repair;
  }
}
