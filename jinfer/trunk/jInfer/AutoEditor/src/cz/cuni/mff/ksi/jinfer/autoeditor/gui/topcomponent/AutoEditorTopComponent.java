/*
 *  Copyright (C) 2010 vektor
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
package cz.cuni.mff.ksi.jinfer.autoeditor.gui.topcomponent;

import cz.cuni.mff.ksi.jinfer.autoeditor.gui.component.AbstractComponent;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import java.awt.GridBagConstraints;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.log4j.Logger;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.windows.WindowManager;

/**
 * Represents AutoEditor tab in GUI. {@link AbstractComponent} can be plotted
 * in this component.
 *
 * @author vektor
 */
@ConvertAsProperties(dtd = "-//cz.cuni.mff.ksi.jinfer.autoeditor.gui.topcomponent//AutoEditor//EN",
autostore = false)
public final class AutoEditorTopComponent extends TopComponent {

  private static AutoEditorTopComponent instance;
  /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
  private static final String PREFERRED_ID = "AutoEditorTopComponent";
  private static final long serialVersionUID = 87543L;

  private AbstractComponent component;
  private final JFileChooser fileChooser;

  public AutoEditorTopComponent() {
    initComponents();
    setName(NbBundle.getMessage(AutoEditorTopComponent.class, "CTL_AutoEditorTopComponent"));
    setToolTipText(NbBundle.getMessage(AutoEditorTopComponent.class, "HINT_AutoEditorTopComponent"));
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
    
    // Initialize FileChooser.
    // Get all supported image format names.
    String[] supportedFormatNames = ImageIO.getWriterFormatNames();
    /* Format names can contain duplicites like "jpg", "JPG", "JPEG".
     * Remove them and for each unique format name create an appropriate
     * FileNameExtensionFilter.
     */
    fileChooser = new JFileChooser();
    Map<String, FileNameExtensionFilter> supportedFormatNameSet = new HashMap<String, FileNameExtensionFilter>();
    for (String format : supportedFormatNames) {
      format = format.toLowerCase();
      if (format.equals("jpg")) {
        format = "jpeg";
      }
      if (!supportedFormatNameSet.containsKey(format)) {
        FileNameExtensionFilter filter = new FileNameExtensionFilter(format.toUpperCase() + " Image", format);
        supportedFormatNameSet.put(format, filter);
        fileChooser.addChoosableFileFilter(filter);
      }
    }

    // Remove default filter which shows all files.
    fileChooser.setAcceptAllFileFilterUsed(false);

    // If PNG image is supported, make it default in a save dialog.
    if (supportedFormatNameSet.containsKey("png")) {
      fileChooser.setFileFilter(supportedFormatNameSet.get("png"));
    }

    if (component == null) {
      jButton1.setEnabled(false);
    } else {
      jButton1.setEnabled(true);
    }
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jScrollPane1 = new javax.swing.JScrollPane();
    jPanel2 = new javax.swing.JPanel();
    jButton1 = new javax.swing.JButton();
    jSeparator2 = new javax.swing.JSeparator();
    jPanel1 = new javax.swing.JPanel();

    org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(AutoEditorTopComponent.class, "AutoEditorTopComponent.jButton1.text")); // NOI18N
    jButton1.setEnabled(false);
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    jPanel1.setLayout(new java.awt.GridBagLayout());

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel2Layout.createSequentialGroup()
            .addComponent(jButton1)
            .addGap(276, 276, 276))
          .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
          .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE))
        .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jButton1)
        .addGap(2, 2, 2)
        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
        .addContainerGap())
    );

    jScrollPane1.setViewportView(jPanel2);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
    );
  }// </editor-fold>//GEN-END:initComponents

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    /* Show the save dialog and get a selected file if user approved it.
     * Get selected extension filter, append appropriate extension to the
     * selected file, and save visualizer to it.
     */
    if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
      final File file = fileChooser.getSelectedFile();
      String fileName = file.getPath();
      final FileNameExtensionFilter fileFilter = (FileNameExtensionFilter)fileChooser.getFileFilter();
      final String extension = fileFilter.getExtensions()[0];

      final int fileNameLastDotIndex = fileName.lastIndexOf('.');
      if (fileNameLastDotIndex == -1) {
        // Extension is not present, append it.
        fileName = fileName + '.' + extension;
      } else if (fileNameLastDotIndex == fileName.length() - 1) {
        // File name ends with '.', append extension.
        fileName += extension;
      } else if (!fileName.substring(fileNameLastDotIndex + 1).equals(extension)) {
        // File has another extenstion, keep it and append the appropriate one.
        fileName += extension;
      }
      // If file name is just the extension, prefix it with default name.
      if (fileNameLastDotIndex == 0) {
        fileName = "unnamed" + fileName;
      }

      try {
        component.getVisualizer().saveImage(new File(fileName), extension);
      } catch (IOException e) {
        NotifyDescriptor notifyDescriptor = new NotifyDescriptor.Message("Saving of image '" + fileName + "' failed:\n" + e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
        DialogDisplayer.getDefault().notify(notifyDescriptor);
      }
    }
  }//GEN-LAST:event_jButton1ActionPerformed

  /**
   * Specified component is drawn on this top component. Only one component can
   * be drawn at a time. Component will be replaced by a new one specified in
   * a next call.
   *
   * @param component Component to be drawn.
   */
  public void drawComponent(final AbstractComponent component) {
    this.component = component;

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.weightx = 1.0;
    constraints.weighty = 1.0;
    constraints.fill = GridBagConstraints.BOTH;
    jPanel1.removeAll();
    jPanel1.add(component, constraints);
    jPanel1.validate();

    jButton1.setEnabled(true);

    this.open();
    this.requestActive();
 }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton jButton1;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JSeparator jSeparator2;
  // End of variables declaration//GEN-END:variables

  /**
   * Gets default instance. Do not use directly: reserved for *.settings files only,
   * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
   * To obtain the singleton instance, use {@link #findInstance}.
   */
  public static synchronized AutoEditorTopComponent getDefault() {
    if (instance == null) {
      instance = new AutoEditorTopComponent();
    }
    return instance;
  }

  /**
   * Obtain the AutoEditorTopComponent instance. Never call {@link #getDefault} directly!
   */
  public static synchronized AutoEditorTopComponent findInstance() {
    TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
    if (win == null) {
      Logger.getLogger(AutoEditorTopComponent.class.getName()).warn(
              "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
      return getDefault();
    }
    if (win instanceof AutoEditorTopComponent) {
      return (AutoEditorTopComponent) win;
    }
    Logger.getLogger(AutoEditorTopComponent.class.getName()).warn(
            "There seem to be multiple components with the '" + PREFERRED_ID
            + "' ID. That is a potential source of errors and unexpected behavior.");
    return getDefault();
  }

  @Override
  public int getPersistenceType() {
    return TopComponent.PERSISTENCE_ALWAYS;
  }

  @Override
  public void componentOpened() {
  }

  @Override
  public void componentClosed() {
  }

  @Override
  public boolean canClose() {
    if (RunningProject.isActiveProject()) {
      NotifyDescriptor notifyDescriptor = new NotifyDescriptor.Confirmation("Closing this window will stop the inference. Proceed?", "Stop inferrence", NotifyDescriptor.OK_CANCEL_OPTION);
      if (DialogDisplayer.getDefault().notify(notifyDescriptor) == NotifyDescriptor.OK_OPTION) {
        component.GUIInterrupt();
        return true;
      } else {
        return false;
      }
    }
    return true;
  }

  private void writeProperties(java.util.Properties p) {
  }

  private Object readProperties(java.util.Properties p) {
    if (instance == null) {
      instance = this;
    }
    instance.readPropertiesImpl(p);
    return instance;
  }

  private void readPropertiesImpl(java.util.Properties p) {
  }

  @Override
  protected String preferredID() {
    return PREFERRED_ID;
  }
}
