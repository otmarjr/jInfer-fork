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
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
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


    // TODO for each element output a <!ELEMENT ...> and his <!ATTRLIST>

    final InputOutput io = IOProvider.getDefault().getIO("Inference", true);

    for (final AbstractNode node : grammar) {
      if (node.getType().equals(NodeType.ELEMENT)) {
        outputElement((Element) node, io);
      }
    }
  }

  private static void outputElement(final Element e, final InputOutput io) {
    io.getOut().println("<!ELEMENT " + e.getName() + " " + toString(e.getSubnodes()) + ">");
  }

  private static String toString(final Regexp<AbstractNode> regexp) {
    switch (regexp.getType()) {
      case TOKEN:
        return regexp.getContent().getType().equals(NodeType.SIMPLE_DATA) ? "#PCDATA" : regexp.getContent().getName();
      case KLEENE:
        return "(" + toString(regexp.getChildren().get(0)) + ")*";
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
    boolean first = true;
    for (final Regexp<AbstractNode> child : list) {
      if (!first) {
        ret.append(separator);
      }
      first = false;
      ret.append(toString(child));
    }
    ret.append(')');
    return ret.toString();
  }

}
