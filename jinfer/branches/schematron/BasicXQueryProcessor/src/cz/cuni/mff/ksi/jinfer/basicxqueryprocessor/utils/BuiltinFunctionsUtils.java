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
package cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.utils;

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.XSDType;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.UnknownType;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.PathType;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.NodeType;
import cz.cuni.mff.ksi.jinfer.base.interfaces.xquery.Type;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.*;
import cz.cuni.mff.ksi.jinfer.base.objects.xsd.XSDBuiltinAtomicType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * An utility class providing basic functions for handling built-in functions.
 * @author rio
 */
public class BuiltinFunctionsUtils {
  
  private final static Map<String, Type> builtinFunctionTypes = new HashMap<String, Type>();
  static {
    builtinFunctionTypes.put("data", new UnknownType());
    builtinFunctionTypes.put("doc", new NodeType(NodeType.NodeTypeCategory.DOCUMENT, Cardinality.ONE));
    builtinFunctionTypes.put("min", new UnknownType());
    builtinFunctionTypes.put("max", new UnknownType());
    builtinFunctionTypes.put("distinct-values", new UnknownType());
    builtinFunctionTypes.put("count", new XSDType(XSDBuiltinAtomicType.INTEGER, Cardinality.ONE));
    builtinFunctionTypes.put("exists", new XSDType(XSDBuiltinAtomicType.BOOLEAN, Cardinality.ONE));
    builtinFunctionTypes.put("contains", new XSDType(XSDBuiltinAtomicType.BOOLEAN, Cardinality.ONE));
    builtinFunctionTypes.put("empty", new XSDType(XSDBuiltinAtomicType.BOOLEAN, Cardinality.ONE));
    builtinFunctionTypes.put("not", new XSDType(XSDBuiltinAtomicType.BOOLEAN, Cardinality.ONE));
    builtinFunctionTypes.put("concat", new XSDType(XSDBuiltinAtomicType.STRING, Cardinality.ONE));
    builtinFunctionTypes.put("string", new XSDType(XSDBuiltinAtomicType.STRING, Cardinality.ONE));
    builtinFunctionTypes.put("zero-or-one", new UnknownType());
    builtinFunctionTypes.put("exactly-one", new UnknownType());
    builtinFunctionTypes.put("last", new UnknownType());
  }
  
  private final static List<String> DEFAULT_BUILTIN_FUNCTIONS_NAMESPACES = new ArrayList<String>();
  static {
    DEFAULT_BUILTIN_FUNCTIONS_NAMESPACES.add("");
    DEFAULT_BUILTIN_FUNCTIONS_NAMESPACES.add("fn");
  }
  
  private static final Logger LOG = Logger.getLogger(BuiltinFunctionsUtils.class);
  
  /**
   * If a specified function is built-in returns its name without prefix, else
   * returns null.
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
      if (prefix.equals(builtinFunctionsPrefix) && builtinFunctionTypes.containsKey(name)) {
        return name;
      }
    }
    
    return null;
  }
  
  /**
   * Returns a return type of a built-in function specified by an instance
   * of {@link FunctionCallNode}.
   * @param functionCallNode A function.
   * @return A return type of the function.
   */
  public static Type getFunctionCallType(final FunctionCallNode functionCallNode) {
    final String functionName = functionCallNode.getFuncName();
    final String builtinFuncName = BuiltinFunctionsUtils.isBuiltinFunction(functionName);
    
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
      LOG.warn("getFunctioncCallType: Built-in function " + builtinFuncName + " is not implemented yet. Returning UnknownType.");
      return new UnknownType(); // TODO rio Implement completely if needed.
    }
  }
  
  /**
   * Returns formal arguments of a specified function represented by an instance
   * of {@link ParamListNode}.
   * @param functionName A function name.
   * @return Formal arguments of the function.
   */
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
    } else if (functionName.equals("min") || functionName.equals("max") || functionName.equals("distinct-values")) {
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
    } else if (functionName.equals("zero-or-one") || functionName.equals("exactly-one") || functionName.equals("string") || functionName.equals("empty")) {
      final ParamNode paramNode1 = new ParamNode("arg", new TypeNode(Cardinality.ZERO_OR_MORE));
      paramNodes.add(paramNode1);
    } else if (functionName.equals("last")) {
        return null;
    } else if (functionName.equals("contains")) {
      final ItemTypeNode itemTypeNode1 = new AtomicTypeNode("xs:string");
      final ParamNode paramNode1 = new ParamNode("arg1", new TypeNode(Cardinality.ZERO_OR_ONE, itemTypeNode1));
      final ParamNode paramNode2 = new ParamNode("arg2", new TypeNode(Cardinality.ZERO_OR_ONE, itemTypeNode1));
      final ParamNode paramNode3 = new ParamNode("collation", new TypeNode(Cardinality.ONE, itemTypeNode1));
      paramNodes.add(paramNode1);
      paramNodes.add(paramNode2);
      paramNodes.add(paramNode3);
    } else {
      LOG.warn("getParamListNode: Built-in function " + functionName + " is not implemented yet.");
      return null; // TODO rio Implement completely if needed.
    }
    
    return new ParamListNode(paramNodes);
  }
}
