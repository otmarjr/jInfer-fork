/*
 *  Copyright (C) 2010 sviro
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
package cz.cuni.mff.ksi.jinfer.base.utils;

import java.util.Properties;

/**
 *
 * @author sviro
 */
public class ModuleProperties extends Properties {

  private final String moduleName;
  private final Properties properties;

  public ModuleProperties(final String moduleName, final Properties properties) {
    this.moduleName = moduleName;
    this.properties = properties;
  }

  @Override
  public synchronized Object put(final Object key, final Object value) {
    return properties.put(moduleName + "." + key, value);
  }

  @Override
  public String getProperty(final String key) {
    return properties.getProperty(moduleName + "." + key);
  }
}
