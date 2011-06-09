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
package cz.cuni.mff.ksi.jinfer.basicigg.dtd;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.FolderType;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Processor;
import cz.cuni.mff.ksi.jinfer.basicigg.properties.BasicIGGPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.utils.IGGUtils;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;
import org.xml.sax.InputSource;
import org.xmlmiddleware.schemas.dtds.Attribute;
import org.xmlmiddleware.schemas.dtds.DTD;
import org.xmlmiddleware.schemas.dtds.DTDParser;
import org.xmlmiddleware.schemas.dtds.ElementType;

/**
 * Implementation of {@link cz.cuni.mff.ksi.jinfer.base.interfaces.Processor}
 * providing logic for IG retrieval from DTD schemas.
 *
 * @author vektor
 */
@ServiceProvider(service = Processor.class)
public class DTDProcessor implements Processor<Element> {

  private static final Logger LOG = Logger.getLogger(DTDProcessor.class);

  @Override
  public String getExtension() {
    return "dtd";
  }

  @Override
  public boolean processUndefined() {
    return false;
  }

  @Override
  public FolderType getFolder() {
    return FolderType.SCHEMA;
  }

  /**
   * Parses the DTD schema and returns the IG rules contained within.
   *
   * @param s DTD schema file.
   * @return List of IG rules retrieved from it.
   */
  @Override
  public List<Element> process(final InputStream s) {
    try {
      final DTDParser parser = new DTDParser();
      final DTD result = parser.parseExternalSubset(new InputSource(s), null);

      final List<Element> ret = new ArrayList<Element>();

      for (final Object o : result.elementTypes.values()) {
        ret.add(processElement((ElementType) o));
      }

      return ret;
    } catch (final Exception e) {
      if (Boolean.parseBoolean(RunningProject.getActiveProjectProps(BasicIGGPropertiesPanel.NAME).getProperty(BasicIGGPropertiesPanel.STOP_ON_ERROR_PROP, BasicIGGPropertiesPanel.STOP_ON_ERROR_DEFAULT))) {
        throw new RuntimeException("Error processing DTD", e);
      } else {
        LOG.error("Error processing DTD, ignoring and going on.", e);
        return Collections.emptyList();
      }
    }
  }

  private static Element processElement(final ElementType e) {
    final Regexp<AbstractStructuralNode> re = DTD2RETranslator.particle2Regexp(e.content,
            e.contentType == ElementType.CONTENT_MIXED
              || e.contentType == ElementType.CONTENT_PCDATA);

    final Element el = new Element(Collections.<String>emptyList(),
            e.name.getLocalName(),
            new HashMap<String, Object>(IGGUtils.ATTR_FROM_SCHEMA),
            re, getAttributes(e));

    return el;
  }

  private static List<cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute>
          getAttributes(final ElementType e) {
    final List<cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute> attList =
            new ArrayList<cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute>();
    if (e.attributes.size() > 0) {
      // for each attribute, add a subnode representing it
      for (final Object oa : e.attributes.values()) {
        final Attribute a = (Attribute) oa;
        final Map<String, Object> nodeMetadata = new HashMap<String, Object>(1);
        nodeMetadata.put(IGGUtils.REQUIRED, Boolean.valueOf(a.required == Attribute.REQUIRED_REQUIRED));
        final cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute at =
                new cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute(
                IGGUtils.EMPTY_CONTEXT, a.name.getLocalName(),
                nodeMetadata, null, new ArrayList<String>(0));
        attList.add(at);
      }
    }
    return attList;
  }
  
  @Override
    public Class<?> getResultType() {
        return Element.class;
    }
}
