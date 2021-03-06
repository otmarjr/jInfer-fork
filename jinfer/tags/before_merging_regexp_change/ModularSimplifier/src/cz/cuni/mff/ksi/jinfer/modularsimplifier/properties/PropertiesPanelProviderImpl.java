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

package cz.cuni.mff.ksi.jinfer.modularsimplifier.properties;

import cz.cuni.mff.ksi.jinfer.base.interfaces.PropertiesPanelProvider;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import java.util.Properties;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author sviro
 */
@ServiceProvider(service = PropertiesPanelProvider.class)
public class PropertiesPanelProviderImpl implements PropertiesPanelProvider{
  private static final int PANEL_PRIORITY = 200000;

  @Override
  public AbstractPropertiesPanel getPanel(final Properties properties) {
    return new PropertiesPanel(properties);
  }

  @Override
  public String getName() {
    return PropertiesPanel.NAME;
  }

  @Override
  public String getDisplayName() {
    return "Modular Simplifier";
  }

  @Override
  public int getPriority() {
    return PANEL_PRIORITY;
  }

}
