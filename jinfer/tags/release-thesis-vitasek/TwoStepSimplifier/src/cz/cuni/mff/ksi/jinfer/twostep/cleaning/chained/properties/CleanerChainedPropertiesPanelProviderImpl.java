/*
 *  Copyright (C) 2010 anti
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
package cz.cuni.mff.ksi.jinfer.twostep.cleaning.chained.properties;

import cz.cuni.mff.ksi.jinfer.base.interfaces.PropertiesPanelProvider;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.objects.VirtualCategoryPanel;
import cz.cuni.mff.ksi.jinfer.twostep.cleaning.chained.ChainedFactory;
import java.util.List;
import java.util.Properties;
import org.openide.util.lookup.ServiceProvider;

/**
 * Properties panel provider for module selection category.
 * @author anti
 */
@ServiceProvider(service = PropertiesPanelProvider.class)
public class CleanerChainedPropertiesPanelProviderImpl implements PropertiesPanelProvider {

  private static final int PANEL_PRIORITY = 400000;
  private static final String TWOSTEP_SIMPLIFIER_CLEANER = "Cleaners";

  @Override
  public AbstractPropertiesPanel getPanel(final Properties properties) {
    return new CleanerChainedPropertiesPanel(properties);
  }

  @Override
  public String getName() {
    return ChainedFactory.NAME;
  }

  @Override
  public String getDisplayName() {
    return ChainedFactory.DISPLAY_NAME;
  }

  @Override
  public int getPriority() {
    return PANEL_PRIORITY;
  }

  @Override
  public String getParent() {
    return TWOSTEP_SIMPLIFIER_CLEANER;
  }

  @Override
  public List<VirtualCategoryPanel> getSubCategories() {
    return null;
  }
}
