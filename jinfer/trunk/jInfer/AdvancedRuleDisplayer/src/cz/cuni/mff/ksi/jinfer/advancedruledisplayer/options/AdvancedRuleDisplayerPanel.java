/*
 *  Copyright (C) 2011 sviro
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
package cz.cuni.mff.ksi.jinfer.advancedruledisplayer.options;

import cz.cuni.mff.ksi.jinfer.advancedruledisplayer.logic.Utils;
import org.openide.util.NbPreferences;

public final class AdvancedRuleDisplayerPanel extends javax.swing.JPanel {

  private final AdvancedRuleDisplayerOptionsPanelController controller;

  AdvancedRuleDisplayerPanel(AdvancedRuleDisplayerOptionsPanelController controller) {
    this.controller = controller;
    initComponents();
    // TODO listen to changes in form fields and call controller.changed()
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    shape = new javax.swing.JPanel();
    tokenLabel = new javax.swing.JLabel();
    concatLabel = new javax.swing.JLabel();
    alterLabel = new javax.swing.JLabel();
    permutLabel = new javax.swing.JLabel();
    tokenCombo = new javax.swing.JComboBox();
    concatCombo = new javax.swing.JComboBox();
    alterCombo = new javax.swing.JComboBox();
    permutCombo = new javax.swing.JComboBox();
    tokenSpinner = new javax.swing.JSpinner();
    concatSpinner = new javax.swing.JSpinner();
    alterSpinner = new javax.swing.JSpinner();
    permutSpinner = new javax.swing.JSpinner();
    rootLabel = new javax.swing.JLabel();
    rootCombo = new javax.swing.JComboBox();
    rootSpinner = new javax.swing.JSpinner();
    color = new javax.swing.JPanel();
    fill = new javax.swing.JPanel();

    setLayout(new java.awt.GridBagLayout());

    shape.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdvancedRuleDisplayerPanel.class, "AdvancedRuleDisplayerPanel.shape.border.title"))); // NOI18N
    shape.setLayout(new java.awt.GridBagLayout());

    org.openide.awt.Mnemonics.setLocalizedText(tokenLabel, org.openide.util.NbBundle.getMessage(AdvancedRuleDisplayerPanel.class, "AdvancedRuleDisplayerPanel.tokenLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    shape.add(tokenLabel, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(concatLabel, org.openide.util.NbBundle.getMessage(AdvancedRuleDisplayerPanel.class, "AdvancedRuleDisplayerPanel.concatLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    shape.add(concatLabel, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(alterLabel, org.openide.util.NbBundle.getMessage(AdvancedRuleDisplayerPanel.class, "AdvancedRuleDisplayerPanel.alterLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    shape.add(alterLabel, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(permutLabel, org.openide.util.NbBundle.getMessage(AdvancedRuleDisplayerPanel.class, "AdvancedRuleDisplayerPanel.permutLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    shape.add(permutLabel, gridBagConstraints);

    tokenCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Circle", "Square", "Rounded Square", "Polygon", "Star" }));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    shape.add(tokenCombo, gridBagConstraints);

    concatCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Circle", "Square", "Rounded Square", "Polygon", "Star" }));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    shape.add(concatCombo, gridBagConstraints);

    alterCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Circle", "Square", "Rounded Square", "Polygon", "Star" }));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    shape.add(alterCombo, gridBagConstraints);

    permutCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Circle", "Square", "Rounded Square", "Polygon", "Star" }));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    shape.add(permutCombo, gridBagConstraints);

    tokenSpinner.setModel(new javax.swing.SpinnerNumberModel(50, 10, 200, 1));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    shape.add(tokenSpinner, gridBagConstraints);

    concatSpinner.setModel(new javax.swing.SpinnerNumberModel(10, 10, 200, 1));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    shape.add(concatSpinner, gridBagConstraints);

    alterSpinner.setModel(new javax.swing.SpinnerNumberModel(10, 10, 200, 1));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    shape.add(alterSpinner, gridBagConstraints);

    permutSpinner.setModel(new javax.swing.SpinnerNumberModel(10, 10, 200, 1));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    shape.add(permutSpinner, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(rootLabel, org.openide.util.NbBundle.getMessage(AdvancedRuleDisplayerPanel.class, "AdvancedRuleDisplayerPanel.rootLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    shape.add(rootLabel, gridBagConstraints);

    rootCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Circle", "Square", "Rounded Square", "Polygon", "Star" }));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    shape.add(rootCombo, gridBagConstraints);

    rootSpinner.setModel(new javax.swing.SpinnerNumberModel(10, 10, 200, 1));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    shape.add(rootSpinner, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    add(shape, gridBagConstraints);

    color.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdvancedRuleDisplayerPanel.class, "AdvancedRuleDisplayerPanel.color.border.title"))); // NOI18N

    javax.swing.GroupLayout colorLayout = new javax.swing.GroupLayout(color);
    color.setLayout(colorLayout);
    colorLayout.setHorizontalGroup(
      colorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 429, Short.MAX_VALUE)
    );
    colorLayout.setVerticalGroup(
      colorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 0, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    add(color, gridBagConstraints);

    javax.swing.GroupLayout fillLayout = new javax.swing.GroupLayout(fill);
    fill.setLayout(fillLayout);
    fillLayout.setHorizontalGroup(
      fillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 441, Short.MAX_VALUE)
    );
    fillLayout.setVerticalGroup(
      fillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 86, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weighty = 1.0;
    add(fill, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  void load() {
    rootCombo.setSelectedIndex(NbPreferences.forModule(AdvancedRuleDisplayerPanel.class).getInt(Utils.ROOT_SHAPE_PROP, Utils.ROOT_SHAPE_DEFAULT));
    tokenCombo.setSelectedIndex(NbPreferences.forModule(AdvancedRuleDisplayerPanel.class).getInt(Utils.TOKEN_SHAPE_PROP, Utils.TOKEN_SHAPE_DEFAULT));
    concatCombo.setSelectedIndex(NbPreferences.forModule(AdvancedRuleDisplayerPanel.class).getInt(Utils.CONCAT_SHAPE_PROP, Utils.CONCAT_SHAPE_DEFAULT));
    alterCombo.setSelectedIndex(NbPreferences.forModule(AdvancedRuleDisplayerPanel.class).getInt(Utils.ALTER_SHAPE_PROP, Utils.ALTER_SHAPE_DEFAULT));
    permutCombo.setSelectedIndex(NbPreferences.forModule(AdvancedRuleDisplayerPanel.class).getInt(Utils.PERMUT_SHAPE_PROP, Utils.PERMUT_SHAPE_DEFAULT));

    rootSpinner.setValue(NbPreferences.forModule(AdvancedRuleDisplayerPanel.class).getInt(Utils.ROOT_SIZE_PROP, Utils.ROOT_SIZE_DEFAULT));
    tokenSpinner.setValue(NbPreferences.forModule(AdvancedRuleDisplayerPanel.class).getInt(Utils.TOKEN_SIZE_PROP, Utils.TOKEN_SIZE_DEFAULT));
    concatSpinner.setValue(NbPreferences.forModule(AdvancedRuleDisplayerPanel.class).getInt(Utils.CONCAT_SIZE_PROP, Utils.CONCAT_SIZE_DEFAULT));
    alterSpinner.setValue(NbPreferences.forModule(AdvancedRuleDisplayerPanel.class).getInt(Utils.ALTER_SIZE_PROP, Utils.ALTER_SIZE_DEFAULT));
    permutSpinner.setValue(NbPreferences.forModule(AdvancedRuleDisplayerPanel.class).getInt(Utils.PERMUT_SIZE_PROP, Utils.PERMUT_SIZE_DEFAULT));
  }

  void store() {
     NbPreferences.forModule(AdvancedRuleDisplayerPanel.class).putInt(Utils.ROOT_SHAPE_PROP, rootCombo.getSelectedIndex());
     NbPreferences.forModule(AdvancedRuleDisplayerPanel.class).putInt(Utils.TOKEN_SHAPE_PROP, tokenCombo.getSelectedIndex());
     NbPreferences.forModule(AdvancedRuleDisplayerPanel.class).putInt(Utils.CONCAT_SHAPE_PROP, concatCombo.getSelectedIndex());
     NbPreferences.forModule(AdvancedRuleDisplayerPanel.class).putInt(Utils.ALTER_SHAPE_PROP, alterCombo.getSelectedIndex());
     NbPreferences.forModule(AdvancedRuleDisplayerPanel.class).putInt(Utils.PERMUT_SHAPE_PROP, permutCombo.getSelectedIndex());

     NbPreferences.forModule(AdvancedRuleDisplayerPanel.class).putInt(Utils.ROOT_SIZE_PROP,(Integer) rootSpinner.getValue());
     NbPreferences.forModule(AdvancedRuleDisplayerPanel.class).putInt(Utils.TOKEN_SIZE_PROP,(Integer) tokenSpinner.getValue());
     NbPreferences.forModule(AdvancedRuleDisplayerPanel.class).putInt(Utils.CONCAT_SIZE_PROP,(Integer) concatSpinner.getValue());
     NbPreferences.forModule(AdvancedRuleDisplayerPanel.class).putInt(Utils.ALTER_SIZE_PROP,(Integer) alterSpinner.getValue());
     NbPreferences.forModule(AdvancedRuleDisplayerPanel.class).putInt(Utils.PERMUT_SIZE_PROP,(Integer) permutSpinner.getValue());
  }

  boolean valid() {
    // TODO check whether form is consistent and complete
    return true;
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JComboBox alterCombo;
  private javax.swing.JLabel alterLabel;
  private javax.swing.JSpinner alterSpinner;
  private javax.swing.JPanel color;
  private javax.swing.JComboBox concatCombo;
  private javax.swing.JLabel concatLabel;
  private javax.swing.JSpinner concatSpinner;
  private javax.swing.JPanel fill;
  private javax.swing.JComboBox permutCombo;
  private javax.swing.JLabel permutLabel;
  private javax.swing.JSpinner permutSpinner;
  private javax.swing.JComboBox rootCombo;
  private javax.swing.JLabel rootLabel;
  private javax.swing.JSpinner rootSpinner;
  private javax.swing.JPanel shape;
  private javax.swing.JComboBox tokenCombo;
  private javax.swing.JLabel tokenLabel;
  private javax.swing.JSpinner tokenSpinner;
  // End of variables declaration//GEN-END:variables
}
