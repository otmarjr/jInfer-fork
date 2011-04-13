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

/*
 * TODO vektor Comment!
 * 
 * @author vektor
 */
package cz.cuni.mff.ksi.jinfer.attrstats;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author vektor
 */
public class StatisticsPanel extends JPanel {
  
  private static final long serialVersionUID = 5415245241L;  

  public StatisticsPanel() {
    initComponents();
    
    table.setModel(new MyTableModel());
  }
  
  public void setModel(final List<Element> grammar) {
    // TODO vektor
  }
  
  private class MyTableModel extends DefaultTableModel {
    
    private static final long serialVersionUID = 78974631624L;

    @Override
    public int getColumnCount() {
      return 3;
    }

    @Override
    public String getColumnName(final int column) {
      switch (column) {
        case 0:
          return "Element";
        case 1:
          return "Attribute";
        case 2:
          return "Value";
        default:
          throw new IllegalArgumentException("Unkown column: " + column);
      }
    }

  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    tabbedPane = new javax.swing.JTabbedPane();
    graphicalView = new javax.swing.JPanel();
    splitPane = new javax.swing.JSplitPane();
    nodeTreePane = new javax.swing.JScrollPane();
    nodeTree = new javax.swing.JTree();
    jFreeChartPlaceholder = new javax.swing.JPanel();
    labelPlaceholder = new javax.swing.JLabel();
    tableView = new javax.swing.JPanel();
    tablePane = new javax.swing.JScrollPane();
    table = new javax.swing.JTable();

    setLayout(new java.awt.GridBagLayout());

    graphicalView.setLayout(new java.awt.GridBagLayout());

    splitPane.setDividerLocation(200);

    nodeTreePane.setViewportView(nodeTree);

    splitPane.setLeftComponent(nodeTreePane);

    labelPlaceholder.setText(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.labelPlaceholder.text")); // NOI18N

    javax.swing.GroupLayout jFreeChartPlaceholderLayout = new javax.swing.GroupLayout(jFreeChartPlaceholder);
    jFreeChartPlaceholder.setLayout(jFreeChartPlaceholderLayout);
    jFreeChartPlaceholderLayout.setHorizontalGroup(
      jFreeChartPlaceholderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jFreeChartPlaceholderLayout.createSequentialGroup()
        .addGap(127, 127, 127)
        .addComponent(labelPlaceholder)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jFreeChartPlaceholderLayout.setVerticalGroup(
      jFreeChartPlaceholderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFreeChartPlaceholderLayout.createSequentialGroup()
        .addContainerGap(127, Short.MAX_VALUE)
        .addComponent(labelPlaceholder)
        .addGap(125, 125, 125))
    );

    splitPane.setRightComponent(jFreeChartPlaceholder);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    graphicalView.add(splitPane, gridBagConstraints);

    tabbedPane.addTab(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.graphicalView.TabConstraints.tabTitle"), graphicalView); // NOI18N

    tableView.setLayout(new java.awt.GridBagLayout());

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
    tablePane.setViewportView(table);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    tableView.add(tablePane, gridBagConstraints);

    tabbedPane.addTab(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.tableView.TabConstraints.tabTitle"), tableView); // NOI18N

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    add(tabbedPane, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel graphicalView;
  private javax.swing.JPanel jFreeChartPlaceholder;
  private javax.swing.JLabel labelPlaceholder;
  private javax.swing.JTree nodeTree;
  private javax.swing.JScrollPane nodeTreePane;
  private javax.swing.JSplitPane splitPane;
  private javax.swing.JTabbedPane tabbedPane;
  private javax.swing.JTable table;
  private javax.swing.JScrollPane tablePane;
  private javax.swing.JPanel tableView;
  // End of variables declaration//GEN-END:variables
}
