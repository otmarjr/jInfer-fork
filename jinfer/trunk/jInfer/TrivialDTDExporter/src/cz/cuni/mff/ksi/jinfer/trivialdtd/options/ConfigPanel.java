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
package cz.cuni.mff.ksi.jinfer.trivialdtd.options;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.prefs.Preferences;
import javax.swing.JPanel;

public final class ConfigPanel extends JPanel {

  private static final long serialVersionUID = 5421231l;

  private final ConfigOptionsPanelController controller;

  private final NumberFormat nf = NumberFormat.getInstance();

  public ConfigPanel(final ConfigOptionsPanelController controller) {
    super();
    this.controller = controller;
    initComponents();
    // TODO listen to changes in form fields and call controller.changed()
  }

  @SuppressWarnings("PMD")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    jLabel1 = new javax.swing.JLabel();
    maxEnumSize = new javax.swing.JSpinner();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    minDefaultRatio = new javax.swing.JFormattedTextField();
    jLabel4 = new javax.swing.JLabel();
    jPanel1 = new javax.swing.JPanel();

    setLayout(new java.awt.GridBagLayout());

    org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(ConfigPanel.class, "ConfigPanel.jLabel1.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel1, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(maxEnumSize, gridBagConstraints);

    jLabel2.setFont(jLabel2.getFont().deriveFont((jLabel2.getFont().getStyle() | java.awt.Font.ITALIC)));
    org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(ConfigPanel.class, "ConfigPanel.jLabel2.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 10;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 2);
    add(jLabel2, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(ConfigPanel.class, "ConfigPanel.jLabel3.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel3, gridBagConstraints);

    minDefaultRatio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    minDefaultRatio.setText(org.openide.util.NbBundle.getMessage(ConfigPanel.class, "ConfigPanel.minDefaultRatio.text")); // NOI18N
    minDefaultRatio.setMinimumSize(new java.awt.Dimension(50, 20));
    minDefaultRatio.setPreferredSize(new java.awt.Dimension(50, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(minDefaultRatio, gridBagConstraints);

    jLabel4.setFont(jLabel4.getFont().deriveFont((jLabel4.getFont().getStyle() | java.awt.Font.ITALIC)));
    org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(ConfigPanel.class, "ConfigPanel.jLabel4.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 10;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 2);
    add(jLabel4, gridBagConstraints);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 645, Short.MAX_VALUE)
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 90, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weighty = 1.0;
    add(jPanel1, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  public void load() {
    maxEnumSize.setValue(Preferences.userNodeForPackage(ConfigPanel.class).getInt("max.enum.size", 3));
    minDefaultRatio.setValue(
            nf.format(
            Preferences.userNodeForPackage(ConfigPanel.class).getFloat("min.default.ratio", 0.67f)));
  }

  public void store() {
    Preferences.userNodeForPackage(ConfigPanel.class).putInt("max.enum.size", ((Integer) maxEnumSize.getValue()).intValue());
    try {
      Preferences.userNodeForPackage(ConfigPanel.class).putFloat("min.default.ratio", nf.parse(minDefaultRatio.getText()).floatValue());
    } catch (final ParseException ex) {
    }
  }

  public boolean valid() {
    // TODO check whether form is consistent and complete
    return true;
  }
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JSpinner maxEnumSize;
  private javax.swing.JFormattedTextField minDefaultRatio;
  // End of variables declaration//GEN-END:variables
}
