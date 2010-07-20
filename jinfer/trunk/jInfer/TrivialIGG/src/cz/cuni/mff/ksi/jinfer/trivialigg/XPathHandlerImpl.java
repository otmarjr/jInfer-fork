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
package cz.cuni.mff.ksi.jinfer.trivialigg;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.trivialigg.utils.IGGUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.jaxen.saxpath.Axis;
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
  private Element lastStep = null;

  @Override
  public void startAllNodeStep(final int axis) throws SAXPathException {
    super.startAllNodeStep(axis);

    if (axis == Axis.DESCENDANT_OR_SELF || axis == Axis.PARENT) {
      if (lastStep != null) {
        rules.add(lastStep);
      }

      lastStep = null;
    }

    LOG.info("allNode: " + axis + " (" + Axis.lookup(axis) + ")");
  }

  @Override
  public void startNameStep(final int axis, final String prefix, final String localName) throws SAXPathException {
    super.startNameStep(axis, prefix, localName);

    if (axis == Axis.CHILD) {
      final Element newStep = new Element(null, localName, IGGUtils.ATTR_FROM_QUERY, Regexp.<AbstractNode>getConcatenation());
      if (lastStep != null) {
        lastStep.getSubnodes().addChild(Regexp.<AbstractNode>getToken(newStep));
        rules.add(lastStep);
      }
      lastStep = newStep;
    } else if (axis == Axis.ATTRIBUTE) {
      if (lastStep != null) {
        lastStep.getSubnodes().addChild(Regexp.<AbstractNode>getToken(
                new Attribute(null, localName, IGGUtils.ATTR_FROM_QUERY, null, new ArrayList<String>(0))));
        rules.add(lastStep);
      }
    }

    LOG.info("name: " + axis + " (" + Axis.lookup(axis) + "), " + prefix + ", " + localName);
  }

  /**
   * Returns the list of rules that were collected while parsing the query.
   */
  public List<AbstractNode> getRules() {
    final List<AbstractNode> ret = new ArrayList<AbstractNode>(rules);
    if (lastStep != null) {
      ret.add(lastStep);
    }
    lastStep = null;
    return ret;
  }
}
