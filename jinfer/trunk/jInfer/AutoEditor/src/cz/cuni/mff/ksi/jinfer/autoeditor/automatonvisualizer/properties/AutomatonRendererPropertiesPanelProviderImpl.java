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
package cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.properties;

import cz.cuni.mff.ksi.jinfer.base.interfaces.PropertiesPanelProvider;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.openide.util.lookup.ServiceProvider;

/**
 * Properties panel provider for module selection category.
 * @author anti
 */
@ServiceProvider(service = PropertiesPanelProvider.class)
public class AutomatonRendererPropertiesPanelProviderImpl implements PropertiesPanelProvider {

  public static final String GRAPH_RENDERER = "GraphRenderer";
  public static final String GRAPH_RENDERER_DISPLAY = "Graph renderer";
  private static final int PANEL_PRIORITY = 400000;

  @Override
  public AbstractPropertiesPanel getPanel(final Properties properties) {
    return new AutomatonRendererPropertiesPanel(properties);
  }

  @Override
  public String getName() {
    return "GraphRenderer";
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
    return "AutomatonRenderer";
  }

  @Override
  public List<Pair<String, String>> getSubCategories() {
    List<Pair<String, String>> result = new ArrayList<Pair<String, String>>();
    result.add(new Pair<String, String>(GRAPH_RENDERER,
            GRAPH_RENDERER_DISPLAY));

    return result;
  }
}
