/*
 *  Copyright (C) 2011 sviro
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
package cz.cuni.mff.ksi.jinfer.base.objects;

import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * TODO sviro Comment!
 * 
 * @author sviro
 */
public class ProjectPropsComboRenderer extends DefaultListCellRenderer {

  private static final long serialVersionUID = 5376457567l;
  private final ListCellRenderer backend;

  public ProjectPropsComboRenderer(final ListCellRenderer backend) {
    this.backend = backend;
  }

  @Override
  public Component getListCellRendererComponent(final JList list,
          final Object value, final int index, final boolean isSelected,
          final boolean cellHasFocus) {

    Component component = backend.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    if (!(component instanceof JLabel)) {
      component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
    final JLabel label = (JLabel) component;
    if (value instanceof NamedModule) {
      label.setText(((NamedModule) value).getDisplayName());
    } else {
      throw new IllegalArgumentException("This renderer can render only NamedModule values.");
    }
    return label;
  }
}
