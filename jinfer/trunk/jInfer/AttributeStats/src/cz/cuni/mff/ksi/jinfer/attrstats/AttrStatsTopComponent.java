/*
 * Copyright (C) 2011 vitasek
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
package cz.cuni.mff.ksi.jinfer.attrstats;

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Properties;
import javax.swing.JScrollPane;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.WindowManager;

/**
 * Top component for attribute statistics presentation. Works similar to rule
 * displayers, showing a number of panels, each representing one statistics.
 *
 * @author vektor
 */
@ConvertAsProperties(dtd = "-//cz.cuni.mff.ksi.jinfer.attrstats//AttrStats//EN",
autostore = false)
@TopComponent.Description(preferredID = "AttrStatsTopComponent",
iconBase = "cz/cuni/mff/ksi/jinfer/attrstats/graphics/attr-icon-16.png",
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window",
id = "cz.cuni.mff.ksi.jinfer.attrstats.AttrStatsTopComponent")
@ActionReference(path = "Menu/Window/jInfer" /*, position = 333 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_AttrStatsAction",
preferredID = AttrStatsTopComponent.PREFERRED_ID)
public final class AttrStatsTopComponent extends TopComponent {

  private static final long serialVersionUID = -541163454643l;
  private int panelSequence = 0;
  private static AttrStatsTopComponent instance;
  public static final String PREFERRED_ID = "AttrStatsTopComponent";

  public AttrStatsTopComponent() {
    initComponents();
    setName(NbBundle.getMessage(AttrStatsTopComponent.class, "CTL_AttrStatsTopComponent"));
    setToolTipText(NbBundle.getMessage(AttrStatsTopComponent.class, "HINT_AttrStatsTopComponent"));

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

  public StatisticsPanel createNewPanel(final String name) {
    final StatisticsPanel panel = new StatisticsPanel();
    final JScrollPane jsp = new JScrollPane(panel);
    panelSequence++;
    panels.add(name + " [" + panelSequence + "]", jsp);

    if (panels.getTabCount() > 10) { // TODO vektor Get 10 from settings
      panels.remove(0);
    }

    return panel;
  }

  // TODO vektor This is obviously wrong in NBP 7.0, rewrite
  public static synchronized AttrStatsTopComponent getDefault() {
    if (instance == null) {
      instance = new AttrStatsTopComponent();
    }
    return instance;
  }

  public static synchronized AttrStatsTopComponent findInstance() {
    final TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
    if (win == null) {
      getDefault().open();
      return getDefault();
    }
    if (win instanceof AttrStatsTopComponent) {
      win.open();
      return (AttrStatsTopComponent) win;
    }
    return getDefault();
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
        .addComponent(panels, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(panels, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
        .addContainerGap())
    );
  }// </editor-fold>//GEN-END:initComponents
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTabbedPane panels;
  // End of variables declaration//GEN-END:variables

  @Override
  public void componentOpened() {
    // TODO add custom code on component opening
  }

  @Override
  public void componentClosed() {
    // TODO vektor add custom code on component closing
  }

  private void writeProperties(final Properties p) {
    // better to version settings since initial version as advocated at
    // http://wiki.apidesign.org/wiki/PropertyFiles
    // TODO vektor store your settings
  }

  private void readProperties(final Properties p) {
    // TODO vektor read your settings according to their version
  }
}
