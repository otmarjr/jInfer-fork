/*
 *  Copyright (C) 2010 anti
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
package cz.cuni.mff.ksi.jinfer.twostep.properties;

import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.objects.ProjectPropsComboRenderer;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.twostep.TwoStepSimplifierFactory;
import cz.cuni.mff.ksi.jinfer.twostep.cleaning.RegularExpressionCleanerFactory;
import cz.cuni.mff.ksi.jinfer.twostep.clustering.ClustererFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.ClusterProcessorFactory;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;

/**
 * Properties panel for TwoStepSimplifierFactory.
 * @author anti
 */
@SuppressWarnings({"PMD.SingularField", "PMD.UnusedFormalParameter", "PMD.MethodArgumentCouldBeFinal"})
public class TwoStepPropertiesPanel extends AbstractPropertiesPanel {

  private static final long serialVersionUID = 784463431L;

  /** Creates new form ModuleSelectionJPanel */
  public TwoStepPropertiesPanel(final Properties properties) {
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

    labelClusterer = new javax.swing.JLabel();
    clusterer = new javax.swing.JComboBox();
    jScrollPane3 = new javax.swing.JScrollPane();
    descClusterer = new javax.swing.JTextPane();
    labelClusterProcessor = new javax.swing.JLabel();
    clusterProcessor = new javax.swing.JComboBox();
    jScrollPane1 = new javax.swing.JScrollPane();
    descClusterProcessor = new javax.swing.JTextPane();
    labelCleaner = new javax.swing.JLabel();
    cleaner = new javax.swing.JComboBox();
    jScrollPane2 = new javax.swing.JScrollPane();
    descCleaner = new javax.swing.JTextPane();

    setMinimumSize(new java.awt.Dimension(500, 300));
    setPreferredSize(new java.awt.Dimension(500, 300));
    setLayout(new java.awt.GridBagLayout());

    labelClusterer.setText("Clusterer");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(labelClusterer, gridBagConstraints);

    clusterer.setRenderer(new ProjectPropsComboRenderer(clusterer.getRenderer()));
    clusterer.setMinimumSize(new java.awt.Dimension(200, 22));
    clusterer.setPreferredSize(new java.awt.Dimension(200, 22));
    clusterer.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        clustererChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(clusterer, gridBagConstraints);

    jScrollPane3.setBorder(null);

    descClusterer.setContentType("text/html");
    descClusterer.setEditable(false);
    descClusterer.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
    descClusterer.setFocusable(false);
    descClusterer.setOpaque(false);
    jScrollPane3.setViewportView(descClusterer);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(jScrollPane3, gridBagConstraints);

    labelClusterProcessor.setText("Cluster processor");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(labelClusterProcessor, gridBagConstraints);

    clusterProcessor.setRenderer(new ProjectPropsComboRenderer(clusterProcessor.getRenderer()));
    clusterProcessor.setMinimumSize(new java.awt.Dimension(200, 22));
    clusterProcessor.setPreferredSize(new java.awt.Dimension(200, 22));
    clusterProcessor.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        clusterProcessorChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(clusterProcessor, gridBagConstraints);

    jScrollPane1.setBorder(null);

    descClusterProcessor.setContentType("text/html");
    descClusterProcessor.setEditable(false);
    descClusterProcessor.setFocusable(false);
    descClusterProcessor.setOpaque(false);
    jScrollPane1.setViewportView(descClusterProcessor);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(jScrollPane1, gridBagConstraints);

    labelCleaner.setText("Regular expression cleaner");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(labelCleaner, gridBagConstraints);

    cleaner.setRenderer(new ProjectPropsComboRenderer(cleaner.getRenderer()));
    cleaner.setMinimumSize(new java.awt.Dimension(200, 22));
    cleaner.setPreferredSize(new java.awt.Dimension(200, 22));
    cleaner.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cleanerChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(cleaner, gridBagConstraints);

    jScrollPane2.setBorder(null);

    descCleaner.setContentType("text/html");
    descCleaner.setEditable(false);
    descCleaner.setFocusable(false);
    descCleaner.setOpaque(false);
    jScrollPane2.setViewportView(descCleaner);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(jScrollPane2, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private String htmlize(String text) {
    return "<html><head></head><body style=\"margin-top: 0; font-family: sans;\">" + text + "</body></html>";
  }

  private void clustererChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clustererChanged
    descClusterer.setText(htmlize(
            ModuleSelectionHelper.lookupImpl(ClustererFactory.class,
            ((NamedModule) clusterer.getSelectedItem()).getName()).getUserModuleDescription()));
  }//GEN-LAST:event_clustererChanged

  private void clusterProcessorChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clusterProcessorChanged
    descClusterProcessor.setText(htmlize(
            ModuleSelectionHelper.lookupImpl(ClusterProcessorFactory.class,
            ((NamedModule) clusterProcessor.getSelectedItem()).getName()).getUserModuleDescription()));
  }//GEN-LAST:event_clusterProcessorChanged

  private void cleanerChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cleanerChanged
    descCleaner.setText(htmlize(
            ModuleSelectionHelper.lookupImpl(RegularExpressionCleanerFactory.class,
            ((NamedModule) cleaner.getSelectedItem()).getName()).getUserModuleDescription()));
  }//GEN-LAST:event_cleanerChanged

  @Override
  public final void load() {
    clusterer.setModel(new DefaultComboBoxModel(
            ModuleSelectionHelper.lookupNames(ClustererFactory.class).toArray()));
    clusterer.setSelectedItem(ModuleSelectionHelper.lookupName(ClustererFactory.class,
            properties.getProperty(TwoStepSimplifierFactory.PROPERTIES_CLUSTERER, TwoStepSimplifierFactory.PROPERTIES_CLUSTERER_DEFAULT)));
    clustererChanged(null);

    clusterProcessor.setModel(new DefaultComboBoxModel(
            ModuleSelectionHelper.lookupNames(ClusterProcessorFactory.class).toArray()));
    clusterProcessor.setSelectedItem(ModuleSelectionHelper.lookupName(ClusterProcessorFactory.class,
            properties.getProperty(TwoStepSimplifierFactory.PROPERTIES_CLUSTER_PROCESSOR, TwoStepSimplifierFactory.PROPERTIES_CLUSTER_PROCESSOR_DEFAULT)));
    clusterProcessorChanged(null);

    cleaner.setModel(new DefaultComboBoxModel(
            ModuleSelectionHelper.lookupNames(RegularExpressionCleanerFactory.class).toArray()));
    cleaner.setSelectedItem(ModuleSelectionHelper.lookupName(RegularExpressionCleanerFactory.class, 
            properties.getProperty(TwoStepSimplifierFactory.PROPERTIES_CLEANER, TwoStepSimplifierFactory.PROPERTIES_CLEANER_DEFAULT)));
    cleanerChanged(null);
  }

  @Override
  public void store() {
    properties.setProperty(TwoStepSimplifierFactory.PROPERTIES_CLUSTERER,
            ((NamedModule) clusterer.getSelectedItem()).getName());
    properties.setProperty(TwoStepSimplifierFactory.PROPERTIES_CLUSTER_PROCESSOR,
            ((NamedModule) clusterProcessor.getSelectedItem()).getName());
    properties.setProperty(TwoStepSimplifierFactory.PROPERTIES_CLEANER,
            ((NamedModule) cleaner.getSelectedItem()).getName());
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JComboBox cleaner;
  private javax.swing.JComboBox clusterProcessor;
  private javax.swing.JComboBox clusterer;
  private javax.swing.JTextPane descCleaner;
  private javax.swing.JTextPane descClusterProcessor;
  private javax.swing.JTextPane descClusterer;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JLabel labelCleaner;
  private javax.swing.JLabel labelClusterProcessor;
  private javax.swing.JLabel labelClusterer;
  // End of variables declaration//GEN-END:variables
}
