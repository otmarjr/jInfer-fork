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

import cz.cuni.mff.ksi.jinfer.base.objects.JHyperlinkLabel;
import cz.cuni.mff.ksi.jinfer.projecttype.actions.FilesAddAction;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.prefs.Preferences;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import org.apache.log4j.Logger;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.netbeans.spi.project.ui.support.ProjectChooser;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.Lookup.Template;
import org.openide.util.NbPreferences;
import org.openide.util.lookup.Lookups;

/**
 * jInfer welcome window shown on the first run.
 *
 * @author vektor
 */
@TopComponent.Description(
        preferredID = WelcomeTopComponent.PREFERRED_ID,
        iconBase = WelcomeTopComponent.ICON_PATH,
        persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(
        mode = "editor",
        openAtStartup = true)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_WelcomeAction",
        preferredID = WelcomeTopComponent.PREFERRED_ID)
@SuppressWarnings({"PMD.SingularField", "PMD.MethodArgumentCouldBeFinal", "PMD.UnusedFormalParameter"})
public final class WelcomeTopComponent extends TopComponent {

  private static final long serialVersionUID = 789451321321l;
  private static final Logger LOG = Logger.getLogger(WelcomeTopComponent.class);

  public static final String SHOW_ON_STARTUP = "show.startup";
  public static final String ICON_PATH = "cz/cuni/mff/ksi/jinfer/welcome/graphics/jinfer-icon16.png";
  public static final String PREFERRED_ID = "WelcomeTopComponent";

  private static final String CHECK_FOR_UPDATES_ACTION_FOLDER = "Actions/System/";
  private static final String CHECK_FOR_UPDATES_ACTION_INSTANCE = "org-netbeans-modules-autoupdate-ui-actions-CheckForUpdatesAction";

  private static WelcomeTopComponent instance;

  public WelcomeTopComponent() {
    initComponents();
    OpenProjects.getDefault().addPropertyChangeListener(new MainProjectListener(this));
    setName(NbBundle.getMessage(WelcomeTopComponent.class, "CTL_WelcomeTopComponent"));
    setToolTipText(NbBundle.getMessage(WelcomeTopComponent.class, "HINT_WelcomeTopComponent"));
  }

  @SuppressWarnings("PMD")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    jScrollPane1 = new javax.swing.JScrollPane();
    jPanel1 = new javax.swing.JPanel();
    jInferLogo = new javax.swing.JLabel();
    L1welcome = new javax.swing.JLabel();
    showOnStartup = new javax.swing.JCheckBox();
    L2gettingStarted = new javax.swing.JLabel();
    ico1options = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    ico3run = new javax.swing.JLabel();
    jLabel9 = new javax.swing.JLabel();
    jLabel10 = new javax.swing.JLabel();
    step1project = new javax.swing.JButton();
    L2support = new javax.swing.JLabel();
    jLabel12 = new JHyperlinkLabel("http://jinfer.sourceforge.net/documentation.html");
    jLabel13 = new JHyperlinkLabel("http://sourceforge.net/tracker/?group_id=302779&atid=1276495");
    jLabel14 = new JHyperlinkLabel("http://jinfer.sourceforge.net/");
    jLabel15 = new javax.swing.JLabel();
    L2developingjInfer = new javax.swing.JLabel();
    jLabel17 = new JHyperlinkLabel("http://jinfer.sourceforge.net/doc_tutorial_dev.html");
    jLabel18 = new JHyperlinkLabel("http://jinfer.sourceforge.net/download.html");
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    step3runProject = new javax.swing.JButton();
    step2addFiles = new javax.swing.JButton();
    jLabel6 = new javax.swing.JLabel();
    jButton1 = new javax.swing.JButton();
    jLabel7 = new javax.swing.JLabel();

    setBackground(java.awt.Color.white);
    setForeground(java.awt.Color.white);
    setMinimumSize(new java.awt.Dimension(666, 400));
    setPreferredSize(new java.awt.Dimension(200, 400));
    setLayout(new java.awt.GridBagLayout());

    jScrollPane1.setPreferredSize(new java.awt.Dimension(602, 400));

    jPanel1.setPreferredSize(new java.awt.Dimension(600, 600));

    jInferLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/cuni/mff/ksi/jinfer/welcome/graphics/logo.png"))); // NOI18N
    org.openide.awt.Mnemonics.setLocalizedText(jInferLogo, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jInferLogo.text")); // NOI18N

    L1welcome.setFont(L1welcome.getFont().deriveFont(L1welcome.getFont().getSize()+5f));
    org.openide.awt.Mnemonics.setLocalizedText(L1welcome, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.L1welcome.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(showOnStartup, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.showOnStartup.text")); // NOI18N
    showOnStartup.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        showOnStartupStateChanged(evt);
      }
    });

    L2gettingStarted.setFont(L2gettingStarted.getFont().deriveFont(L2gettingStarted.getFont().getSize()+3f));
    org.openide.awt.Mnemonics.setLocalizedText(L2gettingStarted, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.L2gettingStarted.text")); // NOI18N

    ico1options.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/cuni/mff/ksi/jinfer/welcome/graphics/numer-1-32.png"))); // NOI18N
    org.openide.awt.Mnemonics.setLocalizedText(ico1options, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.ico1options.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel3.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel4.text")); // NOI18N

    ico3run.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/cuni/mff/ksi/jinfer/welcome/graphics/numer-3-32.png"))); // NOI18N
    org.openide.awt.Mnemonics.setLocalizedText(ico3run, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.ico3run.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel9, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel9.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel10, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel10.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(step1project, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.step1project.text")); // NOI18N
    step1project.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        step1projectActionPerformed(evt);
      }
    });

    L2support.setFont(L2support.getFont().deriveFont(L2support.getFont().getStyle() | java.awt.Font.BOLD, L2support.getFont().getSize()+3));
    org.openide.awt.Mnemonics.setLocalizedText(L2support, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.L2support.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel12, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel12.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel13, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel13.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel14, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel14.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel15, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel15.text")); // NOI18N
    jLabel15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jLabel15.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabel15MouseClicked(evt);
      }
    });

    L2developingjInfer.setFont(L2developingjInfer.getFont().deriveFont(L2developingjInfer.getFont().getStyle() | java.awt.Font.BOLD, L2developingjInfer.getFont().getSize()+3));
    org.openide.awt.Mnemonics.setLocalizedText(L2developingjInfer, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.L2developingjInfer.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel17, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel17.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel18, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel18.text")); // NOI18N

    jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/cuni/mff/ksi/jinfer/welcome/graphics/bottom_bar.png"))); // NOI18N
    org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel1.text")); // NOI18N

    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/cuni/mff/ksi/jinfer/welcome/graphics/numer-2-32.png"))); // NOI18N
    org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel2.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel5.text")); // NOI18N

    step3runProject.setEnabled(false);
    org.openide.awt.Mnemonics.setLocalizedText(step3runProject, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.step3runProject.text")); // NOI18N
    step3runProject.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        step3runProjectActionPerformed(evt);
      }
    });

    step2addFiles.setEnabled(false);
    org.openide.awt.Mnemonics.setLocalizedText(step2addFiles, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.step2addFiles.text")); // NOI18N
    step2addFiles.setMaximumSize(new java.awt.Dimension(103, 29));
    step2addFiles.setMinimumSize(new java.awt.Dimension(103, 29));
    step2addFiles.setPreferredSize(new java.awt.Dimension(103, 29));
    step2addFiles.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        step2addFilesActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel6.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jButton1.text")); // NOI18N
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(WelcomeTopComponent.class, "WelcomeTopComponent.jLabel7.text")); // NOI18N

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(50, 50, 50)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel15)
              .addComponent(jLabel13)
              .addComponent(L2gettingStarted)
              .addComponent(L1welcome, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jInferLogo)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addComponent(jLabel5)))
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(ico1options)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                      .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                          .addComponent(jLabel7)
                          .addComponent(jLabel4)))))
                  .addComponent(L2support)
                  .addComponent(jLabel14)
                  .addComponent(jLabel12)
                  .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(ico3run)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel10))
                      .addComponent(jLabel9))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(18, 18, 18)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addComponent(jLabel18)
                      .addComponent(L2developingjInfer)
                      .addComponent(jLabel17)))
                  .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(34, 34, 34)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                      .addComponent(step2addFiles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                      .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                      .addComponent(step1project, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                      .addComponent(step3runProject, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)))))))
          .addComponent(showOnStartup))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(showOnStartup)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jInferLogo)
        .addGap(18, 18, 18)
        .addComponent(L1welcome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(L2gettingStarted)
        .addGap(18, 18, 18)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(ico1options)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(jLabel3)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel4)
              .addComponent(step1project, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))))
        .addGap(3, 3, 3)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel7)
          .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(18, 18, 18)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel2)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel5)
              .addComponent(step2addFiles, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addGap(18, 18, 18)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(step3runProject, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(jLabel9)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel10))
          .addComponent(ico3run))
        .addGap(18, 18, 18)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(L2support)
          .addComponent(L2developingjInfer))
        .addGap(18, 18, 18)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel14)
          .addComponent(jLabel17))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel12)
          .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel13)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel15)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jScrollPane1.setViewportView(jPanel1);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.ipadx = 643;
    gridBagConstraints.ipady = 577;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    add(jScrollPane1, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private Preferences prefs() {
    return NbPreferences.forModule(WelcomeTopComponent.class);
  }

  private void step1projectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_step1projectActionPerformed
    CommonProjectActions.newProjectAction().actionPerformed(evt);
  }//GEN-LAST:event_step1projectActionPerformed

  private void step3runProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_step3runProjectActionPerformed
    OpenProjects.getDefault().getMainProject().getLookup().lookup(ActionProvider.class).invokeAction(
            ActionProvider.COMMAND_RUN, Lookup.EMPTY);
  }//GEN-LAST:event_step3runProjectActionPerformed

  private void step2addFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_step2addFilesActionPerformed
    OpenProjects.getDefault().getMainProject().getLookup().lookup(ActionProvider.class).invokeAction(
            FilesAddAction.COMMAND_FILES_ADD, Lookup.EMPTY);
  }//GEN-LAST:event_step2addFilesActionPerformed

  private void showOnStartupStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_showOnStartupStateChanged
    setShowOnStartup(((JCheckBox) evt.getSource()).isSelected());

  }//GEN-LAST:event_showOnStartupStateChanged

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    try {
      final JFileChooser projectChooser = ProjectChooser.projectChooser();
      final int returnVal = projectChooser.showOpenDialog(null);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        final File selectedFile = projectChooser.getSelectedFile();
        final FileObject projectFileObject = FileUtil.toFileObject(selectedFile);
        final Project project = ProjectManager.getDefault().findProject(projectFileObject);
        final Project[] array = new Project[]{project};
        OpenProjects.getDefault().open(array, false);
        OpenProjects.getDefault().setMainProject(project);
      }
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    } catch (IllegalArgumentException ex) {
      Exceptions.printStackTrace(ex);
    }
  }//GEN-LAST:event_jButton1ActionPerformed

  private void jLabel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseClicked
    final Lookup pathLookup = Lookups.forPath(CHECK_FOR_UPDATES_ACTION_FOLDER);
    final Template<Action> actionTemplate = new Template<Action>(Action.class,
            CHECK_FOR_UPDATES_ACTION_FOLDER + CHECK_FOR_UPDATES_ACTION_INSTANCE, null);
    final Result<Action> lookupResult = pathLookup.lookup(actionTemplate);
    final Collection<? extends Action> foundActions = lookupResult.allInstances();

    //For each instance (should ony be one) call actionPerformed()
    for (Action action : foundActions) {
      action.actionPerformed(null);
    }
  }//GEN-LAST:event_jLabel15MouseClicked
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel L1welcome;
  private javax.swing.JLabel L2developingjInfer;
  private javax.swing.JLabel L2gettingStarted;
  private javax.swing.JLabel L2support;
  private javax.swing.JLabel ico1options;
  private javax.swing.JLabel ico3run;
  private javax.swing.JButton jButton1;
  private javax.swing.JLabel jInferLogo;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel10;
  private javax.swing.JLabel jLabel12;
  private javax.swing.JLabel jLabel13;
  private javax.swing.JLabel jLabel14;
  private javax.swing.JLabel jLabel15;
  private javax.swing.JLabel jLabel17;
  private javax.swing.JLabel jLabel18;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel9;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JCheckBox showOnStartup;
  private javax.swing.JButton step1project;
  private javax.swing.JButton step2addFiles;
  private javax.swing.JButton step3runProject;
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
      LOG.warn(
              "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
      return getDefault();
    }
    if (win instanceof WelcomeTopComponent) {
      return (WelcomeTopComponent) win;
    }
    LOG.warn("There seem to be multiple components with the '" + PREFERRED_ID
            + "' ID. That is a potential source of errors and unexpected behavior.");
    return getDefault();
  }

  @Override
  public void componentOpened() {
    final boolean showStartup = prefs().getBoolean(SHOW_ON_STARTUP, true);
    showOnStartup.setSelected(showStartup);
    if (!showStartup) {
      close();
    }
  }

  /**
   * Returns <tt>true</tt> if this panel is shown on startup.
   * @return <tt>true</tt> if this panel is shown on startup.
   */
  public boolean isShowOnStartup() {
    return prefs().getBoolean(SHOW_ON_STARTUP, true);
  }

  private void setShowOnStartup(final boolean show) {
    final boolean oldValue = isShowOnStartup();
    if (oldValue == show) {
      return;
    }
    prefs().putBoolean(SHOW_ON_STARTUP, show);
  }

  @Override
  public void componentClosed() {
    prefs().putBoolean(SHOW_ON_STARTUP, showOnStartup.isSelected());
  }

  @Override
  protected String preferredID() {
    return PREFERRED_ID;
  }

  /**
   * Get {@link JButton button} for run project.
   * @return Button for run project.
   */
  protected JButton getStep3runProject() {
    return step3runProject;
  }

  /**
   * Get {@link JButton button} for add files.
   * @return Button for add files.
   */
  protected JButton getStep2addFiles() {
    return step2addFiles;
  }
}
