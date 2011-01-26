/*
 *  Copyright (C) 2010 anti
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
package cz.cuni.mff.ksi.jinfer.base.automaton;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Basic tests for State.
 *
 * @author anti
 */
@SuppressWarnings("PMD.SystemPrintln")
public class StateTest {

  private State<String> createInstance() {
    return new State<String>(10, 5);
  }

  /**
   * Test of getFinalCount method, of class State.
   */
  @Test
  public void testGetFinalCount() {
    System.out.println("getFinalCount");
    final State<String> instance = createInstance();
    final Integer expResult = 10;
    final Integer result = instance.getFinalCount();
    assertEquals(expResult, result);
  }

  /**
   * Test of setFinalCount method, of class State.
   */
  @Test
  public void testSetFinalCount() {
    System.out.println("setFinalCount");
    final State<String> instance = createInstance();
    final Integer expResult = 20;
    instance.setFinalCount(expResult);
    final Integer result = instance.getFinalCount();
    assertEquals(expResult, result);
  }

  /**
   * Test of incFinalCount method, of class State.
   */
  @Test
  public void testIncFinalCount_0args() {
    System.out.println("incFinalCount");
    final State<String> instance = createInstance();
    instance.incFinalCount();
    instance.incFinalCount();
    final Integer expResult = 12;
    final Integer result = instance.getFinalCount();
    assertEquals(expResult, result);
  }

  /**
   * Test of incFinalCount method, of class State.
   */
  @Test
  public void testIncFinalCount_int() {
    System.out.println("incFinalCount");
    final int i = 10;
    final State<String> instance = createInstance();
    instance.incFinalCount(i);
    final Integer expResult = 20;
    final Integer result = instance.getFinalCount();
    assertEquals(expResult, result);
  }

  /**
   * Test of getName method, of class State.
   */
  @Test
  public void testGetName() {
    System.out.println("getName");
    final State<String> instance = createInstance();
    final int expResult = 5;
    final int result = instance.getName();
    assertEquals(expResult, result);
  }

  /**
   * Test of setName method, of class State.
   */
  @Test
  public void testSetName() {
    System.out.println("setName");
    final int name = 1;
    final State<String> instance = createInstance();
    instance.setName(name);
    final int expResult = 1;
    final int result = instance.getName();
    assertEquals(expResult, result);
  }

  /**
   * Test of toString method, of class State.
   */
  @Test
  public void testToString() {
    System.out.println("toString");
    final State<String> instance = createInstance();
    final String expResult = "[5|10]";
    final String result = instance.toString();
    assertEquals(expResult, result);
  }

  /**
   * Test of toTestString method, of class State.
   */
  @Test
  public void testToTestString() {
    System.out.println("toTestString");
    final State<String> instance = createInstance();
    final String expResult = "[5|10]";
    final String result = instance.toTestString();
    assertEquals(expResult, result);
  }
}
