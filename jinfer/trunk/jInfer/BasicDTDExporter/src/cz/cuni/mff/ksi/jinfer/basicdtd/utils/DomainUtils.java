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

import cz.cuni.mff.ksi.jinfer.base.utils.CollectionToString;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
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

  /** DTD's way of saying the attribute has an implied value. */
  public static final String ATTRIBUTE_IMPLIED = "#IMPLIED";
  /** DTD's way of saying the attribute contains character data. */
  public static final String ATTRIBUTE_CDATA = " CDATA ";

  /**
   * Returns the domain of values of this attribute.
   */
  public static Map<String, Integer> getDomain(final Attribute attribute) {
    final Map<String, Integer> domainMap = new HashMap<String, Integer>();
    if (!BaseUtils.isEmpty(attribute.getContent())) {
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
   * Returns the default value for this domain (that is the value found with
   * probability higher than ratio) or #IMPLIED, if there is no dominant value.
   * 
   * @param ratio Minimal ratio of a single value occurence, above which it is
   *    declared dominant and therefore default.
   * @return Default value in double quotes or the keyword #IMPLIED.
   */
  public static String getDefault(final Map<String, Integer> domain, final double ratio) {
    final long dominantAbsolute = Math.round(DomainUtils.getDomainAbsoluteSize(domain) * ratio);

    for (final Map.Entry<String, Integer> e : domain.entrySet()) {
      if (e.getValue().intValue() > dominantAbsolute) {
        return '"' + e.getKey() + '"';
      }
    }
    return ATTRIBUTE_IMPLIED;
  }

  /**
   * For domain D of attribute A returns its description, or the <i>type</i> of A.
   * 
   * Decides whether this domain is small enough to be enumerated in the
   * attribute definition, or whether it should be an ambiguous CDATA.
   * 
   * @param threshold Domains smaller or equal to this will be returned as an enum, larger as CDATA.
   * @return String defining (describing) this domain.
   */
  public static String getAttributeType(final Map<String, Integer> domain, final int threshold) {
    if (domain.size() <= threshold) {
      final List<String> domainValues = new ArrayList<String>(domain.keySet());
      
      if (BaseUtils.isEmpty(domainValues)) {
        return ATTRIBUTE_CDATA;
      }

      for (final String value : domainValues) {
        if (value.contains(" ")) {
          // we cannot enumerate values containing a space
          return ATTRIBUTE_CDATA;
        }
      }

      Collections.sort(domainValues);
      return " " + CollectionToString.colToString(
              domainValues,
              "|",
              CollectionToString.IDEMPOTENT) + " ";
    }
    return ATTRIBUTE_CDATA;
  }

  private static int getDomainAbsoluteSize(final Map<String, Integer> domain) {
    int domainAbsolute = 0;
    for (final Integer i : domain.values()) {
      domainAbsolute += i.intValue();
    }
    return domainAbsolute;
  }
}
