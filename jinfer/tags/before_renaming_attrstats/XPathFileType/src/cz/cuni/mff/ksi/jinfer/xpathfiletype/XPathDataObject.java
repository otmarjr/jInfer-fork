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
package cz.cuni.mff.ksi.jinfer.xpathfiletype;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.text.DataEditorSupport;

/**
 * DataObject defining XPath file type.
 * 
 * @author sviro
 */
public class XPathDataObject extends MultiDataObject {

  private static final long serialVersionUID = 3534512312l;

  public XPathDataObject(final FileObject pf, final MultiFileLoader loader) throws DataObjectExistsException,
          IOException {
    super(pf, loader);
    final CookieSet cookies = getCookieSet();
    cookies.add((Node.Cookie) DataEditorSupport.create(this, getPrimaryEntry(), cookies));
  }

  @Override
  protected Node createNodeDelegate() {
    return new DataNode(this, Children.LEAF, getLookup());
  }

  @Override
  public Lookup getLookup() {
    return getCookieSet().getLookup();
  }
}
