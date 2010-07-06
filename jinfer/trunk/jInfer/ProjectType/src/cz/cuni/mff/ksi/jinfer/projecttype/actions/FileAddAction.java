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

import cz.cuni.mff.ksi.jinfer.projecttype.nodes.FileChildren;
import cz.cuni.mff.ksi.jinfer.projecttype.nodes.FolderNode;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.Node;

/**
 *
 * @author sviro
 */
public class FileAddAction extends AbstractAction {

  private static final long serialVersionUID = 12121452l;
  private final Collection<FileObject> files;
  private final Node node;

  public FileAddAction(final Node node, final Collection<FileObject> files) {
    super();
    putValue(NAME, "Add file");
    this.files = files;
    this.node = node;
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    final String type = ((FolderNode) node).getDisplayName();
    FileFilter fileFilter = null;
    if ("XML".equals(type)) {
      fileFilter = new FileNameExtensionFilter("XML files", "xml");
    } else if ("XSD".equals(type)) {
      fileFilter = new FileNameExtensionFilter("XSD files", "xsd");
    } else if ("QUERIES".equals(type)) {
      fileFilter = new FileNameExtensionFilter("QUERY files", "query");
    }

    File[] selectedFiles = new FileChooserBuilder(FileAddAction.class).
            setDefaultWorkingDirectory(new File(System.getProperty("user.home"))).
            setTitle("Add " + type + " files").setFileFilter(fileFilter).showMultiOpenDialog();

    if(selectedFiles != null) {
      for (File file : selectedFiles) {
        files.add(FileUtil.toFileObject(file));
      }

      ((FileChildren) node.getChildren()).addNotify();
    }
  }
}
