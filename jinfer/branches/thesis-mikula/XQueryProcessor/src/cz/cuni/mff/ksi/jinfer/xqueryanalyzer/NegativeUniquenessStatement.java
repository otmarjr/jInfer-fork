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
 *
 * @author rio
 */
public class NegativeUniquenessStatement {
  
  private final PathType contextPath;
  private final PathType keyPath;
  private final int weight;

  public NegativeUniquenessStatement(final PathType contextPath, final PathType keyPath, final int weight) {
    this.contextPath = contextPath;
    this.keyPath = keyPath;
    this.weight = weight;
  }
  
  public NegativeUniquenessStatement(final PathType keyPath, final int weight) {
    this(null, keyPath, weight);
  }

  public PathType getContextPath() {
    return contextPath;
  }

  public PathType getKeyPath() {
    return keyPath;
  }

  public int getWeight() {
    return weight;
  }
  
}
