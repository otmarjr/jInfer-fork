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
package cz.cuni.mff.ksi.jinfer.runner.properties;

import cz.cuni.mff.ksi.jinfer.base.interfaces.PropertiesPanelProvider;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.IGGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.SchemaGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.Simplifier;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.objects.VirtualCategoryPanel;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.openide.util.lookup.ServiceProvider;

/**
 * Properties panel provider of the Runner module.
 *
 * @author sviro
 */
@ServiceProvider(service = PropertiesPanelProvider.class)
public class PropertiesPanelProviderImpl implements PropertiesPanelProvider{
  public static final String EXPORTER_CATEGORY = "Exporter";
  public static final String EXPORTER_CATEGORY_DISPLAY = "Exporters";
  public static final String IGG_CATEGORY = "IGG";
  public static final String IGG_CATEGORY_DISPLAY = "Initial grammar generators";
  public static final String SIMPLIFIER_CATEGORY = "Simplifier";
  public static final String SIMPLIFIER_CATEGORY_DISPLAY = "Simplifiers";
  private static final int PANEL_PRIORITY = 500000;

  @Override
  public AbstractPropertiesPanel getPanel(final Properties properties) {
    return new ModuleSelectionPropertiesPanel(properties);
  }

  @Override
  public String getName() {
    return ModuleSelectionPropertiesPanel.NAME;
  }

  @Override
  public String getDisplayName() {
    return ModuleSelectionPropertiesPanel.DISPLAY_NAME;
  }

  @Override
  public int getPriority() {
    return PANEL_PRIORITY;
  }

  @Override
  public String getParent() {
    return null;
  }

  @Override
  public List<VirtualCategoryPanel> getSubCategories() {
    final List<VirtualCategoryPanel> result = new ArrayList<VirtualCategoryPanel>();
    result.add(new VirtualCategoryPanel(IGG_CATEGORY, IGG_CATEGORY_DISPLAY, ModuleSelectionHelper.lookupImpls(IGGenerator.class)));
    result.add(new VirtualCategoryPanel(SIMPLIFIER_CATEGORY, SIMPLIFIER_CATEGORY_DISPLAY, ModuleSelectionHelper.lookupImpls(Simplifier.class)));
    result.add(new VirtualCategoryPanel(EXPORTER_CATEGORY, EXPORTER_CATEGORY_DISPLAY, ModuleSelectionHelper.lookupImpls(SchemaGenerator.class)));

    return result;
  }

}
