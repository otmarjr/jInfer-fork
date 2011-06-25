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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author sviro
 */
public class RepairGroup {
  
  final List<Repair> repairs;
  final FD functionalDependency;
  
  public RepairGroup(final FD functionalDependency) {
    repairs = new ArrayList<Repair>();
    this.functionalDependency = functionalDependency;
  }
  
  public void addRepair(final Repair repair) {
    repairs.add(repair);
  }
  
  public void addRepairs(final Collection<Repair> repairs) {
    repairs.addAll(repairs);
  }
  
}
