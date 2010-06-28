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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author sviro
 */
@org.openide.util.lookup.ServiceProvider(service=ProjectFactory.class)
public class JInferProjectFactory implements ProjectFactory {

  public static final String PROJECT_DIR = "jinferproject";
  public static final String PROJECT_PROPFILE = "project.properties";

  @Override
  public boolean isProject(FileObject projectDirectory) {
    return projectDirectory.getFileObject(PROJECT_DIR) != null;
  }

  @Override
  public Project loadProject(FileObject dir, ProjectState ps) throws IOException {
    return isProject(dir) ? new JInferProject(dir, ps) : null;
  }


  @Override
  public void saveProject(final Project project) throws IOException, ClassCastException {
    FileObject projectRoot = project.getProjectDirectory();
    if (projectRoot.getFileObject(PROJECT_DIR) == null) {
        throw new IOException ("Project dir " + projectRoot.getPath() + " deleted," +
                " cannot save project");
    }

    ((JInferProject) project).getXMLFolder(true);
    ((JInferProject) project).getXSDFolder(true);
    ((JInferProject) project).getQUERYFolder(true);

    String propertiesPath = PROJECT_DIR + "/" + PROJECT_PROPFILE;
    FileObject propertiesFile = projectRoot.getFileObject(propertiesPath);
    if (propertiesFile == null) {
      propertiesFile = projectRoot.createData(propertiesPath);
    }

    Properties properties = (Properties) project.getLookup().lookup(Properties.class);
    File f = FileUtil.toFile(propertiesFile);
    properties.store(new FileOutputStream(f), "JInfer project properties");
  }

}
