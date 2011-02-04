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

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author sviro
 */
public class RulePanel extends JPanel {

  private static final long serialVersionUID = 34535l;
  private final JPanel graph;
  private final JPanel legend;

  public RulePanel(final JPanel graph, final JPanel legend) {
    super(new GridBagLayout());
    this.graph = graph;
    this.legend = legend;

    initComponents();
  }

  private void initComponents() {
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = GridBagConstraints.BOTH;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    this.add(graph, gridBagConstraints);


    final JPanel legendPanel = new JPanel(new GridBagLayout());
    legendPanel.setPreferredSize(legend.getSize());
    final JLabel label = new JLabel("Legend");
    label.setFont(label.getFont().deriveFont(Font.BOLD));
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 12, 2, 12);
    legendPanel.add(label,gridBagConstraints);

    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    legendPanel.add(legend, gridBagConstraints);


    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    final JScrollPane jScrollPane = new JScrollPane(legendPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    this.add(jScrollPane, gridBagConstraints);
  }
}
