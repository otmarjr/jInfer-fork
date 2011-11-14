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
package cz.cuni.mff.ksi.jinfer.base.objects;

/**
 * Folder type enum for input folder.
 * @author sviro
 */
public enum FolderType {

  DOCUMENT("Document"),
  SCHEMA("Schema"),
  QUERY("Query"),
  FD("FD");
  
  private final String name;

  private FolderType(final String name) {
    this.name = name;
  }

  /**
   * Get String representation of particular Folder type.
   * @return String representation of particular Folder type.
   */
  public String getName() {
    return name;
  }
}
