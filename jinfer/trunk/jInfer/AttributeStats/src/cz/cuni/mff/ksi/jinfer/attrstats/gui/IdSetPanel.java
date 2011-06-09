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
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.IdSet;
import cz.cuni.mff.ksi.jinfer.attrstats.tables.MappingsModel;
import java.util.Collections;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import static cz.cuni.mff.ksi.jinfer.attrstats.Utils.NA;

/**
 * Panel displaying one ID set in a table.
 *
 * @author vektor
 */
@SuppressWarnings("PMD.SingularField")
public class IdSetPanel extends JPanel {

  private static final long serialVersionUID = 187541L;

  private IdSet idSet;
  private AMModel model;

  public IdSetPanel() {
    super();
    initComponents();
    table.setModel(new DefaultTableModel());
  }

  /**
   * TODO vektor Comment!
   *
   * @param idSet
   * @param model
   * @param alpha
   * @param beta
   */
  public void setModel(final IdSet idSet, final AMModel model,
          final double alpha, final double beta) {
    this.idSet = idSet;
    Collections.sort(this.idSet.getMappings());
    this.model = model;

    table.setModel(new MappingsModel(idSet.getMappings()));
    weight.setText(Utils.FORMAT.format(model.weight(idSet.getMappings(), alpha, beta)));
    optimal.setText(Utils.boolToString(idSet.isOptimal()));
  }

  @SuppressWarnings({"unchecked", "PMD"})
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    pane = new javax.swing.JScrollPane();
    table = new javax.swing.JTable();
    labelWeight = new javax.swing.JLabel();
    labelOptimal = new javax.swing.JLabel();
    weight = new javax.swing.JTextField();
    optimal = new javax.swing.JTextField();

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
    gridBagConstraints.gridwidth = 5;
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

    labelOptimal.setText("Optimal?"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(labelOptimal, gridBagConstraints);

    weight.setEditable(false);
    weight.setFont(weight.getFont().deriveFont(weight.getFont().getStyle() | java.awt.Font.BOLD));
    weight.setText(NA);
    weight.setBorder(null);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(weight, gridBagConstraints);

    optimal.setEditable(false);
    optimal.setFont(optimal.getFont().deriveFont(optimal.getFont().getStyle() | java.awt.Font.BOLD));
    optimal.setText(NA);
    optimal.setBorder(null);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(optimal, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel labelOptimal;
  private javax.swing.JLabel labelWeight;
  private javax.swing.JTextField optimal;
  private javax.swing.JScrollPane pane;
  private javax.swing.JTable table;
  private javax.swing.JTextField weight;
  // End of variables declaration//GEN-END:variables
}