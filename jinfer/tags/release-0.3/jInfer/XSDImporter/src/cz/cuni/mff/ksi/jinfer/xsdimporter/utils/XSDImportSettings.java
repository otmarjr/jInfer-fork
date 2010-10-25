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

import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.xsdimporter.properties.XSDImportPropertiesPanel;
import java.util.Properties;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author reseto
 */
public class XSDImportSettings {
  private final Properties properties = RunningProject.getActiveProjectProps(XSDImportPropertiesPanel.NAME);

  public boolean isVerbose() {
    return Boolean.parseBoolean(properties.getProperty(XSDImportPropertiesPanel.VERBOSE_INFO));
  }

  public Level logLevel() {
    return Level.toLevel(properties.getProperty(XSDImportPropertiesPanel.LOG_LEVEL), Logger.getRootLogger().getLevel());
  }

  private boolean isStopOnError() {
    return Boolean.parseBoolean(properties.getProperty(XSDImportPropertiesPanel.STOP_ON_ERROR));
  }
}
