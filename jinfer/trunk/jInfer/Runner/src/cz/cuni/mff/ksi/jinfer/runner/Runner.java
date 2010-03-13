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
import java.util.List;
import javax.swing.JOptionPane;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
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

  public void run() {
    final FileSelection fileSelection = lookupFileSelection();

    igGenerator.start(fileSelection.getInput(), new IGGeneratorCallback() {

      public void finished(final List<AbstractNode> grammar) {
        Runner.this.finishedIGGenerator(grammar);
      }
    });
  }

  public void finishedIGGenerator(final List<AbstractNode> grammar) {
    simplifier.start(grammar, new SimplifierCallback() {

      public void finished(final List<AbstractNode> grammar) {
        Runner.this.finishedSimplifier(grammar);
      }
    });
  }

  public void finishedSimplifier(final List<AbstractNode> grammar) {
    schemaGenerator.start(grammar, new SchemaGeneratorCallback() {

      public void finished(final String schema) {
        Runner.this.finishedSchemaGenerator(schema);
      }
    });
  }

  public void finishedSchemaGenerator(final String schema) {
    // TODO process result schema
    JOptionPane.showMessageDialog(null, "Finished.");
  }

  private FileSelection lookupFileSelection() {
    final Lookup lkp = Lookups.forPath("FileSelectionProviders");
    final FileSelection fileSelection = lkp.lookup(FileSelection.class);
    if (fileSelection == null) {
      throw new MissingModuleException("File selector module not found.");
    }
    return fileSelection;
  }

  private ModuleSelection lookupModuleSelection() {
    final Lookup lkp = Lookups.forPath("ModuleSelectionProviders");
    final ModuleSelection moduleSelection = lkp.lookup(ModuleSelection.class);
    if (moduleSelection == null) {
      throw new MissingModuleException("Module selection module not found.");
    }
    return moduleSelection;
  }

  private IGGenerator lookupIGGenerator(final String name) {
    final Lookup lkp = Lookups.forPath("IGGeneratorProviders");
    for (final IGGenerator igGenerator : lkp.lookupAll(IGGenerator.class)) {
      if (igGenerator.getModuleName().equals(name)) {
        return igGenerator;
      }
    }
    throw new MissingModuleException("IG generator module not found.");
  }

  private Simplifier lookupSimplifier(final String name) {
    final Lookup lkp = Lookups.forPath("SimplifierProviders");
    for (final Simplifier simplifier : lkp.lookupAll(Simplifier.class)) {
      if (simplifier.getModuleName().equals(name)) {
        return simplifier;
      }
    }
    throw new MissingModuleException("Simplifier module not found.");
  }

  private SchemaGenerator lookupSchemaGenerator(final String name) {
    final Lookup lkp = Lookups.forPath("SchemaGeneratorProviders");
    for (final SchemaGenerator schemaGenerator : lkp.lookupAll(SchemaGenerator.class)) {
      if (schemaGenerator.getModuleName().equals(name)) {
        return schemaGenerator;
      }
    }
    throw new MissingModuleException("Schema generator module not found.");
  }
}
