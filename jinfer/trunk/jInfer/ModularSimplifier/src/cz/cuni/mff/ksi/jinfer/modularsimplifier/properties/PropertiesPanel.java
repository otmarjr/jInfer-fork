/*
 *  Copyright (C) 2010 vektor
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
package cz.cuni.mff.ksi.jinfer.modularsimplifier.properties;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.kleening.KleeneProcessorFactory;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.processing.ClusterProcessorFactory;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;

/**
 * @author vektor
 */
public final class PropertiesPanel extends AbstractPropertiesPanel {

  private static final long serialVersionUID = 561241l;
  public static final String CLUSTER_PROCESSOR = "modularsimplifier.cluster.processor";
  public static final String CLUSTER_PROCESSOR_DEFAULT = "Trie";

  public static final String KLEENE_PROCESSOR = "modularsimplifier.kleene.processor";
  public static final String KLEENE_PROCESSOR_DEFAULT = "Simple Kleene processor";
  public static final String KLEENE_REPETITIONS = "modularsimplifier.kleene.repetitions";

  public static final String ENABLED = "modularsimplifier.enabled";
  
  public static final String RENDER = "modularsimplifier.render";
  public static final String USE_CONTEXT = "modularsimplifier.use.context";
  public static final boolean ENABLED_DEFAULT = true;
  public static final int KLEENE_REPETITIONS_DEFAULT = 3;
  public static final boolean RENDER_DEFAULT = true;
  public static final boolean USE_CONTEXT_DEFAULT = false;

  public PropertiesPanel(final Properties properties) {
    super(properties);
    initComponents();
  }

  @SuppressWarnings("PMD")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    jLabel1 = new javax.swing.JLabel();
    kleeneRepetitions = new javax.swing.JSpinner();
    context = new javax.swing.JCheckBox();
    jLabel2 = new javax.swing.JLabel();
    clusterProcessor = new javax.swing.JComboBox();
    jPanel1 = new javax.swing.JPanel();
    enabled = new javax.swing.JCheckBox();
    render = new javax.swing.JCheckBox();
    jPanel2 = new javax.swing.JPanel();
    jLabel3 = new javax.swing.JLabel();
    kleeneProcessor = new javax.swing.JComboBox();

    setLayout(new java.awt.GridBagLayout());

    org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.jLabel1.text")); // NOI18N
    jLabel1.setToolTipText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.jLabel1.toolTipText")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel1, gridBagConstraints);

    kleeneRepetitions.setToolTipText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.kleeneRepetitions.toolTipText")); // NOI18N
    kleeneRepetitions.setMinimumSize(new java.awt.Dimension(150, 22));
    kleeneRepetitions.setPreferredSize(new java.awt.Dimension(150, 22));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(kleeneRepetitions, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(context, org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.context.text")); // NOI18N
    context.setToolTipText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.context.toolTipText")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(context, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.jLabel2.text")); // NOI18N
    jLabel2.setToolTipText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.jLabel2.toolTipText")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel2, gridBagConstraints);

    clusterProcessor.setToolTipText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.clusterProcessor.toolTipText")); // NOI18N
    clusterProcessor.setMinimumSize(new java.awt.Dimension(150, 22));
    clusterProcessor.setPreferredSize(new java.awt.Dimension(150, 22));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(clusterProcessor, gridBagConstraints);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 267, Short.MAX_VALUE)
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 54, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weighty = 1.0;
    add(jPanel1, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(enabled, org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.enabled.text")); // NOI18N
    enabled.setToolTipText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.enabled.toolTipText")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(enabled, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(render, org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.render.text")); // NOI18N
    render.setToolTipText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.render.toolTipText")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(render, gridBagConstraints);

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 113, Short.MAX_VALUE)
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 149, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridheight = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    add(jPanel2, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.jLabel3.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel3, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(kleeneProcessor, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  @Override
  public void load() {
    enabled.setSelected(Boolean.parseBoolean(properties.getProperty(
            ENABLED, Boolean.toString(ENABLED_DEFAULT))));
    context.setSelected(Boolean.parseBoolean(properties.getProperty(
            USE_CONTEXT, Boolean.toString(USE_CONTEXT_DEFAULT))));
    render.setSelected(Boolean.parseBoolean(properties.getProperty(
            RENDER, Boolean.toString(RENDER_DEFAULT))));
    
    clusterProcessor.setModel(new DefaultComboBoxModel(
            ModuleSelectionHelper.lookupImplementationNames(
            ClusterProcessorFactory.class).toArray()));
    clusterProcessor.setSelectedItem(properties.getProperty(
            CLUSTER_PROCESSOR, CLUSTER_PROCESSOR_DEFAULT));

    kleeneProcessor.setModel(new DefaultComboBoxModel(
            ModuleSelectionHelper.lookupImplementationNames(
            KleeneProcessorFactory.class).toArray()));
    kleeneProcessor.setSelectedItem(properties.getProperty(
            KLEENE_PROCESSOR, KLEENE_PROCESSOR_DEFAULT));
    kleeneRepetitions.setValue(Integer.valueOf(properties.getProperty(
            KLEENE_REPETITIONS, Integer.toString(KLEENE_REPETITIONS_DEFAULT))));
  }

  @Override
  public void store() {
    properties.setProperty(ENABLED,
            Boolean.toString(enabled.isSelected()));
    properties.setProperty(USE_CONTEXT,
            Boolean.toString(context.isSelected()));
    properties.setProperty(RENDER,
            Boolean.toString(render.isSelected()));
    
    properties.setProperty(CLUSTER_PROCESSOR,
            (String) clusterProcessor.getSelectedItem());
    properties.setProperty(KLEENE_PROCESSOR,
            (String) kleeneProcessor.getSelectedItem());
    properties.setProperty(KLEENE_REPETITIONS,
            ((Integer) kleeneRepetitions.getValue()).toString());
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JComboBox clusterProcessor;
  private javax.swing.JCheckBox context;
  private javax.swing.JCheckBox enabled;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JComboBox kleeneProcessor;
  private javax.swing.JSpinner kleeneRepetitions;
  private javax.swing.JCheckBox render;
  // End of variables declaration//GEN-END:variables
}
