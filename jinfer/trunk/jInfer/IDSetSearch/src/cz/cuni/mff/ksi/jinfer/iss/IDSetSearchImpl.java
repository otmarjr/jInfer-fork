/*
 * Copyright (C) 2011 vektor
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
package cz.cuni.mff.ksi.jinfer.iss;

import cz.cuni.mff.ksi.jinfer.iss.gui.StatisticsPanel;
import cz.cuni.mff.ksi.jinfer.iss.gui.ISSTopComponent;
import cz.cuni.mff.ksi.jinfer.iss.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.iss.utils.GraphUtils;
import cz.cuni.mff.ksi.jinfer.base.interfaces.IDSetSearch;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 * Implementation of the ID Set Search service.
 *
 * @author vektor
 */
@ServiceProvider(service = IDSetSearch.class)
public class IDSetSearchImpl implements IDSetSearch {

  private static final String NAME = "IDSetSearch";
  private static final String DISPLAY_NAME = "ID Set Search";

  @Override
  public void showIDSetPanel(final String panelName, final List<Element> grammar) {
    final ISSTopComponent topComponent = ISSTopComponent.findInstance();
    if (!topComponent.isOpened()) {
      topComponent.open();
    }
    final StatisticsPanel panel = topComponent.createNewPanel(panelName);
    panel.setModel(grammar);
  }

  @Override
  public String getGraphVizInput(final List<Element> grammar) {
    return GraphUtils.getGraphVizInput(new AMModel(grammar));
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }

  @Override
  public String getModuleDescription() {
    return getDisplayName();
  }

}
