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
package cz.cuni.mff.ksi.jinfer.xsdimportdom;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDImportSettings;
import cz.cuni.mff.ksi.jinfer.xsdimporter.interfaces.XSDParser;
import java.io.InputStream;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 * Class providing XSD Schema parsing support using DOM parser.
 * @author reseto
 */
@ServiceProvider(service = XSDParser.class)
public class DOMParser implements XSDParser {
  public static final String NAME = "DOMParser";
  public static final String DISPLAY_NAME = "DOM Parser";

  private DOMHandler handler;
  private static final Logger LOG = Logger.getLogger(DOMParser.class);

  @Override
  public void process(final InputStream stream) {
    final XSDImportSettings settings = new XSDImportSettings();
    LOG.setLevel(settings.logLevel());
    handler = new DOMHandler();
    handler.parse(stream);
  }

  @Override
  public List<Element> getRules() {
    return handler.getRules();
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getModuleDescription() {
    return NbBundle.getMessage(DOMParser.class, "Module.Description");
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }
}
