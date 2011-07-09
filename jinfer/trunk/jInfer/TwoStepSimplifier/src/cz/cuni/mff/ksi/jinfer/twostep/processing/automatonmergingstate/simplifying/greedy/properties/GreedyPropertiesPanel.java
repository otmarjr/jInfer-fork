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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.greedy.properties;

import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.objects.ProjectPropsComboRenderer;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTesterFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.greedy.Greedy;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.greedy.GreedyFactory;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;

/**
 * Properties panel for {@link Greedy}.
 * @author anti
 */
@SuppressWarnings({"PMD.SingularField", "PMD.MethodArgumentCouldBeFinal", "PMD.UnusedFormalParameter"})
public class GreedyPropertiesPanel extends AbstractPropertiesPanel {

  private static final long serialVersionUID = 784463431L;

  /** Creates new form ModuleSelectionJPanel */
  public GreedyPropertiesPanel(final Properties properties) {
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
  }// </editor-fold>//GEN-END:initComponents

  private String htmlize(String text) {
    return "<html><head></head><body style=\"margin-top: 0; font-family: sans;\">" + text + "</body></html>";
  }

  private void comboConditionTesterChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboConditionTesterChanged
    final MergeConditionTesterFactory factory = ModuleSelectionHelper.lookupImpl(MergeConditionTesterFactory.class,
            ((NamedModule) comboConditionTester.getSelectedItem()).getName());
    descContitionTester.setText(htmlize(factory.getUserModuleDescription()));
  }//GEN-LAST:event_comboConditionTesterChanged

  @Override
  public final void load() {
    comboConditionTester.setModel(new DefaultComboBoxModel(
            ModuleSelectionHelper.lookupImpls(MergeConditionTesterFactory.class).toArray()));
    comboConditionTester.setSelectedItem(ModuleSelectionHelper.lookupImpl(MergeConditionTesterFactory.class,
            properties.getProperty(GreedyFactory.PROPERTIES_CONDITION_TESTER,
            GreedyFactory.PROPERTIES_CONDITION_TESTER_DEFAULT)));
    comboConditionTesterChanged(null);
  }

  @Override
  public void store() {
    properties.setProperty(GreedyFactory.PROPERTIES_CONDITION_TESTER,
            ((NamedModule) comboConditionTester.getSelectedItem()).getName());


  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JComboBox comboConditionTester;
  private javax.swing.JTextPane descContitionTester;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JScrollPane jScrollPane3;
  // End of variables declaration//GEN-END:variables
}
