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
import java.util.Collection;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;
import org.xml.sax.SAXException;

/**
 * A trivial implementation of IGGenerator module. Works only with XML documents.
 * 
 * @author vektor
 */
@ServiceProvider(service = IGGenerator.class)
public class IGGeneratorImpl implements IGGenerator {

  private static final Logger LOG = Logger.getLogger(IGGeneratorImpl.class);

  @Override
  public String getModuleName() {
    return "Trivial IG Generator";
  }

  @Override
  public void start(final Input input, final IGGeneratorCallback callback) {
    final List<AbstractNode> ret = new ArrayList<AbstractNode>();

    ret.addAll(getRulesFromDocuments(input.getDocuments()));

    callback.finished(ret);
  }

  private static List<AbstractNode> getRulesFromDocuments(final Collection<File> files) {
    if (files.isEmpty()) {
      return null;
    }

    final SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    final TrivialHandler handler = new TrivialHandler();

    try {
      final SAXParser parser = parserFactory.newSAXParser();
      // do the parsing
      for (final File doc : files) {
        parser.parse(doc, handler);
      }
      return handler.getRules();
    } catch (final ParserConfigurationException ex) {
      LOG.error(null, ex);
    } catch (final SAXException ex) {
      LOG.error(null, ex);
    } catch (final IOException ex) {
      LOG.error(null, ex);
    }

    return null;
  }
}
