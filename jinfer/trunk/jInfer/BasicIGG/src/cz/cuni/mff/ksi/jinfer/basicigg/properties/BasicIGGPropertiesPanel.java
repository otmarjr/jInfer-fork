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
package cz.cuni.mff.ksi.jinfer.basicigg.properties;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import java.util.Properties;

/**
 * Properties panel of the Basic IGG module.
 * 
 * @author vektor
 */
@SuppressWarnings("PMD.SingularField")
public class BasicIGGPropertiesPanel extends AbstractPropertiesPanel {

  private static final long serialVersionUID = 18746542132L;

  public static final String NAME = "basicIGG";
  public static final String KEEP_ATTRIBUTES = "keep.attributes";
  public static final String KEEP_SIMPLE_DATA = "keep.simple.data";
  public static final String STOP_ON_ERROR = "stop.on.error";

  /** Creates new form BasicIGGPropertiesPanel */
  public BasicIGGPropertiesPanel(final Properties properties) {
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

    keepAttributeValues = new javax.swing.JCheckBox();
    keepSimpleData = new javax.swing.JCheckBox();
    stopOnError = new javax.swing.JCheckBox();
    fill = new javax.swing.JPanel();

    setLayout(new java.awt.GridBagLayout());

    keepAttributeValues.setSelected(true);
    keepAttributeValues.setText(org.openide.util.NbBundle.getMessage(BasicIGGPropertiesPanel.class, "BasicIGGPropertiesPanel.keepAttributeValues.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(keepAttributeValues, gridBagConstraints);

    keepSimpleData.setSelected(true);
    keepSimpleData.setText(org.openide.util.NbBundle.getMessage(BasicIGGPropertiesPanel.class, "BasicIGGPropertiesPanel.keepSimpleData.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(keepSimpleData, gridBagConstraints);

    stopOnError.setSelected(true);
    stopOnError.setText(org.openide.util.NbBundle.getMessage(BasicIGGPropertiesPanel.class, "BasicIGGPropertiesPanel.stopOnError.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(stopOnError, gridBagConstraints);

    javax.swing.GroupLayout fillLayout = new javax.swing.GroupLayout(fill);
    fill.setLayout(fillLayout);
    fillLayout.setHorizontalGroup(
      fillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 400, Short.MAX_VALUE)
    );
    fillLayout.setVerticalGroup(
      fillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 219, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    add(fill, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  @Override
  public void store() {
    properties.setProperty(KEEP_ATTRIBUTES,
            Boolean.toString(keepAttributeValues.isSelected()));
    properties.setProperty(KEEP_SIMPLE_DATA,
            Boolean.toString(keepSimpleData.isSelected()));
    properties.setProperty(STOP_ON_ERROR,
            Boolean.toString(stopOnError.isSelected()));
  }

  @Override
  public void load() {
    keepAttributeValues.setSelected(Boolean.parseBoolean(properties
            .getProperty(KEEP_ATTRIBUTES, "true")));
    keepSimpleData.setSelected(Boolean.parseBoolean(properties
            .getProperty(KEEP_SIMPLE_DATA, "true")));
    stopOnError.setSelected(Boolean.parseBoolean(properties
            .getProperty(STOP_ON_ERROR, "true")));
  }
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel fill;
  private javax.swing.JCheckBox keepAttributeValues;
  private javax.swing.JCheckBox keepSimpleData;
  private javax.swing.JCheckBox stopOnError;
  // End of variables declaration//GEN-END:variables
}
