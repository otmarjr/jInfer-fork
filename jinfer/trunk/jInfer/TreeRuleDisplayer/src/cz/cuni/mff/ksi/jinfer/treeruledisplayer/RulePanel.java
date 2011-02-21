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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.netbeans.api.options.OptionsDisplayer;

/**
 * Panel for Tree rule displayer with <i>show settings</i> button.
 * @author sviro
 */
public class RulePanel extends JPanel {

  private static final long serialVersionUID = 34535l;
  private final JPanel graph;

  /**
   * Default constructor, the parameter is panel with rule displayer graph.
   * @param graph Panel with rule displayer graph.
   */
  public RulePanel(final JPanel graph) {
    super(new GridBagLayout());
    this.graph = graph;

    initComponents();
  }

  private void initComponents() {
    final GridBagConstraints graphConstraints = new GridBagConstraints();
    graphConstraints.gridx = 0;
    graphConstraints.gridy = 0;
    graphConstraints.fill = GridBagConstraints.BOTH;
    graphConstraints.weightx = 1.0;
    graphConstraints.weighty = 1.0;
    this.add(graph, graphConstraints);

    final JButton settings = new JButton("Show Settings");
    settings.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(final ActionEvent e) {
        showSettings();
      }
    });

    GridBagConstraints settingsConstraints = new GridBagConstraints();
    settingsConstraints = new GridBagConstraints();
    settingsConstraints.gridx = 0;
    settingsConstraints.gridy = 1;
    settingsConstraints.insets = new Insets(2, 12, 2, 2);
    settingsConstraints.anchor = GridBagConstraints.WEST;
    this.add(settings, settingsConstraints);
  }

  private void showSettings() {
    OptionsDisplayer.getDefault().open("jInfer/treeRuleDisplayer");
  }
}
