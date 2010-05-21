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
import javax.swing.AbstractAction;
import org.openide.explorer.ExplorerManager;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataNode;

/**
 *
 * @author sviro
 */
public class FileDeleteAction extends AbstractAction {

  private final FileSelectorTopComponent topComponent;

  FileDeleteAction(final FileSelectorTopComponent topComponent) {
    super();
    this.topComponent = topComponent;
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    final ExplorerManager em = topComponent.getExplorerManager();
    final String parentName = ((FolderNode)em.getExploredContext()).getFolderName();
    final DataNode dataNode = (DataNode) em.getSelectedNodes()[0];

    final File delFile = FileUtil.toFile(dataNode.getDataObject().getPrimaryFile());
    if ("XML".equals(parentName)) {
      topComponent.getInput().getDocuments().remove(delFile);
    } else if ("XSD".equals(parentName)) {
      topComponent.getInput().getSchemas().remove(delFile);
    } else if ("QUERY".equals(parentName)) {
      topComponent.getInput().getQueries().remove(delFile);
    } else {
      throw new IllegalArgumentException(parentName);
    }
    topComponent.storeInput();

    ((FileChildren) dataNode.getParentNode().getChildren()).addNotify();
  }
}
