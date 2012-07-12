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
package cz.cuni.mff.ksi.jinfer.projecttype;

import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.projecttype.actions.FilesAddAction;
import cz.cuni.mff.ksi.jinfer.projecttype.actions.RunAction;
import java.util.Arrays;
import java.util.List;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.ui.support.DefaultProjectOperations;
import org.openide.util.Lookup;

/**
 * Ability for the jInfer project to have various actions invoked on it.
 *
 * @author sviro
 */
public class ActionProviderImpl implements ActionProvider {

  private final String[] supported = new String[]{ActionProvider.COMMAND_DELETE,
    ActionProvider.COMMAND_COPY, ActionProvider.COMMAND_RUN, ActionProvider.COMMAND_RENAME,
    ActionProvider.COMMAND_MOVE, FilesAddAction.COMMAND_FILES_ADD};
  private final JInferProject project;

  public ActionProviderImpl(final JInferProject project) {
    this.project = project;
  }

  @Override
  public String[] getSupportedActions() {
    return supported;
  }

  @Override
  public void invokeAction(final String action, final Lookup lookup) throws IllegalArgumentException {
    if (action.equalsIgnoreCase(ActionProvider.COMMAND_DELETE)) {
      DefaultProjectOperations.performDefaultDeleteOperation(project);
    }
    if (action.equalsIgnoreCase(ActionProvider.COMMAND_COPY)) {
      DefaultProjectOperations.performDefaultCopyOperation(project);
    }
    if (action.equalsIgnoreCase(ActionProvider.COMMAND_RUN)) {
      new RunAction(project).actionPerformed(null);
    }
    if (action.equalsIgnoreCase(FilesAddAction.COMMAND_FILES_ADD)) {
      new FilesAddAction(project).actionPerformed(null);
    }
    if (action.equalsIgnoreCase(ActionProvider.COMMAND_RENAME)) {
      DefaultProjectOperations.performDefaultRenameOperation(project, null);
    }
    if (action.equalsIgnoreCase(ActionProvider.COMMAND_MOVE)) {
      DefaultProjectOperations.performDefaultMoveOperation(project);
    }
  }

  @Override
  public boolean isActionEnabled(final String action, final Lookup lookup) throws
          IllegalArgumentException {
    final List<String> supportedActions = Arrays.asList(supported);
    if (RunningProject.isActiveProject() && RunningProject.getActiveProject().equals(project)) {
      return false;
    } else if (supportedActions.contains(action)) {
      return true;
    } else {
      throw new IllegalArgumentException(action);
    }
  }
}
