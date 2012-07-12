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
package cz.cuni.mff.ksi.jinfer.basicxqueryprocessor;

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.ModuleNode;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.builtintypeinference.BuiltinTypesInferrer;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.builtintypeinference.InferredTypeStatement;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.expressiontypesanalysis.ExpressionTypesAnalyser;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.expressiontypesanalysis.FunctionsAnalyser;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.keydiscovery.joinpatterns.JoinPattern;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.keydiscovery.joinpatterns.JoinPatternsFinder;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.keydiscovery.negativeuniqueness.NegativeUniquenessFinder;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.keydiscovery.negativeuniqueness.NegativeUniquenessStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a processing of one syntax tree. The processing consists
 * of 5 steps:
 *  - Analysis of function types. See {@link FunctionsAnalyser}.
 *  - Analysis of expression types. See {@link ExpressionTypesAnalyser}.
 *  - Inference of XSD built-in types for paths. See {@link BuiltinTypesInferrer}.
 *  - Inference of negative uniqueness statements. See {@link NegativeUniquenessFinder}.
 *  - Finding join patterns. See {@link JoinPatternsFinder}.
 * 
 * After a construction of an instance, method {@link #process()} can be called,
 * and after the call, result can be retrieved by the getter methods.
 * 
 * @author rio
 */
public class SyntaxTreeProcessor {
  
  private final ModuleNode root;
  private final List<InferredTypeStatement> inferredTypes = new ArrayList<InferredTypeStatement>();
  private final List<JoinPattern> joinPatterns = new ArrayList<JoinPattern>();
  private final List<NegativeUniquenessStatement> negativeUniquenessStatements = new ArrayList<NegativeUniquenessStatement>();
  
  /**
   * A constructor from a syntax tree.
   * @param root A syntax tree.
   */
  public SyntaxTreeProcessor(final ModuleNode root) {
    this.root = root;
  }
  
  /**
   * Processes the syntax tree as described in javadoc for the entire class.
   */
  public void process() {
    final FunctionsAnalyser functionsAnalyser = new FunctionsAnalyser(root);
    
    final ExpressionTypesAnalyser expressionProcessor = new ExpressionTypesAnalyser(root, functionsAnalyser);
    expressionProcessor.process();
    
    final BuiltinTypesInferrer builtinTypesInferrer = new BuiltinTypesInferrer(root, functionsAnalyser);
    builtinTypesInferrer.process();
    inferredTypes.addAll(builtinTypesInferrer.getInferredTypes());
    
    final NegativeUniquenessFinder negativeUniquenessFinder = new NegativeUniquenessFinder(root);
    negativeUniquenessFinder.process();
    negativeUniquenessStatements.addAll(negativeUniquenessFinder.getNegativeUniquenessStatements());
    
    final JoinPatternsFinder joinPatternsFinder = new JoinPatternsFinder(root);
    joinPatternsFinder.process();
    joinPatterns.addAll(joinPatternsFinder.getJoinPatterns());
  }
  
  /**
   * Can be called after the call to {@link #process()} method.
   * @return Inferred type statements.
   */
  public List<InferredTypeStatement> getInferredTypes() {
    return inferredTypes;
  }
  
  /**
   * Can be called after the call to {@link #process()} method.
   * @return Found join patterns.
   */
  public List<JoinPattern> getJoinPatterns() {
    return joinPatterns;
  }
  
  /**
   * Can be called after the call to {@link #process()} method.
   * @return Inferred negative uniqueness statements.
   */
  public List<NegativeUniquenessStatement> getNegativeUniquenessStatements() {
    return negativeUniquenessStatements;
  }
  
}
