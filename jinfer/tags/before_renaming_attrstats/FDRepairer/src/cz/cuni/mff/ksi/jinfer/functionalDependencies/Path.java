/*
 * Copyright (C) 2011 sviro
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
package cz.cuni.mff.ksi.jinfer.functionalDependencies;

/**
 * Class representing path.
 * @author sviro
 */
public class Path {
  
  final private String pathValue;

  /**
   * Constructor which gets {@link String} representation of the path.
   * @param path String representation of the path.
   */
  public Path(final String path) {
    if (path == null || path.isEmpty()) {
      throw new IllegalArgumentException("Path has to be non-null and not empty.");
    }
    this.pathValue = path;
    
  }
  
  /**
   * Check if this path is a leaf path, i.e. it points to the string value 
   * of the element of attribute.
   * @return true if this path represents string value.
   */
  public boolean isStringPath() {
    return pathValue.endsWith("text()") || pathValue.contains("@");
  }
  
  /**
   * Chesk if this path point to the element, i.e. is not a string path.
   * @return true if this path represents element.
   */
  public boolean isElementPath() {
    return !isStringPath();
  }

  /**
   * Get the string representation of this path.
   * @return String representation of this path.
   */
  public String getPathValue() {
    return pathValue;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof Path)) {
      return false;
    }
    
    final Path path1 = (Path) obj;
    
    return this.getPathValue().equals(path1.getPathValue());
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + (this.pathValue != null ? this.pathValue.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString() {
    return pathValue;
  }
}