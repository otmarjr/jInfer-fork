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
 * Utilities for rule cloning.
 * 
 * @author vektor
 */
public class CloneHelper {

  private final Map<Element, Element> cloned = new HashMap<Element, Element>();

  public List<Element> cloneRules(final List<Element> l, final List<String> contextPrefix) {
    final List<Element> ret = new ArrayList<Element>(l.size());
    for (final Element n : l) {
      ret.add(cloneElement(n, null));
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

  private Element cloneElement(final Element e, final List<String> contextPrefix) {
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

  private static List<String> getPrefixedContext (final NamedNode node, final List<String> contextPrefix) {
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
