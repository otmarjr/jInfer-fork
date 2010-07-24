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

import cz.cuni.mff.ksi.jinfer.projecttype.JInferProject;
import cz.cuni.mff.ksi.jinfer.projecttype.nodes.FileChildren;
import cz.cuni.mff.ksi.jinfer.projecttype.nodes.FolderNode;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.nodes.Node;

/**
 *
 * @author sviro
 */
public class FileAddAction extends AbstractAction {

  private static final long serialVersionUID = 12121452l;
  private final Collection<File> files;
  private final Node node;
  private final JInferProject project;
  private final String type;

  public FileAddAction(final JInferProject project, final Node node, final Collection<File> files) {
    super();
    type = ((FolderNode) node).getDisplayName();
    putValue(NAME, "Add " + type + " files");
    this.project = project;
    this.files = files;
    this.node = node;
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    FileChooserBuilder fileChooserBuilder = new FileChooserBuilder(FileAddAction.class).setDefaultWorkingDirectory(new File(System.getProperty("user.home"))).
            setTitle("Add " + type + " files");
    if ("XML".equals(type)) {
      fileChooserBuilder = fileChooserBuilder.addFileFilter(new FileNameExtensionFilter("XML files (*.xml)", "xml"));
    } else if ("schema".equals(type)) {
      fileChooserBuilder = fileChooserBuilder.addFileFilter(new FileNameExtensionFilter("Schema files (*.dtd, *.xsd)", "dtd", "xsd"));
    } else if ("query".equals(type)) {
      fileChooserBuilder = fileChooserBuilder.addFileFilter(new FileNameExtensionFilter("XPath files (*.xpath)", "xpath")).addFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));
    }

    final File[] selectedFiles = fileChooserBuilder.showMultiOpenDialog();

    if (selectedFiles != null) {
      files.addAll(Arrays.asList(selectedFiles));

      ((FileChildren) node.getChildren()).addNotify();
      
      project.getLookup().lookup(ProjectState.class).markModified();
    }
  }
}
