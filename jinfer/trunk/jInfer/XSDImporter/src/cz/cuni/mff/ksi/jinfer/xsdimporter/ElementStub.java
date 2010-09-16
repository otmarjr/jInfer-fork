/*
 *  Copyright (C) 2010 reseto
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

package cz.cuni.mff.ksi.jinfer.xsdimporter;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author reseto
 */
public class ElementStub extends AbstractNode {
  
  private List<Regexp<AbstractNode>> children = new ArrayList<Regexp<AbstractNode>>();

  public ElementStub(List<String> context, String name, Map<String, Object> metadata) {
    super(context, name, metadata);
  }

  public List<Regexp<AbstractNode>> getChildren() {
    return children;
  }

  public void setChildren(List<Regexp<AbstractNode>> children) {
    this.children = children;
  }

  @Override
  public NodeType getType() {
    return NodeType.ELEMENT;
  }
}
