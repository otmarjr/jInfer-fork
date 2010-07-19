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
package cz.cuni.mff.ksi.jinfer.modularsimplifier.options;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import java.util.Properties;

public final class ModularSimplifierPropertiesPanel extends AbstractPropertiesPanel{

  private static final long serialVersionUID = 561241l;

  public ModularSimplifierPropertiesPanel(final Properties properties) {
    super(properties);
    initComponents();
    load();
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

    setLayout(new java.awt.GridBagLayout());

    org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(ModularSimplifierPropertiesPanel.class, "ModularSimplifierPropertiesPanel.jLabel1.text")); // NOI18N
    jLabel1.setToolTipText(org.openide.util.NbBundle.getMessage(ModularSimplifierPropertiesPanel.class, "ModularSimplifierPropertiesPanel.jLabel1.toolTipText")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel1, gridBagConstraints);

    kleeneRepetitions.setToolTipText(org.openide.util.NbBundle.getMessage(ModularSimplifierPropertiesPanel.class, "ModularSimplifierPropertiesPanel.kleeneRepetitions.toolTipText")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(kleeneRepetitions, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(context, org.openide.util.NbBundle.getMessage(ModularSimplifierPropertiesPanel.class, "ModularSimplifierPropertiesPanel.context.text")); // NOI18N
    context.setToolTipText(org.openide.util.NbBundle.getMessage(ModularSimplifierPropertiesPanel.class, "ModularSimplifierPropertiesPanel.context.toolTipText")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(context, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(ModularSimplifierPropertiesPanel.class, "ModularSimplifierPropertiesPanel.jLabel2.text")); // NOI18N
    jLabel2.setToolTipText(org.openide.util.NbBundle.getMessage(ModularSimplifierPropertiesPanel.class, "ModularSimplifierPropertiesPanel.jLabel2.toolTipText")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel2, gridBagConstraints);

    clusterProcessor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Trie", "Alternations" }));
    clusterProcessor.setToolTipText(org.openide.util.NbBundle.getMessage(ModularSimplifierPropertiesPanel.class, "ModularSimplifierPropertiesPanel.clusterProcessor.toolTipText")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(clusterProcessor, gridBagConstraints);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 907, Short.MAX_VALUE)
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 7, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weighty = 1.0;
    add(jPanel1, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(enabled, org.openide.util.NbBundle.getMessage(ModularSimplifierPropertiesPanel.class, "ModularSimplifierPropertiesPanel.enabled.text")); // NOI18N
    enabled.setToolTipText(org.openide.util.NbBundle.getMessage(ModularSimplifierPropertiesPanel.class, "ModularSimplifierPropertiesPanel.enabled.toolTipText")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(enabled, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(render, org.openide.util.NbBundle.getMessage(ModularSimplifierPropertiesPanel.class, "ModularSimplifierPropertiesPanel.render.text")); // NOI18N
    render.setToolTipText(org.openide.util.NbBundle.getMessage(ModularSimplifierPropertiesPanel.class, "ModularSimplifierPropertiesPanel.render.toolTipText")); // NOI18N
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
      .addGap(0, 308, Short.MAX_VALUE)
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 138, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridheight = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    add(jPanel2, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  @Override
  public void load() {
    enabled.setSelected(Boolean.parseBoolean(properties.getProperty("modularsimplifier.enabled", "true")));
    context.setSelected(Boolean.parseBoolean(properties.getProperty("modularsimplifier.use.context", "false")));
    render.setSelected(Boolean.parseBoolean(properties.getProperty("modularsimplifier.render", "true")));
    kleeneRepetitions.setValue(Integer.valueOf(properties.getProperty("modularsimplifier.kleene.repetitions", "3")));
    clusterProcessor.setSelectedItem(properties.getProperty("modularsimplifier.cluster.processor", "Trie"));
  }

  @Override
  public void store() {
    properties.setProperty("modularsimplifier.enabled", Boolean.toString(enabled.isSelected()));
    properties.setProperty("modularsimplifier.use.context", Boolean.toString(context.isSelected()));
    properties.setProperty("modularsimplifier.render", Boolean.toString(render.isSelected()));
    properties.setProperty("modularsimplifier.kleene.repetitions",  ((Integer)kleeneRepetitions.getValue()).toString());
    properties.setProperty("modularsimplifier.cluster.processor", (String) clusterProcessor.getSelectedItem());
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JComboBox clusterProcessor;
  private javax.swing.JCheckBox context;
  private javax.swing.JCheckBox enabled;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JSpinner kleeneRepetitions;
  private javax.swing.JCheckBox render;
  // End of variables declaration//GEN-END:variables
}
