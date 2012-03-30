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
package cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.keydiscovery.negativeuniqueness;

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.NormalizedPathType;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.PathType;

/**
 * A representation of negative uniqueness statements. For description of
 * negative uniqueness statements, refer to the PDF documentation.
 * @author rio
 */
public class NegativeUniquenessStatement {
  
  private final NormalizedPathType contextPath;
  private final NormalizedPathType targetPath;
  private final int weight;
  
  public NegativeUniquenessStatement(final NormalizedPathType contextPath, final NormalizedPathType targetPath, final int weight) {
    this.contextPath = contextPath;
    this.targetPath = targetPath;
    this.weight = weight;
  }
  
  public NegativeUniquenessStatement(final NormalizedPathType targetPath, final int weight) {
    this(null, targetPath, weight);
  }

  public NegativeUniquenessStatement(final PathType contextPath, final PathType targetPath, final int weight) {
    this(contextPath != null ? new NormalizedPathType(contextPath) : null, new NormalizedPathType(targetPath), weight);
  }
  
  public NegativeUniquenessStatement(final PathType targetPath, final int weight) {
    this(null, targetPath, weight);
  }

  public NormalizedPathType getContextPath() {
    return contextPath;
  }

  public NormalizedPathType getTargetPath() {
    return targetPath;
  }

  public int getWeight() {
    return weight;
  }
  
}
