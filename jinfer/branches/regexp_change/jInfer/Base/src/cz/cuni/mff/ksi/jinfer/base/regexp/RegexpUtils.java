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
package cz.cuni.mff.ksi.jinfer.base.regexp;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.List;

/**
 *
 * @author rio
 * TODO rio comment
 */
public final class RegexpUtils {

  private RegexpUtils() {}

  public static Regexp<AbstractNode> omitAttributes(final Regexp<AbstractNode> regexp) {
    switch (regexp.getType()) {
      case LAMBDA:
        return regexp;
      case TOKEN:
        if (regexp.getContent().isAttribute()) {
          return Regexp.<AbstractNode>getLambda();
        }
        return regexp;
      case CONCATENATION:
      case ALTERNATION:
      case PERMUTATION:
        final List<Regexp<AbstractNode>> nonAttributeChildren = BaseUtils.filter(regexp.getChildren(),
                new BaseUtils.Predicate<Regexp<AbstractNode>>() {

                  @Override
                  public boolean apply(final Regexp<AbstractNode> regexp) {
                    return !regexp.isToken() || !regexp.getContent().isAttribute();
                  }
                });

        if (nonAttributeChildren.isEmpty()) {
          assert(false);
        }

        if (nonAttributeChildren.size() == 1) {
          return nonAttributeChildren.get(0);
        }

        return new Regexp<AbstractNode>(null, nonAttributeChildren, regexp.getType(), regexp.getInterval());

      default:
        assert(false);
    }
    
    // non-reachable
    assert(false);
    return null;
  }
}
