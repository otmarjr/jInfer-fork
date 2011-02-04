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
    GridBagConstraints graphConstraints = new GridBagConstraints();
    graphConstraints.gridx = 0;
    graphConstraints.gridy = 0;
    graphConstraints.fill = GridBagConstraints.BOTH;
    graphConstraints.gridwidth = 2;
    graphConstraints.weightx = 1.0;
    graphConstraints.weighty = 1.0;
    this.add(graph, graphConstraints);


    final JPanel legendPanel = new JPanel(new GridBagLayout());
    legendPanel.setPreferredSize(legend.getSize());

    final JLabel label = new JLabel("Legend");
    label.setFont(label.getFont().deriveFont(Font.BOLD));
    GridBagConstraints labelConstraints = new GridBagConstraints();
    labelConstraints = new GridBagConstraints();
    labelConstraints.gridx = 0;
    labelConstraints.gridy = 0;
    labelConstraints.insets = new Insets(2, 12, 2, 12);
    legendPanel.add(label,labelConstraints);

    GridBagConstraints legendGraphConstraints = new GridBagConstraints();
    legendGraphConstraints.gridx = 1;
    legendGraphConstraints.gridy = 0;
    legendGraphConstraints.fill = GridBagConstraints.BOTH;
    legendGraphConstraints.weightx = 1.0;
    legendGraphConstraints.weighty = 1.0;
    legendPanel.add(legend, legendGraphConstraints);


    GridBagConstraints scrollPaneConstraints = new GridBagConstraints();
    scrollPaneConstraints.gridx = 0;
    scrollPaneConstraints.gridy = 1;
    scrollPaneConstraints.fill = GridBagConstraints.BOTH;
    scrollPaneConstraints.weightx = 1.0;
    final JScrollPane jScrollPane = new JScrollPane(legendPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    this.add(jScrollPane, scrollPaneConstraints);
  }
}
