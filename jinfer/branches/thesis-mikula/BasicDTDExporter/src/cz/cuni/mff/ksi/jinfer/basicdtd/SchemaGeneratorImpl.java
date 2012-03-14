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
package cz.cuni.mff.ksi.jinfer.basicdtd;

import cz.cuni.mff.ksi.jinfer.base.utils.TopologicalSort;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.SchemaGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.SchemaGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils.Predicate;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.basicdtd.properties.DTDExportPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.utils.CollectionToString;
import cz.cuni.mff.ksi.jinfer.base.utils.IGGUtils;
import cz.cuni.mff.ksi.jinfer.basicdtd.utils.DomainUtils;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * A simple DTD exporter.
 *
 * @author vektor
 */
@ServiceProvider(service = SchemaGenerator.class)
public class SchemaGeneratorImpl implements SchemaGenerator {

  private static final Logger LOG = Logger.getLogger(SchemaGenerator.class);
  private static final String NAME = "Basic_DTD_exporter";
  private static final String DISPLAY_NAME = "Basic DTD exporter";
  private int maxEnumSize;
  private double minDefaultRatio;

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
    return Collections.emptyList();
  }

  @Override
  public void start(final List<Element> grammar,
          final SchemaGeneratorCallback callback) throws InterruptedException {

    LOG.info("DTD Exporter: got " + grammar.size()
            + " rules.");

    // load settings
    final Properties properties = RunningProject.getActiveProjectProps(DTDExportPropertiesPanel.NAME);

    maxEnumSize = Integer.parseInt(properties.getProperty(DTDExportPropertiesPanel.MAX_ENUM_SIZE, Integer.
            toString(DTDExportPropertiesPanel.MAX_ENUM_SIZE_DEFAULT)));
    minDefaultRatio = Float.parseFloat(properties.getProperty(
            DTDExportPropertiesPanel.MIN_DEFAULT_RATIO, Float.toString(
            DTDExportPropertiesPanel.MIN_DEFAULT_RATIO_DEFAULT)));

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

    LOG.info("DTD Exporter: schema generated at "
            + ret.toString().length() + " characters.");

    callback.finished(ret.toString(), "dtd");
  }

  private String elementToString(final Element e) {
    final StringBuilder ret = new StringBuilder();
    ret.append("<!ELEMENT ").append(e.getName()).append(' ');
    final Regexp<AbstractStructuralNode> regexp = e.getSubnodes();
    if (regexp.isToken()) {
      ret.append("(");
    }
    IntervalExpander ie = new IntervalExpander();
    ret.append(regexpToString(ie.expandIntervalsRegexp(regexp)));
    if (regexp.isToken()) {
      ret.append(")");
    }
    ret.append(">\n");
    final List<Attribute> attributes = e.getAttributes();
    if (!attributes.isEmpty()) {
      ret.append("<!ATTLIST ").append(e.getName()).append(' ').append(attributesToString(attributes)).
              append(">\n");
    }
    return ret.toString();
  }

  /**
   * If we want to output PCDATA in DTD, it needs to be like this
   * <code>
   * (#PCDATA|a|b|c)*
   * </code>

   * @param subnodes
   * @param topLevel
   * @return
   */
  private String regexpToString(final Regexp<AbstractStructuralNode> regexp) {
    switch (regexp.getType()) {
      case LAMBDA:
        return "EMPTY";
      case TOKEN:
        final String intervalString = regexp.getInterval().toString();
        final String contentString = regexp.getContent().isSimpleData() ? "#PCDATA" : regexp.getContent().getName();
        if (regexp.getContent().isSimpleData()) {
          return contentString;
        }
        return contentString + intervalString;
      case CONCATENATION:
        return comboToString(regexp, ",");
      case ALTERNATION:
        return comboToString(regexp, "|");
      case PERMUTATION:
        return comboToString(regexp, "|");
      default:
        throw new IllegalArgumentException("Unknown enum member: " + regexp.getType());
    }
  }

  /**
   * Returns true if one of the supplied children is a PCDATA.
   */
  private boolean containsPCDATA(final List<AbstractStructuralNode> children) {
    for (final AbstractStructuralNode child : children) {
      if (child.isSimpleData()) {
        return true;
      }
    }
    return false;
  }

  private String comboToString(final Regexp<AbstractStructuralNode> regexp,
          final String delimiter) {
    if (!containsPCDATA(regexp.getTokens())) {
      return listToString(regexp.getChildren(), delimiter) +
              regexp.getInterval().toString();
    }

    final List<AbstractStructuralNode> content = regexp.getTokens();

    Collections.sort(content,
      new Comparator<AbstractStructuralNode>() {
          @Override
          public int compare(final AbstractStructuralNode o1, final AbstractStructuralNode o2) {
            if (o1.isSimpleData()) {
              return -1;
            }
            if (o2.isSimpleData()) {
              return 1;
            }
            return 0;
          }
        }
    );

    final List<AbstractStructuralNode> distinctContent = BaseUtils.filter(content, new Predicate<AbstractStructuralNode>() {
      private Set<String> encountered = new HashSet<String>();
      private boolean encounteredSimpleData = false;
      @Override
      public boolean apply(final AbstractStructuralNode argument) {
        if (argument.isSimpleData()) {
          if (encounteredSimpleData) {
            return false;
          }
          encounteredSimpleData = true;
          return true;
        }
        if (encountered.contains(argument.getName())) {
          return false;
        }
        encountered.add(argument.getName());
        return true;
      }
    });

    return CollectionToString.colToString(
            distinctContent,
            "|",
            new CollectionToString.ToString<AbstractStructuralNode>() {
              @Override
              public String toString(final AbstractStructuralNode t) {
                return regexpToString(Regexp.<AbstractStructuralNode>getToken(t));
              }
            }) + "*";
  }

  private String listToString(final List<Regexp<AbstractStructuralNode>> list,
          final String separator) {
    return CollectionToString.colToString(
            list,
            separator,
            new CollectionToString.ToString<Regexp<AbstractStructuralNode>>() {
                @Override
                public String toString(final Regexp<AbstractStructuralNode> t) {
                  return regexpToString(t);
                }
            });
  }

  private String attributesToString(final List<Attribute> attributes) {
    final StringBuilder ret = new StringBuilder();
    // TODO anti atributes better
    for (final Attribute attribute : attributes) {
      ret.append(attributeToString(attribute));
    }
    return ret.toString();
  }

  private String attributeToString(final Attribute a) {
    if (a.getName().startsWith("xmlns:")) {
      return "\n\t" + a.getName() + " CDATA #IMPLIED";
    }
    final Map<String, Integer> domain = DomainUtils.getDomain(a);

    final StringBuilder ret = new StringBuilder();
    // type declaration of this attribute
    ret.append("\n\t")
        .append(a.getName());

    // ID attribute
    if (a.getMetadata().containsKey(IGGUtils.IS_ID)) {
      ret.append(" ID #REQUIRED");
      return ret.toString();
    }

    ret.append(DomainUtils.getAttributeType(domain, maxEnumSize));

    // requiredness/default value
    if (a.getMetadata().containsKey(IGGUtils.REQUIRED)) {
      ret.append("#REQUIRED");
    }
    else {
      ret.append(DomainUtils.getDefault(domain, minDefaultRatio));
    }
    return ret.toString();
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }
}
