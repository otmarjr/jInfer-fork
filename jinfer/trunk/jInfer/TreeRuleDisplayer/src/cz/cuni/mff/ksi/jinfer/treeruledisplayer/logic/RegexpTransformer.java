/*
 *  Copyright (C) 2010 sviro
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
package cz.cuni.mff.ksi.jinfer.treeruledisplayer.logic;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractNamedNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import org.apache.commons.collections15.Transformer;

/**
 * Transformer for Rule Tree Vertex which transform {@link Regexp} into readeble Vertex label.
 *
 * @author sviro
 */
public class RegexpTransformer implements Transformer<Regexp<? extends AbstractNamedNode>, String> {

  @Override
  public String transform(final Regexp<? extends AbstractNamedNode> regexp) {
    switch (regexp.getType()) {
      case LAMBDA:
        return "\u03BB";
      case TOKEN:
        if (regexp.getContent() instanceof AbstractStructuralNode && ((AbstractStructuralNode) regexp.getContent()).isSimpleData()) {
          return "\"" + regexp.getContent().getName() + "\"";
        }
        return regexp.getContent().getName();
      case ALTERNATION:
      case CONCATENATION:
      case PERMUTATION:
        return regexp.getType().toString();
      default:
        return null;
    }
  }
}
