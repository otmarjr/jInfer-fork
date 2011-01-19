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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.properties;

import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.objects.ProjectPropsComboRenderer;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.StateRemovalFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordering.OrdererFactory;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;

/**
 * Properties panel for RegexpAutomatonSimplifierStateRemoval.
 * 
 * @author anti
 */
public class StateRemovalPropertiesPanel extends AbstractPropertiesPanel {

  private static final long serialVersionUID = 784463431L;

  /** Creates new form ModuleSelectionJPanel */
  public StateRemovalPropertiesPanel(final Properties properties) {
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

        setMinimumSize(new java.awt.Dimension(500, 300));
        setPreferredSize(new java.awt.Dimension(500, 300));
        setLayout(new java.awt.GridBagLayout());

        labelOrderer.setText("StateRemovalOrderer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        add(labelOrderer, gridBagConstraints);

        comboOrderer.setRenderer(new ProjectPropsComboRenderer());
        comboOrderer.setMinimumSize(new java.awt.Dimension(200, 22));
        comboOrderer.setPreferredSize(new java.awt.Dimension(200, 22));
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
            ModuleSelectionHelper.lookupImpl(OrdererFactory.class,
            ((NamedModule) comboOrderer.getSelectedItem()).getName()).getUserModuleDescription());
  }//GEN-LAST:event_comboOrdererChanged

  @Override
  public final void load() {
    comboOrderer.setModel(new DefaultComboBoxModel(
            ModuleSelectionHelper.lookupNames(OrdererFactory.class).toArray()));

    comboOrderer.setSelectedItem(ModuleSelectionHelper.lookupName(OrdererFactory.class, properties.getProperty(StateRemovalFactory.PROPERTIES_ORDERER, DEFAULT_MENU_TEXT)));
    comboOrdererChanged(null);
  }

  @Override
  public void store() {
    properties.setProperty(StateRemovalFactory.PROPERTIES_ORDERER,
            ((NamedModule) comboOrderer.getSelectedItem()).getName());
  }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox comboOrderer;
    private javax.swing.JTextPane descOrderer;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel labelOrderer;
    // End of variables declaration//GEN-END:variables
}
