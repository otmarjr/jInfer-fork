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
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.trivialigg.utils.IGGUtils;
import java.util.ArrayList;
import java.util.List;
import org.jaxen.saxpath.Axis;
import org.jaxen.saxpath.SAXPathException;
import org.jaxen.saxpath.helpers.DefaultXPathHandler;

/**
 * XPath handler for the IGGenerator module.
 *
 * @author vektor
 */
public class XPathHandlerImpl extends DefaultXPathHandler {

  /** Rules that have been inferred so far. */
  private final List<AbstractNode> rules = new ArrayList<AbstractNode>();
  /** The element we were looking at the last time. */
  private String lastStep = null;

  @Override
  public void startAllNodeStep(final int axis) throws SAXPathException {
    super.startAllNodeStep(axis);

    if (axis == Axis.DESCENDANT_OR_SELF || axis == Axis.PARENT) {
      if (lastStep != null) {
        rules.add(new Element(null, lastStep, IGGUtils.ATTR_FROM_QUERY, Regexp.<AbstractNode>getConcatenation()));
      }

      lastStep = null;
    }
  }

  @Override
  public void startNameStep(final int axis, final String prefix, final String localName) throws SAXPathException {
    super.startNameStep(axis, prefix, localName);

    if (lastStep != null) {
      final List<Regexp<AbstractNode>> children = new ArrayList<Regexp<AbstractNode>>(1);
      children.add(Regexp.<AbstractNode>getToken(new Element(null, localName, IGGUtils.ATTR_FROM_QUERY, Regexp.<AbstractNode>getConcatenation())));
      rules.add(new Element(null, lastStep, IGGUtils.ATTR_FROM_QUERY, Regexp.<AbstractNode>getConcatenation(children)));
    }

    lastStep = localName;
  }

  /**
   * Returns the list of rules that were collected while parsing the query.
   */
  public List<AbstractNode> getRules() {
    final List<AbstractNode> ret = new ArrayList<AbstractNode>(rules);
    if (lastStep != null) {
      ret.add(new Element(null, lastStep, IGGUtils.ATTR_FROM_QUERY, Regexp.<AbstractNode>getConcatenation()));
    }
    lastStep = null;
    return ret;
  }
}
