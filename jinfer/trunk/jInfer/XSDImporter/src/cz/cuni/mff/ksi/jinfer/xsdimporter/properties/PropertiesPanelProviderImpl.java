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

package cz.cuni.mff.ksi.jinfer.xsdimporter.properties;

import cz.cuni.mff.ksi.jinfer.base.interfaces.PropertiesPanelProvider;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.objects.VirtualCategoryPanel;
import cz.cuni.mff.ksi.jinfer.basicigg.properties.BasicIGGPropertiesPanel;
import java.util.List;
import java.util.Properties;
import org.openide.util.lookup.ServiceProvider;

/**
 * Properties panel provider for XSD Import.
 * @author reseto
 */
@ServiceProvider(service = PropertiesPanelProvider.class)
public class PropertiesPanelProviderImpl implements PropertiesPanelProvider {
  
  @Override
  public AbstractPropertiesPanel getPanel(final Properties properties) {
    return new XSDImportPropertiesPanel(properties);
  }

  @Override
  public String getName() {
    return XSDImportPropertiesPanel.PANEL_NAME;
  }

  @Override
  public String getDisplayName() {
    return XSDImportPropertiesPanel.PANEL_DISPLAY_NAME;
  }

  @Override
  public int getPriority() {
    return XSDImportPropertiesPanel.PANEL_PRIORITY;
  }

  @Override
  public String getParent() {
    return BasicIGGPropertiesPanel.NAME;
  }

  @Override
  public List<VirtualCategoryPanel> getSubCategories() {
    return null;
  }

}
