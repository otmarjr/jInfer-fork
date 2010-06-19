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
package cz.cuni.mff.ksi.jinfer.modularsimplifier.clustering;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import java.util.ArrayList;
import java.util.Arrays;
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

    for (final AbstractNode node : initialGrammar) {
      boolean found = false;
      for (final Pair<AbstractNode, List<AbstractNode>> cluster : ret) {
        if (clusters(node, cluster.getFirst())) {
          // if n belongs to this cluster, add it
          addNodeToCluster((Element) node, cluster);
          found = true;
          break;
        }
      }
      if (!found) {
        // if n doesn't belong to any of the clusters, create a new one for it
        final List<AbstractNode> l = new ArrayList<AbstractNode>(1);
        l.add(node);
        ret.add(new Pair<AbstractNode, List<AbstractNode>>(node, l));
      }
    }

    return ret;
  }

  /**
   * Answers the question whether these two nodes belong to the same cluster.
   */
  protected abstract boolean clusters(final AbstractNode n, final AbstractNode first);

  private static void addNodeToCluster(final Element node,
          final Pair<AbstractNode, List<AbstractNode>> cluster) {
    cluster.getSecond().add(node);

    final Element clusterHead = (Element) cluster.getFirst();

    reflectAttributes(clusterHead.getElementAttributes(), node.getElementAttributes());
  }

  /**
   * Attributes from the node are added to those of the cluster. 
   * 
   * Requiredness is evaluated: if the attribute is required in the cluster
   * and simultaneously it is found in the node, it is still required. Otherwise
   * its requiredness is removed.
   * 
   * @param clusterAttrs
   * @param nodeAttrs
   */
  @SuppressWarnings("unchecked")
  private static void reflectAttributes(final List<Attribute> clusterAttrs, final List<Attribute> nodeAttrs) {
    // add attributes from the node to the cluster, but only if not there already.
    for (final Attribute nodeAttr : nodeAttrs) {
      boolean found = false;
      for (final Attribute clusterAttr : clusterAttrs) {
        if (clusterAttr.getName().equalsIgnoreCase(nodeAttr.getName())) {
          found = true;
          clusterAttr.getContent().addAll(nodeAttr.getContent());
          break;
        }
      }
      if (!found) {
        nodeAttr.getAttributes().remove("required");
        clusterAttrs.add(nodeAttr);
      }
    }
    // walk the attributes in the cluster. if not among those from the node, remove their requiredness.
    for (final Attribute clusterAttr : clusterAttrs) {
      boolean found = false;
      for (final Attribute nodeAttr : nodeAttrs) {
        if (clusterAttr.getName().equalsIgnoreCase(nodeAttr.getName())) {
          found = true;
          break;
        }
      }
      if (!found) {
        clusterAttr.getAttributes().remove("required");
      }
    }
  }
}
