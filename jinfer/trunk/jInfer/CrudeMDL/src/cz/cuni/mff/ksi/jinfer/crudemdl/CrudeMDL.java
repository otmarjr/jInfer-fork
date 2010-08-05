/*
 *  Copyright (C) 2010 anti
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

package cz.cuni.mff.ksi.jinfer.crudemdl;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
// TODO this class should be rewrited from scratch
/**
 * TODO anti comment
 *
 * @author anti
 */
public class CrudeMDL {
  public double codeLengthGrammar(AbstractNode grammarRootNode) {
    HashSet<String> encounteredElements = new HashSet<String>();
    LinkedList<Regexp<AbstractNode>> stack = new LinkedList<Regexp<AbstractNode>>();

    if (NodeType.ELEMENT.equals(grammarRootNode.getType())) {
      encounteredElements.add(((Element) grammarRootNode).getName());
      stack.addFirst(((Element) grammarRootNode).getSubnodes());
    } else {
      throw new IllegalArgumentException("Root node of grammar must be element.");
    }

    Regexp<AbstractNode> reg = null;
    int charsCount= 0;
    while (!stack.isEmpty()) {
      reg = stack.removeFirst();
      if (RegexpType.CONCATENATION.equals(reg.getType())) {
        stack.addAll(reg.getChildren());
      } else if (RegexpType.ALTERNATION.equals(reg.getType())) {
        charsCount+= reg.getChildren().size() + 1;
        stack.addAll(reg.getChildren());
      } else if (RegexpType.KLEENE.equals(reg.getType())) {
        charsCount++;
        stack.addAll(reg.getChildren());
      } else if (RegexpType.TOKEN.equals(reg.getType())) {
        charsCount++;
      //  encounteredElements.add(((Element) reg.getChildren()).getName());
      }
    }
    return charsCount * Math.log(encounteredElements.size());
  }

  public double codeLengthDocument(AbstractNode grammarRootNode, AbstractNode rootNode) {
    LinkedList<Regexp<AbstractNode>> stack = new LinkedList<Regexp<AbstractNode>>();

    if (NodeType.ELEMENT.equals(grammarRootNode.getType())) {
      stack.addFirst(((Element) grammarRootNode).getSubnodes());
    } else {
      throw new IllegalArgumentException("Root node of grammar must be element.");
    }

    Regexp<AbstractNode> reg = null;
    StringBuilder sb = new StringBuilder();
    int charsCount= 0;
    while (!stack.isEmpty()) {
      reg = stack.removeFirst();
      if (RegexpType.CONCATENATION.equals(reg.getType())) {
        sb.append(grammarRootNode.getName());
        stack.addAll(reg.getChildren());
      } else if (RegexpType.ALTERNATION.equals(reg.getType())) {
        charsCount+= reg.getChildren().size() + 1;
        stack.addAll(reg.getChildren());
      } else if (RegexpType.KLEENE.equals(reg.getType())) {
        charsCount++;
        stack.addAll(reg.getChildren());
      } else if (RegexpType.TOKEN.equals(reg.getType())) {
        charsCount++;
      }
    }
    return 0;
  }

  public double codeLengthDocuments(List<AbstractNode> documentRootNodes) {
    double sum = 0;
    for (AbstractNode rootNode : documentRootNodes) {
    }
    return sum;
  }
}
