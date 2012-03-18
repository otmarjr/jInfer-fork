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

import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.merger.Merger;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.keydiscovery.KeysInferrer;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.keys.Key;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.keys.ForeignKey;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Processor;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.XQueryProcessor;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.XQueryProcessorCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.FileUtils;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.ModuleNode;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.builtintypeinference.InferredTypeStatement;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.keydiscovery.summary.SummarizedKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 * The main class of this module. It is a basic implementation
 * of {@link XQueryProcessor} interface.
 * 
 * It takes two inputs. One is a simplifier grammar, the second is a data structure
 * holding all project input files. 
 * It selects XQuery queries from the input files,
 * for each it parses it to create its syntax tree. The syntax trees are then
 * processed resulting in inferred statements.
 * Finally, the inferred statements are written to the metadata of the grammar
 * and the grammar is passed to the next module.
 *
 * @author rio
 */
@ServiceProvider(service = XQueryProcessor.class)
public class XQueryAnalyzerImpl implements XQueryProcessor {
  
  private final static Logger LOG = Logger.getLogger(XQueryAnalyzerImpl.class);
  
  private static final String NAME = "Basic_XQuery_Processor";
  private static final String DISPLAY_NAME = "Basic XQuery Processor";

  @Override
  public void start(final Input input, final List<Element> grammar, final XQueryProcessorCallback callback) throws InterruptedException {
    List<ModuleNode> xquerySyntaxTrees = (processXQueries(input.getQueries(), getXQueryProcessor()));
    LOG.info("Input XQuery files parsed into " + xquerySyntaxTrees.size() + " syntax trees");
    
    final List<InferredTypeStatement> inferredTypes = new ArrayList<InferredTypeStatement>();
    final KeysInferrer keysInferrer = new KeysInferrer();
    
    for (final ModuleNode root : xquerySyntaxTrees) {
      final SyntaxTreeProcessor syntaxTreeProcessor = new SyntaxTreeProcessor(root);
      syntaxTreeProcessor.process();
      inferredTypes.addAll(syntaxTreeProcessor.getInferredTypes());
      keysInferrer.addJoinPatterns(syntaxTreeProcessor.getJoinPatterns());
      keysInferrer.addNegativeUniquenessStatements(syntaxTreeProcessor.getNegativeUniquenessStatements());
    }
    
    LOG.info("Total Number of inferred type statements: " + inferredTypes.size());
    
    keysInferrer.summarize();
    final Collection<SummarizedKey> keys = keysInferrer.getKeys();
    final Map<Key, Set<ForeignKey>> foreignKeys = keysInferrer.getForeignKeys();
    LOG.info("Total number of inferred key statements: " + keys.size());
    
    final Merger merger = new Merger(grammar);
    merger.mergeInferredTypes(inferredTypes);
    merger.mergeInferredKeys(keys, foreignKeys);
    
    callback.finished(grammar);
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
  
}
