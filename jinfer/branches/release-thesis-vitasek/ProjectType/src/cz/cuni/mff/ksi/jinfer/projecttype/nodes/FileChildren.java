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
import java.util.Collection;
import org.apache.log4j.Logger;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 * Creates children for folder node. For each {@link File}, {@link FileNode} which
 * represents particular file in project tree is created.
 * 
 * @author sviro
 * @see FolderNode
 */
public class FileChildren extends Children.Keys<File> {

  private final Collection<File> files;
  private static final Logger LOG = Logger.getLogger(FileChildren.class);

  public FileChildren(final Collection<File> files) {
    super();
    this.files = files;
  }

  public void refreshNodes() {
    if (isInitialized()) {
      addNotify();
    }
  }

  @Override
  protected void addNotify() {
    //Input.removeNonExistFiles(files);

    setKeys(files);
  }

  @Override
  protected Node[] createNodes(final File file) {
    try {
      return new Node[]{new FileNode(DataObject.find(FileUtil.toFileObject(file)).getNodeDelegate())};
    } catch (DataObjectNotFoundException ex) {
      LOG.error(ex);
      throw new RuntimeException(ex);
    }
  }
}
