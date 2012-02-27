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

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.PathExprNode;

/**
 * TODO rio comment
 * @author rio
 */
public class Key {
  
  private final PathExprNode contextPath;
  private final PathExprNode targetPath;
  private final PathExprNode keyPath;
  
  public Key(final PathExprNode contextPath, final PathExprNode targetPath, final PathExprNode keyPath) {
    this.contextPath = contextPath;
    this.targetPath = targetPath;
    this.keyPath = keyPath;
  }
  
  public Key(final PathExprNode targetPath, final PathExprNode keyPath) {
    this.contextPath = null;
    this.targetPath = targetPath;
    this.keyPath = keyPath;
  }
  
  public boolean isPossitive() {
    return true;
  }
}
