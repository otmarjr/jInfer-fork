package cz.cuni.mff.ksi.jinfer.projecttype.properties;

import cz.cuni.mff.ksi.jinfer.base.interfaces.PropertiesPanelProvider;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.projecttype.JInferProject;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.spi.project.ui.CustomizerProvider;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.netbeans.spi.project.ui.support.ProjectCustomizer.Category;
import org.openide.util.Lookup;

/**
 *
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
    final Map<Category, JPanel> result = new HashMap<Category, JPanel>();

    final Lookup lkp = Lookup.getDefault();
    for (final PropertiesPanelProvider propPanelProvider : lkp.lookupAll(PropertiesPanelProvider.class)) {
      result.put(Category.create(propPanelProvider.getName(),
              propPanelProvider.getDisplayName(), null), propPanelProvider.getPanel(properties));
    }
    
    return result;
  }

  @Override
  public void showCustomizer() {
    init();

    final Dialog dialog = ProjectCustomizer.createCustomizerDialog(categories, componentProvider,
            null, new PropertiesListener(project), null);

    dialog.setTitle(ProjectUtils.getInformation(project).getDisplayName());

    dialog.setVisible(true);

  }

  private class PropertiesListener implements ActionListener {

    private final JInferProject project;

    private PropertiesListener(final JInferProject project) {
      this.project = project;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
      if ("ok".equalsIgnoreCase(e.getActionCommand())) {
        for (Category category : categories) {
          ((AbstractPropertiesPanel)componentProvider.create(category)).store();
        }
      }
    }
  }

  private class JInferComponentProvider implements ProjectCustomizer.CategoryComponentProvider {

    private final Map<Category, JPanel> panels;
    
    private JInferComponentProvider(final Map<Category, JPanel> panels) {
      this.panels = panels;
    }

    @Override
    public JComponent create(final Category category) {
      final JPanel panel = panels.get(category);
      return panel == null ? new JPanel() : panel;
    }
  }
}
