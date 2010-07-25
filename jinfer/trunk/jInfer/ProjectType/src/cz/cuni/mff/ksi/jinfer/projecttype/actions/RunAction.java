/*
 *  Copyright (C) 2010 sviro
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
package cz.cuni.mff.ksi.jinfer.projecttype.actions;

import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.projecttype.JInferProject;
import cz.cuni.mff.ksi.jinfer.runner.Runner;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 * TODO sviro Comment!
 * @author sviro
 */
public class RunAction extends AbstractAction {

  private static final long serialVersionUID = 135854187L;
  private final JInferProject project;

  public RunAction(final JInferProject project) {
    super("Run");
    this.project = project;
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    if (RunningProject.setActiveProject(project)) {
      new Runner().run();
    } else {
      //TODO treba oznamit ze inferencia bezi uz niekde inde
    }

  }
}
