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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.chained.properties;

import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.objects.ProjectPropsComboRenderer;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.twostep.cleaning.emptychildren.EmptyChildrenFactory;
import cz.cuni.mff.ksi.jinfer.twostep.cleaning.nestedconcatenation.NestedConcatenationFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifierFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.chained.Chained;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.chained.ChainedFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.defective.defectivemdl.DefectiveMDLFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.greedymdl.GreedyMDLFactory;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
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
 * Properties panel for {@link Chained} module.
 *
 * Lets user select regular expression cleaner that are to be used in chain.
 * 
 * @author anti
 */
public class AutomatonSimplifierChainedPropertiesPanel extends AbstractPropertiesPanel {

  private static final long serialVersionUID = 74463433L;
  private final Map<Integer, JComboBox> dynamicComponents;

  /** Creates new form ModuleSelectionJPanel */
  public AutomatonSimplifierChainedPropertiesPanel(final Properties properties) {
    super(properties);
    initComponents();
    dynamicComponents = new HashMap<Integer, JComboBox>();
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings({"unchecked", "PMD"})
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    setLayout(new java.awt.GridBagLayout());
  }// </editor-fold>//GEN-END:initComponents

  private String htmlize(final String text) {
    return "<html><head></head><body style=\"margin-top: 0; font-family: sans;\">" + text + "</body></html>";
  }

  @Override
  public final void load() {
    GridBagConstraints gridBagConstraints;
    setLayout(new GridBagLayout());
    final List<? extends NamedModule> cleanerNames = ModuleSelectionHelper.lookupImpls(AutomatonSimplifierFactory.class);
    final List<NamedModule> modelStrings = new ArrayList<NamedModule>();
    for (NamedModule name : cleanerNames) {
      if (!name.getName().equals(ChainedFactory.NAME)) {
        modelStrings.add(name);
      }
    }
    int i;
    for (i = 0; i < modelStrings.size(); i++) {
      final JLabel lbl = new javax.swing.JLabel();
      final JComboBox cmb = new JComboBox();
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
      cmb.setRenderer(new ProjectPropsComboRenderer(cmb.getRenderer()));
      cmb.addActionListener(new java.awt.event.ActionListener() {

        @Override
        public void actionPerformed(final ActionEvent evt) {
          desc.setText(htmlize(
                  ModuleSelectionHelper.lookupImpl(AutomatonSimplifierFactory.class,
                  ((NamedModule) cmb.getSelectedItem()).getName()).getUserModuleDescription()));
        }
      });
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

      desc.setText(htmlize(
              ModuleSelectionHelper.lookupImpl(AutomatonSimplifierFactory.class,
              ((NamedModule) cmb.getSelectedItem()).getName()).getUserModuleDescription()));
    }
    if (i == 0) {
      final JTextPane noImplInfo = new JTextPane();
      noImplInfo.setEditable(false);
      noImplInfo.setFocusable(false);
      noImplInfo.setOpaque(false);

      noImplInfo.setText("No implementation, other than myself, of AutomatonSimplifierFactory found.");
      add(noImplInfo);
    }
    for (i = 0; i < dynamicComponents.keySet().size(); ++i) {
      dynamicComponents.get(i).setSelectedItem(ModuleSelectionHelper.lookupImpl(AutomatonSimplifierFactory.class, ChainedFactory.PROPERTIES_SIMPLIFIER_DEFAULT));
    }
    final String _count = properties.getProperty(ChainedFactory.PROPERTIES_COUNT, "notJebHojid4");
    if ("notJebHojid4".equals(_count)) {
      dynamicComponents.get(0).setSelectedItem(ModuleSelectionHelper.lookupImpl(AutomatonSimplifierFactory.class, GreedyMDLFactory.NAME));
      dynamicComponents.get(1).setSelectedItem(ModuleSelectionHelper.lookupImpl(AutomatonSimplifierFactory.class, DefectiveMDLFactory.NAME));
    } else {
      int count;
      try {
        count = Integer.valueOf(_count);
      } catch (NumberFormatException e) {
        count = 0;
      }
      for (i = 0; i < count; i++) {
        dynamicComponents.get(i).setSelectedItem(
                ModuleSelectionHelper.lookupImpl(
                AutomatonSimplifierFactory.class,
                properties.getProperty(ChainedFactory.PROPERTIES_PREFIX + i, ChainedFactory.PROPERTIES_SIMPLIFIER_DEFAULT)));
      }
    }
  }

  @Override
  public void store() {
    for (Integer i : dynamicComponents.keySet()) {
      properties.setProperty(ChainedFactory.PROPERTIES_PREFIX + i,
              ((NamedModule) dynamicComponents.get(i).getSelectedItem()).getName());
    }
    properties.setProperty(ChainedFactory.PROPERTIES_COUNT, String.valueOf(dynamicComponents.keySet().size()));
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  // End of variables declaration//GEN-END:variables
}
