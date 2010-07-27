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

  private final IGGenerator igGenerator;
  private final Simplifier simplifier;
  private final SchemaGenerator schemaGenerator;
  private static Logger LOG = Logger.getLogger(Runner.class);

  public Runner() {
    final Properties projectProperties = RunningProject.getActiveProject().getLookup().lookup(Properties.class);

    igGenerator = ModuleSelection.lookupIGGenerator(projectProperties.getProperty("moduleselector.initialgrammar"));
    simplifier = ModuleSelection.lookupSimplifier(projectProperties.getProperty("moduleselector.simplifier"));
    schemaGenerator = ModuleSelection.lookupSchemaGenerator(projectProperties.getProperty("moduleselector.schemagenerator"));
  }

  /**
   * Starts process of inference.
   */
  public void run() {

    igGenerator.start(RunningProject.getActiveProject().getLookup().lookup(Input.class), new IGGeneratorCallback() {

      @Override
      public void finished(final List<AbstractNode> grammar) {
        Runner.this.finishedIGGenerator(grammar);
      }
    });
  }

  public void finishedIGGenerator(final List<AbstractNode> grammar) {
    LOG.info("Runner: initial grammar contains " + grammar.size()
            + " rules.");

    final RequestProcessor.Task theTask = RequestProcessor.getDefault().create(new Runnable() {

      @Override
      public void run() {
        simplifier.start(grammar, new SimplifierCallback() {

          @Override
          public void finished(final List<AbstractNode> grammar) {
            Runner.this.finishedSimplifier(grammar);
          }
        });
      }
    });

    final ProgressHandle handle = ProgressHandleFactory.createHandle("Inferring the schema", theTask);
    theTask.addTaskListener(new TaskListener() {

      @Override
      public void taskFinished(final org.openide.util.Task task) {
        handle.finish();
      }
    });

    handle.start();

    theTask.schedule(0);
  }

  public void finishedSimplifier(final List<AbstractNode> grammar) {
    LOG.info("Runner: simplified grammar contains " + grammar.size()
            + " rules.");
    schemaGenerator.start(grammar, new SchemaGeneratorCallback() {

      @Override
      public void finished(final String schema, final String extension) {
        Runner.this.finishedSchemaGenerator(schema, extension);
      }
    });
  }

  public void finishedSchemaGenerator(final String schema, final String extension) {
    LOG.info("Runner: writing schema.");
    final InputOutput ioResult = IOProvider.getDefault().getIO("jInfer result", false);
    ioResult.getOut().println(schema);

    RunningProject.getActiveProject().getLookup().lookup(IOutputHandler.class)
            .addOutput("generated-schema", getCommentedSchema(schema), extension);

    RunningProject.removeActiveProject();
  }

  private String getCommentedSchema(final String schema) {
    return "<!-- Inferred on " + (new Date()).toString()
            + " by " + igGenerator.getModuleName() + ", "
            + simplifier.getModuleName() + ", "
            + schemaGenerator.getModuleName() + " -->\n" + schema;
  }

}
