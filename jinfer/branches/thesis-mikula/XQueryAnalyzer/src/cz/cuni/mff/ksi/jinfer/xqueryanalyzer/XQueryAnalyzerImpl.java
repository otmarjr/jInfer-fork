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
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.*;
import cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types.PathType;
import cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types.Type;
import cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types.TypeFactory;
import cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types.XSDType;
import java.util.*;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * TODO rio comment
 *
 * Hlavna trieda XQuery modulu. Postupne pusta jednotlive kroky algoritmu
 * na zistovanie informacii z dotazov.
 *
 * @author rio
 */
@ServiceProvider(service = XQueryAnalyzer.class)
public class XQueryAnalyzerImpl implements XQueryAnalyzer {
  
  private final static Logger LOG = Logger.getLogger(XQueryAnalyzerImpl.class);
  
  private static final String NAME = "XQuery_Analyzer";
  private static final String DISPLAY_NAME = "XQuery analyzer";
  
  private static final String METADATA_KEY_TYPE = "xquery_analyzer_type";
  private static final String METADATA_KEY_HAS_PREDICATES = "xquery_analyzer_type";
  private static final String METADATA_KEY_KEYS = "xquery_analyzer_keys";
  
  private final KeysInferrer keysInferrer = new KeysInferrer();
  private Map<PathType, Type> inferredTypes;

  @Override
  public void start(InferenceDataHolder idh, XQueryAnalyzerCallback callback) throws InterruptedException {
    for (final ModuleNode mn : idh.getXQuerySyntaxTrees()) {
      processSyntaxTree(mn);
      saveInferredTypes(idh.getGrammar(), inferredTypes);
    }
    
    keysInferrer.summarize();
    final Map<Key, KeySummarizer.SummarizedInfo> keys = keysInferrer.getKeys();
    saveInferredKeys(idh.getGrammar(), keys);
    
    callback.finished(idh);
  }
  
  private static void saveInferredTypes(final List<Element> grammar, Map<PathType, Type> inferredTypes) {
    for (final PathType pathType : inferredTypes.keySet()) {
      final Type inferredType = inferredTypes.get(pathType);
      if (inferredType.getCategory() != Type.Category.BUILT_IN) {
        continue;
      }
      
      final PathTypeParser ptp = new PathTypeParser(pathType);
      final boolean hasPredicates = ptp.isHasPredicates();      
      
      PathTypeEvaluationContextNodesSet contextSet = new PathTypeEvaluationContextNodesSet();
      contextSet.addNode(grammar.get(0));
      for (final StepExprNode step : ptp.getSteps()) {
        contextSet = evaluateStep(contextSet, step, grammar);
      }

      for (final AbstractStructuralNode node : contextSet.getNodes()) {
        final Map<String, Object> metadata = node.getMetadata();
        final XSDType type = (XSDType)metadata.get(METADATA_KEY_TYPE);
        if (type == null) {
          metadata.put(METADATA_KEY_TYPE, inferredType);
          metadata.put(METADATA_KEY_HAS_PREDICATES, hasPredicates);
        } else {
          final XSDType moreSpecificType = XSDAtomicTypesUtils.selectMoreSpecific((XSDType)inferredType, type);
          if (moreSpecificType != null) {
            metadata.put(METADATA_KEY_TYPE, moreSpecificType);
            metadata.put(METADATA_KEY_HAS_PREDICATES, hasPredicates);
          } else {
            final boolean oldHasPredicates = (Boolean)metadata.get(METADATA_KEY_HAS_PREDICATES);
            if (!hasPredicates && oldHasPredicates) {
              metadata.put(METADATA_KEY_TYPE, inferredType);
              metadata.put(METADATA_KEY_HAS_PREDICATES, hasPredicates);
            }
          }
        }
      }
    }
  }
  
  private static void saveInferredKeys(final List<Element> grammar, Map<Key, KeySummarizer.SummarizedInfo> keys) {
    for (final Key key : keys.keySet()) {
      final KeySummarizer.SummarizedInfo keyInfo = keys.get(key);
      
      if (keyInfo.getNormalizedWeight() < 0.3) {
        continue;
      }

      final PathType contextPath = key.getContextPath() != null ? TypeFactory.createPathType(key.getContextPath()) : null;
      
      PathTypeEvaluationContextNodesSet contextSet = new PathTypeEvaluationContextNodesSet();
      contextSet.addNode(grammar.get(0));
      
      if (contextPath != null) {
        final PathTypeParser ptp = new PathTypeParser(contextPath);
        for (final StepExprNode step : ptp.getSteps()) {
          contextSet = evaluateStep(contextSet, step, grammar);
        }
      }
      
      for (final AbstractStructuralNode node : contextSet.getNodes()) {
        final Map<String, Object> metadata = node.getMetadata();
        final List<Key> savedKeys = (List<Key>)metadata.get(METADATA_KEY_KEYS);
        if (savedKeys == null) {
          final List<Key> keyList = new ArrayList<Key>();
          keyList.add(key);
          metadata.put(METADATA_KEY_KEYS, keyList);
        } else {
          savedKeys.add(key);
        }
      }
    }
  }
  
  private static PathTypeEvaluationContextNodesSet evaluateStep(PathTypeEvaluationContextNodesSet contextSet, final StepExprNode step, final List<Element> grammar) {
    assert(contextSet != null);
    
    if (SelfOrDescendantStepNode.class.isInstance(step)) {
      return contextSet;
    }
    
    final PathTypeEvaluationContextNodesSet result = new PathTypeEvaluationContextNodesSet();
    
    if (step.isAxisStep()) {
      final AxisNode axisNode = step.getAxisNode();
      final ItemTypeNode itemTypeNode = axisNode.getNodeTestNode();
      switch (axisNode.getAxisKind()) {
        case ATTRIBUTE: {
          assert(NameTestNode.class.isInstance(itemTypeNode)); // Mozno dokoncit, v pripade potreby
          final NameTestNode ntn = (NameTestNode)itemTypeNode;
          final String attName = ntn.getName();
          for (final AbstractStructuralNode node : contextSet.getNodes()) {
            if (node.isElement()) {
              for (final Attribute att : ((Element)node).getAttributes()) {
                if (att.getName().equals(attName)) {
                  result.addAttribute(att);
                }
              }
            }
          }
          break;
        }
          
        case CHILD: {
          assert(NameTestNode.class.isInstance(itemTypeNode)); // Mozno dokoncit, v pripade potreby
          final NameTestNode ntn = (NameTestNode)itemTypeNode;
          final String nodeName = ntn.getName();
          for (final AbstractStructuralNode node : contextSet.getNodes()) {
            if (node.isElement()) {
              for (final AbstractStructuralNode subnode : ((Element)node).getSubnodes().getTokens()) {
                if (subnode.isElement()) {
                  for (final Element element : grammar) {
                    if (element.getName().equals(nodeName)) {
                      result.addNode(element);
                    }
                  }
                }
              }
            }
          }
          break;
        }
          
        default:
          assert(false); // Mozno dokoncit, v pripade potreby
      }
    } else {
      assert(false); // Mozno dokoncit, v pripade potreby
    }
    
    return result;
  }
  
  private void processSyntaxTree(final ModuleNode root) {
    init(root); // Sets references to parent nodes.
    
    final FunctionsProcessor functionsProcessor = new FunctionsProcessor(root);
    final ExpressionsProcessor expressionProcessor = new ExpressionsProcessor(root, functionsProcessor);
    expressionProcessor.process();
    final BuiltinTypesInferrer bti = new BuiltinTypesInferrer(root, functionsProcessor);
    bti.process();
    inferredTypes = bti.getInferredTypes();
    
    keysInferrer.process(root);
  }
  
  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }

  @Override
  public String getModuleDescription() {
    return getDisplayName();
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.emptyList();
  }
  
  private void init(final XQNode node) {
    final List<XQNode> subnodes = node.getSubnodes();
    if (subnodes != null) {
      for (final XQNode subnode : subnodes) {
        subnode.setParentNode(node);
        init(subnode);
      }
    }
  }
}
