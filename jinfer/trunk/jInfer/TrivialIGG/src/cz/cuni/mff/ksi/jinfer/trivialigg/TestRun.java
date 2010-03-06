/*
 *  Copyright (C) 2010 vektor
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

package cz.cuni.mff.ksi.jinfer.trivialigg;

import cz.cuni.mff.ksi.jinfer.base.interfaces.IGGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SchemaGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SchemaGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * Example runner for TrivialIGG, should be removed when the whole framework is operational.
 *
 * @author vektor
 */
public final class TestRun implements ActionListener {

  // TODO remove this Action

  @Override
  public void actionPerformed(final ActionEvent e) {
    final IGGeneratorImpl i = new IGGeneratorImpl();
    final List<File> docs = new ArrayList<File>();
    docs.add(new File("C:\\domain.xml"));
    i.start(new Input(docs, null, null), new IGGeneratorCallback() {

      @Override
      public void finished(final List<AbstractNode> grammar) {
        final Lookup lkp = Lookups.forPath("SchemaGeneratorProviders");
        final SchemaGenerator schemagen = lkp.lookup(SchemaGenerator.class);
        schemagen.start(grammar, new SchemaGeneratorCallback() {

          @Override
          public void finished(final String schema) {
            InputOutput io = IOProvider.getDefault().getIO("jInfer", false);
            io.getOut().println(schema);
          }
        });
      }
    });
  }
}
