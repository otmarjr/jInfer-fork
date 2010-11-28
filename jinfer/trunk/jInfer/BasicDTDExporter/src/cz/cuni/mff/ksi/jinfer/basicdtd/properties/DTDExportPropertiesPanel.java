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
package cz.cuni.mff.ksi.jinfer.basicdtd.properties;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.basicdtd.SchemaGeneratorImpl;
import java.util.Properties;

/**
 * Properties panel of the Basic DTD Exporter module.
 * 
 * @author vektor
 */
public final class DTDExportPropertiesPanel extends AbstractPropertiesPanel {

  private static final long serialVersionUID = 5421231l;
  public static final String NAME = "basicdtdexporter";
  public static final String MAX_ENUM_SIZE = "max.enum.size";
  public static final String MIN_DEFAULT_RATIO = "min.default.ratio";
  public static final int MAX_ENUM_SIZE_DEFAULT = 3;
  public static final float MIN_DEFAULT_RATIO_DEFAULT = 0.67f;

  public DTDExportPropertiesPanel(final Properties properties) {
    super(properties);
    initComponents();
  }

  @SuppressWarnings("PMD")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    jLabel1 = new javax.swing.JLabel();
    maxEnumSize = new javax.swing.JSpinner();
    jLabel3 = new javax.swing.JLabel();
    minDefaultRatio = new javax.swing.JFormattedTextField();
    jPanel1 = new javax.swing.JPanel();
    jPanel2 = new javax.swing.JPanel();

    setLayout(new java.awt.GridBagLayout());

    org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(DTDExportPropertiesPanel.class, "DTDExportPropertiesPanel.jLabel1.text")); // NOI18N
    jLabel1.setToolTipText(org.openide.util.NbBundle.getMessage(DTDExportPropertiesPanel.class, "DTDExportPropertiesPanel.jLabel1.toolTipText")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel1, gridBagConstraints);

    maxEnumSize.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
    maxEnumSize.setToolTipText(org.openide.util.NbBundle.getMessage(DTDExportPropertiesPanel.class, "DTDExportPropertiesPanel.maxEnumSize.toolTipText")); // NOI18N
    maxEnumSize.setMinimumSize(new java.awt.Dimension(150, 22));
    maxEnumSize.setPreferredSize(new java.awt.Dimension(150, 22));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(maxEnumSize, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(DTDExportPropertiesPanel.class, "DTDExportPropertiesPanel.jLabel3.text")); // NOI18N
    jLabel3.setToolTipText(org.openide.util.NbBundle.getMessage(DTDExportPropertiesPanel.class, "DTDExportPropertiesPanel.jLabel3.toolTipText")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel3, gridBagConstraints);

    minDefaultRatio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    minDefaultRatio.setText(org.openide.util.NbBundle.getMessage(DTDExportPropertiesPanel.class, "DTDExportPropertiesPanel.minDefaultRatio.text")); // NOI18N
    minDefaultRatio.setToolTipText(org.openide.util.NbBundle.getMessage(DTDExportPropertiesPanel.class, "DTDExportPropertiesPanel.minDefaultRatio.toolTipText")); // NOI18N
    minDefaultRatio.setMinimumSize(new java.awt.Dimension(150, 22));
    minDefaultRatio.setPreferredSize(new java.awt.Dimension(150, 22));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(minDefaultRatio, gridBagConstraints);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 322, Short.MAX_VALUE)
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 114, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weighty = 1.0;
    add(jPanel1, gridBagConstraints);

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 66, Short.MAX_VALUE)
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 52, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    add(jPanel2, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  @Override
  public void load() {
    maxEnumSize.setValue(Integer.valueOf(properties.getProperty(MAX_ENUM_SIZE, Integer.toString(
            MAX_ENUM_SIZE_DEFAULT))));
    minDefaultRatio.setValue(properties.getProperty(MIN_DEFAULT_RATIO,
            Float.toString(MIN_DEFAULT_RATIO_DEFAULT)));
  }

  @Override
  public void store() {
    properties.setProperty(MAX_ENUM_SIZE,
            ((Integer) maxEnumSize.getValue()).toString());
    properties.setProperty(MIN_DEFAULT_RATIO,
            minDefaultRatio.getText());
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JSpinner maxEnumSize;
  private javax.swing.JFormattedTextField minDefaultRatio;
  // End of variables declaration//GEN-END:variables
}
