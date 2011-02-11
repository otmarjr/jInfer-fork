/*
 *  Copyright (C) 2011 rio
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

package cz.cuni.mff.ksi.jinfer.basicxsd.utils;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;

/**
 * Utility class to recognize some specific types of regexp.
 * @author rio
 */
public final class RegexpTypeUtils {

  private RegexpTypeUtils() {
  }

  /**
   * Determines whether a given regexp is alternation of lambda and a node.
   * @param regexp Input regexp.
   * @return If regexp is of the described type, returns true. Else, returns false.
   */
  public static boolean isLambdaNodeAlternation(final Regexp<AbstractStructuralNode> regexp) {
    // Alternation lambda|A for some node A.
    if (regexp.isAlternation()
            && (regexp.getChildren().size() == 2)
            && regexp.getChild(0).isLambda()) {
      return true;
    }
    return false;
  }

  /**
   * Determines whether a given regexp is alternation of lambda and a token.
   * @param regexp Input regexp.
   * @return If regexp is of the described type, returns true. Else, returns false.
   */
  public static boolean isLambdaTokenAlternation(final Regexp<AbstractStructuralNode> regexp) {
    if (regexp.isAlternation()
            && (regexp.getChildren().size() == 2)
            && regexp.getChild(0).isLambda()
            && regexp.getChild(1).isToken()) {
      return true;
    }
    return false;
  }

  /**
   * Determines whether a given regexp is alternation of a SIMPLE_DATA token and any token.
   * @param regexp Input regexp.
   * @return If regexp is of the described type, returns true. Else, returns false.
   */
  public static boolean isSimpleDataTokenAlternation(final Regexp<AbstractStructuralNode> regexp) {
    if (regexp.isAlternation()
            && (regexp.getChildren().size() == 2)
            && regexp.getChild(0).isToken()
            && regexp.getChild(0).getContent().isSimpleData()
            && regexp.getChild(1).isToken()) {
      return true;
    }
    return false;
  }
}
