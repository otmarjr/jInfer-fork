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

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.base.objects.SimpleData;
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

  private final List<Pair<Element, Element>> cloned = new ArrayList<Pair<Element, Element>>();

  public List<AbstractNode> cloneRules(final List<AbstractNode> l) {
    final List<AbstractNode> ret = new ArrayList<AbstractNode>(l.size());
    for (final AbstractNode n : l) {
      ret.add(cloneAbstractNode(n));
    }
    return ret;
  }

  public List<Pair<AbstractNode, List<AbstractNode>>> cloneClusters(
          final List<Pair<AbstractNode, List<AbstractNode>>> clusters) {
    final List<Pair<AbstractNode, List<AbstractNode>>> ret = new ArrayList<Pair<AbstractNode, List<AbstractNode>>>(clusters.size());
    for (final Pair<AbstractNode, List<AbstractNode>> cluster : clusters) {
      ret.add(cloneCluster(cluster));
    }
    return ret;
  }

  private static Attribute cloneAttribute(final Attribute a) {
    return new Attribute(cloneList(a.getContext()), String.valueOf(a.getName()), cloneMap(a.getMetadata()), String.valueOf(a.getContentType()), cloneList(a.getContent()));
  }

  private static SimpleData cloneSimpleData(final SimpleData s) {
    return new SimpleData(cloneList(s.getContext()), String.valueOf(s.getName()), cloneMap(s.getMetadata()), String.valueOf(s.getContentType()), cloneList(s.getContent()));
  }

  private Element cloneElement(final Element e) {
    for (final Pair<Element, Element> p : cloned) {
      if (e == p.getFirst()) {
        return p.getSecond();
      }
    }

    final Element clone;
    if (e.getSubnodes().isToken()) {
      clone = new Element(cloneList(e.getContext()), String.valueOf(e.getName()), cloneMap(e.getMetadata()), new Regexp<AbstractNode>(null, null, RegexpType.TOKEN));
      cloned.add(new Pair<Element, Element>(e, clone));
      final AbstractNode clonedToken = cloneAbstractNode(e.getSubnodes().getContent());
      clone.getSubnodes().setContent(clonedToken);
    } else {
      clone = new Element(cloneList(e.getContext()), String.valueOf(e.getName()), cloneMap(e.getMetadata()), new Regexp<AbstractNode>(null, new ArrayList<Regexp<AbstractNode>>(), e.getSubnodes().getType()));
      cloned.add(new Pair<Element, Element>(e, clone));
      final Regexp<AbstractNode> clonedRegexp = cloneRegexp(e.getSubnodes());
      clone.getSubnodes().getChildren().addAll(clonedRegexp.getChildren());
    }

    return clone;
  }

  private static List<String> cloneList(final List<String> l) {
    if (l == null) {
      return null;
    }
    return new ArrayList<String>(l);
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

  private Regexp<AbstractNode> cloneRegexp(final Regexp<AbstractNode> r) {
    return new Regexp<AbstractNode>(cloneAbstractNode(r.getContent()), cloneChildren(r.getChildren()), r.getType());
  }

  private AbstractNode cloneAbstractNode(final AbstractNode n) {
    if (n == null) {
      return null;
    }
    if (n instanceof Attribute) {
      return cloneAttribute((Attribute) n);
    }
    if (n instanceof SimpleData) {
      return cloneSimpleData((SimpleData) n);
    }
    if (n instanceof Element) {
      return cloneElement((Element) n);
    }
    throw new IllegalArgumentException("Unknown abstract node: " + n);
  }

  private List<Regexp<AbstractNode>> cloneChildren(final List<Regexp<AbstractNode>> c) {
    final List<Regexp<AbstractNode>> ret = new ArrayList<Regexp<AbstractNode>>(c.size());
    for (final Regexp<AbstractNode> r : c) {
      ret.add(cloneRegexp(r));
    }
    return ret;
  }

  private Pair<AbstractNode, List<AbstractNode>> cloneCluster(final Pair<AbstractNode, List<AbstractNode>> c) {
    return new Pair<AbstractNode, List<AbstractNode>>(cloneAbstractNode(c.getFirst()), cloneRules(c.getSecond()));
  }
}
