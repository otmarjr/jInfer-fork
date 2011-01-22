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
@SuppressWarnings("PMD.SystemPrintln")
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
}
