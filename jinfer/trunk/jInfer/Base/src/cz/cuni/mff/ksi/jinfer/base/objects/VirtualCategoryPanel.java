/*
 *  Copyright (C) 2011 sviro
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
package cz.cuni.mff.ksi.jinfer.base.objects;

import java.util.List;

/**
 *
 * @author sviro
 */
public class VirtualCategoryPanel extends javax.swing.JPanel {

  private final String overallLabel;
  private final String categoryId;
  private final String categoryName;

  /** Creates new form VirtualCategoryPanel */
  public VirtualCategoryPanel(final String categoryId, final String categoryName, final List<String> modules) {
    this.categoryId = categoryId;
    this.categoryName = categoryName;
    this.overallLabel = getOverallLabel(modules);
    initComponents();
  }

  private String getOverallLabel(final List<String> modules) {
    StringBuilder builder = new StringBuilder();
    if (modules != null) {
      boolean isOne = modules.size() == 1;
      builder.append("<html>There ");
      builder.append(isOne ? "is " : "are ");
      builder.append("currently ").append(modules.size()).append(" ");
      builder.append(categoryName).append(" installed: ");

      boolean first = true;
      for (int i = 0; i < modules.size(); i++) {
        String module = modules.get(i);

        if (first) {
          first = false;
        } else {
          builder.append((i != modules.size() - 1) ? ", " : " and ");
        }
        builder.append("<b>").append(module).append("</b>");
      }

      builder.append(".</html>");
    }
    return builder.toString();
  }

  public String getCategoryId() {
    return categoryId;
  }

  public String getCategoryName() {
    return categoryName;
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

    jLabel1 = new javax.swing.JLabel();
    jPanel1 = new javax.swing.JPanel();
    jPanel2 = new javax.swing.JPanel();

    setLayout(new java.awt.GridBagLayout());

    jLabel1.setText(overallLabel);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 0);
    add(jLabel1, gridBagConstraints);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 0, Short.MAX_VALUE)
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 0, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    add(jPanel1, gridBagConstraints);

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 0, Short.MAX_VALUE)
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 0, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
    gridBagConstraints.weighty = 1.0;
    add(jPanel2, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel jLabel1;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  // End of variables declaration//GEN-END:variables
}
