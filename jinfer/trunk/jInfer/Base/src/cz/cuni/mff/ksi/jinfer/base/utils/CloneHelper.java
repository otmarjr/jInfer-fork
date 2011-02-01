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
package cz.cuni.mff.ksi.jinfer.base.utils;

import cz.cuni.mff.ksi.jinfer.base.interfaces.nodes.NamedNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.SimpleData;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Utilities for cloning. Note that this class uses an inner cache of
 * already cloned elements to deal with cyclic dependencies, and should be used
 * to clone at most one full grammar. If not sure, create a new instance of this
 * class to clone something.
 * 
 * @author vektor
 */
public class CloneHelper {

  private final Map<Element, Element> cloned = new HashMap<Element, Element>();

  /**
   * Clones the whole grammar at once.
   * 
   * @param grammar Grammar to be cloned.
   * @return Cloned grammar: contains the same elements, but in different object
   * instances.
   */
  public List<Element> cloneGrammar(final List<Element> grammar) {
    final List<Element> ret = new ArrayList<Element>(grammar.size());
    for (final Element e : grammar) {
      ret.add(cloneElement(e));
    }
    return ret;
  }

  private static Attribute cloneAttribute(final Attribute a, final List<String> contextPrefix) {
    return new Attribute(getPrefixedContext(a, contextPrefix), String.valueOf(a.getName()),
            cloneMap(a.getMetadata()), String.valueOf(a.getContentType()),
            cloneList(a.getContent()));
  }

  private static List<Attribute> cloneAttributes(final List<Attribute> attrs, final List<String> contextPrefix) {
    final List<Attribute> ret = new ArrayList<Attribute>(attrs.size());
    for (final Attribute a : attrs) {
      ret.add(cloneAttribute(a, contextPrefix));
    }
    return ret;
  }

  private static SimpleData cloneSimpleData(final SimpleData s, final List<String> contextPrefix) {
    return new SimpleData(getPrefixedContext(s, contextPrefix), String.valueOf(s.getName()),
            cloneMap(s.getMetadata()), String.valueOf(s.getContentType()),
            cloneList(s.getContent()));
  }

  /**
   * Clones a single element.
   * This is done for all its children recursively.
   * @param e Element to be cloned.
   * @return Cloned element that is equal to the original, but different instance.
   * @see CloneHelper#cloneElement(cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element, java.util.List) 
   */
  public Element cloneElement(final Element e) {
    return cloneElement(e, null);
  }

  /**
   * Clones a single element. Context of every element is prefixed with the <code>contextPrefix</code>.
   * For example: if element A has children B,C and <code>contextPrefix</code> is <code>null</code> or empty, the contexts are
   * <ul>
   * <li>A</li>
   * <li>A/B</li>
   * <li>A/C</li>
   * </ul>
   * When the <code>contentPrefix</code> contains strings "X","Y","Z", the final contexts of the elements are
   * <ul>
   * <li>X/Y/X/A</li>
   * <li>X/Y/X/A/B</li>
   * <li>X/Y/X/A/C</li>
   * </ul>
   * respectively. This is done for all children recursively.
   *
   * @param e Element to be cloned.
   * @param contextPrefix Context to be used as prefix.
   * @return Cloned element that is equal to the original in everything except the new context, but different instance.
   */
  public Element cloneElement(final Element e, final List<String> contextPrefix) {
    if (cloned.containsKey(e)) {
      return cloned.get(e);
    }

    Element clone;

    if (e.getSubnodes().isLambda()) {
      clone = new Element(getPrefixedContext(e, contextPrefix), String.valueOf(e.getName()),
              cloneMap(e.getMetadata()), cloneRegexp(e.getSubnodes(), contextPrefix),
              cloneAttributes(e.getAttributes(), contextPrefix));
      cloned.put(e, clone);
      return clone;
    }

    clone = new Element(getPrefixedContext(e, contextPrefix),
            String.valueOf(e.getName()),
            cloneMap(e.getMetadata()),
            Regexp.<AbstractStructuralNode>getMutable(),
            cloneAttributes(e.getAttributes(), contextPrefix));
    cloned.put(e, clone);
    clone.getSubnodes().setInterval(e.getSubnodes().getInterval());

    if (e.getSubnodes().isToken()) {
      clone.getSubnodes().setType(RegexpType.TOKEN);
      final AbstractStructuralNode clonedToken = cloneAbstractNode(e.getSubnodes().getContent(), contextPrefix);
      clone.getSubnodes().setContent(clonedToken);
      clone.getSubnodes().setImmutable();
      return clone;
    }

    clone.getSubnodes().setType(e.getSubnodes().getType());
    final Regexp<AbstractStructuralNode> clonedRegexp = cloneRegexp(e.getSubnodes(), contextPrefix);
    clone.getSubnodes().getChildren().addAll(clonedRegexp.getChildren());
    clone.getSubnodes().setImmutable();
    return clone;
  }

  private static <T> List<T> cloneList(final List<T> l) {
    if (l == null) {
      return null;
    }
    return new ArrayList<T>(l);
  }

  private static Map<String, Object> cloneMap(final Map<String, Object> m) {
    if (m == null) {
      return null;
    }
    final Map<String, Object> ret = new HashMap<String, Object>(m.size());
    for (final Entry<String, Object> e : m.entrySet()) {
      // not a true clone, though :-(
      ret.put(String.valueOf(e.getKey()), e.getValue());
    }
    return ret;
  }

  /**
   * Clones a regular expression.
   * Elements are prefixed with the <code>contextPrefix</code> as in {@link CloneHelper#cloneElement(cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element, java.util.List) }.
   * @param r Regexp to be cloned.
   * @param contextPrefix Context to be used as prefix.
   * @return Cloned regexp: equal, but different instance.
   */
  public Regexp<AbstractStructuralNode> cloneRegexp(
          final Regexp<AbstractStructuralNode> r, final List<String> contextPrefix) {
    return new Regexp<AbstractStructuralNode>(
            cloneAbstractNode(r.getContent(), contextPrefix),
            cloneChildren(r.getChildren(), contextPrefix),
            r.getType(),
            r.getInterval());
  }

  private List<Regexp<AbstractStructuralNode>> cloneChildren(
          final List<Regexp<AbstractStructuralNode>> c, final List<String> contextPrefix) {
    if (c == null) {
      return null;
    }
    final List<Regexp<AbstractStructuralNode>> ret = new ArrayList<Regexp<AbstractStructuralNode>>(c.size());
    for (final Regexp<AbstractStructuralNode> r : c) {
      ret.add(cloneRegexp(r, contextPrefix));
    }
    return ret;
  }

  private AbstractStructuralNode cloneAbstractNode(final AbstractStructuralNode n, final List<String> contextPrefix) {
    if (n == null) {
      return null;
    }
    if (n instanceof SimpleData) {
      return cloneSimpleData((SimpleData) n, contextPrefix);
    }
    if (n instanceof Element) {
      return cloneElement((Element) n, contextPrefix);
    }
    throw new IllegalArgumentException("Unknown abstract node: " + n);
  }

  /**
   * Concatenate context from <code>contextPrefix</code> and the context of <code>node</code>
   * When <code>contextPrefix</code> is {"X","Y"} and the context of <code>node</code> is {"A", "B"},
   * method returns {"X","Y","A","B"}.
   * @param node Node from which the context is retrieved.
   * @param contextPrefix Context that should be appended before the <code>node</code> context.
   * @return New concatenated list. Otherwise,
   * the result will be equal to the context of <code>node</code> when <code>contextPrefix</code> is empty or <code>null</code>.
   * Result will be equal to <code>contextPrefix</code> if the context of <code>node</code> is empty or <code>null</code>.
   * Result will be <code>null</code> if both arguments are <code>null</code>.
   */
  public static List<String> getPrefixedContext(final NamedNode node, final List<String> contextPrefix) {
    if (BaseUtils.isEmpty(contextPrefix)) {
      return cloneList(node.getContext());
    } else {
      final List<String> res = cloneList(contextPrefix);
      if (node.getContext() != null) {
        res.addAll(cloneList(node.getContext()));
      }
      return res;
    }
  }
}
