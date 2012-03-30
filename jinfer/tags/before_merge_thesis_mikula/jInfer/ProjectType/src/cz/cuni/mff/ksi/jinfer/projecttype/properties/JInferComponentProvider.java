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

import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.netbeans.spi.project.ui.support.ProjectCustomizer.Category;

/**
 * Creates JPanels in jInfer project properties dialog for provided categories.
 * 
 * @author sviro
 */
public class JInferComponentProvider implements ProjectCustomizer.CategoryComponentProvider {

  private final Map<Category, JPanel> panels;

  /**
   * Default constructor.
   * @param panels Map of {@link Category categories} and its {@link JPanel panels}.
   */
  public JInferComponentProvider(final Map<Category, JPanel> panels) {
    this.panels = panels;
  }

  @Override
  public JComponent create(final Category category) {
    final JPanel panel = panels.get(category);
    return panel == null ? new JPanel() : panel;
  }
}
