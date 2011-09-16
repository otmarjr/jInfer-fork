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

import java.util.Properties;
import cz.cuni.mff.ksi.jinfer.base.interfaces.OutputHandler;
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import java.util.List;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.windows.IOProvider;
import org.openide.windows.IOSelect;
import org.openide.windows.InputOutput;
import java.util.EnumSet;
import org.apache.log4j.Logger;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.ModelGenerator;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.ModelGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.RepairedXMLGenerator;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.RepairedXMLGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.Repairer;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.RepairerCallback;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.properties.RepairerPropertiesPanel;
import org.openide.util.Lookup;
import static cz.cuni.mff.ksi.jinfer.base.utils.AsynchronousUtils.runAsync;

/**
 * RepairRunner is responsible for running other modules in process of repairing,
 * ensuring correct order and passing data from output of a module
 * to input of another.
 *
 * <p>
 *  Note that a constructed instance of RepairRunner remembers the modules selected
 *  for that specific inference run: every Runner should be used for one
 *  inference only!
 * </p>
 *
 * @author sviro
 */
public class RepairRunner {

  private static final Logger LOG = Logger.getLogger(RepairRunner.class);
  private final ModelGenerator modelGenerator;
  private final Repairer repairer;
  private final RepairedXMLGenerator repairedXMLGenerator;
  private final ModelGeneratorCallback mgCallback = new ModelGeneratorCallback() {

    @Override
    public void finished(final InitialModel model) {
      for (RXMLTree rXMLTree : model.getTrees()) {
        LOG.debug("Tree:\n" + rXMLTree.toString() + "\n");
        LOG.debug("Paths:" + rXMLTree.pathsToString() + "\n");
      }

      RepairRunner.this.finishedModelGenerator(model);
    }
  };
  private final RepairerCallback repairerCallback = new RepairerCallback() {

    @Override
    public void finished(final List<RXMLTree> repairedTrees) {
      RepairRunner.this.finishedRepairer(repairedTrees);
    }
  };
  private final RepairedXMLGeneratorCallback repairedXMLCallback = new RepairedXMLGeneratorCallback() {

    @Override
    public void finished(final List<String> xmls) {
      RepairRunner.this.finishedRepairedXMLGenerator(xmls);
    }
  };

  /**
   * Default, nonparametric constructor. Looks up all the repair modules.
   */
  public RepairRunner() {
    final Properties projectProperties = RunningProject.getActiveProjectProps(RepairerPropertiesPanel.NAME);

    modelGenerator = Lookup.getDefault().lookup(ModelGenerator.class);
    repairer = ModuleSelectionHelper.lookupImpl(Repairer.class, projectProperties.getProperty(RepairerPropertiesPanel.REPAIRER_PROP));
    repairedXMLGenerator = Lookup.getDefault().lookup(RepairedXMLGenerator.class);
  }

    /**
   * Starts process of repairing. Runs the first module and passes it the
   * reference to a callback to run when it finishes.
   */
  public void run() {
    runAsync(new Runnable() {

      @Override
      public void run() {
        LOG.info("Starting retreiving input files");
        try {
          modelGenerator.start(RunningProject.getActiveProject().getLookup().lookup(Input.class), mgCallback);
        } catch (final InterruptedException e) {
          interrupted();
        } catch (final Throwable t) {
          unexpected(t);
        }
      }
    }, "Retreiving XML Tree");
  }

  private void finishedModelGenerator(final InitialModel model) {
    LOG.info("Initial Model has been created.");
    LOG.info("Repair Runner: initial model contains " + model.getFDsCount() + " functional dependencies and " + model.getTreesCount() + " trees.");

    runAsync(new Runnable() {

      @Override
      public void run() {
        try {
          repairer.start(model, repairerCallback);
        } catch (final InterruptedException e) {
          interrupted();
        } catch (final Throwable t) {
          unexpected(t);
        }
      }
    }, "Repairing XMLs");
  }

  private void finishedRepairer(final List<RXMLTree> repairedTrees) {
    LOG.info("Finished repairing.");

    runAsync(new Runnable() {

      @Override
      public void run() {
        try {
          repairedXMLGenerator.start(repairedTrees, repairedXMLCallback);
        } catch (final InterruptedException e) {
          interrupted();
        } catch (final Throwable t) {
          unexpected(t);
        }
      }
    }, "Creating Repaired XMLs");
  }

  private void finishedRepairedXMLGenerator(final List<String> xmls) {
    LOG.info("Repair Runner: writing repaired XMLs.");

    final InputOutput ioResult = IOProvider.getDefault().getIO("jInfer repair result", true);

    for (String xml : xmls) {
      RunningProject.getActiveProject().getLookup().lookup(OutputHandler.class).addOutput(
              "repaired-xml{n}", xml, "xml", true);

      ioResult.getOut().println(xml);
      ioResult.getOut().println();
    }

    IOSelect.select(ioResult, EnumSet.allOf(IOSelect.AdditionalOperation.class));
    ioResult.getOut().close();

    RunningProject.removeActiveProject();
    LOG.info("------------- DONE -------------");
  }

  private static void interrupted() {
    LOG.error("User interrupted the inference.");
    RunningProject.removeActiveProject();
    RunningProject.setNextModuleCaps(null);
  }

  private static void unexpected(final Throwable t) {
    LOG.error("Inference interrupted due to an unexpected error.", t);
    RunningProject.removeActiveProject();
    RunningProject.setNextModuleCaps(null);

    // show Output window
    final InputOutput ioResult = IOProvider.getDefault().getIO("jInfer", false);
    IOSelect.select(ioResult, EnumSet.allOf(IOSelect.AdditionalOperation.class));

    // display a message box
    final NotifyDescriptor message = new NotifyDescriptor.Message("Process of inferrence caused an unexpected error:\n\n" + t.toString() + "\n\n Detailed information was logged to the logfile and inferrence was cancelled.", NotifyDescriptor.ERROR_MESSAGE);
    DialogDisplayer.getDefault().notify(message);
  }
}