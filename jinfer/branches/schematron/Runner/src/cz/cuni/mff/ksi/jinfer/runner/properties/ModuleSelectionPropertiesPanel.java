/*
 *  Copyright (C) 2010 sviro
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
package cz.cuni.mff.ksi.jinfer.runner.properties;

import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.IGGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.NonGrammaticalInputProcessor;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.SchemaGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.Simplifier;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.objects.ProjectPropsComboRenderer;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;

/**
 * Properties panel of the Runner module.
 *
 * @author sviro
 */
@SuppressWarnings("PMD.SingularField")
public class ModuleSelectionPropertiesPanel extends AbstractPropertiesPanel {

  private static final long serialVersionUID = 784463434L;
  public static final String NAME = "moduleselector";
  public static final String DISPLAY_NAME = "Module selection";

  public static final String IGG_PROP = "initialgrammar";
  public static final String IGG_DEFAULT = "Basic_IG_Generator";
  public static final String SIMPLIFIER_PROP = "simplifier";
  public static final String SIMPLIFIER_DEFAULT = "TwoStepSimplifier";
  public static final String SCHEMAGEN_PROP = "schemagenerator";
  public static final String SCHEMAGEN_DEFAULT = "Basic_XSD_exporter";
  public static final String NON_GRAMMATICAL_INPUT_PROCESSOR_PROP = "nongrammaticalinputprocessor";
  public static final String NON_GRAMMATICAL_INPUT_PROCESSOR_DEFAULT = "Basic_XQuery_Processor";

  public ModuleSelectionPropertiesPanel(final Properties properties) {
    super(properties);
    initComponents();
  }

  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        initialGrammar = new javax.swing.JComboBox();
        simplifier = new javax.swing.JComboBox();
        schemaGenerator = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        nonGrammaticalInputProcessor = new javax.swing.JComboBox();

        setLayout(new java.awt.GridBagLayout());

        initialGrammar.setRenderer(new ProjectPropsComboRenderer(initialGrammar.getRenderer()));
        initialGrammar.setMinimumSize(new java.awt.Dimension(150, 22));
        initialGrammar.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(initialGrammar, gridBagConstraints);

        simplifier.setRenderer(new ProjectPropsComboRenderer(simplifier.getRenderer()));
        simplifier.setMinimumSize(new java.awt.Dimension(150, 22));
        simplifier.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(simplifier, gridBagConstraints);

        schemaGenerator.setRenderer(new ProjectPropsComboRenderer(schemaGenerator.getRenderer()));
        schemaGenerator.setMinimumSize(new java.awt.Dimension(150, 22));
        schemaGenerator.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(schemaGenerator, gridBagConstraints);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(ModuleSelectionPropertiesPanel.class, "ModuleSelectionPropertiesPanel.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        add(jLabel1, gridBagConstraints);

        jLabel2.setText(org.openide.util.NbBundle.getMessage(ModuleSelectionPropertiesPanel.class, "ModuleSelectionPropertiesPanel.jLabel2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        add(jLabel2, gridBagConstraints);

        jLabel3.setText(org.openide.util.NbBundle.getMessage(ModuleSelectionPropertiesPanel.class, "ModuleSelectionPropertiesPanel.jLabel3.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        add(jLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        add(jPanel1, gridBagConstraints);

        jPanel2.setMinimumSize(new java.awt.Dimension(0, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        add(jPanel2, gridBagConstraints);

        jLabel4.setText(org.openide.util.NbBundle.getMessage(ModuleSelectionPropertiesPanel.class, "ModuleSelectionPropertiesPanel.jLabel4.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        add(jLabel4, gridBagConstraints);

        nonGrammaticalInputProcessor.setRenderer(new ProjectPropsComboRenderer(nonGrammaticalInputProcessor.getRenderer()));
        nonGrammaticalInputProcessor.setMinimumSize(new java.awt.Dimension(150, 22));
        nonGrammaticalInputProcessor.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(nonGrammaticalInputProcessor, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

  @Override
  public final void load() {
    initialGrammar.setModel(new DefaultComboBoxModel(
            ModuleSelectionHelper.lookupImpls(IGGenerator.class).toArray()));
    simplifier.setModel(new DefaultComboBoxModel(
            ModuleSelectionHelper.lookupImpls(Simplifier.class).toArray()));
    schemaGenerator.setModel(new DefaultComboBoxModel(
            ModuleSelectionHelper.lookupImpls(SchemaGenerator.class).toArray()));
    nonGrammaticalInputProcessor.setModel(new DefaultComboBoxModel(
            ModuleSelectionHelper.lookupImpls(NonGrammaticalInputProcessor.class).toArray()));

    initialGrammar.setSelectedItem(ModuleSelectionHelper.lookupImpl(IGGenerator.class, properties.getProperty(IGG_PROP, IGG_DEFAULT)));
    simplifier.setSelectedItem(ModuleSelectionHelper.lookupImpl(Simplifier.class, properties.getProperty(SIMPLIFIER_PROP, SIMPLIFIER_DEFAULT)));
    schemaGenerator.setSelectedItem(ModuleSelectionHelper.lookupImpl(SchemaGenerator.class, properties.getProperty(SCHEMAGEN_PROP, SCHEMAGEN_DEFAULT)));
    nonGrammaticalInputProcessor.setSelectedItem(ModuleSelectionHelper.lookupImpl(NonGrammaticalInputProcessor.class, properties.getProperty(NON_GRAMMATICAL_INPUT_PROCESSOR_PROP, NON_GRAMMATICAL_INPUT_PROCESSOR_DEFAULT)));
  }

  @Override
  public void store() {
    properties.setProperty(IGG_PROP,
            ((NamedModule) initialGrammar.getSelectedItem()).getName());
    properties.setProperty(SIMPLIFIER_PROP,
            ((NamedModule) simplifier.getSelectedItem()).getName());
    properties.setProperty(SCHEMAGEN_PROP,
            ((NamedModule) schemaGenerator.getSelectedItem()).getName());
    properties.setProperty(NON_GRAMMATICAL_INPUT_PROCESSOR_PROP,
            ((NamedModule) nonGrammaticalInputProcessor.getSelectedItem()).getName());
  }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox initialGrammar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JComboBox nonGrammaticalInputProcessor;
    private javax.swing.JComboBox schemaGenerator;
    private javax.swing.JComboBox simplifier;
    // End of variables declaration//GEN-END:variables
}
