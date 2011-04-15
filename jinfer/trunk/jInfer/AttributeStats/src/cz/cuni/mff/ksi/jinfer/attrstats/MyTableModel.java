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
package cz.cuni.mff.ksi.jinfer.attrstats;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * An implementation of {@link TableModel} for use in {@link StatisticsPanel}.
 *
 * @author vektor
 */
class MyTableModel extends DefaultTableModel {

  private static final long serialVersionUID = 78974631624L;
  
  private final List<Triplet> model;

  public MyTableModel(final List<Triplet> model) {
    super(model.size(), 3);
    if (model == null) {
      throw new IllegalArgumentException("Model must be not null.");
    }
    this.model = model;
  }

  @Override
  public int getColumnCount() {
    return 3;
  }

  @Override
  public String getColumnName(final int column) {
    switch (column) {
      case 0:
        return "Element";
      case 1:
        return "Attribute";
      case 2:
        return "Value";
      default:
        throw new IllegalArgumentException("Unkown column: " + column);
    }
  }

  @Override
  public int getRowCount() {
    if (model == null) {
      return 0;
    }
    return model.size();
  }

  @Override
  public Object getValueAt(final int row, final int column) {
    final Triplet value = model.get(row);
    switch (column) {
      case 0:
        return value.getElement();
      case 1:
        return value.getAttribute();
      case 2:
        return value.getValue();
      default:
        throw new IllegalArgumentException("Unkown column: " + column);
    }
  }

  @Override
  public boolean isCellEditable(final int row, final int column) {
    return false;
  }

}
