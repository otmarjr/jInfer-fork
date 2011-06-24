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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.greedymdl.properties;

import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.objects.ProjectPropsComboRenderer;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.twostep.ModuleParameters;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTesterFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.AutomatonEvaluatorFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.greedy.Greedy;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.greedymdl.GreedyMDLFactory;
import java.awt.GridBagConstraints;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

/**
 * Properties panel for {@link Greedy}.
 * @author anti
 */
@SuppressWarnings({"PMD.SingularField", "PMD.MethodArgumentCouldBeFinal", "PMD.UnusedFormalParameter"})
public class GreedyMDLPropertiesPanel extends AbstractPropertiesPanel {

  private static final long serialVersionUID = 784463431L;
  private static Map<String, JTextField> dynamicComponents;
  private static Map<String, String> dynamicParameters;

  /** Creates new form ModuleSelectionJPanel */
  public GreedyMDLPropertiesPanel(final Properties properties) {
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
    jLabel3 = new javax.swing.JLabel();
    evaluator = new javax.swing.JComboBox();
    jScrollPane4 = new javax.swing.JScrollPane();
    descEvaluator = new javax.swing.JTextPane();
    jLabel2 = new javax.swing.JLabel();
    panelParams = new javax.swing.JPanel();

    setMinimumSize(new java.awt.Dimension(500, 300));
    setPreferredSize(new java.awt.Dimension(500, 50));
    setLayout(new java.awt.GridBagLayout());

    jLabel1.setText("Merge condition tester");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel1, gridBagConstraints);

    comboConditionTester.setRenderer(new ProjectPropsComboRenderer(comboConditionTester.getRenderer()));
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

    jScrollPane3.setBorder(null);
    jScrollPane3.setMinimumSize(new java.awt.Dimension(200, 22));
    jScrollPane3.setPreferredSize(new java.awt.Dimension(200, 22));

    descContitionTester.setContentType("text/html");
    descContitionTester.setEditable(false);
    descContitionTester.setFocusable(false);
    descContitionTester.setMinimumSize(new java.awt.Dimension(200, 22));
    descContitionTester.setOpaque(false);
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

    jLabel3.setText("Automaton Evaluator"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel3, gridBagConstraints);

    comboConditionTester.setRenderer(new ProjectPropsComboRenderer(comboConditionTester.getRenderer()));
    evaluator.setMinimumSize(new java.awt.Dimension(200, 22));
    evaluator.setPreferredSize(new java.awt.Dimension(200, 22));
    evaluator.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        evaluatorChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(evaluator, gridBagConstraints);

    jScrollPane4.setBorder(null);
    jScrollPane4.setMinimumSize(new java.awt.Dimension(200, 22));
    jScrollPane4.setPreferredSize(new java.awt.Dimension(200, 22));

    descEvaluator.setContentType("text/html");
    descEvaluator.setEditable(false);
    descEvaluator.setFocusable(false);
    descEvaluator.setMinimumSize(new java.awt.Dimension(200, 22));
    descEvaluator.setOpaque(false);
    descEvaluator.setPreferredSize(new java.awt.Dimension(200, 22));
    jScrollPane4.setViewportView(descEvaluator);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 1, 2, 1);
    add(jScrollPane4, gridBagConstraints);

    jLabel2.setText("Parameters of submodule");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel2, gridBagConstraints);

    panelParams.setMinimumSize(new java.awt.Dimension(200, 22));
    panelParams.setLayout(new java.awt.GridBagLayout());
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 1, 2, 1);
    add(panelParams, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private String htmlize(String text) {
    return "<html><head></head><body style=\"margin-top: 0; font-family: sans;\">" + text + "</body></html>";
  }

  private void comboConditionTesterChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboConditionTesterChanged
    final MergeConditionTesterFactory factory = ModuleSelectionHelper.lookupImpl(MergeConditionTesterFactory.class,
            ((NamedModule) comboConditionTester.getSelectedItem()).getName());
    descContitionTester.setText(htmlize(factory.getUserModuleDescription()));

    panelParams.removeAll();
    panelParams.validate();
    dynamicComponents = new HashMap<String, JTextField>();
    dynamicParameters = new HashMap<String, String>();
    GridBagConstraints gridBagConstraints;
    if (factory.getCapabilities().contains("parameters")) {
      final ModuleParameters factParams = (ModuleParameters) factory;
      int i = 0;
      for (String parameterName : factParams.getParameterNames()) {
        final JLabel l = new JLabel("Parameter " + parameterName);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2 * i;
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        panelParams.add(l, gridBagConstraints);


        final JTextField t = new JTextField(properties.getProperty(factory.getName() + parameterName, factParams.getParameterDefaultValue(parameterName)));
        l.setLabelFor(t);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2 * i;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        t.setColumns(10);
        panelParams.add(t, gridBagConstraints);

        final JTextPane ar = new JTextPane();
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2 * i + 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        ar.setContentType("text/html");
        ar.setEditable(false);
        ar.setFocusable(false);
        ar.setOpaque(false);
        ar.setText(htmlize(factParams.getParameterDisplayDescription(parameterName)));

        panelParams.add(ar, gridBagConstraints);

        dynamicComponents.put(parameterName, t);
        dynamicParameters.put(parameterName, factory.getName() + parameterName);
        ++i;
      }
    } else {
      final JLabel l = new JLabel("Submodule has no parameters to set.");
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
      gridBagConstraints.weightx = 0.0;
      gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
      panelParams.add(l, gridBagConstraints);
    }
    panelParams.validate();
  }//GEN-LAST:event_comboConditionTesterChanged

  private void evaluatorChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_evaluatorChanged
    final AutomatonEvaluatorFactory factory = ModuleSelectionHelper.lookupImpl(AutomatonEvaluatorFactory.class,
            ((NamedModule) evaluator.getSelectedItem()).getName());
    descEvaluator.setText(htmlize(factory.getUserModuleDescription()));
  }//GEN-LAST:event_evaluatorChanged

  @Override
  public final void load() {
    comboConditionTester.setModel(new DefaultComboBoxModel(
            ModuleSelectionHelper.lookupImpls(MergeConditionTesterFactory.class).toArray()));
    comboConditionTester.setSelectedItem(ModuleSelectionHelper.lookupImpl(MergeConditionTesterFactory.class,
            properties.getProperty(GreedyMDLFactory.PROPERTIES_CONDITION_TESTER,
            GreedyMDLFactory.PROPERTIES_CONDITION_TESTER_DEFAULT)));
    
    evaluator.setModel(new DefaultComboBoxModel(
            ModuleSelectionHelper.lookupImpls(AutomatonEvaluatorFactory.class).toArray()));
    evaluator.setSelectedItem(ModuleSelectionHelper.lookupImpl(AutomatonEvaluatorFactory.class,
            properties.getProperty(GreedyMDLFactory.PROPERTIES_EVALUATOR,
            GreedyMDLFactory.PROPERTIES_EVALUATOR_DEFAULT)));
    
    comboConditionTesterChanged(null);
    evaluatorChanged(null);
  }

  @Override
  public void store() {
    properties.setProperty(GreedyMDLFactory.PROPERTIES_CONDITION_TESTER,
            ((NamedModule) comboConditionTester.getSelectedItem()).getName());

    properties.setProperty(GreedyMDLFactory.PROPERTIES_EVALUATOR,
        ((NamedModule) evaluator.getSelectedItem()).getName());

    for (String parameterName : dynamicComponents.keySet()) {
      properties.setProperty(
              dynamicParameters.get(parameterName),
              dynamicComponents.get(parameterName).getText());
    }
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JComboBox comboConditionTester;
  private javax.swing.JTextPane descContitionTester;
  private javax.swing.JTextPane descEvaluator;
  private javax.swing.JComboBox evaluator;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JScrollPane jScrollPane4;
  private javax.swing.JPanel panelParams;
  // End of variables declaration//GEN-END:variables
}
