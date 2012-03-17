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

import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.utils.XSDAtomicTypesUtils;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.keys.Key;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.keys.KeySummarizer;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.keys.ForeignKey;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.utils.PathTypeEvaluationContextNodesSet;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.utils.PathTypeParser;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.utils.BuiltinFunctions;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Processor;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.XQueryAnalyzer;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.XQueryAnalyzerCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.FileUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.TopologicalSort;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.*;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.ModuleNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.xqueryprocessor.types.PathType;
import cz.cuni.mff.ksi.jinfer.base.interfaces.xquery.xqueryprocessor.Type;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.xqueryprocessor.types.XSDType;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.utils.InferredType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.openide.util.Lookup;
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
  private static final String METADATA_KEY_HAS_PREDICATES = "xquery_analyzer_type_has_predicates";
  private static final String METADATA_KEY_KEYS = "xquery_analyzer_keys";
  private static final String METADATA_KEY_FOREIGN_KEYS = "xquery_analyzer_foreign_keys";
  
  private KeysInferrer keysInferrer;
  private List<InferredType> inferredTypes;

  @Override
  public void start(final Input input, final List<Element> grammar, final XQueryAnalyzerCallback callback) throws InterruptedException {
    List<ModuleNode> xquerySyntaxTrees = (processXQueries(input.getQueries(), getXQueryProcessor()));
    
    keysInferrer = new KeysInferrer();
    
    for (final ModuleNode mn : xquerySyntaxTrees) {
      processSyntaxTree(mn);
      saveInferredTypes(grammar, inferredTypes);
      LOG.info("Number of inferred type statements in a current tree: " + inferredTypes.size());
    }
    
    keysInferrer.summarize();
    final Map<Key, KeySummarizer.SummarizedInfo> keys = keysInferrer.getKeys();
    final Map<Key, Set<ForeignKey>> foreignKeys = keysInferrer.getForeignKeys();
    saveInferredKeys(grammar, keys, foreignKeys);
    
    LOG.info("Total number of inferred key statements: " + keys.size());
    
    callback.finished(grammar);
  }
  
  /**
   * Returns a processor handling XQuery queries. Suitable processor is found
   * by matching return type and handled extension.
   */
  private Processor<ModuleNode> getXQueryProcessor() {
    for (final Processor p : Lookup.getDefault().lookupAll(Processor.class)) {
      if (p.getResultType().equals(ModuleNode.class) && p.getExtension().equals("xq")) {
        return p;
      }
    }
    
    return null;
  }
  
  /**
   * Processes files with XQuery queries by supplying them to the specified
   * processor. Result is a list of respective syntax trees.
   */
  private List<ModuleNode> processXQueries(final Collection<File> files,
          final Processor<ModuleNode> xqueryProcessor) throws InterruptedException {
    if (BaseUtils.isEmpty(files) || xqueryProcessor == null) {
      return new ArrayList<ModuleNode>(0);
    }

    final List<ModuleNode> ret = new ArrayList<ModuleNode>();

    for (final File f : files) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      try {
        if (FileUtils.getExtension(f.getAbsolutePath()).equals(xqueryProcessor.getExtension())) {
          // TODO rio Toto je hack, kedze nam vyleze len jeden syntax tree ale kvoli rozhraniu processoru musi byt vysledok list.
          final List<ModuleNode> syntaxTree = xqueryProcessor.process(new FileInputStream(f));
          if (syntaxTree.size() > 0) {
            assert(syntaxTree.size() == 1);
            ret.add(syntaxTree.get(0));
          } 
        }
      } catch (final FileNotFoundException e) {
        throw new RuntimeException("File not found: " + f.getAbsolutePath(), e);
      }
    }

    return ret;
  }
  
  private static void saveInferredTypes(final List<Element> grammar, List<InferredType> inferredTypes) throws InterruptedException {
    final List<Element> topologicalSortedGrammar = new TopologicalSort(grammar).sort();
    
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
      
      XSDType.XSDAtomicType inferredAtomicType = ((XSDType)type).getAtomicType();
      
      final PathTypeParser ptp = new PathTypeParser(pathType);
      final boolean hasPredicates = ptp.isHasPredicates();      
      
      PathTypeEvaluationContextNodesSet contextSet = new PathTypeEvaluationContextNodesSet();
      contextSet.addNode(root);
      for (final StepExprNode step : ptp.getSteps()) {
        contextSet = evaluateStep(contextSet, step, grammar);
      }

      for (final AbstractStructuralNode node : contextSet.getNodes()) {
        final Map<String, Object> metadata = node.getMetadata();
        final XSDType.XSDAtomicType xsdType = (XSDType.XSDAtomicType)metadata.get(METADATA_KEY_TYPE);
        if (type == null) {
          metadata.put(METADATA_KEY_TYPE, inferredAtomicType);
          metadata.put(METADATA_KEY_HAS_PREDICATES, hasPredicates);
        } else {
          final XSDType.XSDAtomicType moreSpecificType = XSDAtomicTypesUtils.selectMoreSpecific(inferredAtomicType, xsdType);
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
        final XSDType.XSDAtomicType xsdType = (XSDType.XSDAtomicType)metadata.get(METADATA_KEY_TYPE);
        if (type == null) {
          metadata.put(METADATA_KEY_TYPE, inferredAtomicType);
          metadata.put(METADATA_KEY_HAS_PREDICATES, hasPredicates);
        } else {
          final XSDType.XSDAtomicType moreSpecificType = XSDAtomicTypesUtils.selectMoreSpecific(inferredAtomicType, xsdType);
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
  
  private static void saveInferredKeys(final List<Element> grammar, Map<Key, KeySummarizer.SummarizedInfo> keys, Map<Key, Set<ForeignKey>> foreignKeys) throws InterruptedException {
    final List<Element> topologicalSortedGrammar = new TopologicalSort(grammar).sort();
    
    if (BaseUtils.isEmpty(topologicalSortedGrammar)) {
      return;
    }
    
    final Element root = topologicalSortedGrammar.get(topologicalSortedGrammar.size() - 1);
    
    for (Key key : keys.keySet()) {
      final KeySummarizer.SummarizedInfo keyInfo = keys.get(key);
      
      if (keyInfo.getNormalizedWeight() < 0.3) {
        continue;
      }

      final PathType contextPath = key.getContextPath();
      
      PathTypeEvaluationContextNodesSet contextSet = new PathTypeEvaluationContextNodesSet();
      contextSet.addNode(root);
      
      Set<ForeignKey> fKeys = foreignKeys.get(key);
      
      if (contextPath != null) {
        final PathTypeParser ptp = new PathTypeParser(contextPath);
        for (final StepExprNode step : ptp.getSteps()) {
          contextSet = evaluateStep(contextSet, step, topologicalSortedGrammar);
        }
      } else {
        key = new Key(removeFirstPathItemTypeNode(key.getTargetPath()), key.getKeyPath());
        Set<ForeignKey> modifiedFKeys = new LinkedHashSet<ForeignKey>();
        for (final ForeignKey fKey : fKeys) {
          modifiedFKeys.add(new ForeignKey(key, removeFirstPathItemTypeNode(fKey.getForeignTargetPath()), fKey.getForeignKeyPath()));
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
  
  private static PathType removeFirstPathItemTypeNode(final PathType pathType) {
    final StepExprNode step = pathType.getSteps().get(0);
    if (step.isAxisStep()) {
      final AxisNode axisNode = step.getAxisNode();
      if (axisNode != null) {
        final ItemTypeNode itemTypeNode = axisNode.getNodeTestNode();
        if (itemTypeNode != null) {
          List<StepExprNode> newSteps = new ArrayList<StepExprNode>(pathType.getSteps());
          newSteps.remove(0);
          return new PathType(newSteps, InitialStep.CONTEXT, pathType.getSubpaths(), pathType.isForBound());
        }
      }
    }
    
    return pathType;
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
