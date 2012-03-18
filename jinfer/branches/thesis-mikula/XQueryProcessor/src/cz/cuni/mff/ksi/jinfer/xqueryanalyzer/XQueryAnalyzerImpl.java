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
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.keys.Key;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.keys.KeySummarizer;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.keys.ForeignKey;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.utils.PathTypeEvaluationContextNodesSet;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.utils.BuiltinFunctions;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Processor;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.XQueryProcessor;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.XQueryProcessorCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.FileUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.TopologicalSort;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.*;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.ModuleNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.PathType;
import cz.cuni.mff.ksi.jinfer.base.interfaces.xquery.Type;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.NormalizedPathType;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.XSDType;
import cz.cuni.mff.ksi.jinfer.base.objects.xsd.XSDBuiltinAtomicType;
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
@ServiceProvider(service = XQueryProcessor.class)
public class XQueryAnalyzerImpl implements XQueryProcessor {
  
  private final static Logger LOG = Logger.getLogger(XQueryAnalyzerImpl.class);
  
  private static final String NAME = "Basic_XQuery_Processor";
  private static final String DISPLAY_NAME = "Basic XQuery Processor";
  
  private KeysInferrer keysInferrer;
  private List<InferredType> inferredTypes;

  @Override
  public void start(final Input input, final List<Element> grammar, final XQueryProcessorCallback callback) throws InterruptedException {
    List<ModuleNode> xquerySyntaxTrees = (processXQueries(input.getQueries(), getXQueryProcessor()));
    LOG.info("Input XQuery files parsed into " + xquerySyntaxTrees.size() + " syntax trees");
    
    inferredTypes = new ArrayList<InferredType>();
    keysInferrer = new KeysInferrer();
    
    for (final ModuleNode mn : xquerySyntaxTrees) {
      processSyntaxTree(mn);
    }
    
    LOG.info("Total Number of inferred type statements: " + inferredTypes.size());
    
    keysInferrer.summarize();
    final Map<Key, KeySummarizer.SummarizedInfo> keys = keysInferrer.getKeys();
    final Map<Key, Set<ForeignKey>> foreignKeys = keysInferrer.getForeignKeys();
    LOG.info("Total number of inferred key statements: " + keys.size());
    
    final Merger merger = new Merger(grammar);
    merger.mergeInferredTypes(inferredTypes);
    merger.mergeInferredKeys(keys, foreignKeys);
    
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
          /* TODO rio Interface of input processor requires List as a return type
           * but our XQuery processor returns only one syntax tree per file. So
           * it is at index 0. An empty list indicates parsing error.
           */
          final List<ModuleNode> syntaxTree = xqueryProcessor.process(new FileInputStream(f));
          if (syntaxTree.size() > 0) {
            assert(syntaxTree.size() == 1);
            ret.add(syntaxTree.get(0));
          } else {
            LOG.error("Error in XQuery file " + f.getAbsolutePath() + ". Try to strip it of empty lines and comments."); // TODO rio Fix weird parsing errors.
          }
        }
      } catch (final FileNotFoundException e) {
        throw new RuntimeException("File not found: " + f.getAbsolutePath(), e);
      }
    }

    return ret;
  }
  
  
  
  private void processSyntaxTree(final ModuleNode root) {
    init(root); // Sets references to parent nodes.
    
    final FunctionsProcessor functionsProcessor = new FunctionsProcessor(root);
    final ExpressionsProcessor expressionProcessor = new ExpressionsProcessor(root, functionsProcessor);
    expressionProcessor.process();
    final BuiltinTypesInferrer bti = new BuiltinTypesInferrer(root, functionsProcessor);
    bti.process();
    inferredTypes.addAll(bti.getInferredTypes());
    
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
