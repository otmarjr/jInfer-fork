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

import cz.cuni.mff.ksi.jinfer.fileselector.FileSelectorTopComponent;
import java.util.Collection;
import javax.swing.Action;
import org.openide.filesystems.FileObject;

/**
 *
 * @author sviro
 */
public class OutputNode extends FolderNode{

  private Collection<FileObject> files;

  public OutputNode(final String name, final Collection<FileObject> files, final FileSelectorTopComponent topComponent) {
    super(name, null, topComponent);
    this.files = files;
    setChildren(new OutputChildren(files));
  }

  @Override
  public Action[] getActions(final boolean bln) {
    return new Action[]{};
  }


}
