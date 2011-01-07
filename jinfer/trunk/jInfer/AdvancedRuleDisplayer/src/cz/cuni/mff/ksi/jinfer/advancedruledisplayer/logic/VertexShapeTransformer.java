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
import edu.uci.ics.jung.visualization.util.VertexShapeFactory;
import java.awt.Shape;
import java.util.List;
import org.apache.commons.collections15.Transformer;

/**
 * Transformer for Rule Tree Vertex which transform {@link Regexp} into Vertex shape.
 * @author sviro
 */
public class VertexShapeTransformer implements Transformer<Regexp<AbstractStructuralNode>, Shape> {
  private VertexShapeFactory<Regexp<AbstractStructuralNode>> shapeFactory;
  private Utils utils;

  public VertexShapeTransformer(Utils utils) {
    shapeFactory = new VertexShapeFactory<Regexp<AbstractStructuralNode>>(new VertexSizeTransformer(utils.getRoots()), new Transformer<Regexp<AbstractStructuralNode>, Float>() {

      @Override
      public Float transform(Regexp<AbstractStructuralNode> i) {
        return 1f;
      }
    });
    this.utils = utils;
  }

  @Override
  public Shape transform(Regexp<AbstractStructuralNode> regexp) {
    return utils.getVertexShape(shapeFactory, regexp);
  }
}

