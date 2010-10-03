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
package cz.cuni.mff.ksi.jinfer.basicigg.xpath;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.SimpleData;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.basicigg.properties.BasicIGGPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.basicigg.utils.IGGUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.jaxen.saxpath.Axis;
import org.jaxen.saxpath.Operator;
import org.jaxen.saxpath.SAXPathException;
import org.jaxen.saxpath.helpers.DefaultXPathHandler;

/**
 * XPath handler for the IGGenerator module.
 *
 * @author vektor
 */
public class XPathHandlerImpl extends DefaultXPathHandler {

  private final Properties properties = RunningProject.getActiveProjectProps(BasicIGGPropertiesPanel.NAME);

  /** Rules that have been inferred so far. */
  private final List<AbstractNode> rules = new ArrayList<AbstractNode>();
  /** The element we were looking at the last time. */
  private Element lastElement = null;
  /** Has lastElement already been written to output? */
  private boolean dirty = false;
  /** The attribute we were looking at the last time. */
  private Attribute lastAttribute = null;
  private boolean isSimpleData = false;
  /** The literal we were looking at the last time. */
  private String lastLiteral = null;

  @Override
  public void startAllNodeStep(final int axis) throws SAXPathException {
    super.startAllNodeStep(axis);

    if (axis == Axis.DESCENDANT_OR_SELF || axis == Axis.PARENT) {
      if (lastElement != null) {
        rules.add(lastElement);
      }

      lastElement = null;
      dirty = false;
    }
  }

  @Override
  public void startNameStep(final int axis, final String prefix, final String localName) throws SAXPathException {
    super.startNameStep(axis, prefix, localName);

    switch (axis) {
      case Axis.CHILD:
        final Element newElement = new Element(null, localName, IGGUtils.ATTR_FROM_QUERY, Regexp.<AbstractNode>getMutable());
        if (lastElement != null) {
          lastElement.getSubnodes().addChild(Regexp.<AbstractNode>getToken(newElement));
          rules.add(lastElement);
        }
        lastElement = newElement;
        dirty = true;
        lastAttribute = null;
        isSimpleData = false;
        break;
      case Axis.ATTRIBUTE:
        if (lastElement != null) {
          final Attribute newAttr = new Attribute(null, localName, IGGUtils.ATTR_FROM_QUERY, null, new ArrayList<String>(0));
          lastElement.getSubnodes().addChild(Regexp.<AbstractNode>getToken(newAttr));
          lastAttribute = newAttr;
          rules.add(lastElement);
          dirty = false;
        }
        break;
    }
  }

  @Override
  public void literal(final String literal) throws SAXPathException {
    super.literal(literal);
    lastLiteral = literal;
  }

  @Override
  public void startEqualityExpr() throws SAXPathException {
    super.startEqualityExpr();
    lastLiteral = null;
  }

  @Override
  public void endEqualityExpr(final int operator) throws SAXPathException {
    super.endEqualityExpr(operator);

    if (operator == Operator.EQUALS && lastLiteral != null && lastAttribute != null) {
      if (Boolean.valueOf(properties.getProperty(BasicIGGPropertiesPanel.KEEP_ATTRIBUTES, "true"))) {
        lastAttribute.getContent().add(lastLiteral);
      }
    }
    if (operator == Operator.EQUALS
            && lastLiteral != null
            && isSimpleData
            && lastElement != null) {
      final SimpleData newSimpleData;
      if (Boolean.valueOf(properties.getProperty(BasicIGGPropertiesPanel.KEEP_SIMPLE_DATA, "true"))) {
        newSimpleData = new SimpleData(null, lastLiteral, IGGUtils.ATTR_FROM_QUERY, null, new ArrayList<String>(0));
      } else {
        newSimpleData = new SimpleData(null, "simple data", IGGUtils.ATTR_FROM_QUERY, null, new ArrayList<String>(0));
      }
      lastElement.getSubnodes().addChild(Regexp.<AbstractNode>getToken(newSimpleData));
      rules.add(lastElement);
      dirty = false;
      isSimpleData = false;
    }

    lastLiteral = null;
  }

  @Override
  public void startTextNodeStep(final int axis) throws SAXPathException {
    super.startTextNodeStep(axis);

    isSimpleData = true;

    if (lastElement != null) {
      final SimpleData newSimpleData;
      if (Boolean.valueOf(properties.getProperty(BasicIGGPropertiesPanel.KEEP_SIMPLE_DATA, "true"))) {
        newSimpleData = new SimpleData(null, null, IGGUtils.ATTR_FROM_QUERY, null, new ArrayList<String>(0));
      } else {
        newSimpleData = new SimpleData(null, "simple data", IGGUtils.ATTR_FROM_QUERY, null, new ArrayList<String>(0));
      }
      lastElement.getSubnodes().addChild(Regexp.<AbstractNode>getToken(newSimpleData));
      rules.add(lastElement);
      dirty = false;
    }
  }

  /**
   * Returns the list of rules that were collected while parsing the query.
   */
  public List<AbstractNode> getRules() {
    //final List<AbstractNode> ret = new ArrayList<AbstractNode>();
    if (lastElement != null && dirty) {
      rules.add(lastElement);
    }
    lastElement = null;
    dirty = false;

    for (AbstractNode node : rules) {
      Element e = (Element) node;

      e.getSubnodes().setType(RegexpType.CONCATENATION);
      e.getSubnodes().setInterval(RegexpInterval.getOnce());
    }

    return rules;
  }
}
