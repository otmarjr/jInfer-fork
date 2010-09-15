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

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.FolderType;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.basicigg.interfaces.Processor;
import cz.cuni.mff.ksi.jinfer.basicigg.properties.BasicIGGPropertiesPanel;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.SAXParserFactory;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author reseto
 */
@ServiceProvider(service = Processor.class)
public class XSDProcessor implements Processor {

  private static final SAXParserFactory PARSER_FACTORY = SAXParserFactory.newInstance();
  private static final Logger LOG = Logger.getLogger(XSDProcessor.class);

  @Override
  public FolderType getFolder() {
    return FolderType.SCHEMA;
  }

  @Override
  public String getExtension() {
    return "xsd";
  }

  @Override
  public List<AbstractNode> process(InputStream stream) {
    final XSDSimpleHandler handler = new XSDSimpleHandler();
    try {
      
      PARSER_FACTORY.newSAXParser().parse(stream, handler);
      return handler.getRules();
    } catch (final Exception e) {
      if (Boolean.parseBoolean(RunningProject.getActiveProjectProps().getProperty(BasicIGGPropertiesPanel.STOP_ON_ERROR, "true"))) {
        throw new RuntimeException("Error parsing XSD schema file.", e);
      } else {
        LOG.warn("Error parsing XSD schema file, ignoring and going on.", e);
        return Collections.emptyList();
      }
    }
  }

}
