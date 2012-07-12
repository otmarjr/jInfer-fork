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
package cz.cuni.mff.ksi.jinfer.xqueryimporter;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Processor;
import cz.cuni.mff.ksi.jinfer.base.objects.FolderType;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.ModuleNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.XQNode;
import cz.cuni.mff.ksi.jinfer.xqueryimporter.xqanalyzer.XQConverter;
import cz.cuni.mff.ksi.jinfer.xqueryimporter.xqanalyzer.XQParseException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Implementation of {@link cz.cuni.mff.ksi.jinfer.base.interfaces.Processor}
 * providing logic for parsing XQuery 1.0 queries and creating their syntax trees.
 * 
 * @author rio
 */
@ServiceProvider(service = Processor.class)
public class XQueryProcessor implements Processor<ModuleNode> {
  
  private static final Logger LOG = Logger.getLogger(XQueryProcessor.class);

  @Override
  public String getExtension() {
    return "xq";
  }

  @Override
  public FolderType getFolder() {
    return FolderType.QUERY;
  }

  @Override
  public Class<?> getResultType() {
    return ModuleNode.class;
  }

  /**
   * Parses the file containing an XQuery query and returns the
   * query in a form of the syntax tree.
   * 
   * The {@link cz.cuni.mff.ksi.jinfer.base.interfaces.Processor} interface
   * requires List return type. Since the query is represented by exactly one
   * syntax tree, the List contains just this one item.
   *
   * @param s File containing an XQuery query.
   * @return List containing a syntax tree of the query at index 0 or empty list
   *   in a case of error.
   */
  @Override
  public List<ModuleNode> process(InputStream s) throws InterruptedException {
    final XQConverter converter = new XQConverter(s);
    try {
      converter.convert();
    } catch (final XQParseException e) {
      LOG.error("Error parsing XQuery file, ignoring and going on.", e);
      return Collections.EMPTY_LIST;
    }
    
    final ModuleNode syntaxTree = converter.getModuleNode();
    initParentReferences(syntaxTree);
    
    final List<ModuleNode> result = new ArrayList<ModuleNode>();
    result.add(syntaxTree);
    return result;
  }

  @Override
  public boolean processUndefined() {
    return false;
  }
  
  /**
   * Initializes references to a parent node for every node in a specified
   * syntax tree. This is needed because the parser does not create these
   * references.
   * @param root A syntax tree to be initialized.
   */
  private static void initParentReferences(final XQNode root) {
    final List<XQNode> subnodes = root.getSubnodes();
    if (subnodes != null) {
      for (final XQNode subnode : subnodes) {
        subnode.setParentNode(root);
        initParentReferences(subnode);
      }
    }
  }
  
}
