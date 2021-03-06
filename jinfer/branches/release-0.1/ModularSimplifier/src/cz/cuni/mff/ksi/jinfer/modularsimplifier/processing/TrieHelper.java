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
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import java.util.ArrayList;
import java.util.List;

/**
 * Trie helper - contains the logic for adding a tree to a Trie data structure.
 *
 * @author vektor
 */
public final class TrieHelper {

  private TrieHelper() {
  }

  /**
   * Adds a new branch (a concatenation) into a Trie (a prefix tree).
   *
   * @param tree Regular expression - concatenation representing a root node of a Trie.
   * @param branch Regular expression - concatenation representing a branch to be added to this trie.
   */
  public static void addBranchToTree(final Regexp<AbstractNode> tree,
          final Regexp<AbstractNode> branch) {
    if (!tree.isConcatenation()
            || !branch.isConcatenation()) {
      throw new IllegalArgumentException();
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
                && tree.getChild(posTree).getChild(0).isEmpty()) {
          return;
        }
        // branch the tree here and add an empty concatenation (lambda)
        tree.branch(posTree);
        final Regexp<AbstractNode> split = tree.getChild(posTree);
        // some acrobacy to have the lambda as
        // the first element in the alternation
        split.addChild(Regexp.<AbstractNode>getConcatenation());
        split.addChild(split.getChild(0));
        split.getChildren().remove(0);
        return;
      }

      // we have run out of the tree
      if (posTree >= tree.getChildren().size()) {
        // append a new alternation between all remaining items from
        // the branch and an empty concatenation (lambda) to tree
        final List<Regexp<AbstractNode>> alt = new ArrayList<Regexp<AbstractNode>>();
        alt.add(Regexp.<AbstractNode>getConcatenation());
        alt.add(branch.getEnd(posBranch));
        tree.addChild(Regexp.getAlternation(alt));
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
        for (final Regexp<AbstractNode> alternated : tree.getChild(posTree).getChildren()) {
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

  // TODO vektor Move to a common package
  public static boolean equalTokens(final Regexp<AbstractNode> t1,
          final Regexp<AbstractNode> t2) {
    // both need to be tokens
    if (!t1.isToken() || !t2.isToken()) {
      throw new IllegalArgumentException();
    }
    // if they are both simple data, TRUE
    if (t1.getContent().isSimpleData()
            && t2.getContent().isSimpleData()) {
      return true;
    }
    // if they are both elements of the same name, TRUE
    if (t1.getContent().isElement()
            && t2.getContent().isElement()
            && t1.getContent().getName().equalsIgnoreCase(t2.getContent().getName())) {
      return true;
    }
    if (t1.getContent().isAttribute()
            && t2.getContent().isAttribute()
            && t1.getContent().getName().equalsIgnoreCase(t2.getContent().getName())) {
      return true;
    }
    return false;
  }
}
