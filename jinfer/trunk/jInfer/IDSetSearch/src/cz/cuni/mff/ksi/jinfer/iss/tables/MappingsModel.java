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
package cz.cuni.mff.ksi.jinfer.iss.tables;

import cz.cuni.mff.ksi.jinfer.iss.objects.AttributeMappingId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 * Table model for a list of attribute mapping IDs.
 *
 * @author vektor
 */
public class MappingsModel extends DefaultTableModel {

  private static final long serialVersionUID = 1874512L;

  private final List<AttributeMappingId> data;

  public MappingsModel(final Collection<AttributeMappingId> data) {
    super(data.size(), 2);
    this.data = new ArrayList<AttributeMappingId>(data);
  }

  @Override
  public int getColumnCount() {
    return 2;
  }

  @Override
  public String getColumnName(final int column) {
    switch (column) {
      case 0:
        return "Element";
      case 1:
        return "Attribute";
      default:
        throw new IllegalArgumentException("Unkown column: " + column);
    }
  }

  @Override
  public int getRowCount() {
    if (data == null) {
      return 0;
    }
    return data.size();
  }

  @Override
  public Object getValueAt(final int row, final int column) {
    final AttributeMappingId value = data.get(row);
    switch (column) {
      case 0:
        return value.getElement();
      case 1:
        return value.getAttribute();
      default:
        throw new IllegalArgumentException("Unkown column: " + column);
    }
  }

  @Override
  public boolean isCellEditable(final int row, final int column) {
    return false;
  }

  public AttributeMappingId getObjectAt(final int row) {
    return data.get(row);
  }

}
