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
package cz.cuni.mff.ksi.jinfer.modularsimplifier.processing;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import java.util.ArrayList;
import java.util.List;

/**
 * Cluster processor implementation that exports a cluster as a trie (prefix tree).
 * 
 * @author vektor
 */
public class CPTrie implements ClusterProcessor {

  @Override
  public List<AbstractNode> processClusters(
          final List<Pair<AbstractNode, List<AbstractNode>>> clusters) {
    final List<AbstractNode> ret = new ArrayList<AbstractNode>();

    for (final Pair<AbstractNode, List<AbstractNode>> cluster : clusters) {
      ret.add(processCluster(cluster));
    }

    return ret;
  }

  private static Element processCluster(
          final Pair<AbstractNode, List<AbstractNode>> cluster) {
    verify(cluster);

    final Element treeBase = (Element) cluster.getFirst();

    // put every item from the cluster into the trie
    for (final AbstractNode n : cluster.getSecond()) {
      if (n != cluster.getFirst()) {
        TrieHelper.addBranchToTree(treeBase.getSubnodes(), ((Element) n).getSubnodes());
      }
    }

    // walk the tree and shorten its concatenations/alternations
    return new Shortener().simplify(treeBase);
  }

  private static void verify(
          final Pair<AbstractNode, List<AbstractNode>> cluster) {
    for (final AbstractNode n : cluster.getSecond()) {
      if (!n.isElement()) {
        throw new IllegalArgumentException("Element expected");
      }
      if (!((Element) n).getSubnodes().isConcatenation()) {
        throw new IllegalArgumentException("Concatenation expected");
      }
    }
  }
}