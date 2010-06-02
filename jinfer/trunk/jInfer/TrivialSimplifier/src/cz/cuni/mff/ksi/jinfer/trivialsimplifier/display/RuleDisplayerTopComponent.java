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
package cz.cuni.mff.ksi.jinfer.trivialsimplifier.display;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Properties;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.util.ImageUtilities;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//cz.cuni.mff.ksi.jinfer.trivialsimplifier.display//RuleDisplayer//EN",
autostore = false)
public final class RuleDisplayerTopComponent extends TopComponent {

  private static final long serialVersionUID = 5123131;
  private static RuleDisplayerTopComponent instance;
  private static final String ICON_PATH = "cz/cuni/mff/ksi/jinfer/trivialsimplifier/graphics/icon16.png";
  private static final String PREFERRED_ID = "RuleDisplayerTopComponent";
  
  public RuleDisplayerTopComponent() {
    initComponents();
    setName(NbBundle.getMessage(RuleDisplayerTopComponent.class, "CTL_RuleDisplayerTopComponent"));
    setToolTipText(NbBundle.getMessage(RuleDisplayerTopComponent.class, "HINT_RuleDisplayerTopComponent"));
    setIcon(ImageUtilities.loadImage(ICON_PATH, true));
  }

  public RulePainter createNewPanel(final String name) {
    final RulePanel panel = new RulePanel();
    panels.add(name, panel);
    return panel.getRulePainter();
  }

  private class RulePanel extends JPanel {

    private static final long serialVersionUID = 8745252121l;

    private final RulePainter rp = new RulePainter(RulePanel.this);

    public RulePainter getRulePainter() {
      return this.rp;
    }

    @Override
    public void paint(Graphics g) {
      rp.paint((Graphics2D) g);
    }
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    panels = new javax.swing.JTabbedPane();

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(panels, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(panels, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
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
  public static synchronized RuleDisplayerTopComponent getDefault() {
    if (instance == null) {
      instance = new RuleDisplayerTopComponent();
    }
    return instance;
  }

  /**
   * Obtain the RuleDisplayerTopComponent instance. Never call {@link #getDefault} directly!
   */
  public static synchronized RuleDisplayerTopComponent findInstance() {
    TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
    if (win == null) {
      Logger.getLogger(RuleDisplayerTopComponent.class.getName()).warning(
              "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
      return getDefault();
    }
    if (win instanceof RuleDisplayerTopComponent) {
      return (RuleDisplayerTopComponent) win;
    }
    Logger.getLogger(RuleDisplayerTopComponent.class.getName()).warning(
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
    // TODO add custom code on component opening
  }

  @Override
  public void componentClosed() {
    // TODO add custom code on component closing
  }

  void writeProperties(Properties p) {
    // better to version settings since initial version as advocated at
    // http://wiki.apidesign.org/wiki/PropertyFiles
    p.setProperty("version", "1.0");
    // TODO store your settings
  }

  Object readProperties(Properties p) {
    if (instance == null) {
      instance = this;
    }
    instance.readPropertiesImpl(p);
    return instance;
  }

  private void readPropertiesImpl(Properties p) {
    String version = p.getProperty("version");
    // TODO read your settings according to their version
  }

  @Override
  protected String preferredID() {
    return PREFERRED_ID;
  }
}
