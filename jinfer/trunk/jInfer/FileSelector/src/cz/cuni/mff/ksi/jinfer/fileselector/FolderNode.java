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

import java.io.File;
import java.util.Collection;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;

/**
 *
 * @author sviro
 */
public class FolderNode extends AbstractNode {

  private final Collection<File> files;
  private final String folderName;

  public FolderNode(final String name, final Collection<File> files) {
    super(new FileChildren(files));
    this.files = files;
    this.folderName = name;
  }

  @Override
  public String getDisplayName() {
    return folderName;
  }

  @Override
  public Action[] getActions(final boolean bln) {
    return new Action[]{new FileAddAction(this, files)};
  }
}
