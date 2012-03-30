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
package cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.builtintypeinference;

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.*;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.PathType;
import cz.cuni.mff.ksi.jinfer.base.interfaces.xquery.Type;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.AbstractType;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.expressiontypesanalysis.FunctionsAnalyser;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rio
 */
public class BuiltinTypesInferrer {
  
  private final ModuleNode root;
  private final FunctionsAnalyser fp;
  private final List<InferredTypeStatement> inferredTypes = new ArrayList<InferredTypeStatement>();
  
  public BuiltinTypesInferrer(final ModuleNode root, final FunctionsAnalyser fp) {
    this.root = root;
    this.fp = fp;
  }
  
  public List<InferredTypeStatement> process() {
    processRecursive(root);
    return inferredTypes;
  }
  
  private void processRecursive(final XQNode node) {
    if (FunctionCallNode.class.isInstance(node)) {
      processFunctionCall((FunctionCallNode)node);
    } else if (OperatorNode.class.isInstance(node)) {
      processOperator((OperatorNode)node);
    } else if (node instanceof FunctionBodyNode) {
      // To process function body nodes, we need know the real arguments.
      // It is not implemented yet.
      return;
    } else if (node instanceof PathExprNode) {
      // We do not want to dive to PathExprNode instances, because it results
      // in inference of types for parts of paths. For example /n[@a = 5] infers
      // @a -> integer.
      return;
    }
    
    final List<XQNode> subnodes = node.getSubnodes();
    if (subnodes == null) {
      return;
    }
    for (final XQNode subnode : subnodes) {
      processRecursive(subnode);
    }
  }
  
  private void processFunctionCall(final FunctionCallNode fcn) {
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
        final Type formalParamType = AbstractType.createType(paramTypeDeclarationNode);
        if (formalParamType.getCategory() != Type.Category.UNKNOWN) {
          inferredTypes.add(new InferredTypeStatement((PathType)paramType, formalParamType));
        }
      }
    }
  }
  
  private void processOperator(final OperatorNode opNode) {
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
          if (rightType.isNumeric() && rightType.getCategory() == Type.Category.XSD_BUILT_IN) {
            pathType = (PathType)leftType;
            type = rightType;
          }
        } else if (rightType.getCategory() == Type.Category.PATH
                && leftType.isNumeric()
                && leftType.getCategory() == Type.Category.XSD_BUILT_IN) {
          pathType = (PathType)rightType;
          type = leftType;
        }
        break;
      }
        
      case GEN_LESS_THAN:
      case GEN_LESS_THAN_EQUALS:
      case GEN_GREATER_THAN_EQUALS:
      case GEN_GREATER_THAN:
      case GEN_EQUALS:
      case GEN_NOT_EQUALS:
      {
        final ExprNode leftOperand = opNode.getOperand();
        final ExprNode rightOperand = opNode.getRightSide();
        final Type leftType = leftOperand.getType();
        final Type rightType = rightOperand.getType();
        if (leftType.getCategory() == Type.Category.PATH) {
          if (rightType.getCategory() == Type.Category.XSD_BUILT_IN) {
            pathType = (PathType)leftType;
            type = rightType;
          }
        } else if (rightType.getCategory() == Type.Category.PATH
                && leftType.getCategory() == Type.Category.XSD_BUILT_IN) {
          pathType = (PathType)rightType;
          type = leftType;
        }
        break;
      }
        
      default:
        break;
    }
    
    if (pathType != null) {
      assert(type != null);
      inferredTypes.add(new InferredTypeStatement(pathType, type));
    }
  }

  public List<InferredTypeStatement> getInferredTypes() {
    return inferredTypes;
  }
    
}
