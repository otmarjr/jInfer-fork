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

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.VariableBindingNode;

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
  
  public JoinPattern(final JoinPatternType type, final VariableBindingNode firstVariableFBindingNode, final VariableBindingNode secondVariableBindingNode) {
    this.type = type;
    this.firstVariableBindingNode = firstVariableFBindingNode;
    this.secondVariableBindingNode = secondVariableBindingNode;
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
  
}
