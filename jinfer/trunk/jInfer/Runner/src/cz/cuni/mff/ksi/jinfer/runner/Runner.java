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
import cz.cuni.mff.ksi.jinfer.base.interfaces.ModuleSelection;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SchemaGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Simplifier;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.runner.callbacks.IGGeneratorCallbackImpl;
import cz.cuni.mff.ksi.jinfer.runner.callbacks.SchemaGeneratorCallbackImpl;
import cz.cuni.mff.ksi.jinfer.runner.callbacks.SimplifierCallbackImpl;
import java.util.List;
import javax.swing.JOptionPane;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author rio
 */
public class Runner {

    private FileSelection fs;
    private ModuleSelection ms;
    private IGGenerator igg;
    private Simplifier s;
    private SchemaGenerator sg;

    public void run() {
        try {
            lookupFileSelection();
            lookupModuleSelection();
            lookupIGGenerator(ms.getIGGenerator());
            lookupSimplifier(ms.getSimplifier());
            lookupSchemaGenerator(ms.getSchemaGenerator());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return;
        }

        igg.start(fs.getInput(), new IGGeneratorCallbackImpl(this));
    }

    private void lookupFileSelection() throws Exception {
        Lookup lkp = Lookups.forPath("FileSelectionProviders");
        fs = lkp.lookup(FileSelection.class);
        if (fs == null) {
            throw new Exception("File selector module not found.");
        }
    }

    private void lookupModuleSelection() throws Exception {
        Lookup lkp = Lookups.forPath("ModuleSelectionProviders");
        ms = lkp.lookup(ModuleSelection.class);
        if (ms == null) {
            throw new Exception("Module selection module not found.");
        }
    }

    private void lookupIGGenerator(final String name) throws Exception {
        Lookup lkp = Lookups.forPath("IGGeneratorProviders");
        for (IGGenerator igg : lkp.lookupAll(IGGenerator.class)) {
            if (igg.getModuleName().equals(name)) {
                this.igg = igg;
                return;
            }
        }
        throw new Exception("IG generator module not found.");
    }

    private void lookupSimplifier(final String name) throws Exception {
        Lookup lkp = Lookups.forPath("SimplifierProviders");
        for (Simplifier s : lkp.lookupAll(Simplifier.class)) {
            if (s.getModuleName().equals(name)) {
                this.s = s;
                return;
            }
        }
        throw new Exception("Simplifier module not found.");
    }

    private void lookupSchemaGenerator(final String name) throws Exception {
        Lookup lkp = Lookups.forPath("SimplifierProviders");
        for (SchemaGenerator sg : lkp.lookupAll(SchemaGenerator.class)) {
            if (sg.getModuleName().equals(name)) {
                this.sg = sg;
                return;
            }
        }
        throw new Exception("Schema generator module not found.");
    }

    public void finishedIGGenerator(List<AbstractNode> grammar) {
        s.start(grammar, new SimplifierCallbackImpl(this));
    }

    public void finishedSimplifier(List<AbstractNode> grammar) {
        sg.start(grammar, new SchemaGeneratorCallbackImpl(this));
    }

    public void finishedSchemaGenerator() {
        JOptionPane.showMessageDialog(null, "Finished.");
    }
}
