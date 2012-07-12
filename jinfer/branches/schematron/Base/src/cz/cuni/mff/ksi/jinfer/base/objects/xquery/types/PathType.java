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
package cz.cuni.mff.ksi.jinfer.base.objects.xquery.types;

import cz.cuni.mff.ksi.jinfer.base.interfaces.xquery.Type;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.ExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.InitialStep;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.PathExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.StepExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.VarRefNode;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A representation of type of XQuery paths. This is the most important type
 * in our XQuery processing.
 * 
 * How is a path represented?
 *  - The <code>steps</code> variable represents steps of the paths as instances
 *    of {@link StepExprNode}.
 *  - If a path is relative or absolute is determined by the <code>initialStep</code>
 *    variable.
 *  - The <code>subpaths</code> variable is a mapping from instances of {@link StepExprNode}
 *    from the <code>steps</code> to instances of PathType. This mapping allows us
 *    to represent paths containing variables referring to other paths.
 *    So, the instances of {@link StepExprNode} are only steps containing variable
 *    references.
 *  - The <code>specialFunctionCalls</code> variable holds a list of special
 *    functions which was this instance passed to. In other words, if the list
 *    contains a function <em>f</em>, this instance is not a representation of
 *    plain path, but it is a result of a call to function <em>f</em> supplied
 *    with the path as its argument.
 *  - If the instance of this class is for bound is determined by <code>isForBound</code>
 *    variable
 * 
 * <strong>IMPORTANT:</strong> Note that instances of this class are created
 * by using the references supplied to the constructors and so, changes made
 * to those references (after the construction of this class) affect also
 * this class instances and can lead to inconsistency. Also the getters should
 * not be used to change the provided data.
 * 
 * @author rio
 */
public class PathType extends AbstractType {
  
  /**
   * A definition of special functions to mark if they are called with an instance
   * of this class.
   */
  public final static List<String> SPECIAL_FUNCTION_NAMES = new ArrayList<String>();
  static {
    SPECIAL_FUNCTION_NAMES.add("data");
    SPECIAL_FUNCTION_NAMES.add("distinc-values");
    SPECIAL_FUNCTION_NAMES.add("min");
    SPECIAL_FUNCTION_NAMES.add("max");
    SPECIAL_FUNCTION_NAMES.add("avg");
    SPECIAL_FUNCTION_NAMES.add("sum");
    SPECIAL_FUNCTION_NAMES.add("zero-or-one");
    SPECIAL_FUNCTION_NAMES.add("exactly-one");
  }
  
  private final List<StepExprNode> steps;
  private final InitialStep initialStep;
  private final Map<StepExprNode, PathType> subpaths; // StepExprNode -> PathType
  private final List<String> specialFunctionCalls = new ArrayList<String>(); // An ordered list of functions that have been applied to this path.
  private final boolean isForBound;
  
  /**
   * A basic constructor.
   */
  public PathType(final List<StepExprNode> steps, final InitialStep initialStep, final Map<StepExprNode, PathType> subpaths, final boolean isForBound) {
    this.steps = steps;
    this.initialStep = initialStep;
    this.subpaths = subpaths;
    this.isForBound = isForBound;
  }
  
  /**
   * A constructor from an instance of {@link PathExprNode}. Note that the
   * changed made to the instance of PathExprNode affect also the instance of
   * this classes created by this constructor and can lead to inconsistency.
   * @param pathExprNode 
   */
  public PathType(final PathExprNode pathExprNode) {
    this.steps = pathExprNode.getSteps();
    this.initialStep = pathExprNode.getInitialStep();
    
    subpaths = new HashMap<StepExprNode, PathType>();
    
    for (final StepExprNode stepNode : pathExprNode.getSteps()) {
      final ExprNode detailNode = stepNode.getDetailNode();
      if (detailNode != null) {
        if (VarRefNode.class.isInstance(detailNode)) {
          final VarRefNode varRefNode = (VarRefNode)detailNode;
          final Type type = varRefNode.getType();
          if (!type.isPathType()) {
            assert(false);
          }
          final PathType pathType = (PathType)type;
          subpaths.put(stepNode, pathType);
        }
      }
    }
    
    isForBound = false;
  }
  
  @Override
  public Category getCategory() {
    return Category.PATH;
  }

  public InitialStep getInitialStep() {
    return initialStep;
  }

  public List<StepExprNode> getSteps() {
    return steps;
  }
  
  public void addSpecialFunctionCall(final String specialFunctionName) {
    specialFunctionCalls.add(specialFunctionName);
  }  
  
  public Map<StepExprNode, PathType> getSubpaths() {
    return subpaths;
  }
  
  public boolean isForBound() {
    return isForBound;
  }
  
  public List<String> getSpecialFunctionCalls() {
    return specialFunctionCalls;
  }
  
  public boolean containsSpecialFunctionCalls() {
    return !BaseUtils.isEmpty(specialFunctionCalls);
  }

  @Override
  public boolean isPathType() {
    return true;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final PathType other = (PathType) obj;
    if (this.steps != other.steps && (this.steps == null || !this.steps.equals(other.steps))) {
      return false;
    }
    if (this.initialStep != other.initialStep) {
      return false;
    }
    if (this.subpaths != other.subpaths && (this.subpaths == null || !this.subpaths.equals(other.subpaths))) {
      return false;
    }
    if (this.specialFunctionCalls != other.specialFunctionCalls && (this.specialFunctionCalls == null || !this.specialFunctionCalls.equals(other.specialFunctionCalls))) {
      return false;
    }
    if (this.isForBound != other.isForBound) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    return hash;
  }
  
}
