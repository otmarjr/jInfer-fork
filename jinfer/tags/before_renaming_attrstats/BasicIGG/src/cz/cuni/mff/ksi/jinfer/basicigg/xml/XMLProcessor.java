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
package cz.cuni.mff.ksi.jinfer.basicigg.xml;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.FolderType;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Processor;
import cz.cuni.mff.ksi.jinfer.basicigg.properties.BasicIGGPropertiesPanel;
import java.io.IOException;
import java.io.InputStream;
import java.net.NoRouteToHostException;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.SAXParserFactory;
import org.apache.log4j.Logger;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.lookup.ServiceProvider;

/**
 * Implementation of {@link cz.cuni.mff.ksi.jinfer.base.interfaces.Processor}
 * providing logic for IG retrieval from XML documents.
 *
 * @author vektor
 */
@ServiceProvider(service = Processor.class)
public class XMLProcessor implements Processor<Element> {

  private static final SAXParserFactory PARSER_FACTORY = SAXParserFactory.newInstance();
  private static final Logger LOG = Logger.getLogger(XMLProcessor.class);

  @Override
  public String getExtension() {
    return "xml";
  }

  @Override
  public boolean processUndefined() {
    return true;
  }

  @Override
  public FolderType getFolder() {
    return FolderType.DOCUMENT;
  }

  @Override
  public List<Element> process(final InputStream f) {

    final TrivialHandler handler = new TrivialHandler();
    try {
      PARSER_FACTORY.newSAXParser().parse(f, handler);
      return handler.getRules();
    } catch (final Exception e) {
      if (e instanceof NoRouteToHostException
              || e instanceof IOException) {
        final NotifyDescriptor message = new NotifyDescriptor.Message(
                "This XML document has external schema definition, but the schema could not be retrieved."
                + "\nMake sure you're connected to the Internet and the schema URL is valid."
                + "\nAlternatively, remove the schema definition from the document.");
        DialogDisplayer.getDefault().notify(message);
      }
      if (Boolean.parseBoolean(RunningProject.getActiveProjectProps(BasicIGGPropertiesPanel.NAME).
              getProperty(BasicIGGPropertiesPanel.STOP_ON_ERROR_PROP, BasicIGGPropertiesPanel.STOP_ON_ERROR_DEFAULT))) {
        throw new RuntimeException("Error parsing XML file.", e);
      } else {
        LOG.error("Error parsing XML file, ignoring and going on.", e);
        return Collections.emptyList();
      }
    }
  }
  
  @Override
    public Class<?> getResultType() {
        return Element.class;
    }
}
