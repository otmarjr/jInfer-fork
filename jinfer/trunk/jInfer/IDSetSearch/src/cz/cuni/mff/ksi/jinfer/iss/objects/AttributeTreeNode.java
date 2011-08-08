/*
 * Copyright (C) 2011 vektor
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.iss.objects;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.List;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Class representing an {@link Attribute} in a {@link JTree}.
 *
 * @author vektor
 */
public class AttributeTreeNode extends DefaultMutableTreeNode {

  private static final long serialVersionUID = 454563134L;

  private final String elementName;

  private final String attributeName;

  private final List<String> content;

  /**
   * Full constructor.
   *
   * @param elementName Name of the {@link Element} where the represented {@link Attribute} resides.
   * @param attributeName Name of the represented {@link Attribute}.
   * @param content Content of the represented {@link Attribute} (see {@link Attribute#content}).
   */
  public AttributeTreeNode(final String elementName,
          final String attributeName, final List<String> content) {
    super(attributeName);
    this.elementName = elementName;
    this.attributeName = attributeName;
    this.content = content;
  }

  public String getElementName() {
    return elementName;
  }

  public String getAttributeName() {
    return attributeName;
  }

  public List<String> getContent() {
    return content;
  }

}
