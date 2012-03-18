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

import cz.cuni.mff.ksi.jinfer.base.interfaces.xquery.Type;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.keys.ForeignKey;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.keys.Key;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.AxisNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.ExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.FunctionCallNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.ItemTypeNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.KindTestNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.NameTestNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.NodeKind;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.SelfOrDescendantStepNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.StepExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.NormalizedPathType;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.PathType;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.XSDType;
import cz.cuni.mff.ksi.jinfer.base.objects.xsd.XSDBuiltinAtomicType;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.TopologicalSort;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.keys.KeySummarizer;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.utils.BuiltinFunctions;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.utils.InferredType;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.utils.PathTypeEvaluationContextNodesSet;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.utils.XSDAtomicTypesUtils;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class responsible for merging the inferred statements with the grammar.
 * 
 * The merging is done in the most simple way. The inferred statements contain
 * path determining involved elements and attributes, and some their property (the inferred one).
 * These path expressions are evaluated upon the grammar and the property is then
 * written to the metadata of the involved elements and attributes.
 * 
 * @author rio
 */
public class Merger {
  
  /* Grammar metadata keys. Inferred statements will be written to the metadata
   * under these keys. Any exporter which want to use statements inferred by
   * this module, must read them from the metadata using these keys.
   */
  private static final String METADATA_KEY_TYPE = "xquery_processor_type";
  private static final String METADATA_KEY_HAS_PREDICATES = "xquery_processor_type_has_predicates";
  private static final String METADATA_KEY_KEYS = "xquery_processor_keys";
  private static final String METADATA_KEY_FOREIGN_KEYS = "xquery_processor_foreign_keys";
  
  private final List<Element> topologicallySortedGrammar;
  
  /**
   * A constructor from a grammar which will be written inferred statements to.
   * @param grammar Grammar to write inferred statements to.
   * @throws InterruptedException 
   */
  public Merger(final List<Element> grammar) throws InterruptedException {
    topologicallySortedGrammar = new TopologicalSort(grammar).sort();
  }
  
  /**
   * Merges specified inferred type statements with the grammar.
   * @param inferredTypes
   * @throws InterruptedException 
   */
  public void mergeInferredTypes(List<InferredType> inferredTypes) throws InterruptedException {
    writeInferredTypesToGrammar(topologicallySortedGrammar, inferredTypes);
  }
  
  /**
   * Merges specified inferred key statements with the grammar.
   * @param keys
   * @param foreignKeys
   * @throws InterruptedException 
   */
  public void mergeInferredKeys(Map<Key, KeySummarizer.SummarizedInfo> keys, Map<Key, Set<ForeignKey>> foreignKeys) throws InterruptedException {
    writeInferredKeysToGrammar(topologicallySortedGrammar, keys, foreignKeys);
  }
  
  /**
   * Writes the supplied inferred type statements to a topologically sorted grammar metadata.
   * @param grammar A topologically grammar to write the statements to.
   * @param inferredTypes The inferred type statements.
   * @throws InterruptedException 
   */
  private static void writeInferredTypesToGrammar(final List<Element> topologicalSortedGrammar, List<InferredType> inferredTypes) throws InterruptedException {
    if (BaseUtils.isEmpty(topologicalSortedGrammar)) {
      return;
    }
    
    final Element root = topologicalSortedGrammar.get(topologicalSortedGrammar.size() - 1);
    
    for (final InferredType inferredType : inferredTypes) {
      final PathType pathType = inferredType.getPathType();
      final Type type = inferredType.getType();
      if (type.getCategory() != Type.Category.XSD_BUILT_IN) {
        continue;
      }
      
      XSDBuiltinAtomicType inferredAtomicType = ((XSDType)type).getAtomicType();
      
      final NormalizedPathType ptp = new NormalizedPathType(pathType);
      final boolean hasPredicates = ptp.hasPredicates();      
      
      PathTypeEvaluationContextNodesSet contextSet = new PathTypeEvaluationContextNodesSet();
      contextSet.addNode(root);
      for (final StepExprNode step : ptp.getSteps()) {
        contextSet = evaluateStep(contextSet, step, topologicalSortedGrammar);
      }

      for (final AbstractStructuralNode node : contextSet.getNodes()) {
        final Map<String, Object> metadata = node.getMetadata();
        final XSDBuiltinAtomicType xsdType = (XSDBuiltinAtomicType)metadata.get(METADATA_KEY_TYPE);
        if (type == null) {
          metadata.put(METADATA_KEY_TYPE, inferredAtomicType);
          metadata.put(METADATA_KEY_HAS_PREDICATES, hasPredicates);
        } else {
          final XSDBuiltinAtomicType moreSpecificType = XSDAtomicTypesUtils.selectMoreSpecific(inferredAtomicType, xsdType);
          if (moreSpecificType != null) {
            metadata.put(METADATA_KEY_TYPE, moreSpecificType);
            metadata.put(METADATA_KEY_HAS_PREDICATES, hasPredicates);
          } else {
            final boolean oldHasPredicates = (Boolean)metadata.get(METADATA_KEY_HAS_PREDICATES);
            if (!hasPredicates && oldHasPredicates) {
              metadata.put(METADATA_KEY_TYPE, inferredAtomicType);
              metadata.put(METADATA_KEY_HAS_PREDICATES, hasPredicates);
            }
          }
        }
      }
      
      for (final Attribute attribute : contextSet.getAttributes()) {
        final Map<String, Object> metadata = attribute.getMetadata();
        final XSDBuiltinAtomicType xsdType = (XSDBuiltinAtomicType)metadata.get(METADATA_KEY_TYPE);
        if (type == null) {
          metadata.put(METADATA_KEY_TYPE, inferredAtomicType);
          metadata.put(METADATA_KEY_HAS_PREDICATES, hasPredicates);
        } else {
          final XSDBuiltinAtomicType moreSpecificType = XSDAtomicTypesUtils.selectMoreSpecific(inferredAtomicType, xsdType);
          if (moreSpecificType != null) {
            metadata.put(METADATA_KEY_TYPE, moreSpecificType);
            metadata.put(METADATA_KEY_HAS_PREDICATES, hasPredicates);
          } else {
            final boolean oldHasPredicates = (Boolean)metadata.get(METADATA_KEY_HAS_PREDICATES);
            if (!hasPredicates && oldHasPredicates) {
              metadata.put(METADATA_KEY_TYPE, inferredAtomicType);
              metadata.put(METADATA_KEY_HAS_PREDICATES, hasPredicates);
            }
          }
        }
      }
    }
  }
  
  /**
   * Writes the supplied inferred key statements to a topologically sorted grammar metadata.
   * @param topologicalSortedGrammar
   * @param keys
   * @param foreignKeys
   * @throws InterruptedException 
   */
  private static void writeInferredKeysToGrammar(final List<Element> topologicalSortedGrammar, Map<Key, KeySummarizer.SummarizedInfo> keys, Map<Key, Set<ForeignKey>> foreignKeys) throws InterruptedException {
    if (BaseUtils.isEmpty(topologicalSortedGrammar)) {
      return;
    }
    
    final Element root = topologicalSortedGrammar.get(topologicalSortedGrammar.size() - 1);
    
    for (Key key : keys.keySet()) {
      final KeySummarizer.SummarizedInfo keyInfo = keys.get(key);
      
      if (keyInfo.getNormalizedWeight() < 0.3) {
        continue;
      }

      final NormalizedPathType contextPath = key.getContextPath();
      
      PathTypeEvaluationContextNodesSet contextSet = new PathTypeEvaluationContextNodesSet();
      contextSet.addNode(root);
      
      Set<ForeignKey> fKeys = foreignKeys.get(key);
      
      if (contextPath != null) {
        for (final StepExprNode step : contextPath.getSteps()) {
          contextSet = evaluateStep(contextSet, step, topologicalSortedGrammar);
        }
      } else {
        key = new Key(key.getTargetPath().copyAndRemoveFirstItemTypeNode(), key.getKeyPath());
        Set<ForeignKey> modifiedFKeys = new LinkedHashSet<ForeignKey>();
        for (final ForeignKey fKey : fKeys) {
          modifiedFKeys.add(new ForeignKey(key, fKey.getForeignTargetPath().copyAndRemoveFirstItemTypeNode(), fKey.getForeignKeyPath()));
        }
        fKeys = modifiedFKeys;
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
        
        if (!BaseUtils.isEmpty(fKeys)) {
          final Set<ForeignKey> savedFKeys = (Set<ForeignKey>)metadata.get(METADATA_KEY_FOREIGN_KEYS);
          if (savedFKeys == null) {
            metadata.put(METADATA_KEY_FOREIGN_KEYS, fKeys);
          } else {
            savedFKeys.addAll(fKeys);
          }
        }
      }
    }
  }
  
  private static PathTypeEvaluationContextNodesSet evaluateStep(PathTypeEvaluationContextNodesSet contextSet, final StepExprNode step, final List<Element> grammar) {
    assert(contextSet != null);
    
    if (SelfOrDescendantStepNode.class.isInstance(step)) {
      // TODO rio Toto nestaci, treba aj vsetkych potomkov.
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
          if (NameTestNode.class.isInstance(itemTypeNode)) {
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
          } else if (KindTestNode.class.isInstance(itemTypeNode)) { // TODO rio do diplomky!!
            final KindTestNode ktn = (KindTestNode)itemTypeNode;
            final NodeKind nk = ktn.getNodeKind();
            if (nk == NodeKind.TEXT) {
              for (final AbstractStructuralNode node : contextSet.getNodes()) {
                if (node.isElement()) {
                  result.addNode(node);
                }
              }
            } else {
              assert(false);
            }
          } else {
              assert(false); // Mozno dokoncit, v pripade potreby
          }
          break;
        }
          
        default:
          assert(false); // Mozno dokoncit, v pripade potreby
      }
    } else {
      final ExprNode detailNode = step.getDetailNode();
      
      if (FunctionCallNode.class.isInstance(detailNode)) {
        final String builtinFuncName = BuiltinFunctions.isBuiltinFunction(((FunctionCallNode)detailNode).getFuncName());
        if ("doc".equals(builtinFuncName)) {
          return contextSet;
        }
      } 
      
      assert(false); // Mozno dokoncit, v pripade potreby
    }
    
    return result;
  }
}
