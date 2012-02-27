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
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTesterFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.AutomatonEvaluatorFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.greedy.Greedy;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.greedymdl.GreedyMDLFactory;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;

/**
 * Properties panel for {@link Greedy}.
 * @author anti
 */
@SuppressWarnings({"PMD.SingularField", "PMD.MethodArgumentCouldBeFinal", "PMD.UnusedFormalParameter"})
public class GreedyMDLPropertiesPanel extends AbstractPropertiesPanel {

  private static final long serialVersionUID = 784463431L;

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

    comboConditionTester.setMinimumSize(new java.awt.Dimension(200, 22));
    comboConditionTester.setPreferredSize(new java.awt.Dimension(200, 22));
    comboConditionTester.setRenderer(new ProjectPropsComboRenderer(comboConditionTester.getRenderer()));
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

    evaluator.setMinimumSize(new java.awt.Dimension(200, 22));
    evaluator.setPreferredSize(new java.awt.Dimension(200, 22));
    evaluator.setRenderer(new ProjectPropsComboRenderer(evaluator.getRenderer()));
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
  }// </editor-fold>//GEN-END:initComponents

  private String htmlize(String text) {
    return "<html><head></head><body style=\"margin-top: 0; font-family: sans;\">" + text + "</body></html>";
  }

  private void comboConditionTesterChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboConditionTesterChanged
    final MergeConditionTesterFactory factory = ModuleSelectionHelper.lookupImpl(MergeConditionTesterFactory.class,
            ((NamedModule) comboConditionTester.getSelectedItem()).getName());
    descContitionTester.setText(htmlize(factory.getUserModuleDescription()));
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

  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JComboBox comboConditionTester;
  private javax.swing.JTextPane descContitionTester;
  private javax.swing.JTextPane descEvaluator;
  private javax.swing.JComboBox evaluator;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JScrollPane jScrollPane4;
  // End of variables declaration//GEN-END:variables
}