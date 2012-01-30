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

import cz.cuni.mff.ksi.jinfer.functionalDependencies.RXMLTree;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.RepairPicker;
import org.openide.util.lookup.ServiceProvider;

/**
 * Implementation of the {@link RepairPicker} representing picker which choose 
 * repair candidate with the lowest weight.
 * @author sviro
 */
@ServiceProvider(service = RepairPicker.class)
public class RepairPickerImpl implements RepairPicker {

  public static final String NAME = "repair_picker";
  
  @Override
  public RepairCandidate getRepair(final RXMLTree tree) {
    final RepairGroup minimalRepairGroup = tree.getMinimalRepairGroup();
    tree.removeRG(minimalRepairGroup);
    
    return minimalRepairGroup.getMinimalRepair();
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getDisplayName() {
    return "Minimal Repair Picker";
  }

  @Override
  public String getModuleDescription() {
    return "This picker choose RepairGroup with minimal weight and from this"
            + "repair group choose repair with minimal weight";
  }
  
}
