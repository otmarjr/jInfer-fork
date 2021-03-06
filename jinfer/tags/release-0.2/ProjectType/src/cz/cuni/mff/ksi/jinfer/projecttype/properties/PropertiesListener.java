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
import cz.cuni.mff.ksi.jinfer.projecttype.JInferProject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.spi.project.ui.support.ProjectCustomizer.Category;

/**
 * Listener which listens to jInfer project properties dialog OK button.
 * 
 * @author sviro
 */
public class PropertiesListener implements ActionListener {

  private final JInferCustomizerProvider customizerProvider;

  public PropertiesListener(final JInferCustomizerProvider customizerProvider) {
    this.customizerProvider = customizerProvider;
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    if ("ok".equalsIgnoreCase(e.getActionCommand())) {
      for (Category category : customizerProvider.getCategories()) {
        ((AbstractPropertiesPanel) customizerProvider.getComponentProvider().create(category)).store();
      }
    }
  }
}
