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
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.AxisNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.ExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.InitialStep;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.ItemTypeNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.KindTestNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.NameTestNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.NodeKind;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.SelfOrDescendantStepNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.StepExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.VarRefNode;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * A representation of a path type in a normalized form. It means that all
 * variable references are resolved, so the path does not contain any subpaths.
 * 
 * This class does not extend {@link AbstractType} nor implements {@link Type},
 * because it does not represent a type in the processing of syntax tree as do
 * the other types. This class is used to make a work (especially comparing)
 * with PathType more simple.
 * 
 * Two instances of this class are equal if they begin with the same initial step,
 * if they have the same special function calls (and in the same order), if
 * they have the same for bound status and if their steps are the same (semantically
 * same, not the same instances).
 * 
 * @author rio
 */
public class NormalizedPathType {
  
  private final static Logger LOG = Logger.getLogger(NormalizedPathType.class);

  private final List<StepExprNode> steps;
  private InitialStep initialStep;
  private final List<String> specialFunctionCalls; // TODO rio Does this make sense in this context?
  private final boolean isForBound; // TODO rio Does this make sense in this context?
  
  private boolean isFirstStep = true;
  private boolean hasPredicates = false;
  
  private NormalizedPathType(final List<StepExprNode> steps, final InitialStep initialStep, final List<String> specialFunctionCalls, final boolean isForBound) {
    this.steps = steps;
    this.initialStep = initialStep;
    this.specialFunctionCalls = specialFunctionCalls;
    this.isForBound = isForBound;
  }
  
  public NormalizedPathType(final PathType pathType) {
    steps = getSteps(pathType);
    specialFunctionCalls = pathType.getSpecialFunctionCalls();
    isForBound = pathType.isForBound();
  }
  
  private List<StepExprNode> getSteps(final PathType pathType) {
    final List<StepExprNode> newSteps = new ArrayList<StepExprNode>();
    
    for (final StepExprNode step : pathType.getSteps()) {
      final ExprNode detailNode = step.getDetailNode();
      if (detailNode != null && detailNode instanceof VarRefNode) {
        newSteps.addAll(getSteps(pathType.getSubpaths().get(step)));
      } else {
        newSteps.add(step);
        if (step.hasPredicates()) {
          hasPredicates = true;
        }
      }
    }
    
    if (isFirstStep) {
      initialStep = pathType.getInitialStep();
      isFirstStep = false;
    }
    
    return newSteps;
  }
  
  public InitialStep getInitialStep() {
    return initialStep;
  }

  public boolean hasPredicates() {
    return hasPredicates;
  }

  public List<StepExprNode> getSteps() {
    return steps;
  }
  
  /**
   * Determines if this instance "covers" the specified other instance.
   * The coverage means that this instance has the same beginning as the
   * other one, or in other words, the other one is more specific.
   * 
   * For example /document/aaa covers /documents/aaa/bbb.
   * 
   * @param covered The other instance to determine if it is covered by this instance.
   * @return True if this instance covers the other instance.
   */
  public boolean covers(final NormalizedPathType covered) {
    if (getInitialStep() != covered.getInitialStep()) {
      return false;
    }
    
    int i = 0;
    final List<StepExprNode> coveredSteps = covered.getSteps();
    final int size1 = steps.size();
    final int size2 = coveredSteps.size();
    
    while (i < size1 && i < size2 && steps.get(i).equals(coveredSteps.get(i))) {
      ++i;
    }
    
    if (i < size1 && i < size2) {
      return false;
    }
    
    if (i < size1) {
      return false;
    } else {
      return true;
    }
  }
  
  private NormalizedPathType copy() {
    return new NormalizedPathType(new ArrayList<StepExprNode>(steps), initialStep, new ArrayList<String>(specialFunctionCalls), isForBound);
  }
  
  /**
   * Creates a copy of this instance (new lists, their items are the same instances!)
   * and removes the first step if it is an instance of {@link ItemTypeNode}.
   */
  public NormalizedPathType copyAndRemoveFirstItemTypeNode() {
    final NormalizedPathType newNormalizedPathType = copy();
    
    final StepExprNode step = newNormalizedPathType.steps.get(0);
    if (step.isAxisStep()) {
      final AxisNode axisNode = step.getAxisNode();
      if (axisNode != null) {
        final ItemTypeNode itemTypeNode = axisNode.getNodeTestNode();
        if (itemTypeNode != null) {
          newNormalizedPathType.steps.remove(0);
          newNormalizedPathType.initialStep = InitialStep.CONTEXT;
        }
      }
    }
    
    return newNormalizedPathType;
  }

  /**
   * Creates an XQuery string representation of the normalized path.
   * TODO rio Print predicates, take initial step into consideration, ...
   */
  @Override
  public String toString() {
    final StringBuilder stringBuilder = new StringBuilder();
    boolean isFirstNode = true;
    boolean printChildAxis = true;
    
    if (getInitialStep() == InitialStep.ROOT) {
      stringBuilder.append("/");
    }
    
    for (final StepExprNode step : steps) {
            
      if (step instanceof SelfOrDescendantStepNode) {
        if (isFirstNode) {
          stringBuilder.append(".");
        }
        stringBuilder.append("//");
        printChildAxis = false;
      } else {
        assert(step.isAxisStep());
        final AxisNode axisNode = step.getAxisNode();

        switch (axisNode.getAxisKind()) {
          case ATTRIBUTE:
            if (!isFirstNode && printChildAxis) {
              stringBuilder.append("/");
            }
            stringBuilder.append("@");
            break;
            
          case CHILD:
            if (!isFirstNode && printChildAxis) {
              stringBuilder.append("/");
            }
            break;
            
          default:
            assert(false);
        }
        
        final ItemTypeNode itemTypeNode = axisNode.getNodeTestNode();
        if (itemTypeNode instanceof NameTestNode) {
          stringBuilder.append(((NameTestNode)itemTypeNode).getName());
        } else if (itemTypeNode instanceof KindTestNode) {
          final NodeKind nodeKind = ((KindTestNode)itemTypeNode).getNodeKind();
          if (nodeKind == NodeKind.TEXT) {
            stringBuilder.append("text()");
          } else {
            LOG.warn("toString(): NodeKind " + nodeKind + " is not implemented yet, the string representation may be incorrect.");
          }
        } else {
          assert(false);
        }
        
        printChildAxis = true;
      }
      
      isFirstNode = false;
    }
    
    return stringBuilder.toString();
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final NormalizedPathType other = (NormalizedPathType) obj;
    if (this.steps != other.steps && (this.steps == null || !this.steps.equals(other.steps))) {
      return false;
    }
    if (this.initialStep != other.initialStep) {
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
    int hash = 5;
    hash = 73 * hash + (this.steps != null ? this.steps.hashCode() : 0);
    hash = 73 * hash + (this.initialStep != null ? this.initialStep.hashCode() : 0);
    hash = 73 * hash + (this.specialFunctionCalls != null ? this.specialFunctionCalls.hashCode() : 0);
    hash = 73 * hash + (this.isForBound ? 1 : 0);
    return hash;
  }
  
}
