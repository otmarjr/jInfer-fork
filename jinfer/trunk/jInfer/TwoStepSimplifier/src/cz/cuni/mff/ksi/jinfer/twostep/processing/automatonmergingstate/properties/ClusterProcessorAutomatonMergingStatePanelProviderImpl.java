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
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.ClusterProcessorAutomatonMergingStateFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.openide.util.lookup.ServiceProvider;

/**
 * Properties panel provider for module selection category.
 * @author anti
 */
@ServiceProvider(service = PropertiesPanelProvider.class)
public class ClusterProcessorAutomatonMergingStatePanelProviderImpl implements PropertiesPanelProvider {
  public static final String MERGING_STATE_AUTOMATON_SIMPLIFIER = "AutomatonSimplifier";
  public static final String MERGING_STATE_AUTOMATON_SIMPLIFIER_DISPLAY = "Automaton Simplifier";
  public static final String MERGING_STATE_REGEXP_AUTOMATON_SIMPLIFIER = "RegexpAutomatonSimplifier";
  public static final String MERGING_STATE_REGEXP_AUTOMATON_SIMPLIFIER_DISPLAY = "Regexp Automaton Simplifier";
  private static final int PANEL_PRIORITY = 400000;
  public static final String TWOSTEP_SIMPLIFIER_CLUSTERER_PROCESSOR = "ClustererProcessor";

  @Override
  public AbstractPropertiesPanel getPanel(final Properties properties) {
    return new ClusterProcessorAutomatonMergingStatePropertiesPanel(properties);
  }

  @Override
  public String getName() {
    return ClusterProcessorAutomatonMergingStateFactory.NAME;
  }

  @Override
  public String getDisplayName() {
    return ClusterProcessorAutomatonMergingStateFactory.NAME;
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
  public List<Pair<String, String>> getSubCategories() {
    List<Pair<String, String>> result = new ArrayList<Pair<String, String>>();
    result.add(new Pair<String, String>(MERGING_STATE_AUTOMATON_SIMPLIFIER,
            MERGING_STATE_AUTOMATON_SIMPLIFIER_DISPLAY));
    result.add(new Pair<String, String>(MERGING_STATE_REGEXP_AUTOMATON_SIMPLIFIER,
            MERGING_STATE_REGEXP_AUTOMATON_SIMPLIFIER_DISPLAY));

    return result;
  }
}
