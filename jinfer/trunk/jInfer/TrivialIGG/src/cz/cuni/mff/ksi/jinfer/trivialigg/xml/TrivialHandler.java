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
package cz.cuni.mff.ksi.jinfer.trivialigg.xml;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
import cz.cuni.mff.ksi.jinfer.base.objects.SimpleData;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX handler for the IGGenerator module.
 * 
 * @author vektor
 */
public class TrivialHandler extends DefaultHandler {

  /** Stack to hold currently open nodes. */
  private final Stack<AbstractNode> stack = new Stack<AbstractNode>();
  /** Rules that have been inferred so far. */
  private final List<AbstractNode> rules = new ArrayList<AbstractNode>();

  @Override
  public void startElement(final String uri, final String localName,
          final String qName, final Attributes attributes) throws SAXException {
    super.startElement(uri, localName, qName, attributes);

    final List<String> context = getContext();

    final Element e = new Element(context, qName, null, 
            Regexp.<AbstractNode>getConcatenation());

    if (attributes.getLength() > 0) {
      final List<String> attrContext = new ArrayList<String>(context);
      attrContext.add(qName);
      // for each attribute, add a subnode representing it
      for (int i = 0; i < attributes.getLength(); i++) {
        final Map<String, Object> nodeAttrs = new HashMap<String, Object>(1);
        nodeAttrs.put("required", Boolean.TRUE);
        final List<String> content = new ArrayList<String>(1);
        content.add(attributes.getValue(i));
        final Attribute a = new Attribute(attrContext, attributes.getQName(i), 
                nodeAttrs, null, content);
        e.getSubnodes().addChild(Regexp.<AbstractNode>getToken(a));
      }
    }

    // if there is parent element, it sits at the top of the stack
    // we add the current element to its parent's rule
    if (!stack.isEmpty() && (stack.peek().getType().equals(NodeType.ELEMENT))) {
      ((Element) stack.peek()).getSubnodes().addChild(Regexp.<AbstractNode>getToken(e));
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

    rules.add(end);
  }

  @Override
  public void characters(final char[] ch, final int start, final int length) throws SAXException {
    super.characters(ch, start, length);
    final String text = String.copyValueOf(ch, start, length).trim();
    if (BaseUtils.isEmpty(text)) {
      return;
    }
    if (stack.peek().getType().equals(NodeType.ELEMENT)) {
      final SimpleData sd = new SimpleData(getContext(), text, null, null, Arrays.asList(""));
      ((Element) stack.peek()).getSubnodes().addChild(Regexp.<AbstractNode>getToken(sd));
    } else {
      throw new IllegalArgumentException("Element expected");
    }
  }

  public List<AbstractNode> getRules() {
    return rules;
  }

  private List<String> getContext() {
    if (stack.isEmpty()) {
      return new ArrayList<String>(0);
    }
    final List<String> ret = new ArrayList<String>(stack.peek().getContext());
    ret.add(stack.peek().getName());
    return ret;
  }
}
