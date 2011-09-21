/*
 *  Copyright (C) 2010 rio
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

package cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.transformers;

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import org.apache.commons.collections15.Transformer;

/**
 * Transformation of {@link Step} (in {@link Automaton}&#60;{@link Regexp}&#60;{@link AbstractStructuralNode}&#62;&#62;) to {@link String}.
 * @author rio
 */
public class RegexpEdgeLabeller implements Transformer<Step<Regexp<AbstractStructuralNode>>, String> {

  @Override
  public String transform(final Step<Regexp<AbstractStructuralNode>> step) {
    final Regexp<AbstractStructuralNode> regexp = step.getAcceptSymbol();
    return new RegexpToStringTransformer().transform(regexp);
  }
}
