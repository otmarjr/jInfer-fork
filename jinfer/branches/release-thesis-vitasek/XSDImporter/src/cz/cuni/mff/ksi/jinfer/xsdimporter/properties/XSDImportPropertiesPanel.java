/*
 *  Copyright (C) 2010 reseto
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

package cz.cuni.mff.ksi.jinfer.xsdimporter.properties;

import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.base.objects.ProjectPropsComboRenderer;
import cz.cuni.mff.ksi.jinfer.base.utils.LogLevels;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.xsdimporter.interfaces.XSDParser;
import java.util.List;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;
import org.apache.log4j.lf5.LogLevel;

/**
 * Properties panel of the XSD Importer module.
 * The "Parser" setting selects whether SAX or DOM parser is used.
 * The "Log level" setting determines the minimal priority for a message from this module to be logged.
 * If "Stop on error" checkbox is set, parsing halts on the first error;
 * if it's not set, then the file that caused the error is simply skipped and not imported.
 * The "Verbose" setting enables logging of some additional messages like imported rules etc.
 *
 * @author reseto
 */
@SuppressWarnings("PMD.SingularField")
public class XSDImportPropertiesPanel extends AbstractPropertiesPanel {

  private static final long serialVersionUID = 35344234;

  /**
   * Internal name of the panel.
   */
  public static final String PANEL_NAME = "XSDImport";
  /**
   * Name of the panel as displayed in project properties.
   */
  public static final String PANEL_DISPLAY_NAME = "XSD Import";
  /**
   * Sorting priority of the panel.
   */
  public static final int    PANEL_PRIORITY = 20000;
  /**
   * Property name of the selected parser.
   */
  public static final String PARSER_PROP = "parser";
  public static final String PARSER_DEFAULT = "DOMParser";
  /**
   * Property name of the stop on error setting.
   */
  public static final String STOP_ON_ERROR_PROP = "stop.on.error";
  public static final String STOP_ON_ERROR_DEFAULT = "true";
  /**
   * Property name of the verbose info setting.
   */
  public static final String VERBOSE_INFO_PROP = "verbose.info";
  public static final String VERBOSE_INFO_DEFAULT = "false";
  /**
   * Property name of the selected logging level setting.
   */
  public static final String LOG_LEVEL_PROP = "log.level";
  public static final String LOG_LEVEL_DEFAULT = LogLevel.WARN.toString();

  /** Creates new form XSDImportPropertiesPanel */
  public XSDImportPropertiesPanel(final Properties properties) {
    super(properties);
    initComponents();
  }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("PMD")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    parserLabel = new javax.swing.JLabel();
    parserComboBox = new javax.swing.JComboBox();
    loglevelLabel = new javax.swing.JLabel();
    logLevelCombo = new javax.swing.JComboBox();
    stopLabel = new javax.swing.JLabel();
    stopOnError = new javax.swing.JCheckBox();
    verboseLabel = new javax.swing.JLabel();
    verbose = new javax.swing.JCheckBox();
    fillV = new javax.swing.JPanel();
    fillH = new javax.swing.JPanel();
    loglevelLabel2 = new javax.swing.JLabel();

    setMinimumSize(new java.awt.Dimension(500, 300));
    setPreferredSize(new java.awt.Dimension(500, 300));
    setLayout(new java.awt.GridBagLayout());

    parserLabel.setText(org.openide.util.NbBundle.getMessage(XSDImportPropertiesPanel.class, "XSDImportPropertiesPanel.parserLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(parserLabel, gridBagConstraints);

    parserComboBox.setMinimumSize(new java.awt.Dimension(100, 22));
    parserComboBox.setPreferredSize(new java.awt.Dimension(150, 22));
    parserComboBox.setRenderer(new ProjectPropsComboRenderer(parserComboBox.getRenderer()));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(parserComboBox, gridBagConstraints);

    loglevelLabel.setText(org.openide.util.NbBundle.getMessage(XSDImportPropertiesPanel.class, "XSDImportPropertiesPanel.loglevelLabel.text")); // NOI18N
    loglevelLabel.setToolTipText(org.openide.util.NbBundle.getMessage(XSDImportPropertiesPanel.class, "XSDImportPropertiesPanel.loglevelLabel.toolTipText")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(loglevelLabel, gridBagConstraints);

    logLevelCombo.setMaximumRowCount(12);
    logLevelCombo.setModel(LogLevels.getDefaultModel());
    logLevelCombo.setMinimumSize(new java.awt.Dimension(100, 22));
    logLevelCombo.setPreferredSize(new java.awt.Dimension(150, 22));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(logLevelCombo, gridBagConstraints);

    stopLabel.setText(org.openide.util.NbBundle.getMessage(XSDImportPropertiesPanel.class, "XSDImportPropertiesPanel.stopLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(stopLabel, gridBagConstraints);

    stopOnError.setSelected(true);
    stopOnError.setText(org.openide.util.NbBundle.getMessage(XSDImportPropertiesPanel.class, "XSDImportPropertiesPanel.stopOnError.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(stopOnError, gridBagConstraints);

    verboseLabel.setText(org.openide.util.NbBundle.getMessage(XSDImportPropertiesPanel.class, "XSDImportPropertiesPanel.verboseLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    add(verboseLabel, gridBagConstraints);

    verbose.setText(org.openide.util.NbBundle.getMessage(XSDImportPropertiesPanel.class, "XSDImportPropertiesPanel.verbose.text")); // NOI18N
    verbose.setToolTipText(org.openide.util.NbBundle.getMessage(XSDImportPropertiesPanel.class, "XSDImportPropertiesPanel.verbose.toolTipText")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(verbose, gridBagConstraints);

    javax.swing.GroupLayout fillVLayout = new javax.swing.GroupLayout(fillV);
    fillV.setLayout(fillVLayout);
    fillVLayout.setHorizontalGroup(
      fillVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 537, Short.MAX_VALUE)
    );
    fillVLayout.setVerticalGroup(
      fillVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 234, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.gridwidth = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weighty = 1.0;
    add(fillV, gridBagConstraints);

    javax.swing.GroupLayout fillHLayout = new javax.swing.GroupLayout(fillH);
    fillH.setLayout(fillHLayout);
    fillHLayout.setHorizontalGroup(
      fillHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 41, Short.MAX_VALUE)
    );
    fillHLayout.setVerticalGroup(
      fillHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 102, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridheight = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    add(fillH, gridBagConstraints);

    loglevelLabel2.setFont(loglevelLabel2.getFont().deriveFont((loglevelLabel2.getFont().getStyle() | java.awt.Font.ITALIC), 10));
    loglevelLabel2.setText(org.openide.util.NbBundle.getMessage(XSDImportPropertiesPanel.class, "XSDImportPropertiesPanel.loglevelLabel2.text")); // NOI18N
    loglevelLabel2.setToolTipText(org.openide.util.NbBundle.getMessage(XSDImportPropertiesPanel.class, "XSDImportPropertiesPanel.loglevelLabel2.toolTipText")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 2);
    add(loglevelLabel2, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  @Override
  public void store() {
    properties.setProperty(PARSER_PROP,
            ((NamedModule) parserComboBox.getSelectedItem()).getName());
    properties.setProperty(LOG_LEVEL_PROP,
            (String) logLevelCombo.getSelectedItem());
    properties.setProperty(STOP_ON_ERROR_PROP,
            Boolean.toString(stopOnError.isSelected()));
    properties.setProperty(VERBOSE_INFO_PROP,
            Boolean.toString(verbose.isSelected()));
  }

  @Override
  public void load() {
    final List<? extends NamedModule> names = ModuleSelectionHelper.lookupImpls(XSDParser.class);
    parserComboBox.setModel(new DefaultComboBoxModel(names.toArray()));

    parserComboBox.setSelectedItem(ModuleSelectionHelper.lookupImpl(XSDParser.class, properties.getProperty(PARSER_PROP, PARSER_DEFAULT)));

    logLevelCombo.setSelectedItem(properties.getProperty(LOG_LEVEL_PROP, LOG_LEVEL_DEFAULT));

    stopOnError.setSelected(
            Boolean.parseBoolean(properties.getProperty(STOP_ON_ERROR_PROP, STOP_ON_ERROR_DEFAULT)));
    verbose.setSelected(
            Boolean.parseBoolean(properties.getProperty(VERBOSE_INFO_PROP, VERBOSE_INFO_DEFAULT)));
  }


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel fillH;
  private javax.swing.JPanel fillV;
  private javax.swing.JComboBox logLevelCombo;
  private javax.swing.JLabel loglevelLabel;
  private javax.swing.JLabel loglevelLabel2;
  private javax.swing.JComboBox parserComboBox;
  private javax.swing.JLabel parserLabel;
  private javax.swing.JLabel stopLabel;
  private javax.swing.JCheckBox stopOnError;
  private javax.swing.JCheckBox verbose;
  private javax.swing.JLabel verboseLabel;
  // End of variables declaration//GEN-END:variables

}
