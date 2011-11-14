/*
 * Copyright (C) 2011 vektor
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
package cz.cuni.mff.ksi.jinfer.iss.gui;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Pair;
import cz.cuni.mff.ksi.jinfer.iss.options.ISSPanel;
import cz.cuni.mff.ksi.jinfer.iss.utils.Utils;
import cz.cuni.mff.ksi.jinfer.iss.tables.FlatModel;
import cz.cuni.mff.ksi.jinfer.iss.utils.MappingUtils;
import cz.cuni.mff.ksi.jinfer.iss.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.iss.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.iss.objects.AttributeTreeNode;
import cz.cuni.mff.ksi.jinfer.iss.tables.MappingsModel;
import cz.cuni.mff.ksi.jinfer.base.objects.ImmutablePair;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import org.openide.util.NbPreferences;
import static cz.cuni.mff.ksi.jinfer.iss.utils.Utils.NA;

/**
 * A panel containing a table representation of an AM model. There is a
 * "flat view" table, a list of all mappings and their images.
 *
 * @author vektor
 */
@SuppressWarnings("PMD.SingularField")
public class TableViewPanel extends JPanel {

  private static final long serialVersionUID = 18443L;

  private AMModel model;
  private final Map<AttributeMappingId, Pair<Double, Double>> cache =
          new HashMap<AttributeMappingId, Pair<Double, Double>>();

  public TableViewPanel() {
    super();
    initComponents();
    tableFlat.setSelectionModel(new DefaultListSelectionModel());
  }

  public void setModel(final AMModel model) {
    this.model = model;
    tableFlat.setModel(new FlatModel(model.getFlat()));
    tableMappings.setModel(new MappingsModel(model.getAMs().keySet()));
    tableMappings.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

      @Override
      public void valueChanged(final ListSelectionEvent e) {
        computeStats();
      }
    });
  }

  public void selectInTable(final List<AttributeTreeNode> nodes) {
    final DefaultListSelectionModel selectionModel = (DefaultListSelectionModel)tableFlat.getSelectionModel();
    selectionModel.clearSelection();
    final TableModel m = tableFlat.getModel();

    for (int i = 0; i < m.getRowCount(); i++) {
      final int row = tableFlat.convertRowIndexToModel(i);
      for (final AttributeTreeNode atn : nodes) {
        if (atn.getElementName().equals(m.getValueAt(row, 0))
                && atn.getAttributeName().equals(m.getValueAt(row, 1))
                && atn.getContent().contains((String)m.getValueAt(row, 2))) {
          selectionModel.addSelectionInterval(i, i);
          break;
        }
      }
    }
  }

  private void computeStats() {
    final MappingsModel tableModel = (MappingsModel)tableMappings.getModel();
    if (tableMappings.getSelectedRowCount() == 1) {
      final int row = tableMappings.convertRowIndexToModel(tableMappings.getSelectedRow());
      final AttributeMappingId mapping = tableModel.getObjectAt(row);
      showStats(mapping);
      final DefaultListModel imageModel = new DefaultListModel();
      for (final String value : model.getAMs().get(mapping).getImage()) {
        imageModel.addElement(value);
      }
      listValues.setModel(imageModel);
    }
    else {
      listValues.setModel(new DefaultListModel());
      support.setText(NA);
      coverage.setText(NA);
    }

    final List<AttributeMappingId> ids = new ArrayList<AttributeMappingId>(tableMappings.getSelectedRowCount());
    for (int row : tableMappings.getSelectedRows()) {
      ids.add(tableModel.getObjectAt(tableMappings.convertRowIndexToModel(row)));
    }

    if (!BaseUtils.isEmpty(ids)) {
      final double alpha = NbPreferences.forModule(ISSPanel.class).getFloat(ISSPanel.ALPHA_PROP, ISSPanel.ALPHA_DEFAULT);
      final double beta = NbPreferences.forModule(ISSPanel.class).getFloat(ISSPanel.BETA_PROP, ISSPanel.BETA_DEFAULT);

      idSet.setText(Utils.boolToString(MappingUtils.isIDset(ids, model)));
      weight.setText(Utils.FORMAT.format(model.weight(ids, alpha, beta)));
    }
    else {
      idSet.setText("no");
      weight.setText(NA);
    }
  }

  private void showStats(final AttributeMappingId targetMapping) {
    final Pair<Double, Double> value;
    if (cache.containsKey(targetMapping)) {
      value = cache.get(targetMapping);
    }
    else {
      value = new ImmutablePair<Double, Double>(
              Double.valueOf(model.support(targetMapping)),
              Double.valueOf(model.coverage(targetMapping)));
      cache.put(targetMapping, value);
    }
    support.setText(Utils.FORMAT.format(value.getFirst()));
    coverage.setText(Utils.FORMAT.format(value.getSecond()));
  }

  @SuppressWarnings({"unchecked", "PMD"})
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    splitHorizontal = new javax.swing.JSplitPane();
    panelFlat = new javax.swing.JPanel();
    paneFlat = new javax.swing.JScrollPane();
    tableFlat = new javax.swing.JTable();
    panelMappings = new javax.swing.JPanel();
    splitVertical = new javax.swing.JSplitPane();
    panelMappingsTable = new javax.swing.JPanel();
    paneMappings = new javax.swing.JScrollPane();
    tableMappings = new javax.swing.JTable();
    labelSupport = new javax.swing.JLabel();
    labelCoverage = new javax.swing.JLabel();
    labelIDset = new javax.swing.JLabel();
    labelWeight = new javax.swing.JLabel();
    support = new javax.swing.JTextField();
    idSet = new javax.swing.JTextField();
    coverage = new javax.swing.JTextField();
    weight = new javax.swing.JTextField();
    panelValues = new javax.swing.JPanel();
    paneValues = new javax.swing.JScrollPane();
    listValues = new javax.swing.JList();

    setPreferredSize(new java.awt.Dimension(466, 300));
    setLayout(new java.awt.BorderLayout());

    splitHorizontal.setDividerLocation(150);
    splitHorizontal.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
    splitHorizontal.setContinuousLayout(true);

    panelFlat.setBorder(javax.swing.BorderFactory.createTitledBorder("Flat View"));
    panelFlat.setLayout(new java.awt.BorderLayout());

    tableFlat.setAutoCreateRowSorter(true);
    tableFlat.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {
        {null, null, null},
        {null, null, null},
        {null, null, null},
        {null, null, null}
      },
      new String [] {
        "Element", "Attribute", "Value"
      }
    ) {
      Class[] types = new Class [] {
        java.lang.String.class, java.lang.String.class, java.lang.String.class
      };

      public Class getColumnClass(int columnIndex) {
        return types [columnIndex];
      }
    });
    paneFlat.setViewportView(tableFlat);

    panelFlat.add(paneFlat, java.awt.BorderLayout.CENTER);

    splitHorizontal.setTopComponent(panelFlat);

    panelMappings.setBorder(javax.swing.BorderFactory.createTitledBorder("Attribute Mappings"));
    panelMappings.setLayout(new java.awt.BorderLayout());

    splitVertical.setDividerLocation(400);
    splitVertical.setContinuousLayout(true);

    panelMappingsTable.setMinimumSize(new java.awt.Dimension(0, 0));
    panelMappingsTable.setPreferredSize(new java.awt.Dimension(300, 100));
    panelMappingsTable.setLayout(new java.awt.GridBagLayout());

    tableMappings.setAutoCreateRowSorter(true);
    tableMappings.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {
        {null, null},
        {null, null},
        {null, null},
        {null, null}
      },
      new String [] {
        "Element", "Attribute"
      }
    ) {
      Class[] types = new Class [] {
        java.lang.String.class, java.lang.String.class
      };
      boolean[] canEdit = new boolean [] {
        false, false
      };

      public Class getColumnClass(int columnIndex) {
        return types [columnIndex];
      }

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
      }
    });
    paneMappings.setViewportView(tableMappings);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 7;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelMappingsTable.add(paneMappings, gridBagConstraints);

    labelSupport.setFont(labelSupport.getFont());
    labelSupport.setText("Support:"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 2);
    panelMappingsTable.add(labelSupport, gridBagConstraints);

    labelCoverage.setFont(labelCoverage.getFont());
    labelCoverage.setText("Coverage:"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelMappingsTable.add(labelCoverage, gridBagConstraints);

    labelIDset.setFont(labelIDset.getFont());
    labelIDset.setText("Feasible ID set:"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelMappingsTable.add(labelIDset, gridBagConstraints);

    labelWeight.setFont(labelWeight.getFont());
    labelWeight.setText("Weight:"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelMappingsTable.add(labelWeight, gridBagConstraints);

    support.setEditable(false);
    support.setFont(support.getFont().deriveFont(support.getFont().getStyle() | java.awt.Font.BOLD));
    support.setText(NA);
    support.setBorder(null);
    support.setMaximumSize(null);
    support.setMinimumSize(new java.awt.Dimension(75, 14));
    support.setPreferredSize(new java.awt.Dimension(75, 14));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    panelMappingsTable.add(support, gridBagConstraints);

    idSet.setEditable(false);
    idSet.setFont(idSet.getFont().deriveFont(idSet.getFont().getStyle() | java.awt.Font.BOLD));
    idSet.setText(NA);
    idSet.setBorder(null);
    idSet.setMaximumSize(null);
    idSet.setMinimumSize(new java.awt.Dimension(75, 14));
    idSet.setPreferredSize(new java.awt.Dimension(75, 14));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    panelMappingsTable.add(idSet, gridBagConstraints);

    coverage.setEditable(false);
    coverage.setFont(coverage.getFont().deriveFont(coverage.getFont().getStyle() | java.awt.Font.BOLD));
    coverage.setText(NA);
    coverage.setBorder(null);
    coverage.setMaximumSize(null);
    coverage.setMinimumSize(new java.awt.Dimension(75, 14));
    coverage.setPreferredSize(new java.awt.Dimension(75, 14));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    panelMappingsTable.add(coverage, gridBagConstraints);

    weight.setEditable(false);
    weight.setFont(weight.getFont().deriveFont(weight.getFont().getStyle() | java.awt.Font.BOLD));
    weight.setText(NA);
    weight.setBorder(null);
    weight.setMaximumSize(null);
    weight.setMinimumSize(new java.awt.Dimension(75, 14));
    weight.setPreferredSize(new java.awt.Dimension(75, 14));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
    panelMappingsTable.add(weight, gridBagConstraints);

    splitVertical.setLeftComponent(panelMappingsTable);

    panelValues.setBorder(javax.swing.BorderFactory.createTitledBorder("Image"));
    panelValues.setPreferredSize(new java.awt.Dimension(0, 0));
    panelValues.setLayout(new java.awt.GridBagLayout());

    paneValues.setViewportView(listValues);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    panelValues.add(paneValues, gridBagConstraints);

    splitVertical.setRightComponent(panelValues);

    panelMappings.add(splitVertical, java.awt.BorderLayout.CENTER);

    splitHorizontal.setRightComponent(panelMappings);

    add(splitHorizontal, java.awt.BorderLayout.CENTER);
  }// </editor-fold>//GEN-END:initComponents
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTextField coverage;
  private javax.swing.JTextField idSet;
  private javax.swing.JLabel labelCoverage;
  private javax.swing.JLabel labelIDset;
  private javax.swing.JLabel labelSupport;
  private javax.swing.JLabel labelWeight;
  private javax.swing.JList listValues;
  private javax.swing.JScrollPane paneFlat;
  private javax.swing.JScrollPane paneMappings;
  private javax.swing.JScrollPane paneValues;
  private javax.swing.JPanel panelFlat;
  private javax.swing.JPanel panelMappings;
  private javax.swing.JPanel panelMappingsTable;
  private javax.swing.JPanel panelValues;
  private javax.swing.JSplitPane splitHorizontal;
  private javax.swing.JSplitPane splitVertical;
  private javax.swing.JTextField support;
  private javax.swing.JTable tableFlat;
  private javax.swing.JTable tableMappings;
  private javax.swing.JTextField weight;
  // End of variables declaration//GEN-END:variables
}
