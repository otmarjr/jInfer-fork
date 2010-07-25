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

import cz.cuni.mff.ksi.jinfer.base.interfaces.FileSelection;
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import cz.cuni.mff.ksi.jinfer.projecttype.actions.RunAction;
import cz.cuni.mff.ksi.jinfer.projecttype.properties.JInferCustomizerProvider;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.actions.Openable;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.CopyOperationImplementation;
import org.netbeans.spi.project.DeleteOperationImplementation;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.support.DefaultProjectOperations;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 * TODO sviro Comment!
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
                new OutputHandler(),
                new JInferCustomizerProvider(this),
                new JInferProjectInformation(),
                new JInferLogicalView(this),
                new ActionProviderImpl(),
                new JInferCopyOperation(),
                new JinferDeleteOperation()
              });
    }
    return lookup;
  }

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

  private Input loadInput() {
    if (input == null) {
      final FileObject inputFilesFileOb = projectDir.getFileObject(JInferProjectFactory.PROJECT_DIR
            + "/" + JInferProjectFactory.PROJECT_INPUTFILE);

      input = new Input();
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

  // TODO sviro Comment! & move to the top level
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
    public void addPropertyChangeListener(final PropertyChangeListener pl) {
      //do nothing
    }

    @Override
    public void removePropertyChangeListener(final PropertyChangeListener pl) {
      //do nothing
    }
  }

  // TODO sviro Comment! & move to the top level
  private final class ActionProviderImpl implements ActionProvider {

    private final String[] supported = new String[]{
      ActionProvider.COMMAND_DELETE,
      ActionProvider.COMMAND_COPY,
      ActionProvider.COMMAND_RUN};

    @Override
    public String[] getSupportedActions() {
      return supported;
    }

    @Override
    public void invokeAction(final String action, final Lookup lookup) throws IllegalArgumentException {
      if (action.equalsIgnoreCase(ActionProvider.COMMAND_DELETE)) {
        DefaultProjectOperations.performDefaultDeleteOperation(JInferProject.this);
      }
      if (action.equalsIgnoreCase(ActionProvider.COMMAND_COPY)) {
        DefaultProjectOperations.performDefaultCopyOperation(JInferProject.this);
      }
      if (action.equalsIgnoreCase(ActionProvider.COMMAND_RUN)) {
        new RunAction(JInferProject.this).actionPerformed(null);
      }
    }

    @Override
    public boolean isActionEnabled(final String action, final Lookup lookup) throws IllegalArgumentException {
      if ((action.equals(ActionProvider.COMMAND_DELETE)) 
              || (action.equals(ActionProvider.COMMAND_COPY))
              || (action.equals(ActionProvider.COMMAND_RUN))) {
        return true;
      } else {
        throw new IllegalArgumentException(action);
      }
    }
  }

  // TODO sviro Comment! & move to the top level
  private final class JinferDeleteOperation implements DeleteOperationImplementation {

    @Override
    public void notifyDeleting() throws IOException {
      //do nothing
    }

    @Override
    public void notifyDeleted() throws IOException {
      JInferProject.this.projectDir.delete();
      JInferProject.this.state.notifyDeleted();
    }

    @Override
    public List<FileObject> getMetadataFiles() {
      return new ArrayList<FileObject>();
    }

    @Override
    public List<FileObject> getDataFiles() {
      return new ArrayList<FileObject>();
    }
  }

  // TODO sviro Comment! & move to the top level
  private final class JInferCopyOperation implements CopyOperationImplementation {

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
      //do nothing
    }

    @Override
    public void notifyCopied(final Project arg0, final File arg1, final String arg2) throws IOException {
      //do nothing
    }
  }

  // TODO sviro Comment! & move to the top level
  private final class OutputHandler implements FileSelection{

    @Override
    public void addOutput(final String name, final String data, final String extension) {
      try {
        final FileObject outputFolder = getOutputFolder(true);

        int min = 1;

        while (true) {
          if (outputFolder.getFileObject(name + min, extension) == null) {
            break;
          }
          min++;
        }

        final FileObject output = outputFolder.createData(name + min, extension);
        final OutputStream out = output.getOutputStream();
        out.write(data.getBytes());
        out.flush();
        out.close();
        DataObject.find(output).getLookup().lookup(Openable.class).open();
        outputFolder.refresh();
      } catch (IOException ex) {
        Exceptions.printStackTrace(ex);
      }
    }

    @Override
    public Input getInput() {
      return null;
    }
  }
}
