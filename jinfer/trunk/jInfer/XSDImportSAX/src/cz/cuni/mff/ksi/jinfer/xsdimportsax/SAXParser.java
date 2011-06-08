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

package cz.cuni.mff.ksi.jinfer.xsdimportsax;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.xsdimporter.interfaces.XSDParser;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.InterruptChecker;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDException;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDImportSettings;
import cz.cuni.mff.ksi.jinfer.xsdimportsax.utils.SAXInterruptedException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.apache.log4j.Logger;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.xml.sax.SAXException;

/**
 * Class responsible for creating Initial Grammar rules from XSD Schema.
 * Provides XSD Schema parsing support using {@link javax.xml.parsers.SAXParserFactory }.
 * @author reseto
 */
@ServiceProvider(service = XSDParser.class)
public class SAXParser implements XSDParser {
  private static final String NAME = "SAXParser";
  private static final String DISPLAY_NAME = "SAX Parser";
  private static final SAXParserFactory PARSER_FACTORY = SAXParserFactory.newInstance();
  private static final Logger LOG = Logger.getLogger(SAXParser.class);

  @Override
  public List<Element> parse(final InputStream stream) throws XSDException, InterruptedException {
    LOG.setLevel(XSDImportSettings.getLogLevel());

    final SAXHandler handler = new SAXHandler();

    try {
      PARSER_FACTORY.newSAXParser().parse(stream, handler);
      InterruptChecker.checkInterrupt();
    } catch (SAXInterruptedException sIe) {
      throw new InterruptedException();
    } catch (SAXException ex) {
      throw new XSDException(NbBundle.getMessage(SAXParser.class, "Error.GenericExceptionMessage"), ex);
    } catch (IOException ex) {
      throw new XSDException(NbBundle.getMessage(SAXParser.class, "Error.GenericExceptionMessage"), ex);
    } catch (ParserConfigurationException ex) {
      throw new XSDException(NbBundle.getMessage(SAXParser.class, "Error.GenericExceptionMessage"), ex);
    }

    return handler.getRules();
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getModuleDescription() {
    return NbBundle.getMessage(SAXParser.class, "Module.Description");
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }

}
