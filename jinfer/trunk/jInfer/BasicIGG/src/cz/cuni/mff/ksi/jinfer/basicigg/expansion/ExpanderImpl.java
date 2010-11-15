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
package cz.cuni.mff.ksi.jinfer.basicigg.expansion;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Expander;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * A simple implementation of the Expander interface.
 * 
 * @author vektor
 */
@ServiceProvider(service = Expander.class)
public class ExpanderImpl implements Expander {

  private static final Logger LOG = Logger.getLogger(ExpanderImpl.class);

  @Override
  public List<Element> expand(final List<Element> grammar) {
    final List<Element> ret = new ArrayList<Element>();

    for (final Element e : grammar) {
      ret.addAll(expandRule(e));
    }

    return ret;
  }

  private static List<Element> expandRule(final Element e) {
    if (alreadyOk(e)) {
      LOG.info("Element \"" + e.getName() + "\" already expanded.");
      return Arrays.asList(e);
    }

    final List<Element> ret = new ArrayList<Element>();

    final List<List<AbstractStructuralNode>> exploded = unpackRE(e.getSubnodes());

    for (final List<AbstractStructuralNode> line : exploded) {
      final Regexp<AbstractStructuralNode> subnodes;
      if (BaseUtils.isEmpty(line)) {
        subnodes = Regexp.getLambda();
      } else {
        final List<Regexp<AbstractStructuralNode>> regexpChildren = new ArrayList<Regexp<AbstractStructuralNode>>();
        for (final AbstractStructuralNode n : line) {
          regexpChildren.add(Regexp.getToken(n));
        }
        subnodes = Regexp.getConcatenation(regexpChildren);
      }
      ret.add(new Element(e.getContext(), e.getName(), e.getMetadata(), subnodes, e.getAttributes()));
    }

    LOG.info("Element \"" + e.getName() + "\" expanded into " + ret.size() + " new elements.");
    return ret;
  }

  private static List<List<AbstractStructuralNode>> unpackRE(
          final Regexp<AbstractStructuralNode> r) {
    switch (r.getType()) {
      case LAMBDA:
        return Collections.emptyList();
      case TOKEN:
        final List<List<AbstractStructuralNode>> tokenRet = new ArrayList<List<AbstractStructuralNode>>(1);
        tokenRet.add(Arrays.asList(r.getContent()));
        return applyRI(tokenRet, r.getInterval());
      case PERMUTATION:
        throw new IllegalArgumentException("Sorry, there is not enough time till the end of the universe");
      case CONCATENATION:
        final List<List<AbstractStructuralNode>> concatRet = new ArrayList<List<AbstractStructuralNode>>(1);

        int maxChildSize = 0;
        final List<List<List<AbstractStructuralNode>>> children = new ArrayList<List<List<AbstractStructuralNode>>>();
        for (final Regexp<AbstractStructuralNode> child : r.getChildren()) {
          final List<List<AbstractStructuralNode>> childList = unpackRE(child);
          children.add(childList);
          maxChildSize = Math.max(maxChildSize, childList.size());
        }

        LOG.info("Max child size in concat is " + maxChildSize);

        for (int i = 0; i < maxChildSize; i++) {
          final List<AbstractStructuralNode> resultLine = new ArrayList<AbstractStructuralNode>();

          for (int j = 0; j < r.getChildren().size(); j++) {
            resultLine.addAll(children.get(j).get(i % children.get(j).size()));
          }

          concatRet.add(resultLine);
        }

        return applyRI(concatRet, r.getInterval());
      case ALTERNATION:
        final List<List<AbstractStructuralNode>> altRet = new ArrayList<List<AbstractStructuralNode>>();
        for (final Regexp<AbstractStructuralNode> child : r.getChildren()) {
          altRet.addAll(unpackRE(child));
        }
        return applyRI(altRet, r.getInterval());
    }
    throw new IllegalArgumentException("Unknown regexp type: " + r.getType());
  }

  private static List<List<AbstractStructuralNode>> applyRI(
          final List<List<AbstractStructuralNode>> input,
          final RegexpInterval ri) {
    final List<List<AbstractStructuralNode>> ret = new ArrayList<List<AbstractStructuralNode>>();

    if (ri.isOnce()) {
      return input;
    } else if (ri.isOptional()) {
      ret.add(Collections.<AbstractStructuralNode>emptyList());
      ret.addAll(input);
    } else if (ri.isKleeneCross()) {
      for (final List<AbstractStructuralNode> l : input) {
        ret.add(l);
        ret.add(cloneList(l, 3));
      }
    } else if (ri.isKleeneStar()) {
      ret.add(Collections.<AbstractStructuralNode>emptyList());
      for (final List<AbstractStructuralNode> l : input) {
        ret.add(l);
        ret.add(cloneList(l, 3));
      }
    } else {
      for (final List<AbstractStructuralNode> l : input) {
        ret.add(cloneList(l, ri.getMin()));
        ret.add(cloneList(l, ri.getMax()));
      }
    }

    return ret;
  }

  private static List<AbstractStructuralNode> cloneList(
          final List<AbstractStructuralNode> l, final int times) {
    final List<AbstractStructuralNode> ret = new ArrayList<AbstractStructuralNode>();
    for (int i = 0; i < times; i++) {
      ret.addAll(l);
    }
    return ret;
  }

  private static boolean alreadyOk(final Element e) {
    final Regexp<AbstractStructuralNode> r = e.getSubnodes();
    if (!(r.isLambda() || r.isConcatenation())) {
      return false;
    }
    for (final Regexp<AbstractStructuralNode> child : r.getChildren()) {
      if (!(
              child.isLambda()
              || child.isToken())) {
        return false;
      }
      if (!child.getInterval().isOnce()) {
        return false;
      }
      if (!(
              child.getContent().isElement()
              || child.getContent().isSimpleData())) {
        return false;
      }
    }
    return true;
  }
}
