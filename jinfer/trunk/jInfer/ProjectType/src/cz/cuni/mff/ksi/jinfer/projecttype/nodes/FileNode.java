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
package cz.cuni.mff.ksi.jinfer.projecttype.nodes;

import cz.cuni.mff.ksi.jinfer.projecttype.actions.IDSetSearchAction;
import cz.cuni.mff.ksi.jinfer.projecttype.actions.GraphRepAction;
import cz.cuni.mff.ksi.jinfer.projecttype.actions.ValidateAction;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import javax.swing.Action;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataNode;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;

/**
 * Node which encapsulates input Files in folder node. For this node is disabled copy and cut.
 * @author sviro
 * @see FolderNode
 */
public class FileNode extends FilterNode {

  public FileNode(final Node node) {
    super(node, Children.LEAF, node.getLookup());
    disableDelegation(DELEGATE_DESTROY);
  }

  @Override
  public boolean canCopy() {
    return false;
  }

  @Override
  public boolean canCut() {
    return false;
  }

  @Override
  public boolean canRename() {
    return false;
  }



  @Override
  public void destroy() throws IOException {
    final FolderNode folderNode = (FolderNode) this.getParentNode();
    @SuppressWarnings("unchecked")
    final Collection<File> files = (Collection<File>) folderNode.getLookup().
            lookup(Collection.class);
    final DataNode orig = (DataNode) this.getOriginal();
    final FileObject fileOb = orig.getDataObject().getPrimaryFile();
    files.remove(FileUtil.toFile(fileOb));

    folderNode.getLookup().lookup(Project.class).getLookup().lookup(ProjectState.class).markModified();
    ((FileChildren) folderNode.getChildren()).refreshNodes();

    super.destroy();
  }

  @Override
  public Action[] getActions(final boolean context) {
    final Action[] actions = super.getActions(context);
    Action[] result = new Action[actions.length + 3];

    System.arraycopy(actions, 0, result, 0, actions.length);
    result[result.length - 3] = IDSetSearchAction.getInstance();
    result[result.length - 2] = GraphRepAction.getInstance();
    result[result.length - 1] = ValidateAction.getInstance();
    return result;
  }


}
