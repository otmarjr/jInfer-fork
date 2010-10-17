/*
 *  Copyright (C) 2010 reseto
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

package cz.cuni.mff.ksi.jinfer.xsdimporter;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.Exceptions;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * TODO reseto Comment!
 * 
 * @author reseto
 */
public class DOMHandler {

  private static final Logger LOG = Logger.getLogger(SAXHandler.class);

  public void parse(final InputStream stream) {
    LOG.info("Parsing with DOM");
    try {
      final DOMParser parser = new DOMParser();
      parser.parse(new InputSource(stream));
      final Document doc = parser.getDocument();
      final org.w3c.dom.Element root = doc.getDocumentElement();

      final NodeList nl = root.getElementsByTagName("complexType");
      for (int i = 0; i < nl.getLength(); i++) {
        final org.w3c.dom.Element el = (org.w3c.dom.Element) nl.item(i);
        if (el.hasAttribute("name")){
          addNamedCType(el);
        }
      }


    } catch (SAXException ex) {
      Exceptions.printStackTrace(ex);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }

  }

  public List<Element> getRules() {
    LOG.warn("No rules for now, functionality is not implemented.");
    return Collections.emptyList();
  }

  private void addNamedCType(final org.w3c.dom.Element el) {
    LOG.info("Adding complextype: " + el.toString());
  }

}
