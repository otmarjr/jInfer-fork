/*
 *  Copyright (C) 2010 vektor
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

package cz.cuni.mff.ksi.jinfer.trivialigg;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX handler for the trivial IGGenerator module.
 * 
 * @author vektor
 */
public class TrivialHandler extends DefaultHandler {

  /** Stack to hold currently open nodes. */
  private final Stack<AbstractNode> stack = new Stack<AbstractNode>();
  /** Set of rules that have been inferred so far. */
  private final Set<AbstractNode> rules = new HashSet<AbstractNode>();

  @Override
  public void startElement(final String uri, final String localName,
          final String qName, final Attributes attributes) throws SAXException {
    super.startElement(uri, localName, qName, attributes);

    final Element e = new Element(null, qName, null, new ArrayList<AbstractNode>());

    // if there is parent element, it sits at the top of the stack
    // we add the current element to its parent's rule
    if (!stack.isEmpty() && stack.peek() instanceof Element) {
      ((Element)stack.peek()).getSubnodes().add(e);
    }

    // push the current element to the stack with an empty rule
    stack.push(e);
  }

  @Override
  public void endElement(final String uri, final String localName,
          final String qName) throws SAXException {
    super.endElement(uri, localName, qName);

    // current element ends, it is time to give out its rule
    final AbstractNode end = stack.pop();
    if (!end.getName().equals(qName)) {
      throw new IllegalArgumentException("unpaired element");
    }

    getRules().add(end);
  }

  public Set<AbstractNode> getRules() {
    return rules;
  }
}
