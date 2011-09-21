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
import org.openide.windows.WindowManager;

/**
 * Implementation of the {@link RepairPicker} representing user selection picker.
 * 
 * @author sviro
 */
@ServiceProvider(service = RepairPicker.class)
public class RepairPickerUserInteractive implements RepairPicker {

  public static final String NAME = "repair_picker_UI";
  private boolean askUser = true;

  @Override
  public RepairCandidate getRepair(final RXMLTree tree) throws InterruptedException {
    if (askUser) {
      final RepairPickerComponent component = new RepairPickerComponent();
      component.setModel(tree.getRepairGroups());

      drawComponentAndWaitForGUI(component);

      if (!component.shallAskUser()) {
        askUser = false;
      }
      final RepairCandidate pickedRepair = component.getPickedRepair();
      tree.saveUserSelection(pickedRepair);

      return pickedRepair;
    }

    return guessRepairFromUserSelection(tree);
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getDisplayName() {
    return "User Interactive";
  }

  @Override
  public String getModuleDescription() {
    return "This picker depends on user selection of repair.";
  }

  private static void drawInGUI(final RepairPickerComponent component) {
    // Call GUI in a special thread. Required by NB.
    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

      @Override
      public void run() {
        // Pass this as argument so the thread will be able to wake us up.
        RepairPickerTopComponent.findInstance().drawRepairPicker(component);
      }
    });
  }

  /**
   * Draws component in the Repair picker window.
   *
   * This function is synchronous. It returns when drawn component signals it.
   *
   * @param component Component with initialized instance of {@link Visualizer}.
   * @return Value of <code>true</code> if the component signaled return,
   * <code>false</code> if waiting was interrupted by another thread.
   * @throws InterruptedException If the AutoEditor tab was closed.
   */
  public static boolean drawComponentAndWaitForGUI(final RepairPickerComponent component) throws InterruptedException {
    drawInGUI(component);
    component.waitForGuiDone();

    if (component.guiInterrupted()) {
      throw new InterruptedException();
    }

    return true;
  }

  private RepairCandidate guessRepairFromUserSelection(final RXMLTree tree) {
    for (RepairGroup repairGroup : tree.getRepairGroups()) {
      for (RepairCandidate repair : repairGroup.getRepairs()) {
        if (canBeUsedUserSelection(tree, repair)) {
          return repair;
        }
      }
    }

    return tree.getMinimalRepairGroup().getMinimalRepair();
  }

  private boolean canBeUsedUserSelection(final RXMLTree tree, final RepairCandidate repair) {
    final double thresholdT = tree.getThresholdT();
    for (UserNodeSelection userSelection : tree.getSavedUserSelections()) {
      if (userSelection.repairsSameFD(repair) && userSelection.isUsingSameOperation(repair)
              && (Math.ceil(userSelection.getNodeSize() * thresholdT) <= repair.getNodeSize())) {
        if (Math.ceil(userSelection.getNodeSize() * thresholdT) == repair.getNodeSize()
                && userSelection.getNodePaths().containsAll(repair.getNodePaths())) {
          return true;
        }
        if (Math.ceil(userSelection.getNodeSize() * thresholdT) < repair.getNodeSize() 
                && userSelection.existSubset(repair, thresholdT)) {
          return true;
        }
      }
    }
    return false;
  }
}
