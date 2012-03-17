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

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.InitialStep;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.PathExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.xqueryprocessor.types.PathType;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.utils.PathTypeParser;

/**
 * TODO rio comment
 * @author rio
 */
public class Key {
  
  private final PathType contextPath;
  private final PathType targetPath;
  private final PathType keyPath;
  
  public Key(final PathType contextPath, final PathType targetPath, final PathType keyPath) {
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
    
    final PathTypeParser keyPathParser = new PathTypeParser(keyPath);
    final PathExprNode keyPathExprNode = new PathExprNode(keyPathParser.getSteps(), keyPath.getPathExprNode().getInitialStep());
    this.keyPath = new PathType(keyPathExprNode, null, false);
  }
  
  public Key(final PathType targetPath, final PathType keyPath) {
    this(null, targetPath, keyPath);
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
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Key other = (Key) obj;

    
    if (contextPath != null && other.contextPath != null) {
      final PathTypeParser ptp1 = new PathTypeParser(contextPath);
      final PathTypeParser ptp2 = new PathTypeParser(other.contextPath);
      if (!ptp1.getSteps().equals(ptp2.getSteps())) {
        return false;
      }
    } else if (contextPath != null || other.contextPath != null) {
      return false;
    }
    
    final PathTypeParser ptpTarget1 = new PathTypeParser(targetPath);
    final PathTypeParser ptpTarget2 = new PathTypeParser(other.targetPath);
    if (!ptpTarget1.getSteps().equals(ptpTarget2.getSteps())) {
      return false;
    }
    
    final PathTypeParser ptpKey1 = new PathTypeParser(keyPath);
    final PathTypeParser ptpKey2 = new PathTypeParser(other.keyPath);
    if (!ptpKey1.getSteps().equals(ptpKey2.getSteps())) {
      return false;
    }
    
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    PathTypeParser ptpContext = null;
    if (contextPath != null) {
      ptpContext = new PathTypeParser(contextPath);
    }
    final PathTypeParser ptpTarget = new PathTypeParser(targetPath);
    final PathTypeParser ptpKey = new PathTypeParser(keyPath);
    hash = 17 * hash + (this.contextPath != null ? ptpContext.getSteps().hashCode() : 0);
    hash = 17 * hash + (this.targetPath != null ? ptpTarget.getSteps().hashCode() : 0);
    hash = 17 * hash + (this.keyPath != null ? ptpKey.getSteps().hashCode() : 0);
    return hash;
  }
  
}
