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
import cz.cuni.mff.ksi.jinfer.base.interfaces.SchemaGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SchemaGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils.Predicate;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.basicdtd.properties.DTDExportPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.basicdtd.utils.DTDUtils;
import cz.cuni.mff.ksi.jinfer.basicdtd.utils.CollectionToString;
import cz.cuni.mff.ksi.jinfer.basicdtd.utils.DomainUtils;
import java.util.ArrayList;
import java.util.Collections;
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
  private int maxEnumSize;
  private double minDefaultRatio;

  // TODO anti Next input creates <!ELEMENT delay (PCDATA*)>
  // <registration_status>
//   <delay>7</delay>
//   <delay>7</delay>
//   <delay>7</delay>
//   <delay>7</delay>
//</registration_status>

  @Override
  public String getName() {
    return "Basic DTD exporter";
  }

  @Override
  public String getModuleDescription() {
    return getName();
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.emptyList();
  }

  @Override
  public void start(final List<AbstractNode> grammar,
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

    // filter only the elements
    final List<Element> elements = new ArrayList<Element>();
    for (final AbstractNode node : grammar) {
      if (node.isElement()) {
        elements.add((Element) node);
      }
    }

    LOG.info("DTD Exporter: that is " + elements.size()
            + " elements.");

    // sort elements topologically
    final TopologicalSort s = new TopologicalSort(elements);
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
    ret.append("<!ELEMENT ").append(e.getName()).append(' ').append(subElementsToString(e.
            getSubnodes(), true)).append(">\n");
    final List<Attribute> attributes = e.getElementAttributes();
    if (!BaseUtils.isEmpty(attributes)) {
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

   * @param regexp
   * @param topLevel
   * @return
   */
  private String subElementsToString(final Regexp<AbstractNode> regexp,
          final boolean topLevel) {
    if (regexp.isLambda()) {
      return "EMPTY";
    }
    if (topLevel
            && BaseUtils.filter(regexp.getTokens(), new BaseUtils.Predicate<AbstractNode>() {

      @Override
      public boolean apply(final AbstractNode argument) {
        return argument.isElement() || argument.isSimpleData();
      }
    }).isEmpty()) {
      return "EMPTY";
    }
    Regexp<AbstractNode> _regexp= withoutAttributes(regexp);

    switch (regexp.getType()) {
      case TOKEN:
        StringBuilder sb = new StringBuilder();
        if (topLevel) {
          sb.append('(');
        }
        if (regexp.getContent().isSimpleData()) {
          if (regexp.getInterval().isOnce()) {
            sb.append("#PCDATA");
          } else {
            sb.append("(#PCDATA)");
          }
        } else {
          sb.append(regexp.getContent().getName());
        }
        sb.append(regexp.getInterval().toString());
        if (topLevel) {
          sb.append(')');
        }
        return sb.toString();
      case CONCATENATION:
        return comboToString(regexp, ',');
      case ALTERNATION:
        return comboToString(regexp, '|');
      case PERMUTATION:
        return comboToString(regexp, '|');
      default:
        throw new IllegalArgumentException("Unknown enum member: " + regexp.getType());
    }
  }

  private Regexp<AbstractNode> withoutAttributes(Regexp<AbstractNode> regexp) {
    switch (regexp.getType()) {
      case LAMBDA:
        return regexp;
      case TOKEN:
        if (regexp.getContent().isAttribute()) {
          return Regexp.<AbstractNode>getLambda();
        }
        return regexp;
      case CONCATENATION:
      case ALTERNATION:
      case PERMUTATION:
        List<Regexp<AbstractNode>> nonAttributeChildren= BaseUtils.filter(regexp.getChildren(),
                new BaseUtils.Predicate<Regexp<AbstractNode>>() {
                  @Override
                  public boolean apply(final Regexp<AbstractNode> r) {
                    return !r.isToken() || !r.getContent().isAttribute();
                  }
        });
        if (nonAttributeChildren.isEmpty()) {
          throw new IllegalArgumentException("On DTD Exporter input came regexp for element, in which we don't have any TOKEN.");
        }
        if (nonAttributeChildren.size() == 1) {
          return nonAttributeChildren.get(0);
        }
        return new Regexp<AbstractNode>(null, nonAttributeChildren, regexp.getType(), regexp.getInterval());
      default:
        return null;
    }
  }

  private String tokenToString(AbstractNode t) {
    if (t.isSimpleData()) {
      return "#PCDATA";
    }
    return t.getName();
  }

  private String comboToString(final Regexp<AbstractNode> regexp, Character delimiter) {
    if (!DTDUtils.containsPCDATA(regexp.getTokens())) {
      return listToString(DTDUtils.omitAttributes(regexp.getChildren()), delimiter) +
              regexp.getInterval().toString();
    }

    final List<AbstractNode> content = new ArrayList<AbstractNode>();
    for (final Regexp<AbstractNode> r : DTDUtils.omitAttributes(regexp.getChildren())) {
      content.addAll(r.getTokens());
    }

    Collections.sort(content, DTDUtils.PCDATA_CMP);

    final List<AbstractNode> filteredContent= BaseUtils.filter(content, new Predicate<AbstractNode>() {
      private Set<String> encountered= new HashSet<String>();

      @Override
      public boolean apply(AbstractNode argument) {
        if (encountered.contains(argument.getName())) {
          return false;
        }
        encountered.add(argument.getName());
        return true;
      }
    });

    return CollectionToString.colToString(
            DTDUtils.uniquePCDATA(filteredContent),
            '|',
            new CollectionToString.ToString<AbstractNode>() {
              @Override
              public String toString(final AbstractNode t) {
                return tokenToString(t);
              }
            }) + "*";
  }
  
  private String listToString(final List<Regexp<AbstractNode>> list,
          final char separator) {
    return CollectionToString.colToString(
            list,
            separator,
            new CollectionToString.ToString<Regexp<AbstractNode>>() {

              @Override
              public String toString(final Regexp<AbstractNode> t) {
                return subElementsToString(t, false);
              }
            });
  }

  private String attributesToString(final List<Attribute> attributes) {
    final StringBuilder ret = new StringBuilder();
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
        .append(a.getName())
        .append(DomainUtils.getAttributeType(domain, maxEnumSize));

    // requiredness/default value
    if (a.getMetadata().containsKey("required")) {
      ret.append("#REQUIRED");
    } else {
      ret.append(DomainUtils.getDefault(domain, minDefaultRatio));
    }
    return ret.toString();
  }
}
