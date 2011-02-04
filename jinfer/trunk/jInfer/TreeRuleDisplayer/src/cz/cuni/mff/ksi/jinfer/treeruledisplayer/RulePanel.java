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
package cz.cuni.mff.ksi.jinfer.treeruledisplayer;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author sviro
 */
public class RulePanel extends JPanel {

  private static final long serialVersionUID = 34535l;

  public RulePanel(final JPanel graph, final JPanel legend) {
    super(new BorderLayout(0, 0));
    this.add(graph, BorderLayout.CENTER);
    final JPanel bottom = new JPanel(new BorderLayout(0, 0));
    this.add(bottom, BorderLayout.PAGE_END);
    final JLabel label = new JLabel("Legend:");
    label.setFont(label.getFont().deriveFont(Font.BOLD));
    bottom.add(label, BorderLayout.LINE_START);
    bottom.add(legend, BorderLayout.CENTER);
  }
}
