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
package cz.cuni.mff.ksi.jinfer.attrstats.glpk.options;

import cz.cuni.mff.ksi.jinfer.attrstats.glpk.GlpkUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.io.File;
import java.io.IOException;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.NbPreferences;

@SuppressWarnings("PMD.SingularField")
public final class GlpkPanel extends JPanel {

  private static final long serialVersionUID = 1875412L;

  private static final Logger LOG = Logger.getLogger(GlpkPanel.class);

  public GlpkPanel() {
    super();
    initComponents();
  }

  @SuppressWarnings("PMD")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    label = new javax.swing.JLabel();
    binaryPath = new javax.swing.JTextField();
    browse = new javax.swing.JButton();
    fillV = new javax.swing.JPanel();
    fillH = new javax.swing.JPanel();

    setLayout(new java.awt.GridBagLayout());

    org.openide.awt.Mnemonics.setLocalizedText(label, "GLPK Binary Path"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(label, gridBagConstraints);

    binaryPath.setEditable(false);
    binaryPath.setMaximumSize(null);
    binaryPath.setMinimumSize(new java.awt.Dimension(250, 27));
    binaryPath.setPreferredSize(new java.awt.Dimension(250, 27));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(binaryPath, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(browse, "Browse..."); // NOI18N
    browse.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        browseActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.insets = new java.awt.Insets(2, 8, 2, 8);
    add(browse, gridBagConstraints);

    javax.swing.GroupLayout fillVLayout = new javax.swing.GroupLayout(fillV);
    fillV.setLayout(fillVLayout);
    fillVLayout.setHorizontalGroup(
      fillVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 520, Short.MAX_VALUE)
    );
    fillVLayout.setVerticalGroup(
      fillVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 64, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weighty = 1.0;
    add(fillV, gridBagConstraints);

    javax.swing.GroupLayout fillHLayout = new javax.swing.GroupLayout(fillH);
    fillH.setLayout(fillHLayout);
    fillHLayout.setHorizontalGroup(
      fillHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 65, Short.MAX_VALUE)
    );
    fillHLayout.setVerticalGroup(
      fillHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 31, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    add(fillH, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void browseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseActionPerformed
    final FileChooserBuilder fileChooserBuilder = new FileChooserBuilder(GlpkPanel.class).setTitle("Select GLPK binary").setFilesOnly(true);
    final File selectedFile = fileChooserBuilder.showOpenDialog();
    if (selectedFile != null) {
      try {
        final String binary = selectedFile.getCanonicalPath();
        if (GlpkUtils.isBinaryValid(binary)) {
          binaryPath.setText(binary);
        } else {
          DialogDisplayer.getDefault().notify(
                  new NotifyDescriptor.Message("Selected file is not a valid GLPK binary.",
                  NotifyDescriptor.WARNING_MESSAGE));
        }
      } catch (IOException ex) {
        LOG.error(ex.getMessage());
      }
    }
  }//GEN-LAST:event_browseActionPerformed

  public void load() {
    binaryPath.setText(GlpkUtils.getPath());
  }

  public void store() {
    NbPreferences.forModule(GlpkPanel.class).put(GlpkUtils.BINARY_PATH_PROP, binaryPath.getText());
  }

  public boolean valid() {
    return BaseUtils.isEmpty(binaryPath.getText())
            || GlpkUtils.isBinaryValid();
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTextField binaryPath;
  private javax.swing.JButton browse;
  private javax.swing.JPanel fillH;
  private javax.swing.JPanel fillV;
  private javax.swing.JLabel label;
  // End of variables declaration//GEN-END:variables
}
