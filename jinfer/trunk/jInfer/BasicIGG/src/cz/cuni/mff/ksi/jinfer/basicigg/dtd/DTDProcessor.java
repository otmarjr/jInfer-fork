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
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.SimpleData;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Processor;
import cz.cuni.mff.ksi.jinfer.basicigg.properties.BasicIGGPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.basicigg.utils.IGGUtils;
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
 * Contains logic for IG retrieval from DTD schemas.
 * 
 * @author vektor
 */
@ServiceProvider(service = Processor.class)
public class DTDProcessor implements Processor {

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
      if (Boolean.parseBoolean(RunningProject.getActiveProjectProps(BasicIGGPropertiesPanel.NAME).getProperty(BasicIGGPropertiesPanel.STOP_ON_ERROR, "true"))) {
        throw new RuntimeException("Error processing DTD", e);
      } else {
        LOG.warn("Error processing DTD, ignoring and going on.", e);
        return Collections.emptyList();
      }
    }
  }

  private static Element processElement(final ElementType e) {
    List<cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute> attList=
            new ArrayList<cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute>();
    if (e.attributes.size() > 0) {
      // for each attribute, add a subnode representing it
      for (final Object oa : e.attributes.values()) {
        final Attribute a = (Attribute) oa;
        final Map<String, Object> nodeMetadata = new HashMap<String, Object>(1);
        nodeMetadata.put("required",
                Boolean.valueOf(a.required == Attribute.REQUIRED_REQUIRED));
        final cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute at =
                new cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute(new ArrayList<String>(0),
                                a.name.getLocalName(), nodeMetadata, null,
                                new ArrayList<String>(0));
        attList.add(at);
      }
    }
    final Element ret = Element.getMutable();
    ret.setName(e.name.getLocalName());
    ret.getMetadata().putAll(IGGUtils.ATTR_FROM_SCHEMA);
    ret.getAttributes().addAll(attList);

    // for each subelement ditto
    if (e.children.size() > 0) {
      for (final Object oc : e.children.values()) {
        final ElementType c = (ElementType) oc;
        final Element child = Element.getMutable();
        child.setName(c.name.getLocalName());
        child.getMetadata().putAll(IGGUtils.ATTR_FROM_SCHEMA);
        child.getMetadata().putAll(IGGUtils.METADATA_SENTINEL);
        child.getSubnodes().setType(RegexpType.LAMBDA);
        child.setImmutable();
        ret.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(child));
      }
    }

    // if there is #PCDATA inside...
    if (e.contentType == ElementType.CONTENT_MIXED || e.contentType == ElementType.CONTENT_PCDATA) {
      SimpleData sd = SimpleData.getMutable();
      sd.setImmutable();
      ret.getSubnodes().addChild(
              Regexp.<AbstractStructuralNode>getToken(sd));
    }

    ret.getSubnodes().setType(RegexpType.CONCATENATION);
    ret.getSubnodes().setInterval(RegexpInterval.getOnce());
    ret.setImmutable();

    return ret;
  }

}
