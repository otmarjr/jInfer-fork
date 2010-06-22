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
package cz.cuni.mff.ksi.jinfer.welcome;

import java.util.Properties;
import java.util.logging.Logger;
import org.netbeans.api.options.OptionsDisplayer;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.netbeans.api.settings.ConvertAsProperties;
import cz.cuni.mff.ksi.jinfer.fileselector.FileAddAction;
import cz.cuni.mff.ksi.jinfer.fileselector.FileSelectorTopComponent;
import cz.cuni.mff.ksi.jinfer.fileselector.nodes.FolderNode;
import cz.cuni.mff.ksi.jinfer.runner.Runner;
import javax.swing.Action;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.NbPreferences;

/**
 * jInfer welcome window shown on the first run.
 */
@ConvertAsProperties(dtd = "-//cz.cuni.mff.ksi.jinfer.welcome//Welcome//EN",
autostore = false)
public final class WelcomeTopComponent extends TopComponent {

  private static final long serialVersionUID = 789451321321l;
  private static WelcomeTopComponent instance;
  /** path to the icon used by the component and its open action */
  private static final String ICON_PATH = "cz/cuni/mff/ksi/jinfer/welcome/graphics/jinfer-icon16.png";
  private static final String PREFERRED_ID = "WelcomeTopComponent";

  public WelcomeTopComponent() {
    initComponents();
    setName(NbBundle.getMessage(WelcomeTopComponent.class, "CTL_WelcomeTopComponent"));
    setToolTipText(NbBundle.getMessage(WelcomeTopComponent.class, "HINT_WelcomeTopComponent"));
    setIcon(ImageUtilities.loadImage(ICON_PATH, true));
  }

  @SuppressWarnings("PMD")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jInferLogo = new javax.swing.JLabel();
    L1welcome = new javax.swing.JLabel();
    showOnStartup = new javax.swing.JCheckBox();
    L2gettingStarted = new javax.swing.JLabel();
    ico1options = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    ico2files = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    jLabel7 = new javax.swing.JLabel();
    ico3run = new javax.swing.JLabel();
    jLabel9 = new javax.swing.JLabel();
    jLabel10 = new javax.swing.JLabel();
    step1options = new javax.swing.JButton();
    step2addFiles = new javax.swing.JButton();
    step3run = new javax.swing.JButton();
    L2support = new javax.swing.JLabel();
    jLabel12 = new javax.swing.JLabel();
    jLabel13 = new javax.swing.JLabel();
    jLabel14 = new javax.swing.JLabel();
    jLabel15 = new javax.swing.JLabel();
    L2developingjInfer = new javax.swing.JLabel();
    jLabel17 = new javax.swing.JLabel();
    jLabel18 = new javax.swing.JLabel();
    jLabel1 = new javax.swing.JLabel();

    setBackground(java.awt.Color.white);
    setForeground(java.awt.Color.white);

    jInferLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/cuni/mff/ksi/jinfer/welcome/graphics/logo-vektor.png"))); // NOI18N
    org.openide.awt.Mnemonics.setLocalizedText(jInferLogo, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jInferLogo.text")); // NOI18N

    L1welcome.setFont(L1welcome.getFont().deriveFont(L1welcome.getFont().getSize()+5f));
    org.openide.awt.Mnemonics.setLocalizedText(L1welcome, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.L1welcome.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(showOnStartup, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.showOnStartup.text")); // NOI18N

    L2gettingStarted.setFont(L2gettingStarted.getFont().deriveFont(L2gettingStarted.getFont().getSize()+3f));
    org.openide.awt.Mnemonics.setLocalizedText(L2gettingStarted, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.L2gettingStarted.text")); // NOI18N

    ico1options.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/cuni/mff/ksi/jinfer/welcome/graphics/numer-1-32.png"))); // NOI18N
    org.openide.awt.Mnemonics.setLocalizedText(ico1options, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.ico1options.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel3.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel4.text")); // NOI18N

    ico2files.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/cuni/mff/ksi/jinfer/welcome/graphics/numer-2-32.png"))); // NOI18N
    org.openide.awt.Mnemonics.setLocalizedText(ico2files, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.ico2files.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel6.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel7.text")); // NOI18N

    ico3run.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/cuni/mff/ksi/jinfer/welcome/graphics/numer-3-32.png"))); // NOI18N
    org.openide.awt.Mnemonics.setLocalizedText(ico3run, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.ico3run.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel9, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel9.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel10, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel10.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(step1options, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.step1options.text")); // NOI18N
    step1options.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        step1optionsActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(step2addFiles, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.step2addFiles.text")); // NOI18N
    step2addFiles.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        step2addFilesActionPerformed(evt);
      }
    });

    step3run.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/cuni/mff/ksi/jinfer/welcome/graphics/arrow-right.png"))); // NOI18N
    org.openide.awt.Mnemonics.setLocalizedText(step3run, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.step3run.text")); // NOI18N
    step3run.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        step3runActionPerformed(evt);
      }
    });

    L2support.setFont(L2support.getFont().deriveFont(L2support.getFont().getSize()+3f));
    org.openide.awt.Mnemonics.setLocalizedText(L2support, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.L2support.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel12, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel12.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel13, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel13.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel14, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel14.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel15, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel15.text")); // NOI18N

    L2developingjInfer.setFont(L2developingjInfer.getFont().deriveFont(L2developingjInfer.getFont().getSize()+3f));
    org.openide.awt.Mnemonics.setLocalizedText(L2developingjInfer, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.L2developingjInfer.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel17, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel17.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel18, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel18.text")); // NOI18N

    jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/cuni/mff/ksi/jinfer/welcome/graphics/bottom_bar.png"))); // NOI18N
    org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel1.text")); // NOI18N

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGap(50, 50, 50)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel15)
              .addComponent(jLabel13)
              .addGroup(layout.createSequentialGroup()
                .addComponent(ico3run)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jLabel9)
                  .addGroup(layout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addComponent(jLabel10))))
              .addComponent(L2gettingStarted)
              .addComponent(L1welcome, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jInferLogo)
              .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(layout.createSequentialGroup()
                    .addComponent(ico2files)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel7))
                      .addComponent(jLabel6)))
                  .addGroup(layout.createSequentialGroup()
                    .addComponent(ico1options)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel4))
                      .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)))
                  .addComponent(L2support)
                  .addComponent(jLabel14)
                  .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jLabel18)
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(step1options, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                    .addComponent(step3run, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(L2developingjInfer, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(step2addFiles, javax.swing.GroupLayout.Alignment.LEADING))
                  .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 622, Short.MAX_VALUE))))
          .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(showOnStartup))
          .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1084, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(16, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(showOnStartup)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jInferLogo)
        .addGap(18, 18, 18)
        .addComponent(L1welcome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(18, 18, 18)
        .addComponent(L2gettingStarted)
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(ico1options)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel3)
              .addComponent(step1options, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel4)))
        .addGap(29, 29, 29)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(ico2files)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel6)
              .addComponent(step2addFiles, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel7)))
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(ico3run)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel9)
              .addComponent(step3run, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel10)))
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(L2support)
          .addComponent(L2developingjInfer))
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel14)
          .addComponent(jLabel17))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel12)
          .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel13)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel15)
        .addGap(18, 36, Short.MAX_VALUE)
        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
    );
  }// </editor-fold>//GEN-END:initComponents

  private void step1optionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_step1optionsActionPerformed
    OptionsDisplayer.getDefault().open("jInfer");
  }//GEN-LAST:event_step1optionsActionPerformed

  private void step2addFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_step2addFilesActionPerformed
    // TODO sviro Refactor to AddXmlAction call
    final Node[] nodes = ((FileSelectorTopComponent) WindowManager.getDefault().findTopComponent("FileSelectorTopComponent")).getExplorerManager().getRootContext().getChildren().getNodes();
    for (Node node : nodes) {
      if (((FolderNode) node).getFolderName().equals("XML")) {
        final Action[] actions = node.getActions(false);
        for (Action action : actions) {
          if (action instanceof FileAddAction) {
            action.actionPerformed(null);
          }
        }
      }
    }
  }//GEN-LAST:event_step2addFilesActionPerformed

  private void step3runActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_step3runActionPerformed
    // TODO sviro Refactor to a RunnerAction call
    new Runner().run();
  }//GEN-LAST:event_step3runActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel L1welcome;
  private javax.swing.JLabel L2developingjInfer;
  private javax.swing.JLabel L2gettingStarted;
  private javax.swing.JLabel L2support;
  private javax.swing.JLabel ico1options;
  private javax.swing.JLabel ico2files;
  private javax.swing.JLabel ico3run;
  private javax.swing.JLabel jInferLogo;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel10;
  private javax.swing.JLabel jLabel12;
  private javax.swing.JLabel jLabel13;
  private javax.swing.JLabel jLabel14;
  private javax.swing.JLabel jLabel15;
  private javax.swing.JLabel jLabel17;
  private javax.swing.JLabel jLabel18;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel9;
  private javax.swing.JCheckBox showOnStartup;
  private javax.swing.JButton step1options;
  private javax.swing.JButton step2addFiles;
  private javax.swing.JButton step3run;
  // End of variables declaration//GEN-END:variables

  /**
   * Gets default instance. Do not use directly: reserved for *.settings files only,
   * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
   * To obtain the singleton instance, use {@link #findInstance}.
   */
  public static synchronized WelcomeTopComponent getDefault() {
    if (instance == null) {
      instance = new WelcomeTopComponent();
    }
    return instance;
  }

  /**
   * Obtain the WelcomeTopComponent instance. Never call {@link #getDefault} directly!
   */
  public static synchronized WelcomeTopComponent findInstance() {
    final TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
    if (win == null) {
      Logger.getLogger(WelcomeTopComponent.class.getName()).warning(
              "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
      return getDefault();
    }
    if (win instanceof WelcomeTopComponent) {
      return (WelcomeTopComponent) win;
    }
    Logger.getLogger(WelcomeTopComponent.class.getName()).warning(
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
    final boolean showStartup = NbPreferences.forModule(WelcomeTopComponent.class).getBoolean("show.startup", true);
    showOnStartup.setSelected(showStartup);
    if (!showStartup) {
      close();
    }
  }

  @Override
  public void componentClosed() {
    NbPreferences.forModule(WelcomeTopComponent.class).putBoolean("show.startup", showOnStartup.isSelected());
  }

  // TODO vektor perhaps these 3 may be removed?
  private void writeProperties(final Properties p) {
  }

  private Object readProperties(final Properties p) {
    if (instance == null) {
      instance = this;
    }
    instance.readPropertiesImpl(p);
    return instance;
  }

  private void readPropertiesImpl(final Properties p) {
  }

  @Override
  protected String preferredID() {
    return PREFERRED_ID;
  }
}
