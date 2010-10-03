/*
 *  Copyright (C) 2010 sviro
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
package cz.cuni.mff.ksi.jinfer.crudemdl.properties;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.ClustererFactory;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.ClusterProcessorFactory;
import java.util.List;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author sviro
 */
public class CrudeMDLPropertiesPanel extends AbstractPropertiesPanel {

  public static final String NAME = "TwoStepSimplifier";
  public static final String PROPERTIES_CLUSTERER = "clusterer";
  public static final String PROPERTIES_CLUSTER_PROCESSOR = "cluster-processor";
  private static final String DEFAULT_MENU_TEXT = "<none available>";
  private static final long serialVersionUID = 784463431L;

  /** Creates new form ModuleSelectionJPanel */
  public CrudeMDLPropertiesPanel(final Properties properties) {
    super(properties);
    initComponents();
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

    clusterer = new javax.swing.JComboBox();
    clusterProcessor = new javax.swing.JComboBox();
    labelClusterer = new javax.swing.JLabel();
    labelClusterProcessor = new javax.swing.JLabel();
    jPanel1 = new javax.swing.JPanel();
    jPanel2 = new javax.swing.JPanel();

    setLayout(new java.awt.GridBagLayout());

    clusterer.setMinimumSize(new java.awt.Dimension(200, 22));
    clusterer.setPreferredSize(new java.awt.Dimension(200, 22));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 2);
    add(clusterer, gridBagConstraints);

    clusterProcessor.setMinimumSize(new java.awt.Dimension(200, 22));
    clusterProcessor.setPreferredSize(new java.awt.Dimension(200, 22));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 2);
    add(clusterProcessor, gridBagConstraints);

    labelClusterer.setText(org.openide.util.NbBundle.getMessage(CrudeMDLPropertiesPanel.class, "CrudeMDLPropertiesPanel.labelClusterer.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(labelClusterer, gridBagConstraints);

    labelClusterProcessor.setText(org.openide.util.NbBundle.getMessage(CrudeMDLPropertiesPanel.class, "CrudeMDLPropertiesPanel.labelClusterProcessor.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(labelClusterProcessor, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weighty = 1.0;
    add(jPanel1, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridheight = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    add(jPanel2, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  @Override
  public final void load() {
    clusterer.setModel(new DefaultComboBoxModel(
            ModuleSelectionHelper.lookupNames(ClustererFactory.class).toArray()
            ));
    clusterProcessor.setModel(new DefaultComboBoxModel(
           ModuleSelectionHelper.lookupNames(ClusterProcessorFactory.class).toArray()
           ));

    clusterer.setSelectedItem(properties.getProperty(PROPERTIES_CLUSTERER, DEFAULT_MENU_TEXT));
    clusterProcessor.setSelectedItem(properties.getProperty(PROPERTIES_CLUSTER_PROCESSOR,
            DEFAULT_MENU_TEXT));
  }

  @Override
  public void store() {
    properties.setProperty(PROPERTIES_CLUSTERER,
            (String) clusterer.getSelectedItem());
    properties.setProperty(PROPERTIES_CLUSTER_PROCESSOR,
            (String) clusterProcessor.getSelectedItem());
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JComboBox clusterProcessor;
  private javax.swing.JComboBox clusterer;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JLabel labelClusterProcessor;
  private javax.swing.JLabel labelClusterer;
  // End of variables declaration//GEN-END:variables
}
