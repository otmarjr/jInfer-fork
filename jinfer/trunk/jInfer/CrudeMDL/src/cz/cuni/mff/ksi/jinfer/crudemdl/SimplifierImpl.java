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
import java.util.logging.Logger;

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

    HashMap<Element, Element> elements = new HashMap<Element, Element>();

    Element el = null;
    for (AbstractNode node : initialGrammar) {
      if (!NodeType.ELEMENT.equals(node.getType())) {
        StringBuilder sb = new StringBuilder("Initial grammar contains rule with ");
        sb.append(node.getType().toString());
        sb.append(" as left side.");
        throw new IllegalArgumentException(sb.toString());
      }

      el= (Element) node;
      if (!elements.containsKey(el)) {
        elements.put(
                el,
                new Element(
                  el.getContext(), el.getName(), null,
                  new Regexp<AbstractNode>(
                    null,
                    new ArrayList<Regexp<AbstractNode>>(),
                    RegexpType.ALTERNATION)
                  )
                );
      }
      elements.get(el).getSubnodes().addChild( el.getSubnodes() );
    }

    callback.finished( new ArrayList<AbstractNode>(elements.values()) );
  }
}
