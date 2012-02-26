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
package cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.PathExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.StepExprNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO rio poriadne spracovavat, napr zlucovat cesty v ktorych su premenne, ktore su tiez cesty a podobne
 * @author rio
 */
public class PathType implements Type {
  
  public final static List<String> SPECIAL_FUNCTION_NAMES = new ArrayList<String>();
  static {
    SPECIAL_FUNCTION_NAMES.add("data");
    SPECIAL_FUNCTION_NAMES.add("distinc-values");
    SPECIAL_FUNCTION_NAMES.add("min");
    SPECIAL_FUNCTION_NAMES.add("max");
    SPECIAL_FUNCTION_NAMES.add("avg");
    SPECIAL_FUNCTION_NAMES.add("sum");
  }
  
  private final PathExprNode pathExprNode;
  private final Map<StepExprNode, PathType> substeps; // StepExprNode -> PathType
  private final List<String> specialFunctionCalls = new ArrayList<String>(); // An ordered list of functions that have been applied to this path.
  private final boolean isForBound;
  
  public PathType(final PathExprNode pathExprNode, final Map<StepExprNode, PathType> forBoundSubsteps, final boolean isForBound) {
    this.pathExprNode = pathExprNode;
    this.substeps = forBoundSubsteps;
    this.isForBound = isForBound;
  }
  
  @Override
  public Category getCategory() {
    return Category.PATH;
  }
  
  @Override
  public boolean isNumeric() {
    return false;
  }
  
  /*public List<StepExprNode> getStepNodes() {
    return pathExprNode.getSteps();
  }*/
  public PathExprNode getPathExprNode() {
    return pathExprNode;
  }
  
  public void addSpecialFunctionCall(final String specialFunctionName) {
    specialFunctionCalls.add(specialFunctionName);
  }  
  
  public Map<StepExprNode, PathType> getSubsteps() {
    return substeps;
  }
  
  public boolean isForBound() {
    return isForBound;
  }
  
  public List<String> getSpecialFunctionCalls() {
    return specialFunctionCalls;
  }
}
