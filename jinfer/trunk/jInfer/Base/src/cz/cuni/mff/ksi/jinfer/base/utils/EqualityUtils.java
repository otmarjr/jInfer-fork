/*
 *  Copyright (C) 2011 vektor
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
package cz.cuni.mff.ksi.jinfer.base.utils;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.SimpleData;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;

/**
 * Utility class to test equalities in XML representation.
 *
 * <p>
 *   See the integer flags used to specified which features to ignore while comparing. Example usage:
 * </p>
 *
 * <code>
 * EqualityUtils.sameElements(e1, e2, EqualityUtils.IGNORE_METADATA | EqualityUtils.IGNORE_NAME)
 * </code>
 *
 * <p>
 *   to ignore metadata and names of provided elements.
 * </p>
 *
 * @author vektor
 */
public final class EqualityUtils {

  private EqualityUtils() {
  }

  /** While comparing, ignore {@see AbstractStructuralNode#getName() }. */
  public static final int IGNORE_NAME = 1;
  /** While comparing, ignore {@see AbstractStructuralNode#getContext() }. */
  public static final int IGNORE_CONTEXT = 2;
  /** While comparing, ignore {@see AbstractStructuralNode#getMetadata() }. */
  public static final int IGNORE_METADATA = 4;
  /** While comparing, ignore {@see Element#getAttributes() }. */
  public static final int IGNORE_ATTRIBUTES = 8;
  /** While comparing, ignore {@see Element#getSubnodes() }. */
  public static final int IGNORE_SUBNODES = 16;
  /** While comparing, ignore {@see SimpleData#getContentType() }. */
  public static final int IGNORE_CONTENT_TYPE = 32;
  /** While comparing, ignore {@see SimpleData#getContent() }. */
  public static final int IGNORE_CONTENT = 64;

  /**
   * Compares two elements, ignoring specified features.
   *
   * @param e1 Element 1 to compare.
   * @param e2 Element 2 to compare.
   * @param ignore Bitmap of flags to compare. Combine multiple flags with a
   * bitwise OR (pipe character "|").
   * @return True if provided elements are same, ignoring specified features.
   */
  public static boolean sameElements(final Element e1, final Element e2,
          final int ignore) {
    if (e1 == null || e2 == null) {
      throw new IllegalArgumentException("Both elements must not be null");
    }
    if ((ignore & IGNORE_NAME) == 0 && !e1.getName().equals(e2.getName())) {
      return false;
    }
    if ((ignore & IGNORE_CONTEXT) == 0 && !e1.getContext().equals(e2.getContext())) {
      return false;
    }
    if ((ignore & IGNORE_METADATA) == 0 && !e1.getMetadata().equals(e2.getMetadata())) {
      return false;
    }
    if ((ignore & IGNORE_ATTRIBUTES) == 0 && !e1.getAttributes().equals(e2.getAttributes())) {
      return false;
    }
    if ((ignore & IGNORE_SUBNODES) != 0) {
      return true;
    }
    return sameRegexps(e1.getSubnodes(), e2.getSubnodes(), ignore);
  }

  /**
   * Compares two simple data, ignoring specified features.
   *
   * @param sd1 Simple data 1 to compare.
   * @param sd2 Simple data 2 to compare.
   * @param ignore Bitmap of flags to compare. Combine multiple flags with a
   * bitwise OR (pipe character "|").
   * @return True if provided simple data are same, ignoring specified features.
   */
  public static boolean sameSimpleData(final SimpleData sd1, final SimpleData sd2,
          final int ignore) {
    if (sd1 == null || sd2 == null) {
      throw new IllegalArgumentException("Both simple data must not be null");
    }
    if ((ignore & IGNORE_NAME) == 0 && !sd1.getName().equals(sd2.getName())) {
      return false;
    }
    if ((ignore & IGNORE_CONTEXT) == 0 && !sd1.getContext().equals(sd2.getContext())) {
      return false;
    }
    if ((ignore & IGNORE_METADATA) == 0 && !sd1.getMetadata().equals(sd2.getMetadata())) {
      return false;
    }
    if ((ignore & IGNORE_CONTENT_TYPE) == 0 && !sd1.getContentType().equals(sd2.getContentType())) {
      return false;
    }
    if ((ignore & IGNORE_CONTENT) != 0) {
      return true;
    }
    if (sd1.getContent().size() != sd2.getContent().size()) {
      return false;
    }
    for (int i = 0; i < sd1.getContent().size(); i++) {
      if (!sd1.getContent().get(i).equals(sd2.getContent().get(i))) {
        return false;
      }
    }
    return true;
  }

  private static boolean sameRegexps(final Regexp<AbstractStructuralNode> r1,
          final Regexp<AbstractStructuralNode> r2, final int ignore) {
    if (r1 == null || r2 == null) {
      throw new IllegalArgumentException("Both regexps must not be null");
    }
    if (!r1.getType().equals(r2.getType())) {
      return false;
    }
    if (RegexpType.LAMBDA.equals(r1.getType())) {
      return true;
    }
    if (RegexpType.TOKEN.equals(r1.getType())) {
      if (!r1.getContent().getType().equals(r2.getContent().getType())) {
        return false;
      }
      if (r1.getContent().isElement()) {
        return sameElements((Element) r1.getContent(), (Element) r2.getContent(), ignore);
      }
      if (r1.getContent().isSimpleData()) {
        return sameSimpleData((SimpleData) r1.getContent(), (SimpleData) r2.getContent(), ignore);
      }
      throw new IllegalArgumentException("Expected element or simple data");
    }

    if (r1.getChildren().size() != r2.getChildren().size()) {
      return false;
    }

    for (int i = 0; i < r1.getChildren().size(); i++) {
      final Regexp<AbstractStructuralNode> r1n = r1.getChild(i);
      final Regexp<AbstractStructuralNode> r2n = r2.getChild(i);
      if (!sameRegexps(r1n, r2n, ignore)) {
        return false;
      }
    }

    return true;
  }

  /**
   * Compare two intervals if they have equal minimum and maximum.
   * Intervals must not be null. If both intervals are unbounded and they have the same minimum,
   * they are considered equal.
   * @param i1 First interval to be compared.
   * @param i2 Second interval to be compared.
   * @return True if minimums and maximums of both intervals match, false otherwise.
   */
  public static boolean sameIntervals(final RegexpInterval i1, final RegexpInterval i2) {
    if (i1 == null || i2 == null) {
      throw new IllegalArgumentException("Both intervals must not be null");
    } else if (i1.isUnbounded() && !i2.isUnbounded()) {
      return false;
    } else if (!i1.isUnbounded() && i2.isUnbounded()) {
      return false;
    } else if (!i1.isUnbounded() && !i2.isUnbounded() && i1.getMax() != i2.getMax()) {
      return false;
    }

    if (i1.getMin() != i2.getMin()) {
      return false;
    }
    return true;
  }
}
