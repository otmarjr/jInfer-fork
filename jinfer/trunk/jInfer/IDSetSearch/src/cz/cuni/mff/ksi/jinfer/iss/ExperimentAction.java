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
package cz.cuni.mff.ksi.jinfer.iss;

import cz.cuni.mff.ksi.jinfer.iss.experiments.ExperimentChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Build",
        id = "cz.cuni.mff.ksi.jinfer.iss.ExperimentAction")
@ActionRegistration(
        iconBase = "cz/cuni/mff/ksi/jinfer/iss/graphics/experiment.png",
        displayName = "#CTL_ExperimentAction")
@ActionReferences({
  @ActionReference(path = "Toolbars/Build", position = 500)
})
@Messages("CTL_ExperimentAction=Experiment now")
public final class ExperimentAction implements ActionListener {

  @Override
  public void actionPerformed(final ActionEvent e) {
    final ExperimentChooser chooser = new ExperimentChooser();
    chooser.setVisible(true);
  }
}
