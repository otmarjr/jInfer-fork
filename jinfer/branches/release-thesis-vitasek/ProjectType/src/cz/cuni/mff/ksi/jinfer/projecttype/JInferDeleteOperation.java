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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.spi.project.DeleteOperationImplementation;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;

/**
 * Provides delete operation for jInfer project.
 * 
 * @author sviro
 */
public class JInferDeleteOperation implements DeleteOperationImplementation {

  private final JInferProject project;

  public JInferDeleteOperation(final JInferProject project) {
    this.project = project;
  }

  @Override
  public void notifyDeleting() throws IOException {
    //do nothing
  }

  @Override
  public void notifyDeleted() throws IOException {
    project.getLookup().lookup(ProjectState.class).notifyDeleted();
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
