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

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.List;
import cz.cuni.mff.ksi.jinfer.base.interfaces.OutputHandler;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.*;
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.runner.properties.ModuleSelectionPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.runner.options.RunnerPanel;
import java.util.Date;
import java.util.EnumSet;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.NbPreferences;
import org.openide.windows.IOProvider;
import org.openide.windows.IOSelect;
import org.openide.windows.InputOutput;
import static cz.cuni.mff.ksi.jinfer.base.utils.AsynchronousUtils.runAsync;

/**
 * Runner is responsible for running other modules in process of inference,
 * ensuring correct order and passing data from output of a module
 * to input of another.
 *
 * <p>
 *  Note that a constructed instance of Runner remembers the modules selected
 *  for that specific inference run: every Runner should be used for one
 *  inference only!
 * </p>
 *
 * @author rio
 */
public class Runner {

  private static final Logger LOG = Logger.getLogger(Runner.class);
  private final IGGenerator igGenerator;
  private final Simplifier simplifier;
  private final NonGrammaticalInputProcessor nonGrammaticalInputProcessor;
  private final SchemaGenerator schemaGenerator;
  private final IGGeneratorCallback iggCallback = new IGGeneratorCallback() {

    @Override
    public void finished(final List<Element> grammar) {
      Runner.this.finishedIGGenerator(grammar);
    }
  };
  private final SimplifierCallback simplCallback = new SimplifierCallback() {

    @Override
    public void finished(final List<Element> grammar) {
      Runner.this.finishedSimplifier(grammar);
    }
  };
  private final NonGrammaticalInputProcessorCallback nonGrammticalInputProcessorCallback = new NonGrammaticalInputProcessorCallback() {

    @Override
    public void finished(final List<Element> grammar) {
      Runner.this.finishedNonGrammaticalInputProcessor(grammar);
    }
  };
  private final SchemaGeneratorCallback sgCallback = new SchemaGeneratorCallback() {

    @Override
    public void finished(final String schema, final String extension) {
      Runner.this.finishedSchemaGenerator(schema, extension);
    }
  };

  /**
   * Default, nonparametric constructor. Looks up all the inference modules.
   */
  public Runner() {
    final Properties projectProperties = RunningProject.getActiveProjectProps(ModuleSelectionPropertiesPanel.NAME);

    igGenerator = ModuleSelectionHelper.lookupImpl(IGGenerator.class,
            projectProperties.getProperty(ModuleSelectionPropertiesPanel.IGG_PROP));
    simplifier = ModuleSelectionHelper.lookupImpl(Simplifier.class,
            projectProperties.getProperty(ModuleSelectionPropertiesPanel.SIMPLIFIER_PROP));
    schemaGenerator = ModuleSelectionHelper.lookupImpl(SchemaGenerator.class,
            projectProperties.getProperty(ModuleSelectionPropertiesPanel.SCHEMAGEN_PROP, ModuleSelectionPropertiesPanel.SCHEMAGEN_DEFAULT));
    nonGrammaticalInputProcessor = ModuleSelectionHelper.lookupImpl(NonGrammaticalInputProcessor.class,
            projectProperties.getProperty(ModuleSelectionPropertiesPanel.NON_GRAMMATICAL_INPUT_PROCESSOR_PROP, ModuleSelectionPropertiesPanel.NON_GRAMMATICAL_INPUT_PROCESSOR_DEFAULT));
  }

  /**
   * Starts process of inference. Runs the first module and passes it the
   * reference to a callback to run when it finishes.
   */
  public void run() {
    runAsync(new Runnable() {

      @Override
      public void run() {
        try {
          RunningProject.setNextModuleCaps(simplifier);
          igGenerator.start(RunningProject.getActiveProject().getLookup().lookup(Input.class),
                  iggCallback);
        } catch (final InterruptedException e) {
          interrupted();
        }
        catch (final Throwable t) {
          unexpected(t);
        }
      }
    }, "Retrieving IG");
  }

  private void finishedIGGenerator(final List<Element> grammar) {
    LOG.info("Runner: initial grammar contains " + grammar.size()
            + " rules.");

    runAsync(new Runnable() {

      @Override
      public void run() {
        try {
          RunningProject.setNextModuleCaps(schemaGenerator);
          simplifier.start(grammar, simplCallback);
        } catch (final InterruptedException e) {
          interrupted();
        } catch (final Throwable t) {
          unexpected(t);
        }
      }
    }, "Inferring the schema");
  }

  private void finishedSimplifier(final List<Element> grammar) {
    LOG.info("Runner: simplified grammar contains " + grammar.size()
            + " rules.");

    runAsync(new Runnable() {

      @Override
      public void run() {
        try {
          RunningProject.setNextModuleCaps(null);
          nonGrammaticalInputProcessor.start(RunningProject.getActiveProject().getLookup().lookup(Input.class), grammar, nonGrammticalInputProcessorCallback);
        } catch (final InterruptedException e) {
          interrupted();
        } catch (final Throwable t) {
          unexpected(t);
        }
      }
    }, "Generating result schema");
  }
  
  private void finishedNonGrammaticalInputProcessor(final List<Element> grammar) {
    runAsync(new Runnable() {

      @Override
      public void run() {
        try {
          RunningProject.setNextModuleCaps(null);
          schemaGenerator.start(grammar, sgCallback);
        } catch (final InterruptedException e) {
          interrupted();
        } catch (final Throwable t) {
          unexpected(t);
        }
      }
    }, "Analyzing XQuery queries");
  }

  private void finishedSchemaGenerator(final String schema, final String extension) {
    LOG.info("Runner: writing schema.");
    final String commentedSchema = getCommentedSchema(schema);

    final boolean openSchema = NbPreferences.forModule(RunnerPanel.class).getBoolean(
            RunnerPanel.SCHEMA_OPEN_PROP, RunnerPanel.SCHEMA_OPEN_DEFAULT);

    final String namePattern = NbPreferences.forModule(RunnerPanel.class).get(RunnerPanel.NAME_PATTERN_PROP, RunnerPanel.NAME_PATTERN_DEFAULT);

    RunningProject.getActiveProject().getLookup().lookup(OutputHandler.class).addOutput(
            namePattern, commentedSchema, extension, openSchema);

    final boolean showOutput = NbPreferences.forModule(RunnerPanel.class).getBoolean(
            RunnerPanel.OUTPUT_SHOW_PROP, RunnerPanel.OUTPUT_SHOW_DEFAULT);

    final InputOutput ioResult = IOProvider.getDefault().getIO("jInfer result", true);
    ioResult.getOut().println(commentedSchema);

    if (showOutput) {
      IOSelect.select(ioResult, EnumSet.allOf(IOSelect.AdditionalOperation.class));
    }
    ioResult.getOut().close();


    RunningProject.removeActiveProject();
    LOG.info("------------- DONE -------------");
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

  private String getCommentedSchema(final String schema) {
    return schema.replace("%generated%",
            "Inferred on " + (new Date()).toString()
            + " by " + igGenerator.getModuleDescription() + ", "
            + simplifier.getModuleDescription() + ", "
            + schemaGenerator.getModuleDescription());
  }
}
