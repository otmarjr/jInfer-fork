/*
 *  Copyright (C) 2011 reseto
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

import cz.cuni.mff.ksi.jinfer.base.utils.EqualityUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author reseto
 */
@SuppressWarnings("PMD.SystemPrintln")
public class RegexpIntervalTest {

  @Test(expected = IllegalArgumentException.class)
  public void testIntersectIntervalsNull() {
    System.out.println("intersect null");
    final int min = 42;
    final int max = 42;
    final RegexpInterval i1 = RegexpInterval.getBounded(min, max);
    final RegexpInterval i2 = null;
    final RegexpInterval intersection = RegexpInterval.intersectIntervals(i1, i2);
    final RegexpInterval expInterval = RegexpInterval.getBounded(min, max);
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameIntervals(expInterval, intersection);
    assertEquals(expResult, result);
  }
  
  @Test
  public void testIntersectIntervalsOnce() {
    System.out.println("intersect once");
    final RegexpInterval i1 = RegexpInterval.getOnce();
    final RegexpInterval i2 = RegexpInterval.getOnce();
    final RegexpInterval intersection = RegexpInterval.intersectIntervals(i1, i2);
    final RegexpInterval expInterval = RegexpInterval.getOnce();
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameIntervals(expInterval, intersection);
    assertEquals(expResult, result);
  }
  
  @Test
  public void testIntersectIntervalsUnbounded() {
    System.out.println("intersect unbounded");
    final int min = 42;
    final RegexpInterval i1 = RegexpInterval.getUnbounded(min);
    final RegexpInterval i2 = RegexpInterval.getUnbounded(min-5);
    final RegexpInterval intersection = RegexpInterval.intersectIntervals(i1, i2);
    final RegexpInterval expInterval = RegexpInterval.getUnbounded(min);
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameIntervals(expInterval, intersection);
    assertEquals(expResult, result);
  }

  @Test
  public void testIntersectIntervalsBounded() {
    System.out.println("intersect bounded");
    final int min = 42;
    final int max = 42;
    final RegexpInterval i1 = RegexpInterval.getBounded(min, max);
    final RegexpInterval i2 = RegexpInterval.getBounded(min-min-3, max+max);
    final RegexpInterval intersection = RegexpInterval.intersectIntervals(i1, i2);
    final RegexpInterval expInterval = RegexpInterval.getBounded(min, max);
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameIntervals(expInterval, intersection);
    assertEquals(expResult, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIntersectIntervalsWrong() {
    System.out.println("intersect min > max");
    final int min = 1000;
    final int max = 50;
    final RegexpInterval i1 = RegexpInterval.getUnbounded(max);
    final RegexpInterval i2 = RegexpInterval.getBounded(min, max);
    final RegexpInterval intersection = RegexpInterval.intersectIntervals(i1, i2);
    final RegexpInterval expInterval = RegexpInterval.getBounded(max, max);
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameIntervals(expInterval, intersection);
    assertEquals(expResult, result);
  }

  @Test
  public void testIntersectIntervalsTouching() {
    System.out.println("intersect touching");
    final int min = 10;
    final int max = 50;
    final RegexpInterval i1 = RegexpInterval.getUnbounded(max);
    final RegexpInterval i2 = RegexpInterval.getBounded(min, max);
    final RegexpInterval intersection = RegexpInterval.intersectIntervals(i1, i2);
    final RegexpInterval expInterval = RegexpInterval.getBounded(max, max);
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameIntervals(expInterval, intersection);
    assertEquals(expResult, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIntersectIntervalsNoCommon() {
    System.out.println("intersect no common intersection");
    final int min = 10;
    final int max = 50;
    final int min2 = 2000;
    // there is no intersection for <10,50> and <2000,oo>
    final RegexpInterval i1 = RegexpInterval.getUnbounded(min2);
    final RegexpInterval i2 = RegexpInterval.getBounded(min, max);
    final RegexpInterval intersection = RegexpInterval.intersectIntervals(i1, i2);
    //intersect intervals should now return
    final RegexpInterval expInterval = RegexpInterval.getBounded(max, min2);
    final boolean expResult = false;
    final boolean result = EqualityUtils.sameIntervals(expInterval, intersection);
    assertEquals(expResult, result);
  }
}