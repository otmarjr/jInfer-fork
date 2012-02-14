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
package cz.cuni.mff.ksi.jinfer.xqueryanalyzer.types;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.PathExprNode;

/**
 * TODO rio poriadne spracovavat, napr zlucovat cesty v ktorych su premenne, ktore su tiez cesty a podobne
 * @author rio
 */
public class PathType implements Type {
  
  private final PathExprNode pathExpr;

  public PathType(final PathExprNode pathExpr) {
    this.pathExpr = pathExpr;
  }
  
  @Override
  public Category getCategory() {
    return Category.PATH;
  }
  
  @Override
  public boolean isNumeric() {
    return false;
  }
  
}
