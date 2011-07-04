/*
 * Copyright (C) 2011 anti
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordered.ordering.weighted.properties;

import cz.cuni.mff.ksi.jinfer.base.interfaces.PropertiesPanelProvider;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.objects.VirtualCategoryPanel;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordered.ordering.weighted.WeightedFactory;
import java.util.List;
import java.util.Properties;
import org.openide.util.lookup.ServiceProvider;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
@ServiceProvider(service = PropertiesPanelProvider.class)
public class WeightedPropertiesPanetProviderImpl implements PropertiesPanelProvider {
  private static final int PANEL_PRIORITY = 400000;
  private static final String UPCATEGORY = "ordering";


  @Override
  public AbstractPropertiesPanel getPanel(Properties properties) {
    return new WeightedPropertiesPanel(properties);
  }

  @Override
  public String getName() {
    return WeightedFactory.NAME;
  }

  @Override
  public String getDisplayName() {
    return WeightedFactory.DISPLAY_NAME;
  }

  @Override
  public int getPriority() {
    return PANEL_PRIORITY;
  }

  @Override
  public String getParent() {
    return UPCATEGORY;
  }

  @Override
  public List<VirtualCategoryPanel> getSubCategories() {
    return null;
  }
  
}
