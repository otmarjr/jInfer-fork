/*
 *  Copyright (C) 2011 sviro
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
package cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.graphviz.options;

import java.io.File;
import java.io.IOException;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.NbPreferences;

/**
 * Options panel of Graphviz layout binary selection.
 *
 * @author sviro
 */
@SuppressWarnings("PMD.SingularField")
final class GraphvizLayoutPanel extends JPanel {

  private static final long serialVersionUID = 4535346356l;
  private static final Logger LOG = Logger.getLogger(GraphvizLayoutPanel.class);

  public static final String BINARY_PATH_PROP = "dot.binary";
  public static final String BINARY_PATH_DEFAULT = "";

  public GraphvizLayoutPanel() {
    super();
    initComponents();
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("PMD")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    jLabel1 = new javax.swing.JLabel();
    binaryTextField = new javax.swing.JTextField();
    browseButton = new javax.swing.JButton();
    jPanel1 = new javax.swing.JPanel();
    jPanel2 = new javax.swing.JPanel();

    setLayout(new java.awt.GridBagLayout());

    org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(GraphvizLayoutPanel.class, "GraphvizLayoutPanel.jLabel1.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jLabel1, gridBagConstraints);

    binaryTextField.setEditable(false);
    binaryTextField.setText(org.openide.util.NbBundle.getMessage(GraphvizLayoutPanel.class, "GraphvizLayoutPanel.binaryTextField.text")); // NOI18N
    binaryTextField.setMinimumSize(new java.awt.Dimension(250, 27));
    binaryTextField.setPreferredSize(new java.awt.Dimension(250, 27));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(binaryTextField, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(browseButton, org.openide.util.NbBundle.getMessage(GraphvizLayoutPanel.class, "GraphvizLayoutPanel.browseButton.text")); // NOI18N
    browseButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        browseButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.insets = new java.awt.Insets(2, 8, 2, 8);
    add(browseButton, gridBagConstraints);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 49, Short.MAX_VALUE)
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 33, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    add(jPanel1, gridBagConstraints);

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 497, Short.MAX_VALUE)
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 35, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weighty = 1.0;
    add(jPanel2, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  @SuppressWarnings({"PMD.MethodArgumentCouldBeFinal", "PMD.UnusedFormalParameter"})
  private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
    final FileChooserBuilder fileChooserBuilder = new FileChooserBuilder(GraphvizLayoutPanel.class).setTitle("Select Dot binary").setFilesOnly(true);
    final File selectedFile = fileChooserBuilder.showOpenDialog();
    if (selectedFile != null) {
      try {
        final String binaryPath = selectedFile.getCanonicalPath();
        if (GraphvizUtils.isBinaryValid(binaryPath, true)) {
          binaryTextField.setText(binaryPath);
        } else {
          showInvalidBinaryDialog(binaryPath);
        }
      } catch (IOException ex) {
        LOG.error(ex.getMessage());
      }
    }

  }//GEN-LAST:event_browseButtonActionPerformed

  /**
   * Load values for all fields from properties. If no value for particular field is saved
   * in properties, default value is used.
   */
  public void load() {
    binaryTextField.setText(GraphvizUtils.getPath());
  }

  /**
   * Save all values in fields into properties.
   */
  public void store() {
    NbPreferences.forModule(GraphvizLayoutPanel.class).put(BINARY_PATH_PROP, binaryTextField.getText());
  }

  /**
   * Check if path entered in field is a valid path to dot binary.
   * @return <tt>true</tt> if path entered in field is valid path to dot binary, otherwise return <tt>false</tt>.
   */
  public boolean valid() {
    final String binaryPath = binaryTextField.getText();
    return GraphvizUtils.isBinaryValid(binaryPath, true);
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTextField binaryTextField;
  private javax.swing.JButton browseButton;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  // End of variables declaration//GEN-END:variables

  /**
   * Show warning dialog about invalidity of entered path to dot binary.
   *
   * @param binaryPath Path for which is dialog shown.
   */
  public void showInvalidBinaryDialog(final String binaryPath) {
    DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(org.openide.util.NbBundle.getMessage(GraphvizLayoutPanel.class, "dotbinary.notValid", binaryPath),
            NotifyDescriptor.WARNING_MESSAGE));
  }
}
