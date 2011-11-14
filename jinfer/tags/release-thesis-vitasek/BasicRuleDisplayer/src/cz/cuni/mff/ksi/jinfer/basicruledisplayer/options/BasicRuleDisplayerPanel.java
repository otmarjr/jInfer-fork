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
package cz.cuni.mff.ksi.jinfer.basicruledisplayer.options;

import cz.cuni.mff.ksi.jinfer.basicruledisplayer.logic.Utils;
import java.awt.Color;
import java.util.prefs.Preferences;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import org.openide.util.NbPreferences;

/**
 * Options panel of the Rule Displayer module.
 *
 * @author vektor
 */
@SuppressWarnings({"PMD.SingularField", "PMD.MethodArgumentCouldBeFinal", "PMD.UnusedFormalParameter"})
public final class BasicRuleDisplayerPanel extends JPanel {

  private static final long serialVersionUID = 1211214L;

  public static final String MAX_PANELS_PROP = "max.panels";
  public static final int MAX_PANELS_DEFAULT = 8;
  public static final String NESTING_LEVEL_PROP = "nesting.level";
  public static final int NESTING_LEVEL_DEFAULT = 25;
  public static final String MAX_RULES_PROP = "max.rules";
  public static final int MAX_RULES_DEFAULT = 50;
  public static final String MARGIN_PROP = "margin";
  public static final int MARGIN_DEFAULT = 2;
  public static final String COLOR_ELEMENT_PROP = "element.color";
  public static final Color COLOR_ELEMENT_DEFAULT = Color.gray;
  public static final String COLOR_ATTRIBUTE_PROP = "attribute.color";
  public static final Color COLOR_ATTRIBUTE_DEFAULT = Color.blue;
  public static final String COLOR_SIMPLE_DATA_PROP = "simple.data.color";
  public static final Color COLOR_SIMPLE_DATA_DEFAULT = Color.black;
  public static final String COLOR_CONCATENATION_PROP = "concatenation.color";
  public static final Color COLOR_CONCATENATION_DEFAULT = Color.red;
  public static final String COLOR_ALTERNATION_PROP = "alternation.color";
  public static final Color COLOR_ALTERNATION_DEFAULT = Color.yellow;

  public BasicRuleDisplayerPanel() {
    super();
    initComponents();
  }

  @SuppressWarnings("PMD")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    panelBehaviour = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    nestingLevel = new javax.swing.JSpinner();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    maxRules = new javax.swing.JSpinner();
    jLabel4 = new javax.swing.JLabel();
    jLabel11 = new javax.swing.JLabel();
    margin = new javax.swing.JSpinner();
    jLabel12 = new javax.swing.JLabel();
    jLabel13 = new javax.swing.JLabel();
    maxPanels = new javax.swing.JSpinner();
    jLabel14 = new javax.swing.JLabel();
    panelFill1 = new javax.swing.JPanel();
    panelColors = new javax.swing.JPanel();
    jLabel5 = new javax.swing.JLabel();
    panelElement = new javax.swing.JPanel();
    jLabel6 = new javax.swing.JLabel();
    panelAttribute = new javax.swing.JPanel();
    jLabel7 = new javax.swing.JLabel();
    panelSimpleData = new javax.swing.JPanel();
    reset = new javax.swing.JButton();
    jLabel9 = new javax.swing.JLabel();
    jLabel10 = new javax.swing.JLabel();
    panelConcatenation = new javax.swing.JPanel();
    panelAlternation = new javax.swing.JPanel();
    panelFill2 = new javax.swing.JPanel();
    fill = new javax.swing.JPanel();

    setLayout(new java.awt.GridBagLayout());

    panelBehaviour.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(BasicRuleDisplayerPanel.class, "BasicRuleDisplayerPanel.panelBehaviour.border.title"))); // NOI18N
    panelBehaviour.setLayout(new java.awt.GridBagLayout());

    org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(BasicRuleDisplayerPanel.class, "BasicRuleDisplayerPanel.jLabel1.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    panelBehaviour.add(jLabel1, gridBagConstraints);

    nestingLevel.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
    nestingLevel.setPreferredSize(new java.awt.Dimension(60, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelBehaviour.add(nestingLevel, gridBagConstraints);

    jLabel2.setFont(jLabel2.getFont().deriveFont((jLabel2.getFont().getStyle() | java.awt.Font.ITALIC), 10));
    org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(BasicRuleDisplayerPanel.class, "BasicRuleDisplayerPanel.jLabel2.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 2);
    panelBehaviour.add(jLabel2, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(BasicRuleDisplayerPanel.class, "BasicRuleDisplayerPanel.jLabel3.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    panelBehaviour.add(jLabel3, gridBagConstraints);

    maxRules.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
    maxRules.setPreferredSize(new java.awt.Dimension(60, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelBehaviour.add(maxRules, gridBagConstraints);

    jLabel4.setFont(jLabel4.getFont().deriveFont((jLabel4.getFont().getStyle() | java.awt.Font.ITALIC), 10));
    org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(BasicRuleDisplayerPanel.class, "BasicRuleDisplayerPanel.jLabel4.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 2);
    panelBehaviour.add(jLabel4, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(jLabel11, org.openide.util.NbBundle.getMessage(BasicRuleDisplayerPanel.class, "BasicRuleDisplayerPanel.jLabel11.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    panelBehaviour.add(jLabel11, gridBagConstraints);

    margin.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(2), Integer.valueOf(1), null, Integer.valueOf(1)));
    margin.setPreferredSize(new java.awt.Dimension(60, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelBehaviour.add(margin, gridBagConstraints);

    jLabel12.setFont(jLabel12.getFont().deriveFont((jLabel12.getFont().getStyle() | java.awt.Font.ITALIC), 10));
    org.openide.awt.Mnemonics.setLocalizedText(jLabel12, org.openide.util.NbBundle.getMessage(BasicRuleDisplayerPanel.class, "BasicRuleDisplayerPanel.jLabel12.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 2);
    panelBehaviour.add(jLabel12, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(jLabel13, org.openide.util.NbBundle.getMessage(BasicRuleDisplayerPanel.class, "BasicRuleDisplayerPanel.jLabel13.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    panelBehaviour.add(jLabel13, gridBagConstraints);

    maxPanels.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
    maxPanels.setPreferredSize(new java.awt.Dimension(60, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelBehaviour.add(maxPanels, gridBagConstraints);

    jLabel14.setFont(jLabel14.getFont().deriveFont((jLabel14.getFont().getStyle() | java.awt.Font.ITALIC), 10));
    org.openide.awt.Mnemonics.setLocalizedText(jLabel14, org.openide.util.NbBundle.getMessage(BasicRuleDisplayerPanel.class, "BasicRuleDisplayerPanel.jLabel14.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 2);
    panelBehaviour.add(jLabel14, gridBagConstraints);

    javax.swing.GroupLayout panelFill1Layout = new javax.swing.GroupLayout(panelFill1);
    panelFill1.setLayout(panelFill1Layout);
    panelFill1Layout.setHorizontalGroup(
      panelFill1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 67, Short.MAX_VALUE)
    );
    panelFill1Layout.setVerticalGroup(
      panelFill1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 96, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridheight = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    panelBehaviour.add(panelFill1, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    add(panelBehaviour, gridBagConstraints);

    panelColors.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(BasicRuleDisplayerPanel.class, "BasicRuleDisplayerPanel.panelColors.border.title_1"))); // NOI18N
    panelColors.setLayout(new java.awt.GridBagLayout());

    org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(BasicRuleDisplayerPanel.class, "BasicRuleDisplayerPanel.jLabel5.text_1")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    panelColors.add(jLabel5, gridBagConstraints);

    panelElement.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
    panelElement.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    panelElement.setMaximumSize(new java.awt.Dimension(20, 20));
    panelElement.setMinimumSize(new java.awt.Dimension(20, 20));
    panelElement.setPreferredSize(new java.awt.Dimension(20, 20));
    panelElement.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        panelElementMouseClicked(evt);
      }
    });

    javax.swing.GroupLayout panelElementLayout = new javax.swing.GroupLayout(panelElement);
    panelElement.setLayout(panelElementLayout);
    panelElementLayout.setHorizontalGroup(
      panelElementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 18, Short.MAX_VALUE)
    );
    panelElementLayout.setVerticalGroup(
      panelElementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 18, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelColors.add(panelElement, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(BasicRuleDisplayerPanel.class, "BasicRuleDisplayerPanel.jLabel6.text_1")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    panelColors.add(jLabel6, gridBagConstraints);

    panelAttribute.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
    panelAttribute.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    panelAttribute.setMaximumSize(new java.awt.Dimension(20, 20));
    panelAttribute.setMinimumSize(new java.awt.Dimension(20, 20));
    panelAttribute.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        panelAttributeMouseClicked(evt);
      }
    });

    javax.swing.GroupLayout panelAttributeLayout = new javax.swing.GroupLayout(panelAttribute);
    panelAttribute.setLayout(panelAttributeLayout);
    panelAttributeLayout.setHorizontalGroup(
      panelAttributeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 18, Short.MAX_VALUE)
    );
    panelAttributeLayout.setVerticalGroup(
      panelAttributeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 18, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelColors.add(panelAttribute, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(BasicRuleDisplayerPanel.class, "BasicRuleDisplayerPanel.jLabel7.text_1")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    panelColors.add(jLabel7, gridBagConstraints);

    panelSimpleData.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
    panelSimpleData.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    panelSimpleData.setMaximumSize(new java.awt.Dimension(20, 20));
    panelSimpleData.setMinimumSize(new java.awt.Dimension(20, 20));
    panelSimpleData.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        panelSimpleDataMouseClicked(evt);
      }
    });

    javax.swing.GroupLayout panelSimpleDataLayout = new javax.swing.GroupLayout(panelSimpleData);
    panelSimpleData.setLayout(panelSimpleDataLayout);
    panelSimpleDataLayout.setHorizontalGroup(
      panelSimpleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 18, Short.MAX_VALUE)
    );
    panelSimpleDataLayout.setVerticalGroup(
      panelSimpleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 18, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelColors.add(panelSimpleData, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(reset, org.openide.util.NbBundle.getMessage(BasicRuleDisplayerPanel.class, "BasicRuleDisplayerPanel.reset.text_1")); // NOI18N
    reset.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        resetActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 4;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelColors.add(reset, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(jLabel9, org.openide.util.NbBundle.getMessage(BasicRuleDisplayerPanel.class, "BasicRuleDisplayerPanel.jLabel9.text_1")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    panelColors.add(jLabel9, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(jLabel10, org.openide.util.NbBundle.getMessage(BasicRuleDisplayerPanel.class, "BasicRuleDisplayerPanel.jLabel10.text_1")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    panelColors.add(jLabel10, gridBagConstraints);

    panelConcatenation.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
    panelConcatenation.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    panelConcatenation.setMaximumSize(new java.awt.Dimension(20, 20));
    panelConcatenation.setMinimumSize(new java.awt.Dimension(20, 20));
    panelConcatenation.setPreferredSize(new java.awt.Dimension(20, 20));
    panelConcatenation.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        panelConcatenationMouseClicked(evt);
      }
    });

    javax.swing.GroupLayout panelConcatenationLayout = new javax.swing.GroupLayout(panelConcatenation);
    panelConcatenation.setLayout(panelConcatenationLayout);
    panelConcatenationLayout.setHorizontalGroup(
      panelConcatenationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 18, Short.MAX_VALUE)
    );
    panelConcatenationLayout.setVerticalGroup(
      panelConcatenationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 18, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelColors.add(panelConcatenation, gridBagConstraints);

    panelAlternation.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
    panelAlternation.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    panelAlternation.setMaximumSize(new java.awt.Dimension(20, 20));
    panelAlternation.setMinimumSize(new java.awt.Dimension(20, 20));
    panelAlternation.setPreferredSize(new java.awt.Dimension(20, 20));
    panelAlternation.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        panelAlternationMouseClicked(evt);
      }
    });

    javax.swing.GroupLayout panelAlternationLayout = new javax.swing.GroupLayout(panelAlternation);
    panelAlternation.setLayout(panelAlternationLayout);
    panelAlternationLayout.setHorizontalGroup(
      panelAlternationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 18, Short.MAX_VALUE)
    );
    panelAlternationLayout.setVerticalGroup(
      panelAlternationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 18, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelColors.add(panelAlternation, gridBagConstraints);

    javax.swing.GroupLayout panelFill2Layout = new javax.swing.GroupLayout(panelFill2);
    panelFill2.setLayout(panelFill2Layout);
    panelFill2Layout.setHorizontalGroup(
      panelFill2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 178, Short.MAX_VALUE)
    );
    panelFill2Layout.setVerticalGroup(
      panelFill2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 81, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 5;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridheight = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    panelColors.add(panelFill2, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    add(panelColors, gridBagConstraints);

    javax.swing.GroupLayout fillLayout = new javax.swing.GroupLayout(fill);
    fill.setLayout(fillLayout);
    fillLayout.setHorizontalGroup(
      fillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 608, Short.MAX_VALUE)
    );
    fillLayout.setVerticalGroup(
      fillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 33, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weighty = 1.0;
    add(fill, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void panelElementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelElementMouseClicked
    final Color c = JColorChooser.showDialog(BasicRuleDisplayerPanel.this, "Choose the element color", panelElement.getBackground());
    if (c != null) {
      panelElement.setBackground(c);
    }
  }//GEN-LAST:event_panelElementMouseClicked

  private void panelAttributeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAttributeMouseClicked
    final Color c = JColorChooser.showDialog(BasicRuleDisplayerPanel.this, "Choose the attribute color", panelAttribute.getBackground());
    if (c != null) {
      panelAttribute.setBackground(c);
    }
  }//GEN-LAST:event_panelAttributeMouseClicked

  private void panelSimpleDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelSimpleDataMouseClicked
    final Color c = JColorChooser.showDialog(BasicRuleDisplayerPanel.this, "Choose the simple data color", panelSimpleData.getBackground());
    if (c != null) {
      panelSimpleData.setBackground(c);
    }
  }//GEN-LAST:event_panelSimpleDataMouseClicked

  private void resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetActionPerformed
    panelElement.setBackground(COLOR_ELEMENT_DEFAULT);
    panelAttribute.setBackground(COLOR_ATTRIBUTE_DEFAULT);
    panelSimpleData.setBackground(COLOR_SIMPLE_DATA_DEFAULT);
    panelConcatenation.setBackground(COLOR_CONCATENATION_DEFAULT);
    panelAlternation.setBackground(COLOR_ALTERNATION_DEFAULT);
  }//GEN-LAST:event_resetActionPerformed

  private void panelConcatenationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelConcatenationMouseClicked
    final Color c = JColorChooser.showDialog(BasicRuleDisplayerPanel.this, "Choose the concatenation color", panelConcatenation.getBackground());
    if (c != null) {
      panelConcatenation.setBackground(c);
    }
  }//GEN-LAST:event_panelConcatenationMouseClicked

  private void panelAlternationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAlternationMouseClicked
    final Color c = JColorChooser.showDialog(BasicRuleDisplayerPanel.this, "Choose the alternation color", panelAlternation.getBackground());
    if (c != null) {
      panelAlternation.setBackground(c);
    }
  }//GEN-LAST:event_panelAlternationMouseClicked

  public void load() {
    final Preferences p = NbPreferences.forModule(BasicRuleDisplayerPanel.class);
    maxPanels.setValue(p.getInt(MAX_PANELS_PROP, MAX_PANELS_DEFAULT));
    nestingLevel.setValue(p.getInt(NESTING_LEVEL_PROP, NESTING_LEVEL_DEFAULT));
    maxRules.setValue(p.getInt(MAX_RULES_PROP, MAX_RULES_DEFAULT));
    margin.setValue(p.getInt(MARGIN_PROP, MARGIN_DEFAULT));

    panelElement.setBackground(Utils.getColorElement());
    panelAttribute.setBackground(Utils.getColorAttribute());
    panelSimpleData.setBackground(Utils.getColorSimpleData());
    panelConcatenation.setBackground(Utils.getColorConcatenation());
    panelAlternation.setBackground(Utils.getColorAlternation());
  }

  public void store() {
    final Preferences p = NbPreferences.forModule(BasicRuleDisplayerPanel.class);
    p.putInt(MAX_PANELS_PROP, ((Integer) maxPanels.getValue()).intValue());
    p.putInt(NESTING_LEVEL_PROP, ((Integer) nestingLevel.getValue()).intValue());
    p.putInt(MAX_RULES_PROP, ((Integer) maxRules.getValue()).intValue());
    p.putInt(MARGIN_PROP, ((Integer) margin.getValue()).intValue());

    p.put(COLOR_ELEMENT_PROP, String.valueOf(panelElement.getBackground().getRGB()));
    p.put(COLOR_ATTRIBUTE_PROP, String.valueOf(panelAttribute.getBackground().getRGB()));
    p.put(COLOR_SIMPLE_DATA_PROP, String.valueOf(panelSimpleData.getBackground().getRGB()));
    p.put(COLOR_CONCATENATION_PROP, String.valueOf(panelConcatenation.getBackground().getRGB()));
    p.put(COLOR_ALTERNATION_PROP, String.valueOf(panelAlternation.getBackground().getRGB()));
  }

  public boolean valid() {
    return true;
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel fill;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel10;
  private javax.swing.JLabel jLabel11;
  private javax.swing.JLabel jLabel12;
  private javax.swing.JLabel jLabel13;
  private javax.swing.JLabel jLabel14;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel9;
  private javax.swing.JSpinner margin;
  private javax.swing.JSpinner maxPanels;
  private javax.swing.JSpinner maxRules;
  private javax.swing.JSpinner nestingLevel;
  private javax.swing.JPanel panelAlternation;
  private javax.swing.JPanel panelAttribute;
  private javax.swing.JPanel panelBehaviour;
  private javax.swing.JPanel panelColors;
  private javax.swing.JPanel panelConcatenation;
  private javax.swing.JPanel panelElement;
  private javax.swing.JPanel panelFill1;
  private javax.swing.JPanel panelFill2;
  private javax.swing.JPanel panelSimpleData;
  private javax.swing.JButton reset;
  // End of variables declaration//GEN-END:variables
}
