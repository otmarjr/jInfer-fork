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
 *
 * @author sviro
 */
public class Path {
  
  private String path;
  
  public Path() {}

  public Path(final String path) {
    if (path == null || path.isEmpty()) {
      throw new IllegalArgumentException("Path has to be non-null and not empty.");
    }
    this.path = path;
    
  }
  
  public boolean isStringPath() {
    return path.endsWith("text()") || path.contains("@");
  }
  
  public boolean isElementPath() {
    return !isStringPath();
  }

  public String getPathValue() {
    return path;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof Path)) {
      return false;
    }
    
    Path path1 = (Path) obj;
    
    return this.getPathValue().equals(path1.getPathValue());
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + (this.path != null ? this.path.hashCode() : 0);
    return hash;
  }
  
  
}
