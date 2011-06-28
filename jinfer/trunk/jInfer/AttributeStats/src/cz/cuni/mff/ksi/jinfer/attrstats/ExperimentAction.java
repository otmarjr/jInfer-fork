/*
 * Copyright (C) 2011 vektor
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
package cz.cuni.mff.ksi.jinfer.attrstats;

import cz.cuni.mff.ksi.jinfer.attrstats.experiments.sets.VariousBetas;
import cz.cuni.mff.ksi.jinfer.base.utils.AsynchronousUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Build",
id = "cz.cuni.mff.ksi.jinfer.attrstats.ExperimentAction")
@ActionRegistration(iconBase = "cz/cuni/mff/ksi/jinfer/attrstats/graphics/attr-icon-16.png",
displayName = "#CTL_ExperimentAction")
@ActionReferences({
  @ActionReference(path = "Toolbars/Build", position = 500)
})
@Messages("CTL_ExperimentAction=Experiment now")
public final class ExperimentAction implements ActionListener {

  @Override
  public void actionPerformed(final ActionEvent e) {
    AsynchronousUtils.runAsync(new Runnable() {

      @Override
      public void run() {
        new VariousBetas().run(0);
      }
    }, "Experimenting");
  }
}
