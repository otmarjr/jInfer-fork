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

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.CopyOperationImplementation;
import org.netbeans.spi.project.DeleteOperationImplementation;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.support.DefaultProjectOperations;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author sviro
 */
public class JInferProject implements Project {

  public static final String OUTPUT_DIR = "output";
  private final FileObject projectDir;
  private final ProjectState state;
  private Lookup lookup;

  public JInferProject(FileObject projectDir, ProjectState state) {
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
                new JInferProjectInformation(),
                new JInferLogicalView(this),
                new ActionProviderImpl(),
                new JInferCopyOperation(this),
                new JinferDeleteOperation()
              });
    }
    return lookup;
  }

  FileObject getOutputFolder(boolean create) {
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

  private Properties loadProperties() {
    FileObject fob = projectDir.getFileObject(JInferProjectFactory.PROJECT_DIR
            + "/" + JInferProjectFactory.PROJECT_PROPFILE);
    Properties properties = new NotifyProperties(state);
    if (fob != null) {
      try {
        properties.load(fob.getInputStream());
      } catch (Exception e) {
        Exceptions.printStackTrace(e);
      }
    }
    return properties;
  }

  private Input loadInput() {
    Input input = new Input(new ArrayList<FileObject>(), new ArrayList<FileObject>(), new ArrayList<FileObject>());
    //TODO load input files from project properties
    return input;
  }

  private static class NotifyProperties extends Properties {

    private final ProjectState state;

    NotifyProperties(ProjectState state) {
      this.state = state;
    }

    @Override
    public Object put(Object key, Object val) {
      Object result = super.put(key, val);
      if (((result == null) != (val == null)) || (result != null
              && val != null && !val.equals(result))) {
        state.markModified();
      }
      return result;
    }
  }

  private final class JInferProjectInformation implements ProjectInformation {

    @Override
    public String getName() {
      return getProjectDirectory().getName();
    }

    @Override
    public String getDisplayName() {
      return getName();
    }

    @Override
    public Icon getIcon() {
      return new ImageIcon(ImageUtilities.loadImage("cz/cuni/mff/ksi/jinfer/projecttype/graphics/icon16.png"));
    }

    @Override
    public Project getProject() {
      return JInferProject.this;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener pl) {
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener pl) {
    }
  }

  private final class ActionProviderImpl implements ActionProvider {

    private String[] supported = new String[]{
      ActionProvider.COMMAND_DELETE,
      ActionProvider.COMMAND_COPY,};

    @Override
    public String[] getSupportedActions() {
      return supported;
    }

    @Override
    public void invokeAction(String action, Lookup lookup) throws IllegalArgumentException {
      if (action.equalsIgnoreCase(ActionProvider.COMMAND_DELETE)) {
        DefaultProjectOperations.performDefaultDeleteOperation(JInferProject.this);
      }
      if (action.equalsIgnoreCase(ActionProvider.COMMAND_COPY)) {
        DefaultProjectOperations.performDefaultCopyOperation(JInferProject.this);
      }
    }

    @Override
    public boolean isActionEnabled(String action, Lookup lookup) throws IllegalArgumentException {
      if ((action.equals(ActionProvider.COMMAND_DELETE))) {
        return true;
      } else if ((action.equals(ActionProvider.COMMAND_COPY))) {
        return true;
      } else {
        throw new IllegalArgumentException(action);
      }
    }
  }

  private final class JinferDeleteOperation implements DeleteOperationImplementation {

    @Override
    public void notifyDeleting() throws IOException {
    }

    @Override
    public void notifyDeleted() throws IOException {
      JInferProject.this.projectDir.delete();
      JInferProject.this.state.notifyDeleted();
    }

    @Override
    public List<FileObject> getMetadataFiles() {
      List<FileObject> dataFiles = new ArrayList<FileObject>();
      return dataFiles;
    }

    @Override
    public List<FileObject> getDataFiles() {
      List<FileObject> dataFiles = new ArrayList<FileObject>();
      return dataFiles;
    }
  }

  private final class JInferCopyOperation implements CopyOperationImplementation {

    private final JInferProject project;
    private final FileObject projectDir;

    public JInferCopyOperation(JInferProject project) {
      this.project = project;
      this.projectDir = project.getProjectDirectory();
    }

    @Override
    public List<FileObject> getMetadataFiles() {
      return Collections.EMPTY_LIST;
    }

    @Override
    public List<FileObject> getDataFiles() {
      return Collections.EMPTY_LIST;
    }

    @Override
    public void notifyCopying() throws IOException {
    }

    @Override
    public void notifyCopied(Project arg0, File arg1, String arg2) throws IOException {
    }
  }
}
