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

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.basicigg.interfaces.Processor;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParserFactory;
import org.apache.log4j.Logger;

/**
 * Contains logic for IG retrieval from XML documents.
 * 
 * @author vektor
 */
public class XMLProcessor implements Processor {

  private static final Logger LOG = Logger.getLogger(XMLProcessor.class);
  private static final SAXParserFactory PARSER_FACTORY = SAXParserFactory.newInstance();

  @Override
  public List<AbstractNode> process(final InputStream f) {
    
    final TrivialHandler handler = new TrivialHandler();
    try {
      PARSER_FACTORY.newSAXParser().parse(f, handler);
      return handler.getRules();
    } catch (final Exception e) {
      LOG.error("Error parsing XML file.", e);
    }
    return new ArrayList<AbstractNode>(0);
  }
}
