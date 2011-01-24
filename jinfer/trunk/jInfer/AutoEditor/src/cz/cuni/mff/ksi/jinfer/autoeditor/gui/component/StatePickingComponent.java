/*
 *  Copyright (C) 2010 rio
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

/*
 * StatesPickingComponent.java
 *
 * Created on Oct 20, 2010, 1:23:27 PM
 */

package cz.cuni.mff.ksi.jinfer.autoeditor.gui.component;

import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.Visualizer;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * TODO rio comment
 * @author rio
 */
public class StatePickingComponent<T> extends AbstractComponent<T> {

    public StatePickingComponent() {
      initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel1 = new javax.swing.JLabel();
    jScrollPane1 = new javax.swing.JScrollPane();
    jPanel1 = new javax.swing.JPanel();
    jLabel2 = new javax.swing.JLabel();

    jLabel1.setText(org.openide.util.NbBundle.getMessage(StatePickingComponent.class, "StatePickingComponent.jLabel1.text")); // NOI18N

    jPanel1.setLayout(new java.awt.GridBagLayout());
    jScrollPane1.setViewportView(jPanel1);

    jLabel2.setText(org.openide.util.NbBundle.getMessage(StatePickingComponent.class, "StatePickingComponent.jLabel2.text")); // NOI18N

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 673, Short.MAX_VALUE)
          .addComponent(jLabel1)
          .addComponent(jLabel2))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(12, 12, 12)
        .addComponent(jLabel1)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel2)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
  }// </editor-fold>//GEN-END:initComponents

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JScrollPane jScrollPane1;
  // End of variables declaration//GEN-END:variables

  @Override
  public JPanel getAutomatonDrawPanel() {
    return jPanel1;
  }

  public State<T> getPickedState() {
    final Visualizer<T> visualizer = getVisualizer();
    return new ArrayList<State<T>>(visualizer.getPickedVertexState().getPicked()).get(0);
  }

  public void setLabel(final String text) {
    jLabel1.setText(text);
  }
}
