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
import cz.cuni.mff.ksi.jinfer.autoeditor.AutoEditor;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import java.awt.GridBagConstraints;
import java.util.logging.Logger;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 * TODO rio Comment!
 * @author vektor
 */
@ConvertAsProperties(dtd = "-//cz.cuni.mff.ksi.jinfer.autoeditor.gui//AutoEditor//EN",
autostore = false)
public final class AutoEditorTopComponent extends TopComponent {

  private static AutoEditorTopComponent instance;
  /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
  private static final String PREFERRED_ID = "AutoEditorTopComponent";
  private static final long serialVersionUID = 87543L;

  /**
   * Instance of AutoEditor which AutoEditor thread sleeps on.
   * When user interaction is done we should call notify() on this variable.
   */
  private AutoEditor autoEditorInstance = null;

  public AutoEditorTopComponent() {
    initComponents();
    setName(NbBundle.getMessage(AutoEditorTopComponent.class, "CTL_AutoEditorTopComponent"));
    setToolTipText(NbBundle.getMessage(AutoEditorTopComponent.class, "HINT_AutoEditorTopComponent"));
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jPanel1 = new javax.swing.JPanel();

    jPanel1.setLayout(new java.awt.GridBagLayout());

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
    );
  }// </editor-fold>//GEN-END:initComponents

  public <T> void drawAutomatonBasicVisualizationServer(final AbstractComponent component) {
    jPanel1.removeAll();
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.weightx = 1.0;
    constraints.weighty = 1.0;
    constraints.fill = GridBagConstraints.BOTH;
    jPanel1.add(component, constraints);
    jPanel1.validate();
    this.open();
    this.requestActive();
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel jPanel1;
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
      Logger.getLogger(AutoEditorTopComponent.class.getName()).warning(
              "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
      return getDefault();
    }
    if (win instanceof AutoEditorTopComponent) {
      return (AutoEditorTopComponent) win;
    }
    Logger.getLogger(AutoEditorTopComponent.class.getName()).warning(
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
    // TODO rio je to spravne? vyhodit dialog s potvrdenim
    if (RunningProject.isActiveProject()) {
      NotifyDescriptor notifyDescriptor = new NotifyDescriptor.Confirmation("Do you really want to stop the inferrence process?", "Stop inferrence", NotifyDescriptor.OK_CANCEL_OPTION);
      if (DialogDisplayer.getDefault().notify(notifyDescriptor) == NotifyDescriptor.OK_OPTION) {
        RunningProject.removeActiveProject();
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
