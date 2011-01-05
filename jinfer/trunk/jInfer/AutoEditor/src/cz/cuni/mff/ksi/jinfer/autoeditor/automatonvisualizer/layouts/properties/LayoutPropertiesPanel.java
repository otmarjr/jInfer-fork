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
package cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.properties;

import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.LayoutF;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import java.util.Map;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;

/**
 * Properties panel for AutomatonSimplifierGreedy.
 * @author anti
 */
public class LayoutPropertiesPanel extends AbstractPropertiesPanel {
  private static final String DEFAULT_MENU_TEXT = "<none available>";
  private static final long serialVersionUID = 784463431L;
  // TODO rio What are these two for??
  private static Map<String, JTextField> dynamicComponents;
  private static Map<String, String> dynamicParameters;


  /** Creates new form ModuleSelectionJPanel */
  public LayoutPropertiesPanel(final Properties properties) {
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

    jLabel1 = new javax.swing.JLabel();
    graphRenderer = new javax.swing.JComboBox();
    jScrollPane3 = new javax.swing.JScrollPane();
    desc = new javax.swing.JTextPane();

    setMinimumSize(new java.awt.Dimension(500, 300));
    setPreferredSize(new java.awt.Dimension(500, 50));
    setLayout(new java.awt.GridBagLayout());

    jLabel1.setText("Layout");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel1, gridBagConstraints);

    graphRenderer.setMinimumSize(new java.awt.Dimension(200, 22));
    graphRenderer.setPreferredSize(new java.awt.Dimension(200, 22));
    graphRenderer.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        graphRendererActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(graphRenderer, gridBagConstraints);

    jScrollPane3.setMinimumSize(new java.awt.Dimension(200, 22));
    jScrollPane3.setPreferredSize(new java.awt.Dimension(200, 22));

    desc.setEditable(false);
    desc.setMinimumSize(new java.awt.Dimension(200, 22));
    desc.setPreferredSize(new java.awt.Dimension(200, 22));
    jScrollPane3.setViewportView(desc);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 1, 2, 1);
    add(jScrollPane3, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void graphRendererActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_graphRendererActionPerformed
    LayoutF factory= ModuleSelectionHelper.lookupImpl(LayoutF.class,
            (String) graphRenderer.getSelectedItem());
    desc.setText(factory.getModuleDescription());
  }//GEN-LAST:event_graphRendererActionPerformed

  @Override
  public final void load() {
    graphRenderer.setModel(new DefaultComboBoxModel(
            ModuleSelectionHelper.lookupNames(LayoutF.class).toArray()
            ));
    graphRenderer.setSelectedItem(properties.getProperty("user-layout", DEFAULT_MENU_TEXT));
  }

  @Override
  public void store() {
    properties.setProperty("user-layout",
            (String) graphRenderer.getSelectedItem());
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTextPane desc;
  private javax.swing.JComboBox graphRenderer;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JScrollPane jScrollPane3;
  // End of variables declaration//GEN-END:variables
}
