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
package cz.cuni.mff.ksi.jinfer.basicigg.xml;

import cz.cuni.mff.ksi.jinfer.base.objects.StructuralAbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.StructuralNodeType;
import cz.cuni.mff.ksi.jinfer.base.objects.SimpleData;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.basicigg.properties.BasicIGGPropertiesPanel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
  private final Stack<StructuralAbstractNode> stack = new Stack<StructuralAbstractNode>();
  /** Rules that have been inferred so far. */
  private final List<StructuralAbstractNode> rules = new ArrayList<StructuralAbstractNode>();

  private final Properties properties = RunningProject.getActiveProjectProps(BasicIGGPropertiesPanel.NAME);

  @Override
  public void startElement(final String uri, final String localName,
          final String qName, final Attributes attributes) throws SAXException {
    super.startElement(uri, localName, qName, attributes);

    final List<String> context = getContext();

    final List<Attribute> elAttributes= new ArrayList<Attribute>();
    if (attributes.getLength() > 0) {
      final List<String> attrContext = new ArrayList<String>(context);
      attrContext.add(qName);
      // for each attribute, add a subnode representing it
      for (int i = 0; i < attributes.getLength(); i++) {
        final Map<String, Object> metadata = new HashMap<String, Object>(1);
        metadata.put("required", Boolean.TRUE);
        final List<String> content = new ArrayList<String>(1);
        if (Boolean.valueOf(properties.getProperty(BasicIGGPropertiesPanel.KEEP_ATTRIBUTES, "true"))) {
          content.add(attributes.getValue(i));
        }
        final Attribute a = new Attribute(attrContext, attributes.getQName(i), 
                metadata, null, content);
        elAttributes.add(a);
      }
    }

    final Element e = new Element(context, qName, null,
            Regexp.<StructuralAbstractNode>getMutable(), elAttributes);

    // if there is parent element, it sits at the top of the stack
    // we add the current element to its parent's rule
    if (!stack.isEmpty() && (stack.peek().getType().equals(StructuralNodeType.ELEMENT))) {
      ((Element) stack.peek()).getSubnodes().addChild(Regexp.<StructuralAbstractNode>getToken(e));
    }

    // push the current element to the stack with an empty rule
    stack.push(e);
  }

  @Override
  public void endElement(final String uri, final String localName,
          final String qName) throws SAXException {
    super.endElement(uri, localName, qName);

    // current element ends, it is time to give out its rule
    final StructuralAbstractNode end = stack.pop();

    if (!end.getName().equals(qName)) {
      throw new IllegalArgumentException("unpaired element");
    }
    Element e = (Element) end;
    e.getSubnodes().setType(RegexpType.CONCATENATION);
    e.getSubnodes().setInterval(RegexpInterval.getOnce());
    e.getSubnodes().setImmutable();
    
    rules.add(end);
  }

  @Override
  public void characters(final char[] ch, final int start, final int length) throws SAXException {
    super.characters(ch, start, length);
    final String text = String.copyValueOf(ch, start, length).trim();
    if (BaseUtils.isEmpty(text)) {
      return;
    }
    if (stack.peek().getType().equals(StructuralNodeType.ELEMENT)) {
      final SimpleData sd;
      if (Boolean.valueOf(properties.getProperty(BasicIGGPropertiesPanel.KEEP_SIMPLE_DATA, "true"))) {
        sd = new SimpleData(getContext(), text, null, null, Arrays.asList(""));
      }
      else {
        sd = new SimpleData(getContext(), "simple data", null, null, Arrays.asList(""));
      }
      ((Element) stack.peek()).getSubnodes().addChild(Regexp.<StructuralAbstractNode>getToken(sd));
    } else {
      throw new IllegalArgumentException("Element expected");
    }
  }

  public List<StructuralAbstractNode> getRules() {
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
