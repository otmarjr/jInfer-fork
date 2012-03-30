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
package cz.cuni.mff.ksi.jinfer.iss.gui;

import cz.cuni.mff.ksi.jinfer.iss.options.ISSPanel;
import org.openide.util.NbPreferences;
import org.netbeans.api.options.OptionsDisplayer;
import cz.cuni.mff.ksi.jinfer.iss.objects.IdSet;
import cz.cuni.mff.ksi.jinfer.iss.JFCWrapper;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk.GlpkInputGenerator;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk.GlpkOutputParser;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk.GlpkRunner;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk.GlpkUtils;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.fidax.FidaxAlgorithm;
import cz.cuni.mff.ksi.jinfer.iss.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.iss.objects.AttributeTreeNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import static cz.cuni.mff.ksi.jinfer.base.utils.AsynchronousUtils.runAsync;

/**
 * Panel representing and displaying one complete attribute statistics.
 *
 * @author vektor
 */
@SuppressWarnings({"PMD.SingularField", "PMD.UnusedFormalParameter",
  "PMD.MethodArgumentCouldBeFinal", "PMD.TooManyFields", "PMD.TooManyMethods"})
public class StatisticsPanel extends JPanel {

  private static final long serialVersionUID = 5415245241L;

  private static final Logger LOG = Logger.getLogger(StatisticsPanel.class);

  private AMModel model;

  public StatisticsPanel() {
    super();
    initComponents();
  }

  public void setModel(final List<Element> grammar) {
    model = new AMModel(grammar);
    tableView.setModel(model);
    nodeTree.setModel(new DefaultTreeModel(model.getTree()));
  }

  private void notAnIdSet() {
    DialogDisplayer.getDefault().notify(
            new NotifyDescriptor.Message("Oops! This is not an ID set.",
            NotifyDescriptor.ERROR_MESSAGE));
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
    tableView = new cz.cuni.mff.ksi.jinfer.iss.gui.TableViewPanel();
    idSet = new javax.swing.JPanel();
    panelArticle = new javax.swing.JPanel();
    run = new javax.swing.JButton();
    idSetArticle = new cz.cuni.mff.ksi.jinfer.iss.gui.IdSetPanel();
    jButton1 = new javax.swing.JButton();
    glpk = new javax.swing.JPanel();
    panelGlpk = new javax.swing.JPanel();
    generateInput = new javax.swing.JButton();
    runGlpk = new javax.swing.JButton();
    split = new javax.swing.JSplitPane();
    glpkInputPane = new javax.swing.JScrollPane();
    glpkInput = new javax.swing.JTextArea();
    idSetGlpk = new cz.cuni.mff.ksi.jinfer.iss.gui.IdSetPanel();
    settings = new javax.swing.JButton();

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
    labelPlaceholder.setText("< Select an attribute to see its content"); // NOI18N
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

    tabbedPane.addTab("Chart View", chartView);
    tabbedPane.addTab("Table View", tableView);

    idSet.setLayout(new java.awt.GridBagLayout());

    panelArticle.setBorder(javax.swing.BorderFactory.createTitledBorder("Article \"Finding ID Attributes in XML Documents\""));
    panelArticle.setLayout(new java.awt.GridBagLayout());

    run.setText("Run \"the algorithm\""); // NOI18N
    run.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        runActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelArticle.add(run, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    panelArticle.add(idSetArticle, gridBagConstraints);

    jButton1.setText("Settings..."); // NOI18N
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelArticle.add(jButton1, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    idSet.add(panelArticle, gridBagConstraints);

    tabbedPane.addTab("ID set - Article", idSet);

    glpk.setLayout(new java.awt.GridBagLayout());

    panelGlpk.setBorder(javax.swing.BorderFactory.createTitledBorder("ID set via GLPK MIP optimization"));
    panelGlpk.setLayout(new java.awt.GridBagLayout());

    generateInput.setText("Just Generate Input"); // NOI18N
    generateInput.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        generateInputActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelGlpk.add(generateInput, gridBagConstraints);

    runGlpk.setText("Run GLPK"); // NOI18N
    runGlpk.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        runGlpkActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelGlpk.add(runGlpk, gridBagConstraints);

    split.setDividerLocation(250);
    split.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
    split.setPreferredSize(new java.awt.Dimension(300, 200));

    glpkInput.setColumns(20);
    glpkInput.setEditable(false);
    glpkInput.setFont(new java.awt.Font("Courier New", 0, 12));
    glpkInput.setRows(5);
    glpkInputPane.setViewportView(glpkInput);

    split.setRightComponent(glpkInputPane);
    split.setLeftComponent(idSetGlpk);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelGlpk.add(split, gridBagConstraints);

    settings.setText("GLPK settings..."); // NOI18N
    settings.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        settingsActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelGlpk.add(settings, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    glpk.add(panelGlpk, gridBagConstraints);

    tabbedPane.addTab("ID set - GLPK", glpk);

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
    final double alpha = NbPreferences.forModule(ISSPanel.class).getFloat(ISSPanel.ALPHA_PROP, ISSPanel.ALPHA_DEFAULT);
    final double beta = NbPreferences.forModule(ISSPanel.class).getFloat(ISSPanel.BETA_PROP, ISSPanel.BETA_DEFAULT);

    final IdSet resultingSet = FidaxAlgorithm.findIDSet(model, alpha, beta);
    if (!resultingSet.isValid(model)) {
      notAnIdSet();
    }
    idSetArticle.setModel(resultingSet, model, alpha, beta);
  }//GEN-LAST:event_runActionPerformed

  private void generateInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateInputActionPerformed
    runAsync(new Runnable() {

      @Override
      public void run() {
        try {
          final double alpha = NbPreferences.forModule(ISSPanel.class).getFloat(ISSPanel.ALPHA_PROP, ISSPanel.ALPHA_DEFAULT);
          final double beta = NbPreferences.forModule(ISSPanel.class).getFloat(ISSPanel.BETA_PROP, ISSPanel.BETA_DEFAULT);
          glpkInput.setText(GlpkInputGenerator.generateGlpkInput(model, alpha, beta));
        }
        catch (final IOException e) {
          LOG.error("There was a problem creating GLPK input.", e);
        }
        catch (final InterruptedException e) {
          LOG.error("User interrupted GLPK input generation.");
        }
      }
    }, "Generating GLPK input");
  }//GEN-LAST:event_generateInputActionPerformed

  private void runGlpkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runGlpkActionPerformed
    final boolean glpkOk = GlpkUtils.isBinaryValid();
    if (!glpkOk)  {
      DialogDisplayer.getDefault().notify(
            new NotifyDescriptor.Message("There is a problem with GLPK binary.",
            NotifyDescriptor.ERROR_MESSAGE));
      return;
    }
    runAsync(new Runnable() {

      @Override
      public void run() {
        try {
          final double alpha = NbPreferences.forModule(ISSPanel.class).getFloat(ISSPanel.ALPHA_PROP, ISSPanel.ALPHA_DEFAULT);
          final double beta = NbPreferences.forModule(ISSPanel.class).getFloat(ISSPanel.BETA_PROP, ISSPanel.BETA_DEFAULT);
          final int timeLimit = NbPreferences.forModule(ISSPanel.class).getInt(ISSPanel.TIME_LIMIT_PROP, ISSPanel.TIME_LIMIT_DEFAULT);
          final String glpkOut = GlpkRunner.run(model, alpha, beta, timeLimit);
          glpkInput.setText(glpkOut);

          final IdSet idSet = GlpkOutputParser.getIDSet(glpkOut, model);
          if (!idSet.isValid(model)) {
            notAnIdSet();
          }
          idSetGlpk.setModel(idSet, model, alpha, beta);
        } catch (final InterruptedException e) {
          LOG.error("User interrupted GLPK run.");
        }
      }
    }, "Running GLPK");
  }//GEN-LAST:event_runGlpkActionPerformed

  private void settingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsActionPerformed
    OptionsDisplayer.getDefault().open("jInfer/IDSetSearch");
  }//GEN-LAST:event_settingsActionPerformed

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    OptionsDisplayer.getDefault().open("jInfer/IDSetSearch");
  }//GEN-LAST:event_jButton1ActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel chartView;
  private javax.swing.JButton generateInput;
  private javax.swing.JPanel glpk;
  private javax.swing.JTextArea glpkInput;
  private javax.swing.JScrollPane glpkInputPane;
  private javax.swing.JPanel idSet;
  private cz.cuni.mff.ksi.jinfer.iss.gui.IdSetPanel idSetArticle;
  private cz.cuni.mff.ksi.jinfer.iss.gui.IdSetPanel idSetGlpk;
  private javax.swing.JButton jButton1;
  private javax.swing.JPanel jFreeChartPlaceholder;
  private javax.swing.JLabel labelPlaceholder;
  private javax.swing.JTree nodeTree;
  private javax.swing.JScrollPane nodeTreePane;
  private javax.swing.JPanel panelArticle;
  private javax.swing.JPanel panelGlpk;
  private javax.swing.JButton run;
  private javax.swing.JButton runGlpk;
  private javax.swing.JButton settings;
  private javax.swing.JSplitPane split;
  private javax.swing.JSplitPane splitPaneChart;
  private javax.swing.JTabbedPane tabbedPane;
  private cz.cuni.mff.ksi.jinfer.iss.gui.TableViewPanel tableView;
  // End of variables declaration//GEN-END:variables
}
