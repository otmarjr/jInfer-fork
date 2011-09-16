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
package cz.cuni.mff.ksi.jinfer.base.utils;

import java.util.Collection;
import java.util.Set;
import java.util.Collections;
import java.util.Arrays;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils.Predicate;
import java.util.HashSet;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author vektor
 */
@SuppressWarnings({"PMD.SystemPrintln", "PMD.DataflowAnomalyAnalysis"})
public class BaseUtilsTest {

  @Test(expected = IllegalArgumentException.class)
  public void testFilterNull1() {
    System.out.println("filterNull1");
    final List<String> target = null;
    final Predicate<String> predicate = new Predicate<String>() {

      @Override
      public boolean apply(final String argument) {
        return "a".equals(argument);
      }
    };
    BaseUtils.filter(target, predicate);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFilterNull2() {
    System.out.println("filterNull2");
    final List<String> target = Collections.emptyList();
    final Predicate<String> predicate = null;
    BaseUtils.filter(target, predicate);
  }

  @Test
  public void testFilterEmpty() {
    System.out.println("filterEmpty");
    final List<String> target = Collections.emptyList();
    final Predicate<String> predicate = new Predicate<String>() {

      @Override
      public boolean apply(final String argument) {
        return "a".equals(argument);
      }
    };
    final List<String> expResult = Collections.emptyList();
    final List<String> result = BaseUtils.filter(target, predicate);
    assertEquals(expResult, result);
  }

  @Test
  public void testFilter() {
    System.out.println("filter");
    final List<String> target = Arrays.asList("a", "b", "c", "a");
    final Predicate<String> predicate = new Predicate<String>() {

      @Override
      public boolean apply(final String argument) {
        return "a".equals(argument);
      }
    };
    final List<String> expResult = Arrays.asList("a", "a");
    final List<String> result = BaseUtils.filter(target, predicate);
    assertEquals(expResult, result);

    final Predicate<String> predicate2 = new Predicate<String>() {

      @Override
      public boolean apply(final String argument) {
        return "X".equals(argument);
      }
    };
    final List<String> expResult2 = Collections.emptyList();
    final List<String> result2 = BaseUtils.filter(target, predicate2);
    assertEquals(expResult2, result2);
  }

  @Test
  public void testIsEmpty_Collection() {
    System.out.println("isEmptyCollection");
    final List<String> nll = null;
    assertEquals(true, BaseUtils.isEmpty(nll));
    assertEquals(true, BaseUtils.isEmpty(Collections.emptyList()));
    final List<String> fullList = Arrays.asList("a");
    assertEquals(false, BaseUtils.isEmpty(fullList));
    final Set<String> fullSet = new HashSet<String>();
    assertEquals(true, BaseUtils.isEmpty(fullSet));
    fullSet.add("a");
    assertEquals(false, BaseUtils.isEmpty(fullSet));
  }

  @Test
  public void testIsEmpty_String() {
    System.out.println("isEmptyString");
    assertEquals(true, BaseUtils.isEmpty((String) null));
    assertEquals(true, BaseUtils.isEmpty(""));
    assertEquals(false, BaseUtils.isEmpty("a"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCloneListNull() {
    System.out.println("cloneListNull");
    BaseUtils.cloneList(null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCloneListNegative() {
    System.out.println("cloneListNegative");
    BaseUtils.cloneList(Collections.emptyList(), -1);
  }

  @Test
  public void testCloneListEmpty() {
    System.out.println("cloneListEmpty");
    final List<String> expResult = Collections.emptyList();
    final List<String> result = BaseUtils.cloneList(Collections.<String>emptyList(), 5);
    assertEquals(expResult, result);
  }

  @Test
  public void testCloneListZero() {
    System.out.println("cloneListZero");
    final List<String> expResult = Collections.emptyList();
    final List<String> result = BaseUtils.cloneList(Arrays.asList("a", "b", "c"), 0);
    assertEquals(expResult, result);
  }

  @Test
  public void testCloneList1() {
    System.out.println("cloneList1");
    final List<String> expResult = Arrays.asList("a", "a", "a");
    final List<String> result = BaseUtils.cloneList(Arrays.asList("a"), 3);
    assertEquals(expResult, result);
  }

  @Test
  public void testCloneList2() {
    System.out.println("cloneList2");
    final List<String> expResult = Arrays.asList("a", "b", "a", "b", "a", "b");
    final List<String> result = BaseUtils.cloneList(Arrays.asList("a", "b"), 3);
    assertEquals(expResult, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCrossectionNull() {
    System.out.println("crossectionNull");
    BaseUtils.intersect(null, null);
  }

  @Test
  public void testCrossectionEmpty() {
    System.out.println("crossectionEmpty");
    final Set<String> arg1 = new HashSet<String>(Arrays.asList("a", "b", "c"));
    final Set<String> arg2 = new HashSet<String>();

    final Set<String> ret1 = BaseUtils.intersect(arg1, arg2);
    assertEquals("Expecting empty crossection", arg2, ret1);

    final Set<String> ret2 = BaseUtils.intersect(arg2, arg1);
    assertEquals("Expecting empty crossection", arg2, ret2);
  }

  @Test
  public void testCrossection1() {
    System.out.println("crossection1");
    final Set<String> arg1 = new HashSet<String>(Arrays.asList("a", "b", "c"));
    final Set<String> arg2 = new HashSet<String>(Arrays.asList("b", "c", "d"));
    final Set<String> expected = new HashSet<String>(Arrays.asList("b", "c"));

    final Set<String> ret = BaseUtils.intersect(arg1, arg2);
    assertEquals(expected, ret);
  }

  @Test
  public void testCrossection2() {
    System.out.println("crossection2");
    final Set<String> arg1 = new HashSet<String>(Arrays.asList("a", "b", "c"));
    final Set<String> arg2 = new HashSet<String>(Arrays.asList("d", "e", "f"));
    final Set<String> expected = new HashSet<String>();

    final Set<String> ret = BaseUtils.intersect(arg1, arg2);
    assertEquals(expected, ret);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRndSubsetEmpty() {
    System.out.println("rndSubsetEmpty");
    BaseUtils.rndSubset(null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRndSubsetNegative() {
    System.out.println("rndSubsetNegative");
    BaseUtils.rndSubset(Arrays.asList(1, 2, 3), -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRndSubsetTooMuch() {
    System.out.println("rndSubsetTooMuch");
    BaseUtils.rndSubset(Arrays.asList(1, 2, 3), 4);
  }

  @Test
  public void testRndSubsetAll1() {
    System.out.println("rndSubsetAll1");
    final List<String> ret = BaseUtils.rndSubset(Collections.<String>emptyList(), 0);
    assertEquals(Collections.<String>emptyList(), ret);
  }

  @Test
  public void testRndSubsetAll2() {
    System.out.println("rndSubsetAll2");
    final List<String> parameter = Arrays.asList("1", "2", "3");
    final List<String> ret = BaseUtils.rndSubset(parameter, 3);
    assertTrue(ret.containsAll(parameter));
    assertTrue(parameter.containsAll(ret));
  }

  @Test
  public void testRndSubsetSome() {
    System.out.println("rndSubsetSome");
    final List<String> parameter = Arrays.asList("1", "2", "3", "4", "5");
    for (int i = 0; i < 6; i++) {
      final List<String> ret = BaseUtils.rndSubset(parameter, i);
      assertEquals(i, ret.size());
      assertTrue(parameter.containsAll(ret));
    }
  }

  @Test
  public void testIsSubset() {
    System.out.println("isSubset");
    final Collection<String> set1 = Arrays.asList("a", "b", "c", "d", "e");
    final Collection<String> set2 = Arrays.asList("a", "b", "c");
    final Collection<String> set3 = Collections.emptyList();

    assertTrue(BaseUtils.isSubset(set1, set1));
    assertTrue(BaseUtils.isSubset(set2, set2));
    assertTrue(BaseUtils.isSubset(set3, set3));

    assertTrue(BaseUtils.isSubset(set2, set1));
    assertFalse(BaseUtils.isSubset(set1, set2));
    assertTrue(BaseUtils.isSubset(set3, set1));
    assertTrue(BaseUtils.isSubset(set3, set2));
  }

  @Test
  public void testEquals() {
    assertTrue(BaseUtils.equal(null, null));
    assertFalse(BaseUtils.equal("a", null));
    assertFalse(BaseUtils.equal(null, "a"));
    assertTrue(BaseUtils.equal("a", "a"));
    assertFalse(BaseUtils.equal("a", "b"));
  }

}
