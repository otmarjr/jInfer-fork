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

  public List<Element> cloneRules(final List<Element> l) {
    final List<Element> ret = new ArrayList<Element>(l.size());
    for (final Element n : l) {
      ret.add(cloneElement(n));
    }
    return ret;
  }

  private static Attribute cloneAttribute(final Attribute a) {
    return new Attribute(cloneList(a.getContext()), String.valueOf(a.getName()),
            cloneMap(a.getMetadata()), String.valueOf(a.getContentType()),
            cloneList(a.getContent()));
  }

  private static List<Attribute> cloneAttributes(final List<Attribute> attrs) {
    final List<Attribute> ret = new ArrayList<Attribute>(attrs.size());
    for (final Attribute a : attrs) {
      ret.add(cloneAttribute(a));
    }
    return ret;
  }

  private static SimpleData cloneSimpleData(final SimpleData s) {
    return new SimpleData(cloneList(s.getContext()), String.valueOf(s.getName()),
            cloneMap(s.getMetadata()), String.valueOf(s.getContentType()),
            cloneList(s.getContent()));
  }

  private Element cloneElement(final Element e) {
    if (cloned.containsKey(e)) {
      return cloned.get(e);
    }

    Element clone;

    if (e.getSubnodes().isLambda()) {
      clone = new Element(cloneList(e.getContext()), String.valueOf(e.getName()),
              cloneMap(e.getMetadata()), cloneRegexp(e.getSubnodes()),
              cloneAttributes(e.getAttributes()));
      cloned.put(e, clone);
      return clone;
    }

    clone = new Element(cloneList(e.getContext()),
            String.valueOf(e.getName()),
            cloneMap(e.getMetadata()),
            Regexp.<AbstractStructuralNode>getMutable(),
            cloneAttributes(e.getAttributes()));
    cloned.put(e, clone);
    clone.getSubnodes().setInterval(e.getSubnodes().getInterval());

    if (e.getSubnodes().isToken()) {
      clone.getSubnodes().setType(RegexpType.TOKEN);
      final AbstractStructuralNode clonedToken = cloneAbstractNode(e.getSubnodes().getContent());
      clone.getSubnodes().setContent(clonedToken);
      clone.getSubnodes().setImmutable();
      return clone;
    }

    clone.getSubnodes().setType(e.getSubnodes().getType());
    final Regexp<AbstractStructuralNode> clonedRegexp = cloneRegexp(e.getSubnodes());
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

  private Regexp<AbstractStructuralNode> cloneRegexp(
          final Regexp<AbstractStructuralNode> r) {
    return new Regexp<AbstractStructuralNode>(
            cloneAbstractNode(r.getContent()),
            cloneChildren(r.getChildren()),
            r.getType(),
            r.getInterval());
  }

  private List<Regexp<AbstractStructuralNode>> cloneChildren(
          final List<Regexp<AbstractStructuralNode>> c) {
    if (c == null) {
      return null;
    }
    final List<Regexp<AbstractStructuralNode>> ret = new ArrayList<Regexp<AbstractStructuralNode>>(c.size());
    for (final Regexp<AbstractStructuralNode> r : c) {
      ret.add(cloneRegexp(r));
    }
    return ret;
  }

  private AbstractStructuralNode cloneAbstractNode(final AbstractStructuralNode n) {
    if (n == null) {
      return null;
    }
    if (n instanceof SimpleData) {
      return cloneSimpleData((SimpleData) n);
    }
    if (n instanceof Element) {
      return cloneElement((Element) n);
    }
    throw new IllegalArgumentException("Unknown abstract node: " + n);
  }
}
