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

import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Basic tests for automaton.
 *
 * @author anti
 */
// TODO anti when autoamton using of frequencies is defined - write tests on it
public class AutomatonTest {

    public AutomatonTest() {
    }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

  private Automaton<String> createInstance() {
    Automaton<String> r = new Automaton<String>(true);
    return r;
  }

  private void buildPTA(final Automaton<String> instance) {
    List<String> inputs = Arrays.asList(
            "xa", "xb", "xc", "x", "" );
    for (List<String> symbolString : tearInputs(inputs)) {
      instance.buildPTAOnSymbol(symbolString);
    }
  }

  /**
   * Test of createNewState method, of class Automaton.
   */
  @Test
  public void testCreateNewState() {
    System.out.println("createNewState");
    Automaton<String> instance = createInstance();
    State<String> result = instance.createNewState();
    assertEquals(0, result.getFinalCount());
    assertEquals(2, result.getName());
    assertEquals("[2|0]", result.toTestString());
  }

  /**
   * Test of getOutStepOnSymbol method, of class Automaton.
   */
  @Test
  public void testGetOutStepOnSymbol() {
    System.out.println("getOutStepOnSymbol");
    Automaton<String> instance = createInstance();
    State<String> state = instance.getInitialState();
    String symbol = null;
    Step<String> expResult = null;
    Step<String> result = instance.getOutStepOnSymbol(state, symbol);
    assertEquals(expResult, result);
  }

  /**
   * Test of getOutStepOnSymbol method, of class Automaton.
   */
  @Test
  public void testGetOutStepOnSymbol2() {
    System.out.println("getOutStepOnSymbol");
    Automaton<String> instance = createInstance();
    instance.buildPTAOnSymbol(Arrays.asList("a"));
    State<String> state = instance.getInitialState();
    String symbol = "a";
    Step<String> result = instance.getOutStepOnSymbol(state, symbol);
    assertNotNull(result);
  }

  private List<List<String>> tearInputs(List<String> pta) {
    final List<List<String>> result= new ArrayList<List<String>>();
    for (String in : pta) {
      final List<String> seq= new ArrayList<String>(in.length());
      for (int i = 0; i < in.length(); i++) {
        seq.add(in.substring(i, i+1));
      }
      result.add(seq);
    }
    return result;
  }

  /**
   * Test of buildPTAOnSymbol method, of class Automaton.
   */
  @Test
  public void testBuildPTAOnSymbol() {
    System.out.println("buildPTAOnSymbol");
    Automaton<String> instance = createInstance();
    buildPTA(instance);
    String expResult = "[1|1]>>1--x|4--2   [2|1]>>2--a|1--3   2--b|1--4   " +
            "2--c|1--5   [3|1]   [4|1]   [5|1]   @@@[1|1]   [2|1]<<1--x|4--2   " +
            "[3|1]<<2--a|1--3   [4|1]<<2--b|1--4   [5|1]<<2--c|1--5   ";
    String result = instance.toTestString();
    assertEquals(expResult, result);
  }

  /**
   * Test of toString method, of class Automaton.
   */
  @Test
  public void testToString() {
    System.out.println("toString");
    Automaton<String> instance = createInstance();
    buildPTA(instance);
    String expResult = "Automaton\n" +
            "[1|1] outSteps:\n" +
            "from 1 on {x|4} to 2\n" +
            "[2|1] outSteps:\n" +
            "from 2 on {a|1} to 3\n" +
            "from 2 on {b|1} to 4\n" +
            "from 2 on {c|1} to 5\n" +
            "[3|1] outSteps:\n" +
            "[4|1] outSteps:\n" +
            "[5|1] outSteps:\n" +
            "reversed:\n" +
            "[1|1] inSteps:\n" +
            "[2|1] inSteps:\n" +
            "from 1 on {x|4} to 2\n" +
            "[3|1] inSteps:\n" +
            "from 2 on {a|1} to 3\n" +
            "[4|1] inSteps:\n" +
            "from 2 on {b|1} to 4\n" +
            "[5|1] inSteps:\n" +
            "from 2 on {c|1} to 5\n";
    String result = instance.toString();
    assertEquals(expResult, result);
  }

  /**
   * Test of toTestString method, of class Automaton.
   */
  @Test
  public void testToTestString() {
    System.out.println("toTestString");
    Automaton<String> instance = createInstance();
    String expResult = "[1|0]   @@@[1|0]   ";
    String result = instance.toTestString();
    assertEquals(expResult, result);
  }

  /**
   * Test of getInitialState method, of class Automaton.
   */
  @Test
  public void testGetInitialState() {
    System.out.println("getInitialState");
    Automaton<String> instance = createInstance();
    State<String> result = instance.getInitialState();
    assertNotNull(result);
  }

  /**
   * Test of getInitialState method, of class Automaton.
   */
  @Test
  public void testGetInitialState2() {
    System.out.println("getInitialState");
    Automaton<String> instance = new Automaton<String>(false);
    State<String> result = instance.getInitialState();
    assertNull(result);
  }

  /**
   * Test of getDelta method, of class Automaton.
   */
  @Test
  public void testGetDelta() {
    System.out.println("getDelta");
    Automaton<String> instance = createInstance();
    Map<State<String>, Set<Step<String>>> result = instance.getDelta();
    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

  /**
   * Test of getReverseDelta method, of class Automaton.
   */
  @Test
  public void testGetReverseDelta() {
    System.out.println("getReverseDelta");
    Automaton<String> instance = createInstance();
    Map<State<String>, Set<Step<String>>> result = instance.getReverseDelta();
    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

  /**
   * Test of getNewStateName method, of class Automaton.
   */
  @Test
  public void testGetNewStateName() {
    System.out.println("getNewStateName");
    Automaton<String> instance = createInstance();
    Integer expResult = 2;
    Integer result = instance.getNewStateName();
    assertEquals(expResult, result);
  }

  /**
   * Test of getNewStateName method, of class Automaton.
   */
  @Test
  public void testGetNewStateName2() {
    System.out.println("getNewStateName");
    Automaton<String> instance = createInstance();
    instance.createNewState();
    Integer expResult = 3;
    Integer result = instance.getNewStateName();
    assertEquals(expResult, result);
  }
}