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

import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import cz.cuni.mff.ksi.jinfer.fileselector.FileSelectorTopComponent;
import java.io.File;
import java.util.Collection;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.windows.TopComponent;

/**
 *
 * @author sviro
 */
public class RootNode extends AbstractNode {

  // TODO sviro Convert this to an enum
  private static final String[] FOLDERS = {"XML", "XSD", "QUERIES"};

  public RootNode(final FileSelectorTopComponent topComponent, final Input input) {
    super(new Children.Keys<String>() {

      @Override
      protected void addNotify() {
        setKeys(FOLDERS);
      }

      @Override
      protected Node[] createNodes(final String folderName) {
        final Collection<File> files;
        if (folderName.equals("XML")) {
          files = input.getDocuments();
        } else if (folderName.equals("XSD")) {
          files = input.getSchemas();
        } else if (folderName.equals("QUERIES")) {
          files = input.getQueries();
        } else {
          throw new IllegalArgumentException("unknown folder type");
        }
        return new Node[]{new FolderNode(folderName, files, topComponent)};
      }
    });
  }
}
