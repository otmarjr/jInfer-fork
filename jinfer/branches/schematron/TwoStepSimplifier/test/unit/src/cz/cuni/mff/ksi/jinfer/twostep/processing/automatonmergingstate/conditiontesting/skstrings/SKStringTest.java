/*
 * Copyright (C) 2011 anti
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.skstrings;

import java.util.List;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import java.util.Deque;
import java.util.LinkedList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class SKStringTest {

  public SKStringTest() {
  }

  /**
   * Test of preceede method, of class SKString.
   */
  @Test
  public void testPreceede() {
    System.out.println("preceede");
    Step<String> step = new Step<String>("x", null, null, 7, 7);
    Step<String> step2 = new Step<String>("y", null, null, 6, 6);
    SKString<String> instance = new SKString<String>(step, 0.7);
    instance.preceede(step2, 0.6);
    assertEquals(instance.getProbability(), 0.42, 0.0);
    assertEquals(instance.getStr().size(), 2);
    assertEquals(instance.getStr().getFirst(), step2);
    assertEquals(instance.getStr().getLast(), step);
  }

  @Test
  public void testComplexConstructor() {
    System.out.println("complex constructor");
    Step<String> testStep = new Step<String>("x", null, null, 7, 7);
    SKString<String> instance = new SKString<String>(testStep, 0.7);
    assertEquals(instance.getProbability(), 0.7, 0.0);
    assertEquals(instance.getStr().size(), 1);
    assertEquals(instance.getStr().getFirst(), testStep);
  }

  /**
   * Test of getProbability method, of class SKString.
   */
  @Test
  public void testGetProbability() {
    System.out.println("getProbability");
    SKString<String> instance = new SKString<String>();
    double expResult = 1.0;
    double result = instance.getProbability();
    assertEquals(expResult, result, 0.0);
  }

  /**
   * Test of getStr method, of class SKString.
   */
  @Test
  public void testGetStr() {
    System.out.println("getStr");
    SKString<String> instance = new SKString<String>();
    Deque<Step<String>> result = instance.getStr();
    assertTrue(result.isEmpty());
  }

  /**
   * Test of equals method, of class SKString.
   */
  @Test
  public void testEquals() {
    System.out.println("equals");
    Object obj = null;
    SKString<String> instance = new SKString<String>();
    boolean expResult = false;
    boolean result = instance.equals(obj);
    assertEquals(expResult, result);
  }

  /**
   * Test of equals method, of class SKString.
   */
  @Test
  public void testEquals1() {
    System.out.println("equals");
    String obj = "kkk";
    SKString<String> instance = new SKString<String>();
    boolean expResult = false;
    boolean result = instance.equals(obj);
    assertEquals(expResult, result);
  }

  /**
   * Test of equals method, of class SKString.
   */
  @Test
  public void testEquals2() {
    System.out.println("equals");
    Step<String> step = new Step<String>("x", null, null, 7, 7);
    Step<String> step2 = new Step<String>("y", null, null, 6, 6);
    SKString<String> instance = new SKString<String>(step, 0.7);
    SKString<String> instance2 = new SKString<String>();
    boolean expResult = false;
    boolean result = instance.equals(instance2);
    assertEquals(expResult, result);
  }

  /**
   * Test of equals method, of class SKString.
   */
  @Test
  public void testEquals3() {
    System.out.println("equals");
    Step<String> step = new Step<String>("x", null, null, 7, 7);
    Step<String> step2 = new Step<String>("x", null, null, 6, 6);
    SKString<String> instance = new SKString<String>(step, 0.4);
    SKString<String> instance2 = new SKString<String>(step2, 0.2);
    boolean expResult = true;
    boolean result = instance.equals(instance2);
    assertEquals(expResult, result);
  }

  /**
   * Test of equals method, of class SKString.
   */
  @Test
  public void testEquals4() {
    System.out.println("equals");
    Step<String> step = new Step<String>("x", null, null, 7, 7);
    Step<String> step2 = new Step<String>("x", null, null, 6, 6);
    SKString<String> instance = new SKString<String>(step, 0.4);
    SKString<String> instance2 = new SKString<String>(step2, 0.2);

    List<SKString<String>> list0 = new LinkedList<SKString<String>>();
    list0.add(instance);
    List<SKString<String>> list1 = new LinkedList<SKString<String>>();
    list1.add(instance2);
    List<SKString<String>> list2 = new LinkedList<SKString<String>>();
    list2.add(instance);
    list2.add(instance2);

    assertTrue(list2.containsAll(list0));
    assertTrue(list2.containsAll(list1));
    assertTrue(list0.containsAll(list1));
    assertTrue(list1.containsAll(list0));
  }  
  
  /**
   * Test of hashCode method, of class SKString.
   */
  @Test
  public void testHashCode() {
    System.out.println("hashCode");
    SKString<String> instance = new SKString<String>();
    assertEquals(instance.hashCode(), 5);
  }

  /**
   * Test of hashCode method, of class SKString.
   */
  @Test
  public void testHashCode1() {
    System.out.println("hashCode");
    Step<String> step = new Step<String>("x", null, null, 7, 7);
    SKString<String> instance = new SKString<String>(step, 0.4);
    assertNotSame(instance.hashCode(), "kkk".hashCode());
  }

  /**
   * Test of hashCode method, of class SKString.
   */
  @Test
  public void testHashCode2() {
    System.out.println("hashCode");
    Step<String> step = new Step<String>("x", null, null, 7, 7);
    Step<String> step2 = new Step<String>("y", null, null, 6, 6);
    SKString<String> instance = new SKString<String>(step, 0.4);
    SKString<String> instance2 = new SKString<String>(step2, 0.2);
    assertNotSame(instance.hashCode(), instance2.hashCode());
  }

  /**
   * Test of hashCode method, of class SKString.
   */
  @Test
  public void testHashCode3() {
    System.out.println("hashCode");
    Step<String> step = new Step<String>("x", null, null, 7, 7);
    Step<String> step2 = new Step<String>("x", null, null, 6, 6);
    SKString<String> instance = new SKString<String>(step, 0.4);
    SKString<String> instance2 = new SKString<String>(step2, 0.2);
    assertEquals(instance.hashCode(), instance2.hashCode());
  }
}
