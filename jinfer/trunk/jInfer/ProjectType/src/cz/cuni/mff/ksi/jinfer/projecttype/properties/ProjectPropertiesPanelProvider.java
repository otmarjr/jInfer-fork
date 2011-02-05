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

package cz.cuni.mff.ksi.jinfer.projecttype.properties;

import cz.cuni.mff.ksi.jinfer.base.interfaces.PropertiesPanelProvider;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.objects.VirtualCategoryPanel;
import java.util.List;
import java.util.Properties;
import org.openide.util.lookup.ServiceProvider;

/**
 * PropertiesPanelProvider for Project properties category.
 * @author sviro
 */
@ServiceProvider(service = PropertiesPanelProvider.class)
public class ProjectPropertiesPanelProvider implements PropertiesPanelProvider{
  private static final String CATEGORY_DISPLAY_NAME = "Miscellaneous";
  /**
   * Programmatic name of this category.
   */
  public static final String CATEGORY_NAME = "ProjectProps";

  @Override
  public AbstractPropertiesPanel getPanel(final Properties properties) {
    return new ProjectPropertiesPanel(properties);
  }

  @Override
  public String getName() {
    return CATEGORY_NAME;
  }

  @Override
  public String getDisplayName() {
    return CATEGORY_DISPLAY_NAME;
  }

  @Override
  public int getPriority() {
    return 10;
  }

  @Override
  public String getParent() {
    return null;
  }

  @Override
  public List<VirtualCategoryPanel> getSubCategories() {
    return null;
  }

}
