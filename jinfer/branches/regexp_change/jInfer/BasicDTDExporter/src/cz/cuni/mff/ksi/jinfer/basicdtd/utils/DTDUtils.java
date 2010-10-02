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
package cz.cuni.mff.ksi.jinfer.basicdtd.utils;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.ArrayList;
import java.util.Comparator;
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

  /**
   * Returns the list without tokens of the type <strong>attribute<strong>.
   */
  public static List<Regexp<AbstractNode>> omitAttributes(
          final List<Regexp<AbstractNode>> list) {
    return BaseUtils.filter(list, new BaseUtils.Predicate<Regexp<AbstractNode>>() {

      @Override
      public boolean apply(final Regexp<AbstractNode> r) {
        return !r.isToken() || !r.getContent().isAttribute();
      }
    });
  }

  /**
   * A simple comparator on AbstractNode that puts SIMPLE_DATA nodes in the front.
   */
  public static final Comparator<AbstractNode> PCDATA_CMP = new Comparator<AbstractNode>() {

    @Override
    public int compare(final AbstractNode o1, final AbstractNode o2) {
      if (o1.isSimpleData()) {
        return -1;
      }
      if (o2.isSimpleData()) {
        return 1;
      }
      return 0;
    }
  };

  /**
   * Returns true if one of the supplied children is a PCDATA.
   */
  public static boolean containsPCDATA(final List<AbstractNode> children) {
    for (final AbstractNode child : children) {
      if (child.isSimpleData()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Modifies a list so that only one PCDATA element remains in it (the first one).
   */
  public static List<AbstractNode> uniquePCDATA(final List<AbstractNode> l) {
    boolean wasPCData = false;
    final List<AbstractNode> ret = new ArrayList<AbstractNode>(l.size());

    for (final AbstractNode n : l) {
      if (n.isSimpleData()) {
        if (!wasPCData) {
          ret.add(n);
        }
        wasPCData = true;
      } else {
        ret.add(n);
      }
    }

    return ret;
  }
}
