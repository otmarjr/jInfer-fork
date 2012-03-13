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

import cz.cuni.mff.ksi.jinfer.base.xqanalyser.nodes.PathExprNode;
import cz.cuni.mff.ksi.jinfer.base.xqanalyser.nodes.VariableBindingNode;

/**
 *
 * @author rio
 */
public class JoinPattern {
  
  public enum JoinPatternType {
    FOR,
    LET,
    JP3;
  }
  
  private final JoinPatternType type;
  private final VariableBindingNode firstVariableBindingNode;
  private final VariableBindingNode secondVariableBindingNode;
  private final PathExprNode P1;
  private final PathExprNode P2;
  private final PathExprNode L1;
  private final PathExprNode L2;
  
  public JoinPattern(final JoinPatternType type, final VariableBindingNode firstVariableBindingNode, final VariableBindingNode secondVariableBindingNode,
          final PathExprNode P1, final PathExprNode P2, final PathExprNode L1, final PathExprNode L2) {
    this.type = type;
    this.firstVariableBindingNode = firstVariableBindingNode;
    this.secondVariableBindingNode = secondVariableBindingNode;
    this.P1 = P1;
    this.P2 = P2;
    this.L1 = L1;
    this.L2 = L2;
  }

  public VariableBindingNode getFirstVariableBindingNode() {
    return firstVariableBindingNode;
  }

  public VariableBindingNode getSecondVariableBindingNode() {
    return secondVariableBindingNode;
  }

  public JoinPatternType getType() {
    return type;
  }

  public PathExprNode getL1() {
    return L1;
  }

  public PathExprNode getL2() {
    return L2;
  }

  public PathExprNode getP1() {
    return P1;
  }

  public PathExprNode getP2() {
    return P2;
  }
  
  
  
}
