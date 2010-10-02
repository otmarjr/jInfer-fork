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
import cz.cuni.mff.ksi.jinfer.base.objects.Cluster;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
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

  private final Map<Element, Element> cloned = new HashMap<Element, Element>();

  public List<AbstractNode> cloneRules(final List<AbstractNode> l) {
    final List<AbstractNode> ret = new ArrayList<AbstractNode>(l.size());
    for (final AbstractNode n : l) {
      ret.add(cloneAbstractNode(n));
    }
    return ret;
  }

  public List<Cluster> cloneClusters(
          final List<Cluster> clusters) {
    final List<Cluster> ret = new ArrayList<Cluster>(clusters.size());
    for (final Cluster cluster : clusters) {
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
    if (cloned.containsKey(e)) {
      return cloned.get(e);
    }

    final Element clone;

    if (e.getSubnodes().isLambda()) {
      clone = new Element(cloneList(e.getContext()), String.valueOf(e.getName()), cloneMap(e.getMetadata()),
              Regexp.<AbstractNode>getLambda());
      cloned.put(e, clone);
      return clone;
    }

    clone = new Element(cloneList(e.getContext()), 
            String.valueOf(e.getName()),
            cloneMap(e.getMetadata()),
            Regexp.<AbstractNode>getMutable());
    cloned.put(e, clone);
    clone.getSubnodes().setInterval(e.getSubnodes().getInterval());

    if (e.getSubnodes().isToken()) {
      clone.getSubnodes().setType(RegexpType.TOKEN);
      final AbstractNode clonedToken = cloneAbstractNode(e.getSubnodes().getContent());
      clone.getSubnodes().setContent(clonedToken);
      clone.getSubnodes().setUnmutable();
      return clone;
    }

    clone.getSubnodes().setType(e.getSubnodes().getType());
    final Regexp<AbstractNode> clonedRegexp = cloneRegexp(e.getSubnodes());
    clone.getSubnodes().getChildren().addAll(clonedRegexp.getChildren());
    clone.getSubnodes().setUnmutable();
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
    return new Regexp<AbstractNode>(
            cloneAbstractNode(r.getContent()),
            (r.getChildren()),
            r.getType(),
            r.getInterval());
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

  private Cluster cloneCluster(final Cluster c) {
    return new Cluster(cloneAbstractNode(c.getRepresentant()), cloneRules(c.getContent()));
  }
}
