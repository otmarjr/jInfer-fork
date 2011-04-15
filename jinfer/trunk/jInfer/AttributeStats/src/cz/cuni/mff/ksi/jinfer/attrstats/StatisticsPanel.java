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

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.tree.DefaultTreeModel;

/*
 * Panel representing and displaying one complete attribute statistics.
 *
 * @author vektor
 */
public class StatisticsPanel extends JPanel {

  private static final long serialVersionUID = 5415245241L;

  public StatisticsPanel() {
    initComponents();
  }

  public void setModel(final List<Element> grammar) {
    table.setModel(new MyTableModel(MappingExtractor.extractFlat(grammar)));
    nodeTree.setModel(new DefaultTreeModel(MappingExtractor.createTree(grammar)));
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

    nodeTree.setRootVisible(false);
    nodeTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
      public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
        nodeTreeValueChanged(evt);
      }
    });
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

    table.setAutoCreateRowSorter(true);
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
    table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
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

  private void nodeTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_nodeTreeValueChanged
    final Object o = evt.getNewLeadSelectionPath().getLastPathComponent();
    if (o instanceof AttributeTreeNode) {
      final AttributeTreeNode atn = (AttributeTreeNode) o;
      splitPane.setRightComponent(JFCWrapper.createGraphPanel(
              atn.getElementName() + "@" + atn.getAttributeName(), atn.getContent()));
    }
  }//GEN-LAST:event_nodeTreeValueChanged

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
