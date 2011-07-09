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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.skstrings.properties;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.skstrings.SKStringsFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.greedy.Greedy;
import java.util.Properties;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * Properties panel for {@link Greedy}.
 * @author anti
 */
@SuppressWarnings({"PMD.SingularField", "PMD.MethodArgumentCouldBeFinal", "PMD.UnusedFormalParameter"})
public class SKStringsPropertiesPanel extends AbstractPropertiesPanel {

  private static final long serialVersionUID = 784477498435L;

  /** Creates new form ModuleSelectionJPanel */
  public SKStringsPropertiesPanel(final Properties properties) {
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

    jLabel2 = new javax.swing.JLabel();
    lblS = new javax.swing.JLabel();
    lblK = new javax.swing.JLabel();
    txtS = new javax.swing.JTextField();
    txtK = new javax.swing.JTextField();
    lblStrategy = new javax.swing.JLabel();
    txtStrategy = new javax.swing.JTextField();
    jPanel1 = new javax.swing.JPanel();

    setMinimumSize(new java.awt.Dimension(500, 300));
    setPreferredSize(new java.awt.Dimension(500, 50));
    setLayout(new java.awt.GridBagLayout());

    jLabel2.setText("Parameters"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel2, gridBagConstraints);

    lblS.setText("s"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(lblS, gridBagConstraints);

    lblK.setText("k"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(lblK, gridBagConstraints);

    txtS.setColumns(10);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(txtS, gridBagConstraints);

    txtK.setColumns(10);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(txtK, gridBagConstraints);

    lblStrategy.setText("strategy"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(lblStrategy, gridBagConstraints);

    txtStrategy.setColumns(10);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(txtStrategy, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridheight = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    add(jPanel1, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private String htmlize(String text) {
    return "<html><head></head><body style=\"margin-top: 0; font-family: sans;\">" + text + "</body></html>";
  }

  @Override
  public final void load() {
    txtS.setText(properties.getProperty(SKStringsFactory.PROPERTIES_S, SKStringsFactory.PROPERTIES_S_DEFAULT));
    txtK.setText(properties.getProperty(SKStringsFactory.PROPERTIES_K, SKStringsFactory.PROPERTIES_K_DEFAULT));
    txtStrategy.setText(properties.getProperty(SKStringsFactory.PROPERTIES_STRATEGY, SKStringsFactory.PROPERTIES_STRATEGY_DEFAULT));
    txtS.setInputVerifier(new InputVerifier() {

      @Override
      public boolean verify(JComponent input) {
        JTextField x = (JTextField) input;
        try {
          double s = Double.parseDouble(x.getText());
        } catch (NumberFormatException e) {
          return false;
        }
        return true;
      }
    });
    txtK.setInputVerifier(new InputVerifier() {

      @Override
      public boolean verify(JComponent input) {
        JTextField x = (JTextField) input;
        try {
          int k = Integer.parseInt(x.getText());
        } catch (NumberFormatException e) {
          return false;
        }
        return true;
      }
    });
  }

  @Override
  public void store() {
    properties.setProperty(SKStringsFactory.PROPERTIES_S, txtS.getText());
    properties.setProperty(SKStringsFactory.PROPERTIES_K, txtK.getText());
    properties.setProperty(SKStringsFactory.PROPERTIES_STRATEGY, txtStrategy.getText());
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel jLabel2;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JLabel lblK;
  private javax.swing.JLabel lblS;
  private javax.swing.JLabel lblStrategy;
  private javax.swing.JTextField txtK;
  private javax.swing.JTextField txtS;
  private javax.swing.JTextField txtStrategy;
  // End of variables declaration//GEN-END:variables
}
