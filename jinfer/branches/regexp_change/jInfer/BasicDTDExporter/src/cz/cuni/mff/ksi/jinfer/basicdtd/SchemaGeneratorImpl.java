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
import cz.cuni.mff.ksi.jinfer.basicdtd.utils.CollectionToString;
import cz.cuni.mff.ksi.jinfer.basicdtd.utils.DomainUtils;
import java.util.ArrayList;
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
  private int maxEnumSize;
  private double minDefaultRatio;

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
      } else {
        throw new IllegalArgumentException("The output grammar can contain only elements. Got " + node.toString());
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
    ret.append("<!ELEMENT ").append(e.getName()).append(' ');
    Regexp<AbstractNode> regexp= e.getSubnodes();
    if (regexp.isToken()) {
      ret.append("(");
    }
    ret.append(regexpToString(regexp));
    if (regexp.isToken()) {
      ret.append(")");
    }
    ret.append(">\n");
    final Regexp<AbstractNode> attributes = e.getAttributes();
    if (!attributes.isLambda()) {
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
  private String regexpToString(final Regexp<AbstractNode> regexp) {
    // TODO anti Exception when r.getTokens contains attributes
    switch (regexp.getType()) {
      case LAMBDA:
        return "EMPTY";
      case TOKEN:
        String intervalString= regexp.getInterval().toString();
        String contentString= regexp.getContent().isSimpleData() ? "#PCDATA" : regexp.getContent().getName();
        if (regexp.getContent().isSimpleData()) {
          return contentString;
        }
        return contentString + intervalString;
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

  /**
   * Returns true if one of the supplied children is a PCDATA.
   */
  private boolean containsPCDATA(final List<AbstractNode> children) {
    for (final AbstractNode child : children) {
      if (child.isSimpleData()) {
        return true;
      }
    }
    return false;
  }

  private String comboToString(final Regexp<AbstractNode> regexp, Character delimiter) {
    if (!containsPCDATA(regexp.getTokens())) {
      return listToString(regexp.getChildren(), delimiter) +
              regexp.getInterval().toString();
    }

    final List<AbstractNode> content = regexp.getTokens();

    Collections.sort(content,
      new Comparator<AbstractNode>() {
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
        }
    );

    final List<AbstractNode> distinctContent= BaseUtils.filter(content, new Predicate<AbstractNode>() {
      private Set<String> encountered= new HashSet<String>();
      private boolean encounteredSimpleData= false;
      @Override
      public boolean apply(AbstractNode argument) {
        if (argument.isSimpleData()) {
          if (encounteredSimpleData) {
            return false;
          }
          encounteredSimpleData= true;
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
            '|',
            new CollectionToString.ToString<AbstractNode>() {
              @Override
              public String toString(final AbstractNode t) {
                return regexpToString(Regexp.<AbstractNode>getToken(t));
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
                  return regexpToString(t);
                }
            });
  }

  private String attributesToString(final Regexp<AbstractNode> attributes) {
    final StringBuilder ret = new StringBuilder();
    if (!attributes.isConcatenation()) {
      throw new IllegalArgumentException("Attributes have to be concatenation for now.");
    }
    // TODO anti atributes better
    for (final AbstractNode attribute : attributes.getTokens()) {
      ret.append(attributeToString(attribute));
    }
    return ret.toString();
  }

  private String attributeToString(final AbstractNode b) {
    Attribute a = (Attribute) b;
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
