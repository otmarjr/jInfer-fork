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
import cz.cuni.mff.ksi.jinfer.projecttype.properties.JInferCustomizerProvider;
import java.io.IOException;
import java.util.Properties;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 * Represents jInfer project in Projects window.
 * @author sviro
 */
public class JInferProject implements Project {

  public static final String OUTPUT_DIR = "output";
  private final FileObject projectDir;
  private final ProjectState state;
  private Lookup lookup;
  private Input input;
  private Properties properties;

  public JInferProject(final FileObject projectDir, final ProjectState state) {
    this.projectDir = projectDir;
    this.state = state;
  }

  @Override
  public FileObject getProjectDirectory() {
    return projectDir;
  }

  @Override
  public Lookup getLookup() {
    if (lookup == null) {
      lookup = Lookups.fixed(new Object[]{
                this,
                state,
                loadProperties(),
                loadInput(),
                new OutputHandlerImpl(this),
                new JInferCustomizerProvider(this),
                new JInferProjectInformation(this),
                new JInferLogicalView(this),
                new ActionProviderImpl(this),
                new JInferCopyOperation(),
                new JInferDeleteOperation(this)
              });
    }
    return lookup;
  }

  /**
   * Get FileObject of output folder. If not exist and create is true, then it's created.
   * 
   * @param create If is folder created when it not exist.
   * @return FileObject of output folder.
   */
  public FileObject getOutputFolder(final boolean create) {
    FileObject result = projectDir.getFileObject(OUTPUT_DIR);
    if (result == null && create) {
      try {
        result = projectDir.createFolder(OUTPUT_DIR);
      } catch (IOException ex) {
        Exceptions.printStackTrace(ex);
      }
    }

    return result;
  }

  /**
   * Loads jInfer project properties from Properties file in jinferproject folder.
   *
   * @return Properties loaded from properties file of project.
   */
  private Properties loadProperties() {
    if (properties == null) {
      final FileObject fob = projectDir.getFileObject(JInferProjectFactory.PROJECT_DIR
              + "/" + JInferProjectFactory.PROJECT_PROPFILE);
      properties = new NotifyProperties(state);
      if (fob != null) {
        try {
          properties.load(fob.getInputStream());
        } catch (Exception e) {
          Exceptions.printStackTrace(e);
        }
      }
    }
    return properties;
  }

  /**
   * Loads jInfer project Input file names from Input file in jinferproject folder.
   *
   * @return Model of jInfer Project input files.
   */
  private Input loadInput() {
    if (input == null) {
      final FileObject inputFilesFileOb = projectDir.getFileObject(JInferProjectFactory.PROJECT_DIR
              + "/" + JInferProjectFactory.PROJECT_INPUTFILE);

      input = new Input(new InputFilesList(), new InputFilesList(), new InputFilesList());
      if (inputFilesFileOb != null) {
        try {
          input.load(inputFilesFileOb.getInputStream());
        } catch (Exception e) {
          Exceptions.printStackTrace(e);
        }
      }
    }

    return input;
  }

  /**
   * Properties class which mark jInfer project as modified when some value is added to project properties.
   *
   */
  private static class NotifyProperties extends Properties {

    private final ProjectState state;

    NotifyProperties(final ProjectState state) {
      super();
      this.state = state;
    }

    @Override
    public Object put(final Object key, final Object val) {
      final Object result = super.put(key, val);
      if (((result == null) != (val == null)) || (result != null
              && val != null && !val.equals(result))) {
        state.markModified();
      }
      return result;
    }
  }
}
