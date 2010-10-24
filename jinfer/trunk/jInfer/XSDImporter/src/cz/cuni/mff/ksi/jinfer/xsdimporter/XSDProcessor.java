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

import cz.cuni.mff.ksi.jinfer.base.interfaces.Processor;
import cz.cuni.mff.ksi.jinfer.base.objects.FolderType;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.xsdimporter.properties.XSDImportPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDException;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDParser;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author reseto
 */
@ServiceProvider(service = Processor.class)
public class XSDProcessor implements Processor {

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
  public List<Element> process(final InputStream stream) {

    //TODO reseto refactor, ask sviro
    final Properties properties = RunningProject.getActiveProjectProps(XSDImportPropertiesPanel.NAME);
    final XSDParser parser = ModuleSelectionHelper.lookupImpl(XSDParser.class,
                                                              properties.getProperty(XSDImportPropertiesPanel.PARSER));

    final Level level = Level.toLevel(properties.getProperty(XSDImportPropertiesPanel.LOG_LEVEL),
                                      Logger.getRootLogger().getLevel());
    LOG.setLevel(level);
    
    try {
      if (parser != null) {
        //SAX or DOM parser selected
        parser.process(stream);
        return parser.getRules();
      } else {
        //no parser selected
        LOG.error("NO PARSER selected for importing XSD schemas, all rules are empty!");
        return Collections.emptyList();
      }
    } catch (final XSDException e) {
      if (Boolean.parseBoolean(properties.getProperty(XSDImportPropertiesPanel.STOP_ON_ERROR, "true"))) {
        throw new RuntimeException("Error parsing XSD schema file.", e);
      } else {
        LOG.error("Error parsing XSD schema file, ignoring and going on.", e);
        return Collections.emptyList();
      }
    }
  }

  @Override
  public boolean processUndefined() {
    return false;
  }
}
