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
public class ForeignKey {

  private final Key key;
  private final PathType foreignTargetPath;
  private final PathType foreignKeyPath;
  
  public ForeignKey(final Key key, final PathType foreignTargetPath, final PathType foreignKeyPath) {
    this.key = key;
   
    final PathTypeParser targetPathParser = new PathTypeParser(foreignTargetPath);
    final PathExprNode targetPathExprNode = new PathExprNode(targetPathParser.getSteps(), foreignTargetPath.getPathExprNode().getInitialStep());
    this.foreignTargetPath = new PathType(targetPathExprNode, null, false);
    
    final PathTypeParser keyPathParser = new PathTypeParser(foreignKeyPath);
    final PathExprNode keyPathExprNode = new PathExprNode(keyPathParser.getSteps(), foreignKeyPath.getPathExprNode().getInitialStep());
    this.foreignKeyPath = new PathType(keyPathExprNode, null, false);
  }
  
  public Key getKey() {
    return key;
  }

  public PathType getForeignKeyPath() {
    return foreignKeyPath;
  }

  public PathType getForeignTargetPath() {
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
    
    if (!key.equals(other.key)) {
      return false;
    }
    
    final PathTypeParser ptpTarget1 = new PathTypeParser(foreignTargetPath);
    final PathTypeParser ptpTarget2 = new PathTypeParser(other.foreignTargetPath);
    if (!ptpTarget1.getSteps().equals(ptpTarget2.getSteps())) {
      return false;
    }
    
    final PathTypeParser ptpKey1 = new PathTypeParser(foreignKeyPath);
    final PathTypeParser ptpKey2 = new PathTypeParser(other.foreignKeyPath);
    if (!ptpKey1.getSteps().equals(ptpKey2.getSteps())) {
      return false;
    }
    
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    final PathTypeParser ptp1 = new PathTypeParser(foreignTargetPath);
    final PathTypeParser ptp2 = new PathTypeParser(foreignKeyPath);
    hash = 37 * hash + (this.key != null ? this.key.hashCode() : 0);
    hash = 37 * hash + (this.foreignTargetPath != null ? ptp1.getSteps().hashCode() : 0);
    hash = 37 * hash + (this.foreignKeyPath != null ? ptp2.getSteps().hashCode() : 0);
    return hash;
  }
  
  
}
