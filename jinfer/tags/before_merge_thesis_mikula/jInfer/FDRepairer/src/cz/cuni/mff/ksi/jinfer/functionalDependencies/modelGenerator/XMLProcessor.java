/*
 * Copyright (C) 2011 sviro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.functionalDependencies.modelGenerator;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Processor;
import cz.cuni.mff.ksi.jinfer.base.objects.FolderType;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.RXMLTree;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Implementation of {@link cz.cuni.mff.ksi.jinfer.base.interfaces.Processor}
 * providing logic for initial model retrieval from XML documents.
 * 
 * @author sviro
 */
@ServiceProvider(service = Processor.class)
public class XMLProcessor implements Processor<RXMLTree> {

  private static final Logger LOG = Logger.getLogger(XMLProcessor.class);

  @Override
  public FolderType getFolder() {
    return FolderType.DOCUMENT;
  }

  @Override
  public String getExtension() {
    return "xml";
  }

  @Override
  public boolean processUndefined() {
    return false;
  }

  @Override
  public List<RXMLTree> process(final InputStream s) throws InterruptedException {
    final ArrayList<RXMLTree> result = new ArrayList<RXMLTree>();


    final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    builderFactory.setNamespaceAware(true);
    builderFactory.setIgnoringElementContentWhitespace(true);
    try {
      final DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
      final Document document = documentBuilder.parse(s);
      
      result.add(new RXMLTree(document));

    } catch (ParserConfigurationException ex) {
      LOG.error(ex);
    } catch (SAXException ex) {
      LOG.error("Error processing XML", ex);
    } catch (IOException ex) {
      LOG.error("Error processing XML", ex);
    }

    return result;
  }

  @Override
  public Class<?> getResultType() {
    return RXMLTree.class;
  }
}
