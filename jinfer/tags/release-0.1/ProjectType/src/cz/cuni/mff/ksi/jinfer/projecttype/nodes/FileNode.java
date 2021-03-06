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

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataNode;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;

/**
 * Node which encapsulate input Files in folder node. For this node is disabled copy and cut.
 * @author sviro
 */
public class FileNode extends FilterNode {

  public FileNode(final Node node) {
    super(node, new FilterNode.Children(node), node.getLookup());
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
  public void destroy() throws IOException {
    final Collection<File> files = (Collection<File>) ((FolderNode) this.getParentNode()).getLookup().
            lookup(Collection.class);
    final DataNode orig = (DataNode) this.getOriginal();
    final FileObject fileOb = orig.getDataObject().getPrimaryFile();
    files.remove(FileUtil.toFile(fileOb));

    ((FileChildren) ((FolderNode) this.getParentNode()).getChildren()).addNotify();

    super.destroy();
  }
}
