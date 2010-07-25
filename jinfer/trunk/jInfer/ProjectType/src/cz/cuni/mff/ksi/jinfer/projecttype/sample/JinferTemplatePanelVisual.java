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
package cz.cuni.mff.ksi.jinfer.projecttype.sample;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import org.netbeans.spi.project.ui.support.ProjectChooser;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.filesystems.FileUtil;

/**
 * // TODO sviro Comment!
 * @author sviro
 */
public class JinferTemplatePanelVisual extends JPanel implements DocumentListener {

  public static final String PROP_PROJECT_NAME = "projectName";

  public static final String WIZARD_ERROR_MESSAGE = "WizardPanel_errorMessage";

  private static final long serialVersionUID = 785187562L;

  private JinferTemplateWizardPanel panel;

  public JinferTemplatePanelVisual(final JinferTemplateWizardPanel panel) {
    super();
    initComponents();
    this.panel = panel;
    // Register listener on the textFields to make the automatic updates
    projectNameTextField.getDocument().addDocumentListener(this);
    projectLocationTextField.getDocument().addDocumentListener(this);
  }

  public String getProjectName() {
    return this.projectNameTextField.getText();
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("PMD")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    projectNameLabel = new javax.swing.JLabel();
    projectNameTextField = new javax.swing.JTextField();
    projectLocationLabel = new javax.swing.JLabel();
    projectLocationTextField = new javax.swing.JTextField();
    browseButton = new javax.swing.JButton();
    createdFolderLabel = new javax.swing.JLabel();
    createdFolderTextField = new javax.swing.JTextField();

    projectNameLabel.setLabelFor(projectNameTextField);
    org.openide.awt.Mnemonics.setLocalizedText(projectNameLabel, org.openide.util.NbBundle.getMessage(JinferTemplatePanelVisual.class, "JinferTemplatePanelVisual.projectNameLabel.text")); // NOI18N

    projectLocationLabel.setLabelFor(projectLocationTextField);
    org.openide.awt.Mnemonics.setLocalizedText(projectLocationLabel, org.openide.util.NbBundle.getMessage(JinferTemplatePanelVisual.class, "JinferTemplatePanelVisual.projectLocationLabel.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(browseButton, org.openide.util.NbBundle.getMessage(JinferTemplatePanelVisual.class, "JinferTemplatePanelVisual.browseButton.text")); // NOI18N
    browseButton.setActionCommand(org.openide.util.NbBundle.getMessage(JinferTemplatePanelVisual.class, "JinferTemplatePanelVisual.browseButton.actionCommand")); // NOI18N
    browseButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        browseButtonActionPerformed(evt);
      }
    });

    createdFolderLabel.setLabelFor(createdFolderTextField);
    org.openide.awt.Mnemonics.setLocalizedText(createdFolderLabel, org.openide.util.NbBundle.getMessage(JinferTemplatePanelVisual.class, "JinferTemplatePanelVisual.createdFolderLabel.text")); // NOI18N

    createdFolderTextField.setEditable(false);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(projectNameLabel)
          .addComponent(projectLocationLabel)
          .addComponent(createdFolderLabel))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(projectNameTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
          .addComponent(projectLocationTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
          .addComponent(createdFolderTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(browseButton)
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(projectNameLabel)
          .addComponent(projectNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(projectLocationLabel)
          .addComponent(projectLocationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(browseButton))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(createdFolderLabel)
          .addComponent(createdFolderTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(213, Short.MAX_VALUE))
    );
  }// </editor-fold>//GEN-END:initComponents

  @SuppressWarnings("PMD")
  private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
    final String command = evt.getActionCommand();
    if ("BROWSE".equals(command)) {
      final JFileChooser chooser = new JFileChooser();
      FileUtil.preventFileChooserSymlinkTraversal(chooser, null);
      chooser.setDialogTitle("Select Project Location");
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      final String path = this.projectLocationTextField.getText();
      if (path.length() > 0) {
        final File f = new File(path);
        if (f.exists()) {
          chooser.setSelectedFile(f);
        }
      }
      if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(this)) {
        final File projectDir = chooser.getSelectedFile();
        projectLocationTextField.setText(FileUtil.normalizeFile(projectDir).getAbsolutePath());
      }
      panel.fireChangeEvent();
    }

  }//GEN-LAST:event_browseButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton browseButton;
  private javax.swing.JLabel createdFolderLabel;
  private javax.swing.JTextField createdFolderTextField;
  private javax.swing.JLabel projectLocationLabel;
  private javax.swing.JTextField projectLocationTextField;
  private javax.swing.JLabel projectNameLabel;
  private javax.swing.JTextField projectNameTextField;
  // End of variables declaration//GEN-END:variables

  @Override
  public void addNotify() {
    super.addNotify();
    //same problem as in 31086, initial focus on Cancel button
    projectNameTextField.requestFocus();
  }

  public boolean valid(final WizardDescriptor wizardDescriptor) {

    if (projectNameTextField.getText().length() == 0) {
      // TODO if using org.openide.dialogs >= 7.8, can use WizardDescriptor.PROP_ERROR_MESSAGE:
      wizardDescriptor.putProperty(WIZARD_ERROR_MESSAGE,
              "Project Name is not a valid folder name.");
      return false; // Display name not specified
    }
    final File f = FileUtil.normalizeFile(new File(projectLocationTextField.getText()).getAbsoluteFile());
    if (!f.isDirectory()) {
      wizardDescriptor.putProperty(WIZARD_ERROR_MESSAGE, "Project Folder is not a valid path.");
      return false;
    }
    final File destFolder = FileUtil.normalizeFile(new File(createdFolderTextField.getText()).getAbsoluteFile());

    File projLoc = destFolder;
    while (projLoc != null && !projLoc.exists()) {
      projLoc = projLoc.getParentFile();
    }
    if (projLoc == null || !projLoc.canWrite()) {
      wizardDescriptor.putProperty(WIZARD_ERROR_MESSAGE,
              "Project Folder cannot be created.");
      return false;
    }

    if (FileUtil.toFileObject(projLoc) == null) {
      wizardDescriptor.putProperty(WIZARD_ERROR_MESSAGE, "Project Folder is not a valid path.");
      return false;
    }

    final File[] kids = destFolder.listFiles();
    if (destFolder.exists() && kids != null && kids.length > 0) {
      // Folder exists and is not empty
      wizardDescriptor.putProperty(WIZARD_ERROR_MESSAGE,
              "Project Folder already exists and is not empty.");
      return false;
    }
    wizardDescriptor.putProperty(WIZARD_ERROR_MESSAGE, "");
    return true;
  }

  public void store(final WizardDescriptor d) {
    final String name = projectNameTextField.getText().trim();
    final String folder = createdFolderTextField.getText().trim();

    d.putProperty("projdir", new File(folder));
    d.putProperty("name", name);
  }

  public void read(final WizardDescriptor settings) {
    File projectLocation = (File) settings.getProperty("projdir");
    if (projectLocation == null || projectLocation.getParentFile() == null || !projectLocation.getParentFile().isDirectory()) {
      projectLocation = ProjectChooser.getProjectsFolder();
    } else {
      projectLocation = projectLocation.getParentFile();
    }
    this.projectLocationTextField.setText(projectLocation.getAbsolutePath());

    String projectName = (String) settings.getProperty("name");
    if (projectName == null) {
      projectName = "JinferTemplate";
    }
    this.projectNameTextField.setText(projectName);
    this.projectNameTextField.selectAll();
  }

  public void validate(final WizardDescriptor d) throws WizardValidationException {
    // nothing to validate
  }

  // Implementation of DocumentListener --------------------------------------
  public void changedUpdate(final DocumentEvent e) {
    updateTexts(e);
    if (this.projectNameTextField.getDocument() == e.getDocument()) {
      firePropertyChange(PROP_PROJECT_NAME, null, this.projectNameTextField.getText());
    }
  }

  public void insertUpdate(final DocumentEvent e) {
    updateTexts(e);
    if (this.projectNameTextField.getDocument() == e.getDocument()) {
      firePropertyChange(PROP_PROJECT_NAME, null, this.projectNameTextField.getText());
    }
  }

  public void removeUpdate(final DocumentEvent e) {
    updateTexts(e);
    if (this.projectNameTextField.getDocument() == e.getDocument()) {
      firePropertyChange(PROP_PROJECT_NAME, null, this.projectNameTextField.getText());
    }
  }

  /** Handles changes in the Project name and project directory, */
  private void updateTexts(final DocumentEvent e) {

    final Document doc = e.getDocument();

    if (doc == projectNameTextField.getDocument() || doc == projectLocationTextField.getDocument()) {
      // Change in the project name

      final String projectName = projectNameTextField.getText();
      final String projectFolder = projectLocationTextField.getText();

      //if (projectFolder.trim().length() == 0 || projectFolder.equals(oldName)) {
      createdFolderTextField.setText(projectFolder + File.separatorChar + projectName);
      //}

    }
    panel.fireChangeEvent(); // Notify that the panel changed
  }
}
