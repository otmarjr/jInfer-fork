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
    labelTimeLimit = new javax.swing.JLabel();
    timeLimit = new javax.swing.JSpinner();
    labelTimeLimitExplain = new javax.swing.JLabel();

    setLayout(new java.awt.GridBagLayout());

    org.openide.awt.Mnemonics.setLocalizedText(label, "GLPK Binary Path"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(label, gridBagConstraints);

    binaryPath.setEditable(false);
    binaryPath.setMaximumSize(null);
    binaryPath.setMinimumSize(new java.awt.Dimension(250, 27));
    binaryPath.setPreferredSize(new java.awt.Dimension(250, 27));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(binaryPath, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(browse, "Browse..."); // NOI18N
    browse.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        browseActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 8, 2, 8);
    add(browse, gridBagConstraints);

    fillV.setMaximumSize(new java.awt.Dimension(0, 0));
    fillV.setPreferredSize(new java.awt.Dimension(0, 0));

    javax.swing.GroupLayout fillVLayout = new javax.swing.GroupLayout(fillV);
    fillV.setLayout(fillVLayout);
    fillVLayout.setHorizontalGroup(
      fillVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 634, Short.MAX_VALUE)
    );
    fillVLayout.setVerticalGroup(
      fillVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 75, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weighty = 1.0;
    add(fillV, gridBagConstraints);

    fillH.setMaximumSize(new java.awt.Dimension(0, 0));
    fillH.setPreferredSize(new java.awt.Dimension(0, 0));

    javax.swing.GroupLayout fillHLayout = new javax.swing.GroupLayout(fillH);
    fillH.setLayout(fillHLayout);
    fillHLayout.setHorizontalGroup(
      fillHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 36, Short.MAX_VALUE)
    );
    fillHLayout.setVerticalGroup(
      fillHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 77, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    add(fillH, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(labelTimeLimit, "Time Limit [s]"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(labelTimeLimit, gridBagConstraints);

    timeLimit.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(60), Integer.valueOf(1), null, Integer.valueOf(1)));
    timeLimit.setMinimumSize(new java.awt.Dimension(60, 20));
    timeLimit.setPreferredSize(new java.awt.Dimension(60, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(timeLimit, gridBagConstraints);

    labelTimeLimitExplain.setFont(labelTimeLimitExplain.getFont().deriveFont((labelTimeLimitExplain.getFont().getStyle() | java.awt.Font.ITALIC)));
    org.openide.awt.Mnemonics.setLocalizedText(labelTimeLimitExplain, "<html>How long will be GLPK allowed to run.<br/>If it needs to run longer, it will return<br/>the best solution found so far.</html>"); // NOI18N
    labelTimeLimitExplain.setMaximumSize(new java.awt.Dimension(0, 0));
    labelTimeLimitExplain.setMinimumSize(new java.awt.Dimension(0, 0));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(labelTimeLimitExplain, gridBagConstraints);
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
    timeLimit.setValue(NbPreferences.forModule(GlpkPanel.class).getInt(GlpkUtils.TIME_LIMIT_PROP, GlpkUtils.TIME_LIMIT_DEFAULT));
  }

  public void store() {
    NbPreferences.forModule(GlpkPanel.class).put(GlpkUtils.BINARY_PATH_PROP, binaryPath.getText());
    NbPreferences.forModule(GlpkPanel.class).putInt(GlpkUtils.TIME_LIMIT_PROP, ((Integer)timeLimit.getValue()).intValue());
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
  private javax.swing.JLabel labelTimeLimit;
  private javax.swing.JLabel labelTimeLimitExplain;
  private javax.swing.JSpinner timeLimit;
  // End of variables declaration//GEN-END:variables
}
