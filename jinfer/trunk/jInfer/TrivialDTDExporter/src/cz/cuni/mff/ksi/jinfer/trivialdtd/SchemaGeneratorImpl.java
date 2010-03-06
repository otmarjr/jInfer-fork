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
import java.util.Collection;
import java.util.List;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * A simple DTD exporter.
 * 
 * @author vektor
 */
public class SchemaGeneratorImpl implements SchemaGenerator {

  public String getModuleName() {
    return "Trivial DTD exporter";
  }

  public void start(final List<AbstractNode> grammar, final SchemaGeneratorCallback callback) {
    // TODO topological sort of grammar


    final InputOutput io = IOProvider.getDefault().getIO("Inference", false);

    for (final AbstractNode node : grammar) {
      if (node.getType().equals(NodeType.ELEMENT)) {
        outputElement((Element) node, io);
      }
    }
  }

  private static void outputElement(final Element e, final InputOutput io) {
    io.getOut().println("<!ELEMENT " + e.getName() + " " + subElementsToString(e.getSubnodes()) + ">");
    final List<Attribute> attributes = e.getElementAttributes();
    if (!attributes.isEmpty()) {
      io.getOut().println("<!ATTLIST " + e.getName() + " " + attributesToString(attributes) + ">");
    }
  }

  private static String subElementsToString(final Regexp<AbstractNode> regexp) {
    switch (regexp.getType()) {
      case TOKEN:
        return regexp.getContent().getType().equals(NodeType.SIMPLE_DATA) ? "#PCDATA" : regexp.getContent().getName();
      case KLEENE:
        return "(" + subElementsToString(regexp.getChildren().get(0)) + ")*";
      case CONCATENATION:
        return listToString(regexp.getChildren(), ',');
      case ALTERNATION:
        return listToString(regexp.getChildren(), '|');
      default:
        throw new IllegalArgumentException("Unknown enum member.");
    }
  }

  private static String listToString(final List<Regexp<AbstractNode>> list, final char separator) {
    final StringBuilder ret = new StringBuilder();
    ret.append('(');

    final Collection<Regexp<AbstractNode>> filteredList = BaseUtils.filter(list, new BaseUtils.Predicate<Regexp<AbstractNode>>() {
      public boolean apply(final Regexp<AbstractNode> type) {
        return !type.getType().equals(RegexpType.TOKEN) || !type.getContent().getType().equals(NodeType.ATTRIBUTE);
      }
    });

    boolean first = true;
    for (final Regexp<AbstractNode> child : filteredList) {
      if (!first) {
        ret.append(separator);
      }
      first = false;
      ret.append(subElementsToString(child));
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
