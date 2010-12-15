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

package cz.cuni.mff.ksi.jinfer.twostep.properties;

import cz.cuni.mff.ksi.jinfer.base.interfaces.PropertiesPanelProvider;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.twostep.TwoStepSimplifierFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.openide.util.lookup.ServiceProvider;

/**
 * Properties panel provider for module selection category.
 * @author anti
 */
@ServiceProvider(service = PropertiesPanelProvider.class)
public class TwoStepPropertiesPanelProviderImpl implements PropertiesPanelProvider {
  public static final String TWOSTEP_SIMPIFIER_CLUSTERER = "Clusterers";
  public static final String TWOSTEP_SIMPLIFIER_CLEANER = "Cleaners";
  public static final String TWOSTEP_SIMPLIFIER_CLUSTERER_PROCESSOR = "ClustererProcessor";
  public static final String TWOSTEP_SIMPLIFIER_CLUSTERER_PROCESSOR_DISPLAY = "Clusterer Processors";
  private static final int PANEL_PRIORITY = 400000;
  public static final String SIMPLIFIER_CATEGORY = "Simplifier";

  @Override
  public AbstractPropertiesPanel getPanel(final Properties properties) {
    return new TwoStepPropertiesPanel(properties);
  }

  @Override
  public String getName() {
    return TwoStepSimplifierFactory.NAME;
  }

  @Override
  public String getDisplayName() {
    return getName();
  }

  @Override
  public int getPriority() {
    return PANEL_PRIORITY;
  }

  @Override
  public String getParent() {
    return SIMPLIFIER_CATEGORY;
  }

  @Override
  public List<Pair<String, String>> getSubCategories() {
    List<Pair<String, String>> result = new ArrayList<Pair<String, String>>();
    result.add(new Pair<String, String>(TWOSTEP_SIMPIFIER_CLUSTERER, TWOSTEP_SIMPIFIER_CLUSTERER));
    result.add(new Pair<String, String>(TWOSTEP_SIMPLIFIER_CLUSTERER_PROCESSOR, TWOSTEP_SIMPLIFIER_CLUSTERER_PROCESSOR_DISPLAY));
    result.add(new Pair<String, String>(TWOSTEP_SIMPLIFIER_CLEANER, TWOSTEP_SIMPLIFIER_CLEANER));

    return result;
  }
}
