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

import cz.cuni.mff.ksi.jinfer.base.xqanalyser.nodes.*;
import cz.cuni.mff.ksi.jinfer.base.xqueryprocessor.types.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rio
 */
public class BuiltinFunctions {
  
  private final static Map<String, Type> builtinFunctionTypes = new HashMap<String, Type>();
  static {
    builtinFunctionTypes.put("data", new UnknownType());
    builtinFunctionTypes.put("doc", new NodeType(NodeType.NType.DOCUMENT, Cardinality.ONE));
    builtinFunctionTypes.put("min", new UnknownType());
    builtinFunctionTypes.put("distinct-values", new UnknownType());
    builtinFunctionTypes.put("count", new XSDType(XSDType.XSDAtomicType.INTEGER, Cardinality.ONE));
    builtinFunctionTypes.put("exists", new XSDType(XSDType.XSDAtomicType.BOOLEAN, Cardinality.ONE));
    builtinFunctionTypes.put("not", new XSDType(XSDType.XSDAtomicType.BOOLEAN, Cardinality.ONE));
    builtinFunctionTypes.put("concat", new XSDType(XSDType.XSDAtomicType.STRING, Cardinality.ONE));
  }
  
  private final static List<String> DEFAULT_BUILTIN_FUNCTIONS_NAMESPACES = new ArrayList<String>();
  static {
    DEFAULT_BUILTIN_FUNCTIONS_NAMESPACES.add("");
    DEFAULT_BUILTIN_FUNCTIONS_NAMESPACES.add("fn");
  }
  
  /**
   * Zisti ci je funkcia vstavana. Ak ano vrati jej meno, inak null.
   */
  public static String isBuiltinFunction(final String functionName) {
    String prefix;
    String name;
    final int colonPos = functionName.lastIndexOf(':');
    if (colonPos != -1) {
      prefix = functionName.substring(0, colonPos);
      name = functionName.substring(colonPos + 1);
    } else {
      prefix = "";
      name = functionName;
    }
    
    for (final String builtinFunctionsPrefix : DEFAULT_BUILTIN_FUNCTIONS_NAMESPACES) {
      if (prefix.equals(builtinFunctionsPrefix)) {
        if (builtinFunctionTypes.containsKey(name)) {
          return name;
        }
      }
    }
    
    return null;
  }
  
  public static Type getFunctionCallType(final FunctionCallNode functionCallNode) {
    final String functionName = functionCallNode.getFuncName();
    final String builtinFuncName = BuiltinFunctions.isBuiltinFunction(functionName);
    
    assert (builtinFuncName != null);
    
    for (final String specFuncName : PathType.SPECIAL_FUNCTION_NAMES) {
      if (builtinFuncName.equals(specFuncName)) {
        final Type type = functionCallNode.getParams().get(0).getType();
        if (PathType.class.isInstance(type)) {
          ((PathType)type).addSpecialFunctionCall(builtinFuncName);
        }
        return type;
      }
    }
    
    if (builtinFunctionTypes.containsKey(builtinFuncName)) {
      return builtinFunctionTypes.get(builtinFuncName);
    } else {
      assert(false);
      return null;
    }
  }
  
  public static ParamListNode getParamListNode(final String functionName) {
    if (!builtinFunctionTypes.containsKey(functionName)) {
      return null;
    }
    
    final XQNodeList<ParamNode> paramNodes = new XQNodeList<ParamNode>();
    
    if (functionName.equals("data") || functionName.equals("count") || functionName.equals("exists") || functionName.equals("not")) {
      final ParamNode paramNode = new ParamNode("arg", new TypeNode(Cardinality.ZERO_OR_MORE));
      paramNodes.add(paramNode);
    } else if (functionName.equals("doc")) {
      final ItemTypeNode itemTypeNode = new AtomicTypeNode("xs:string");
      final ParamNode paramNode = new ParamNode("uri", new TypeNode(Cardinality.ZERO_OR_ONE, itemTypeNode));
      paramNodes.add(paramNode);
    } else if (functionName.equals("min") || functionName.equals("distinct-values")) {
      final ParamNode paramNode1 = new ParamNode("arg", new TypeNode(Cardinality.ZERO_OR_MORE));
      final ItemTypeNode itemTypeNode2 = new AtomicTypeNode("xs:string");
      final ParamNode paramNode2 = new ParamNode("collation", new TypeNode(Cardinality.ONE, itemTypeNode2));
      paramNodes.add(paramNode1);
      paramNodes.add(paramNode2);
    } else if (functionName.equals("concat")) {
      final ParamNode paramNode = new ParamNode("arg", new TypeNode(Cardinality.ZERO_OR_ONE));
      for (int i = 0; i < 5; ++i) {
        paramNodes.add(paramNode);
      }
    } else {
      assert(false); // TODO rio Doplnit dalsie vstavane funckie podla potreby.
    }
    
    return new ParamListNode(paramNodes);
  }
}