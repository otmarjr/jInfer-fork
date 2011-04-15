/*
 * Copyright (C) 2011 vektor
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.attrstats;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * Library class for attribute mapping extraction.
 *
 * @author vektor
 */
public final class MappingExtractor {

  private MappingExtractor() {

  }

  /**
   * Extracts the attribute mapping from the specified grammar as a flat list
   * of {@link Triplet}s <code>(element name, attribute name, attribute content)</code>.
   *
   * @param grammar Grammar from which the attribute mapping should be retrieved.
   * @return Flat representation of the attribute mapping contained in the
   * specified grammar. Resulting list of {@link Triplet}s is sorted
   * (see {@link Triplet#compareTo(Triplet)}).
   */
  public static List<Triplet> extractFlat(final List<Element> grammar) {
    final List<Triplet> ret = new ArrayList<Triplet>();
    for (final Element e : grammar) {
      if (!BaseUtils.isEmpty(e.getAttributes())) {
        for (final Attribute a : e.getAttributes()) {
          for (final String c : a.getContent()) {
            ret.add(new Triplet(e.getName(), a.getName(), c));
          }
        }
      }
    }

    Collections.sort(ret);

    return ret;
  }

  /**
   * Extracts the attribute mapping from the specified grammar as a tree ready
   * to be used in a {@link JTree}.
   *
   * @param grammar Grammar from which the attribute mapping should be retrieved.
   * @return Tree representation of the attribute mapping contained in the
   * specified grammar. Under the root node there are nodes representing each
   * {@link Element} in the grammar. The element nodes then contain nodes for
   * each of the {@link Attribute}s they contain.
   */
  public static TreeNode createTree(final List<Element> grammar) {
    Collections.sort(grammar, BaseUtils.NAMED_NODE_COMPARATOR);

    final DefaultMutableTreeNode ret = new DefaultMutableTreeNode("");
    for (final Element e : grammar) {
      if (!BaseUtils.isEmpty(e.getAttributes())) {
        final DefaultMutableTreeNode elementNode = new DefaultMutableTreeNode(e.getName());
        final List<Attribute> attributes = new ArrayList<Attribute>(e.getAttributes());
        Collections.sort(attributes, BaseUtils.NAMED_NODE_COMPARATOR);
        for (final Attribute a : attributes) {
          final AttributeTreeNode attributeNode =
                  new AttributeTreeNode(e.getName(), a.getName(), a.getContent());
          elementNode.add(attributeNode);
        }
        ret.add(elementNode);
      }
    }

    return ret;
  }

}
