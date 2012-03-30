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
package cz.cuni.mff.ksi.jinfer.treeruledisplayer.logic;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractNamedNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import java.util.List;
import org.apache.commons.collections15.Transformer;

/**
 * Transformer for Rule Tree Vertex which transform {@link Regexp} into Vertex size.
 * @author sviro
 */
public class VertexSizeTransformer implements Transformer<Regexp<? extends AbstractNamedNode>, Integer> {

  private final List<Regexp<AbstractStructuralNode>> roots;
  private final int root;
  private final int token;
  private final int concat;
  private final int alter;
  private final int permut;
  private final int lambda;
  private final int simpleData;
  private final int attribute;

  /**
   * Default contructor.
   * @param roots List of root Regexp of each rule tree.
   */
  public VertexSizeTransformer(final List<Regexp<AbstractStructuralNode>> roots) {
    this.roots = roots;
    this.root = Utils.getProperty(Utils.ROOT_SIZE_PROP, Utils.ROOT_SIZE_DEFAULT);
    this.token = Utils.getProperty(Utils.ELEMENT_SIZE_PROP, Utils.ELEMENT_SIZE_DEFAULT);
    this.concat = Utils.getProperty(Utils.CONCAT_SIZE_PROP, Utils.CONCAT_SIZE_DEFAULT);
    this.alter = Utils.getProperty(Utils.ALTER_SIZE_PROP, Utils.ALTER_SIZE_DEFAULT);
    this.permut = Utils.getProperty(Utils.PERMUT_SIZE_PROP, Utils.PERMUT_SIZE_DEFAULT);
    this.lambda = Utils.getProperty(Utils.LAMBDA_SIZE_PROP, Utils.LAMBDA_SIZE_DEFAULT);
    this.simpleData = Utils.getProperty(Utils.SIMPLE_DATA_SIZE_PROP, Utils.SIMPLE_DATA_SIZE_DEFAULT);
    this.attribute = Utils.getProperty(Utils.ATTRIBUTE_SIZE_PROP, Utils.ATTRIBUTE_SIZE_DEFAULT);

  }

  @Override
  public Integer transform(final Regexp<? extends AbstractNamedNode> regexp) {
    switch (regexp.getType()) {
      case LAMBDA:
        return lambda;
      case TOKEN:
        if (roots.contains(regexp)) {
          return root;
        } else if (regexp.getContent() instanceof AbstractStructuralNode) {
          if (((AbstractStructuralNode) regexp.getContent()).isSimpleData()) {
            return simpleData;
          } else {
            return token;
          }
        } else {
          return attribute;
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
