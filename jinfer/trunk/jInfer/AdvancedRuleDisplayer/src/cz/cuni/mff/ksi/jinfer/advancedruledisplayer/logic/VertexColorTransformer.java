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
import java.awt.Color;
import java.awt.Paint;
import java.util.List;
import org.apache.commons.collections15.Transformer;

/**
 * Transformer for Rule Tree Vertex which transform {@link Regexp} into Vertex color.
 * @author sviro
 */
public class VertexColorTransformer implements Transformer<Regexp<AbstractStructuralNode>, Paint> {

  private Color rootColor;
  private Color tokenColor;
  private Color concatColor;
  private Color alterColor;
  private Color permutColor;
  private Color lambdaColor;
  private final List<Regexp<AbstractStructuralNode>> roots;

  public VertexColorTransformer(List<Regexp<AbstractStructuralNode>> roots) {
    this.rootColor = Utils.getColorProperty(Utils.ROOT_COLOR_PROP, Utils.ROOT_COLOR_DEFAULT);
    this.tokenColor = Utils.getColorProperty(Utils.TOKEN_COLOR_PROP, Utils.TOKEN_COLOR_DEFAULT);
    this.concatColor = Utils.getColorProperty(Utils.CONCAT_COLOR_PROP, Utils.CONCAT_COLOR_DEFAULT);
    this.alterColor = Utils.getColorProperty(Utils.ALTER_COLOR_PROP, Utils.ALTER_COLOR_DEFAULT);
    this.permutColor = Utils.getColorProperty(Utils.PERMUT_COLOR_PROP, Utils.PERMUT_COLOR_DEFAULT);
    this.lambdaColor = Utils.getColorProperty(Utils.LAMBDA_COLOR_PROP, Utils.LAMBDA_COLOR_DEFAULT);
    this.roots = roots;
  }

  @Override
  public Paint transform(Regexp<AbstractStructuralNode> regexp) {
    switch (regexp.getType()) {
      case LAMBDA:
        return lambdaColor;
      case TOKEN:
        if (roots.contains(regexp)) {
          return rootColor;
        } else {
          return tokenColor;
        }
      case ALTERNATION:
        return alterColor;
      case CONCATENATION:
        return concatColor;
      case PERMUTATION:
        return permutColor;
      default:
        return null;
    }
  }
}
