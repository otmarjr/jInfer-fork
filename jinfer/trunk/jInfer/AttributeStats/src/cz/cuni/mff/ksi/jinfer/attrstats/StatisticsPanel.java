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

import cz.cuni.mff.ksi.jinfer.attrstats.logic.MappingUtils;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AttributeTreeNode;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.Triplet;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * Panel representing and displaying one complete attribute statistics.
 *
 * @author vektor
 */
public class StatisticsPanel extends JPanel {

  private static final long serialVersionUID = 5415245241L;

  private final Map<AttributeMappingId, Pair<Double, Double>> cache =
          new HashMap<AttributeMappingId, Pair<Double, Double>>();

  private AMModel model;

  public StatisticsPanel() {
    initComponents();
    table.setSelectionModel(new DefaultListSelectionModel());
  }

  public void setModel(final List<Element> grammar) {
    model = new AMModel(grammar);
    table.setModel(new MyTableModel(model.getFlat()));
    nodeTree.setModel(new DefaultTreeModel(model.getTree()));

    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

      @Override
      public void valueChanged(ListSelectionEvent e) {
        computeStats();
      }
    });
  }

  private void selectInTable(final List<AttributeTreeNode> nodes) {
    final DefaultListSelectionModel selectionModel = (DefaultListSelectionModel)table.getSelectionModel();
    selectionModel.clearSelection();
    final TableModel m = table.getModel();

    for (int i = 0; i < m.getRowCount(); i++) {
      final int row = table.convertRowIndexToModel(i);
      for (final AttributeTreeNode atn : nodes) {
        if (atn.getElementName().equals(m.getValueAt(row, 0))
                && atn.getAttributeName().equals(m.getValueAt(row, 1))
                && atn.getContent().contains((String)m.getValueAt(row, 2))) {
          selectionModel.addSelectionInterval(i, i);
          break;
        }
      }
    }
  }

  private void computeStats() {
    if (table.getSelectedRowCount() == 1) {
      final MyTableModel tableModel = (MyTableModel)table.getModel();
      final int row = table.convertRowIndexToModel(table.getSelectedRow());
      showStats(new AttributeMappingId((String) tableModel.getValueAt(row, 0), (String) tableModel.getValueAt(row, 1)));
    }
    else {
      support.setText("N/A");
      coverage.setText("N/A");
    }
  }

  private void showStats(final AttributeMappingId targetMapping) {
    final Pair<Double, Double> value;
    if (cache.containsKey(targetMapping)) {
      value = cache.get(targetMapping);
    }
    else {
      value = new Pair<Double, Double>(
              Double.valueOf(MappingUtils.support(targetMapping, model)),
              Double.valueOf(MappingUtils.coverage(targetMapping, model)));
      cache.put(targetMapping, value);
    }
    support.setText(value.getFirst().toString());
    coverage.setText(value.getSecond().toString());
  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    tabbedPane = new javax.swing.JTabbedPane();
    chartView = new javax.swing.JPanel();
    splitPane = new javax.swing.JSplitPane();
    nodeTreePane = new javax.swing.JScrollPane();
    nodeTree = new javax.swing.JTree();
    jFreeChartPlaceholder = new javax.swing.JPanel();
    labelPlaceholder = new javax.swing.JLabel();
    tableView = new javax.swing.JPanel();
    tablePane = new javax.swing.JScrollPane();
    table = new javax.swing.JTable();
    stats = new javax.swing.JPanel();
    labelSupport = new javax.swing.JLabel();
    support = new javax.swing.JLabel();
    labelCoverage = new javax.swing.JLabel();
    coverage = new javax.swing.JLabel();
    misc = new javax.swing.JPanel();

    setLayout(new java.awt.GridBagLayout());

    chartView.setLayout(new java.awt.GridBagLayout());

    splitPane.setDividerLocation(200);

    nodeTreePane.setMinimumSize(new java.awt.Dimension(200, 23));
    nodeTreePane.setPreferredSize(new java.awt.Dimension(200, 322));

    nodeTree.setPreferredSize(null);
    nodeTree.setRootVisible(false);
    nodeTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
      public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
        nodeTreeValueChanged(evt);
      }
    });
    nodeTreePane.setViewportView(nodeTree);

    splitPane.setLeftComponent(nodeTreePane);

    jFreeChartPlaceholder.setLayout(new java.awt.GridBagLayout());

    labelPlaceholder.setFont(labelPlaceholder.getFont().deriveFont(labelPlaceholder.getFont().getSize()+19f));
    labelPlaceholder.setForeground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
    labelPlaceholder.setText(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.labelPlaceholder.text")); // NOI18N
    jFreeChartPlaceholder.add(labelPlaceholder, new java.awt.GridBagConstraints());

    splitPane.setRightComponent(jFreeChartPlaceholder);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    chartView.add(splitPane, gridBagConstraints);

    tabbedPane.addTab(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.chartView.TabConstraints.tabTitle"), chartView); // NOI18N

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
    tablePane.setViewportView(table);
    table.getColumnModel().getColumn(0).setHeaderValue(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.table.columnModel.title0")); // NOI18N
    table.getColumnModel().getColumn(1).setHeaderValue(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.table.columnModel.title1")); // NOI18N
    table.getColumnModel().getColumn(2).setHeaderValue(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.table.columnModel.title2")); // NOI18N
    table.getColumnModel().getColumn(3).setHeaderValue(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.table.columnModel.title3")); // NOI18N

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    tableView.add(tablePane, gridBagConstraints);

    stats.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.stats.border.title"))); // NOI18N
    stats.setLayout(new java.awt.GridBagLayout());

    labelSupport.setFont(labelSupport.getFont().deriveFont(labelSupport.getFont().getStyle() | java.awt.Font.BOLD));
    labelSupport.setText(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.labelSupport.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 2);
    stats.add(labelSupport, gridBagConstraints);

    support.setText(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.support.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    stats.add(support, gridBagConstraints);

    labelCoverage.setFont(labelCoverage.getFont().deriveFont(labelCoverage.getFont().getStyle() | java.awt.Font.BOLD));
    labelCoverage.setText(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.labelCoverage.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 2);
    stats.add(labelCoverage, gridBagConstraints);

    coverage.setText(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.coverage.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    stats.add(coverage, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    tableView.add(stats, gridBagConstraints);

    tabbedPane.addTab(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.tableView.TabConstraints.tabTitle"), tableView); // NOI18N
    tabbedPane.addTab(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.misc.TabConstraints.tabTitle"), misc); // NOI18N

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(tabbedPane, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void nodeTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_nodeTreeValueChanged
    if (nodeTree.getSelectionPaths() == null || nodeTree.getSelectionPaths().length < 1) {
      splitPane.setRightComponent(jFreeChartPlaceholder);
      return;
    }
    final List<AttributeTreeNode> atns = new ArrayList<AttributeTreeNode>();
    for (final TreePath tp : nodeTree.getSelectionPaths()) {
      if (tp.getLastPathComponent() instanceof AttributeTreeNode) {
        atns.add((AttributeTreeNode) tp.getLastPathComponent());
      }
    }
    if (BaseUtils.isEmpty(atns)) {
      splitPane.setRightComponent(jFreeChartPlaceholder);
      return;
    }

    splitPane.setRightComponent(JFCWrapper.createGraphPanel(atns));

    selectInTable(atns);
  }//GEN-LAST:event_nodeTreeValueChanged

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel chartView;
  private javax.swing.JLabel coverage;
  private javax.swing.JPanel jFreeChartPlaceholder;
  private javax.swing.JLabel labelCoverage;
  private javax.swing.JLabel labelPlaceholder;
  private javax.swing.JLabel labelSupport;
  private javax.swing.JPanel misc;
  private javax.swing.JTree nodeTree;
  private javax.swing.JScrollPane nodeTreePane;
  private javax.swing.JSplitPane splitPane;
  private javax.swing.JPanel stats;
  private javax.swing.JLabel support;
  private javax.swing.JTabbedPane tabbedPane;
  private javax.swing.JTable table;
  private javax.swing.JScrollPane tablePane;
  private javax.swing.JPanel tableView;
  // End of variables declaration//GEN-END:variables
}
