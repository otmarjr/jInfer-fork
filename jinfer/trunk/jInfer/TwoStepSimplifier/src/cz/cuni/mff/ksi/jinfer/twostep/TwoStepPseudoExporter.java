/*
 *  Copyright (C) 2010 anti
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

package cz.cuni.mff.ksi.jinfer.twostep;

import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.SchemaGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.SchemaGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.utils.TopologicalSort;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Exporter to DTD-like format but without employing any DTD restrictions.
 * It simply exports ELEMENT sth (regexp) and ATTLIST sth (each attribute)
 *
 * The format of export may be changed for debugging purposes of inferring method.
 *
 * @author anti
 */
@ServiceProvider(service = SchemaGenerator.class)
public class TwoStepPseudoExporter implements SchemaGenerator {
  /**
   * Name of this module.
   */
  public static final String NAME = "TwoStepPseudoExporter";
  public static final String DISPLAY_NAME = "TwoStep PseudoExporter";

  private static final Logger LOG = Logger.getLogger(TwoStepPseudoExporter.class);

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getModuleDescription() {
    return getDisplayName();
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.<String>emptyList();
  }

  @Override
  public void start(final List<Element> grammar,
          final SchemaGeneratorCallback callback) throws InterruptedException {

    LOG.info("got " + grammar.size() + " rules.");

    // sort elements topologically
    final TopologicalSort s = new TopologicalSort(grammar);
    final List<Element> toposorted = s.sort();

    // generate DTD schema
    final StringBuilder ret = new StringBuilder("<!-- %generated% -->\n");
    for (final Element element : toposorted) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      ret.append(elementToString(element));
    }

    LOG.info("schema generated at "+ ret.toString().length() + " characters.");
    callback.finished(ret.toString(), "dtd");
  }

  private String elementToString(final Element e) {
    final StringBuilder sb = new StringBuilder();
    sb.append("<!ELEMENT ");
    sb.append(e.getName());
    sb.append(' ');
    sb.append(regexpToString(e.getSubnodes()));
    sb.append(">\n");
    final List<Attribute> attributes = e.getAttributes();
    if (!attributes.isEmpty()) {
      sb.append("<!ATTLIST ");
      sb.append(e.getName());
      sb.append('\n');
      sb.append(attributesToString(attributes));
      sb.append(">\n");
    }
    return sb.toString();
  }

  private String regexpToString(final Regexp<AbstractStructuralNode> regexp) {
    switch (regexp.getType()) {
      case TOKEN:
          return structuralToString(regexp.getContent()) + regexp.getInterval().toString();
      case CONCATENATION:
        return childrenListToString(regexp.getChildren(), ",") + regexp.getInterval().toString();
      case ALTERNATION:
        return childrenListToString(regexp.getChildren(), "|") + regexp.getInterval().toString();
      case PERMUTATION:
        return childrenListToString(regexp.getChildren(), "&") + regexp.getInterval().toString();
      case LAMBDA:
        return "\u03BB";
      default:
        throw new IllegalArgumentException("Unknown enum member.");
    }

  }

  private String structuralToString(final AbstractStructuralNode node) {
    if (node.isSimpleData()) {
      return "#CDATA";
    }
    return node.getName();
  }

  // TODO anti Can this be converted to CollectionToString.colToString() ?
  private String childrenListToString(
          final List<Regexp<AbstractStructuralNode>> children,
          final String delimiter) {
    final StringBuilder sb = new StringBuilder();
    sb.append('(');
    final Iterator<Regexp<AbstractStructuralNode>> it= children.iterator();
    while (it.hasNext()) {
      final Regexp<AbstractStructuralNode> child= it.next();
      sb.append(regexpToString(child));
      if (it.hasNext()) {
        sb.append(delimiter);
      }
    }
    sb.append(')');
    return sb.toString();
  }

  private String attributesToString(final List<Attribute> attributes) {
    final StringBuilder sb = new StringBuilder();
    final Iterator<Attribute> it = attributes.iterator();
    while (it.hasNext()) {
      final Attribute att= it.next();
      sb.append("\t");
      sb.append(att.getName());
      sb.append(" ");
      sb.append(contentToString(att.getContent()));
      if (it.hasNext()) {
        sb.append("\n");
      }
    }
    return sb.toString();
  }

  private String contentToString(final List<String> content) {
    final StringBuilder sb = new StringBuilder();
    sb.append("{");
    final Iterator<String> it = content.iterator();
    while (it.hasNext()) {
      final String ct = it.next();
      sb.append(ct);
      if (it.hasNext()) {
        sb.append(", ");
      }
    }
    sb.append("}");
    return sb.toString();
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }
}
