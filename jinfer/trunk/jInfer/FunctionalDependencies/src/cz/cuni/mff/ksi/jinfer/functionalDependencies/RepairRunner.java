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

import cz.cuni.mff.ksi.jinfer.base.objects.Input;
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
import org.openide.util.Lookup;
import static cz.cuni.mff.ksi.jinfer.base.utils.AsynchronousUtils.runAsync;

/**
 *
 * @author sviro
 */
public class RepairRunner {
  
  private static final Logger LOG = Logger.getLogger(RepairRunner.class);
  private final ModelGenerator modelGenerator;
  
  private final ModelGeneratorCallback mgCallback = new ModelGeneratorCallback() {

    @Override
    public void finished(InitialModel model) {
      LOG.info("Initial Model has been created.");
      LOG.debug("Number of FDs: " + model.getFunctionalDependencies().size());
      LOG.debug("Number of Trees: " + model.getTrees().size());
      for (RXMLTree rXMLTree : model.getTrees()) {
        LOG.debug("Tree:\n" + rXMLTree.getXmlTree().toString() + "\n");
      }
      RunningProject.removeActiveProject();
    }
  };

  public RepairRunner() {
    modelGenerator = Lookup.getDefault().lookup(ModelGenerator.class);
  }

  
  
  
  public void run() {
    runAsync(new Runnable() {

      @Override
      public void run() {
        LOG.info("Starting retreiving input files");
        try {
          modelGenerator.start(RunningProject.getActiveProject().getLookup().lookup(Input.class), mgCallback);
        } catch (final InterruptedException e) {
          interrupted();
        }
        catch (final Throwable t) {
          unexpected(t);
        }
      }
    }, "Retreiving XML Tree");
  }
  
  private static void interrupted() {
    // TODO vektor Perhaps a message box?
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
