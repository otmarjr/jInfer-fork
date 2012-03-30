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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.MoveOrRenameOperationImplementation;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.filesystems.FileObject;
import org.openide.nodes.Node;

/**
 * Provides move or remove operation for jInfer project.
 * @author sviro
 */
public class JInferMoveOrRenameOperation implements MoveOrRenameOperationImplementation {

  private final JInferProject project;

  public JInferMoveOrRenameOperation(final JInferProject project) {
    this.project = project;

  }

  @Override
  public void notifyRenaming() throws IOException {
    //do nothing
  }

  @Override
  public void notifyRenamed(final String newName) throws IOException {
    moveOrRename(newName);
  }

  @Override
  public void notifyMoving() throws IOException {
    //do nothing
  }

  @Override
  public void notifyMoved(final Project original, final File originalPath, final String newName)
          throws IOException {
    moveOrRename(newName);
  }

  private void moveOrRename(final String newName) {
    project.getLookup().lookup(Properties.class).setProperty(
            JInferProject.JINFER_PROJECT_NAME_PROPERTY, newName);
    project.getLookup().lookup(ProjectState.class).markModified();
    final Node rootNode = project.getLookup().
            lookup(LogicalViewProvider.class).createLogicalView();
    rootNode.setDisplayName(newName);
  }

  @Override
  public List<FileObject> getMetadataFiles() {
    return new ArrayList<FileObject>();
  }

  @Override
  public List<FileObject> getDataFiles() {
    final List<FileObject> result = new ArrayList<FileObject>();
    result.add(project.getProjectDirectory());
    return result;
  }
}
