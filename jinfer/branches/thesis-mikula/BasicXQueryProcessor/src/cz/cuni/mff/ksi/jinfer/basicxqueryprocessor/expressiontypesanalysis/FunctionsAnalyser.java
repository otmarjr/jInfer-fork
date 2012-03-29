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
package cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.expressiontypesanalysis;

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.*;
import cz.cuni.mff.ksi.jinfer.base.interfaces.xquery.Type;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.AbstractType;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.UnknownType;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.utils.BuiltinFunctionsUtils;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * This class provides type analysis of of user-defined function in a syntax tree,
 * and encapsulates the user-defined functions under one interface together with
 * built-in functions.
 * 
 * In particular, it determines and provides types of arguments (in a form
 * of {@link ParamListNode} instances), types of return values, and definition
 * of user-defined functions (in a form of {@link FunctionDeclNode} instances).
 * 
 * @author rio
 */
public class FunctionsAnalyser {
  
  private static final Logger LOG = Logger.getLogger(FunctionsAnalyser.class);
  private final Map<String, FunctionDeclNode> functionDeclarationNodes;
  private final Map<String, Type> functionTypes;
  
  /**
   * 
   * @param root A syntax tree.
   */
  public FunctionsAnalyser(final ModuleNode root) {
    functionDeclarationNodes = getFunctionDeclarationNodes(root);
    functionTypes = determineFunctionReturnTypes(functionDeclarationNodes);
  }
  
  /**
   * Retrieves {@link FunctionDeclNode} instance for a specified function name.
   */
  public FunctionDeclNode getFunctionDeclarationNode(final String functionName) {
    return functionDeclarationNodes.get(functionName);
  }
  
  /**
   * Retrieves a type of the function specified by {@link FunctionDeclNode}.
   * @param functionCallNode A node of the syntax tree representing a function call.
   * @return A type of the function's return value.
   */
  public Type getFunctionType(final FunctionCallNode functionCallNode) {
    final String qualifiedName = functionCallNode.getFuncName();
    final String builtinFunctionName = BuiltinFunctionsUtils.isBuiltinFunction(qualifiedName);
    
    if (builtinFunctionName != null) {
      return BuiltinFunctionsUtils.getFunctionCallType(functionCallNode);
    } else {
      if (functionTypes.containsKey(qualifiedName)) {
        return functionTypes.get(qualifiedName);
      }
    }
    
    LOG.warn("getFunctionType: Function " + qualifiedName + " is not implemented yet. Returning UnknownType.");
    return new UnknownType();
  }
  
  /**
   * Retrieves information on a function's arguments.
   * @param functionName A name of a function.
   * @return The function's arguments.
   */
  public ParamListNode getParamListNode(final String functionName) {
    final String builtinFunctionName = BuiltinFunctionsUtils.isBuiltinFunction(functionName);
            
    if (builtinFunctionName != null) {
      return BuiltinFunctionsUtils.getParamListNode(builtinFunctionName);
    } else {
      if (functionDeclarationNodes.containsKey(functionName)) {
        return functionDeclarationNodes.get(functionName).getParamListNode();
      }
    }
    
    LOG.warn("getParamListNode: Function " + functionName + " is not implemented yet.");
    return null;
  }
  
  private Map<String, FunctionDeclNode> getFunctionDeclarationNodes(final ModuleNode root) {
    // Function declarations are located in prolog section.
    final Map<String, FunctionDeclNode> fdNodes = new HashMap<String, FunctionDeclNode>();
    for (final XQNode moduleChild : root.getSubnodes()) {
      if (moduleChild.getClass().equals(PrologNode.class)) {
        for (final XQNode prologChild : moduleChild.getSubnodes()) {
          if (prologChild.getClass().equals(FunctionDeclNode.class)) {
            final FunctionDeclNode fdNode = (FunctionDeclNode)prologChild;
            fdNodes.put(fdNode.getFuncName(), fdNode);
          }
        }
        
        // A syntax tree can contain only one prolog, so we can end the searching.
        break;
      }
    }
    return fdNodes;
  }
  
  private Map<String, Type> determineFunctionReturnTypes(final Map<String, FunctionDeclNode> functionDeclarationNodes) {
    final Map<String, Type> fTypes = new HashMap<String, Type>();
    for (final FunctionDeclNode functionDeclarationNode : functionDeclarationNodes.values()) {
      final String functionName = functionDeclarationNode.getFuncName();
      final TypeNode returnTypeNode = functionDeclarationNode.getReturnTypeNode();
      fTypes.put(functionName, AbstractType.createType(returnTypeNode));
    }
    return fTypes;
  }
  
}
