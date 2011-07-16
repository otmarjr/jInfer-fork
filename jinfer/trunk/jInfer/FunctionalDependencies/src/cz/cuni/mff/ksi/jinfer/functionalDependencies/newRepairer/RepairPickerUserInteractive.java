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
import cz.cuni.mff.ksi.jinfer.functionalDependencies.repairer.Repair;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.WindowManager;

/**
 *
 * @author sviro
 */
@ServiceProvider(service = RepairPicker.class)
public class RepairPickerUserInteractive implements RepairPicker {

  public static final String NAME = "repair_picker_UI";
  private boolean askUser = true;

  @Override
  public Repair getRepair(final RXMLTree tree) throws InterruptedException {
    if (askUser) {
      RepairPickerComponent component = new RepairPickerComponent();
      component.setModel(tree.getRepairGroups());

      drawComponentAndWaitForGUI(component);

      if (!component.shallAskUser()) {
        askUser = false;
      }
      Repair pickedRepair = component.getPickedRepair();
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
   * Asynchronously draws component in the AutoEditor tab.
   *
   * This function is asynchronous, which means that the drawing is not done
   * in a thread which this function is executed in. So this function can return
   * before the drawing is done.
   *
   * @param component Component with initialized instance of {@link Visualizer}.
   */
  public static void drawComponentAsync(final RepairPickerComponent component) {
    drawInGUI(component);
  }

  /**
   * Draws component in the AutoEditor tab.
   *
   * This function is synchronous. It returns when drawn component signals it.
   *
   * @param component Component with initialized instance of {@link Visualizer}.
   * @return Value of <code>true</code> if the component signaled return,
   * <code>false</code> if waiting was interrupted by another thread.
   * @throws InterruptedException If the AutoEditor tab was closed.
   */
  public static boolean drawComponentAndWaitForGUI(final RepairPickerComponent component) throws InterruptedException {
    drawComponentAsync(component);
    component.waitForGuiDone();

    if (component.guiInterrupted()) {
      throw new InterruptedException();
    }

    return true;
  }

  private Repair guessRepairFromUserSelection(RXMLTree tree) {
    for (RepairGroup repairGroup : tree.getRepairGroups()) {
      for (Repair repair : repairGroup.getRepairs()) {
        if (canBeUsedUserSelection(tree, repair)) {
          return repair;
        }
      }
    }

    return tree.getMinimalRepairGroup().getMinimalRepair();
  }

  private boolean canBeUsedUserSelection(RXMLTree tree, Repair repair) {
    double thresholdT = tree.getThresholdT();
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
