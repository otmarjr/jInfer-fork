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
package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.regexping.stateremoval.properties;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.regexping.stateremoval.RegexpAutomatonSimplifierStateRemovalFactory;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.regexping.stateremoval.ordering.RegexpAutomatonSimplifierStateRemovalOrdererFactory;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;

/**
 * Properties panel for RegexpAutomatonSimplifierStateRemoval.
 * 
 * @author anti
 */
public class RegexpAutomatonSimplifierStateRemovalPropertiesPanel extends AbstractPropertiesPanel {

  private static final String DEFAULT_MENU_TEXT = "<none available>";
  private static final long serialVersionUID = 784463431L;

  /** Creates new form ModuleSelectionJPanel */
  public RegexpAutomatonSimplifierStateRemovalPropertiesPanel(final Properties properties) {
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

    labelOrderer = new javax.swing.JLabel();
    comboOrderer = new javax.swing.JComboBox();
    jScrollPane3 = new javax.swing.JScrollPane();
    descOrderer = new javax.swing.JTextPane();

    setMinimumSize(new java.awt.Dimension(600, 62));
    setPreferredSize(new java.awt.Dimension(600, 62));
    setLayout(new java.awt.GridBagLayout());

    labelOrderer.setText("StateRemovalOrderer");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(labelOrderer, gridBagConstraints);

    comboOrderer.setMinimumSize(new java.awt.Dimension(400, 22));
    comboOrderer.setPreferredSize(new java.awt.Dimension(400, 22));
    comboOrderer.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        comboOrdererChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(comboOrderer, gridBagConstraints);

    descOrderer.setEditable(false);
    jScrollPane3.setViewportView(descOrderer);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(jScrollPane3, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void comboOrdererChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboOrdererChanged
    descOrderer.setText(
            ModuleSelectionHelper.lookupImpl(RegexpAutomatonSimplifierStateRemovalOrdererFactory.class,
            (String) comboOrderer.getSelectedItem()).getDisplayModuleDescription()
            );
  }//GEN-LAST:event_comboOrdererChanged

  @Override
  public final void load() {
    comboOrderer.setModel(new DefaultComboBoxModel(
            ModuleSelectionHelper.lookupNames(RegexpAutomatonSimplifierStateRemovalOrdererFactory.class).toArray()
            ));

    comboOrderer.setSelectedItem(properties.getProperty(RegexpAutomatonSimplifierStateRemovalFactory.PROPERTIES_ORDERER, DEFAULT_MENU_TEXT));
    comboOrdererChanged(null);
  }

  @Override
  public void store() {
    properties.setProperty(RegexpAutomatonSimplifierStateRemovalFactory.PROPERTIES_ORDERER,
            (String) comboOrderer.getSelectedItem());
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JComboBox comboOrderer;
  private javax.swing.JTextPane descOrderer;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JLabel labelOrderer;
  // End of variables declaration//GEN-END:variables
}
