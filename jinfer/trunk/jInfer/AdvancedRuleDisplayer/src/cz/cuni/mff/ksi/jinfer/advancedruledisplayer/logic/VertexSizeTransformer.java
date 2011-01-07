/*
 *  Copyright (C) 2011 sviro
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
package cz.cuni.mff.ksi.jinfer.advancedruledisplayer.logic;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import java.util.List;
import org.apache.commons.collections15.Transformer;

/**
 * Transformer for Rule Tree Vertex which transform {@link Regexp} into Vertex size.
 * @author sviro
 */
public class VertexSizeTransformer implements Transformer<Regexp<AbstractStructuralNode>, Integer> {

  private final List<Regexp<AbstractStructuralNode>> roots;
  private int root;
  private int token;
  private int concat;
  private int alter;
  private int permut;

  public VertexSizeTransformer(List<Regexp<AbstractStructuralNode>> roots) {
    this.roots = roots;
    root = Utils.getProperty(Utils.ROOT_SIZE_PROP, Utils.ROOT_SIZE_DEFAULT);
    token = Utils.getProperty(Utils.TOKEN_SIZE_PROP, Utils.TOKEN_SIZE_DEFAULT);
    concat = Utils.getProperty(Utils.CONCAT_SIZE_PROP, Utils.CONCAT_SIZE_DEFAULT);
    alter = Utils.getProperty(Utils.ALTER_SIZE_PROP, Utils.ALTER_SIZE_DEFAULT);
    permut = Utils.getProperty(Utils.PERMUT_SIZE_PROP, Utils.PERMUT_SIZE_DEFAULT);
  }

  @Override
  public Integer transform(Regexp<AbstractStructuralNode> regexp) {
    switch (regexp.getType()) {
      case LAMBDA:
        return 50;
      case TOKEN:
        if (roots.contains(regexp)) {
          return root;
        } else {
          return token;
        }
      case ALTERNATION:
        return alter;
      case CONCATENATION:
        return concat;
      case PERMUTATION:
        return permut;
      default:
        return null;
    }
  }
}
