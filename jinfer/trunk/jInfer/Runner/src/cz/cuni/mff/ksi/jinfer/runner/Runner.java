/*
 *  Copyright (C) 2010 rio
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.runner;

import cz.cuni.mff.ksi.jinfer.base.interfaces.IOutputHandler;
import cz.cuni.mff.ksi.jinfer.base.interfaces.IGGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.IGGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SchemaGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SchemaGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Simplifier;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SimplifierCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.moduleselection.ModuleSelection;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;
import org.openide.util.TaskListener;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * Runner is responsible for running other modules in process of inference,
 * ensuring correct order and passing data from output of a module
 * to input of another.
 * 
 * @author rio
 */
public class Runner {

  private static final Logger LOG = Logger.getLogger(Runner.class);

  private final IGGenerator igGenerator;
  private final Simplifier simplifier;
  private final SchemaGenerator schemaGenerator;
  private final IGGeneratorCallback iggCallback = new IGGeneratorCallback() {

    @Override
    public void finished(final List<AbstractNode> grammar) {
      Runner.this.finishedIGGenerator(grammar);
    }
  };
  private final SimplifierCallback simplCallback = new SimplifierCallback() {

    @Override
    public void finished(final List<AbstractNode> grammar) {
      Runner.this.finishedSimplifier(grammar);
    }
  };
  private final SchemaGeneratorCallback sgCallback = new SchemaGeneratorCallback() {

    @Override
    public void finished(final String schema, final String extension) {
      Runner.this.finishedSchemaGenerator(schema, extension);
    }
  };

  public Runner() {
    final Properties projectProperties = RunningProject.getActiveProject().getLookup().lookup(Properties.class);

    igGenerator = ModuleSelection.lookupIGGenerator(projectProperties.getProperty("moduleselector.initialgrammar"));
    simplifier = ModuleSelection.lookupSimplifier(projectProperties.getProperty("moduleselector.simplifier"));
    schemaGenerator = ModuleSelection.lookupSchemaGenerator(projectProperties.getProperty("moduleselector.schemagenerator"));
  }

  private static void runAsync(final Runnable r, final String taskName) {
    final RequestProcessor rp = new RequestProcessor("interruptible", 1, true);
    final RequestProcessor.Task theTask = rp.create(r);
    final ProgressHandle handle = ProgressHandleFactory.createHandle(taskName, theTask);
    theTask.addTaskListener(new TaskListener() {
      @Override
      public void taskFinished(final Task task) {
        handle.finish();
      }
    });
    handle.start();
    theTask.schedule(0);
  }

  /**
   * Starts process of inference.
   */
  public void run() {
    runAsync(new Runnable() {

      @Override
      public void run() {
        try {
          igGenerator.start(RunningProject.getActiveProject().getLookup().lookup(Input.class), iggCallback);
        }
        catch (final InterruptedException e) {
          LOG.error("User interrupted the inference.");
          RunningProject.removeActiveProject();
        }
      }
    }, "Retrieving IG");
  }

  public void finishedIGGenerator(final List<AbstractNode> grammar) {
    LOG.info("Runner: initial grammar contains " + grammar.size()
            + " rules.");

    runAsync(new Runnable() {

      @Override
      public void run() {
        try {
          simplifier.start(grammar, simplCallback);
        }
        catch (final InterruptedException e) {
          LOG.error("User interrupted the inference.");
          RunningProject.removeActiveProject();
        }
      }
    }, "Inferring the schema");
  }

  public void finishedSimplifier(final List<AbstractNode> grammar) {
    LOG.info("Runner: simplified grammar contains " + grammar.size()
            + " rules.");

    runAsync(new Runnable() {

      @Override
      public void run() {
        try {
          schemaGenerator.start(grammar, sgCallback);
        }
        catch (final InterruptedException e) {
          LOG.error("User interrupted the inference.");
          RunningProject.removeActiveProject();
        }
      }
    }, "Generating result schema");
  }

  public void finishedSchemaGenerator(final String schema, final String extension) {
    LOG.info("Runner: writing schema.");
    final InputOutput ioResult = IOProvider.getDefault().getIO("jInfer result", false);
    ioResult.getOut().println(schema);

    RunningProject.getActiveProject().getLookup().lookup(IOutputHandler.class)
            .addOutput("generated-schema", getCommentedSchema(schema), extension);

    RunningProject.removeActiveProject();
    LOG.info("------------- DONE -------------");
  }

  private String getCommentedSchema(final String schema) {
    return "<!-- Inferred on " + (new Date()).toString()
            + " by " + igGenerator.getModuleName() + ", "
            + simplifier.getModuleName() + ", "
            + schemaGenerator.getModuleName() + " -->\n" + schema;
  }

}
