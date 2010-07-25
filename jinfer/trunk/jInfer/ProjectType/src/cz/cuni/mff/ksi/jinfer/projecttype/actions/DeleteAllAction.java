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
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.netbeans.spi.project.ProjectState;
import org.openide.nodes.Node;

/**
 * TODO sviro Comment!
 * @author sviro
 */
public class DeleteAllAction extends AbstractAction {

  private static final long serialVersionUID = 8956231L;
  private final JInferProject project;
  private final Node node;
  private final Collection<File> files;

  public DeleteAllAction(final JInferProject project, final Node node, final Collection<File> files) {
    super("Remove all files");
    this.project = project;
    this.node = node;
    this.files = files;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    final String folderName = ((FolderNode) node).getDisplayName();
    final int result = JOptionPane.showOptionDialog(null,
            "Are you sure you want to remove all files in " + folderName + " folder?",
            NAME, JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, null, null);
    if (result == JOptionPane.YES_OPTION) {
      files.clear();
      ((FileChildren) node.getChildren()).addNotify();

      project.getLookup().lookup(ProjectState.class).markModified();
    }
  }
}
