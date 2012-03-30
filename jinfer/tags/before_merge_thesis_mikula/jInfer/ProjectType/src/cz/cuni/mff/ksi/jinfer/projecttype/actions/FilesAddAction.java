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

import cz.cuni.mff.ksi.jinfer.base.interfaces.Processor;
import cz.cuni.mff.ksi.jinfer.base.objects.FolderType;
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import cz.cuni.mff.ksi.jinfer.base.utils.FileUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleProperties;
import cz.cuni.mff.ksi.jinfer.projecttype.JInferProject;
import cz.cuni.mff.ksi.jinfer.projecttype.nodes.FileChildren;
import cz.cuni.mff.ksi.jinfer.projecttype.nodes.FolderNode;
import cz.cuni.mff.ksi.jinfer.projecttype.properties.ProjectPropertiesPanelProvider;
import cz.cuni.mff.ksi.jinfer.projecttype.properties.ProjectPropertiesPanel;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import javax.swing.AbstractAction;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 * Action to add files to all input folders of jInfer project.
 * @author sviro
 */
public class FilesAddAction extends AbstractAction {

  private static final long serialVersionUID = 35345345;
  private final JInferProject project;
  public static final String COMMAND_FILES_ADD = "FilesAddAction";

  /**
   * Default constructor.
   * @param project jInfer project for which is this action registered.
   */
  public FilesAddAction(final JInferProject project) {
    super("Add files");
    this.project = project;
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    final File[] selectedFiles = getSelectedFiles();

    if (selectedFiles != null) {
      // find processor mappings for all folders
      final Map<FolderType, List<String>> registeredProcessors = getRegisteredProcessors();

      final List<File> selectedFilesList = new ArrayList<File>(Arrays.asList(selectedFiles));

      final Collection<File> xmlFiles = getSpecificFiles(selectedFilesList, registeredProcessors.get(
              FolderType.DOCUMENT));
      final Collection<File> schemaFiles = getSpecificFiles(selectedFilesList, registeredProcessors.get(
              FolderType.SCHEMA));
      final Collection<File> queryFiles = getSpecificFiles(selectedFilesList, registeredProcessors.get(
              FolderType.QUERY));
      final Collection<File> fdsFiles = getSpecificFiles(selectedFilesList, registeredProcessors.get(
              FolderType.FD));

      removeKnownSelectegFiles(selectedFilesList, xmlFiles, schemaFiles, queryFiles, fdsFiles);

      if (!selectedFilesList.isEmpty()) {
        addRemainingFilesToDefault(selectedFilesList, xmlFiles,  schemaFiles, queryFiles, fdsFiles);
      }

      final Input input = project.getLookup().lookup(Input.class);
      input.getDocuments().addAll(xmlFiles);
      input.getSchemas().addAll(schemaFiles);
      input.getQueries().addAll(queryFiles);
      input.getFunctionalDependencies().addAll(fdsFiles);

      project.getLookup().lookup(ProjectState.class).markModified();

      final Node root = project.getLookup().lookup(LogicalViewProvider.class).createLogicalView();
      final Node[] nodes = root.getChildren().getNodes();

      for (int i = 0; i < nodes.length; i++) {
        final Node node = nodes[i];
        if (node instanceof FolderNode) {
          ((FileChildren) node.getChildren()).refreshNodes();
        }
      }
    }
  }

  private void addRemainingFilesToDefault(final List<File> selectedFilesList, final Collection<File> xmlFiles,  final Collection<File> schemaFiles, final Collection<File> queryFiles, final Collection<File> fdFiles) {
    final Properties properties = new ModuleProperties(ProjectPropertiesPanelProvider.CATEGORY_NAME, project.getLookup().lookup(Properties.class));
    final String defaultFolder = properties.getProperty(ProjectPropertiesPanel.FOLDER_TYPE, ProjectPropertiesPanel.FOLDER_TYPE_DEFAULT);
    if (defaultFolder.equals(FolderType.DOCUMENT.getName())) {
      xmlFiles.addAll(selectedFilesList);
    }
    if (defaultFolder.equals(FolderType.SCHEMA.getName())) {
      schemaFiles.addAll(selectedFilesList);
    }
    if (defaultFolder.equals(FolderType.QUERY.getName())) {
      queryFiles.addAll(selectedFilesList);
    }
    if (defaultFolder.equals(FolderType.FD.getName())) {
      fdFiles.addAll(selectedFilesList);
    }
    
  }

  private void removeKnownSelectegFiles(final List<File> selectedFilesList, final Collection<File> xmlFiles, final Collection<File> schemaFiles, final Collection<File> queryFiles, final Collection<File> fdFiles) {
    selectedFilesList.removeAll(xmlFiles);
    selectedFilesList.removeAll(schemaFiles);
    selectedFilesList.removeAll(queryFiles);
    selectedFilesList.removeAll(fdFiles);
  }

  private File[] getSelectedFiles() {
    final List<String> extensions = getExtensions();
    final FileFilter fileFilter = new FileNameExtensionFilter(getFileFilterDesc(extensions), extensions.toArray(new String[extensions.size()]));
    return new FileChooserBuilder(FilesAddAction.class).setDefaultWorkingDirectory(new File(System.getProperty("user.home"))).setTitle("Add files").setFileFilter(fileFilter).setFilesOnly(true).showMultiOpenDialog();
  }

  private String getFileFilterDesc(final List<String> extensions) {
    final StringBuilder builder = new StringBuilder("Files (");
    boolean first = true;
    for (String ext : extensions) {
      if (first) {
        first = false;
      } else {
        builder.append(", ");
      }
      builder.append("*.").append(ext);
    }
    builder.append(")");
    return builder.toString();
  }

  private Collection<File> getSpecificFiles(final List<File> files, final List<String> extensions) {
    final Collection<File> result = new ArrayList<File>();

    for (File file : files) {
      final String ext = FileUtils.getExtension(file.getAbsolutePath()).toLowerCase(Locale.ENGLISH);
      if (extensions.contains(ext)) {
        result.add(file);
      }
    }

    return result;
  }

  private List<String> getExtensions() {
    final ArrayList<String> result = new ArrayList<String>();
    for (Processor processor : Lookup.getDefault().lookupAll(Processor.class)) {
      result.add(processor.getExtension().toLowerCase(Locale.ENGLISH));
    }
    return result;
  }

  private Map<FolderType, List<String>> getRegisteredProcessors() {
    final Map<FolderType, List<String>> ret =
            new HashMap<FolderType, List<String>>();

    for (final FolderType ft : FolderType.values()) {
      ret.put(ft, new ArrayList<String>());
    }

    for (final Processor p : Lookup.getDefault().lookupAll(Processor.class)) {
      ret.get(p.getFolder()).add(p.getExtension().toLowerCase(Locale.ENGLISH));
    }

    return ret;
  }
}
