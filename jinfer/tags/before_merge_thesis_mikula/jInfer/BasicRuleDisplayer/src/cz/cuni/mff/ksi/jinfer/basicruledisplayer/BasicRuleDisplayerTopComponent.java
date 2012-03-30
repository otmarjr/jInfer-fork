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
package cz.cuni.mff.ksi.jinfer.basicruledisplayer;

import cz.cuni.mff.ksi.jinfer.basicruledisplayer.logic.RulePainter;
import cz.cuni.mff.ksi.jinfer.basicruledisplayer.logic.Utils;
import cz.cuni.mff.ksi.jinfer.basicruledisplayer.options.BasicRuleDisplayerPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.apache.log4j.Logger;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.openide.util.NbPreferences;

/**
 * Rule display component. Acts as a tabbed pane, each tab showing one ruleset.
 *
 * @author vektor
 */
@TopComponent.Description(
        preferredID = BasicRuleDisplayerTopComponent.PREFERRED_ID,
        iconBase = BasicRuleDisplayerTopComponent.ICON_PATH,
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(
        mode = "editor",
        openAtStartup = false)
@ActionID(
        category = "Window",
        id = "cz.cuni.mff.ksi.jinfer.basicruledisplayer.BasicRuleDisplayerTopComponent")
@ActionReference(
        path = "Menu/Window/jInfer",
        position = 101)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_BasicRuleDisplayerAction",
        preferredID = BasicRuleDisplayerTopComponent.PREFERRED_ID)
@SuppressWarnings("PMD.SingularField")
public final class BasicRuleDisplayerTopComponent extends TopComponent {

  private static final long serialVersionUID = 5123131;
  private static final Logger LOG = Logger.getLogger(BasicRuleDisplayerTopComponent.class);

  private static BasicRuleDisplayerTopComponent instance;

  public static final String ICON_PATH = "cz/cuni/mff/ksi/jinfer/basicruledisplayer/graphics/icon16.png";
  public static final String PREFERRED_ID = "BasicRuleDisplayerTopComponent";

  private int panelSequence = 0;

  public BasicRuleDisplayerTopComponent() {
    super();
    initComponents();
    setName(NbBundle.getMessage(BasicRuleDisplayerTopComponent.class, "CTL_BasicRuleDisplayerTopComponent"));
    setToolTipText(NbBundle.getMessage(BasicRuleDisplayerTopComponent.class, "HINT_BasicRuleDisplayerTopComponent"));

    panels.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(final MouseEvent evt) {
        final int tabIndex = panels.indexAtLocation(evt.getX(), evt.getY());

        if (tabIndex < 0) {
          return;
        }
        if ((evt.getModifiers() & InputEvent.BUTTON1_MASK) != 0
                && evt.getClickCount() >= 2) {
          panels.remove(tabIndex);
        }
      }
    });
  }

  /**
   * Creates a new panel with provided name.
   *
   * @return The RulePainter assigned to the newly created panel. It can be
   *  immediately used to draw a set of rules
   *  (via the RulePainter.setRules() method).
   */
  public RulePainter createNewPanel(final String name) {
    final RulePanel panel = new RulePanel();
    final JScrollPane jsp = new JScrollPane(panel);
    panelSequence++;
    panels.add(name + " [" + panelSequence + "]", jsp);

    if (panels.getTabCount() > NbPreferences.forModule(BasicRuleDisplayerPanel.class).getInt("max.panels", 10)) {
      panels.remove(0);
    }

    return panel.getRulePainter();
  }

  private class RulePanel extends JPanel {

    private static final long serialVersionUID = 8745252121l;
    private final RulePainter rp = new RulePainter(RulePanel.this);

    public RulePainter getRulePainter() {
      return this.rp;
    }

    @Override
    public void paint(final Graphics g) {
      rp.paint((Graphics2D) g);
    }
  }

  @SuppressWarnings("PMD")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    panels = new javax.swing.JTabbedPane();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    jLabel7 = new javax.swing.JLabel();
    jLabel8 = new javax.swing.JLabel();

    org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(BasicRuleDisplayerTopComponent.class, "BasicRuleDisplayerTopComponent.jLabel1.text")); // NOI18N

    jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getStyle() | java.awt.Font.BOLD));
    jLabel2.setForeground(Utils.getColorElement());
    org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(BasicRuleDisplayerTopComponent.class, "BasicRuleDisplayerTopComponent.jLabel2.text")); // NOI18N

    jLabel3.setFont(jLabel3.getFont().deriveFont(jLabel3.getFont().getStyle() | java.awt.Font.BOLD));
    jLabel3.setForeground(Utils.getColorAttribute());
    org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(BasicRuleDisplayerTopComponent.class, "BasicRuleDisplayerTopComponent.jLabel3.text")); // NOI18N

    jLabel4.setFont(jLabel4.getFont().deriveFont(jLabel4.getFont().getStyle() | java.awt.Font.BOLD));
    jLabel4.setForeground(Utils.getColorSimpleData());
    org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(BasicRuleDisplayerTopComponent.class, "BasicRuleDisplayerTopComponent.jLabel4.text")); // NOI18N

    jLabel5.setFont(jLabel5.getFont().deriveFont(jLabel5.getFont().getStyle() | java.awt.Font.BOLD));
    jLabel5.setForeground(Utils.getColorConcatenation());
    org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(BasicRuleDisplayerTopComponent.class, "BasicRuleDisplayerTopComponent.jLabel5.text")); // NOI18N

    jLabel6.setFont(jLabel6.getFont().deriveFont(jLabel6.getFont().getStyle() | java.awt.Font.BOLD));
    jLabel6.setForeground(Utils.getColorAlternation());
    org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(BasicRuleDisplayerTopComponent.class, "BasicRuleDisplayerTopComponent.jLabel6.text")); // NOI18N

    jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/cuni/mff/ksi/jinfer/basicruledisplayer/graphics/arrow.png"))); // NOI18N
    org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(BasicRuleDisplayerTopComponent.class, "BasicRuleDisplayerTopComponent.jLabel7.text")); // NOI18N

    jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/cuni/mff/ksi/jinfer/basicruledisplayer/graphics/lambda.png"))); // NOI18N
    org.openide.awt.Mnemonics.setLocalizedText(jLabel8, org.openide.util.NbBundle.getMessage(BasicRuleDisplayerTopComponent.class, "BasicRuleDisplayerTopComponent.jLabel8.text")); // NOI18N

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(panels, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addComponent(jLabel1)
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8))
              .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)))))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(panels, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(jLabel2)
          .addComponent(jLabel3)
          .addComponent(jLabel4)
          .addComponent(jLabel5)
          .addComponent(jLabel6))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel7)
          .addComponent(jLabel8))
        .addContainerGap())
    );
  }// </editor-fold>//GEN-END:initComponents
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JTabbedPane panels;
  // End of variables declaration//GEN-END:variables

  /**
   * Gets default instance. Do not use directly: reserved for *.settings files only,
   * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
   * To obtain the singleton instance, use {@link #findInstance}.
   */
  public static synchronized BasicRuleDisplayerTopComponent getDefault() {
    if (instance == null) {
      instance = new BasicRuleDisplayerTopComponent();
    }
    return instance;
  }

  /**
   * Obtain the RuleDisplayerTopComponent instance. Never call {@link #getDefault} directly!
   */
  public static synchronized BasicRuleDisplayerTopComponent findInstance() {
    final TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
    if (win == null) {
      getDefault().open();
      LOG.warn("Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
      return getDefault();
    }
    if (win instanceof BasicRuleDisplayerTopComponent) {
      win.open();
      return (BasicRuleDisplayerTopComponent) win;
    }
    LOG.warn("There seem to be multiple components with the '" + PREFERRED_ID
            + "' ID. That is a potential source of errors and unexpected behavior.");
    return getDefault();
  }
}
