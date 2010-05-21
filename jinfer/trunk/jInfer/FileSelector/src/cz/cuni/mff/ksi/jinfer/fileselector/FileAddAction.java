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
package cz.cuni.mff.ksi.jinfer.fileselector;

import cz.cuni.mff.ksi.jinfer.fileselector.nodes.FileChildren;
import cz.cuni.mff.ksi.jinfer.fileselector.nodes.FolderNode;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.openide.nodes.Node;
/**
 *
 * @author sviro
 */
public class FileAddAction extends AbstractAction {

  private static final long serialVersionUID = 12121452l;
  private final Collection<File> files;
  private final Node node;

  public FileAddAction(final Node node, final Collection<File> files) {
    super();
    putValue(NAME, "Add file");
    this.files = files;
    this.node = node;
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    final JFileChooser fc = new JFileChooser();

    final String type = ((FolderNode) node).getFolderName();
    FileFilter fileFilter = null;
    if ("XML".equals(type)) {
      fileFilter = new FileNameExtensionFilter("XML files", "xml");
    } else if ("XSD".equals(type)) {
      fileFilter = new FileNameExtensionFilter("XSD files", "xsd");
    } else if ("QUERIES".equals(type)) {
      fileFilter = new FileNameExtensionFilter("QUERY files", "query");
    }

    fc.setFileFilter(fileFilter);

    final int retVal = fc.showOpenDialog(null);
    if (retVal == JFileChooser.APPROVE_OPTION) {
      final File file = fc.getSelectedFile();
      files.add(file);

      ((FileChildren) node.getChildren()).addNotify();

      ((FolderNode) node).getTopComponent().storeInput();
    }
  }
}
