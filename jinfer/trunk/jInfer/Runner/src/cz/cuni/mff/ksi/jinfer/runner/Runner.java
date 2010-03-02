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

  private IGGenerator igg;
  private Simplifier s;
  private SchemaGenerator sg;

  public void run() {
    final FileSelection fs;
    final ModuleSelection ms;

    try {
      fs = lookupFileSelection();
      ms = lookupModuleSelection();
      igg = lookupIGGenerator(ms.getIGGenerator());
      s = lookupSimplifier(ms.getSimplifier());
      sg = lookupSchemaGenerator(ms.getSchemaGenerator());
      igg.start(fs.getInput(), new IGGeneratorCallbackImpl());
    } catch (RuntimeException e) {
      JOptionPane.showMessageDialog(null, e.getMessage());
    }
  }

  public void finishedIGGenerator(final List<AbstractNode> grammar) {
    s.start(grammar, new SimplifierCallbackImpl());
  }

  public void finishedSimplifier(final List<AbstractNode> grammar) {
    sg.start(grammar, new SchemaGeneratorCallbackImpl());
  }

  public void finishedSchemaGenerator() {
    JOptionPane.showMessageDialog(null, "Finished.");
  }

  private class IGGeneratorCallbackImpl implements IGGeneratorCallback {

    public void finished(final List<AbstractNode> grammar) {
      Runner.this.finishedIGGenerator(grammar);
    }
  }

  private class SimplifierCallbackImpl implements SimplifierCallback {

    public void finished(final List<AbstractNode> grammar) {
      Runner.this.finishedSimplifier(grammar);
    }
  }

  private class SchemaGeneratorCallbackImpl implements SchemaGeneratorCallback {

    public void finished() {
      Runner.this.finishedSchemaGenerator();
    }
  }

  private FileSelection lookupFileSelection() {
    final Lookup lkp = Lookups.forPath("FileSelectionProviders");
    final FileSelection fs = lkp.lookup(FileSelection.class);
    if (fs == null) {
      throw new RuntimeException("File selector module not found.");
    }
    return fs;
  }

  private ModuleSelection lookupModuleSelection() {
    final Lookup lkp = Lookups.forPath("ModuleSelectionProviders");
    final ModuleSelection ms = lkp.lookup(ModuleSelection.class);
    if (ms == null) {
      throw new RuntimeException("Module selection module not found.");
    }
    return ms;
  }

  private IGGenerator lookupIGGenerator(final String name) {
    final Lookup lkp = Lookups.forPath("IGGeneratorProviders");
    for (IGGenerator igg : lkp.lookupAll(IGGenerator.class)) {
      if (igg.getModuleName().equals(name)) {
        return igg;
      }
    }
    throw new RuntimeException("IG generator module not found.");
  }

  private Simplifier lookupSimplifier(final String name) {
    final Lookup lkp = Lookups.forPath("SimplifierProviders");
    for (Simplifier s : lkp.lookupAll(Simplifier.class)) {
      if (s.getModuleName().equals(name)) {
        return s;
      }
    }
    throw new RuntimeException("Simplifier module not found.");
  }

  private SchemaGenerator lookupSchemaGenerator(final String name) {
    final Lookup lkp = Lookups.forPath("SimplifierProviders");
    for (SchemaGenerator sg : lkp.lookupAll(SchemaGenerator.class)) {
      if (sg.getModuleName().equals(name)) {
        return sg;
      }
    }
    throw new RuntimeException("Schema generator module not found.");
  }
}
