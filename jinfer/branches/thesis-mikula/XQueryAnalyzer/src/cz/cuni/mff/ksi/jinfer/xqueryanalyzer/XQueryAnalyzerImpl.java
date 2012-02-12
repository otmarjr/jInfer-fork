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

import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.XQueryAnalyzer;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.XQueryAnalyzerCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.InferenceDataHolder;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.*;
import java.util.*;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * TODO rio
 * @author rio
 */
@ServiceProvider(service = XQueryAnalyzer.class)
public class XQueryAnalyzerImpl implements XQueryAnalyzer {
  
  private final static Logger LOG = Logger.getLogger(XQueryAnalyzerImpl.class);

  @Override
  public void start(InferenceDataHolder idh, XQueryAnalyzerCallback callback) throws InterruptedException {
    for (final ModuleNode mn : idh.getXQuerySyntaxTrees()) {
      processSyntaxTree(mn);
    }
    callback.finished(idh);
  }
  
  private void processSyntaxTree(final ModuleNode root) {
    final FunctionsProcessor functionsProcessor = new FunctionsProcessor(root);
    final Map<ExprNode, Type> expressionTypes = new HashMap<ExprNode, Type>();
    analysisOfExpressionTypes(expressionTypes, root, new HashMap<String, Type>(), functionsProcessor);
  }
  
  
  private Map<String, Type> analysisOfExpressionTypes(final Map<ExprNode, Type> expressionTypes, final XQNode root, final Map<String, Type> contextVarTypes, final FunctionsProcessor fp) {
    // Copy context variable types as we do not want to modify it for the previous recursive calls.
    final Map<String, Type> localContextVarTypes = new HashMap<String, Type>(contextVarTypes);
    final Map<String, Type> newContextVarTypes = new HashMap<String, Type>();
    
    // Analyze types of the subnodes.
    final List<XQNode> subnodes = root.getSubnodes();
    if (subnodes != null) {
      for (final XQNode subnode : root.getSubnodes()) {
        final Map<String, Type> newVars = analysisOfExpressionTypes(expressionTypes, subnode, localContextVarTypes, fp);
        newContextVarTypes.putAll(newVars);
        // Extend context variable types for the next subnodes.
        localContextVarTypes.putAll(newVars);
      }
    }
    
    // If root is an expression, analyze it and save its type.
    if (ExprNode.class.isInstance(root)) {
      expressionTypes.put((ExprNode)root, determineExpressionType(fp, (ExprNode)root, expressionTypes, localContextVarTypes));
    }
    
    // If root extends context variables for its siblings in recursion, return those variables.
    Map<String, Type> newVars = new HashMap<String, Type>();
    if (VariableBindingNode.class.isInstance(root)) {
      final VariableBindingNode variableBindingNode = (VariableBindingNode)root;
      if (variableBindingNode.getTypeNode() != null) {
        newVars.put(variableBindingNode.getVarName(), new Type(variableBindingNode.getTypeNode()));
      } else {
        final Type type = determineExpressionType(fp, variableBindingNode.getBindingSequenceNode().getExprNode(), expressionTypes, localContextVarTypes);
        newVars.put(variableBindingNode.getVarName(), type);
      }
    } else if (TupleStreamNode.class.isInstance(root)) {
      newVars = newContextVarTypes;
    }
    
    return newVars;
  }
  
  private Type determineExpressionType(final FunctionsProcessor fp, final ExprNode expressionNode, final Map<ExprNode, Type> expressionTypes, final Map<String, Type> contextVarTypes) {
    final Class c = expressionNode.getClass();
    if (c.equals(LiteralNode.class)) {
      final LiteralNode literalNode = (LiteralNode)expressionNode;
      return new Type(literalNode.getType());
    } else if (c.equals(FunctionCallNode.class)) {
      final FunctionCallNode functionCallNode = (FunctionCallNode)expressionNode;
      return fp.getFunctionType(functionCallNode.getFuncName());
    } else if (OperatorNode.class.isInstance(expressionNode)) {
      return determineOperatorType((OperatorNode)expressionNode, expressionTypes);
    } else if (VarRefNode.class.isInstance(expressionNode)) {
      return contextVarTypes.get(((VarRefNode)expressionNode).getVarName());
    }
    assert(false); // TODO rio
    return null;
  }
  
  private Type determineOperatorType(final OperatorNode operatorNode, final Map<ExprNode, Type> expressionTypes) {
    if (operatorNode.getTypeNode() != null) {
      return new Type(operatorNode.getTypeNode());
    }
    
    final Operator operator = operatorNode.getOperator();
    
    if (isOperatorClassComparison(operator)) {
      return new Type(Type.XSDAtomicType.BOOLEAN, Cardinality.ONE);
    }
    
    if (isOperatorClassAddition(operator)) {
      final Type leftType = expressionTypes.get(operatorNode.getOperand());
      final Type rightType = expressionTypes.get(operatorNode.getRightSide());
      if (leftType.isNumeric() && rightType.isNumeric()) {
        return leftType; // TODO rio vybrat obecnejsi z typov
      } else if (leftType.isNumeric()) {
        return leftType;
      } else if (rightType.isNumeric()) {
        return rightType;
      } // TODO rio else?
    }
    
    if (operator == Operator.TO) {
      return new Type(Type.XSDAtomicType.INT, Cardinality.ONE); // TODO rio je to tak?
    }
    
    return null;
  }
  
  private boolean isOperatorClassComparison(final Operator op) {
    switch (op) {
      case GEN_EQUALS:
      case GEN_GREATER_THAN:
      case GEN_GREATER_THAN_EQUALS:
      case GEN_LESS_THAN:
      case GEN_LESS_THAN_EQUALS:
      case GEN_NOT_EQUALS:
      case VAL_EQUALS:
      case VAL_GREATER_THAN:
      case VAL_GREATER_THAN_EQUALS:
      case VAL_LESS_THAN:
      case VAL_LESS_THAN_EQUALS:
      case VAL_NOT_EQUALS:
        return true;
      default:
        return false;
    }
  }
  
  private boolean isOperatorClassAddition(final Operator op) {
    switch (op) {
      case PLUS:
      case MINUS:
        return true;
      default:
        return false; // TODO rio treba pridat aj unarne plus a minus
    }
  }
  

  @Override
  public String getDisplayName() {
    // TODO rio
    return "XQueryAnalyzer";
  }

  @Override
  public String getModuleDescription() {
    // TODO rio
    return "XQueryAnalyzer";
  }

  @Override
  public String getName() {
    // TODO rio
    return "XQueryAnalyzer";
  }

  @Override
  public List<String> getCapabilities() {
    // TODO rio
    return new LinkedList<String>();
  }
  
  
}
