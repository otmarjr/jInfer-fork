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

import cz.cuni.mff.ksi.jinfer.base.objects.Attribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility functions for attribute domain handling.
 *
 * Domain is the range of values an attribute may have.
 * In the context of this class, it is a hash map of 
 * <code>(attribute value, number of times it was used)</code>.
 * 
 * @author vektor
 */
public final class DomainUtils {

  private DomainUtils() {
  }

  /**
   * Returns the domain of values of this attribute.
   */
  public static Map<String, Integer> getDomain(final Attribute attribute) {
    final Map<String, Integer> domainMap = new HashMap<String, Integer>();
    if (attribute.getContent() != null && !attribute.getContent().isEmpty()) {
      for (final Object o : attribute.getContent()) {
        if (domainMap.containsKey((String) o)) {
          domainMap.put((String) o, Integer.valueOf(domainMap.get((String) o).intValue() + 1));
        }
        else {
          domainMap.put((String) o, Integer.valueOf(1));
        }
      }
    }
    return domainMap;
  }  

  /**
   * Returns the string definition of this domain.
   * 
   * Decides whether this domain is small enough to be enumerated in the
   * attribute definition, or whether it should be an ambiguous CDATA.
   * 
   * @param threshold Domains smaller or equal to this will be returned as an enum, larger as CDATA.
   * @return String defining (describing) this domain.
   */
  public static String getDomainType(final Map<String, Integer> domain, final int threshold) {
    if (domain.size() <= threshold) {
      final List<String> domainValues = new ArrayList<String>(domain.keySet());
      Collections.sort(domainValues);
      return " " + CollectionToString.colToString(
              domainValues,
              '|',
              CollectionToString.IDEMPOTENT) + " ";
    }
    return " CDATA ";
  }

}
