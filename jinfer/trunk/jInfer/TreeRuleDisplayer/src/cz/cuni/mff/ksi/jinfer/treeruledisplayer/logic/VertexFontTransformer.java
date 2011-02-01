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
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import java.awt.Font;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author sviro
 */
class VertexFontTransformer implements Transformer<Regexp<? extends AbstractNamedNode>, Font> {

  @Override
  public Font transform(final Regexp<? extends AbstractNamedNode> regexp) {
    if (RegexpType.LAMBDA.equals(regexp.getType())) {
      return new Font(null, Font.BOLD, 20);
    }

    return new Font(null, Font.PLAIN, 12);
  }
}
