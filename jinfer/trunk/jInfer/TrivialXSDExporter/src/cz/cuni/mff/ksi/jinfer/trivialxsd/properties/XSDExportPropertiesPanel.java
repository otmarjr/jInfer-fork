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

/*
 * XSDExportPropertiesPanel.java
 *
 * Created on Aug 13, 2010, 10:49:44 PM
 */
package cz.cuni.mff.ksi.jinfer.trivialxsd.properties;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.trivialxsd.SchemaGeneratorImpl;
import java.util.Properties;

/**
 *
 * @author rio
 */
public final class XSDExportPropertiesPanel extends AbstractPropertiesPanel {

  /** Creates new form XSDExportPropertiesPanel */
  public XSDExportPropertiesPanel(final Properties properties) {
    super(properties);
    initComponents();
    load();
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
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

    setLayout(new java.awt.GridBagLayout());

    generateGlobalTypes.setText(org.openide.util.NbBundle.getMessage(XSDExportPropertiesPanel.class, "XSDExportPropertiesPanel.generateGlobalTypes.text")); // NOI18N
    generateGlobalTypes.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        generateGlobalTypesStateChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(generateGlobalTypes, gridBagConstraints);

    countToGlobal.setModel(new javax.swing.SpinnerNumberModel(1, 1, 99, 1));
    countToGlobal.setMinimumSize(new java.awt.Dimension(60, 20));
    countToGlobal.setPreferredSize(new java.awt.Dimension(150, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(countToGlobal, gridBagConstraints);

    jLabel1.setText(org.openide.util.NbBundle.getMessage(XSDExportPropertiesPanel.class, "XSDExportPropertiesPanel.jLabel1.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel1, gridBagConstraints);

    spacesPerIndent.setModel(new javax.swing.SpinnerNumberModel(0, 0, 99, 1));
    spacesPerIndent.setMinimumSize(new java.awt.Dimension(60, 20));
    spacesPerIndent.setPreferredSize(new java.awt.Dimension(150, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(spacesPerIndent, gridBagConstraints);

    jLabel2.setText(org.openide.util.NbBundle.getMessage(XSDExportPropertiesPanel.class, "XSDExportPropertiesPanel.jLabel2.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel2, gridBagConstraints);

    typenamePrefix.setText(org.openide.util.NbBundle.getMessage(XSDExportPropertiesPanel.class, "XSDExportPropertiesPanel.typenamePrefix.text")); // NOI18N
    typenamePrefix.setMinimumSize(new java.awt.Dimension(60, 20));
    typenamePrefix.setPreferredSize(new java.awt.Dimension(150, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(typenamePrefix, gridBagConstraints);

    jLabel3.setText(org.openide.util.NbBundle.getMessage(XSDExportPropertiesPanel.class, "XSDExportPropertiesPanel.jLabel3.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel3, gridBagConstraints);

    typenamePostfix.setText(org.openide.util.NbBundle.getMessage(XSDExportPropertiesPanel.class, "XSDExportPropertiesPanel.typenamePostfix.text")); // NOI18N
    typenamePostfix.setMinimumSize(new java.awt.Dimension(60, 20));
    typenamePostfix.setPreferredSize(new java.awt.Dimension(150, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(typenamePostfix, gridBagConstraints);

    jLabel4.setText(org.openide.util.NbBundle.getMessage(XSDExportPropertiesPanel.class, "XSDExportPropertiesPanel.jLabel4.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel4, gridBagConstraints);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 124, Short.MAX_VALUE)
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 123, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridheight = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    add(jPanel1, gridBagConstraints);

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 546, Short.MAX_VALUE)
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 147, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weighty = 1.0;
    add(jPanel2, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void generateGlobalTypesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_generateGlobalTypesStateChanged
    final boolean enabled = generateGlobalTypes.isSelected();
    countToGlobal.setEnabled(enabled);
    typenamePrefix.setEnabled(enabled);
    typenamePostfix.setEnabled(enabled);
  }//GEN-LAST:event_generateGlobalTypesStateChanged

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JSpinner countToGlobal;
  private javax.swing.JCheckBox generateGlobalTypes;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JSpinner spacesPerIndent;
  private javax.swing.JTextField typenamePostfix;
  private javax.swing.JTextField typenamePrefix;
  // End of variables declaration//GEN-END:variables

  @Override
  public void load() {
    // TODO rio tooltips
    generateGlobalTypes.setSelected(Boolean.parseBoolean(properties
            .getProperty(SchemaGeneratorImpl.TRIVIAL_XSD_EXPORTER_GENERATE_GLOBAL, String.valueOf(SchemaGeneratorImpl.GENERATE_GLOBAL_DEFAULT))));
    countToGlobal.setValue(Integer.valueOf(properties
            .getProperty(SchemaGeneratorImpl.TRIVIAL_XSD_EXPORTER_NUMBER_TO_GLOBAL, String.valueOf(SchemaGeneratorImpl.NUMBER_TO_GLOBAL_DEFAULT))));
    spacesPerIndent.setValue(Integer.valueOf(properties
            .getProperty(SchemaGeneratorImpl.TRIVIAL_XSD_EXPORTER_SPACES_PER_INDENT, String.valueOf(SchemaGeneratorImpl.SPACES_PER_INDENT_DEFAULT))));
    typenamePrefix.setText(properties
            .getProperty(SchemaGeneratorImpl.TRIVIAL_XSD_EXPORTER_TYPENAME_PREFIX, SchemaGeneratorImpl.TYPENAME_PREFIX_DEFAULT));
    typenamePostfix.setText(properties
            .getProperty(SchemaGeneratorImpl.TRIVIAL_XSD_EXPORTER_TYPENAME_POSTFIX, SchemaGeneratorImpl.TYPENAME_POSTFIX_DEFAULT));
  }

  @Override
  public void store() {
    properties.setProperty(SchemaGeneratorImpl.TRIVIAL_XSD_EXPORTER_GENERATE_GLOBAL,
            Boolean.toString(generateGlobalTypes.isSelected()));
    properties.setProperty(SchemaGeneratorImpl.TRIVIAL_XSD_EXPORTER_NUMBER_TO_GLOBAL,
            ((Integer) countToGlobal.getValue()).toString());
    properties.setProperty(SchemaGeneratorImpl.TRIVIAL_XSD_EXPORTER_SPACES_PER_INDENT,
            ((Integer) spacesPerIndent.getValue()).toString());
    properties.setProperty(SchemaGeneratorImpl.TRIVIAL_XSD_EXPORTER_TYPENAME_PREFIX,
            typenamePrefix.getText());
    properties.setProperty(SchemaGeneratorImpl.TRIVIAL_XSD_EXPORTER_TYPENAME_POSTFIX,
            typenamePostfix.getText());
  }
}
