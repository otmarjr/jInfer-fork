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
package cz.cuni.mff.ksi.jinfer.base.objects;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.ModuleNode;
import java.util.List;

/**
 * Encapsulation of data that need to be transfered between modules in the chain
 * of inference.
 * 
 * Main component is a list of {@link cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element}s
 * representing a grammar.
 * 
 * If needed, this class can be extended to hold additional data. This may be
 * useful in development of new modules which require passing data that does
 * not fit into previously defined structures.
 * 
 * Example of such extension is addition of syntax trees retrieved from input
 * XQuery files.
 * 
 * @author rio
 */
public class InferenceDataHolder {
  
  private List<Element> grammar;
  private final List<ModuleNode> xquerySyntaxTrees;
  
  public InferenceDataHolder(final List<Element> grammar, final List<ModuleNode> xquerySyntaxTrees) {
    this.grammar = grammar;
    this.xquerySyntaxTrees = xquerySyntaxTrees;
  }

  public List<Element> getGrammar() {
    return grammar;
  }

  public void setGrammar(List<Element> grammar) {
    this.grammar = grammar;
  }
  
  public List<ModuleNode> getXQuerySyntaxTrees() {
    return xquerySyntaxTrees;
  }
  
  public void addToGrammar(final List<Element> rules) {
    grammar.addAll(rules);
  }
  
}
