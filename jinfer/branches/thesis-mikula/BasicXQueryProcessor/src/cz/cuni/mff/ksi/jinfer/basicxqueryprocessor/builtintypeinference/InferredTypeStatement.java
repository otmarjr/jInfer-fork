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
package cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.builtintypeinference;

import cz.cuni.mff.ksi.jinfer.base.interfaces.xquery.Type;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.PathType;

/**
 *
 * @author rio
 */
public class InferredTypeStatement {

  private final PathType pathType;
  private final Type type;
  
  public InferredTypeStatement(final PathType pathType, final Type type) {
    this.pathType = pathType;
    this.type = type;
  }

  public PathType getPathType() {
    return pathType;
  }

  public Type getType() {
    return type;
  }
  
}
