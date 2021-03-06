/*
 *  Copyright (C) 2010 rio
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
package cz.cuni.mff.ksi.jinfer.basicxsd.properties;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import java.util.Properties;

/**
 * Properties panel of the XSD Exporter.
 *
 * @author rio
 */
@SuppressWarnings("PMD.SingularField")
public final class XSDExportPropertiesPanel extends AbstractPropertiesPanel {

  private static final long serialVersionUID = 35787687;

  public static final String NAME = "basicxsdexporter";
  public static final String GENERATE_GLOBAL_PROP = "generate.global";
  public final static boolean GENERATE_GLOBAL_DEFAULT = true;
  public static final String NUMBER_TO_GLOBAL_PROP = "number.to.global";
  /// Default value of number of occurrences of element to consider it as a global type.
  public final static int NUMBER_TO_GLOBAL_DEFAULT = 1;
  public static final String SPACES_PER_INDENT_PROP = "spaces.per.indent";
  public final static int SPACES_PER_INDENT_DEFAULT = 2;
  public static final String TYPENAME_PREFIX_PROP = "typename.prefix";
  public final static String TYPENAME_PREFIX_DEFAULT = "T";
  public static final String TYPENAME_POSTFIX_PROP = "typename.postfix";
  public final static String TYPENAME_POSTFIX_DEFAULT = "";

  /** Creates new form XSDExportPropertiesPanel */
  public XSDExportPropertiesPanel(final Properties properties) {
    super(properties);
    initComponents();
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings({"unchecked", "PMD"})
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    generateGlobalTypes = new javax.swing.JCheckBox();
    countToGlobal = new javax.swing.JSpinner();
    jLabel1 = new javax.swing.JLabel();
    spacesPerIndent = new javax.swing.JSpinner();
    jLabel2 = new javax.swing.JLabel();
    typenamePrefix = new javax.swing.JTextField();
    jLabel3 = new javax.swing.JLabel();
    typenamePostfix = new javax.swing.JTextField();
    jLabel4 = new javax.swing.JLabel();
    jPanel1 = new javax.swing.JPanel();
    jPanel2 = new javax.swing.JPanel();
    jLabel5 = new javax.swing.JLabel();

    setMinimumSize(new java.awt.Dimension(500, 300));
    setPreferredSize(new java.awt.Dimension(500, 300));
    setLayout(new java.awt.GridBagLayout());

    generateGlobalTypes.setText(org.openide.util.NbBundle.getMessage(XSDExportPropertiesPanel.class, "XSDExportPropertiesPanel.generateGlobalTypes.text_1")); // NOI18N
    generateGlobalTypes.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        generateGlobalTypesItemStateChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(generateGlobalTypes, gridBagConstraints);

    countToGlobal.setModel(new javax.swing.SpinnerNumberModel(1, 1, 99, 1));
    countToGlobal.setMaximumSize(new java.awt.Dimension(500, 20));
    countToGlobal.setMinimumSize(new java.awt.Dimension(40, 20));
    countToGlobal.setPreferredSize(new java.awt.Dimension(60, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(countToGlobal, gridBagConstraints);

    jLabel1.setText(org.openide.util.NbBundle.getMessage(XSDExportPropertiesPanel.class, "XSDExportPropertiesPanel.jLabel1.text_1")); // NOI18N
    jLabel1.setToolTipText(org.openide.util.NbBundle.getMessage(XSDExportPropertiesPanel.class, "XSDExportPropertiesPanel.jLabel1.toolTipText")); // NOI18N
    jLabel1.setMinimumSize(new java.awt.Dimension(150, 14));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel1, gridBagConstraints);

    spacesPerIndent.setModel(new javax.swing.SpinnerNumberModel(0, 0, 99, 1));
    spacesPerIndent.setMaximumSize(new java.awt.Dimension(500, 20));
    spacesPerIndent.setMinimumSize(new java.awt.Dimension(40, 20));
    spacesPerIndent.setPreferredSize(new java.awt.Dimension(60, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(spacesPerIndent, gridBagConstraints);

    jLabel2.setText(org.openide.util.NbBundle.getMessage(XSDExportPropertiesPanel.class, "XSDExportPropertiesPanel.jLabel2.text_1")); // NOI18N
    jLabel2.setToolTipText(org.openide.util.NbBundle.getMessage(XSDExportPropertiesPanel.class, "XSDExportPropertiesPanel.jLabel2.toolTipText")); // NOI18N
    jLabel2.setMinimumSize(new java.awt.Dimension(150, 14));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel2, gridBagConstraints);

    typenamePrefix.setText(org.openide.util.NbBundle.getMessage(XSDExportPropertiesPanel.class, "XSDExportPropertiesPanel.typenamePrefix.text")); // NOI18N
    typenamePrefix.setMaximumSize(new java.awt.Dimension(500, 20));
    typenamePrefix.setMinimumSize(new java.awt.Dimension(40, 20));
    typenamePrefix.setPreferredSize(new java.awt.Dimension(60, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.insets = new java.awt.Insets(12, 2, 2, 2);
    add(typenamePrefix, gridBagConstraints);

    jLabel3.setText(org.openide.util.NbBundle.getMessage(XSDExportPropertiesPanel.class, "XSDExportPropertiesPanel.jLabel3.text_1")); // NOI18N
    jLabel3.setToolTipText(org.openide.util.NbBundle.getMessage(XSDExportPropertiesPanel.class, "XSDExportPropertiesPanel.jLabel3.toolTipText")); // NOI18N
    jLabel3.setMinimumSize(new java.awt.Dimension(150, 14));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(12, 12, 2, 12);
    add(jLabel3, gridBagConstraints);

    typenamePostfix.setText(org.openide.util.NbBundle.getMessage(XSDExportPropertiesPanel.class, "XSDExportPropertiesPanel.typenamePostfix.text")); // NOI18N
    typenamePostfix.setMaximumSize(new java.awt.Dimension(500, 20));
    typenamePostfix.setMinimumSize(new java.awt.Dimension(40, 20));
    typenamePostfix.setPreferredSize(new java.awt.Dimension(60, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(typenamePostfix, gridBagConstraints);

    jLabel4.setText(org.openide.util.NbBundle.getMessage(XSDExportPropertiesPanel.class, "XSDExportPropertiesPanel.jLabel4.text_1")); // NOI18N
    jLabel4.setToolTipText(org.openide.util.NbBundle.getMessage(XSDExportPropertiesPanel.class, "XSDExportPropertiesPanel.jLabel4.toolTipText")); // NOI18N
    jLabel4.setMinimumSize(new java.awt.Dimension(150, 14));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel4, gridBagConstraints);

    jPanel1.setPreferredSize(new java.awt.Dimension(10, 80));

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 84, Short.MAX_VALUE)
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 133, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridheight = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    add(jPanel1, gridBagConstraints);

    jPanel2.setPreferredSize(new java.awt.Dimension(300, 50));

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 500, Short.MAX_VALUE)
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 167, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.gridwidth = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weighty = 1.0;
    add(jPanel2, gridBagConstraints);

    jLabel5.setText(org.openide.util.NbBundle.getMessage(XSDExportPropertiesPanel.class, "XSDExportPropertiesPanel.jLabel5.text")); // NOI18N
    jLabel5.setToolTipText(org.openide.util.NbBundle.getMessage(XSDExportPropertiesPanel.class, "XSDExportPropertiesPanel.jLabel5.toolTipText")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel5, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  @SuppressWarnings("PMD")
  private void generateGlobalTypesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_generateGlobalTypesItemStateChanged
    final boolean enabled = generateGlobalTypes.isSelected();
    countToGlobal.setEnabled(enabled);
    typenamePrefix.setEnabled(enabled);
    typenamePostfix.setEnabled(enabled);
  }//GEN-LAST:event_generateGlobalTypesItemStateChanged

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JSpinner countToGlobal;
  private javax.swing.JCheckBox generateGlobalTypes;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JSpinner spacesPerIndent;
  private javax.swing.JTextField typenamePostfix;
  private javax.swing.JTextField typenamePrefix;
  // End of variables declaration//GEN-END:variables

  @Override
  public void load() {
    generateGlobalTypes.setSelected(Boolean.parseBoolean(properties
            .getProperty(GENERATE_GLOBAL_PROP, String.valueOf(GENERATE_GLOBAL_DEFAULT))));
    countToGlobal.setValue(Integer.valueOf(properties
            .getProperty(NUMBER_TO_GLOBAL_PROP, String.valueOf(NUMBER_TO_GLOBAL_DEFAULT))));
    spacesPerIndent.setValue(Integer.valueOf(properties
            .getProperty(SPACES_PER_INDENT_PROP, String.valueOf(SPACES_PER_INDENT_DEFAULT))));
    typenamePrefix.setText(properties
            .getProperty(TYPENAME_PREFIX_PROP,TYPENAME_PREFIX_DEFAULT));
    typenamePostfix.setText(properties
            .getProperty(TYPENAME_POSTFIX_PROP, TYPENAME_POSTFIX_DEFAULT));
    generateGlobalTypesItemStateChanged(null);
  }

  @Override
  public void store() {
    properties.setProperty(GENERATE_GLOBAL_PROP,
            Boolean.toString(generateGlobalTypes.isSelected()));
    properties.setProperty(NUMBER_TO_GLOBAL_PROP,
            ((Integer) countToGlobal.getValue()).toString());
    properties.setProperty(SPACES_PER_INDENT_PROP,
            ((Integer) spacesPerIndent.getValue()).toString());
    properties.setProperty(TYPENAME_PREFIX_PROP,
            typenamePrefix.getText());
    properties.setProperty(TYPENAME_POSTFIX_PROP,
            typenamePostfix.getText());
  }
}
