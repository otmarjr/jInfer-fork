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

import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import cz.cuni.mff.ksi.jinfer.projecttype.JInferProject;
import cz.cuni.mff.ksi.jinfer.projecttype.nodes.FileChildren;
import cz.cuni.mff.ksi.jinfer.projecttype.nodes.FolderNode;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.Node;

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
    final FileFilter fileFilter = new FileNameExtensionFilter("Files (*.xml, *.xsd, *.dtd, *.xpath)",
            "xml", "xsd", "dtd", "xpath");

    final File[] selectedFiles = new FileChooserBuilder(FilesAddAction.class).
            setDefaultWorkingDirectory(new File(System.getProperty("user.home"))).
            setTitle("Add files").setFileFilter(fileFilter).setFilesOnly(true).showMultiOpenDialog();

    if (selectedFiles != null) {
      final Collection<File> xmlFiles = getSpecificFiles(selectedFiles, "xml");
      final Collection<File> schemaFiles = getSpecificFiles(selectedFiles, "xsd", "dtd");
      final Collection<File> queryFiles = getSpecificFiles(selectedFiles, "xpath");

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

  private Collection<File> getSpecificFiles(final File[] files, final String... extensions) {
    final Collection<File> result = new ArrayList<File>();

    for (int i = 0; i < files.length; i++) {
      File file = files[i];
      for (String ext : extensions) {
        if (ext.equalsIgnoreCase(FileUtil.toFileObject(file).getExt())) {
          result.add(file);
        }
      }
    }
    return result;
  }
}
