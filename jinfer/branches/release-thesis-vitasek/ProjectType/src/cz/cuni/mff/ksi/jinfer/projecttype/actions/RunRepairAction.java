/*
 * Copyright (C) 2011 sviro
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
package cz.cuni.mff.ksi.jinfer.projecttype.actions;

import cz.cuni.mff.ksi.jinfer.base.utils.LogLevels;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.RepairRunner;
import cz.cuni.mff.ksi.jinfer.projecttype.JInferProject;
import cz.cuni.mff.ksi.jinfer.projecttype.properties.ProjectPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.projecttype.properties.ProjectPropertiesPanelProvider;
import java.awt.event.ActionEvent;
import java.util.Properties;
import javax.swing.AbstractAction;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 *
 * @author sviro
 */
public class RunRepairAction extends AbstractAction {

  private static final long serialVersionUID = 135854187L;
  private final JInferProject project;

  public RunRepairAction(JInferProject project) {
    super("Run Repair");
    this.project = project;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (RunningProject.setActiveProject(project)) {
      final Properties properties = RunningProject.getActiveProjectProps(ProjectPropertiesPanelProvider.CATEGORY_NAME);
      final String logLevel = properties.getProperty(ProjectPropertiesPanel.LOG_LEVEL,
              LogLevels.getRootLogLevel());
      LogLevels.setRootLogLevel(logLevel);

      new RepairRunner().run();
    } else {
      DialogDisplayer.getDefault().notifyLater(new NotifyDescriptor.Message(
              org.openide.util.NbBundle.getMessage(RunAction.class,
              "RunRepairAction.runningRepair.message"),
              NotifyDescriptor.INFORMATION_MESSAGE));
    }
  }

}
