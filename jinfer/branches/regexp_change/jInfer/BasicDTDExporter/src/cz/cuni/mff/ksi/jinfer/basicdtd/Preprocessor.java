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

package cz.cuni.mff.ksi.jinfer.basicdtd;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class Preprocessor {
  private final Set<Element> visited = new HashSet<Element>();

  public Element expandIntervalsElement(final Element treeBase) {
    if (visited.contains(treeBase)) {
      return treeBase;
    }
    visited.add(treeBase);
    return new Element(
            treeBase.getContext(),
            treeBase.getName(),
            treeBase.getMetadata(),
            expandIntervalsRegexp(treeBase.getSubnodes()));
  }

  private boolean isSafeInterval(RegexpInterval interval) {
    if (interval.isOnce() ||
            interval.isOptional() ||
            interval.isKleeneStar() ||
            interval.isKleeneCross()) {
      return true;
    }
    return false;
  }

  private Regexp<AbstractNode> expandIntervalsRegexp(final Regexp<AbstractNode> regexp) {
    switch (regexp.getType()) {
      case LAMBDA:
        return regexp;
      case TOKEN:
        if (NodeType.ELEMENT.equals(regexp.getContent().getType())) {
          AbstractNode expandedContent= expandIntervalsElement((Element) regexp.getContent());

          if (isSafeInterval(regexp.getInterval())) {
            return Regexp.<AbstractNode>getToken(expandedContent, regexp.getInterval());
          }

          List<Regexp<AbstractNode>> l= new ArrayList<Regexp<AbstractNode>>();
          for (int i = 0; i < regexp.getInterval().getMin() - 1; i++) {
            l.add(Regexp.<AbstractNode>getToken(expandedContent));
          }
          l.add(Regexp.<AbstractNode>getToken(expandedContent, RegexpInterval.getKleeneCross()));
          return Regexp.<AbstractNode>getConcatenation(l);
        }
        return Regexp.<AbstractNode>getToken(regexp.getContent()); // nullying any intervals in simple data and attributes!
      case ALTERNATION:
      case CONCATENATION:
      case PERMUTATION:
        final List<Regexp<AbstractNode>> children = new ArrayList<Regexp<AbstractNode>>(regexp.getChildren().size());
        for (final Regexp<AbstractNode> child : regexp.getChildren()) {
          children.add(expandIntervalsRegexp(child));
        }

        if (isSafeInterval(regexp.getInterval())) {
         return new Regexp<AbstractNode>(null, children, regexp.getType(), regexp.getInterval());
        }

        List<Regexp<AbstractNode>> m= new ArrayList<Regexp<AbstractNode>>();
        for (int i = 0; i < regexp.getInterval().getMin() - 1; i++) {
          m.add(new Regexp<AbstractNode>(null, children, regexp.getType(), RegexpInterval.getOnce()));
        }
        m.add(new Regexp<AbstractNode>(null, children, regexp.getType(), RegexpInterval.getKleeneCross()));
        return Regexp.<AbstractNode>getConcatenation(m);
      default:
        throw new IllegalArgumentException("Unknown regexp type");
    }
  }

}
