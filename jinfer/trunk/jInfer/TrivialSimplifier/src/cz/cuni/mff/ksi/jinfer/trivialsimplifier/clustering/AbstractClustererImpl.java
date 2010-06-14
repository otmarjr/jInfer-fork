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
package cz.cuni.mff.ksi.jinfer.trivialsimplifier.clustering;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract clusterer implementation. Walks all the rules and uses overridable
 * logic to determine, whether they belong to a common cluster.
 * 
 * @author vektor
 */
public abstract class AbstractClustererImpl implements Clusterer {

  @Override
  public List<Pair<AbstractNode, List<AbstractNode>>> cluster(final List<AbstractNode> initialGrammar) {
    final List<Pair<AbstractNode, List<AbstractNode>>> ret = new ArrayList<Pair<AbstractNode, List<AbstractNode>>>();

    for (final AbstractNode n : initialGrammar) {
      boolean found = false;
      for (final Pair<AbstractNode, List<AbstractNode>> p : ret) {
        if (clusters(n, p.getFirst())) {
          // if n belongs to this cluster, add it
          addNodeToCluster(n, p);
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
  protected abstract boolean clusters(final AbstractNode n, final AbstractNode first);

  private static void addNodeToCluster(final AbstractNode n,
          final Pair<AbstractNode, List<AbstractNode>> p) {
    p.getSecond().add(n);

    final Element node = (Element) n;
    final Element clusterHead = (Element) p.getFirst();

    reflectAttributes(clusterHead.getElementAttributes(), node.getElementAttributes());
  }

  /**
   * Attributes from the node are added to those of the cluster. Requiredness is
   * evaluated.
   * 
   * @param clusterAttrs
   * @param nodeAttrs
   */
  private static void reflectAttributes(final List<Attribute> clusterAttrs, final List<Attribute> nodeAttrs) {
    // add attributes from the node to the cluster, but only if not there already.
    for (final Attribute a : nodeAttrs) {
      boolean found = false;
      for (final Attribute ca : clusterAttrs) {
        if (ca.getName().equalsIgnoreCase(a.getName())) {
          found = true;
          break;
        }
      }
      if (!found) {
        a.getAttributes().remove("required");
        clusterAttrs.add(a);
      }
    }
    // walk the attributes in the cluster. if not among those from the node, remove their requiredness.
    for (final Attribute ca : clusterAttrs) {
      boolean found = false;
      for (final Attribute a : nodeAttrs) {
        if (ca.getName().equalsIgnoreCase(a.getName())) {
          found = true;
          break;
        }
      }
      if (!found) {
        ca.getAttributes().remove("required");
      }
    }
  }
}
