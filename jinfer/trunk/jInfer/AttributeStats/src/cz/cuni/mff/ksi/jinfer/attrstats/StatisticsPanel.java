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

import cz.cuni.mff.ksi.jinfer.attrstats.glpk.GlpkInputGenerator;
import cz.cuni.mff.ksi.jinfer.attrstats.glpk.GlpkRunner;
import cz.cuni.mff.ksi.jinfer.attrstats.glpk.GlpkUtils;
import cz.cuni.mff.ksi.jinfer.attrstats.logic.Algorithm;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AttributeTreeNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

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
  }

  public void setModel(final List<Element> grammar) {
    model = new AMModel(grammar);
    tableView.setModel(model);
    nodeTree.setModel(new DefaultTreeModel(model.getTree()));
  }

  @SuppressWarnings({"unchecked", "PMD"})
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    tabbedPane = new javax.swing.JTabbedPane();
    chartView = new javax.swing.JPanel();
    splitPaneChart = new javax.swing.JSplitPane();
    nodeTreePane = new javax.swing.JScrollPane();
    nodeTree = new javax.swing.JTree();
    jFreeChartPlaceholder = new javax.swing.JPanel();
    labelPlaceholder = new javax.swing.JLabel();
    tableView = new cz.cuni.mff.ksi.jinfer.attrstats.TableViewPanel();
    idSet = new javax.swing.JPanel();
    panelArticle = new javax.swing.JPanel();
    run = new javax.swing.JButton();
    listPane = new javax.swing.JScrollPane();
    list = new javax.swing.JList();
    glpk = new javax.swing.JPanel();
    panelGlpk = new javax.swing.JPanel();
    checkGlpk = new javax.swing.JButton();
    generateInput = new javax.swing.JButton();
    glpkInputPane = new javax.swing.JScrollPane();
    glpkInput = new javax.swing.JTextArea();
    runGlpk = new javax.swing.JButton();

    setLayout(new java.awt.GridBagLayout());

    chartView.setLayout(new java.awt.GridBagLayout());

    splitPaneChart.setDividerLocation(200);

    nodeTreePane.setMinimumSize(new java.awt.Dimension(200, 23));
    nodeTreePane.setPreferredSize(new java.awt.Dimension(200, 322));

    nodeTree.setRootVisible(false);
    nodeTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
      public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
        nodeTreeValueChanged(evt);
      }
    });
    nodeTreePane.setViewportView(nodeTree);

    splitPaneChart.setLeftComponent(nodeTreePane);

    jFreeChartPlaceholder.setLayout(new java.awt.GridBagLayout());

    labelPlaceholder.setFont(labelPlaceholder.getFont().deriveFont(labelPlaceholder.getFont().getSize()+19f));
    labelPlaceholder.setForeground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
    labelPlaceholder.setText(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.labelPlaceholder.text")); // NOI18N
    jFreeChartPlaceholder.add(labelPlaceholder, new java.awt.GridBagConstraints());

    splitPaneChart.setRightComponent(jFreeChartPlaceholder);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    chartView.add(splitPaneChart, gridBagConstraints);

    tabbedPane.addTab(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.chartView.TabConstraints.tabTitle"), chartView); // NOI18N
    tabbedPane.addTab("Table View", tableView);

    idSet.setLayout(new java.awt.GridBagLayout());

    panelArticle.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.panelArticle.border.title"))); // NOI18N
    panelArticle.setLayout(new java.awt.GridBagLayout());

    run.setText("Run the algorithm"); // NOI18N
    run.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        runActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelArticle.add(run, gridBagConstraints);

    listPane.setViewportView(list);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelArticle.add(listPane, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    idSet.add(panelArticle, gridBagConstraints);

    tabbedPane.addTab(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.idSet.TabConstraints.tabTitle"), idSet); // NOI18N

    glpk.setLayout(new java.awt.GridBagLayout());

    panelGlpk.setBorder(javax.swing.BorderFactory.createTitledBorder("ID set via GLPK MIP optimization")); // NOI18N
    panelGlpk.setLayout(new java.awt.GridBagLayout());

    checkGlpk.setText(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.checkGlpk.text")); // NOI18N
    checkGlpk.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        checkGlpkActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelGlpk.add(checkGlpk, gridBagConstraints);

    generateInput.setText(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.generateInput.text")); // NOI18N
    generateInput.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        generateInputActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelGlpk.add(generateInput, gridBagConstraints);

    glpkInput.setColumns(20);
    glpkInput.setRows(5);
    glpkInputPane.setViewportView(glpkInput);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelGlpk.add(glpkInputPane, gridBagConstraints);

    runGlpk.setText(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.runGlpk.text")); // NOI18N
    runGlpk.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        runGlpkActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelGlpk.add(runGlpk, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    glpk.add(panelGlpk, gridBagConstraints);

    tabbedPane.addTab(org.openide.util.NbBundle.getMessage(StatisticsPanel.class, "StatisticsPanel.glpk.TabConstraints.tabTitle_1"), glpk); // NOI18N

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(tabbedPane, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void nodeTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_nodeTreeValueChanged
    if (nodeTree.getSelectionPaths() == null || nodeTree.getSelectionPaths().length < 1) {
      splitPaneChart.setRightComponent(jFreeChartPlaceholder);
      return;
    }
    final List<AttributeTreeNode> atns = new ArrayList<AttributeTreeNode>();
    for (final TreePath tp : nodeTree.getSelectionPaths()) {
      if (tp.getLastPathComponent() instanceof AttributeTreeNode) {
        atns.add((AttributeTreeNode) tp.getLastPathComponent());
      }
    }
    if (BaseUtils.isEmpty(atns)) {
      splitPaneChart.setRightComponent(jFreeChartPlaceholder);
      return;
    }

    splitPaneChart.setRightComponent(JFCWrapper.createGraphPanel(atns));

    tableView.selectInTable(atns);
  }//GEN-LAST:event_nodeTreeValueChanged

  private void runActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runActionPerformed
    // TODO vektor Parameters should be customizable
    final List<AttributeMappingId> ids = Algorithm.findIDSet(model);
    // TODO vektor Less ugly please
    list.setModel(new DefaultComboBoxModel(new Vector<AttributeMappingId>(ids)));
  }//GEN-LAST:event_runActionPerformed

  private void generateInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateInputActionPerformed
    glpkInput.setText(GlpkInputGenerator.generateGlpkInput(model));
  }//GEN-LAST:event_generateInputActionPerformed

  private void runGlpkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runGlpkActionPerformed
    final boolean glpkOk = GlpkUtils.isBinaryValid();
    if (!glpkOk)  {
      DialogDisplayer.getDefault().notify(
            new NotifyDescriptor.Message("There is a problem with GLPK binary.",
            NotifyDescriptor.ERROR_MESSAGE));
      return;
    }
    glpkInput.setText(GlpkRunner.run(model));
  }//GEN-LAST:event_runGlpkActionPerformed

  private void checkGlpkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkGlpkActionPerformed
    final boolean glpkOk = GlpkUtils.isBinaryValid();
    DialogDisplayer.getDefault().notify(
            new NotifyDescriptor.Message(glpkOk ? "GLPK binary is OK." : "There is a problem with GLPK binary.",
            glpkOk ? NotifyDescriptor.INFORMATION_MESSAGE : NotifyDescriptor.ERROR_MESSAGE));
  }//GEN-LAST:event_checkGlpkActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel chartView;
  private javax.swing.JButton checkGlpk;
  private javax.swing.JButton generateInput;
  private javax.swing.JPanel glpk;
  private javax.swing.JTextArea glpkInput;
  private javax.swing.JScrollPane glpkInputPane;
  private javax.swing.JPanel idSet;
  private javax.swing.JPanel jFreeChartPlaceholder;
  private javax.swing.JLabel labelPlaceholder;
  private javax.swing.JList list;
  private javax.swing.JScrollPane listPane;
  private javax.swing.JTree nodeTree;
  private javax.swing.JScrollPane nodeTreePane;
  private javax.swing.JPanel panelArticle;
  private javax.swing.JPanel panelGlpk;
  private javax.swing.JButton run;
  private javax.swing.JButton runGlpk;
  private javax.swing.JSplitPane splitPaneChart;
  private javax.swing.JTabbedPane tabbedPane;
  private cz.cuni.mff.ksi.jinfer.attrstats.TableViewPanel tableView;
  // End of variables declaration//GEN-END:variables
}
