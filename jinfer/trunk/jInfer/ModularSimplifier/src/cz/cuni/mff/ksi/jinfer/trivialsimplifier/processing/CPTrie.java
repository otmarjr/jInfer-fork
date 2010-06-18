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
package cz.cuni.mff.ksi.jinfer.trivialsimplifier.processing;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import java.util.ArrayList;
import java.util.List;

/**
 * Cluster processor implementation that exports a cluster as a trie (prefix tree).
 * 
 * @author vektor
 */
public class CPTrie implements ClusterProcessor {

  @Override
  public List<AbstractNode> processClusters(final List<Pair<AbstractNode, List<AbstractNode>>> clusters) {
    final List<AbstractNode> ret = new ArrayList<AbstractNode>();

    for (final Pair<AbstractNode, List<AbstractNode>> cluster : clusters) {
      ret.add(processCluster(cluster));
    }

    return ret;
  }

  private Element processCluster(final Pair<AbstractNode, List<AbstractNode>> cluster) {
    verify(cluster);

    final Element treeBase = (Element) cluster.getFirst();

    // put every item from the cluster into the trie
    for (final AbstractNode n : cluster.getSecond()) {
      if (n != cluster.getFirst()) {
        addBranchToTree(treeBase.getSubnodes(), ((Element) n).getSubnodes());
      }
    }

    // walk the tree and shorten its concatenations/alternations
    return (new Shortener()).simplify(treeBase);
  }

  private static void verify(final Pair<AbstractNode, List<AbstractNode>> cluster) {
    for (final AbstractNode n : cluster.getSecond()) {
      if (!n.isElement()) {
        throw new IllegalArgumentException("Element expected");
      }
      final Element e = (Element) n;
      if (!e.getSubnodes().isConcatenation()) {
        throw new IllegalArgumentException("Concatenation expected");
      }
    }
  }

  public static void addBranchToTree(final Regexp<AbstractNode> tree, final Regexp<AbstractNode> branch) {
    if (!tree.isConcatenation()
            || !branch.isConcatenation()) {
      throw new IllegalArgumentException();
    }

    int posTre = -1;
    int posBra = -1;
    while (true) {
      final int posTree = posTre + 1;
      final int posBranch = posBra + 1;

      // if we are not on an Element in the tree...
      if (posTree < tree.getChildren().size()
              && tree.getChild(posTree).isToken()
              && !tree.getChild(posTree).getContent().isElement()) {
        // move on
        posTre++;
        continue;
      }

      // if we are not on an Element in the branch...
      if (posBranch < branch.getChildren().size()
              && !branch.getChild(posBranch).getContent().isElement()) {
        // move on
        posBra++;
        continue;
      }

      // when do we continue?
      //  when we are not out of tree or branch,
      //  pointing on a token in the tree,
      //  and the tokens in the tree and the branch are equal
      if (posTree < tree.getChildren().size()
              && posBranch < branch.getChildren().size()
              && tree.getChild(posTree).isToken()
              && equalTokens(tree.getChild(posTree), branch.getChild(posBranch))) {
        posTre++;
        posBra++;
        continue;
      }

      if (posBranch >= branch.getChildren().size()) {
        return;
      }

      if (posTree >= tree.getChildren().size()) {
        // append a new alternation between all remaining items from the branch and an empty concatenation to tree
        final List<Regexp<AbstractNode>> alt = new ArrayList<Regexp<AbstractNode>>();
        alt.add(new Regexp<AbstractNode>(null, new ArrayList<Regexp<AbstractNode>>(), RegexpType.CONCATENATION));
        alt.add(branch.getEnd(posBranch));
        tree.addChild(new Regexp<AbstractNode>(null, alt, RegexpType.ALTERNATION));
        return;
      }

      if (tree.getChild(posTree).isToken()
              && !equalTokens(tree.getChild(posTree), branch.getChild(posBranch))) {
        tree.branch(posTree);
        tree.getChild(posTree).addChild(branch.getEnd(posBranch));
        return;
      }

      if (tree.getChild(posTree).isAlternation()) {
        // walk all the items in the alternation
        for (final Regexp<AbstractNode> alternated : tree.getChild(posTree).getChildren()) {
          if (alternated.getChildren().size() > 0
                  && equalTokens(alternated.getChild(0), branch.getChild(posBranch))) {
            // continue in this branch
            addBranchToTree(alternated, branch.getEnd(posBranch));
            return;
          }
        }

        // if not found, simply add the end of the branch to the alternation
        tree.getChild(posTree).addChild(branch.getEnd(posBranch));
        return;
      }

      posTre++;
      posBra++;
    }
  }

  // TODO move to a common package
  public static boolean equalTokens(final Regexp<AbstractNode> t1, final Regexp<AbstractNode> t2) {
    if (!t1.isToken() || !t2.isToken()) {
      throw new IllegalArgumentException();
    }
    if (t1.getContent().isSimpleData()
            && t2.getContent().isSimpleData()) {
      return true;
    }
    if (t1.getContent().isElement()
            && t2.getContent().isElement()
            && ((Element) t1.getContent()).getName().equalsIgnoreCase(((Element) t2.getContent()).getName())) {
      return true;
    }
    if (t1.getContent().isAttribute()
            && t2.getContent().isAttribute()
            && ((Attribute) t1.getContent()).getName().equalsIgnoreCase(((Attribute) t2.getContent()).getName())) {
      return true;
    }
    return false;
  }

}
