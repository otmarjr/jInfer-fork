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
import java.util.Collection;
import javax.swing.AbstractAction;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectState;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 * Action for {@link FolderNode} which deletes all input files in particular folder.
 *
 * @author sviro
 * @see FolderNode
 */
public class DeleteAllAction extends AbstractAction {

  private static final long serialVersionUID = 8956231L;
  private final FolderNode node;

  /**
   * Default constructor.
   * @param node Node for which is this action registered.
   */
  public DeleteAllAction(final FolderNode node) {
    super("Remove all files");
    this.node = node;
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    final String folderName = node.getDisplayName();
    final NotifyDescriptor.Confirmation descriptor = new NotifyDescriptor.Confirmation(
            "Are you sure you want to remove all files in " + folderName + " folder?");

    DialogDisplayer.getDefault().notify(descriptor);

    if (descriptor.getValue().equals(NotifyDescriptor.YES_OPTION)) {
      node.getLookup().lookup(Collection.class).clear();
      ((FileChildren) node.getChildren()).refreshNodes();
      final Project project = node.getLookup().lookup(Project.class);
      project.getLookup().lookup(ProjectState.class).markModified();
    }
  }
}
