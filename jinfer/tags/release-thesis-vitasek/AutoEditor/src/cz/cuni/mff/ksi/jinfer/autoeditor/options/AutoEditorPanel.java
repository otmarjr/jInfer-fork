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
package cz.cuni.mff.ksi.jinfer.autoeditor.options;

import cz.cuni.mff.ksi.jinfer.base.objects.VertexShape;
import java.awt.Color;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import org.openide.util.NbPreferences;

/**
 * Options panel of the AutoEditor module.
 * @author sviro
 */
@SuppressWarnings("PMD.SingularField")
public final class AutoEditorPanel extends JPanel {

  private static final long serialVersionUID = 9454121L;

  public AutoEditorPanel() {
    super();
    initComponents();
  }

  @SuppressWarnings("PMD")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    backgroundColorLabel = new javax.swing.JLabel();
    backgroundColor = new javax.swing.JPanel();
    regularStateColorLabel = new javax.swing.JLabel();
    regularStateColor = new javax.swing.JPanel();
    fill2 = new javax.swing.JPanel();
    fill1 = new javax.swing.JPanel();
    resetToDefaults = new javax.swing.JButton();
    finalStateLabel = new javax.swing.JLabel();
    finalStateColor = new javax.swing.JPanel();
    pickedStateColorLabel = new javax.swing.JLabel();
    pickedStateColor = new javax.swing.JPanel();
    superinitialStateShape = new javax.swing.JComboBox();
    superfinalStateShape = new javax.swing.JComboBox();
    regularStateShape = new javax.swing.JComboBox();
    superInitialStateShapeLabel = new javax.swing.JLabel();
    superfinalStateShapeLabel = new javax.swing.JLabel();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();

    setLayout(new java.awt.GridBagLayout());

    org.openide.awt.Mnemonics.setLocalizedText(backgroundColorLabel, org.openide.util.NbBundle.getMessage(AutoEditorPanel.class, "AutoEditorPanel.backgroundColorLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(backgroundColorLabel, gridBagConstraints);

    backgroundColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    backgroundColor.setMaximumSize(new java.awt.Dimension(20, 20));
    backgroundColor.setMinimumSize(new java.awt.Dimension(20, 20));
    backgroundColor.setPreferredSize(new java.awt.Dimension(20, 20));
    backgroundColor.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        colorMouseClicked(evt);
      }
    });

    javax.swing.GroupLayout backgroundColorLayout = new javax.swing.GroupLayout(backgroundColor);
    backgroundColor.setLayout(backgroundColorLayout);
    backgroundColorLayout.setHorizontalGroup(
      backgroundColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 18, Short.MAX_VALUE)
    );
    backgroundColorLayout.setVerticalGroup(
      backgroundColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 18, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(backgroundColor, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(regularStateColorLabel, org.openide.util.NbBundle.getMessage(AutoEditorPanel.class, "AutoEditorPanel.regularStateColorLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(regularStateColorLabel, gridBagConstraints);

    regularStateColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    regularStateColor.setMaximumSize(new java.awt.Dimension(20, 20));
    regularStateColor.setMinimumSize(new java.awt.Dimension(20, 20));
    regularStateColor.setPreferredSize(new java.awt.Dimension(20, 20));
    regularStateColor.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        colorMouseClicked(evt);
      }
    });

    javax.swing.GroupLayout regularStateColorLayout = new javax.swing.GroupLayout(regularStateColor);
    regularStateColor.setLayout(regularStateColorLayout);
    regularStateColorLayout.setHorizontalGroup(
      regularStateColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 18, Short.MAX_VALUE)
    );
    regularStateColorLayout.setVerticalGroup(
      regularStateColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 18, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(regularStateColor, gridBagConstraints);

    fill2.setPreferredSize(new java.awt.Dimension(0, 0));
    fill2.setLayout(null);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.gridwidth = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weighty = 1.0;
    add(fill2, gridBagConstraints);

    fill1.setPreferredSize(new java.awt.Dimension(0, 0));
    fill1.setLayout(null);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 4;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridheight = 7;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    add(fill1, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(resetToDefaults, org.openide.util.NbBundle.getMessage(AutoEditorPanel.class, "AutoEditorPanel.resetToDefaults.text")); // NOI18N
    resetToDefaults.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        resetToDefaultsActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(resetToDefaults, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(finalStateLabel, org.openide.util.NbBundle.getMessage(AutoEditorPanel.class, "AutoEditorPanel.finalStateLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(finalStateLabel, gridBagConstraints);

    finalStateColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    finalStateColor.setMaximumSize(new java.awt.Dimension(20, 20));
    finalStateColor.setMinimumSize(new java.awt.Dimension(20, 20));
    finalStateColor.setPreferredSize(new java.awt.Dimension(20, 20));
    finalStateColor.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        colorMouseClicked(evt);
      }
    });

    javax.swing.GroupLayout finalStateColorLayout = new javax.swing.GroupLayout(finalStateColor);
    finalStateColor.setLayout(finalStateColorLayout);
    finalStateColorLayout.setHorizontalGroup(
      finalStateColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 18, Short.MAX_VALUE)
    );
    finalStateColorLayout.setVerticalGroup(
      finalStateColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 18, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(finalStateColor, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(pickedStateColorLabel, org.openide.util.NbBundle.getMessage(AutoEditorPanel.class, "AutoEditorPanel.pickedStateColorLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(pickedStateColorLabel, gridBagConstraints);

    pickedStateColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    pickedStateColor.setMaximumSize(new java.awt.Dimension(20, 20));
    pickedStateColor.setMinimumSize(new java.awt.Dimension(20, 20));
    pickedStateColor.setPreferredSize(new java.awt.Dimension(20, 20));
    pickedStateColor.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        colorMouseClicked(evt);
      }
    });

    javax.swing.GroupLayout pickedStateColorLayout = new javax.swing.GroupLayout(pickedStateColor);
    pickedStateColor.setLayout(pickedStateColorLayout);
    pickedStateColorLayout.setHorizontalGroup(
      pickedStateColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 18, Short.MAX_VALUE)
    );
    pickedStateColorLayout.setVerticalGroup(
      pickedStateColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 18, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(pickedStateColor, gridBagConstraints);

    superinitialStateShape.setModel(new javax.swing.DefaultComboBoxModel(VertexShape.values()));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(superinitialStateShape, gridBagConstraints);

    superfinalStateShape.setModel(new javax.swing.DefaultComboBoxModel(VertexShape.values()));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(superfinalStateShape, gridBagConstraints);

    regularStateShape.setModel(new javax.swing.DefaultComboBoxModel(VertexShape.values()));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(regularStateShape, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(superInitialStateShapeLabel, org.openide.util.NbBundle.getMessage(AutoEditorPanel.class, "AutoEditorPanel.superInitialStateShapeLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(superInitialStateShapeLabel, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(superfinalStateShapeLabel, org.openide.util.NbBundle.getMessage(AutoEditorPanel.class, "AutoEditorPanel.superfinalStateShapeLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(superfinalStateShapeLabel, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(AutoEditorPanel.class, "AutoEditorPanel.jLabel1.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(jLabel1, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(AutoEditorPanel.class, "AutoEditorPanel.jLabel2.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(jLabel2, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  @SuppressWarnings("PMD.MethodArgumentCouldBeFinal")
  private void colorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorMouseClicked
    final Object source = evt.getSource();
    if (source instanceof JPanel) {
      final JPanel panel = (JPanel) source;
      final Color c = JColorChooser.showDialog(AutoEditorPanel.this, "Choose the color", panel.getBackground());
      if (c != null) {
        panel.setBackground(c);
      }
    }
  }//GEN-LAST:event_colorMouseClicked

  @SuppressWarnings({"PMD.MethodArgumentCouldBeFinal", "PMD.UnusedFormalParameter"})
  private void resetToDefaultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetToDefaultsActionPerformed
    backgroundColor.setBackground(ColorUtils.BG_COLOR_DEFAULT);
    regularStateColor.setBackground(ColorUtils.NODE_COLOR_DEFAULT);
    finalStateColor.setBackground(ColorUtils.FINAL_COLOR_DEFAULT);
    pickedStateColor.setBackground(ColorUtils.PICKED_COLOR_DEFAULT);
  }//GEN-LAST:event_resetToDefaultsActionPerformed

  /**
   * Load values for all fields from properties. If no value for particular field is saved
   * in properties, default value is used.
   */
  public void load() {
    backgroundColor.setBackground(ColorUtils.getColorProperty(ColorUtils.BG_COLOR_PROP, ColorUtils.BG_COLOR_DEFAULT));
    regularStateColor.setBackground(ColorUtils.getColorProperty(ColorUtils.NODE_COLOR_PROP, ColorUtils.NODE_COLOR_DEFAULT));
    finalStateColor.setBackground(ColorUtils.getColorProperty(ColorUtils.FINAL_COLOR_PROP, ColorUtils.FINAL_COLOR_DEFAULT));
    pickedStateColor.setBackground(ColorUtils.getColorProperty(ColorUtils.PICKED_COLOR_PROP, ColorUtils.PICKED_COLOR_DEFAULT));
    superinitialStateShape.setSelectedItem(ShapeUtils.getShapeProperty(ShapeUtils.SUPERINITIAL_NODE_SHAPE_PROP, ShapeUtils.SUPERINITIAL_NODE_SHAPE_DEFAULT));
    superfinalStateShape.setSelectedItem(ShapeUtils.getShapeProperty(ShapeUtils.SUPERFINAL_NODE_SHAPE_PROP, ShapeUtils.SUPERFINAL_NODE_SHAPE_DEFAULT));
    regularStateShape.setSelectedItem(ShapeUtils.getShapeProperty(ShapeUtils.REGULAR_NODE_SHAPE_PROP, ShapeUtils.REGULAR_NODE_SHAPE_DEFAULT));
  }

  /**
   * Save all values in fields into properties.
   */
  public void store() {
    NbPreferences.forModule(AutoEditorPanel.class).put(ColorUtils.BG_COLOR_PROP, String.valueOf(backgroundColor.getBackground().getRGB()));
    NbPreferences.forModule(AutoEditorPanel.class).put(ColorUtils.NODE_COLOR_PROP, String.valueOf(regularStateColor.getBackground().getRGB()));
    NbPreferences.forModule(AutoEditorPanel.class).put(ColorUtils.FINAL_COLOR_PROP, String.valueOf(finalStateColor.getBackground().getRGB()));
    NbPreferences.forModule(AutoEditorPanel.class).put(ColorUtils.PICKED_COLOR_PROP, String.valueOf(pickedStateColor.getBackground().getRGB()));
    NbPreferences.forModule(AutoEditorPanel.class).put(ShapeUtils.SUPERINITIAL_NODE_SHAPE_PROP, superinitialStateShape.getSelectedItem().toString());
    NbPreferences.forModule(AutoEditorPanel.class).put(ShapeUtils.SUPERFINAL_NODE_SHAPE_PROP, superfinalStateShape.getSelectedItem().toString());
    NbPreferences.forModule(AutoEditorPanel.class).put(ShapeUtils.REGULAR_NODE_SHAPE_PROP, regularStateShape.getSelectedItem().toString());
  }

  /**
   * Check if values entered in fields are valid. In this panel is no check for validity.
   * @return <tt>true</tt>.
   */
  public boolean valid() {
    return true;
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel backgroundColor;
  private javax.swing.JLabel backgroundColorLabel;
  private javax.swing.JPanel fill1;
  private javax.swing.JPanel fill2;
  private javax.swing.JPanel finalStateColor;
  private javax.swing.JLabel finalStateLabel;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JPanel pickedStateColor;
  private javax.swing.JLabel pickedStateColorLabel;
  private javax.swing.JPanel regularStateColor;
  private javax.swing.JLabel regularStateColorLabel;
  private javax.swing.JComboBox regularStateShape;
  private javax.swing.JButton resetToDefaults;
  private javax.swing.JLabel superInitialStateShapeLabel;
  private javax.swing.JComboBox superfinalStateShape;
  private javax.swing.JLabel superfinalStateShapeLabel;
  private javax.swing.JComboBox superinitialStateShape;
  // End of variables declaration//GEN-END:variables
}
