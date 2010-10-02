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
import cz.cuni.mff.ksi.jinfer.base.objects.Cluster;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;

/**
 * Cluster processor implementation that exports a cluster as a trie (prefix tree).
 * 
 * @author vektor
 */
public class CPTrie extends AbstractCPImpl {

  @Override
  protected Element processCluster(
          final Cluster cluster) {
    verify(cluster);

    final Element representant = (Element) cluster.getRepresentant();

    // put every item from the cluster into the trie
    for (final AbstractNode n : cluster.getContent()) {
      if (n != cluster.getRepresentant()) {
        TrieHelper.addBranchToTree(representant.getSubnodes(), ((Element) n).getSubnodes());
      }
    }

    // walk the tree and shorten its concatenations/alternations
    return new Shortener().simplify(representant);
  }

  private static void verify(
          final Cluster cluster) {
    for (final AbstractNode n : cluster.getContent()) {
      if (!n.isElement()) {
        throw new IllegalArgumentException("Element expected");
      }
      if (!((Element) n).getSubnodes().isConcatenation()) {
        throw new IllegalArgumentException("Concatenation expected, instead " + ((Element) n).getSubnodes().getType() + " found.");
      }
    }
  }
}