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
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.trivialdtd.options.ConfigPanel;
import cz.cuni.mff.ksi.jinfer.trivialdtd.utils.DTDUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.prefs.Preferences;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * A simple DTD exporter.
 * 
 * @author vektor
 */
public class SchemaGeneratorImpl implements SchemaGenerator {

  private final InputOutput io = IOProvider.getDefault().getIO("jInfer", false);
  private int maxEnumSize;
  private double minDefaultRatio;

  @Override
  public String getModuleName() {
    return "Trivial DTD exporter";
  }

  @Override
  public void start(final List<AbstractNode> grammar, final SchemaGeneratorCallback callback) {
    io.getOut().println("DTD Exporter: got " + grammar.size()
            + " rules.");

    maxEnumSize = Preferences.userNodeForPackage(ConfigPanel.class).getInt("max.enum.size", 3);
    minDefaultRatio = Preferences.userNodeForPackage(ConfigPanel.class).getFloat("min.default.ratio", 0.67f);
    
    // filter only the elements
    final List<Element> elements = new ArrayList<Element>();
    for (final AbstractNode node : grammar) {
      if (node.getType().equals(NodeType.ELEMENT)) {
        elements.add((Element) node);
      }
    }

    io.getOut().println("DTD Exporter: that is " + elements.size()
            + " elements.");

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

  private String elementToString(final Element e) {
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

  private String subElementsToString(final Regexp<AbstractNode> regexp,
          final boolean topLevel) {
    if (regexp.isEmpty()) {
      return "EMPTY";
    }
    if (topLevel
            && BaseUtils.filter(regexp.getTokens(), new BaseUtils.Predicate<AbstractNode>() {
      @Override
      public boolean apply(final AbstractNode argument) {
        return argument.isElement();
      }
    }).isEmpty()) {
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
        return alternationToString(regexp.getChildren());
      default:
        throw new IllegalArgumentException("Unknown enum member.");
    }
  }

  private String tokenToString(final AbstractNode node, final boolean topLevel) {
    final StringBuilder ret = new StringBuilder();
    if (topLevel) {
      ret.append('(');
    }
    if (node.isSimpleData()) {
      ret.append("#PCDATA");
    } else {
      ret.append(node.getName());
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
  private String concatToString(final List<Regexp<AbstractNode>> children) {
    boolean pcdata = false;
    for (final Regexp<AbstractNode> child : children) {
      if (child.isToken() && child.getContent().isSimpleData()) {
        pcdata = true;
        break;
      }
    }
    if (!pcdata) {
      return listToString(DTDUtils.omitAttributes(children), ',');
    }

    final List<AbstractNode> content = new ArrayList<AbstractNode>();
    for (final Regexp<AbstractNode> r : children) {
      content.addAll(r.getTokens());
    }

    Collections.sort(content, new Comparator<AbstractNode>() {

      @Override
      public int compare(final AbstractNode o1, final AbstractNode o2) {
        if (o1.isSimpleData()) {
          return -1;
        }
        if (o2.isSimpleData()) {
          return 1;
        }
        return 0;
      }
    });

    final StringBuilder ret = new StringBuilder();
    ret.append('(');
    boolean first = true;
    for (final AbstractNode child : content) {
      if (!first) {
        ret.append('|');
      }
      first = false;
      ret.append(tokenToString(child, false));
    }
    ret.append(")*");
    return ret.toString();
  }

  private String alternationToString(final List<Regexp<AbstractNode>> children) {
    if (children.get(0).isEmpty()) {
      return listToString(DTDUtils.omitAttributes(children.subList(1, children.size())), '|') + "?";
    }
    return listToString(DTDUtils.omitAttributes(children), '|');
  }

  private String listToString(final List<Regexp<AbstractNode>> list,
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

  private String attributesToString(final List<Attribute> attributes) {
    final StringBuilder ret = new StringBuilder();
    for (final Attribute attribute : attributes) {
      final Map<String, Integer> domain = getDomain(attribute);

      int domainAbsolute = 0;

      for (final Integer i : domain.values()) {
        domainAbsolute += i.intValue();
      }

      final long dominantAbsolute = Math.round(domainAbsolute * minDefaultRatio);

      final String type;
      if (domain.size() <= maxEnumSize) {
        type = domainToString(domain.keySet());
      }
      else {
        type = " CDATA ";
      }

      ret.append("\n\t").append(attribute.getName()).append(type);
      if (attribute.getAttributes().containsKey("required")) {
        ret.append("#REQUIRED");
      }
      else {
        boolean dominantFound = false;
        for (final Map.Entry<String, Integer> e : domain.entrySet()) {
          if (e.getValue().intValue() > dominantAbsolute) {
            ret.append('"').append(e.getKey()).append('"');
            dominantFound = true;
            break;
          }
        }
        if (!dominantFound) {
          ret.append("#IMPLIED");
        }
      }
    }
    return ret.toString();
  }

  private Map<String, Integer> getDomain(final Attribute attribute) {
    final Map<String, Integer> domainMap = new HashMap<String, Integer>();
    for (final Object o : attribute.getContent()) {
      if (domainMap.containsKey((String) o)) {
        domainMap.put((String) o, Integer.valueOf(domainMap.get((String) o).intValue() + 1));
      }
      else {
      domainMap.put((String) o, Integer.valueOf(1));
      }
    }
    return domainMap;
  }

  private String domainToString(final Set<String> domain) {
    final StringBuilder ret = new StringBuilder();
    ret.append(" (");
    boolean first = true;
    for (final String s : domain) {
      if (!first) {
        ret.append('|');
      }
      else {
        first = false;
      }
      ret.append(s);
    }
    ret.append(") ");
    return ret.toString();
  }
}