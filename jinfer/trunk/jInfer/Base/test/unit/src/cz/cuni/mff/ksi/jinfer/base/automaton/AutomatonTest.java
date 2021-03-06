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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Basic tests for automaton.
 *
 * @author anti
 */
// TODO anti when autoamton using of frequencies is defined - write tests on it
@SuppressWarnings("PMD.SystemPrintln")
public class AutomatonTest {

  private Automaton<String> createInstance() {
    return new Automaton<String>(true);
  }

  private void buildPTA(final Automaton<String> instance) {
    final List<String> inputs = Arrays.asList(
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
    final Automaton<String> instance = createInstance();
    final State<String> result = instance.createNewState();
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
    final Automaton<String> instance = createInstance();
    final State<String> state = instance.getInitialState();
    final String symbol = null;
    final Step<String> expResult = null;
    final Step<String> result = instance.getOutStepOnSymbol(state, symbol);
    assertEquals(expResult, result);
  }

  /**
   * Test of getOutStepOnSymbol method, of class Automaton.
   */
  @Test
  public void testGetOutStepOnSymbol2() {
    System.out.println("getOutStepOnSymbol");
    final Automaton<String> instance = createInstance();
    instance.buildPTAOnSymbol(Arrays.asList("a"));
    final State<String> state = instance.getInitialState();
    final String symbol = "a";
    final Step<String> result = instance.getOutStepOnSymbol(state, symbol);
    assertNotNull(result);
  }

  private List<List<String>> tearInputs(final List<String> pta) {
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
    final Automaton<String> instance = createInstance();
    buildPTA(instance);
    final String expResult = "[1|1]>>1--x|4--2   [2|1]>>2--a|1--3   2--b|1--4   " +
            "2--c|1--5   [3|1]   [4|1]   [5|1]   @@@[1|1]   [2|1]<<1--x|4--2   " +
            "[3|1]<<2--a|1--3   [4|1]<<2--b|1--4   [5|1]<<2--c|1--5   ";
    final String result = instance.toTestString();
    assertEquals(expResult, result);
  }

  /**
   * Test of toString method, of class Automaton.
   */
  @Test
  public void testToString() {
    System.out.println("toString");
    final Automaton<String> instance = createInstance();
    buildPTA(instance);
    final String expResult = "Automaton\n" +
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
    final String result = instance.toString();
    assertEquals(expResult, result);
  }

  /**
   * Test of toTestString method, of class Automaton.
   */
  @Test
  public void testToTestString() {
    System.out.println("toTestString");
    final Automaton<String> instance = createInstance();
    final String expResult = "[1|0]   @@@[1|0]   ";
    final String result = instance.toTestString();
    assertEquals(expResult, result);
  }

  /**
   * Test of getInitialState method, of class Automaton.
   */
  @Test
  public void testGetInitialState() {
    System.out.println("getInitialState");
    final Automaton<String> instance = createInstance();
    final State<String> result = instance.getInitialState();
    assertNotNull(result);
  }

  /**
   * Test of getInitialState method, of class Automaton.
   */
  @Test
  public void testGetInitialState2() {
    System.out.println("getInitialState");
    final Automaton<String> instance = new Automaton<String>(false);
    final State<String> result = instance.getInitialState();
    assertNull(result);
  }

  /**
   * Test of getDelta method, of class Automaton.
   */
  @Test
  public void testGetDelta() {
    System.out.println("getDelta");
    final Automaton<String> instance = createInstance();
    final Map<State<String>, Set<Step<String>>> result = instance.getDelta();
    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

  /**
   * Test of getReverseDelta method, of class Automaton.
   */
  @Test
  public void testGetReverseDelta() {
    System.out.println("getReverseDelta");
    final Automaton<String> instance = createInstance();
    final Map<State<String>, Set<Step<String>>> result = instance.getReverseDelta();
    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

  /**
   * Test of getNewStateName method, of class Automaton.
   */
  @Test
  public void testGetNewStateName() {
    System.out.println("getNewStateName");
    final Automaton<String> instance = createInstance();
    final Integer expResult = 2;
    final Integer result = instance.getNewStateName();
    assertEquals(expResult, result);
  }

  /**
   * Test of getNewStateName method, of class Automaton.
   */
  @Test
  public void testGetNewStateName2() {
    System.out.println("getNewStateName");
    final Automaton<String> instance = createInstance();
    instance.createNewState();
    final Integer expResult = 3;
    final Integer result = instance.getNewStateName();
    assertEquals(expResult, result);
  }
  
  @Test 
  public void testGetPrefix() {
      List<String> t1 = Arrays.asList("1", "2", "3", "4");
      List<String> t2 = Arrays.asList("1", "2", "3", "5");
      List<String> t3 = Arrays.asList("1", "2", "6");
      List<String> t4 = Arrays.asList("1", "7");
      List<String> t5 = Arrays.asList("1", "8", "9", "A");
      List<String> t6 = Arrays.asList("1", "8", "9", "B");
      List<String> t7 = Arrays.asList("1", "8", "C");
      
      Automaton<String> automaton = new Automaton<>(true);
      
      automaton.buildPTAOnSymbol(t1);
      automaton.buildPTAOnSymbol(t2);
      automaton.buildPTAOnSymbol(t3);
      automaton.buildPTAOnSymbol(t4);
      automaton.buildPTAOnSymbol(t5);
      automaton.buildPTAOnSymbol(t6);
      automaton.buildPTAOnSymbol(t7);
      
      assertEquals(13, automaton.getDelta().size());
      
      State<String> s1 = automaton.getInitialState();
      Set<List<String>> prefixes1 = automaton.getStatePrefixes(s1);
      Set<List<String>> suffixes1 = automaton.getStateSuffixes(s1);
      
      List<String> suf114 = Arrays.asList("1", "2", "3", "4");
      List<String> suf113 = Arrays.asList("1", "2", "3");
      List<String> suf112 = Arrays.asList("1", "2");
      List<String> suf111 = Arrays.asList("1");
      List<String> suf214 = Arrays.asList("1", "2", "3", "5");
      List<String> suf313 = Arrays.asList("1", "2", "6");
      List<String> suf412 = Arrays.asList("1", "7");
      List<String> suf514 = Arrays.asList("1", "8", "9", "A");
      List<String> suf513 = Arrays.asList("1", "8", "9");
      List<String> suf512 = Arrays.asList("1", "8");
      List<String> suf614 = Arrays.asList("1", "8", "9", "B");
      List<String> suf714 = Arrays.asList("1", "8", "C");
      
      Set<List<String>> expectedSuffix1;
      expectedSuffix1 = new HashSet<>();
      expectedSuffix1.add(suf114);
      expectedSuffix1.add(suf113);
      expectedSuffix1.add(suf112);
      expectedSuffix1.add(suf111);
      expectedSuffix1.add(suf214);
      expectedSuffix1.add(suf313);
      expectedSuffix1.add(suf412);
      expectedSuffix1.add(suf514);
      expectedSuffix1.add(suf513);
      expectedSuffix1.add(suf512);
      expectedSuffix1.add(suf614);
      expectedSuffix1.add(suf714);
      
      assertEquals(0, prefixes1.size());
      assertEquals(12, suffixes1.size());
      assertThat(suffixes1, is(expectedSuffix1));
      
      // Test for S2
      State<String> s2 = automaton.getDelta().get(s1).stream()
              .filter(t -> t.getAcceptSymbol().equals("1"))
              .findFirst().get().getDestination();
      
      Set<List<String>> expectedSuffix2; 
      // trims "1" out
      expectedSuffix2 = expectedSuffix1.stream().map(l -> l.subList(1, l.size()))
              .filter(l -> l.size() > 0)
              .collect(Collectors.toSet());
      
      
      Set<List<String>> expectedprefix2;
      expectedprefix2 = new HashSet<>();
      expectedprefix2.add(Arrays.asList("1"));
      
      Set<List<String>> prefixes2 = automaton.getStatePrefixes(s2);
      Set<List<String>> suffixes2 = automaton.getStateSuffixes(s2);
      assertEquals(1, prefixes2.size());
      assertThat(prefixes2, is(expectedprefix2));
      assertEquals(11, suffixes2.size());
      assertThat(suffixes2, is(expectedSuffix2));
      
      // Test for S3
      
      State<String> s3 = automaton.getDelta().get(s2).stream()
              .filter(t -> t.getAcceptSymbol().equals("2"))
              .findFirst().get().getDestination();
      
      Set<List<String>> expectedSuffix3; 
      // trims "1" out
      expectedSuffix3 = expectedSuffix2.stream()
              .filter(l -> l.get(0).equals("2"))
              .map(l -> l.subList(1, l.size()))
              .filter(l -> l.size() > 0)
              .collect(Collectors.toSet());
      
      
      Set<List<String>> expectedprefix3;
      expectedprefix3 = new HashSet<>(expectedprefix2);
      expectedprefix3.add(Arrays.asList("1", "2"));
      
      Set<List<String>> prefixes3 = automaton.getStatePrefixes(s3);
      Set<List<String>> suffixes3 = automaton.getStateSuffixes(s3);
      assertEquals(2, prefixes3.size());
      assertThat(prefixes3, is(expectedprefix3));
      assertEquals(4, suffixes3.size());
      assertThat(suffixes3, is(expectedSuffix3));
      
      
      
      
  }
}