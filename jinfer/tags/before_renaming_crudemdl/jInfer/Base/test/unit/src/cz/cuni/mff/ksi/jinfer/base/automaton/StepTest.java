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
 * Basic tests for Step.
 *
 * @author anti
 */
public class StepTest {

    public StepTest() {
    }

  private Step<String> createInstance() {
    State<String> f = new State<String>(10, 1);
    State<String> t = new State<String>(20, 2);
    Step<String> r = new Step<String>("testSymbol", f, t, 0);
    return r;
  }

  /**
   * Test of getAcceptSymbol method, of class Step.
   */
  @Test
  public void testGetAcceptSymbol() {
    System.out.println("getAcceptSymbol");
    Step<String> instance = createInstance();
    String expResult = "testSymbol";
    String result = instance.getAcceptSymbol();
    assertEquals(expResult, result);
  }

  /**
   * Test of setAcceptSymbol method, of class Step.
   */
  @Test
  public void testSetAcceptSymbol() {
    System.out.println("setAcceptSymbol");
    String acceptSymbol = "secondSymbol";
    Step<String> instance = createInstance();
    instance.setAcceptSymbol(acceptSymbol);
    String expResult = "secondSymbol";
    String result = instance.getAcceptSymbol();
    assertEquals(expResult, result);
  }

  /**
   * Test of getUseCount method, of class Step.
   */
  @Test
  public void testGetUseCount() {
    System.out.println("getUseCount");
    Step<String> instance = createInstance();
    int expResult = 0;
    int result = instance.getUseCount();
    assertEquals(expResult, result);
  }

  /**
   * Test of setUseCount method, of class Step.
   */
  @Test
  public void testSetUseCount() {
    System.out.println("setUseCount");
    int useCount = 5;
    Step<String> instance = createInstance();
    instance.setUseCount(useCount);
    int expResult = 5;
    int result = instance.getUseCount();
    assertEquals(expResult, result);
  }

  /**
   * Test of incUseCount method, of class Step.
   */
  @Test
  public void testIncUseCount_0args() {
    System.out.println("incUseCount");
    Step<String> instance = createInstance();
    instance.incUseCount();
    instance.incUseCount();
    instance.incUseCount();
    instance.incUseCount();
    instance.incUseCount();
    instance.incUseCount();
    instance.incUseCount();
    instance.incUseCount();
    instance.incUseCount();
    instance.incUseCount();
    int expResult = 10;
    int result = instance.getUseCount();
    assertEquals(expResult, result);
  }

  /**
   * Test of incUseCount method, of class Step.
   */
  @Test
  public void testIncUseCount_Integer() {
    System.out.println("incUseCount");
    Integer i = 7;
    Step<String> instance = createInstance();
    instance.incUseCount(i);
    int expResult = 7;
    int result = instance.getUseCount();
    assertEquals(expResult, result);
  }

  /**
   * Test of setSource method, of class Step.
   */
  @Test
  public void testSetSource() {
    System.out.println("setSource");
    State<String> expResult = new State<String>(1, 3);
    Step<String> instance = createInstance();
    instance.setSource(expResult);
    State<String> result = instance.getSource();
    assertEquals(expResult, result);
  }

  /**
   * Test of setDestination method, of class Step.
   */
  @Test
  public void testSetDestination() {
    System.out.println("setDestination");
    State<String> expResult = new State<String>(1, 4);
    Step<String> instance = createInstance();
    instance.setDestination(expResult);
    State<String> result = instance.getDestination();
    assertEquals(expResult, result);
  }

  /**
   * Test of toString method, of class Step.
   */
  @Test
  public void testToString() {
    System.out.println("toString");
    Step<String> instance = createInstance();
    String expResult = "from 1 on {testSymbol|0} to 2";
    String result = instance.toString();
    assertEquals(expResult, result);
  }

  /**
   * Test of toTestString method, of class Step.
   */
  @Test
  public void testToTestString() {
    System.out.println("toTestString");
    Step<String> instance = createInstance();
    String expResult = "1--testSymbol|0--2";
    String result = instance.toTestString();
    assertEquals(expResult, result);
  }

}