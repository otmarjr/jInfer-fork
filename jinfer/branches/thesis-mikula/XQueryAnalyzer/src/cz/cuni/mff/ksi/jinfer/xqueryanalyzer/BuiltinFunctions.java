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
import cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types.NodeType;
import cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types.PathType;
import cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types.Type;
import cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types.UnknownType;
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
    String functionName = functionCallNode.getFuncName();
    functionName = functionName.substring(functionName.lastIndexOf(':') + 1);
    
    if (functionName.equals("data")) {
      final Type type = functionCallNode.getParams().get(0).getType();
      if (PathType.class.isInstance(type)) {
        ((PathType)type).addSpecialFunctionCall("data");
      }
      return type;
    } else if (functionName.equals("min")) {
      final Type type = functionCallNode.getParams().get(0).getType();
      if (PathType.class.isInstance(type)) {
        ((PathType)type).addSpecialFunctionCall("min");
      }
      return type;
    } else if (builtinFunctionTypes.containsKey(functionName)) {
      return builtinFunctionTypes.get(functionName);
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
    
    if (functionName.equals("data")) {
      final ParamNode paramNode = new ParamNode(null, "arg", new TypeNode(null, Cardinality.ZERO_OR_MORE));
      paramNodes.add(paramNode);
    } else if (functionName.equals("doc")) {
      final ItemTypeNode itemTypeNode = new AtomicTypeNode(null, "xs:string");
      final ParamNode paramNode = new ParamNode(null, "uri", new TypeNode(null, Cardinality.ZERO_OR_ONE, itemTypeNode));
      paramNodes.add(paramNode);
    } else if (functionName.equals("min")) {
      final ParamNode paramNode1 = new ParamNode(null, "arg", new TypeNode(null, Cardinality.ZERO_OR_MORE));
      final ItemTypeNode itemTypeNode2 = new AtomicTypeNode(null, "xs:string");
      final ParamNode paramNode2 = new ParamNode(null, "collation", new TypeNode(null, Cardinality.ONE, itemTypeNode2));
      paramNodes.add(paramNode1);
      paramNodes.add(paramNode2);
    } else {
      assert(false); // TODO rio
    }
    
    return new ParamListNode(null, paramNodes);
  }
}
