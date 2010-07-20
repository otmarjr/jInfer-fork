/*
 *  Copyright (C) 2010 vitasek
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
package cz.cuni.mff.ksi.jinfer.trivialigg.xpath;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.trivialigg.utils.IGGUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
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

  // TODO vektor Remove logging
  private static final Logger LOG = Logger.getLogger(XPathHandlerImpl.class);
  /** Rules that have been inferred so far. */
  private final List<AbstractNode> rules = new ArrayList<AbstractNode>();
  /** The element we were looking at the last time. */
  private Element lastElement = null;
  /** The attribute we were looking at the last time. */
  private Attribute lastAttribute = null;
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
    }

    LOG.info("allNode: " + axis + " (" + Axis.lookup(axis) + ")");
  }

  @Override
  public void startNameStep(final int axis, final String prefix, final String localName) throws SAXPathException {
    super.startNameStep(axis, prefix, localName);

    // TODO vektor switch
    if (axis == Axis.CHILD) {
      final Element newElement = new Element(null, localName, IGGUtils.ATTR_FROM_QUERY, Regexp.<AbstractNode>getConcatenation());
      if (lastElement != null) {
        lastElement.getSubnodes().addChild(Regexp.<AbstractNode>getToken(newElement));
        rules.add(lastElement);
      }
      lastElement = newElement;
      lastAttribute = null;
    } else if (axis == Axis.ATTRIBUTE && lastElement != null) {
      final Attribute newAttr = new Attribute(null, localName, IGGUtils.ATTR_FROM_QUERY, null, new ArrayList<String>(0));
      lastElement.getSubnodes().addChild(Regexp.<AbstractNode>getToken(newAttr));
      lastAttribute = newAttr;
      rules.add(lastElement);
    }

    LOG.info("name: " + axis + " (" + Axis.lookup(axis) + "), " + prefix + ", " + localName);
  }

  @Override
  public void literal(final String literal) throws SAXPathException {
    super.literal(literal);
    lastLiteral = literal;
    LOG.info("literal: " + literal);
  }

  @Override
  public void startEqualityExpr() throws SAXPathException {
    super.startEqualityExpr();
    lastLiteral = null;
    LOG.info("startEqualityExpr");
  }

  @Override
  public void endEqualityExpr(final int operator) throws SAXPathException {
    super.endEqualityExpr(operator);

    if (operator == Operator.EQUALS && lastLiteral != null && lastAttribute != null) {
      lastAttribute.getContent().add(lastLiteral);
      lastLiteral = null;
    }

    LOG.info("endEqualityExpr: " + operator);
  }

  /**
   * Returns the list of rules that were collected while parsing the query.
   */
  public List<AbstractNode> getRules() {
    final List<AbstractNode> ret = new ArrayList<AbstractNode>(rules);
    if (lastElement != null) {
      ret.add(lastElement);
    }
    lastElement = null;
    return ret;
  }
}
