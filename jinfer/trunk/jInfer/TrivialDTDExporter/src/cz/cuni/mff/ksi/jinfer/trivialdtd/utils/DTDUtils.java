/*
 *  Copyright (C) 2010 vektor
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
package cz.cuni.mff.ksi.jinfer.trivialdtd.utils;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.List;

/**
 * Some utilities for DTD export.
 * 
 * @author vektor
 */
public final class DTDUtils {

  /** Library class. */
  private DTDUtils() {
  }

  public static List<Regexp<AbstractNode>> omitAttributes(
          final List<Regexp<AbstractNode>> col) {
    return BaseUtils.filter(col, new BaseUtils.Predicate<Regexp<AbstractNode>>() {

      @Override
      public boolean apply(final Regexp<AbstractNode> r) {
        return !r.isToken() || !r.getContent().isAttribute();
      }
    });
  }
}
