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
package cz.cuni.mff.ksi.jinfer.advancedruledisplayer;

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Properties;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.apache.log4j.Logger;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.util.ImageUtilities;

/**
 * Rule display component. Acts as a tabbed pane, each tab showing one ruleset.
 *
 * @author vektor
 */
@ConvertAsProperties(dtd = "-//cz.cuni.mff.ksi.jinfer.ruledisplayer.display//RuleDisplayer//EN",
autostore = false)
public final class AdvancedRuleDisplayerTopComponent extends TopComponent {

  private static final long serialVersionUID = 5123131;
  private static AdvancedRuleDisplayerTopComponent instance;
  private static final String ICON_PATH = "cz/cuni/mff/ksi/jinfer/ruledisplayer/graphics/icon16.png";
  private static final String PREFERRED_ID = "RuleDisplayerTopComponent";
  private static final Logger LOG = Logger.getLogger(AdvancedRuleDisplayerTopComponent.class);

  private int panelSequence = 0;

  public AdvancedRuleDisplayerTopComponent() {
    super();
    initComponents();
    setName(NbBundle.getMessage(AdvancedRuleDisplayerTopComponent.class, "CTL_AdvancedRuleDisplayerTopComponent"));
    setToolTipText(NbBundle.getMessage(AdvancedRuleDisplayerTopComponent.class, "HINT_AdvancedRuleDisplayerTopComponent"));
    setIcon(ImageUtilities.loadImage(ICON_PATH, true));

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
  public void createNewPanel(final String name, JPanel graphPanel) {
    final JScrollPane jsp = new JScrollPane(graphPanel);
    panelSequence++;
    panels.add(name + " [" + panelSequence + "]", jsp);

    if (panels.getTabCount() > 10) {
      panels.remove(0);
    }
  }

  @SuppressWarnings("PMD")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    panels = new javax.swing.JTabbedPane();

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(panels, javax.swing.GroupLayout.DEFAULT_SIZE, 1967, Short.MAX_VALUE)
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(panels, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
        .addContainerGap())
    );
  }// </editor-fold>//GEN-END:initComponents
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTabbedPane panels;
  // End of variables declaration//GEN-END:variables

  /**
   * Gets default instance. Do not use directly: reserved for *.settings files only,
   * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
   * To obtain the singleton instance, use {@link #findInstance}.
   */
  public static synchronized AdvancedRuleDisplayerTopComponent getDefault() {
    if (instance == null) {
      instance = new AdvancedRuleDisplayerTopComponent();
    }
    return instance;
  }

  /**
   * Obtain the RuleDisplayerTopComponent instance. Never call {@link #getDefault} directly!
   */
  public static synchronized AdvancedRuleDisplayerTopComponent findInstance() {
    final TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
    if (win == null) {
      getDefault().open();
      LOG.warn("Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
      return getDefault();
    }
    if (win instanceof AdvancedRuleDisplayerTopComponent) {
      win.open();
      return (AdvancedRuleDisplayerTopComponent) win;
    }
    LOG.warn("There seem to be multiple components with the '" + PREFERRED_ID
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
