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
package cz.cuni.mff.ksi.jinfer.twostep.processing.alternations;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.twostep.clustering.Clusterer;
import cz.cuni.mff.ksi.jinfer.twostep.processing.ClusterProcessor;
import java.util.List;

/**
 * Trivial implementation of
 * {@link cz.cuni.mff.ksi.jinfer.twostep.processing.ClusterProcessor}
 * - simply returns all possible
 * right sides as alternation in the resulting rule.
 * 
 * @author vektor
 */
public class Alternations implements ClusterProcessor<AbstractStructuralNode> {

  @Override
  public AbstractStructuralNode processCluster(
          final Clusterer<AbstractStructuralNode> clusterer,
          final List<AbstractStructuralNode> rules) throws InterruptedException {
    final Element ret = Element.getMutable();

    ret.setName(rules.get(0).getName());
    ret.getContext().addAll(rules.get(0).getContext());
    ret.getSubnodes().setType(RegexpType.ALTERNATION);
    ret.getSubnodes().setInterval(RegexpInterval.getOnce());

    for (final AbstractStructuralNode n : rules) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      if (!n.isElement()) {
        throw new IllegalArgumentException("Expecting element here.");
      }
      final Element e = (Element) n;
      ret.getSubnodes().addChild(e.getSubnodes());
    }

    // TODO vektor Probably losing attributes

    ret.setImmutable();
    return ret;
  }
}
