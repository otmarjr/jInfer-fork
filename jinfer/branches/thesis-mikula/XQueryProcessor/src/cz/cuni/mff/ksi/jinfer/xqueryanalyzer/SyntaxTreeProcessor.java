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

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.ModuleNode;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.utils.InferredType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rio
 */
public class SyntaxTreeProcessor {
  
  private final ModuleNode root;
  private final List<InferredType> inferredTypes;
  private final KeysInferrer keysInferrer;
  
  public SyntaxTreeProcessor(final ModuleNode root, final KeysInferrer keysInferrer) {
    this.root = root;
    inferredTypes = new ArrayList<InferredType>();
    this.keysInferrer = keysInferrer;
  }
  
  public void process() {
    final FunctionsProcessor functionsProcessor = new FunctionsProcessor(root);
    final ExpressionsProcessor expressionProcessor = new ExpressionsProcessor(root, functionsProcessor);
    expressionProcessor.process();
    final BuiltinTypesInferrer bti = new BuiltinTypesInferrer(root, functionsProcessor);
    bti.process();
    inferredTypes.addAll(bti.getInferredTypes());
    
    keysInferrer.process(root);
  }
  
  public List<InferredType> getInferredTypes() {
    return inferredTypes;
  }
  
}
