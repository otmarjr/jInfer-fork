/*
 *  Copyright (C) 2010 anti
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
package cz.cuni.mff.ksi.jinfer.twostep.cleaning.chained.properties;

import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.objects.ProjectPropsComboRenderer;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.twostep.cleaning.RegularExpressionCleanerFactory;
import cz.cuni.mff.ksi.jinfer.twostep.cleaning.chained.ChainedFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * Properties panel for ClusterProcessorAutomatonMergingState.
 * @author anti
 */
public class CleanerChainedPropertiesPanel extends AbstractPropertiesPanel {

  private static final long serialVersionUID = 784463433L;
  private Map<Integer, JComboBox> dynamicComponents;

  /** Creates new form ModuleSelectionJPanel */
  public CleanerChainedPropertiesPanel(final Properties properties) {
    super(properties);
    initComponents();
    dynamicComponents = new HashMap<Integer, JComboBox>();
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    setLayout(new java.awt.GridBagLayout());
  }// </editor-fold>//GEN-END:initComponents

  @Override
  public final void load() {
    java.awt.GridBagConstraints gridBagConstraints;
    setLayout(new java.awt.GridBagLayout());
    List<NamedModule> cleanerNames = ModuleSelectionHelper.lookupNames(RegularExpressionCleanerFactory.class);
    List<NamedModule> modelStrings = new ArrayList<NamedModule>();
    modelStrings.add(new NamedModule() {

      @Override
      public String getName() {
        return "No cleaner";
      }

      @Override
      public String getDisplayName() {
        return getName();
      }

      @Override
      public String getModuleDescription() {
        return getDisplayName();
      }
    });
    for (NamedModule name : cleanerNames) {
      if (!name.getName().equals(ChainedFactory.NAME)) {
        modelStrings.add(name);
      }
    }
    int i;
    for (i = 0; i < modelStrings.size() - 1; i++) {
      final JLabel lbl = new javax.swing.JLabel();
      final JComboBox cmb = new JComboBox();
      cmb.setRenderer(new ProjectPropsComboRenderer());
      final JScrollPane scr = new javax.swing.JScrollPane();
      final JTextPane desc = new javax.swing.JTextPane();

      lbl.setText("#" + (i + 1) + ":");
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2 * i;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
      gridBagConstraints.weightx = 0.0;
      gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
      add(lbl, gridBagConstraints);

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 2 * i;
      gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
      gridBagConstraints.weightx = 1.0;
      gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
      cmb.setModel(new DefaultComboBoxModel(modelStrings.toArray()));
      cmb.addActionListener(new java.awt.event.ActionListener() {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          if (cmb.getSelectedIndex() == 0) {
            desc.setText("");
          } else {
            desc.setText(
                    ModuleSelectionHelper.lookupImpl(RegularExpressionCleanerFactory.class,
                    ((NamedModule) cmb.getSelectedItem()).getName()).getUserModuleDescription());
          }
        }
      });
      cmb.setSelectedItem(ModuleSelectionHelper.lookupImpl(RegularExpressionCleanerFactory.class, properties.getProperty(ChainedFactory.PROPERTIES_PREFIX + i, DEFAULT_MENU_TEXT)));
      cmb.getActionListeners()[0].actionPerformed(null);
      add(cmb, gridBagConstraints);
      dynamicComponents.put(Integer.valueOf(i), cmb);

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2 * i + 1;
      gridBagConstraints.gridwidth = 2;
      gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
      gridBagConstraints.weightx = 1.0;
      gridBagConstraints.weighty = 1.0;
      gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
      desc.setContentType("text/html");
      desc.setEditable(false);
      desc.setFocusable(false);
      desc.setOpaque(false);

      scr.setBorder(null);
      scr.setViewportView(desc);

      add(scr, gridBagConstraints);
    }
    if (i == 0) {
      final JTextPane noImplInfo = new JTextPane();
      noImplInfo.setEditable(false);
      noImplInfo.setFocusable(false);
      noImplInfo.setOpaque(false);

      noImplInfo.setText("No implementation, other than myself, of RegularExpressionCleanerFactory found.");
      add(noImplInfo);
    }
  }

  @Override
  public void store() {
    int lastSet = 0;
    for (Integer i : dynamicComponents.keySet()) {
      if (dynamicComponents.get(i).getSelectedIndex() > 0) {
        properties.setProperty(ChainedFactory.PROPERTIES_PREFIX + lastSet,
                ((NamedModule) dynamicComponents.get(i).getSelectedItem()).getName());
        lastSet++;
      }
    }
    properties.setProperty(ChainedFactory.PROPERTIES_COUNT, String.valueOf(lastSet));
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  // End of variables declaration//GEN-END:variables
}
