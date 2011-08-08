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

import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.lookup.ServiceProvider;

/**
 * Creates in-memory jInfer projects from disk directories.
 * @author sviro
 */
@ServiceProvider(service = ProjectFactory.class)
public class JInferProjectFactory implements ProjectFactory {

  /**
   * Name of directory containing important files for jInfer project.
   */
  public static final String PROJECT_DIR = "jinferproject";
  /**
   * Name of properties file for jInfer project.
   */
  public static final String PROJECT_PROPFILE = "project.properties";
  /**
   * Name of file for storing paths to Input files for jInfer project.
   */
  public static final String PROJECT_INPUTFILE = "input.files";

  @Override
  public boolean isProject(final FileObject projectDirectory) {
    return projectDirectory.getFileObject(PROJECT_DIR) != null && projectDirectory.getFileObject(
            PROJECT_DIR).isFolder();
  }

  @Override
  public Project loadProject(final FileObject dir, final ProjectState ps) throws IOException {
    return isProject(dir) ? new JInferProject(dir, ps) : null;
  }

  @Override
  public void saveProject(final Project project) throws IOException, ClassCastException {
    final FileObject projectRoot = project.getProjectDirectory();
    if (projectRoot.getFileObject(PROJECT_DIR) == null) {
      throw new IOException("Project dir " + projectRoot.getPath() + " deleted,"
              + " cannot save project");
    }

    ((JInferProject) project).getOutputFolder(true);

    final String propertiesPath = PROJECT_DIR + "/" + PROJECT_PROPFILE;
    FileObject propertiesFile = projectRoot.getFileObject(propertiesPath);
    if (propertiesFile != null && propertiesFile.isFolder()) {
      propertiesFile.delete();
      propertiesFile = null;
    }

    if (propertiesFile == null) {
      propertiesFile = projectRoot.getFileObject(PROJECT_DIR).createData(PROJECT_PROPFILE);
    }

    final Properties properties = project.getLookup().lookup(Properties.class);
    File f = FileUtil.toFile(propertiesFile);
    properties.store(new FileOutputStream(f), "JInfer project properties");

    final String inputFilesPath = PROJECT_DIR + "/" + PROJECT_INPUTFILE;
    FileObject inputFilesFile = projectRoot.getFileObject(inputFilesPath);
    if (inputFilesFile != null && inputFilesFile.isFolder()) {
      inputFilesFile.delete();
      inputFilesFile = null;
    }

    if (inputFilesFile == null) {
      inputFilesFile = projectRoot.getFileObject(PROJECT_DIR).createData(PROJECT_INPUTFILE);
    }

    final Input input = project.getLookup().lookup(Input.class);
    f = FileUtil.toFile(inputFilesFile);
    InputFiles.store(new FileOutputStream(f), input, FileUtil.toFile(projectRoot));
  }
}
