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
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleProperties;
import cz.cuni.mff.ksi.jinfer.projecttype.JInferProject;
import java.awt.Dialog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
 * @see CustomizerProvider
 */
public class JInferCustomizerProvider implements CustomizerProvider {

  private final JInferProject project;
  private ProjectCustomizer.Category[] categories;
  private ProjectCustomizer.CategoryComponentProvider componentProvider;
  private Map<Category, JPanel> panels;

  public JInferCustomizerProvider(final JInferProject project) {
    this.project = project;
  }

  private void init() {
    final Properties properties = project.getLookup().lookup(Properties.class);

    panels = getCategoriesPanels(properties);

    componentProvider = new JInferComponentProvider(panels);

  }

  private Map<Category, JPanel> getCategoriesPanels(final Properties properties) {
    final Map<Category, JPanel> result = new LinkedHashMap<Category, JPanel>();
    final List<PropertiesPanelProvider> providersList = new ArrayList<PropertiesPanelProvider>(Lookup.
            getDefault().
            lookupAll(PropertiesPanelProvider.class));

    Collections.sort(providersList, new Comparator<PropertiesPanelProvider>() {

      @Override
      public int compare(final PropertiesPanelProvider panel1, final PropertiesPanelProvider panel2) {
        final int priority1 = panel1.getPriority();
        final int priority2 = panel2.getPriority();

        if (priority1 == priority2) {
          return panel1.getDisplayName().compareTo(panel2.getDisplayName());
        }

        return priority2 - priority1;
      }
    });

    final List<PropertiesPanelProvider> topLevelProviders = new ArrayList<PropertiesPanelProvider>();
    for (PropertiesPanelProvider provider : providersList) {
      if (provider.getParent() == null) {
        topLevelProviders.add(provider);
      }
    }

    List<Category> categoriesList = new ArrayList<Category>();
    for (PropertiesPanelProvider topLevelProvider : topLevelProviders) {
      categoriesList.add(buildCategory(topLevelProvider, providersList, result, properties));
    }

    categories = categoriesList.toArray(new Category[categoriesList.size()]);

    return result;
  }

  private Category buildCategory(final PropertiesPanelProvider provider,
          final Collection<? extends PropertiesPanelProvider> providers,
          final Map<Category, JPanel> result, final Properties properties) {
    final String moduleName = provider.getName();
    Category category = null;

    if (provider.getSubCategories() == null) {
      category = Category.create(moduleName,
              provider.getDisplayName(), null);
    } else {
      final List<Category> subCategories = new ArrayList<Category>();
      for (Pair<String, String> subCategory : provider.getSubCategories()) {
        final String subCategoryId = subCategory.getFirst();
        final List<PropertiesPanelProvider> subCategoryProviders = getProvidersByParentId(
                subCategoryId,
                providers);

        final ArrayList<Category> subCateg = new ArrayList<Category>();
        for (PropertiesPanelProvider subCategoryProvider : subCategoryProviders) {
          subCateg.add(buildCategory(subCategoryProvider, providers, result, properties));
        }

        subCategories.add(Category.create(subCategoryId, subCategory.getSecond(), null, subCateg.toArray(new Category[subCateg.
                size()])));
      }

      category = Category.create(moduleName, provider.getDisplayName(), null, subCategories.toArray(
              new Category[subCategories.size()]));
    }
    
    final AbstractPropertiesPanel panel = provider.getPanel(new ModuleProperties(
            moduleName, properties));
    panel.load();

    result.put(category, panel);

    return category;
  }

  private List<PropertiesPanelProvider> getProvidersByParentId(final String subCategoryId,
          final Collection<? extends PropertiesPanelProvider> providers) {
    final List<PropertiesPanelProvider> result = new ArrayList<PropertiesPanelProvider>();
    for (PropertiesPanelProvider provider : providers) {
      if (provider.getParent() != null && provider.getParent().equals(subCategoryId)) {
        result.add(provider);
      }
    }

    return result;
  }

  private Map<Category, JPanel> lookupCategoriesPanels(final Properties properties) {

    final Map<Category, JPanel> result = new LinkedHashMap<Category, JPanel>();
    final List<PropertiesPanelProvider> providersList = new ArrayList<PropertiesPanelProvider>(Lookup.
            getDefault().
            lookupAll(PropertiesPanelProvider.class));

    Collections.sort(providersList, new Comparator<PropertiesPanelProvider>() {

      @Override
      public int compare(final PropertiesPanelProvider panel1, final PropertiesPanelProvider panel2) {
        final int priority1 = panel1.getPriority();
        final int priority2 = panel2.getPriority();

        if (priority1 == priority2) {
          return panel1.getDisplayName().compareTo(panel2.getDisplayName());
        }

        return priority2 - priority1;
      }
    });

    for (PropertiesPanelProvider provider : providersList) {
      final String moduleName = provider.getName();
      final Category category = Category.create(moduleName,
              provider.getDisplayName(), null);

      final AbstractPropertiesPanel panel = provider.getPanel(new ModuleProperties(
              moduleName, properties));
      panel.load();

      result.put(category, panel);
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

  /**
   * Get List of categories is this Project properties dialog.
   * @return List of categories is this Project properties dialog.
   */
  public List<Category> getCategories() {
    return new ArrayList<Category>(panels.keySet());
  }

  /**
   * Get {@link CategoryComponentProvider} for this Project properties dialog.
   * @return CategoryComponentProvider for this Project properties dialog.
   */
  public CategoryComponentProvider getComponentProvider() {
    return componentProvider;
  }
}
