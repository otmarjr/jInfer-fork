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
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.SimpleData;
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
    //TODO I think this should be disabled when we want to recursively expand tokens,
    // because a concatenation of two tokens returns true, but we want to expand the rules inside too
    if (ExpansionHelper.isSimpleConcatenation(e.getSubnodes())) {
      return Arrays.asList(e);
    }

    final List<Element> ret = new ArrayList<Element>();
    final List<List<AbstractStructuralNode>> exploded = unpackRE(e.getSubnodes());

    for (final List<AbstractStructuralNode> line : exploded) {
      final Regexp<AbstractStructuralNode> subnodes;
      if (BaseUtils.isEmpty(line)) {
        subnodes = ExpansionHelper.getEmptyConcat();
      } else {
        final List<Regexp<AbstractStructuralNode>> regexpChildren = new ArrayList<Regexp<AbstractStructuralNode>>();
        for (final AbstractStructuralNode n : line) {
          //TODO this is an open issue
          if (n.getType().equals(StructuralNodeType.ELEMENT)) {
            Element nElem = (Element) n;
            List<String> context = nElem.getContext();
            String name = nElem.getName();
            Map<String, Object> metadata = new HashMap<String, Object>();
            metadata.putAll(nElem.getMetadata());
            metadata.putAll(IGGUtils.METADATA_SENTINEL);
            Regexp<AbstractStructuralNode> subnodes1 = nElem.getSubnodes();
            List<Attribute> attributes = nElem.getAttributes();
            regexpChildren.add(Regexp.<AbstractStructuralNode>getToken(
              new Element(context, name, metadata, subnodes1, attributes)));
          } else {
            SimpleData nData = (SimpleData) n;
            List<String> context = nData.getContext();
            String name = nData.getName();
            Map<String, Object> metadata = new HashMap<String, Object>();
            metadata.putAll(nData.getMetadata());
            metadata.putAll(IGGUtils.METADATA_SENTINEL);
            String contentType = nData.getContentType();
            List<String> content = nData.getContent();
            regexpChildren.add(Regexp.<AbstractStructuralNode>getToken(
              new SimpleData(context, name, metadata, contentType, content)));
          }
            //regexpChildren.add(Regexp.getToken(n)); // original
        }
        subnodes = Regexp.getConcatenation(regexpChildren);
      }
      ret.add(new Element(e.getContext(), e.getName(), e.getMetadata(), subnodes, e.getAttributes()));
    }

    return ret;
  }

  private static List<List<AbstractStructuralNode>> unpackRE(
          final Regexp<AbstractStructuralNode> r) {
//System.err.println("unpacking re: " + r.toString());
    switch (r.getType()) {
      case LAMBDA:
        final List<List<AbstractStructuralNode>> empty = new ArrayList<List<AbstractStructuralNode>>(1);
        empty.add(new ArrayList<AbstractStructuralNode>());
        return empty;
      case TOKEN:
//System.err.println("unpacking token");
        final List<List<AbstractStructuralNode>> tokenRet = new ArrayList<List<AbstractStructuralNode>>(1);
    /*    final AbstractStructuralNode content = r.getContent();
        if (StructuralNodeType.SIMPLE_DATA.equals(content.getType())) {
          tokenRet.add(Arrays.asList(r.getContent()));
        } else {
          // element
          List<Element> subexpand = expandElement((Element) r.getContent());
          for (Element element : subexpand) {
            List<AbstractStructuralNode> concat = new ArrayList<AbstractStructuralNode>();
            concat.add(element);
            tokenRet.add(concat);
          }
        }*/
        tokenRet.add(Arrays.asList(r.getContent())); // original implementation
        return ExpansionHelper.applyInterval(tokenRet, r.getInterval());
      case CONCATENATION:
//System.err.println("unpacking concat");
        return ExpansionHelper.applyInterval(unpackConcat(r.getChildren()), r.getInterval());
      case ALTERNATION:
        return ExpansionHelper.applyInterval(unpackAlternation(r.getChildren()), r.getInterval());
      case PERMUTATION:
        throw new IllegalArgumentException("Sorry, there is not enough time till the end of the universe");
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
}
