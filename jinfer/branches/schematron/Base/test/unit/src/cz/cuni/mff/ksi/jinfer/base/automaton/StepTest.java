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
@SuppressWarnings("PMD.SystemPrintln")
public class StepTest {

  private Step<String> createInstance() {
    final State<String> f = new State<String>(10, 1);
    final State<String> t = new State<String>(20, 2);
    return new Step<String>("testSymbol", f, t, 0, 0);
  }

  /**
   * Test of getAcceptSymbol method, of class Step.
   */
  @Test
  public void testGetAcceptSymbol() {
    System.out.println("getAcceptSymbol");
    final Step<String> instance = createInstance();
    final String expResult = "testSymbol";
    final String result = instance.getAcceptSymbol();
    assertEquals(expResult, result);
  }

  /**
   * Test of setAcceptSymbol method, of class Step.
   */
  @Test
  public void testSetAcceptSymbol() {
    System.out.println("setAcceptSymbol");
    final String acceptSymbol = "secondSymbol";
    final Step<String> instance = createInstance();
    instance.setAcceptSymbol(acceptSymbol);
    final String expResult = "secondSymbol";
    final String result = instance.getAcceptSymbol();
    assertEquals(expResult, result);
  }

  /**
   * Test of getUseCount method, of class Step.
   */
  @Test
  public void testGetUseCount() {
    System.out.println("getUseCount");
    final Step<String> instance = createInstance();
    final int expResult = 0;
    final int result = instance.getUseCount();
    assertEquals(expResult, result);
  }

  /**
   * Test of setUseCount method, of class Step.
   */
  @Test
  public void testSetUseCount() {
    System.out.println("setUseCount");
    final int useCount = 5;
    final Step<String> instance = createInstance();
    instance.setUseCount(useCount);
    final int expResult = 5;
    final int result = instance.getUseCount();
    assertEquals(expResult, result);
  }

  /**
   * Test of incUseCount method, of class Step.
   */
  @Test
  public void testIncUseCount_0args() {
    System.out.println("incUseCount");
    final Step<String> instance = createInstance();
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
    final int expResult = 10;
    final int result = instance.getUseCount();
    assertEquals(expResult, result);
  }

  /**
   * Test of incUseCount method, of class Step.
   */
  @Test
  public void testIncUseCount_Integer() {
    System.out.println("incUseCount");
    final Integer i = 7;
    final Step<String> instance = createInstance();
    instance.incUseCount(i);
    final int expResult = 7;
    final int result = instance.getUseCount();
    assertEquals(expResult, result);
  }

  /**
   * Test of setSource method, of class Step.
   */
  @Test
  public void testSetSource() {
    System.out.println("setSource");
    final State<String> expResult = new State<String>(1, 3);
    final Step<String> instance = createInstance();
    instance.setSource(expResult);
    final State<String> result = instance.getSource();
    assertEquals(expResult, result);
  }

  /**
   * Test of setDestination method, of class Step.
   */
  @Test
  public void testSetDestination() {
    System.out.println("setDestination");
    final State<String> expResult = new State<String>(1, 4);
    final Step<String> instance = createInstance();
    instance.setDestination(expResult);
    final State<String> result = instance.getDestination();
    assertEquals(expResult, result);
  }

  /**
   * Test of toString method, of class Step.
   */
  @Test
  public void testToString() {
    System.out.println("toString");
    final Step<String> instance = createInstance();
    final String expResult = "from 1 on {testSymbol|0} to 2";
    final String result = instance.toString();
    assertEquals(expResult, result);
  }

  /**
   * Test of toTestString method, of class Step.
   */
  @Test
  public void testToTestString() {
    System.out.println("toTestString");
    final Step<String> instance = createInstance();
    final String expResult = "1--testSymbol|0--2";
    final String result = instance.toTestString();
    assertEquals(expResult, result);
  }
}
