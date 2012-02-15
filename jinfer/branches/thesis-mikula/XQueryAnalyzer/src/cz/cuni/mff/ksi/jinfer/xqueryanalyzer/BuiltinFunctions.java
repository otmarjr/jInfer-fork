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

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.Cardinality;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.FunctionCallNode;
import cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types.NodeType;
import cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types.PathType;
import cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types.Type;
import cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types.UnknownType;
import java.util.HashMap;
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
  
  public static boolean isBuiltinFunction(final String functionName) {
    if (builtinFunctionTypes.containsKey(functionName)) {
      return true;
    } else {
      return false;
    }
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
}
