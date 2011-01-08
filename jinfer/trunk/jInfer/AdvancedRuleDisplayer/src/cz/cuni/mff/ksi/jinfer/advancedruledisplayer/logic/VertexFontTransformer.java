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
import java.awt.Font;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author sviro
 */
class VertexFontTransformer implements Transformer<Regexp<AbstractStructuralNode>, Font>{

  public VertexFontTransformer() {
  }

  @Override
  public Font transform(Regexp<AbstractStructuralNode> regexp) {
    switch (regexp.getType()) {
      case LAMBDA: return new Font(null, Font.BOLD, 20);
      default: return new Font(null, Font.PLAIN, 12);
    }
  }

}
