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
 *
 * @author rio
 */
public class ForeignKey {

  private final Key key;
  private final NormalizedPathType foreignTargetPath;
  private final NormalizedPathType foreignKeyPath;
  
  public ForeignKey(final Key key, final NormalizedPathType foreignTargetPath, final NormalizedPathType foreignKeyPath) {
    this.key = key;
    this.foreignTargetPath = foreignTargetPath;
    this.foreignKeyPath = foreignKeyPath;
  }
  
  public ForeignKey(final Key key, final PathType foreignTargetPath, final PathType foreignKeyPath) {
    this(key, new NormalizedPathType(foreignTargetPath), new NormalizedPathType(foreignKeyPath));
  }
  
  public Key getKey() {
    return key;
  }

  public NormalizedPathType getForeignKeyPath() {
    return foreignKeyPath;
  }

  public NormalizedPathType getForeignTargetPath() {
    return foreignTargetPath;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ForeignKey other = (ForeignKey) obj;
    if (this.key != other.key && (this.key == null || !this.key.equals(other.key))) {
      return false;
    }
    if (this.foreignTargetPath != other.foreignTargetPath && (this.foreignTargetPath == null || !this.foreignTargetPath.equals(other.foreignTargetPath))) {
      return false;
    }
    if (this.foreignKeyPath != other.foreignKeyPath && (this.foreignKeyPath == null || !this.foreignKeyPath.equals(other.foreignKeyPath))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 43 * hash + (this.key != null ? this.key.hashCode() : 0);
    hash = 43 * hash + (this.foreignTargetPath != null ? this.foreignTargetPath.hashCode() : 0);
    hash = 43 * hash + (this.foreignKeyPath != null ? this.foreignKeyPath.hashCode() : 0);
    return hash;
  }
 
}
