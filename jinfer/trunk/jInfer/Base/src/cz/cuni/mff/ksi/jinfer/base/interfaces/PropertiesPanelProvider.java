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
package cz.cuni.mff.ksi.jinfer.base.interfaces;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import java.util.List;
import java.util.Properties;

/**
 * Interface of Properties panel provider for module selection category.
 *
 * @author sviro
 */
public interface PropertiesPanelProvider {

  /**
   * Gets a panel for category.
   * 
   * @param properties Properties used to store and load data to panel.
   * @return Panel for category.
   */
  AbstractPropertiesPanel getPanel(final Properties properties);

  /**
   * Gets a programmatic name of category.
   * 
   * @return Programmatic name of category.
   */
  String getName();

  /**
   * Gets display name of category. If priorities of categories are same, categories
   * are sorted by this.
   *
   * @return Display name of category.
   */
  String getDisplayName();

  /**
   * Gets priority by which is sorted category in jInfer Project properties. Higher
   * priority is first. If the priorities of categories are same, categories are sorted
   * by DisplayName.
   *
   * @return priority to sort by.
   */
  int getPriority();

  /**
   * TODO sviro Comment!
   * 
   * @return
   */
  String getParent();

  List<Pair<String, String>> getSubCategories();
}
