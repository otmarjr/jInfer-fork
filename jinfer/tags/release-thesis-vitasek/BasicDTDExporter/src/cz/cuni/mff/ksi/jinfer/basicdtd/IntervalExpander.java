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

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.interfaces.nodes.StructuralNodeType;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for recursive expanding of all intervals of regexps, that cannot be
 * represented in DTD format. That is, interval in form {m, n}, where m > 0
 * or n is not infinity (DTD has only ?, *, + intervals).
 *
 * Expansion is done recursively, every regexp in form
 * a{m > 0, n} is replaced by regexp in form
 * (a,a,a,a, ... (m-1 times),a{1,infinity})
 * that is declaration
 * <ELEMENT xxx (a,a,a,a, ... (m-1 times),a+)>
 * in DTD language.
 *
 * @author anti
 */
public class IntervalExpander {

  private boolean isSafeInterval(final RegexpInterval interval) {
    if (interval.isOnce()
            || interval.isOptional()
            || interval.isKleeneStar()
            || interval.isKleeneCross()) {
      return true;
    }
    return false;
  }

  public Regexp<AbstractStructuralNode> expandIntervalsRegexp(
          final Regexp<AbstractStructuralNode> regexp) {
    switch (regexp.getType()) {
      case LAMBDA:
        return regexp;
      case TOKEN:
        if (StructuralNodeType.ELEMENT.equals(regexp.getContent().getType())) {
          final AbstractStructuralNode expandedContent = ((Element) regexp.getContent());

          if (isSafeInterval(regexp.getInterval())) {
            return Regexp.<AbstractStructuralNode>getToken(expandedContent, regexp.getInterval());
          }

          final List<Regexp<AbstractStructuralNode>> l = new ArrayList<Regexp<AbstractStructuralNode>>();
          for (int i = 0; i < regexp.getInterval().getMin() - 1; i++) {
            l.add(Regexp.<AbstractStructuralNode>getToken(expandedContent));
          }
          if (regexp.getInterval().isUnbounded()) {
            l.add(Regexp.<AbstractStructuralNode>getToken(expandedContent, RegexpInterval.getKleeneCross()));
          } else {
            l.add(Regexp.<AbstractStructuralNode>getToken(expandedContent));
            for (int i = regexp.getInterval().getMin(); i < regexp.getInterval().getMax(); i++) {
              l.add(Regexp.<AbstractStructuralNode>getToken(expandedContent, RegexpInterval.getOptional()));
            }
          }
          return Regexp.<AbstractStructuralNode>getConcatenation(l);
        }
        return Regexp.<AbstractStructuralNode>getToken(regexp.getContent()); // nullying any intervals in simple data and attributes!
      case ALTERNATION:
      case CONCATENATION:
      case PERMUTATION:
        final List<Regexp<AbstractStructuralNode>> children = new ArrayList<Regexp<AbstractStructuralNode>>(regexp.getChildren().size());
        for (final Regexp<AbstractStructuralNode> child : regexp.getChildren()) {
          children.add(expandIntervalsRegexp(child));
        }

        if (isSafeInterval(regexp.getInterval())) {
          return new Regexp<AbstractStructuralNode>(null, children, regexp.getType(), regexp.getInterval());
        }

        final List<Regexp<AbstractStructuralNode>> m = new ArrayList<Regexp<AbstractStructuralNode>>();
        for (int i = 0; i < regexp.getInterval().getMin() - 1; i++) {
          m.add(new Regexp<AbstractStructuralNode>(null, children, regexp.getType(), RegexpInterval.getOnce()));
        }
        if (regexp.getInterval().isUnbounded()) {
          m.add(new Regexp<AbstractStructuralNode>(null, children, regexp.getType(), RegexpInterval.getKleeneCross()));
        } else {
          m.add(new Regexp<AbstractStructuralNode>(null, children, regexp.getType(), RegexpInterval.getOnce()));
          for (int i = regexp.getInterval().getMin(); i < regexp.getInterval().getMax(); i++) {
            m.add(new Regexp<AbstractStructuralNode>(null, children, regexp.getType(), RegexpInterval.getOptional()));
          }
        }
        return Regexp.<AbstractStructuralNode>getConcatenation(m);
      default:
        throw new IllegalArgumentException("Unknown regexp type" + regexp.getType());
    }
  }
}
