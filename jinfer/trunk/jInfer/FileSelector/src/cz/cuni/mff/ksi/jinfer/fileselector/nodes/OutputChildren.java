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

package cz.cuni.mff.ksi.jinfer.fileselector.nodes;

import cz.cuni.mff.ksi.jinfer.fileselector.NoDataObjectException;
import java.util.Collection;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author sviro
 */
public class OutputChildren extends Children.Keys<FileObject>{

  private final Collection<FileObject> model;

  public OutputChildren(final Collection<FileObject> model) {
    super();
    this.model = model;
  }

  @Override
  public void addNotify() {
    setKeys(model);
  }

  @Override
  protected Node[] createNodes(final FileObject fileOb) {
    if (fileOb == null) {
      model.remove(fileOb);
      return null;
    }
    try {
      return new Node[]{DataObject.find(fileOb).getNodeDelegate()};
    } catch (DataObjectNotFoundException ex) {
      throw new NoDataObjectException(ex.getMessage());
    }
  }

}
