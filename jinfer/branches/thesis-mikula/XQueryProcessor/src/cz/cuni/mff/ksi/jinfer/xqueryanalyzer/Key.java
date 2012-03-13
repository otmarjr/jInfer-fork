/*
 * Copyright (C) 2012 rio
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
package cz.cuni.mff.ksi.jinfer.xqueryanalyzer;

import cz.cuni.mff.ksi.jinfer.base.xqueryprocessor.types.PathType;

/**
 * TODO rio comment
 * @author rio
 */
public class Key {
  
  private final PathType contextPath;
  private final PathType targetPath;
  private final PathType keyPath;
  
  public Key(final PathType contextPath, final PathType targetPath, final PathType keyPath) {
    this.contextPath = contextPath;
    this.targetPath = targetPath;
    this.keyPath = keyPath;
  }
  
  public Key(final PathType targetPath, final PathType keyPath) {
    this.contextPath = null;
    this.targetPath = targetPath;
    this.keyPath = keyPath;
  }
  
  public boolean isPossitive() {
    return true;
  }

  public PathType getContextPath() {
    return contextPath;
  }

  public PathType getKeyPath() {
    return keyPath;
  }

  public PathType getTargetPath() {
    return targetPath;
  }
  
  

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    
    if (!(obj instanceof Key)) { 
      return false;
    }
    
    final Key k = (Key)obj;
    
    if (contextPath == null) {
      if (k.contextPath != null) {
        return false;
      }
    } else {
      if (!contextPath.equals(k.contextPath)) {
        return false;
      }
    }
    
    if (!targetPath.equals(k.targetPath)) {
      return false;
    }
    
    if (!keyPath.equals(k.keyPath)) {
      return false;
    }
    
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 37 * hash + (this.contextPath != null ? this.contextPath.hashCode() : 0);
    hash = 37 * hash + (this.targetPath != null ? this.targetPath.hashCode() : 0);
    hash = 37 * hash + (this.keyPath != null ? this.keyPath.hashCode() : 0);
    return hash;
  }
  
  
}
