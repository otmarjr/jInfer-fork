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

import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.utils.BuiltinFunctions;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.*;
import cz.cuni.mff.ksi.jinfer.base.interfaces.xquery.xqueryprocessor.Type;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.xqueryprocessor.types.TypeFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO rio comment
 * Analyza globalnych funkcii pre jeden syntakticky strom.
 * @author rio
 */
public class FunctionsProcessor {
  
  private final Map<String, FunctionDeclNode> functionDeclarationNodes;
  private final Map<String, Type> functionTypes;
  
  public FunctionsProcessor(final ModuleNode root) {
    functionDeclarationNodes = getFunctionDeclarationNodes(root);
    functionTypes = determineFunctionReturnTypes(functionDeclarationNodes);
  }
  
  public FunctionDeclNode getFunctionDeclarationNode(final String functionName) {
    return functionDeclarationNodes.get(functionName);
  }
  
  public Type getFunctionType(final FunctionCallNode functionCallNode) {
    final String qualifiedName = functionCallNode.getFuncName();
    final String builtinFunctionName = BuiltinFunctions.isBuiltinFunction(qualifiedName);
    
    if (builtinFunctionName != null) {
      return BuiltinFunctions.getFunctionCallType(functionCallNode);
    } else {
      if (functionTypes.containsKey(qualifiedName)) {
        return functionTypes.get(qualifiedName);
      }
    }
    
    assert(false);
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
      fTypes.put(functionName, TypeFactory.createType(returnTypeNode));
    }
    return fTypes;
  }
  
  public ParamListNode getParamListNode(final String functionName) {
    final String builtinFunctionName = BuiltinFunctions.isBuiltinFunction(functionName);
            
    if (builtinFunctionName != null) {
      return BuiltinFunctions.getParamListNode(builtinFunctionName);
    } else {
      if (functionDeclarationNodes.containsKey(functionName)) {
        return functionDeclarationNodes.get(functionName).getParamListNode();
      }
    }
    
    assert(false);
    return null;
  }
}
