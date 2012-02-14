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

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.ModuleNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.PrologNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.XQNode;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.types.Type;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rio
 */
public class GlobalVarsProcessor {
  
  private final PrologNode prolog;
  
  public GlobalVarsProcessor(final PrologNode prolog) {
    this.prolog = prolog;
  }
  
  public Map<String, Type> process() {
    final Map<String, Type> globalVarTypes = new HashMap<String, Type>();
    processRecursive(prolog, globalVarTypes);
    return globalVarTypes;
  }
  
  private void processRecursive(final XQNode node, final Map<String, Type> validVarTypes) {
    
  }
  
}
