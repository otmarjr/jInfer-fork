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
package cz.cuni.mff.ksi.jinfer.iss.objects;

import cz.cuni.mff.ksi.jinfer.iss.utils.MappingUtils;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.CloneHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * Class representing a model for attribute mappings operations. It is
 * constructed from a specified grammar and provides various views of the
 * mappings in that grammar, notably:
 * <ul>
 *    <li>
 *      Flat view, returned by the {@link AMModel#getFlat() } method:
 *      attribute mappings are returned as a list of {@link Triplet}s.
 *    </li>
 *    <li>
 *      Tree view ready to be used in {@link JTree}, returned by the
 *      {@link AMModel#getTree() }.
 *    </li>
 *    <li>
 *      Map view, returned by the {@link AMModel#getAMs() } method:
 *      mappings are returned in a map indexed by their IDs
 *      ({@link AttributeMappingId }) containing the full mappings
 *      ({@link AttributeMapping}).
 *    </li>
 * </ul>
 *
 * Most of these methods cache their results (the input grammar cannot change
 * later anyway) and return them lazily. Be aware though that the lazy
 * implementations are <strong>not</strong> thread-safe!
 *
 * @author vektor
 */
public class AMModel {

  private final List<Element> grammar;

  private List<Triplet> flat = null;

  private TreeNode tree = null;

  private Map<AttributeMappingId, AttributeMapping> mappings = null;

  private Set<String> types = null;

  /**
   * Full constructor.
   *
   * @param grammar Grammar from which the model should be created.
   */
  public AMModel(final List<Element> grammar) {
    if (BaseUtils.isEmpty(grammar)) {
      throw new IllegalArgumentException("Grammar must not be empty");
    }
    final CloneHelper ch = new CloneHelper();
    this.grammar = ch.cloneGrammar(grammar);
  }

  /**
   * Extracts the attribute mapping from the underlying grammar as a flat list
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
   * Returns all attribute mappings in the underlying grammar as a map
   * indexed by their IDs ({@link AttributeMappingId }) containing the full
   * mappings ({@link AttributeMapping}).
   *
   * @return Map of all the attribute mappings.
   */
  public Map<AttributeMappingId, AttributeMapping> getAMs() {
    if (mappings == null) {
      mappings = new HashMap<AttributeMappingId, AttributeMapping>();
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

  /**
   * Returns the total size of all attribute mappings represented in this model.
   *
   * @return Size of all the attribute mappings in this model. It is the sum
   * of sizes of all the mappings represented.
   */
  public int size() {
    int ret = 0;

    for (final AttributeMapping mapping : getAMs().values()) {
      ret += mapping.size();
    }

    return ret;
  }

  /**
   * Returns all the element <cite>types</cite> represented in this model.
   *
   * @return Set of all element types (names) in this model.
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

  /**
   * Returns the weight of the provided AM in the context of this AM model
   * as a weighted sum of its <cite>support</cite> and <cite>coverage</cite>.
   *
   * @param mapping Mapping to compute weight for.
   * @param alpha Weight of the mapping <cite>support</cite>.
   * @param beta Weight of the mapping <cite>coverage</cite>.
   * @return Weight of the provided mapping in the context this model.
   *
   * @see MappingUtils#support(AttributeMappingId, AMModel)
   * @see MappingUtils#coverage(AttributeMappingId, AMModel)
   */
  public double weight(final AttributeMappingId mapping,
          final double alpha, final double beta) {
    return alpha * support(mapping)
            + beta * coverage(mapping);
  }

  /**
   * Returns the sum of weights of all the mappings provided in the context
   * this AM model.
   *
   * @param mappings List of the mappings to calculate the weight for.
   * @param alpha Weight of the mapping <cite>support</cite>.
   * @param beta Weight of the mapping <cite>coverage</cite>.
   * @return Sum of all the weights of all the provided mappings.
   *
   * @see MappingUtils#weight(AttributeMappingId, AMModel)
   */
  public double weight(final Collection<AttributeMappingId> mappings,
          final double alpha, final double beta) {
    double sum = 0.0;
    for (final AttributeMappingId mapping : mappings) {
      sum += weight(mapping, alpha, beta);
    }
    return sum;
  }

  /**
   * Please see the "Finding ID Attributes in XML Documents" article for the
   * definition of this function.
   *
   * @param targetMapping Mapping for which <cite>support</cite> should be calculated.
   * @return Support of the specified mapping.
   */
  public double support(final AttributeMappingId targetMapping) {
    verify(targetMapping);
    return (double)getAMs().get(targetMapping).size() / size();
  }

  /**
   * Please see the "Finding ID Attributes in XML Documents" article for the
   * definition of this function.
   *
   * @param targetMapping Mapping for which <cite>coverage</cite> should be calculated.
   * @return Coverage of the specified mapping.
   */
  public double coverage(final AttributeMappingId targetMapping) {
    verify(targetMapping);

    double sum1 = 0;

    for (final Map.Entry<AttributeMappingId, AttributeMapping> mapping : getAMs().entrySet()) {
      if (!mapping.getKey().equals(targetMapping)) {
        sum1 += BaseUtils.intersect(
                new HashSet<String>(mapping.getValue().getImage()),
                new HashSet<String>(getAMs().get(targetMapping).getImage())).size();
      }
    }

    return sum1 / size();
  }

  private void verify(final AttributeMappingId targetMapping) {
    if (targetMapping == null) {
      throw new IllegalArgumentException("Expecting non-null, non empty parameters");
    }
    if (!getAMs().containsKey(targetMapping)) {
      throw new IllegalArgumentException("Target mapping not found in the model.");
    }
  }

}
