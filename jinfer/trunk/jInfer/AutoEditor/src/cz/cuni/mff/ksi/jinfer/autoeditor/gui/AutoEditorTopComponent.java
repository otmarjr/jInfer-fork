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
package cz.cuni.mff.ksi.jinfer.autoeditor.gui;

import cz.cuni.mff.ksi.jinfer.autoeditor.AutoEditor;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import java.util.logging.Logger;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.netbeans.api.settings.ConvertAsProperties;

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
  private static boolean askUser = true;
  /**
   * Instance of AutoEditor which AutoEditor thread sleeps on.
   * When user interaction is done we should call notify() on this variable.
   */
  private AutoEditor autoEditorInstance;

  public AutoEditorTopComponent() {
    initComponents();
    setName(NbBundle.getMessage(AutoEditorTopComponent.class, "CTL_AutoEditorTopComponent"));
    setToolTipText(NbBundle.getMessage(AutoEditorTopComponent.class, "HINT_AutoEditorTopComponent"));
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
    
    jButton1.setEnabled(false);
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jScrollPane1 = new javax.swing.JScrollPane();
    jPanel1 = new javax.swing.JPanel();
    jButton1 = new javax.swing.JButton();
    jLabel1 = new javax.swing.JLabel();
    jCheckBox1 = new javax.swing.JCheckBox();

    jPanel1.setLayout(new java.awt.GridBagLayout());
    jScrollPane1.setViewportView(jPanel1);

    org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(AutoEditorTopComponent.class, "AutoEditorTopComponent.jButton1.text")); // NOI18N
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(AutoEditorTopComponent.class, "AutoEditorTopComponent.jLabel1.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jCheckBox1, org.openide.util.NbBundle.getMessage(AutoEditorTopComponent.class, "AutoEditorTopComponent.jCheckBox1.text")); // NOI18N
    jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jCheckBox1ActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addComponent(jButton1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 204, Short.MAX_VALUE)
            .addComponent(jCheckBox1))
          .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel1)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jButton1)
          .addComponent(jCheckBox1))
        .addContainerGap())
    );
  }// </editor-fold>//GEN-END:initComponents

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    jButton1.setEnabled(false);
    synchronized(autoEditorInstance) {
      autoEditorInstance.notify();
    }
    /*final DirectedSparseMultigraph<Integer, String> g = new DirectedSparseMultigraph<Integer, String>();
    g.addVertex(10);
    g.addVertex(20);
    g.addEdge("lala", 10, 20);
    final Layout<Integer, String> layout = new CircleLayout<Integer, String>(g);
    //layout.setSize(new Dimension(300, 300));
    final VisualizationViewer<Integer, String> vv =
            new VisualizationViewer<Integer, String>(layout);
    vv.setPreferredSize(new Dimension(350, 350));
    vv.setMinimumSize(new Dimension(350, 350));

    vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Integer>());
    vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<String>());

    final DefaultModalGraphMouse<Integer, String> gm = new DefaultModalGraphMouse<Integer, String>();
    gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
    vv.setGraphMouse(gm);

    final GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1;
    gbc.weighty = 1;
    jPanel1.add(vv, gbc);
    jPanel1.repaint();*/
  }//GEN-LAST:event_jButton1ActionPerformed

  private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
    askUser = false;
  }//GEN-LAST:event_jCheckBox1ActionPerformed

  public <T> void drawAutomatonBasicVisualizationServer(final AutoEditor autoEditorInstance, final BasicVisualizationServer<State<T>, Step<T>> bvs, final String labelText) {
    assert(askUser);
    this.autoEditorInstance = autoEditorInstance;
    jPanel1.removeAll();
    jPanel1.add(bvs);
    jPanel1.validate();
    jButton1.setEnabled(true);
    jLabel1.setText(labelText);
    this.open();
    this.requestActive();
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton jButton1;
  private javax.swing.JCheckBox jCheckBox1;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JScrollPane jScrollPane1;
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

  public static boolean getAskUser() {
    return askUser;
  }
}
