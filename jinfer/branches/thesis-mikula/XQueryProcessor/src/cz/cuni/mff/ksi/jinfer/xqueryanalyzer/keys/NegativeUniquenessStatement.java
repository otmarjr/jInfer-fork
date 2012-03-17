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
package cz.cuni.mff.ksi.jinfer.xqueryanalyzer.keys;

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.PathExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.xqueryprocessor.types.PathType;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.utils.PathTypeParser;

/**
 *
 * @author rio
 */
public class NegativeUniquenessStatement {
  
  private final PathType contextPath;
  private final PathType targetPath;
  private final int weight;

  public NegativeUniquenessStatement(final PathType contextPath, final PathType targetPath, final int weight) {
    if (contextPath != null) {
      final PathTypeParser contextPathParser = new PathTypeParser(contextPath);
      final PathExprNode contextPathExprNode = new PathExprNode(contextPathParser.getSteps(), contextPath.getPathExprNode().getInitialStep());
      this.contextPath = new PathType(contextPathExprNode, null, false);
    } else {
      this.contextPath = null;
    }
    
    final PathTypeParser targetPathParser = new PathTypeParser(targetPath);
    final PathExprNode targetPathExprNode = new PathExprNode(targetPathParser.getSteps(), targetPath.getPathExprNode().getInitialStep());
    this.targetPath = new PathType(targetPathExprNode, null, false);
    
    this.weight = weight;
  }
  
  public NegativeUniquenessStatement(final PathType targetPath, final int weight) {
    this(null, targetPath, weight);
  }

  public PathType getContextPath() {
    return contextPath;
  }

  public PathType getTargetPath() {
    return targetPath;
  }

  public int getWeight() {
    return weight;
  }
  
}
