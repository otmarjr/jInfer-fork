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

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.*;
import cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types.PathType;
import cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types.Type;
import cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types.TypeFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rio
 */
public class BuiltinTypesInferrer {
  
  private final ModuleNode root;
  private final FunctionsProcessor fp;
  private final Map<PathType, Type> inferredTypes = new HashMap<PathType, Type>();
  
  public BuiltinTypesInferrer(final ModuleNode root, final FunctionsProcessor fp) {
    this.root = root;
    this.fp = fp;
  }
  
  public void process() {
    processRecursive(root);
  }
  
  public void processRecursive(final XQNode node) {
    if (FunctionCallNode.class.isInstance(node)) {
      processFunctionCall((FunctionCallNode)node);
    } else if (OperatorNode.class.isInstance(node)) {
      processOperator((OperatorNode)node);
    }
    
    final List<XQNode> subnodes = node.getSubnodes();
    if (subnodes == null) {
      return;
    }
    for (final XQNode subnode : subnodes) {
      processRecursive(subnode);
    }
  }
  
  public void processFunctionCall(final FunctionCallNode fcn) {
    final List<ExprNode> params = fcn.getParams();
    final ParamListNode paramListNode = fp.getParamListNode(fcn.getFuncName());
    
    if (paramListNode == null) {
      return;
    }
    
    final List<ParamNode> paramNodes = paramListNode.getParamNodes();
    
    for (int i = 0; i < params.size(); ++i) {
      final ExprNode param = params.get(i);
      final Type paramType = param.getType();
      if (paramType.getCategory() == Type.Category.PATH) {
        final TypeNode paramTypeDeclarationNode = paramNodes.get(i).getTypeDeclarationNode();
        if (paramTypeDeclarationNode == null) {
          continue;
        }
        inferredTypes.put((PathType)paramType, TypeFactory.createType(paramTypeDeclarationNode));
      }
    }
  }
  
  public void processOperator(final OperatorNode opNode) {
    final Operator op = opNode.getOperator();
    
    PathType pathType = null;
    Type type = null;
    
    switch (op) {
      case PLUS:
      case MINUS:
      case MUL:
      case DIV:
      case MOD:
      {
        final ExprNode leftOperand = opNode.getOperand();
        final ExprNode rightOperand = opNode.getRightSide();
        if (rightOperand == null) {
          break;
        }
        final Type leftType = leftOperand.getType();
        final Type rightType = rightOperand.getType();
        if (leftType.getCategory() == Type.Category.PATH) {
          if (rightType.isNumeric() && rightType.getCategory() == Type.Category.BUILT_IN) {
            pathType = (PathType)leftType;
            type = rightType;
          }
        } else if (rightType.getCategory() == Type.Category.PATH) {
          if (leftType.isNumeric() && leftType.getCategory() == Type.Category.BUILT_IN) {
            pathType = (PathType)rightType;
            type = leftType;
          }
        }
        break;
      }
        
      case GEN_LESS_THAN:
      case GEN_LESS_THAN_EQUALS:
      case GEN_GREATER_THAN_EQUALS:
      case GEN_EQUALS:
      case GEN_NOT_EQUALS:
      {
        final ExprNode leftOperand = opNode.getOperand();
        final ExprNode rightOperand = opNode.getRightSide();
        final Type leftType = leftOperand.getType();
        final Type rightType = rightOperand.getType();
        if (leftType.getCategory() == Type.Category.PATH) {
          if (rightType.getCategory() == Type.Category.BUILT_IN) {
            pathType = (PathType)leftType;
            type = rightType;
          }
        } else if (rightType.getCategory() == Type.Category.PATH) {
          if (leftType.getCategory() == Type.Category.BUILT_IN) {
            pathType = (PathType)rightType;
            type = leftType;
          }
        }
        break;
      }
        
      default:
        break;
    }
    
    if (pathType != null) {
      assert(type != null);
      inferredTypes.put(pathType, type);
    }
  }
}
