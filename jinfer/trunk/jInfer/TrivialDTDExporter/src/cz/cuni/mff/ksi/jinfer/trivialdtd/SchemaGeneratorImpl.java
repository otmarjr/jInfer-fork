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
package cz.cuni.mff.ksi.jinfer.trivialdtd;

import cz.cuni.mff.ksi.jinfer.base.interfaces.SchemaGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SchemaGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * A simple DTD exporter.
 * 
 * @author vektor
 */
public class SchemaGeneratorImpl implements SchemaGenerator {

  private final InputOutput io = IOProvider.getDefault().getIO("jInfer", false);

  @Override
  public String getModuleName() {
    return "Trivial DTD exporter";
  }

  @Override
  public void start(final List<AbstractNode> grammar, final SchemaGeneratorCallback callback) {
    io.getOut().println("DTD Exporter: got " + grammar.size() +
            " rules.");
    
    // filter only the elements
    final List<Element> elements = new ArrayList<Element>();
    for (final AbstractNode node : grammar) {
      if (node.getType().equals(NodeType.ELEMENT)) {
        elements.add((Element) node);
      }
    }

    io.getOut().println("DTD Exporter: that is " + elements.size() +
            " elements.");

    // sort elements topologically
    final TopologicalSort s = new TopologicalSort(elements);
    final List<Element> toposorted = s.sort();

    // generate DTD schema
    final StringBuilder ret = new StringBuilder();
    for (final Element element : toposorted) {
      ret.append(elementToString(element));
    }

    io.getOut().println("DTD Exporter: schema generated at "
            + ret.toString().length() + " characters.");

    callback.finished(ret.toString());
  }

  private static String elementToString(final Element e) {
    final StringBuilder ret = new StringBuilder();
    ret.append("<!ELEMENT ")
            .append(e.getName())
            .append(' ')
            .append(subElementsToString(e.getSubnodes(), true))
            .append(">\n");
    final List<Attribute> attributes = e.getElementAttributes();
    if (!attributes.isEmpty()) {
      ret.append("<!ATTLIST ")
              .append(e.getName())
              .append(' ')
              .append(attributesToString(attributes))
              .append(">\n");
    }
    return ret.toString();
  }

  private static String subElementsToString(final Regexp<AbstractNode> regexp,
          final boolean topLevel) {
    if (regexp.isEmpty()) {
      return "EMPTY";
    }
    switch (regexp.getType()) {
      case TOKEN:
        return tokenToString(regexp.getContent(), topLevel);
      case KLEENE:
        return "(" + subElementsToString(regexp.getChild(0), false) + ")*";
      case CONCATENATION:
        return concatToString(regexp.getChildren());
      case ALTERNATION:
        if (regexp.getChild(0).isEmpty()) {
          return listToString(omitAttributes(regexp.getChildren().subList(1, regexp.getChildren().size())), '|') + "?";
        }
        return listToString(omitAttributes(regexp.getChildren()), '|');
      default:
        throw new IllegalArgumentException("Unknown enum member.");
    }
  }

  private static String tokenToString(final AbstractNode n, final boolean topLevel) {
    final StringBuilder ret = new StringBuilder();
    if (topLevel) {
      ret.append('(');
    }
    if (n.getType().equals(NodeType.SIMPLE_DATA)) {
      ret.append("#PCDATA");
    }
    else {
      ret.append(n.getName());
    }
    if (topLevel) {
      ret.append(')');
    }
    return ret.toString();
  }

  /**
   * If we want to output PCDATA in DTD, it needs to be like this
   * <code>
   * (#PCDATA|a|b|c)*
   * </code>
   *
   * @param children
   * @return
   */
  private static String concatToString(final List<Regexp<AbstractNode>> children) {
    boolean pcdata = false;    
    for (final Regexp<AbstractNode> child : children) {
      if (RegexpType.TOKEN.equals(child.getType())
              && NodeType.SIMPLE_DATA.equals(child.getContent().getType())) {
        pcdata = true;
        break;
      }
    }
    if (!pcdata) {
      return listToString(omitAttributes(children), ',');
    }

    Collections.sort(children, new Comparator<Regexp<AbstractNode>>() {
      @Override
      public int compare(final Regexp<AbstractNode> o1, final Regexp<AbstractNode> o2) {
        if (RegexpType.TOKEN.equals(o1.getType())
              && NodeType.SIMPLE_DATA.equals(o1.getContent().getType())) {
          return -1;
        }
        if (RegexpType.TOKEN.equals(o2.getType())
              && NodeType.SIMPLE_DATA.equals(o2.getContent().getType())) {
          return 1;
        }
        return 0;
      }
    });

    return listToString(children, '|') + "*";
  }

  private static List<Regexp<AbstractNode>> omitAttributes(
          final List<Regexp<AbstractNode>> col) {
    return BaseUtils.filter(col, new BaseUtils.Predicate<Regexp<AbstractNode>>() {

      @Override
      public boolean apply(final Regexp<AbstractNode> type) {
        return !type.getType().equals(RegexpType.TOKEN) || !type.getContent().getType().equals(NodeType.ATTRIBUTE);
      }
    });
  }

  private static String listToString(final List<Regexp<AbstractNode>> list,
          final char separator) {
    final StringBuilder ret = new StringBuilder();
    ret.append('(');
    boolean first = true;
    for (final Regexp<AbstractNode> child : list) {
      if (!first) {
        ret.append(separator);
      }
      first = false;
      ret.append(subElementsToString(child, false));
    }
    ret.append(')');
    return ret.toString();
  }

  private static String attributesToString(final List<Attribute> attributes) {
    final StringBuilder ret = new StringBuilder();
    for (final Attribute attribute : attributes) {
      ret.append("\n\t").append(attribute.getName()).append(" CDATA #IMPLIED");
    }
    return ret.toString();
  }
  
}
