/*
 * Copyright (C) 2011 sviro
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
package cz.cuni.mff.ksi.jinfer.functionalDependencies.newRepairer;

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import java.util.List;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import org.apache.log4j.Logger;

/**
 * Main component of the repair picker window.
 * @author sviro
 */
public class RepairPickerComponent extends javax.swing.JPanel {

  private static final Logger LOG = Logger.getLogger(RepairPickerComponent.class);
  private static final long serialVersionUID = 764324657L;
  private final Object monitor;
  private boolean interrupted = false;
  private RepairGroupModel model;
  private XMLShower xmlShower;
  private boolean isGUIDone = false;
  private boolean askUser = true;

  /** Creates new form NewJPanel */
  public RepairPickerComponent() {
    super();
    monitor = new Object();
    initComponents();
    xmlShower = new XMLShower();
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    repairTreePane = new javax.swing.JSplitPane();
    jScrollPane1 = new javax.swing.JScrollPane();
    repairTree = new javax.swing.JTree();
    xmlTreePlaceholder = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    jButton1 = new javax.swing.JButton();
    jCheckBox1 = new javax.swing.JCheckBox();

    setLayout(new java.awt.GridBagLayout());

    repairTreePane.setDividerLocation(200);

    repairTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    repairTree.setRootVisible(false);
    repairTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
      public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
        repairTreeValueChanged(evt);
      }
    });
    jScrollPane1.setViewportView(repairTree);

    repairTreePane.setLeftComponent(jScrollPane1);

    xmlTreePlaceholder.setLayout(new java.awt.GridBagLayout());

    jLabel1.setFont(new java.awt.Font("Ubuntu", 0, 32));
    jLabel1.setForeground(new java.awt.Color(105, 105, 105));
    jLabel1.setText(org.openide.util.NbBundle.getMessage(RepairPickerComponent.class, "RepairPickerComponent.jLabel1.text")); // NOI18N
    xmlTreePlaceholder.add(jLabel1, new java.awt.GridBagConstraints());

    repairTreePane.setRightComponent(xmlTreePlaceholder);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    add(repairTreePane, gridBagConstraints);

    jButton1.setText(org.openide.util.NbBundle.getMessage(RepairPickerComponent.class, "RepairPickerComponent.jButton1.text")); // NOI18N
    jButton1.setEnabled(false);
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(jButton1, gridBagConstraints);

    jCheckBox1.setText(org.openide.util.NbBundle.getMessage(RepairPickerComponent.class, "RepairPickerComponent.jCheckBox1.text")); // NOI18N
    jCheckBox1.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        jCheckBox1StateChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(jCheckBox1, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    guiDone();
    jButton1.setEnabled(isGUIDone);
  }//GEN-LAST:event_jButton1ActionPerformed

  private void repairTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_repairTreeValueChanged
    if (isGUIDone || repairTree.getSelectionCount() == 0 || !(repairTree.getLastSelectedPathComponent() instanceof RepairTreeNode)) {
      repairTreePane.setRightComponent(xmlTreePlaceholder);
      jButton1.setEnabled(false);
      return;
    }
    
    RepairTreeNode repairTreeNode = (RepairTreeNode) repairTree.getLastSelectedPathComponent();
    try {
      xmlShower.setContent(repairTreeNode.getRepair().getContentAfterRepair());
    } catch (InterruptedException ex) {
      LOG.error(ex);
      guiInterrupt();
      return;
    }
    repairTreePane.setRightComponent(xmlShower);
    jButton1.setEnabled(true);
  }//GEN-LAST:event_repairTreeValueChanged

  private void jCheckBox1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox1StateChanged
    askUser = !jCheckBox1.isSelected();
  }//GEN-LAST:event_jCheckBox1StateChanged

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton jButton1;
  private javax.swing.JCheckBox jCheckBox1;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JTree repairTree;
  private javax.swing.JSplitPane repairTreePane;
  private javax.swing.JPanel xmlTreePlaceholder;
  // End of variables declaration//GEN-END:variables

  /**
   * Thread that calls this method is suspended until method <code>guiDone()</code>
   * is called on this instance. Used to wait for user interaction when
   * {@link Automaton} is shown in AutoEditor.
   * @throws InterruptedException
   */
  public void waitForGuiDone() throws InterruptedException {
    synchronized (monitor) {
      monitor.wait();
    }
  }

  /**
   * Call to wake up all threads that are suspended in call of method
   * <code>waitForGUIDone()</code> on this instance.
   */
  public void guiDone() {
    isGUIDone = true;
    synchronized (monitor) {
      monitor.notifyAll();
    }
  }

  /**
   * Signals AutoEditor that waiting for any user interaction (on this instance)
   * has to be interrupted and inference has to interrupted as well.
   */
  public void guiInterrupt() {
    interrupted = true;
    jButton1.setEnabled(false);
    guiDone();
  }

  /**
   * Checks if <code>guiInterrupt()</code> was called on this instance.
   */
  public boolean guiInterrupted() {
    return interrupted;
  }

  public RepairCandidate getPickedRepair() {
    Object lastPathComponent = repairTree.getLastSelectedPathComponent();
    if (lastPathComponent instanceof RepairTreeNode) {
      return ((RepairTreeNode) lastPathComponent).getRepair();
    }
    
    return null;
  }

  public void setModel(final List<RepairGroup> repairGroups) {
    model = new RepairGroupModel(repairGroups);
    repairTree.setModel(new DefaultTreeModel(model.getTree()));
  }

  boolean shallAskUser() {
    return askUser;
  }
}
