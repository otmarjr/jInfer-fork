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
package cz.cuni.mff.ksi.jinfer.base.objects.xquery.keys;

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.NormalizedPathType;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.PathType;

/**
 * This class represents keys using (C,P,{L}) definition. For details, refer
 * to the PDF documentation.
 * @author rio
 */
public class Key {
  
  private final NormalizedPathType contextPath;
  private final NormalizedPathType targetPath;
  private final NormalizedPathType keyPath;
  
  public Key(final NormalizedPathType contextPath, final NormalizedPathType targetPath, final NormalizedPathType keyPath) {
    this.contextPath = contextPath;
    this.targetPath = targetPath;
    this.keyPath = keyPath;
  }
  
  public Key(final NormalizedPathType targetPath, final NormalizedPathType keyPath) {
    this(null, targetPath, keyPath);
  }
  
  public Key(final PathType contextPath, final PathType targetPath, final PathType keyPath) {
    if (contextPath != null) {
      this.contextPath = new NormalizedPathType(contextPath);
    } else {
      this.contextPath = null;
    }
    
    this.targetPath = new NormalizedPathType(targetPath);
    
    this.keyPath = new NormalizedPathType(keyPath);
  }
  
  public Key(final PathType targetPath, final PathType keyPath) {
    this(null, targetPath, keyPath);
  }
  
  /**
   * Is this key positive? In other words, is this key statement satisfied?
   * @return 
   */
  public boolean isPositive() {
    return true;
  }

  public NormalizedPathType getContextPath() {
    return contextPath;
  }

  public NormalizedPathType getKeyPath() {
    return keyPath;
  }

  public NormalizedPathType getTargetPath() {
    return targetPath;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Key other = (Key) obj;
    if (this.contextPath != other.contextPath && (this.contextPath == null || !this.contextPath.equals(other.contextPath))) {
      return false;
    }
    if (this.targetPath != other.targetPath && (this.targetPath == null || !this.targetPath.equals(other.targetPath))) {
      return false;
    }
    if (this.keyPath != other.keyPath && (this.keyPath == null || !this.keyPath.equals(other.keyPath))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 23 * hash + (this.contextPath != null ? this.contextPath.hashCode() : 0);
    hash = 23 * hash + (this.targetPath != null ? this.targetPath.hashCode() : 0);
    hash = 23 * hash + (this.keyPath != null ? this.keyPath.hashCode() : 0);
    return hash;
  }
  
}
