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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.khcontext.properties;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.khcontext.KHContextFactory;
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
public class KHContextPropertiesPanel extends AbstractPropertiesPanel {

  private static final long serialVersionUID = 7844435L;

  /** Creates new form ModuleSelectionJPanel */
  public KHContextPropertiesPanel(final Properties properties) {
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
    lblK = new javax.swing.JLabel();
    lblH = new javax.swing.JLabel();
    txtK = new javax.swing.JTextField();
    txtH = new javax.swing.JTextField();
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

    lblK.setText("k"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(lblK, gridBagConstraints);

    lblH.setText("h"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(lblH, gridBagConstraints);

    txtK.setColumns(10);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(txtK, gridBagConstraints);

    txtH.setColumns(10);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(txtH, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridheight = 4;
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
    txtK.setText(properties.getProperty(KHContextFactory.PROPERTIES_K, KHContextFactory.PROPERTIES_K_DEFAULT));
    txtH.setText(properties.getProperty(KHContextFactory.PROPERTIES_H, KHContextFactory.PROPERTIES_H_DEFAULT));
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
    txtH.setInputVerifier(new InputVerifier() {

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
    properties.setProperty(KHContextFactory.PROPERTIES_K, txtK.getText());
    properties.setProperty(KHContextFactory.PROPERTIES_H, txtH.getText());
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel jLabel2;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JLabel lblH;
  private javax.swing.JLabel lblK;
  private javax.swing.JTextField txtH;
  private javax.swing.JTextField txtK;
  // End of variables declaration//GEN-END:variables
}
