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
public class StateTest {

    public StateTest() {
    }

  private State<String> createInstance() {
    return new State<String>(10, 5);
  }

  /**
   * Test of getFinalCount method, of class State.
   */
  @Test
  public void testGetFinalCount() {
    System.out.println("getFinalCount");
    State<String> instance = createInstance();
    Integer expResult = 10;
    Integer result = instance.getFinalCount();
    assertEquals(expResult, result);
  }

  /**
   * Test of setFinalCount method, of class State.
   */
  @Test
  public void testSetFinalCount() {
    System.out.println("setFinalCount");
    State<String> instance = createInstance();
    Integer expResult = 20;
    instance.setFinalCount(expResult);
    Integer result = instance.getFinalCount();
    assertEquals(expResult, result);
  }

  /**
   * Test of incFinalCount method, of class State.
   */
  @Test
  public void testIncFinalCount_0args() {
    System.out.println("incFinalCount");
    State<String> instance = createInstance();
    instance.incFinalCount();
    instance.incFinalCount();
    Integer expResult = 12;
    Integer result = instance.getFinalCount();
    assertEquals(expResult, result);
  }

  /**
   * Test of incFinalCount method, of class State.
   */
  @Test
  public void testIncFinalCount_int() {
    System.out.println("incFinalCount");
    int i = 10;
    State<String> instance = createInstance();
    instance.incFinalCount(i);
    Integer expResult = 20;
    Integer result = instance.getFinalCount();
    assertEquals(expResult, result);
  }

  /**
   * Test of getName method, of class State.
   */
  @Test
  public void testGetName() {
    System.out.println("getName");
    State<String> instance = createInstance();
    int expResult = 5;
    int result = instance.getName();
    assertEquals(expResult, result);
  }

  /**
   * Test of setName method, of class State.
   */
  @Test
  public void testSetName() {
    System.out.println("setName");
    int name = 1;
    State<String> instance = createInstance();
    instance.setName(name);
    int expResult = 1;
    int result = instance.getName();
    assertEquals(expResult, result);
  }

  /**
   * Test of toString method, of class State.
   */
  @Test
  public void testToString() {
    System.out.println("toString");
    State<String> instance = createInstance();
    String expResult = "[5|10]";
    String result = instance.toString();
    assertEquals(expResult, result);
  }

  /**
   * Test of toTestString method, of class State.
   */
  @Test
  public void testToTestString() {
    System.out.println("toTestString");
    State<String> instance = createInstance();
    String expResult = "[5|10]";
    String result = instance.toTestString();
    assertEquals(expResult, result);
  }

}