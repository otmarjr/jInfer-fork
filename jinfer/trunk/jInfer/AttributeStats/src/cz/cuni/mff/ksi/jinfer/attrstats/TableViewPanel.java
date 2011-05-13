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

package cz.cuni.mff.ksi.jinfer.attrstats;

import javax.swing.JPanel;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public class TableViewPanel extends JPanel {

  private static final long serialVersionUID = 18443L;

  public TableViewPanel() {
    initComponents();
  }

  @SuppressWarnings({"unchecked", "PMD"})
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    splitHorizontal = new javax.swing.JSplitPane();
    panelFlat = new javax.swing.JPanel();
    paneFlat = new javax.swing.JScrollPane();
    tableFlat = new javax.swing.JTable();
    panelMappings = new javax.swing.JPanel();
    splitVertical = new javax.swing.JSplitPane();
    panelValues = new javax.swing.JPanel();
    paneValues = new javax.swing.JScrollPane();
    listValues = new javax.swing.JList();
    panelMappingsTable = new javax.swing.JPanel();
    paneMappings = new javax.swing.JScrollPane();
    tableMappings = new javax.swing.JTable();
    labelSupport = new javax.swing.JLabel();
    support = new javax.swing.JLabel();
    labelCoverage = new javax.swing.JLabel();
    coverage = new javax.swing.JLabel();
    labelIDset = new javax.swing.JLabel();
    idSet = new javax.swing.JLabel();

    setLayout(new java.awt.BorderLayout());

    splitHorizontal.setDividerLocation(150);
    splitHorizontal.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
    splitHorizontal.setContinuousLayout(true);

    panelFlat.setBorder(javax.swing.BorderFactory.createTitledBorder("Flat View")); // NOI18N
    panelFlat.setLayout(new java.awt.BorderLayout());

    tableFlat.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {
        {null, null, null},
        {null, null, null},
        {null, null, null},
        {null, null, null}
      },
      new String [] {
        "Element", "Attribute", "Value"
      }
    ) {
      Class[] types = new Class [] {
        java.lang.String.class, java.lang.String.class, java.lang.String.class
      };

      public Class getColumnClass(int columnIndex) {
        return types [columnIndex];
      }
    });
    paneFlat.setViewportView(tableFlat);

    panelFlat.add(paneFlat, java.awt.BorderLayout.CENTER);

    splitHorizontal.setTopComponent(panelFlat);

    panelMappings.setBorder(javax.swing.BorderFactory.createTitledBorder("Attribute Mappings")); // NOI18N
    panelMappings.setLayout(new java.awt.BorderLayout());

    splitVertical.setDividerLocation(250);

    panelValues.setBorder(javax.swing.BorderFactory.createTitledBorder("Image")); // NOI18N
    panelValues.setPreferredSize(new java.awt.Dimension(0, 0));
    panelValues.setLayout(new java.awt.GridBagLayout());

    paneValues.setViewportView(listValues);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelValues.add(paneValues, gridBagConstraints);

    splitVertical.setLeftComponent(panelValues);

    panelMappingsTable.setMinimumSize(new java.awt.Dimension(0, 0));
    panelMappingsTable.setPreferredSize(new java.awt.Dimension(0, 0));
    panelMappingsTable.setLayout(new java.awt.GridBagLayout());

    tableMappings.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {
        {null, null},
        {null, null},
        {null, null},
        {null, null}
      },
      new String [] {
        "Element", "Attribute"
      }
    ) {
      Class[] types = new Class [] {
        java.lang.String.class, java.lang.String.class
      };
      boolean[] canEdit = new boolean [] {
        false, false
      };

      public Class getColumnClass(int columnIndex) {
        return types [columnIndex];
      }

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
      }
    });
    paneMappings.setViewportView(tableMappings);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 7;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelMappingsTable.add(paneMappings, gridBagConstraints);

    labelSupport.setFont(labelSupport.getFont().deriveFont(labelSupport.getFont().getStyle() | java.awt.Font.BOLD));
    labelSupport.setText("Support:"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 2);
    panelMappingsTable.add(labelSupport, gridBagConstraints);

    support.setText("N/A");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 12);
    panelMappingsTable.add(support, gridBagConstraints);

    labelCoverage.setFont(labelCoverage.getFont().deriveFont(labelCoverage.getFont().getStyle() | java.awt.Font.BOLD));
    labelCoverage.setText("Coverage:"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelMappingsTable.add(labelCoverage, gridBagConstraints);

    coverage.setText("N/A");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 12);
    panelMappingsTable.add(coverage, gridBagConstraints);

    labelIDset.setFont(labelIDset.getFont().deriveFont(labelIDset.getFont().getStyle() | java.awt.Font.BOLD));
    labelIDset.setText("Feasible ID set:"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 4;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelMappingsTable.add(labelIDset, gridBagConstraints);

    idSet.setText("N/A"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 5;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 12);
    panelMappingsTable.add(idSet, gridBagConstraints);

    splitVertical.setRightComponent(panelMappingsTable);

    panelMappings.add(splitVertical, java.awt.BorderLayout.CENTER);

    splitHorizontal.setRightComponent(panelMappings);

    add(splitHorizontal, java.awt.BorderLayout.CENTER);
  }// </editor-fold>//GEN-END:initComponents
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel coverage;
  private javax.swing.JLabel idSet;
  private javax.swing.JLabel labelCoverage;
  private javax.swing.JLabel labelIDset;
  private javax.swing.JLabel labelSupport;
  private javax.swing.JList listValues;
  private javax.swing.JScrollPane paneFlat;
  private javax.swing.JScrollPane paneMappings;
  private javax.swing.JScrollPane paneValues;
  private javax.swing.JPanel panelFlat;
  private javax.swing.JPanel panelMappings;
  private javax.swing.JPanel panelMappingsTable;
  private javax.swing.JPanel panelValues;
  private javax.swing.JSplitPane splitHorizontal;
  private javax.swing.JSplitPane splitVertical;
  private javax.swing.JLabel support;
  private javax.swing.JTable tableFlat;
  private javax.swing.JTable tableMappings;
  // End of variables declaration//GEN-END:variables
}
