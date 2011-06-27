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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.properties;

import cz.cuni.mff.ksi.jinfer.base.interfaces.PropertiesPanelProvider;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.objects.VirtualCategoryPanel;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.AutomatonMergingStateFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTesterFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.RegexpAutomatonSimplifierFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifierFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.openide.util.lookup.ServiceProvider;

/**
 * Properties panel provider for module selection category.
 * @author anti
 */
@ServiceProvider(service = PropertiesPanelProvider.class)
public class AutomatonMergingStatePropertiesPanelProviderImpl implements PropertiesPanelProvider {

  private static final String MERGING_STATE_AUTOMATON_CONDITION_TESTING = "ConditionTesting";
  private static final String MERGING_STATE_AUTOMATON_CONDITION_TESTING_DISPLAY = "Condition testers";
  private static final String MERGING_STATE_AUTOMATON_SIMPLIFIER = "AutomatonSimplifier";
  private static final String MERGING_STATE_AUTOMATON_SIMPLIFIER_DISPLAY = "Automaton simplifiers";
  private static final String MERGING_STATE_REGEXP_AUTOMATON_SIMPLIFIER = "RegexpAutomatonSimplifier";
  private static final String MERGING_STATE_REGEXP_AUTOMATON_SIMPLIFIER_DISPLAY = "Regexp automaton simplifiers";
  private static final String MERGING_STATE_AUTOMATON_RENDERER = "AutomatonRenderer";
  private static final String MERGING_STATE_AUTOMATON_RENDERER_DISPLAY = "Automaton renderer";
  private static final int PANEL_PRIORITY = 400000;
  public static final String TWOSTEP_SIMPLIFIER_CLUSTERER_PROCESSOR = "ClustererProcessor";

  @Override
  public AbstractPropertiesPanel getPanel(final Properties properties) {
    return new AutomatonMergingStatePropertiesPanel(properties);
  }

  @Override
  public String getName() {
    return AutomatonMergingStateFactory.NAME;
  }

  @Override
  public String getDisplayName() {
    return AutomatonMergingStateFactory.DISPLAY_NAME;
  }

  @Override
  public int getPriority() {
    return PANEL_PRIORITY;
  }

  @Override
  public String getParent() {
    return TWOSTEP_SIMPLIFIER_CLUSTERER_PROCESSOR;
  }

  @Override
  public List<VirtualCategoryPanel> getSubCategories() {
    final List<VirtualCategoryPanel> result = new ArrayList<VirtualCategoryPanel>();
    result.add(new VirtualCategoryPanel(MERGING_STATE_AUTOMATON_CONDITION_TESTING, MERGING_STATE_AUTOMATON_CONDITION_TESTING_DISPLAY, ModuleSelectionHelper.lookupImpls(MergeConditionTesterFactory.class)));
    result.add(new VirtualCategoryPanel(MERGING_STATE_AUTOMATON_SIMPLIFIER, MERGING_STATE_AUTOMATON_SIMPLIFIER_DISPLAY, ModuleSelectionHelper.lookupImpls(AutomatonSimplifierFactory.class)));
    result.add(new VirtualCategoryPanel(MERGING_STATE_REGEXP_AUTOMATON_SIMPLIFIER, MERGING_STATE_REGEXP_AUTOMATON_SIMPLIFIER_DISPLAY, ModuleSelectionHelper.lookupImpls(RegexpAutomatonSimplifierFactory.class)));
    result.add(new VirtualCategoryPanel(MERGING_STATE_AUTOMATON_RENDERER, MERGING_STATE_AUTOMATON_RENDERER_DISPLAY, null));

    return result;
  }
}
