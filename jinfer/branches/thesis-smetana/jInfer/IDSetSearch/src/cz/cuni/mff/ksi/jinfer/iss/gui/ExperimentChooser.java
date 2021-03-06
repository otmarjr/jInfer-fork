/*
 * Copyright (C) 2011 vektor
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.iss.gui;

import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.objects.ProjectPropsComboRenderer;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.iss.ExperimentAction;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ExperimentSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author vektor
 */
@SuppressWarnings("PMD.SingularField")
public class ExperimentChooser extends JDialog {

  private static final long serialVersionUID = 87465123L;

  /** Creates new form ExperimentChooser */
  public ExperimentChooser() {
    super((JFrame) null, true);
    initComponents();

    setLocationRelativeTo(null);

    experiments.setModel(new DefaultComboBoxModel(
            ModuleSelectionHelper.lookupImpls(ExperimentSet.class).toArray()));
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

    label = new javax.swing.JLabel();
    experiments = new javax.swing.JComboBox();
    ok = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle(org.openide.util.NbBundle.getMessage(ExperimentChooser.class, "ExperimentChooser.title")); // NOI18N
    getContentPane().setLayout(new java.awt.GridBagLayout());

    label.setText(org.openide.util.NbBundle.getMessage(ExperimentChooser.class, "ExperimentChooser.label.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    getContentPane().add(label, gridBagConstraints);

    experiments.setMinimumSize(new java.awt.Dimension(250, 26));
    experiments.setPreferredSize(new java.awt.Dimension(250, 26));
    experiments.setRenderer(new ProjectPropsComboRenderer(experiments.getRenderer()));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    getContentPane().add(experiments, gridBagConstraints);

    ok.setText(org.openide.util.NbBundle.getMessage(ExperimentChooser.class, "ExperimentChooser.ok.text")); // NOI18N
    ok.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        okActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    getContentPane().add(ok, gridBagConstraints);

    pack();
  }// </editor-fold>//GEN-END:initComponents

private void okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okActionPerformed
  dispose();
  ExperimentAction.runExperiment(((NamedModule) experiments.getSelectedItem()).getName(), 0);
}//GEN-LAST:event_okActionPerformed
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JComboBox experiments;
  private javax.swing.JLabel label;
  private javax.swing.JButton ok;
  // End of variables declaration//GEN-END:variables
}
