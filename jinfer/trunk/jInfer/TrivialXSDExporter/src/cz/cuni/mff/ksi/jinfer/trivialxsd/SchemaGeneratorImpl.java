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
package cz.cuni.mff.ksi.jinfer.trivialxsd;

import cz.cuni.mff.ksi.jinfer.base.interfaces.SchemaGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SchemaGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.trivialxsd.utils.TypeCategory;
import cz.cuni.mff.ksi.jinfer.trivialxsd.utils.XSDUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * A simple XSD exporter.
 * 
 * @author riacik
 */
@ServiceProvider(service = SchemaGenerator.class)
public class SchemaGeneratorImpl implements SchemaGenerator {

  private static Logger LOG = Logger.getLogger(SchemaGenerator.class);
  //private int maxEnumSize;
  //private double minDefaultRatio;
  private int indentationLevel = 0;
  private final int indentationStep = 2;

  @Override
  public String getModuleName() {
    return "Trivial XSD exporter";
  }

  @Override
  public void start(final List<AbstractNode> grammar, final SchemaGeneratorCallback callback) {
    LOG.info("XSD Exporter: got " + grammar.size()
            + " rules.");

    // load settings
    //maxEnumSize = Preferences.userNodeForPackage(ConfigPanel.class).getInt("max.enum.size", 3);
    //minDefaultRatio = Preferences.userNodeForPackage(ConfigPanel.class).getFloat("min.default.ratio", 0.67f);
    
    // filter only the elements
    final List<Element> elements = new ArrayList<Element>();
    for (final AbstractNode node : grammar) {
      if (node.getType().equals(NodeType.ELEMENT)) {
        elements.add((Element) node);
      }
    }

    //io.getOut().println("DTD Exporter: that is " + elements.size()
    //        + " elements.");

    // sort elements topologically
    final TopologicalSort s = new TopologicalSort(elements);
    final List<Element> toposorted = s.sort();

    // generate XSD
    final StringBuilder ret = new StringBuilder();
    ret.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    ret.append("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n");
    
    if (!toposorted.isEmpty()) {
      ret.append(elementToString(toposorted.get(toposorted.size() - 1)));
    }
    // TODO rio ostatne elementy, ktore nie su dostupne z prveho
    //for (final Element element : toposorted) {
    //  ret.append(elementToString(element));
    //}

    //io.getOut().println("DTD Exporter: schema generated at "
    //        + ret.toString().length() + " characters.");

    callback.finished(ret.toString());
  }

  private String elementToString(final Element e) {
    final StringBuilder ret = new StringBuilder();
    indent(ret, "<xs:element name=\"");
    ret.append(e.getName())
            .append("\"");
    TypeCategory typeCategory = XSDUtils.getTypeCategory(e);
    if (typeCategory.equals(TypeCategory.BUILTIN)) {
      ret.append(" type=\"xs:string\">\n");
      return ret.toString();
    }
    ret.append(">\n");
     // TODO rio mixed content
    indentationIncrease();

    switch (typeCategory) {
      case SIMPLE:
        indent(ret, "<xs:simpleType>\n");
        break;
      case COMPLEX:
        indent(ret, "<xs:complexType>\n");
        break;
      default:
        throw new IllegalArgumentException("Unknown of illegal enum member.");
    }
    indentationIncrease();

    final List<Attribute> attributes = e.getElementAttributes();
    if (!attributes.isEmpty()) {
      assert(typeCategory.equals(TypeCategory.COMPLEX));
      for (Attribute attribute : attributes) {
        indent(ret, "<xs:attribute name=\"");
        ret.append(attribute.getName())
              .append("\" type=\"xs:string\">\n");
      }
    }

    ret.append(subElementsToString(e.getSubnodes()));
    indentationDecrease();

    switch (typeCategory) {
      case SIMPLE:
        indent(ret, "</xs:simpleType>\n");
        break;
      case COMPLEX:
        indent(ret, "</xs:complexType>\n");
        break;
      default:
        throw new IllegalArgumentException("Unknown of illegal enum member.");
    }
    indentationDecrease();

    indent(ret, "</xs:element>\n");
    return ret.toString();
  }

  private String subElementsToString(final Regexp<AbstractNode> regexp) {
    assert(regexp.isEmpty() == false);

    if (BaseUtils.filter(regexp.getTokens(), new BaseUtils.Predicate<AbstractNode>() {
      @Override
      public boolean apply(final AbstractNode argument) {
        return argument.isElement();
      }
    }).isEmpty()) {
      return "";
    }

    switch (regexp.getType()) {
      case TOKEN:
        return tokenToString(regexp.getContent());
      case KLEENE:
      {
        final StringBuilder ret = new StringBuilder();
        indent(ret, "<xs:sequence>\n");
        indentationIncrease();
        ret.append(subElementsToString(regexp.getChild(0)));
        indentationDecrease();
        indent(ret, "</xs:sequence>\n");
        return ret.toString();
      }
      case CONCATENATION:
      {
        final StringBuilder ret = new StringBuilder();
        indent(ret, "<xs:sequence>\n");
        indentationIncrease();
        for (Regexp<AbstractNode> subRegexp : regexp.getChildren()) {
          ret.append(subElementsToString(subRegexp));
        }
        indentationDecrease();
        indent(ret, "</xs:sequence>\n");
        return ret.toString();
      }
      case ALTERNATION:
        {
        final StringBuilder ret = new StringBuilder();
        indent(ret, "<xs:choice>\n");
        indentationIncrease();
        for (Regexp<AbstractNode> subRegexp : regexp.getChildren()) {
          ret.append(subElementsToString(subRegexp));
        }
        indentationDecrease();
        indent(ret, "</xs:choice>\n");
        return ret.toString();
      }
      default:
        throw new IllegalArgumentException("Unknown enum member.");
    }
  }

  private String tokenToString(final AbstractNode node) {
    assert(node.isSimpleData() == false);
    assert(node.isElement());

    return elementToString((Element) node);
  }

  private void indent(final StringBuilder sb, final String txt) {
    char[] spaces = new char[indentationLevel];
    for (int i = 0; i < indentationLevel; ++i) {
      spaces[i] = ' ';
    }
    final String indentation = new String(spaces);
    sb.append(indentation)
            .append(txt);
  }

  private void indentationIncrease() {
    indentationLevel += indentationStep;
  }

  private void indentationDecrease() {
    assert(indentationLevel >= indentationStep);
    indentationLevel -= indentationStep;
  }
}