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
package cz.cuni.mff.ksi.jinfer.trivialsimplifier;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vektor
 */
public class ClustererImpl implements Clusterer {

  @Override
  public List<Pair<AbstractNode, List<AbstractNode>>> cluster(final List<AbstractNode> initialGrammar) {
    final List<Pair<AbstractNode, List<AbstractNode>>> ret = new ArrayList<Pair<AbstractNode, List<AbstractNode>>>();

    for (final AbstractNode n : initialGrammar) {
      boolean found = false;
      for (final Pair<AbstractNode, List<AbstractNode>> p : ret) {
        if (clusters(n, p.getFirst())) {
          // if n belongs to this cluster, add it
          p.getSecond().add(n);
          found = true;
          break;
        }
      }
      if (!found) {
        // if n doesn't belong to any of the clusters, create a new one for it
        final List<AbstractNode> l = new ArrayList<AbstractNode>();
        l.add(n);
        ret.add(new Pair<AbstractNode, List<AbstractNode>>(n, l));
      }
    }

    return ret;
  }

  /**
   * Answers the question whether these two nodes belong to the same cluster.
   */
  private boolean clusters(final AbstractNode n, final AbstractNode first) {
    // TODO vektor Here be different implementations
    return n.getName().equalsIgnoreCase(first.getName());
  }
}
