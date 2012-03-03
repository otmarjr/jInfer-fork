/*
 *  Copyright (C) 2010 vektor
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.basicigg;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Expander;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.IGGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.IGGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.FolderType;
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.FileUtils;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Processor;
import cz.cuni.mff.ksi.jinfer.base.objects.InferenceDataHolder;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.ModuleNode;
import cz.cuni.mff.ksi.jinfer.base.utils.CloneHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.IGGUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.RuleDisplayerHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 * A trivial implementation of IGGenerator module. Works with XML documents,
 * DTD schemas, XPath queries and provides an extension framework to enable
 * support for additional languages
 * (see {@link cz.cuni.mff.ksi.jinfer.base.interfaces.Processor}).
 *
 * @author vektor
 */
@ServiceProvider(service = IGGenerator.class)
public class IGGeneratorImpl implements IGGenerator {

  private static final Logger LOG = Logger.getLogger(IGGeneratorImpl.class);
  private static final String NAME = "Basic_IG_Generator";
  private static final String DISPLAY_NAME = "Basic IG generator";


  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getModuleDescription() {
    return getDisplayName();
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.emptyList();
  }

  @Override
  public void start(final Input input, final IGGeneratorCallback callback)
          throws InterruptedException {
    // find processor mappings for all folders
    final Map<FolderType, Map<String, Processor<Element>>> registeredProcessors = getRegisteredProcessors();

    final List<Element> documentRules = new ArrayList<Element>();
    final List<Element> schemaQueryRules = new ArrayList<Element>();
    final List<ModuleNode> xquerySyntaxTrees = new ArrayList<ModuleNode>();

    // run processors on input, gather IG rules
    documentRules.addAll(getRulesFromInput(input.getDocuments(), registeredProcessors.get(FolderType.DOCUMENT)));
    verifySimpleGrammar(documentRules);
    schemaQueryRules.addAll(getRulesFromInput(input.getSchemas(), registeredProcessors.get(FolderType.SCHEMA)));
    schemaQueryRules.addAll(getRulesFromInput(input.getQueries(), registeredProcessors.get(FolderType.QUERY)));
    
    // the XQuery processor differs from the other processors by creating syntax trees of supplied queries
    // instead of IG rules, and thus, it has to be handled separately
    xquerySyntaxTrees.addAll(processXQueries(input.getQueries(), getXQueryProcessor()));
    
    final InferenceDataHolder idh = new InferenceDataHolder(documentRules, xquerySyntaxTrees);

    // if there are no schema/query rules, or the next module can handle simple
    // grammar, just output all of it without expansion
    if (BaseUtils.isEmpty(schemaQueryRules)
            || RunningProject.getNextModuleCaps().getCapabilities().contains("can.handle.complex.regexps")) {
      //documentRules.addAll(schemaQueryRules);
      idh.addToGrammar(schemaQueryRules);

      // show the rules
      //RuleDisplayerHelper.showRulesAsync("IG", new CloneHelper().cloneGrammar(documentRules), true);
      RuleDisplayerHelper.showRulesAsync("IG", new CloneHelper().cloneGrammar(idh.getGrammar()), true);

      //callback.finished(documentRules);
      callback.finished(idh);
      return;
    }

    // otherwise, we have to expand
    // show the rules before expansion
    final List<Element> before = new ArrayList<Element>();
    before.addAll(documentRules);
    before.addAll(schemaQueryRules);
    RuleDisplayerHelper.showRulesAsync("Raw", new CloneHelper().cloneGrammar(before), true);

    // lookup expander
    final Expander expander = Lookup.getDefault().lookup(Expander.class);
    final List<Element> expanded = expander.expand(schemaQueryRules);

    //final List<Element> ret = new ArrayList<Element>();
    //ret.addAll(documentRules);
    //ret.addAll(expanded);
    idh.addToGrammar(expanded);

    // show the rules after expansion
    //RuleDisplayerHelper.showRulesAsync("Expanded", new CloneHelper().cloneGrammar(ret), true);
    RuleDisplayerHelper.showRulesAsync("Expanded", new CloneHelper().cloneGrammar(idh.getGrammar()), true);

    // return expanded
    callback.finished(idh);
  }

  /**
   * Retrieves IG rules from provided input files.
   *
   * @param files A collection of input files from which rules will be retrieved.
   * @param mappings {@link Map} of pairs (extension - processor). Extension is
   * a lowercase file extension (e.g. "dtd"),
   * processor is the {@link Processor} responsible
   * for this file type. If <code>mappings</code> contain the key "*", will the
   * associated processor handle all file types.
   *
   * @return List of IG rules. Empty, if there are no input files or an error occurs.
   */
  private static List<Element> getRulesFromInput(final Collection<File> files,
          final Map<String, Processor<Element>> mappings) throws InterruptedException {
    if (BaseUtils.isEmpty(files)) {
      return new ArrayList<Element>(0);
    }

    final List<Element> ret = new ArrayList<Element>();

    for (final File f : files) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      try {
        final Processor<Element> p = getProcessorForExtension(
                FileUtils.getExtension(f.getAbsolutePath()),
                mappings);
        if (p != null) {
          final List<Element> rules = p.process(new FileInputStream(f));
          for (final Element rule : rules) {
            rule.getMetadata().put(IGGUtils.FILE_ORIGIN, f.getAbsoluteFile());
          }
          ret.addAll(rules);
        }
        else {
          // TODO rio This is a hack to not print errors that XQuery files do not
          // have defined mapping.
          if (!FileUtils.getExtension(f.getAbsolutePath()).equals("xq")){
            LOG.error("File extension does not have a corresponding mapping: " + f.getAbsolutePath());
          }
        }
      } catch (final FileNotFoundException e) {
        throw new RuntimeException("File not found: " + f.getAbsolutePath(), e);
      }
    }

    return ret;
  }

  private static Processor<Element> getProcessorForExtension(final String extension,
          final Map<String, Processor<Element>> mappings) {
    if (mappings.containsKey(extension)) {
      return mappings.get(extension);
    }
    if (mappings.containsKey("*")) {
      return mappings.get("*");
    }
    return null;
  }

  /**
   * Returns the map (folder - (extension - processor) ) of all processors
   * installed in this NetBeans which process IG.
   */
  @SuppressWarnings("unchecked")
  private Map<FolderType, Map<String, Processor<Element>>> getRegisteredProcessors() {
    final Map<FolderType, Map<String, Processor<Element>>> ret =
            new HashMap<FolderType, Map<String, Processor<Element>>>();

    for (final FolderType ft : FolderType.values()) {
      ret.put(ft, new HashMap<String, Processor<Element>>());
    }

    for (final Processor p : Lookup.getDefault().lookupAll(Processor.class)) {
      if (p.getResultType().equals(Element.class)) {
        if (p.processUndefined()) {
          ret.get(p.getFolder()).put("*", p);
        }
        ret.get(p.getFolder()).put(p.getExtension(), p);
      }
    }

    return ret;
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

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }

  private void verifySimpleGrammar(final List<Element> documentRules) {
    for (final Element e : documentRules) {
      if (!IGGUtils.isSimpleConcatenation(e.getSubnodes())) {
        throw new RuntimeException("Rule does not comply with simple grammar specification. " + e.toString());
      }
    }
  }
}
