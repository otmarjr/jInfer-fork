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
package cz.cuni.mff.ksi.jinfer.trivialigg.dtd;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.trivialigg.interfaces.Processor;
import cz.cuni.mff.ksi.jinfer.trivialigg.utils.IGGUtils;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class DTDProcessor implements Processor {

  /**
   * Parses the DTD schema and returns the IG rules contained within.
   *
   * @param f DTD schema file.
   * @return List of IG rules retrieved from it.
   */
  @Override
  public List<AbstractNode> process(final InputStream s) {
    try {
      final DTDParser parser = new DTDParser();
      final DTD result = parser.parseExternalSubset(new InputSource(s), null);

      final List<AbstractNode> ret = new ArrayList<AbstractNode>();

      for (final Object o : result.elementTypes.values()) {
        ret.add(processElement((ElementType) o));
      }

      return ret;
    } catch (Exception ex) {
      Logger.getLogger(DTDProcessor.class.getName()).log(Level.SEVERE, null, ex);
    }

    return new ArrayList<AbstractNode>(0);
  }

  private static Element processElement(final ElementType e) {
    final Element ret = new Element(null, e.name.getLocalName(), IGGUtils.ATTR_FROM_SCHEMA, Regexp.<AbstractNode>getConcatenation());
    if (e.attributes.size() > 0) {
      // for each attribute, add a subnode representing it
      for (final Object oa : e.attributes.values()) {
        final Attribute a = (Attribute) oa;
        final Map<String, Object> nodeAttrs = new HashMap<String, Object>(1);
        nodeAttrs.put("required", Boolean.valueOf(a.required == Attribute.REQUIRED_REQUIRED));
        final cz.cuni.mff.ksi.jinfer.base.objects.Attribute at = new cz.cuni.mff.ksi.jinfer.base.objects.Attribute(null, a.name.getLocalName(), nodeAttrs, null, null);
        ret.getSubnodes().addChild(Regexp.<AbstractNode>getToken(at));
      }
    }
    // for each subelement ditto
    if (e.children.size() > 0) {
      for (final Object oc : e.children.values()) {
        final ElementType c = (ElementType) oc;
        final Element child = new Element(null, c.name.getLocalName(), null, Regexp.<AbstractNode>getConcatenation());
        ret.getSubnodes().addChild(Regexp.<AbstractNode>getToken(child));
      }
    }
    return ret;
  }
  
}
