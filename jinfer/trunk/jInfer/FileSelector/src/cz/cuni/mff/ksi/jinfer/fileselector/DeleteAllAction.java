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
import javax.swing.JOptionPane;
import org.openide.nodes.Node;

/**
 *
 * @author sviro
 */
public class DeleteAllAction extends AbstractAction {

  private final Collection<File> files;
  private final Node node;

  public DeleteAllAction(final Node node, final Collection<File> files) {
    super();
    putValue(NAME, "Delete all files");
    this.files = files;
    this.node = node;
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    final String folderName = ((FolderNode) node).getDisplayName();
    final int result = JOptionPane.showOptionDialog(null, "Are you sure you want to delete all files in " + folderName + " folder?", NAME, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
    if (result == JOptionPane.YES_OPTION) {
      files.clear();
      ((FileChildren) node.getChildren()).addNotify();
    }
  }
}
