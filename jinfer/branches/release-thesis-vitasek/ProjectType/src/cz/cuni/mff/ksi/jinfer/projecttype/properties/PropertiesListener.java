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

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import org.netbeans.spi.project.ui.CustomizerProvider;
import org.netbeans.spi.project.ui.support.ProjectCustomizer.Category;

/**
 * Listener which listens to jInfer project properties dialog OK button.
 * 
 * @author sviro
 */
public class PropertiesListener implements ActionListener {

  private final JInferCustomizerProvider customizerProvider;

  /**
   * Default constructor.
   * @param customizerProvider {@link CustomizerProvider} which creates project properties.
   */
  public PropertiesListener(final JInferCustomizerProvider customizerProvider) {
    this.customizerProvider = customizerProvider;
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    if ("ok".equalsIgnoreCase(e.getActionCommand())) {
      for (Category category : customizerProvider.getCategories()) {
        final JComponent panel = customizerProvider.getComponentProvider().create(category);
        if (panel instanceof AbstractPropertiesPanel) {
          ((AbstractPropertiesPanel) panel).store();
        }
      }
    }
  }
}
