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

  private final List<Element> visited = new ArrayList<Element>();

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

    // walk the tree and simplify it
    return simplify(treeBase);
  }

  private static void verify(final Pair<AbstractNode, List<AbstractNode>> cluster) {
    for (final AbstractNode n : cluster.getSecond()) {
      if (!n.getType().equals(NodeType.ELEMENT)) {
        throw new IllegalArgumentException("Element expected");
      }
      final Element e = (Element) n;
      if (!e.getSubnodes().getType().equals(RegexpType.CONCATENATION)) {
        throw new IllegalArgumentException("Concatenation expected");
      }
    }
  }

  public static void addBranchToTree(final Regexp<AbstractNode> tree, final Regexp<AbstractNode> branch) {
    if (!RegexpType.CONCATENATION.equals(tree.getType())
            || !RegexpType.CONCATENATION.equals(branch.getType())) {
      throw new IllegalArgumentException();
    }

    int posTre = -1;
    int posBra = -1;
    while (true) {
      final int posTree = posTre + 1;
      final int posBranch = posBra + 1;

      // if we are not on an Element in the tree...
      if (posTree < tree.getChildren().size()
              && RegexpType.TOKEN.equals(tree.getChild(posTree).getType())
              && !NodeType.ELEMENT.equals(tree.getChild(posTree).getContent().getType())) {
        // move on
        posTre++;
        continue;
      }

      // if we are not on an Element in the what...
      if (posBranch < branch.getChildren().size()
              && !NodeType.ELEMENT.equals(branch.getChild(posBranch).getContent().getType())) {
        // move on
        posBra++;
        continue;
      }

      // when do we continue?
      if (posTree < tree.getChildren().size()
              && posBranch < branch.getChildren().size()
              && RegexpType.TOKEN.equals(tree.getChild(posTree).getType())
              && equalTokens(tree.getChild(posTree), branch.getChild(posBranch))) {
        posTre++;
        posBra++;
        continue;
      }

      if (posBranch >= branch.getChildren().size()) {
        return;
      }

      if (posTree >= tree.getChildren().size()) {
        // append a new alternation between all remaining items from what and an empty concatenation to tree
        final List<Regexp<AbstractNode>> alt = new ArrayList<Regexp<AbstractNode>>();
        alt.add(new Regexp<AbstractNode>(null, new ArrayList<Regexp<AbstractNode>>(), RegexpType.CONCATENATION));
        alt.add(branch.getEnd(posBranch));
        tree.addChild(new Regexp<AbstractNode>(null, alt, RegexpType.ALTERNATION));
        return;
      }

      if (RegexpType.TOKEN.equals(tree.getChild(posTree).getType())
              && !equalTokens(tree.getChild(posTree), branch.getChild(posBranch))) {
        tree.branch(posTree);
        tree.getChild(posTree).addChild(branch.getEnd(posBranch));
        return;
      }

      if (RegexpType.ALTERNATION.equals(tree.getChild(posTree).getType())) {
        // walk all the items in the alternation
        for (final Regexp<AbstractNode> alternated : tree.getChild(posTree).getChildren()) {
          if (alternated.getChildren().size() > 0
                  && equalTokens(alternated.getChild(0), branch.getChild(posBranch))) {
            // continue in this branch
            addBranchToTree(alternated, branch.getEnd(posBranch));
            return;
          }
        }

        // if not found, simply add the end of "what" to the alternation
        tree.getChild(posTree).addChild(branch.getEnd(posBranch));
        return;
      }

      posTre++;
      posBra++;
    }
  }

  // TODO move to a common package
  public static boolean equalTokens(final Regexp<AbstractNode> t1, final Regexp<AbstractNode> t2) {
    if (!RegexpType.TOKEN.equals(t1.getType())
            || !RegexpType.TOKEN.equals(t2.getType())) {
      throw new IllegalArgumentException();
    }
    if (NodeType.SIMPLE_DATA.equals(t1.getContent().getType())
            && NodeType.SIMPLE_DATA.equals(t2.getContent().getType())) {
      return true;
    }
    if (NodeType.ELEMENT.equals(t1.getContent().getType())
            && NodeType.ELEMENT.equals(t2.getContent().getType())
            && ((Element) t1.getContent()).getName().equalsIgnoreCase(((Element) t2.getContent()).getName())) {
      return true;
    }
    if (NodeType.ATTRIBUTE.equals(t1.getContent().getType())
            && NodeType.ATTRIBUTE.equals(t2.getContent().getType())
            && ((Attribute) t1.getContent()).getName().equalsIgnoreCase(((Attribute) t2.getContent()).getName())) {
      return true;
    }
    return false;
  }

  private Element simplify(final Element treeBase) {
    for (final Element v : visited) {
      if (v == treeBase) {
        return treeBase;
      }
    }
    visited.add(treeBase);
    return new Element(
            treeBase.getContext(),
            treeBase.getName(),
            treeBase.getAttributes(),
            simplify(treeBase.getSubnodes()));
  }

  private Regexp<AbstractNode> simplify(final Regexp<AbstractNode> regexp) {
    switch (regexp.getType()) {
      case TOKEN:
        if (NodeType.ELEMENT.equals(regexp.getContent().getType())) {
          return Regexp.<AbstractNode>getToken(simplify((Element)regexp.getContent()));
        }
        return regexp;
      case ALTERNATION:
      case CONCATENATION:
        if (regexp.getChildren().size() == 1) {
          return simplify(regexp.getChild(0));
        }
        final List<Regexp<AbstractNode>> children = new ArrayList<Regexp<AbstractNode>>();
        for (final Regexp<AbstractNode> child : regexp.getChildren()) {
          children.add(simplify(child));
        }
        return new Regexp<AbstractNode>(null, children, regexp.getType());
      default: return regexp;
    }
  }
}
