/*
 *  Copyright (C) 2010 sviro
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
package cz.cuni.mff.ksi.jinfer.runner.options;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.NbPreferences;

/**
 * Panel displaying runner options in jInfer Option panel section.
 *
 * @author sviro
 */
public final class RunnerPanel extends javax.swing.JPanel {
  public static final String OUTPUT_SHOW = "output.show";
  public static final String SCHEMA_OPEN = "schema.open";
  public static final String NAME_PATTERN = "name.pattern";
  public static final boolean OUTPUT_SHOW_DEFAULT = false;
  public static final boolean SCHEMA_OPEN_DEFAULT = true;
  public static final String NAME_PATTERN_DEFAULT = "generated-schema{n}";

  private final RunnerOptionsPanelController controller;

  RunnerPanel(RunnerOptionsPanelController controller) {
    this.controller = controller;
    initComponents();
    // TODO vektor listen to changes in form fields and call controller.changed()
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    jPanel1 = new javax.swing.JPanel();
    openSchema = new javax.swing.JCheckBox();
    namePattern = new javax.swing.JTextField();
    nameTemplateHelp = new javax.swing.JLabel();
    openSchemaHelp = new javax.swing.JLabel();
    openSchemaLabel = new javax.swing.JLabel();
    nameTemplateLabel = new javax.swing.JLabel();
    jPanel2 = new javax.swing.JPanel();
    showOutputWindow = new javax.swing.JCheckBox();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jPanel4 = new javax.swing.JPanel();
    jPanel3 = new javax.swing.JPanel();

    setLayout(new java.awt.GridBagLayout());

    jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(RunnerPanel.class, "RunnerPanel.jPanel1.border.title"))); // NOI18N
    jPanel1.setLayout(new java.awt.GridBagLayout());

    org.openide.awt.Mnemonics.setLocalizedText(openSchema, org.openide.util.NbBundle.getMessage(RunnerPanel.class, "RunnerPanel.null.text")); // NOI18N
    openSchema.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    jPanel1.add(openSchema, gridBagConstraints);

    namePattern.setText(org.openide.util.NbBundle.getMessage(RunnerPanel.class, "RunnerPanel.namePattern.text")); // NOI18N
    namePattern.setMaximumSize(new java.awt.Dimension(100, 27));
    namePattern.setMinimumSize(new java.awt.Dimension(100, 27));
    namePattern.setPreferredSize(new java.awt.Dimension(100, 27));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    jPanel1.add(namePattern, gridBagConstraints);

    nameTemplateHelp.setFont(new java.awt.Font("DejaVu Sans", 2, 13));
    org.openide.awt.Mnemonics.setLocalizedText(nameTemplateHelp, org.openide.util.NbBundle.getMessage(RunnerPanel.class, "RunnerPanel.nameTemplateHelp.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    jPanel1.add(nameTemplateHelp, gridBagConstraints);

    openSchemaHelp.setFont(new java.awt.Font("DejaVu Sans", 2, 13));
    org.openide.awt.Mnemonics.setLocalizedText(openSchemaHelp, org.openide.util.NbBundle.getMessage(RunnerPanel.class, "RunnerPanel.openSchemaHelp.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    jPanel1.add(openSchemaHelp, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(openSchemaLabel, org.openide.util.NbBundle.getMessage(RunnerPanel.class, "RunnerPanel.openSchemaLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    jPanel1.add(openSchemaLabel, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(nameTemplateLabel, org.openide.util.NbBundle.getMessage(RunnerPanel.class, "RunnerPanel.nameTemplateLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    jPanel1.add(nameTemplateLabel, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    add(jPanel1, gridBagConstraints);

    jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(RunnerPanel.class, "RunnerPanel.jPanel2.border.title"))); // NOI18N
    jPanel2.setLayout(new java.awt.GridBagLayout());

    org.openide.awt.Mnemonics.setLocalizedText(showOutputWindow, org.openide.util.NbBundle.getMessage(RunnerPanel.class, "RunnerPanel.showOutputWindow.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    jPanel2.add(showOutputWindow, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(RunnerPanel.class, "RunnerPanel.jLabel1.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    jPanel2.add(jLabel1, gridBagConstraints);

    jLabel2.setFont(new java.awt.Font("DejaVu Sans", 2, 13));
    org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(RunnerPanel.class, "RunnerPanel.jLabel2.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    jPanel2.add(jLabel2, gridBagConstraints);

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 0, Short.MAX_VALUE)
    );
    jPanel4Layout.setVerticalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 0, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    jPanel2.add(jPanel4, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    add(jPanel2, gridBagConstraints);

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 668, Short.MAX_VALUE)
    );
    jPanel3Layout.setVerticalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 108, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weighty = 1.0;
    add(jPanel3, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  void load() {
    openSchema.setSelected(NbPreferences.forModule(RunnerPanel.class).getBoolean(SCHEMA_OPEN, SCHEMA_OPEN_DEFAULT));
    showOutputWindow.setSelected(NbPreferences.forModule(RunnerPanel.class).getBoolean(OUTPUT_SHOW, OUTPUT_SHOW_DEFAULT));
    namePattern.setText(NbPreferences.forModule(RunnerPanel.class).get(NAME_PATTERN, NAME_PATTERN_DEFAULT));
  }

  void store() {
    NbPreferences.forModule(RunnerPanel.class).putBoolean(SCHEMA_OPEN, openSchema.isSelected());
    NbPreferences.forModule(RunnerPanel.class).putBoolean(OUTPUT_SHOW, showOutputWindow.isSelected());
    if (namePattern.getText().trim().isEmpty()) {
      namePattern.setText(NAME_PATTERN_DEFAULT);
    }

    NbPreferences.forModule(RunnerPanel.class).put(NAME_PATTERN, namePattern.getText());
  }

  boolean valid() {
    Pattern p = Pattern.compile("\\{n\\}");
    Matcher matcher = p.matcher(namePattern.getText());
    int matches = 0;
    while (matcher.find()) {
      matches++;
    }

    if (matches > 1) {
      DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(
              org.openide.util.NbBundle.getMessage(RunnerPanel.class, "RunnerPanel.multiple.numbering"),
              NotifyDescriptor.ERROR_MESSAGE));


      return false;
    }

    return true;
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JPanel jPanel4;
  private javax.swing.JTextField namePattern;
  private javax.swing.JLabel nameTemplateHelp;
  private javax.swing.JLabel nameTemplateLabel;
  private javax.swing.JCheckBox openSchema;
  private javax.swing.JLabel openSchemaHelp;
  private javax.swing.JLabel openSchemaLabel;
  private javax.swing.JCheckBox showOutputWindow;
  // End of variables declaration//GEN-END:variables
}
