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
import org.openide.util.lookup.ServiceProvider;

/**
 * A simple implementation of the Expander interface.
 * 
 * @author vektor
 */
@ServiceProvider(service = Expander.class)
public class ExpanderImpl implements Expander {

  @Override
  public List<Element> expand(final List<Element> grammar) {
    final List<Element> ret = new ArrayList<Element>();

    for (final Element e : grammar) {
      ret.addAll(expandElement(e));
    }

    return ret;
  }

  private static List<Element> expandElement(final Element e) {
    if (isSimpleConcatenation(e)) {
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
        return applyRI(unpackConcat(r.getChildren()), r.getInterval());
      case ALTERNATION:
        return applyRI(unpackAlternation(r.getChildren()), r.getInterval());
    }
    throw new IllegalArgumentException("Unknown regexp type: " + r.getType());
  }

  private static List<List<AbstractStructuralNode>> unpackConcat(
          final List<Regexp<AbstractStructuralNode>> children) {
    final List<List<AbstractStructuralNode>> ret = new ArrayList<List<AbstractStructuralNode>>(1);

    int maxChildSize = 0;
    final List<List<List<AbstractStructuralNode>>> childWords = new ArrayList<List<List<AbstractStructuralNode>>>();
    for (final Regexp<AbstractStructuralNode> child : children) {
      final List<List<AbstractStructuralNode>> childList = unpackRE(child);
      childWords.add(childList);
      maxChildSize = Math.max(maxChildSize, childList.size());
    }
    
    for (int i = 0; i < maxChildSize; i++) {
      final List<AbstractStructuralNode> resultLine = new ArrayList<AbstractStructuralNode>();

      for (int j = 0; j < children.size(); j++) {
        resultLine.addAll(childWords.get(j).get(i % childWords.get(j).size()));
      }

      ret.add(resultLine);
    }

    return ret;
  }

  private static List<List<AbstractStructuralNode>> unpackAlternation(
          final List<Regexp<AbstractStructuralNode>> children) {
    final List<List<AbstractStructuralNode>> ret = new ArrayList<List<AbstractStructuralNode>>();
    for (final Regexp<AbstractStructuralNode> child : children) {
      ret.addAll(unpackRE(child));
    }
    return ret;
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
        ret.add(BaseUtils.cloneList(l, 3));
      }
    } else if (ri.isKleeneStar()) {
      ret.add(Collections.<AbstractStructuralNode>emptyList());
      for (final List<AbstractStructuralNode> l : input) {
        ret.add(l);
        ret.add(BaseUtils.cloneList(l, 3));
      }
    } else {
      for (final List<AbstractStructuralNode> l : input) {
        ret.add(BaseUtils.cloneList(l, ri.getMin()));
        ret.add(BaseUtils.cloneList(l, ri.getMax()));
      }
    }

    return ret;
  }

  private static boolean isSimpleConcatenation(final Element e) {
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
