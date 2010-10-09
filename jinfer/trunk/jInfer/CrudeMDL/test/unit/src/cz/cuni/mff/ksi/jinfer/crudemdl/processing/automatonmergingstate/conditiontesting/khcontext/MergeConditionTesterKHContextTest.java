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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.conditiontesting.khcontext;

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite of MergeConditionTesterKHContext.
 *
 * @author anti
 */
public class MergeConditionTesterKHContextTest {

    public MergeConditionTesterKHContextTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

  private String inputToString(List<String> pta) {
    return Arrays.deepToString(pta.toArray());
  }

  /**
   * Test of getMergableStates method, of class MergeConditionTesterKHContext.
   */
  @Test(expected = IllegalStateException.class)
  public void testNoKHset() {
    System.out.println("k,h not set");
    Automaton<String> automaton = new Automaton<String>(true);
    automaton.buildPTAOnSymbol(Arrays.<String>asList(
            ));
    MergeConditionTesterKHContext<String> instance = new MergeConditionTesterKHContext<String>(2, 1);
    List<List<List<State<String>>>> result = instance.getMergableStates(automaton.getInitialState(), automaton.getInitialState(), automaton);
  }

  /**
   * Test of getMergableStates method, of class MergeConditionTesterKHContext.
   */
  @Test(expected = NullPointerException.class)
  public void testGetMergableStatesEmptyAut() {
    System.out.println("getMergableStates empty automaton");
    Automaton<String> automaton = new Automaton<String>(false);
    MergeConditionTesterKHContext<String> instance = new MergeConditionTesterKHContext<String>(2,1);
    List<List<List<State<String>>>> result = instance.getMergableStates(null, null, automaton);
    assertTrue(result.isEmpty());
  }

  private void testGetMergableStatesNoAlt(List<String> pta, int k, int h) {
    System.out.println("getMergableStates no alternatives " + inputToString(pta));
    Automaton<String> automaton = new Automaton<String>(true);
    for (String in : pta) {
      List<String> seq= new ArrayList<String>(in.length());
      for (int i = 0; i < in.length(); i++) {
        seq.add(in.substring(i, i+1));
      }
      automaton.buildPTAOnSymbol(seq);
    }
    MergeConditionTesterKHContext<String> instance = new MergeConditionTesterKHContext<String>(k, h);
    for (State<String> state1 : automaton.getDelta().keySet()) {
      for (State<String> state2 : automaton.getDelta().keySet()) {
        List<List<List<State<String>>>> result = instance.getMergableStates(state1, state2, automaton);
        assertTrue(result.isEmpty());
      }
    }
  }

  private String testGetMergableStatesMergeAll(List<String> pta, int k, int h) {
    System.out.println("getMergableStates merge all " + inputToString(pta));
    Automaton<String> automaton = new Automaton<String>(true);
    for (String in : pta) {
      List<String> seq= new ArrayList<String>(in.length());
      for (int i = 0; i < in.length(); i++) {
        seq.add(in.substring(i, i+1));
      }
      automaton.buildPTAOnSymbol(seq);
    }
    MergeConditionTesterKHContext<String> instance = new MergeConditionTesterKHContext<String>(k, h);
    boolean search= true;
    boolean found= false;
    List<List<List<State<String>>>> result= Collections.emptyList();
    while (search) {
      search= false;
      for (State<String> state1 : automaton.getDelta().keySet()) {
        for (State<String> state2 : automaton.getDelta().keySet()) {
          result = instance.getMergableStates(state1, state2, automaton);
          if (!result.isEmpty()) {
            found= true;
            break;
          }
        }
        if (found) {
          break;
        }
      }
      for (List<List<State<String>>> alt : result) {
        for (List<State<String>> merg : alt) {
          automaton.mergeStates(merg);
          search= true;
          found= false;
        }
      }
    }

    return automaton.toTestString();
  }

  /**
   * Test of getMergableStates method, of class MergeConditionTesterKHContext.
   */
  @Test
  public void testGetMergableStates1() {
    testGetMergableStatesNoAlt(Arrays.<String>asList(

    ), 2, 1);
  }

  @Test
  public void testGetMergableStates2() {
    testGetMergableStatesNoAlt(Arrays.<String>asList(
    "a"
    ), 2, 1);
  }

  @Test
  public void testGetMergableStates3() {
    testGetMergableStatesNoAlt(Arrays.<String>asList(
    "a","a"
    ), 2, 1);
  }

  @Test
  public void testGetMergableStates4() {
    testGetMergableStatesNoAlt(Arrays.<String>asList(
    "a","a","a"
    ), 2, 1);
  }

  @Test
  public void testGetMergableStates5() {
    testGetMergableStatesNoAlt(Arrays.<String>asList(
    "a","a","a","a","a","a","a","a","a","b","b","b","b","b","b","b","b"
    ), 2, 1);
  }

  @Test
  public void testGetMergableStates6() {
    testGetMergableStatesNoAlt(Arrays.<String>asList(
    "aa", "aa", "bb", "bb"
    ), 2, 1);
  }

  @Test
  public void testGetMergableStates7() {
    testGetMergableStatesNoAlt(Arrays.<String>asList(
    "aa", "aa", "bb", "bb", "aa", "bb", "bb", "aa", "bb", "bb", "aa", "bb", "bb", "aa", "bb", "bb"
    ), 2, 1);
  }

  @Test
  public void testGetMergableStates8() {
    testGetMergableStatesNoAlt(Arrays.<String>asList(
    "aa", "aa", "bb", "bb", "cc", "dd"
    ), 2, 1);
  }

  @Test
  public void testGetMergableStates9() {
    testGetMergableStatesNoAlt(Arrays.<String>asList(
    "aa", "ab", "ac", "ad", "aba", "aca", "ada"
    ), 2, 2);
  }

  @Test
  public void testGetMergableStates10() {
    testGetMergableStatesNoAlt(Arrays.<String>asList(
    "aab", "bab"
    ), 3, 1);
  }

  @Test
  public void testGetMergableStates11() {
    testGetMergableStatesNoAlt(Arrays.<String>asList(
    "aaaaa", "aaaaa"
    ), 5, 1);
  }

  @Test
  public void testGetMergableStates12() {
    assertEquals(
    "[1|0]>>1--a|2--2   [2|0]>>2--a|2--3   [3|2]   @@@[1|0]   [2|0]<<1--a|2--2   [3|2]<<2--a|2--3   ",
    testGetMergableStatesMergeAll(Arrays.<String>asList(
    "aa", "aa"
    ), 2, 1));
  }

  @Test
  public void testGetMergableStates13() {
    assertEquals(
    "[1|0]>>1--a|1--2   1--b|1--5   [2|0]>>2--b|1--3   [3|0]+[6|0]>>3--a|2--4   [4|2]+[7|1]   [5|0]>>5--b|1--3   @@@[1|0]   [2|0]<<1--a|1--2   [3|0]+[6|0]<<2--b|1--3   5--b|1--3   [4|2]+[7|1]<<3--a|2--4   [5|0]<<1--b|1--5   ",
    testGetMergableStatesMergeAll(Arrays.<String>asList(
    "aba", "bba"
    ), 2, 1));
  }

  @Test
  public void testGetMergableStates14() {
    assertEquals(
    "[1|0]>>1--a|1--2   1--b|1--6   [2|0]>>2--b|1--3   [3|0]>>3--a|1--4   [4|0]+[8|0]>>4--c|2--5   [5|2]+[9|1]   [6|0]>>6--b|1--7   [7|0]>>7--a|1--4   @@@[1|0]   [2|0]<<1--a|1--2   [3|0]<<2--b|1--3   [4|0]+[8|0]<<3--a|1--4   7--a|1--4   [5|2]+[9|1]<<4--c|2--5   [6|0]<<1--b|1--6   [7|0]<<6--b|1--7   ",
    testGetMergableStatesMergeAll(Arrays.<String>asList(
    "abac", "bbac"
    ), 3, 1));
  }


}