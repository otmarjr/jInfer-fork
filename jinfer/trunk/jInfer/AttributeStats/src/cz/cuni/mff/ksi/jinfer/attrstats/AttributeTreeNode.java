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
package cz.cuni.mff.ksi.jinfer.attrstats;

import java.util.Collections;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public class AttributeTreeNode extends DefaultMutableTreeNode {

  private static final long serialVersionUID = 454563134L;

  private final List<String> content;

  public AttributeTreeNode(final String name, final List<String> content) {
    super(name);
    this.content = content;
  }

  public List<String> getContent() {
    return content;
  }

}
