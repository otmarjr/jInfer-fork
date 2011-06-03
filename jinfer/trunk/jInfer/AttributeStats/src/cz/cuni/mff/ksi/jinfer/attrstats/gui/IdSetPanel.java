/*
 * Copyright (C) 2011 vektor
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.attrstats.gui;

import cz.cuni.mff.ksi.jinfer.attrstats.Utils;
import cz.cuni.mff.ksi.jinfer.attrstats.logic.MappingUtils;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.attrstats.tables.MappingsModel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
@SuppressWarnings("PMD.SingularField")
public class IdSetPanel extends JPanel {

  private static final long serialVersionUID = 187541L;

  private List<AttributeMappingId> idSet;
  private AMModel model;

  public IdSetPanel() {
    super();
    initComponents();
    table.setModel(new DefaultTableModel());
  }

  public void setModel(final List<AttributeMappingId> idSet, final AMModel model) {
    this.idSet = new ArrayList<AttributeMappingId>(idSet);
    this.model = model;

    table.setModel(new MappingsModel(idSet));
    weight.setText(Utils.FORMAT.format(MappingUtils.weight(idSet, model)));
  }

  @SuppressWarnings({"unchecked", "PMD"})
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    pane = new javax.swing.JScrollPane();
    table = new javax.swing.JTable();
    labelWeight = new javax.swing.JLabel();
    weight = new javax.swing.JLabel();
    use = new javax.swing.JButton();

    setPreferredSize(new java.awt.Dimension(300, 200));
    setLayout(new java.awt.GridBagLayout());

    pane.setPreferredSize(new java.awt.Dimension(300, 200));

    table.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {
        {null, null, null, null},
        {null, null, null, null},
        {null, null, null, null},
        {null, null, null, null}
      },
      new String [] {
        "Title 1", "Title 2", "Title 3", "Title 4"
      }
    ));
    pane.setViewportView(table);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(pane, gridBagConstraints);

    labelWeight.setText("Weight:"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(labelWeight, gridBagConstraints);

    weight.setFont(weight.getFont().deriveFont(weight.getFont().getStyle() | java.awt.Font.BOLD));
    weight.setText("N/A"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 2);
    add(weight, gridBagConstraints);

    use.setText("Use in Schema"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(use, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel labelWeight;
  private javax.swing.JScrollPane pane;
  private javax.swing.JTable table;
  private javax.swing.JButton use;
  private javax.swing.JLabel weight;
  // End of variables declaration//GEN-END:variables
}
