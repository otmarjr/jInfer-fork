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
import cz.cuni.mff.ksi.jinfer.base.interfaces.nodes.StructuralNodeType;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.IGGUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openide.util.lookup.ServiceProvider;

/**
 * A simple implementation of the 
 * {@link cz.cuni.mff.ksi.jinfer.base.interfaces.Expander} interface.
 * 
 * @author vektor
 */
@ServiceProvider(service = Expander.class)
public class ExpanderImpl implements Expander {

  @Override
  public List<Element> expand(final List<Element> grammar) {
    if (grammar == null) {
      throw new IllegalArgumentException("Grammar to expand must not be null.");
    }
    
    final List<Element> ret = new ArrayList<Element>();

    for (final Element e : grammar) {
      ret.addAll(expandElement(e));
    }

    return ret;
  }

  private static List<Element> expandElement(final Element e) {
    // we need to set sentinels even to the elements in a simple concatenation
    // because a concatenation of two tokens returns true, but we want to expand the rules inside too
    // hence, we can only ignore an EMPTY concatenation
    if (BaseUtils.isEmpty(e.getSubnodes().getChildren())
        && e.getSubnodes().isConcatenation() ) {
      // no need to set a sentinel or anything
      return Arrays.asList(e);
    }

    final List<Element> ret = new ArrayList<Element>();
    final List<List<AbstractStructuralNode>> exploded = unpackRE(e.getSubnodes());

    for (List<AbstractStructuralNode> line : exploded) {
      Regexp<AbstractStructuralNode> subnodes;
      if (BaseUtils.isEmpty(line)) {
        subnodes = ExpansionHelper.getEmptyConcat();
      } else {
        final List<Regexp<AbstractStructuralNode>> regexpChildren = new ArrayList<Regexp<AbstractStructuralNode>>();
        for (final AbstractStructuralNode n : line) {
          if (StructuralNodeType.ELEMENT.equals(n.getType())) {
            regexpChildren.add(createSentinelFromASN(n));
          } else {
            regexpChildren.add(Regexp.getToken(n)); // original
          }
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
        final List<List<AbstractStructuralNode>> empty = new ArrayList<List<AbstractStructuralNode>>(1);
        empty.add(new ArrayList<AbstractStructuralNode>());
        return empty;
      case TOKEN:
        final List<List<AbstractStructuralNode>> tokenRet = new ArrayList<List<AbstractStructuralNode>>(1);
        tokenRet.add(Arrays.asList(r.getContent())); // original implementation
        return ExpansionHelper.applyInterval(tokenRet, r.getInterval());
      case CONCATENATION:
        return ExpansionHelper.applyInterval(unpackConcat(r.getChildren()), r.getInterval());
      case ALTERNATION:
        return ExpansionHelper.applyInterval(unpackAlternation(r.getChildren()), r.getInterval());
      case PERMUTATION:
        return ExpansionHelper.applyInterval(unpackPermutation(r.getChildren()), r.getInterval());
      default:
        throw new IllegalArgumentException("Unknown regexp type: " + r.getType());
    }
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

  /**
   * Expand children of a permutation regexp for simplifier that can't handle complex regexps.
   * Children are passed to {@link #unpackConcat(java.util.List) } and the resulting list is copied.
   * Then, the list of original children is reversed and again passed to unpackConcat.
   * The resulting list is also copied and then these two lists are appended and returned.
   * @param children Permutation to be expanded.
   * @return List of children and reversed children expanded as a concatenation, appended together.
   */
  private static List<List<AbstractStructuralNode>> unpackPermutation(
          final List<Regexp<AbstractStructuralNode>> children) {
    final List<List<AbstractStructuralNode>> ret = new ArrayList<List<AbstractStructuralNode>>();

    ret.addAll(unpackConcat(children));

    final List<Regexp<AbstractStructuralNode>> reverseChildren = new ArrayList<Regexp<AbstractStructuralNode>>();
    for (int i = children.size() - 1; i >= 0; i--) {
      final Regexp<AbstractStructuralNode> regexp = children.get(i);
      reverseChildren.add(regexp);
    }

    ret.addAll(unpackConcat(reverseChildren));
    return ret;
  }

  /**
   * Create a TOKEN regexp containing a sentinel element with the original 
   * content of ASNode n with subnodes replaced with Lambda.
   * 
   * This method should be invoked only if the parameter is of
   * {@link StructuralNodeType#ELEMENT } type!
   *
   * @param n Node containing an Element to be converted to sentinel.
   * @return Token regexp containing the sentinel.
   */
  private static Regexp<AbstractStructuralNode> createSentinelFromASN(final AbstractStructuralNode n) {
    final Element e = (Element) n;
    final Map<String, Object> metadata = new HashMap<String, Object>(IGGUtils.METADATA_SENTINEL);
    metadata.putAll(e.getMetadata());
    return Regexp.<AbstractStructuralNode>getToken(
              new Element(e.getContext(), e.getName(), metadata,
              Regexp.<AbstractStructuralNode>getLambda(), e.getAttributes()));
  }
}
