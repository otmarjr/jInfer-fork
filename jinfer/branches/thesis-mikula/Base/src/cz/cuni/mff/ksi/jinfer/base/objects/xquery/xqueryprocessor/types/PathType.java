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
package cz.cuni.mff.ksi.jinfer.base.objects.xquery.xqueryprocessor.types;

import cz.cuni.mff.ksi.jinfer.base.interfaces.xquery.xqueryprocessor.Type;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.ExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.PathExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.StepExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.VarRefNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO rio poriadne spracovavat, napr zlucovat cesty v ktorych su premenne, ktore su tiez cesty a podobne
 * A representation of type of XQuery paths. This is the most important type
 * in our XQuery processing.
 * 
 * How is a path represented?
 *  - An instance of {@link PathExprNode} holds steps of the paths and information
 *    on whether is the path relative or absolute. However, this instance does
 *    not have to be a node in a syntax tree. Thus, it does not have to contain
 *    a reference to it parent, as well.
 *  - The <code>substeps</code> variable is a mapping from instances of {@link StepExprNode}
 *    from the <code>pathExprNode</code> to instances of PathType. This mapping allows us
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
  
  private final PathExprNode pathExprNode;
  private final Map<StepExprNode, PathType> substeps; // StepExprNode -> PathType
  private final List<String> specialFunctionCalls = new ArrayList<String>(); // An ordered list of functions that have been applied to this path.
  private final boolean isForBound;
  
  /**
   * A basic constructor.
   */
  public PathType(final PathExprNode pathExprNode, final Map<StepExprNode, PathType> substeps, final boolean isForBound) {
    this.pathExprNode = pathExprNode;
    this.substeps = substeps;
    this.isForBound = isForBound;
  }
  
  /**
   * A constructor from an instance of {@link PathExprNode}. Note that the
   * changed made to the instance of PathExprNode affect also the instance of
   * this classes created by this constructor and can lead to inconsistency.
   * @param pathExprNode 
   */
  public PathType(final PathExprNode pathExprNode) {
    this.pathExprNode = pathExprNode;
    substeps = new HashMap<StepExprNode, PathType>();
    
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
          substeps.put(stepNode, pathType);
        }
      }
    }
    
    isForBound = false;
  }
  
  @Override
  public Category getCategory() {
    return Category.PATH;
  }
  
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

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    
    if (!(obj instanceof PathType)) {
      return false;
    }
    
    final PathType path = (PathType)obj;
    
    if (isForBound != path.isForBound) {
      return false;
    }
    
    if (!pathExprNode.equals(path.pathExprNode)) {
      return false;
    }
    
    if (substeps != null && !substeps.equals(path.substeps)
            || substeps == null && path.substeps != null) {
      return false;
    }
    
    if (!specialFunctionCalls.equals(path.specialFunctionCalls)) {
      return false;
    }
    
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 37 * hash + (this.pathExprNode != null ? this.pathExprNode.hashCode() : 0);
    hash = 37 * hash + (this.substeps != null ? this.substeps.hashCode() : 0);
    hash = 37 * hash + (this.specialFunctionCalls != null ? this.specialFunctionCalls.hashCode() : 0);
    hash = 37 * hash + (this.isForBound ? 1 : 0);
    return hash;
  }

  @Override
  public boolean isPathType() {
    return true;
  }

}
