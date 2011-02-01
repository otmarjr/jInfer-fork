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
package cz.cuni.mff.ksi.jinfer.twostep.processing.trie;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.twostep.clustering.Clusterer;
import cz.cuni.mff.ksi.jinfer.twostep.processing.ClusterProcessor;
import java.util.List;

/**
 * Trie (prefix tree) implementation of
 * {@link cz.cuni.mff.ksi.jinfer.twostep.processing.ClusterProcessor}.
 * 
 * @author vektor
 */
public class Trie implements ClusterProcessor<AbstractStructuralNode> {

  @Override
  public AbstractStructuralNode processCluster(
          final Clusterer<AbstractStructuralNode> clusterer,
          final List<AbstractStructuralNode> rules) throws InterruptedException {
    final Element ret = Element.getMutable();

    final Element first = (Element) rules.get(0);

    ret.setName(first.getName());
    ret.getContext().addAll(first.getContext());

    ret.getSubnodes().setType(RegexpType.CONCATENATION);
    ret.getSubnodes().setInterval(RegexpInterval.getOnce());

    ret.getSubnodes().getChildren().addAll(first.getSubnodes().getChildren());

    for (final AbstractStructuralNode n : rules) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      if (!n.isElement()) {
        throw new IllegalArgumentException("Expecting element here.");
      }
      final Element e = (Element) n;

      addBranchToTree(ret.getSubnodes(), e.getSubnodes());
    }

    // TODO vektor Probably losing attributes

    ret.setImmutable();

    return ret;
  }

  /**
   * TODO vektor comment
   * 
   * @param tree
   * @param branch
   */
  public static void addBranchToTree(final Regexp<AbstractStructuralNode> tree,
          final Regexp<AbstractStructuralNode> branch) {
    if (!tree.isConcatenation()) {
      throw new IllegalArgumentException("Tree must be concatenation, is " + tree.getType() + " instead.");
    }
    if (!branch.isConcatenation()) {
      throw new IllegalArgumentException("Branch must be concatenation, is " + branch.getType() + " instead.");
    }

    int posTree = 0;
    int posBranch = 0;
    while (true) {

      // if we are not on an Element in the tree...
      if (posTree < tree.getChildren().size()
              && tree.getChild(posTree).isToken()
              && !tree.getChild(posTree).getContent().isElement()) {
        // move on
        posTree++;
        continue;
      }

      // if we are not on an Element in the branch...
      if (posBranch < branch.getChildren().size()
              && !branch.getChild(posBranch).getContent().isElement()) {
        if (branch.getChild(posBranch).getContent().isSimpleData()) {
          tree.getChildren().add(posTree, branch.getChild(posBranch));
          posTree++;
        }
        // move on
        posBranch++;
        continue;
      }

      // when do we continue?
      //  when we are not out of tree or branch,
      //  pointing on a token in the tree,
      //  and the tokens in the tree and the branch are equal
      if (posTree < tree.getChildren().size()
              && posBranch < branch.getChildren().size()
              && tree.getChild(posTree).isToken()
              && equalTokens(tree.getChild(posTree),
              branch.getChild(posBranch))) {
        posTree++;
        posBranch++;
        continue;
      }

      if (posBranch >= branch.getChildren().size()
              && posTree >= tree.getChildren().size()) {
        return;
      }

      // we have run out of the branch
      if (posBranch >= branch.getChildren().size()) {
        // verify that we need to do additional steps
        if (tree.getChild(posTree).isAlternation()
                && tree.getChild(posTree).getChild(0).isLambda()) {
          return;
        }
        // branch the tree here and add an empty concatenation (lambda)
        tree.branch(posTree);
        final Regexp<AbstractStructuralNode> split = tree.getChild(posTree);
        // some acrobacy to have the lambda as
        // the first element in the alternation
        split.addChild(Regexp.<AbstractStructuralNode>getLambda());
        split.addChild(split.getChild(0));
        split.getChildren().remove(0);
        return;
      }

      // we have run out of the tree
      if (posTree >= tree.getChildren().size()) {
        // append a new alternation between all remaining items from
        // the branch and an empty concatenation (lambda) to tree
        final Regexp<AbstractStructuralNode> altRE = Regexp.getMutable();
        altRE.setType(RegexpType.ALTERNATION);
        altRE.addChild(Regexp.<AbstractStructuralNode>getLambda());
        altRE.addChild(branch.getEnd(posBranch));
        altRE.setInterval(RegexpInterval.getOnce());
        tree.addChild(altRE);
        return;
      }

      // we have found a position where tree and branch differ
      if (tree.getChild(posTree).isToken()
              && !equalTokens(tree.getChild(posTree), branch.getChild(posBranch))) {
        tree.branch(posTree);
        tree.getChild(posTree).addChild(branch.getEnd(posBranch));
        return;
      }

      if (tree.getChild(posTree).isAlternation()) {
        // walk all the items in the alternation
        for (final Regexp<AbstractStructuralNode> alternated : tree.getChild(posTree).getChildren()) {
          if (alternated.getChildren().size() > 0
                  && equalTokens(alternated.getChild(0),
                  branch.getChild(posBranch))) {
            // continue in this branch
            addBranchToTree(alternated, branch.getEnd(posBranch));
            return;
          }
        }

        // if not found, simply add the end of the branch to the alternation
        tree.getChild(posTree).addChild(branch.getEnd(posBranch));
        return;
      }

      posTree++;
      posBranch++;
    }
  }

  /**
   * Decides whether the two provided tokens are "the same".
   *
   * <p>
   * Two tokens are considered to be same iff:
   *
   * <ul>
   *   <li>If they are both simple data, or</li>
   *   <li>If they are both elements of the same name (case <strong>insensitive</strong>), or</li>
   *   <li>If they are both attributes of the same name (case <strong>insensitive</strong>).</li>
   * </ul>
   * </p>
   *
   * @param t1 First regexp - token.
   * @param t2 Second regexp - token.
   * @return True, if tokens are equal in the sense described above. False otherwise.
   * @throws IllegalArgumentException When one of the regexps is not a token.
   */
  private static boolean equalTokens(final Regexp<AbstractStructuralNode> t1,
          final Regexp<AbstractStructuralNode> t2) {
    if (!t1.isToken() || !t2.isToken()) {
      throw new IllegalArgumentException();
    }
    if (t1.getContent().isSimpleData()
            && t2.getContent().isSimpleData()) {
      return true;
    }
    if (t1.getContent().isElement()
            && t2.getContent().isElement()
            && t1.getContent().getName().equalsIgnoreCase(t2.getContent().getName())) {
      return true;
    }
    return false;
  }
}
