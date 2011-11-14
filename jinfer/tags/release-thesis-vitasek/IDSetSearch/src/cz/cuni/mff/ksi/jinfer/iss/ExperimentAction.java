/*
 * Copyright (C) 2011 vektor
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
package cz.cuni.mff.ksi.jinfer.iss;

import cz.cuni.mff.ksi.jinfer.base.utils.AsynchronousUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.FileUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ExperimentSet;
import cz.cuni.mff.ksi.jinfer.iss.gui.ExperimentChooser;
import cz.cuni.mff.ksi.jinfer.iss.utils.Constants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Scanner;
import org.apache.log4j.Logger;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Build",
        id = "cz.cuni.mff.ksi.jinfer.iss.ExperimentAction")
@ActionRegistration(
        iconBase = "cz/cuni/mff/ksi/jinfer/iss/graphics/experiment.png",
        displayName = "#CTL_ExperimentAction")
@ActionReferences({
  @ActionReference(path = "Toolbars/Build", position = 500)
})
@Messages("CTL_ExperimentAction=Experiment now")
public final class ExperimentAction implements ActionListener {

  private static final Logger LOG = Logger.getLogger(ExperimentAction.class);

  @Override
  public void actionPerformed(final ActionEvent e) {
    if (Constants.RESTART.exists()) {
      final NotifyDescriptor nd = new NotifyDescriptor(
              "Restart file was found, probably a result of an experiment that did not finish correctly.\nContinue that experiment?",
              "Restart file found", NotifyDescriptor.YES_NO_OPTION, NotifyDescriptor.QUESTION_MESSAGE,
              null, NotifyDescriptor.YES_OPTION);
      if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.YES_OPTION) {
        try {
          final Scanner s = new Scanner(Constants.RESTART);
          final String experimentName = s.nextLine();
          String number = null;
          while (s.hasNextLine()) {
            final String str = s.nextLine();
            if (!BaseUtils.isEmpty(str)) {
              number = str;
            }
          }
          s.close();
          final int highestNumber = number == null ? -1 : Integer.valueOf(number).intValue();
          LOG.info("Restart file: experiment name: " + experimentName + ", highest number: " + highestNumber);
          runExperiment(experimentName, highestNumber + 1);
          return;
        } catch (final IOException ex) {
          throw new RuntimeException(ex);
        }
      } else {
        Constants.RESTART.delete();
      }
    }
    final ExperimentChooser chooser = new ExperimentChooser();
    chooser.setVisible(true);
  }

  /**
   * Asynchronously runs the chosen experimental set (identified by its name),
   * from the specified index.
   *
   * @param experimentName Name of the experimental set (see {@link ExperimentSet#getName()}).
   * @param from Index to start running the set from. Note that 0 means "run
   * from" beginning.
   */
  public static void runExperiment(final String experimentName, final int from) {
    final ExperimentSet set = ModuleSelectionHelper.lookupImpl(ExperimentSet.class, experimentName);
    FileUtils.writeString(experimentName, Constants.RESTART);
    AsynchronousUtils.runAsync(new Runnable() {

      @Override
      public void run() {
        try {
          set.run(from);
        } catch (final InterruptedException e) {
          LOG.error("Interrupted", e);
        }
      }
    }, "Experimenting");

  }
}
