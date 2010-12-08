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
package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.simplifying.greedy.properties;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.crudemdl.ModuleParameters;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.conditiontesting.MergeConditionTesterFactory;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.simplifying.greedy.AutomatonSimplifierGreedyFactory;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Properties panel for AutomatonSimplifierGreedy.
 * @author anti
 */
public class AutomatonSimplifierGreedyPropertiesPanel extends AbstractPropertiesPanel {
  private static final String DEFAULT_MENU_TEXT = "<none available>";
  private static final long serialVersionUID = 784463431L;
  private static Map<String, JTextField> dynamicComponents;
  private static Map<String, String> dynamicParameters;


  /** Creates new form ModuleSelectionJPanel */
  public AutomatonSimplifierGreedyPropertiesPanel(final Properties properties) {
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
    comboConditionTester = new javax.swing.JComboBox();
    jScrollPane3 = new javax.swing.JScrollPane();
    descContitionTester = new javax.swing.JTextPane();
    panelParams = new javax.swing.JPanel();

    setMinimumSize(new java.awt.Dimension(500, 300));
    setPreferredSize(new java.awt.Dimension(500, 50));
    setLayout(new java.awt.GridBagLayout());

    jLabel1.setText("MergeConditionTester");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel1, gridBagConstraints);

    comboConditionTester.setMinimumSize(new java.awt.Dimension(200, 22));
    comboConditionTester.setPreferredSize(new java.awt.Dimension(200, 22));
    comboConditionTester.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        comboConditionTesterChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(comboConditionTester, gridBagConstraints);

    jScrollPane3.setMinimumSize(new java.awt.Dimension(200, 22));
    jScrollPane3.setPreferredSize(new java.awt.Dimension(200, 22));

    descContitionTester.setEditable(false);
    descContitionTester.setMinimumSize(new java.awt.Dimension(200, 22));
    descContitionTester.setPreferredSize(new java.awt.Dimension(200, 22));
    jScrollPane3.setViewportView(descContitionTester);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 1, 2, 1);
    add(jScrollPane3, gridBagConstraints);

    panelParams.setMinimumSize(new java.awt.Dimension(200, 22));
    panelParams.setPreferredSize(new java.awt.Dimension(200, 22));
    panelParams.setLayout(new java.awt.GridBagLayout());
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(panelParams, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void comboConditionTesterChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboConditionTesterChanged
    MergeConditionTesterFactory factory= ModuleSelectionHelper.lookupImpl(MergeConditionTesterFactory.class,
            (String) comboConditionTester.getSelectedItem());
    descContitionTester.setText(factory.getUserModuleDescription());

    panelParams.removeAll();
    dynamicComponents= new HashMap<String, JTextField>();
    dynamicParameters= new HashMap<String, String>();
    java.awt.GridBagConstraints gridBagConstraints;
    if (factory.getCapabilities().contains("parameters")) {
      ModuleParameters factParams= (ModuleParameters) factory;
      int i = 0;
      for (String parameterName : factParams.getParameterNames()) {
        JLabel l= new JLabel(parameterName);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2*i;
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        panelParams.add(l, gridBagConstraints);


        JTextField t = new JTextField(properties.getProperty(factory.getName() + parameterName));
        l.setLabelFor(t);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2*i;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        t.setColumns(10);
        panelParams.add(t, gridBagConstraints);

        JTextArea ar= new JTextArea(factParams.getParameterDisplayDescription(parameterName));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2*i+1;
        gridBagConstraints.gridwidth= 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        ar.setColumns(30);
        ar.setRows(10);
        ar.setWrapStyleWord(true);
        ar.setLineWrap(true);
        ar.setEditable(false);
        panelParams.add(ar, gridBagConstraints);
        
        dynamicComponents.put(parameterName, t);
        dynamicParameters.put(parameterName, factory.getName() + parameterName);
        ++i;
      }
    }
  }//GEN-LAST:event_comboConditionTesterChanged

  @Override
  public final void load() {
    comboConditionTester.setModel(new DefaultComboBoxModel(
            ModuleSelectionHelper.lookupNames(MergeConditionTesterFactory.class).toArray()
            ));
    comboConditionTester.setSelectedItem(properties.getProperty(AutomatonSimplifierGreedyFactory.PROPERTIES_CONDITION_TESTER, DEFAULT_MENU_TEXT));
    comboConditionTesterChanged(null);
  }

  @Override
  public void store() {
    properties.setProperty(AutomatonSimplifierGreedyFactory.PROPERTIES_CONDITION_TESTER,
            (String) comboConditionTester.getSelectedItem());

    for (String parameterName : dynamicComponents.keySet()) {
      properties.setProperty(
              dynamicParameters.get(parameterName),
              dynamicComponents.get(parameterName).getText());
    }
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JComboBox comboConditionTester;
  private javax.swing.JTextPane descContitionTester;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JPanel panelParams;
  // End of variables declaration//GEN-END:variables
}
