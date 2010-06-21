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
package cz.cuni.mff.ksi.jinfer.trivialigg;

import cz.cuni.mff.ksi.jinfer.base.interfaces.IGGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.IGGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.openide.util.lookup.ServiceProvider;
import org.xml.sax.SAXException;

/**
 * A trivial implementation of IGGenerator module. Works only with XML documents.
 * 
 * @author vektor
 */
@ServiceProvider(service = IGGenerator.class)
public class IGGeneratorImpl implements IGGenerator {

  private static final Logger LOG = Logger.getLogger(IGGeneratorImpl.class.getName());

  @Override
  public String getModuleName() {
    return "Trivial IG Generator";
  }

  @Override
  public void start(final Input input, final IGGeneratorCallback callback) {
    final SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    final TrivialHandler handler = new TrivialHandler();

    try {
      final SAXParser parser = parserFactory.newSAXParser();
      // do the parsing
      for (final File doc : input.getDocuments()) {
        parser.parse(doc, handler);
      }
    } catch (ParserConfigurationException ex) {
      // TODO vektor write errors to Output window
      LOG.log(Level.SEVERE, null, ex);
    } catch (SAXException ex) {
      LOG.log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      LOG.log(Level.SEVERE, null, ex);
    }

    callback.finished(handler.getRules());
  }
}
