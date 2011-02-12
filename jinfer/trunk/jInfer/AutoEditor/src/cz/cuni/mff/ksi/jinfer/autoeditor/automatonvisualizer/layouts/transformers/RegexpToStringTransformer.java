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

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.utils.CollectionToString;
import org.apache.commons.collections15.Transformer;

/**
 * Transformation of {@link Regexp}&#60;{@link AbstractStructuralNode}&#62; to {@link String}.
 * @author rio
 */
public class RegexpToStringTransformer implements Transformer<Regexp<AbstractStructuralNode>, String> {

  @Override
  public String transform(final Regexp<AbstractStructuralNode> regexp) {

    switch (regexp.getType()) {
      case LAMBDA:
        return regexp.toString();
      case TOKEN:
        return new AbstractStructuralNodeToStringTransformer().transform(regexp.getContent());
      case CONCATENATION:
        return CollectionToString.<Regexp<AbstractStructuralNode>>colToString(regexp.getChildren(), ",", createCollectionToString());
      case ALTERNATION:
        return CollectionToString.<Regexp<AbstractStructuralNode>>colToString(regexp.getChildren(), "|", createCollectionToString());
      case PERMUTATION:
        return CollectionToString.<Regexp<AbstractStructuralNode>>colToString(regexp.getChildren(), "&", createCollectionToString());
      default:
        return regexp.toString();
    }
  }

  private CollectionToString.ToString<Regexp<AbstractStructuralNode>> createCollectionToString() {
    return new CollectionToString.ToString<Regexp<AbstractStructuralNode>>() {

      @Override
      public String toString(final Regexp<AbstractStructuralNode> t) {
        return RegexpToStringTransformer.this.transform(t);
      }
    };
  }
}
