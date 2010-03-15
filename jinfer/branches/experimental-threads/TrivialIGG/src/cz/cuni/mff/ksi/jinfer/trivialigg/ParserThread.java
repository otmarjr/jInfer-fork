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

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

/**
 * Thread - enabled XML parser.
 * 
 * @author vektor
 */
public class ParserThread extends Thread {

  private static final Logger LOG = Logger.getLogger(ParserThread.class.getCanonicalName());
  private final File f;
  private final TrivialHandler handler = new TrivialHandler();

  public ParserThread(final File f) {
    this.f = f;
  }

  public TrivialHandler getHandler() {
    return this.handler;
  }

  @Override
  public void run() {
    final SAXParserFactory parserFactory = SAXParserFactory.newInstance();

    try {
      final SAXParser parser = parserFactory.newSAXParser();
      // do the parsing
      LOG.log(Level.WARNING, "Parsing of " + f.getName() + " started");
      parser.parse(f, handler);
      LOG.log(Level.WARNING, "Parsing of " + f.getName() + " complete");
    } catch (ParserConfigurationException ex) {
      // TODO write errors to Output window
      LOG.log(Level.SEVERE, null, ex);
    } catch (SAXException ex) {
      LOG.log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      LOG.log(Level.SEVERE, null, ex);
    }
  }
}
