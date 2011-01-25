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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.properties;

import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.objects.ProjectPropsComboRenderer;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.AutomatonMergingStateFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.RegexpAutomatonSimplifierFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifierFactory;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;

/**
 * Properties panel for ClusterProcessorAutomatonMergingState.
 * @author anti
 */
public class AutomatonMergingStatePropertiesPanel extends AbstractPropertiesPanel {

  private static final long serialVersionUID = 784463431L;

  /** Creates new form ModuleSelectionJPanel */
  public AutomatonMergingStatePropertiesPanel(final Properties properties) {
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

    labelAutomatonSimplifier = new javax.swing.JLabel();
    automatonSimplifier = new javax.swing.JComboBox();
    jScrollPane3 = new javax.swing.JScrollPane();
    descAutomatonSimplifier = new javax.swing.JTextPane();
    labelRegexpAutomatonSimplifier = new javax.swing.JLabel();
    jScrollPane4 = new javax.swing.JScrollPane();
    descRegexpAutomatonSimplifier = new javax.swing.JTextPane();
    regexpAutomatonSimplifier = new javax.swing.JComboBox();

    setMinimumSize(new java.awt.Dimension(500, 300));
    setPreferredSize(new java.awt.Dimension(500, 300));
    setLayout(new java.awt.GridBagLayout());

    labelAutomatonSimplifier.setText("Automaton simplifier");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(labelAutomatonSimplifier, gridBagConstraints);

    automatonSimplifier.setRenderer(new ProjectPropsComboRenderer());
    automatonSimplifier.setMinimumSize(new java.awt.Dimension(400, 22));
    automatonSimplifier.setPreferredSize(new java.awt.Dimension(400, 22));
    automatonSimplifier.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        automatonSimplifierChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(automatonSimplifier, gridBagConstraints);

    jScrollPane3.setBorder(null);

    descAutomatonSimplifier.setContentType("text/html");
    descAutomatonSimplifier.setEditable(false);
    descAutomatonSimplifier.setFocusable(false);
    descAutomatonSimplifier.setOpaque(false);
    jScrollPane3.setViewportView(descAutomatonSimplifier);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(jScrollPane3, gridBagConstraints);

    labelRegexpAutomatonSimplifier.setText("Regexp automaton simplifier");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(labelRegexpAutomatonSimplifier, gridBagConstraints);

    jScrollPane4.setBorder(null);

    descRegexpAutomatonSimplifier.setContentType("text/html");
    descRegexpAutomatonSimplifier.setEditable(false);
    descRegexpAutomatonSimplifier.setFocusable(false);
    descRegexpAutomatonSimplifier.setOpaque(false);
    jScrollPane4.setViewportView(descRegexpAutomatonSimplifier);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(jScrollPane4, gridBagConstraints);

    regexpAutomatonSimplifier.setRenderer(new ProjectPropsComboRenderer());
    regexpAutomatonSimplifier.setMinimumSize(new java.awt.Dimension(400, 22));
    regexpAutomatonSimplifier.setPreferredSize(new java.awt.Dimension(400, 22));
    regexpAutomatonSimplifier.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        regexpAutomatonSimplifierChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(regexpAutomatonSimplifier, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private String htmlize(String text) {
    return "<html><head></head><body style=\"margin-top: 0; font-family: sans;\">" + text + "</body></html>";
  }

  private void automatonSimplifierChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_automatonSimplifierChanged
    descAutomatonSimplifier.setText(htmlize(
            ModuleSelectionHelper.lookupImpl(AutomatonSimplifierFactory.class,
            ((NamedModule) automatonSimplifier.getSelectedItem()).getName()).getUserModuleDescription()));
  }//GEN-LAST:event_automatonSimplifierChanged

  private void regexpAutomatonSimplifierChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regexpAutomatonSimplifierChanged
    descRegexpAutomatonSimplifier.setText(htmlize(
            ModuleSelectionHelper.lookupImpl(RegexpAutomatonSimplifierFactory.class,
            ((NamedModule) regexpAutomatonSimplifier.getSelectedItem()).getName()).getUserModuleDescription()));
  }//GEN-LAST:event_regexpAutomatonSimplifierChanged

  @Override
  public final void load() {
    automatonSimplifier.setModel(new DefaultComboBoxModel(
            ModuleSelectionHelper.lookupNames(AutomatonSimplifierFactory.class).toArray()));

    automatonSimplifier.setSelectedItem(ModuleSelectionHelper.lookupName(AutomatonSimplifierFactory.class,
            properties.getProperty(AutomatonMergingStateFactory.PROPERTIES_AUTOMATON_SIMPLIFIER,
            AutomatonMergingStateFactory.PROPERTIES_AUTOMATON_SIMPLIFIER_DEFAULT)));
    automatonSimplifierChanged(null);

    regexpAutomatonSimplifier.setModel(new DefaultComboBoxModel(
            ModuleSelectionHelper.lookupNames(RegexpAutomatonSimplifierFactory.class).toArray()));

    regexpAutomatonSimplifier.setSelectedItem(ModuleSelectionHelper.lookupName(RegexpAutomatonSimplifierFactory.class,
            properties.getProperty(AutomatonMergingStateFactory.PROPERTIES_REGEXP_AUTOMATON_SIMPLIFIER,
            AutomatonMergingStateFactory.PROPERTIES_REGEXP_AUTOMATON_SIMPLIFIER_DEFAULT)));
    regexpAutomatonSimplifierChanged(null);
  }

  @Override
  public void store() {
    properties.setProperty(AutomatonMergingStateFactory.PROPERTIES_AUTOMATON_SIMPLIFIER,
            ((NamedModule) automatonSimplifier.getSelectedItem()).getName());

    properties.setProperty(AutomatonMergingStateFactory.PROPERTIES_REGEXP_AUTOMATON_SIMPLIFIER,
            ((NamedModule) regexpAutomatonSimplifier.getSelectedItem()).getName());
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JComboBox automatonSimplifier;
  private javax.swing.JTextPane descAutomatonSimplifier;
  private javax.swing.JTextPane descRegexpAutomatonSimplifier;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JScrollPane jScrollPane4;
  private javax.swing.JLabel labelAutomatonSimplifier;
  private javax.swing.JLabel labelRegexpAutomatonSimplifier;
  private javax.swing.JComboBox regexpAutomatonSimplifier;
  // End of variables declaration//GEN-END:variables
}
