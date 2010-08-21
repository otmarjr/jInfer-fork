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
package cz.cuni.mff.ksi.jinfer.projecttype.properties;

import cz.cuni.mff.ksi.jinfer.base.interfaces.PropertiesPanelProvider;
import cz.cuni.mff.ksi.jinfer.projecttype.JInferProject;
import java.awt.Dialog;
import java.util.Comparator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import javax.swing.JPanel;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.spi.project.ui.CustomizerProvider;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.netbeans.spi.project.ui.support.ProjectCustomizer.Category;
import org.netbeans.spi.project.ui.support.ProjectCustomizer.CategoryComponentProvider;
import org.openide.util.Lookup;

/**
 * Creates jInfer project Properties Dialog.
 * @author sviro
 */
public class JInferCustomizerProvider implements CustomizerProvider {

  private final JInferProject project;
  private ProjectCustomizer.Category[] categories;
  private ProjectCustomizer.CategoryComponentProvider componentProvider;

  public JInferCustomizerProvider(final JInferProject project) {
    this.project = project;
  }

  private void init() {
    final Properties properties = project.getLookup().lookup(Properties.class);

    final Map<Category, JPanel> panels = lookupCategoriesPanels(properties);
    categories = panels.keySet().toArray(new Category[0]);

    componentProvider = new JInferComponentProvider(panels);

  }

  private Map<Category, JPanel> lookupCategoriesPanels(final Properties properties) {
    final Map<Category, JPanel> result = new TreeMap<Category, JPanel>(new Comparator<Category>() {

      @Override
      public int compare(final Category o1, final Category o2) {
        String categoryName1 = o1.getDisplayName();
        String categoryName2 = o2.getDisplayName();
        return categoryName1.compareTo(categoryName2);
      }
    });

    final Lookup lkp = Lookup.getDefault();
    for (final PropertiesPanelProvider propPanelProvider : lkp.lookupAll(
            PropertiesPanelProvider.class)) {
      result.put(Category.create(propPanelProvider.getName(),
              propPanelProvider.getDisplayName(), null), propPanelProvider.getPanel(properties));
    }

    return result;
  }

  @Override
  public void showCustomizer() {
    init();

    final Dialog dialog = ProjectCustomizer.createCustomizerDialog(categories, componentProvider,
            null, new PropertiesListener(this), null);

    dialog.setTitle(ProjectUtils.getInformation(project).getDisplayName());

    dialog.setVisible(true);

  }

  public Category[] getCategories() {
    return categories;
  }

  public CategoryComponentProvider getComponentProvider() {
    return componentProvider;
  }

  
}
