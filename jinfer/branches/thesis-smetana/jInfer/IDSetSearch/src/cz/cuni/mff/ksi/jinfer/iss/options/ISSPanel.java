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
package cz.cuni.mff.ksi.jinfer.iss.options;

import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk.GlpkUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.NbPreferences;

/**
 * @author vektor
 */
@SuppressWarnings("PMD.SingularField")
public final class ISSPanel extends JPanel {

  private static final long serialVersionUID = 1875412L;
  private static final Logger LOG = Logger.getLogger(ISSPanel.class);

  public static final String BINARY_PATH_PROP = "glpk.binary";
  public static final String BINARY_PATH_DEFAULT = "";
  public static final String TIME_LIMIT_PROP = "glpk.time.limit";
  public static final int TIME_LIMIT_DEFAULT = 60;
  public static final String ALPHA_PROP = "weight.alpha";
  public static final float ALPHA_DEFAULT = 1.0f;
  public static final String BETA_PROP = "weight.beta";
  public static final float BETA_DEFAULT = 1.0f;

  public static final String ITERATIONS_PROP = "experiments.iterations";
  public static final int ITERATIONS_DEFAULT = 20;

  public static final String POOL_SIZE_PROP = "experiments.pool.size";
  public static final int POOL_SIZE_DEFAULT = 10;

  public ISSPanel() {
    super();
    initComponents();
  }

  @SuppressWarnings("PMD")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    fillV = new javax.swing.JPanel();
    panelGlpk = new javax.swing.JPanel();
    browse = new javax.swing.JButton();
    binaryPath = new javax.swing.JTextField();
    label = new javax.swing.JLabel();
    fillH1 = new javax.swing.JPanel();
    panelIss = new javax.swing.JPanel();
    labelWeight = new javax.swing.JLabel();
    labelAlpha = new javax.swing.JLabel();
    labelBeta = new javax.swing.JLabel();
    alpha = new javax.swing.JFormattedTextField();
    beta = new javax.swing.JFormattedTextField();
    fillH2 = new javax.swing.JPanel();
    labelAlphaExplain = new javax.swing.JLabel();
    labelBetaExplain = new javax.swing.JLabel();
    labelTimeLimit = new javax.swing.JLabel();
    timeLimit = new javax.swing.JSpinner();
    labelTimeLimitExplain = new javax.swing.JLabel();
    panelExperiments = new javax.swing.JPanel();
    labelIterations = new javax.swing.JLabel();
    labelPoolSize = new javax.swing.JLabel();
    fillH3 = new javax.swing.JPanel();
    iterations = new javax.swing.JSpinner();
    poolSize = new javax.swing.JSpinner();

    setLayout(new java.awt.GridBagLayout());

    fillV.setMaximumSize(new java.awt.Dimension(0, 0));
    fillV.setPreferredSize(new java.awt.Dimension(0, 0));

    javax.swing.GroupLayout fillVLayout = new javax.swing.GroupLayout(fillV);
    fillV.setLayout(fillVLayout);
    fillVLayout.setHorizontalGroup(
      fillVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 603, Short.MAX_VALUE)
    );
    fillVLayout.setVerticalGroup(
      fillVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 68, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weighty = 1.0;
    add(fillV, gridBagConstraints);

    panelGlpk.setBorder(javax.swing.BorderFactory.createTitledBorder("GLPK"));
    panelGlpk.setLayout(new java.awt.GridBagLayout());

    org.openide.awt.Mnemonics.setLocalizedText(browse, "Browse..."); // NOI18N
    browse.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        browseActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 8, 2, 8);
    panelGlpk.add(browse, gridBagConstraints);

    binaryPath.setEditable(false);
    binaryPath.setMaximumSize(null);
    binaryPath.setMinimumSize(new java.awt.Dimension(250, 27));
    binaryPath.setPreferredSize(new java.awt.Dimension(250, 27));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelGlpk.add(binaryPath, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(label, "GLPK Binary Path"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    panelGlpk.add(label, gridBagConstraints);

    fillH1.setMaximumSize(new java.awt.Dimension(0, 0));
    fillH1.setPreferredSize(new java.awt.Dimension(0, 0));

    javax.swing.GroupLayout fillH1Layout = new javax.swing.GroupLayout(fillH1);
    fillH1.setLayout(fillH1Layout);
    fillH1Layout.setHorizontalGroup(
      fillH1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 136, Short.MAX_VALUE)
    );
    fillH1Layout.setVerticalGroup(
      fillH1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 31, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    panelGlpk.add(fillH1, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    add(panelGlpk, gridBagConstraints);

    panelIss.setBorder(javax.swing.BorderFactory.createTitledBorder("ID Set Search from GUI")); // NOI18N
    panelIss.setLayout(new java.awt.GridBagLayout());

    org.openide.awt.Mnemonics.setLocalizedText(labelWeight, "<html><code>weight</code> function setup</html>"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    panelIss.add(labelWeight, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(labelAlpha, "Alpha"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    panelIss.add(labelAlpha, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(labelBeta, "Beta"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    panelIss.add(labelBeta, gridBagConstraints);

    alpha.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("0.####"))));
    alpha.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    alpha.setMinimumSize(new java.awt.Dimension(60, 20));
    alpha.setPreferredSize(new java.awt.Dimension(60, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelIss.add(alpha, gridBagConstraints);

    beta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("0.####"))));
    beta.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    beta.setMinimumSize(new java.awt.Dimension(60, 20));
    beta.setPreferredSize(new java.awt.Dimension(60, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelIss.add(beta, gridBagConstraints);

    fillH2.setPreferredSize(new java.awt.Dimension(0, 0));

    javax.swing.GroupLayout fillH2Layout = new javax.swing.GroupLayout(fillH2);
    fillH2.setLayout(fillH2Layout);
    fillH2Layout.setHorizontalGroup(
      fillH2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 227, Short.MAX_VALUE)
    );
    fillH2Layout.setVerticalGroup(
      fillH2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 129, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridheight = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    panelIss.add(fillH2, gridBagConstraints);

    labelAlphaExplain.setFont(labelAlphaExplain.getFont().deriveFont((labelAlphaExplain.getFont().getStyle() | java.awt.Font.ITALIC)));
    org.openide.awt.Mnemonics.setLocalizedText(labelAlphaExplain, "<html>Weight of attribute mapping <code>coverage</code>.</html>"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 8, 2, 8);
    panelIss.add(labelAlphaExplain, gridBagConstraints);

    labelBetaExplain.setFont(labelBetaExplain.getFont().deriveFont((labelBetaExplain.getFont().getStyle() | java.awt.Font.ITALIC)));
    org.openide.awt.Mnemonics.setLocalizedText(labelBetaExplain, "<html>Weight of attribute mapping <code>support</code>.</html>"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 8, 2, 8);
    panelIss.add(labelBetaExplain, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(labelTimeLimit, "Time Limit [s]"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    panelIss.add(labelTimeLimit, gridBagConstraints);

    timeLimit.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(60), Integer.valueOf(0), null, Integer.valueOf(1)));
    timeLimit.setMinimumSize(new java.awt.Dimension(60, 20));
    timeLimit.setPreferredSize(new java.awt.Dimension(60, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelIss.add(timeLimit, gridBagConstraints);

    labelTimeLimitExplain.setFont(labelTimeLimitExplain.getFont().deriveFont((labelTimeLimitExplain.getFont().getStyle() | java.awt.Font.ITALIC)));
    org.openide.awt.Mnemonics.setLocalizedText(labelTimeLimitExplain, "<html>\nHow long will be GLPK allowed to run.<br/>\nIf it needs to run longer, it will return<br/>\nthe best solution found so far.<br/>\n0 - no limit.\n</html>"); // NOI18N
    labelTimeLimitExplain.setMaximumSize(new java.awt.Dimension(0, 0));
    labelTimeLimitExplain.setMinimumSize(new java.awt.Dimension(0, 0));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 8, 2, 8);
    panelIss.add(labelTimeLimitExplain, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    add(panelIss, gridBagConstraints);

    panelExperiments.setBorder(javax.swing.BorderFactory.createTitledBorder("Experiments")); // NOI18N
    panelExperiments.setLayout(new java.awt.GridBagLayout());

    org.openide.awt.Mnemonics.setLocalizedText(labelIterations, "Iterations"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    panelExperiments.add(labelIterations, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(labelPoolSize, "Pool Size"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    panelExperiments.add(labelPoolSize, gridBagConstraints);

    javax.swing.GroupLayout fillH3Layout = new javax.swing.GroupLayout(fillH3);
    fillH3.setLayout(fillH3Layout);
    fillH3Layout.setHorizontalGroup(
      fillH3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 0, Short.MAX_VALUE)
    );
    fillH3Layout.setVerticalGroup(
      fillH3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 0, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    panelExperiments.add(fillH3, gridBagConstraints);

    iterations.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(20), Integer.valueOf(1), null, Integer.valueOf(1)));
    iterations.setMinimumSize(new java.awt.Dimension(60, 20));
    iterations.setPreferredSize(new java.awt.Dimension(60, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelExperiments.add(iterations, gridBagConstraints);

    poolSize.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(10), Integer.valueOf(1), null, Integer.valueOf(1)));
    poolSize.setMinimumSize(new java.awt.Dimension(60, 20));
    poolSize.setPreferredSize(new java.awt.Dimension(60, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelExperiments.add(poolSize, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    add(panelExperiments, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void browseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseActionPerformed
    final FileChooserBuilder fileChooserBuilder = new FileChooserBuilder(ISSPanel.class).setTitle("Select GLPK binary").setFilesOnly(true);
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
    final Preferences p = NbPreferences.forModule(ISSPanel.class);
    binaryPath.setText(GlpkUtils.getPath());
    timeLimit.setValue(p.getInt(TIME_LIMIT_PROP, TIME_LIMIT_DEFAULT));
    alpha.setValue(p.getFloat(ALPHA_PROP, ALPHA_DEFAULT));
    beta.setValue(p.getFloat(BETA_PROP, BETA_DEFAULT));
    iterations.setValue(p.getInt(ITERATIONS_PROP, ITERATIONS_DEFAULT));
    poolSize.setValue(p.getInt(POOL_SIZE_PROP, POOL_SIZE_DEFAULT));
  }

  public void store() {
    final Preferences p = NbPreferences.forModule(ISSPanel.class);
    p.put(BINARY_PATH_PROP, binaryPath.getText());
    p.putInt(TIME_LIMIT_PROP, ((Integer)timeLimit.getValue()).intValue());
    p.putFloat(ALPHA_PROP, ((Number)alpha.getValue()).floatValue());
    p.putFloat(BETA_PROP, ((Number)beta.getValue()).floatValue());
    p.putInt(ITERATIONS_PROP, ((Integer)iterations.getValue()).intValue());
    p.putInt(POOL_SIZE_PROP, ((Integer)poolSize.getValue()).intValue());
  }

  public boolean valid() {
    return BaseUtils.isEmpty(binaryPath.getText())
            || GlpkUtils.isBinaryValid();
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JFormattedTextField alpha;
  private javax.swing.JFormattedTextField beta;
  private javax.swing.JTextField binaryPath;
  private javax.swing.JButton browse;
  private javax.swing.JPanel fillH1;
  private javax.swing.JPanel fillH2;
  private javax.swing.JPanel fillH3;
  private javax.swing.JPanel fillV;
  private javax.swing.JSpinner iterations;
  private javax.swing.JLabel label;
  private javax.swing.JLabel labelAlpha;
  private javax.swing.JLabel labelAlphaExplain;
  private javax.swing.JLabel labelBeta;
  private javax.swing.JLabel labelBetaExplain;
  private javax.swing.JLabel labelIterations;
  private javax.swing.JLabel labelPoolSize;
  private javax.swing.JLabel labelTimeLimit;
  private javax.swing.JLabel labelTimeLimitExplain;
  private javax.swing.JLabel labelWeight;
  private javax.swing.JPanel panelExperiments;
  private javax.swing.JPanel panelGlpk;
  private javax.swing.JPanel panelIss;
  private javax.swing.JSpinner poolSize;
  private javax.swing.JSpinner timeLimit;
  // End of variables declaration//GEN-END:variables
}
