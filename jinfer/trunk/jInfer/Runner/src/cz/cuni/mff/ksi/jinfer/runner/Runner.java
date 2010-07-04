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

import cz.cuni.mff.ksi.jinfer.base.interfaces.FileSelection;
import cz.cuni.mff.ksi.jinfer.base.interfaces.IGGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.IGGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.interfaces.ModuleSelection;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SchemaGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SchemaGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Simplifier;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SimplifierCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import java.util.Date;
import java.util.List;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Lookup;
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

  public Runner() {
    final ModuleSelection moduleSelection = lookupModuleSelection();

    igGenerator = lookupIGGenerator(moduleSelection.getIGGenerator());
    simplifier = lookupSimplifier(moduleSelection.getSimplifier());
    schemaGenerator = lookupSchemaGenerator(moduleSelection.getSchemaGenerator());
  }

  /**
   * Starts process of inference.
   */
  public void run() {
    final FileSelection fileSelection = lookupFileSelection();

    igGenerator.start(fileSelection.getInput(), new IGGeneratorCallback() {

      @Override
      public void finished(final List<AbstractNode> grammar) {
        Runner.this.finishedIGGenerator(grammar);
      }
    });
  }

  public void finishedIGGenerator(final List<AbstractNode> grammar) {
    final InputOutput io = IOProvider.getDefault().getIO("jInfer", false);
    io.getOut().println("Runner: initial grammar contains " + grammar.size()
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
      public void taskFinished(org.openide.util.Task task) {
        handle.finish();
      }
    });

    handle.start();

    theTask.schedule(0);
  }

  public void finishedSimplifier(final List<AbstractNode> grammar) {
    final InputOutput io = IOProvider.getDefault().getIO("jInfer", false);
    io.getOut().println("Runner: simplified grammar contains " + grammar.size()
            + " rules.");
    schemaGenerator.start(grammar, new SchemaGeneratorCallback() {

      @Override
      public void finished(final String schema) {
        Runner.this.finishedSchemaGenerator(schema);
      }
    });
  }

  public void finishedSchemaGenerator(final String schema) {
    final InputOutput io = IOProvider.getDefault().getIO("jInfer", false);
    io.getOut().println("Runner: writing schema.");
    final InputOutput ioResult = IOProvider.getDefault().getIO("jInfer result", false);
    ioResult.getOut().println(schema);

    final FileSelection fileSelection = lookupFileSelection();
    fileSelection.addOutput(
            (new Date()).toString()
            , getCommentedSchema(schema), "dtd");
  }

  private String getCommentedSchema(final String schema) {
    return "<!-- Inferred on " + (new Date()).toString()
            + " by " + igGenerator.getModuleName() + ", "
            + simplifier.getModuleName() + ", "
            + schemaGenerator.getModuleName() + " -->\n" + schema;
  }

  private FileSelection lookupFileSelection() {
    final FileSelection fileSelection = Lookup.getDefault().lookup(FileSelection.class);
    if (fileSelection == null) {
      throw new MissingModuleException("File selector module not found.");
    }
    return fileSelection;
  }

  private ModuleSelection lookupModuleSelection() {
    final ModuleSelection moduleSelection = Lookup.getDefault().lookup(ModuleSelection.class);
    if (moduleSelection == null) {
      throw new MissingModuleException("Module selection module not found.");
    }
    return moduleSelection;
  }

  private IGGenerator lookupIGGenerator(final String name) {
    final Lookup lkp = Lookup.getDefault();
    for (final IGGenerator igGenerator : lkp.lookupAll(IGGenerator.class)) {
      if (igGenerator.getModuleName().equals(name)) {
        return igGenerator;
      }
    }
    throw new MissingModuleException("IG generator module not found.");
  }

  private Simplifier lookupSimplifier(final String name) {
    final Lookup lkp = Lookup.getDefault();
    for (final Simplifier simplifier : lkp.lookupAll(Simplifier.class)) {
      if (simplifier.getModuleName().equals(name)) {
        return simplifier;
      }
    }
    throw new MissingModuleException("Simplifier module not found.");
  }

  private SchemaGenerator lookupSchemaGenerator(final String name) {
    final Lookup lkp = Lookup.getDefault();
    for (final SchemaGenerator schemaGenerator : lkp.lookupAll(SchemaGenerator.class)) {
      if (schemaGenerator.getModuleName().equals(name)) {
        return schemaGenerator;
      }
    }
    throw new MissingModuleException("Schema generator module not found.");
  }
}
