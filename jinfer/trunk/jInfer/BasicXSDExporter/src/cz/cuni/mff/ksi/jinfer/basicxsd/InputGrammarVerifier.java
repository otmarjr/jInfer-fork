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

package cz.cuni.mff.ksi.jinfer.basicxsd;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Helper class to perform some checks on input grammar.
 * @author rio
 */
public final class InputGrammarVerifier {

  private InputGrammarVerifier() {
  }

  /**
   * Input grammar must not contain more elements with the same names. This is
   * verified by this method.
   * @param input Input grammar.
   * @return On success, true is returned.
   */
  public static boolean verifyUniqueElementNames(final List<Element> input) {
    final Set<String> set = new HashSet<String>();

    for (Element element : input) {
      final boolean isUnique = set.add(element.getName().toLowerCase());
      if (!isUnique) {
        return false;
      }
    }

    set.clear();
    return true;
  }
}
