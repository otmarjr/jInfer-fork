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

import cz.cuni.mff.ksi.jinfer.base.interfaces.Simplifier;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SimplifierCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 *
 * @author anti
 */
public class SimplifierImpl implements Simplifier {

  @Override
  public String getModuleName() {
    return "CrudeMDL";
  }

  @Override
  public void start(List<AbstractNode> initialGrammar, SimplifierCallback callback) {
    //throw new UnsupportedOperationException("Not supported yet.");

    HashMap<AbstractNode,ArrayList<Regexp<AbstractNode>>> elements = new HashMap<AbstractNode,ArrayList<Regexp<AbstractNode>>>();

    ListIterator<AbstractNode> iter= initialGrammar.listIterator();
    AbstractNode t = null;
    while (iter.hasNext()) {
      t= iter.next();
      if (!NodeType.ELEMENT.equals(t.getType())) {
        StringBuilder sb = new StringBuilder("Initial grammar contains rule with ");
        sb.append(t.getType().toString());
        sb.append(" as left side.");
        throw new IllegalArgumentException(sb.toString());
      }

      if (!elements.containsKey(t)) {
        elements.put(t, new ArrayList<Regexp<AbstractNode>>());
      }
      elements.get(t).add( ((Element) t).getSubnodes() );
    }

    Set<AbstractNode> keys = elements.keySet();
    ArrayList<AbstractNode> newGrammar = new ArrayList<AbstractNode>();
    ArrayList<Regexp<AbstractNode>> rightSide = null;
    AbstractNode leftSide = null;
    for (AbstractNode el : keys) {
      rightSide= new ArrayList<Regexp<AbstractNode>>();
      for (Regexp<AbstractNode> rebeka : elements.get(el)) {
        rightSide.add(rebeka);
      }
      leftSide= new Element(el.getContext(), el.getName(), null,
        new Regexp<AbstractNode>(null, rightSide, RegexpType.ALTERNATION));
      newGrammar.add(leftSide);
    }

    callback.finished(newGrammar);
  }
}
