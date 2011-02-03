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
package cz.cuni.mff.ksi.jinfer.base.objects;

import java.util.Properties;
import javax.swing.JPanel;

/**
 * Class representing a panel for category in jInfer project properties.
 *
 * @author sviro
 */
public abstract class AbstractPropertiesPanel extends JPanel {

  protected final Properties properties;

  /**
   * Default contructor.
   *
   * @param properties Properties used to store and load data to panel.
   */
  public AbstractPropertiesPanel(final Properties properties) {
    super();
    this.properties = properties;
  }

  /**
   * Method used to store data from panel to properties.
   */
  public abstract void store();

  /**
   * Method used to load data from properties to panel.
   */
  public abstract void load();

  /**
   * Get Properties associated with this panel.
   * @return Properties used to load and store data to panel.
   */
  public Properties getProperties() {
    return properties;
  }
}
