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
import cz.cuni.mff.ksi.jinfer.base.objects.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import java.util.ArrayList;
import java.util.List;

/**
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

  private static Element processCluster(final Pair<AbstractNode, List<AbstractNode>> cluster) {
    verify(cluster);

    final Element treeBase = (Element) cluster.getFirst();

    for (final AbstractNode n : cluster.getSecond()) {
      if (n != cluster.getFirst()) {
        addToTree(treeBase.getSubnodes(), ((Element) n).getSubnodes());
      }
    }

    return treeBase;
  }

  private static void verify(final Pair<AbstractNode, List<AbstractNode>> cluster) throws IllegalArgumentException {
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

  public static void addToTree(final Regexp<AbstractNode> tree, final Regexp<AbstractNode> what) {
    if (!RegexpType.CONCATENATION.equals(tree.getType())
            || !RegexpType.CONCATENATION.equals(what.getType())) {
      throw new IllegalArgumentException();
    }

    int i = -1;
    while (true) {
      final int check = i + 1;

      // when do we continue?
      if (check < tree.getChildren().size()
              && check < what.getChildren().size()
              && RegexpType.TOKEN.equals(tree.getChildren().get(check).getType())
              && equalTokens(tree.getChildren().get(check), what.getChildren().get(check))) {
        i++;
        continue;
      }

      if (check >= what.getChildren().size()) {
        return;
      }

      if (check >= tree.getChildren().size()) {
        // append a new alternation between all remaining items from what and an empty concatenation to tree
        final List<Regexp<AbstractNode>> alt = new ArrayList<Regexp<AbstractNode>>();
        alt.add(what.getEnd(check));
        alt.add(new Regexp<AbstractNode>(null, new ArrayList<Regexp<AbstractNode>>(), RegexpType.CONCATENATION));
        tree.getChildren().add(new Regexp<AbstractNode>(null, alt, RegexpType.ALTERNATION));
        return;
      }

      if (RegexpType.TOKEN.equals(tree.getChildren().get(check).getType())
              && !equalTokens(tree.getChildren().get(check), what.getChildren().get(check))) {
        tree.branch(check);
        tree.getChildren().get(check).getChildren().add(what.getEnd(check));
        return;
      }

      if (RegexpType.ALTERNATION.equals(tree.getChildren().get(check).getType())) {
        // walk all the items in the alternation
        //boolean found = false;
        for (final Regexp<AbstractNode> alternated : tree.getChildren().get(check).getChildren()) {
          if (alternated.getChildren().size() > 0
                  && equalTokens(alternated.getChildren().get(0), what.getChildren().get(check))) {
            // continue in this branch
            addToTree(alternated, what.getEnd(check));
            //found = true;
            //break;
            return;
          }
        }

        // if not found, simply add the end of "what" to the alternation
        //if (!found) {
          tree.getChildren().get(check).getChildren().add(what.getEnd(check));
          return;
        //}
      }

      i++;
    }
  }

  private static boolean equalTokens(Regexp<AbstractNode> t1, Regexp<AbstractNode> t2) {
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
}
