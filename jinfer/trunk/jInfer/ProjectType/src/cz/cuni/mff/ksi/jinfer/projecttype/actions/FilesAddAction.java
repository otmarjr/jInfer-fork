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
import cz.cuni.mff.ksi.jinfer.projecttype.JInferProject;
import cz.cuni.mff.ksi.jinfer.projecttype.nodes.FileChildren;
import cz.cuni.mff.ksi.jinfer.projecttype.nodes.FolderNode;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 * Action to add files to all input folders of jInfer project.
 * @author sviro
 */
public class FilesAddAction extends AbstractAction {

  private final JInferProject project;
  public static final String COMMAND_FILES_ADD = "FilesAddAction";

  public FilesAddAction(final JInferProject project) {
    super("Add files");
    this.project = project;
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    final List<String> extensions = getExtensions();

    final StringBuilder builder = new StringBuilder("Files (");
    boolean first = true;
    for (String ext : extensions) {
      if (first) {
        first = false;
        builder.append("*.").append(ext);
      } else {
        builder.append(", *.").append(ext);
      }
    }
    builder.append(")");

    final FileFilter fileFilter = new FileNameExtensionFilter(builder.toString(),
            extensions.toArray(new String[extensions.size()]));

    final File[] selectedFiles = new FileChooserBuilder(FilesAddAction.class).
            setDefaultWorkingDirectory(new File(System.getProperty("user.home"))).
            setTitle("Add files").setFileFilter(fileFilter).setFilesOnly(true).showMultiOpenDialog();

    if (selectedFiles != null) {
      // find processor mappings for all folders
      final Map<FolderType, List<String>> registeredProcessors = getRegisteredProcessors();


      final Collection<File> xmlFiles = getSpecificFiles(selectedFiles, registeredProcessors.get(
              FolderType.XML));
      final Collection<File> schemaFiles = getSpecificFiles(selectedFiles, registeredProcessors.get(
              FolderType.SCHEMA));
      final Collection<File> queryFiles = getSpecificFiles(selectedFiles, registeredProcessors.get(
              FolderType.QUERY));

      final Input input = project.getLookup().lookup(Input.class);
      input.getDocuments().addAll(xmlFiles);
      input.getSchemas().addAll(schemaFiles);
      input.getQueries().addAll(queryFiles);

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

  private Collection<File> getSpecificFiles(final File[] files, final List<String> extensions) {
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
