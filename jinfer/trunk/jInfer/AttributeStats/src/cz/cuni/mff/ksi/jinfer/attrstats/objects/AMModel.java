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
package cz.cuni.mff.ksi.jinfer.attrstats.objects;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.CloneHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * TODO vektor Comment!
 *
 * NOT thread-safe!
 *
 * @author vektor
 */
public class AMModel {

  private final List<Element> grammar;

  private List<Triplet> flat = null;

  private TreeNode tree = null;

  private final Map<AttributeMappingId, AttributeMapping> mappings = new HashMap<AttributeMappingId, AttributeMapping>();

  private Set<String> types = null;

  /**
   * TODO vektor Comment!
   *
   * @param grammar
   */
  public AMModel(final List<Element> grammar) {
    if (BaseUtils.isEmpty(grammar)) {
      throw new IllegalArgumentException("Grammar must not be empty");
    }
    final CloneHelper ch = new CloneHelper();
    this.grammar = ch.cloneGrammar(grammar);
  }

  /**
   * Extracts the attribute mapping from the underlyin grammar as a flat list
   * of {@link Triplet}s <code>(element name, attribute name, attribute content)</code>.
   *
   * <p>Note that the original content of the attribute is split into tokens on spaces.
   * For example an element <code>e</code> with attribute <code>a</code> that
   * contains the string <code>"1 p p"</code> in the original document will
   * produce 3 {@link Triplet}s, containing the <code>attribute content</code>
   * <code>1</code>, <code>p</code> and <code>p</code> respectively.</p>
   *
   * @return Flat representation of the attribute mapping contained in the
   * underlying grammar. Resulting list of {@link Triplet}s is sorted
   * (see {@link Triplet#compareTo(Triplet)}).
   */
  public List<Triplet> getFlat() {
    if (flat == null) {
      flat = new ArrayList<Triplet>();
      for (final Element e : grammar) {
        if (!BaseUtils.isEmpty(e.getAttributes())) {
          for (final Attribute a : e.getAttributes()) {
            for (final String c : a.getContent()) {
              final String[] values = c.split(" ");
              for (final String value : values) {
                flat.add(new Triplet(e.getName(), a.getName(), value));
              }
            }
          }
        }
      }

      Collections.sort(flat);
    }
    return Collections.unmodifiableList(flat);
  }

  /**
   * Extracts the attribute mapping from the underlying grammar as a tree ready
   * to be used in a {@link JTree}.
   *
   * <p>Please note that the attribute content is split into tokens on spaces,
   * see {@link MappingUtils#extractFlat } for details.</p>
   *
   * @return Tree representation of the attribute mapping contained in the
   * underlying grammar. Under the root node there are nodes representing each
   * {@link Element} in the grammar. The element nodes then contain nodes for
   * each of the {@link Attribute}s they contain.
   */
  public TreeNode getTree() {
    if (tree == null) {
      Collections.sort(grammar, BaseUtils.NAMED_NODE_COMPARATOR);

      tree = new DefaultMutableTreeNode("");
      for (final Element e : grammar) {
        if (!BaseUtils.isEmpty(e.getAttributes())) {
          final DefaultMutableTreeNode elementNode = new DefaultMutableTreeNode(e.getName());
          final List<Attribute> attributes = new ArrayList<Attribute>(e.getAttributes());
          Collections.sort(attributes, BaseUtils.NAMED_NODE_COMPARATOR);
          for (final Attribute a : attributes) {
            final List<String> content = new ArrayList<String>();
            for (final String oneContent : a.getContent()) {
              final String[] values = oneContent.split(" ");
              content.addAll(Arrays.asList(values));
            }

            final AttributeTreeNode attributeNode =
                    new AttributeTreeNode(e.getName(), a.getName(), content);
            elementNode.add(attributeNode);
          }
          ((DefaultMutableTreeNode)tree).add(elementNode);
        }
      }
    }
    return tree;
  }

  /**
   * TODO vektor Comment!
   *
   * @return
   */
  public Map<AttributeMappingId, AttributeMapping> getAMs() {
    if (mappings.isEmpty()) {
      for (final Triplet t : getFlat()) {
        final AttributeMappingId mapping = new AttributeMappingId(t.getElement(), t.getAttribute());
        if (!mappings.containsKey(mapping)) {
          mappings.put(mapping, new AttributeMapping(mapping));
        }
        mappings.get(mapping).getImage().add(t.getValue());
      }
    }
    return Collections.unmodifiableMap(mappings);
  }

  // TODO vektor JUnit test to verify that size() returns the same as getFlat().size()
  /**
   * TODO vektor Comment!
   *
   * @return
   */
  public int size() {
    int ret = 0;

    for (final AttributeMapping mapping : getAMs().values()) {
      ret += mapping.size();
    }

    return ret;
  }

  /**
   * TODO vektor Comment!
   * 
   * @return
   */
  public Set<String> getTypes() {
    if (types == null) {
      types = new HashSet<String>();
      for (final AttributeMappingId mapping : getAMs().keySet()) {
        types.add(mapping.getElement());
      }
    }
    return Collections.unmodifiableSet(types);
  }

}
