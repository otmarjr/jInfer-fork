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

package cz.cuni.mff.ksi.jinfer.xsdimporter.utils;

import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.xsdimporter.interfaces.XSDParser;
import cz.cuni.mff.ksi.jinfer.xsdimporter.properties.XSDImportPropertiesPanel;
import java.util.Properties;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Helper class for accessing settings from module's properties panel.
 * @author reseto
 */
public final class XSDImportSettings {

  private XSDImportSettings() {}

  private static final Properties PROPERTIES = RunningProject.getActiveProjectProps(XSDImportPropertiesPanel.PANEL_NAME);

  /**
   * Get the selected parser from XSD Import project properties.
   * Parser is selected from all available parsers using lookup for <code>XSDParser</code> interface.
   * @return Selected parser.
   */
  public static XSDParser getParser() {
    return ModuleSelectionHelper.lookupImpl(XSDParser.class, PROPERTIES.getProperty(XSDImportPropertiesPanel.PARSER));
  }

  /**
   * Check if more verbose information should be logged.
   * @return True if verbose setting enabled, else false. False on error.
   */
  public static boolean isVerbose() {
    return Boolean.parseBoolean(PROPERTIES.getProperty(XSDImportPropertiesPanel.VERBOSE_INFO, "false"));
  }

  /**
   * Get current log level for module XSD Importer. On error, or by default this method returns root log level.
   * @return Current log level for XSD Importer.
   */
  public static Level logLevel() {
    return Level.toLevel(PROPERTIES.getProperty(XSDImportPropertiesPanel.LOG_LEVEL), Logger.getRootLogger().getLevel());
  }

  /**
   * Check if import process should halt on any error.
   * If disabled, the file is just skipped and error is logged.
   * By default this setting is enabled.
   * @return True if "Stop on error" checkbox is checked, else false. True if exception occurs.
   */
  public static boolean stopOnError() {
    return Boolean.parseBoolean(PROPERTIES.getProperty(XSDImportPropertiesPanel.STOP_ON_ERROR, "true"));
  }
}
