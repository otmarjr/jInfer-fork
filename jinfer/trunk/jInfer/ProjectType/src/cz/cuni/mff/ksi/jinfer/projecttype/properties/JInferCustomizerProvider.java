package cz.cuni.mff.ksi.jinfer.projecttype.properties;

import cz.cuni.mff.ksi.jinfer.projecttype.JInferProject;
import cz.cuni.mff.ksi.jinfer.projecttype.properties.panels.DTDExportJPanel;
import cz.cuni.mff.ksi.jinfer.projecttype.properties.panels.ModularSimplifierJPanel;
import cz.cuni.mff.ksi.jinfer.projecttype.properties.panels.ModuleSelectionJPanel;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.spi.project.ui.CustomizerProvider;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.netbeans.spi.project.ui.support.ProjectCustomizer.Category;

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
    final ProjectCustomizer.Category moduleSelection = ProjectCustomizer.Category.create("moduleSelection",
            "Module Selection", null);

    final ProjectCustomizer.Category modularSimplifier = ProjectCustomizer.Category.create("modularSimplifier",
            "Modular Simplifier", null);

    final ProjectCustomizer.Category dtdExport = ProjectCustomizer.Category.create("dtdExport",
            "DTD Export", null);

    categories = new ProjectCustomizer.Category[] {moduleSelection, modularSimplifier, dtdExport};

    final Map<Category, JPanel> panels = new HashMap<Category, JPanel>();
    panels.put(moduleSelection, new ModuleSelectionJPanel());
    panels.put(modularSimplifier, new ModularSimplifierJPanel());
    panels.put(dtdExport, new DTDExportJPanel());

    componentProvider = new JInferComponentProvider(panels);

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
      //TODO changing properties
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
