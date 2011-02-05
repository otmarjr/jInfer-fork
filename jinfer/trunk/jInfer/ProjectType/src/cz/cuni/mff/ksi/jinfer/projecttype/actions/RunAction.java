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

import cz.cuni.mff.ksi.jinfer.base.utils.LogLevels;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.projecttype.JInferProject;
import cz.cuni.mff.ksi.jinfer.projecttype.properties.ProjectPropertiesPanelProvider;
import cz.cuni.mff.ksi.jinfer.projecttype.properties.ProjectPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.runner.Runner;
import java.awt.event.ActionEvent;
import java.util.Properties;
import javax.swing.AbstractAction;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 * Action that runs the inference.
 * @author sviro
 */
public class RunAction extends AbstractAction {

  private static final long serialVersionUID = 135854187L;
  private final JInferProject project;

    /**
   * Default constructor.
   * @param project jInfer project for which is this action registered.
   */
  public RunAction(final JInferProject project) {
    super("Run");
    this.project = project;
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    if (RunningProject.setActiveProject(project)) {
      final Properties properties = RunningProject.getActiveProjectProps(ProjectPropertiesPanelProvider.CATEGORY_NAME);
      final String logLevel = properties.getProperty(ProjectPropertiesPanel.LOG_LEVEL,
              LogLevels.getRootLogLevel());
      LogLevels.setRootLogLevel(logLevel);

      new Runner().run();
    } else {
      DialogDisplayer.getDefault().notifyLater(new NotifyDescriptor.Message(
              org.openide.util.NbBundle.getMessage(RunAction.class,
              "RunAction.runningInference.message"),
              NotifyDescriptor.INFORMATION_MESSAGE));
    }

  }
}
