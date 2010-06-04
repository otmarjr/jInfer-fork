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

import cz.cuni.mff.ksi.jinfer.fileselector.nodes.FolderNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import org.openide.nodes.Node;
import org.openide.windows.WindowManager;

public final class AddXmlAction implements ActionListener {

  @Override
  public void actionPerformed(ActionEvent e) {
    final Node[] nodes = ((FileSelectorTopComponent) WindowManager.getDefault().findTopComponent("FileSelectorTopComponent")).getExplorerManager().getRootContext().getChildren().getNodes();
    for (Node node : nodes) {
      if (((FolderNode) node).getFolderName().equals("XML")) {
        final Action[] actions = node.getActions(false);
        for (Action action : actions) {
          if (action instanceof FileAddAction) {
            action.actionPerformed(null);
          }
        }
      }
    }
  }
}
