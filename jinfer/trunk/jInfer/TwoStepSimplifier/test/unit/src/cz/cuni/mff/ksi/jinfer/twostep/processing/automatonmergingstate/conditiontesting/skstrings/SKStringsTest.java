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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class SKStringsTest {
  
  public SKStringsTest() {
  }

  private String inputToString(final List<String> pta) {
    return Arrays.deepToString(pta.toArray());
  }
  
  /**
   * Test of getMergableStates method, of class MergeConditionTesterKHContext.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBadKSSet() throws InterruptedException {
    System.out.println("k,s bad values set");
    final Automaton<String> automaton = new Automaton<String>(true);
    automaton.buildPTAOnSymbol(Arrays.<String>asList());
    final SKStrings<String> instance = new SKStrings<String>(3, 4, "AND");
    final List<List<List<State<String>>>> result = instance.getMergableStates(automaton);
  }

  /**
   * Test of getMergableStates method, of class MergeConditionTesterKHContext.
   */
  public void testGetMergableStatesEmptyAut() throws InterruptedException {
    System.out.println("getMergableStates empty automaton");
    final Automaton<String> automaton = new Automaton<String>(false);
    final SKStrings<String> instance = new SKStrings<String>(2, 0.01, "AND");
    final List<List<List<State<String>>>> result = instance.getMergableStates(automaton);
    assertTrue(result.isEmpty());
  }
  

  private void testGetMergableStatesNoAlt(final List<String> pta, final int k, final double s) throws InterruptedException {
    System.out.println("getMergableStates no alternatives " + inputToString(pta));
    final Automaton<String> automaton = new Automaton<String>(true);
    for (String in : pta) {
      final List<String> seq = new ArrayList<String>(in.length());
      for (int i = 0; i < in.length(); i++) {
        seq.add(in.substring(i, i + 1));
      }
      automaton.buildPTAOnSymbol(seq);
    }
    final SKStrings<String> instance = new SKStrings<String>(k, s, "AND");
    for (State<String> state1 : automaton.getDelta().keySet()) {
      for (State<String> state2 : automaton.getDelta().keySet()) {
        final List<List<List<State<String>>>> result = instance.getMergableStates(automaton);
        assertTrue(result.isEmpty());
      }
    }
  }

  /**
   * Test of getMergableStates method, of class MergeConditionTesterKHContext.
   */
  @Test
  public void testGetMergableStates1() throws InterruptedException {
    testGetMergableStatesNoAlt(Arrays.<String>asList(), 2, 1);
  }

  @Test
  public void testGetMergableStates2() throws InterruptedException {
    testGetMergableStatesNoAlt(Arrays.<String>asList(
            "a"), 2, 1);
  }

  @Test
  public void testGetMergableStates3() throws InterruptedException {
    testGetMergableStatesNoAlt(Arrays.<String>asList(
            "a", "a"), 2, 1);
  }

  @Test
  public void testGetMergableStates4() throws InterruptedException {
    testGetMergableStatesNoAlt(Arrays.<String>asList(
            "a", "a", "a"), 2, 1);
  }

  @Test
  public void testGetMergableStates5() throws InterruptedException {
    testGetMergableStatesNoAlt(Arrays.<String>asList(
            "a", "a", "a", "a", "a", "a", "a", "a", "a", "b", "b", "b", "b", "b", "b", "b", "b"), 2, 1);
  }

  @Test
  public void testGetMergableStates6() throws InterruptedException {
    testGetMergableStatesNoAlt(Arrays.<String>asList(
            "aa", "aa", "bb", "bb"), 2, 1);
  }

  @Test
  public void testGetMergableStates7() throws InterruptedException {
    testGetMergableStatesNoAlt(Arrays.<String>asList(
            "aa", "aa", "bb", "bb", "aa", "bb", "bb", "aa", "bb", "bb", "aa", "bb", "bb", "aa", "bb", "bb"), 2, 1);
  }

  @Test
  public void testGetMergableStates8() throws InterruptedException {
    testGetMergableStatesNoAlt(Arrays.<String>asList(
            "aa", "aa", "bb", "bb", "cc", "dd"), 2, 1);
  }

  @Test
  public void testGetMergableStates9() throws InterruptedException {
    testGetMergableStatesNoAlt(Arrays.<String>asList(
            "aa", "ab", "ac", "ad", "aba", "aca", "ada"), 2, 1);
  }

  @Test
  public void testGetMergableStates10() throws InterruptedException {
    testGetMergableStatesNoAlt(Arrays.<String>asList(
            "aab", "bab"), 3, 1);
  }

  @Test
  public void testGetMergableStates11() throws InterruptedException {
    testGetMergableStatesNoAlt(Arrays.<String>asList(
            "aaaaa", "aaaaa"), 5, 1);
  }

  @Test
  public void testSimplify12() throws InterruptedException {
    assertEquals(
            "[1|0]>>1--a|2--2   [2|0]>>2--a|2--3   [3|2]   @@@[1|0]   [2|0]<<1--a|2--2   [3|2]<<2--a|2--3   ",
            testUniverzal(Arrays.<String>asList(
            "aa", "aa"), 2, 1));
  }
  
  @Test
  public void testSimplify13() throws InterruptedException {
    assertEquals(
            "[1|0]>>1--a|1--2   1--b|1--2   [2|0]+[5|0]>>2--b|1--3   2--b|1--6   [3|0]>>3--a|1--4   [4|1]   [6|0]>>6--a|1--7   [7|1]   @@@[1|0]   [2|0]+[5|0]<<1--a|1--2   1--b|1--2   [3|0]<<2--b|1--3   [4|1]<<3--a|1--4   [6|0]<<2--b|1--6   [7|1]<<6--a|1--7   ",
            testUniverzal(Arrays.<String>asList(
            "aba", "bba"), 2, 1));
  }

  /**
   * Test of simplify method, of class KHgrams.
   */
  @Test
  public void testSimplify14() throws Exception {
    assertEquals(
            "[1|0]>>1--a|1--2   1--b|1--2   [2|0]+[6|0]>>2--b|2--3   [3|0]+[7|0]>>3--a|1--4   3--a|1--8   [4|0]>>4--c|1--5   [5|1]   [8|0]>>8--c|1--9   [9|1]   @@@[1|0]   [2|0]+[6|0]<<1--a|1--2   1--b|1--2   [3|0]+[7|0]<<2--b|2--3   [4|0]<<3--a|1--4   [5|1]<<4--c|1--5   [8|0]<<3--a|1--8   [9|1]<<8--c|1--9   ",
            testUniverzal(Arrays.<String>asList(
            "abac", "bbac"), 2, 1));

  }

  /**
   * Test of simplify method, of class KHgrams.
   */
  @Test
  public void testSimplify15() throws Exception {
    assertEquals(//"",
            "[1|0]+[2|0]+[11|0]>>1--h|4--1   1--p|1--7   1--p|2--3   1--e|1--17   [3|0]+[12|0]>>3--e|1--4   3--e|1--7   [4|0]>>4--e|1--5   [5|0]>>5--e|1--6   [6|1]   [7|0]+[13|0]>>7--e|1--8   7--e|1--14   [8|0]>>8--e|1--9   [9|0]>>9--h|1--10   [10|1]   [14|0]>>14--e|1--15   [15|0]>>15--h|1--16   [16|1]   [17|0]>>17--e|1--18   [18|1]   @@@[1|0]+[2|0]+[11|0]<<1--h|4--1   [3|0]+[12|0]<<1--p|2--3   [4|0]<<3--e|1--4   [5|0]<<4--e|1--5   [6|1]<<5--e|1--6   [7|0]+[13|0]<<1--p|1--7   3--e|1--7   [8|0]<<7--e|1--8   [9|0]<<8--e|1--9   [10|1]<<9--h|1--10   [14|0]<<7--e|1--14   [15|0]<<14--e|1--15   [16|1]<<15--h|1--16   [17|0]<<1--e|1--17   [18|1]<<17--e|1--18   ",
            testUniverzal(Arrays.<String>asList(
            "hpeee", "peeh", "hhpeeeh", "hee"
            ), 3, 0.01));

  }

  /**
   * Test of simplify method, of class KHgrams.
   */
  @Test
  public void testSimplify16() throws Exception {
    assertEquals(
            "[1|0]>>1--h|3--2   1--p|1--3   [2|0]>>2--p|1--3   2--h|1--11   2--e|1--17   [3|0]+[4|0]+[7|0]+[12|0]+[13|0]>>3--e|2--3   3--e|1--5   3--e|2--8   [5|0]>>5--e|1--6   [6|1]   [8|0]+[14|0]>>8--e|1--9   8--e|1--15   [9|0]>>9--h|1--10   [10|1]   [11|0]>>11--p|1--3   [15|0]>>15--h|1--16   [16|1]   [17|0]>>17--e|1--18   [18|1]   @@@[1|0]   [2|0]<<1--h|3--2   [3|0]+[4|0]+[7|0]+[12|0]+[13|0]<<2--p|1--3   3--e|2--3   1--p|1--3   11--p|1--3   [5|0]<<3--e|1--5   [6|1]<<5--e|1--6   [8|0]+[14|0]<<3--e|2--8   [9|0]<<8--e|1--9   [10|1]<<9--h|1--10   [11|0]<<2--h|1--11   [15|0]<<8--e|1--15   [16|1]<<15--h|1--16   [17|0]<<2--e|1--17   [18|1]<<17--e|1--18   ",
            testUniverzal(Arrays.<String>asList(
            "hpeee", "peeh", "hhpeeeh", "hee"
            ), 2, 1.0));

  }
  

  @Test
  public void testUnity() throws InterruptedException {
    System.out.println("testUnity");
    final Automaton<String> automaton = new Automaton<String>(true);
    List<String> pta = Arrays.<String>asList("hpeee", "peeh", "hhpeeeh", "hee");
    for (String in : pta) {
      final List<String> seq = new ArrayList<String>(in.length());
      for (int i = 0; i < in.length(); i++) {
        seq.add(in.substring(i, i + 1));
      }
      automaton.buildPTAOnSymbol(seq);
    }
    final SKStrings<String> instance = new SKStrings<String>(2, 1.0, "AND");
    SKBucket<String> bk= instance.findSKStrings(2, automaton.getInitialState(), automaton.getDelta());
    double sum = 0;
    for (SKString<String> x : bk.getSKStrings()) {
      sum+= x.getProbability();
    }
    assertEquals(1.0, sum, 0.0);

    sum= 0.0;
    for (SKString<String> x : bk.getMostProbable(1.0).getSKStrings()) {
      sum+= x.getProbability();
    }
    assertEquals(1.0, sum, 0.0);

    sum= 0.0;
    for (SKString<String> x : bk.getMostProbable(0.5).getSKStrings()) {
      sum+= x.getProbability();
    }
    assertEquals(sum, 0.5, 0.0);
  }
  
  
  private String testUniverzal(final List<String> pta, final int k, final double s) throws InterruptedException {
    System.out.println("getMergableStates merge all " + inputToString(pta));
    final Automaton<String> automaton = new Automaton<String>(true);
    for (String in : pta) {
      final List<String> seq = new ArrayList<String>(in.length());
      for (int i = 0; i < in.length(); i++) {
        seq.add(in.substring(i, i + 1));
      }
      automaton.buildPTAOnSymbol(seq);
    }
    final SKStrings<String> instance = new SKStrings<String>(k, s, "AND");
    boolean search = true;
    boolean found = false;
    List<List<List<State<String>>>> result = Collections.emptyList();
    while (search) {
      search = false;
      for (State<String> state1 : automaton.getDelta().keySet()) {
        for (State<String> state2 : automaton.getDelta().keySet()) {
          result = instance.getMergableStates(automaton);
          if (!result.isEmpty()) {
            found = true;
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
          search = true;
          found = false;
        }
      }
    }
    return automaton.toTestString();
  }
  
  @Test // Inspired in example on Table 3.4 of DavidLosThesis
    public void testDavidLoExampleTable34() throws InterruptedException {
        System.out.println("davidLo Examples - chapter 3");
        Automaton<String> automaton = this.getDavidLosAutomaton();

       
        Map<Integer, State<String>> states;
       
        states = new HashMap<Integer, State<String>>();
       
        for (State<String> st : automaton.getDelta().keySet()){
            states.put(st.getName(), st);
        }

        assertEquals(12, states.size());
       
        int k = 2;
        double s = 1.0;

        final SKStrings<String> alg = new SKStrings<String>(k, s, "AND");

        List<SKString<String>> sk0 = alg.findSKStrings(k, states.get(1), automaton.getDelta()).getSKStrings();
        List<SKString<String>> sk1 = alg.findSKStrings(k, states.get(2), automaton.getDelta()).getSKStrings();
        List<SKString<String>> sk2 = alg.findSKStrings(k, states.get(3), automaton.getDelta()).getSKStrings();
        List<SKString<String>> sk3 = alg.findSKStrings(k, states.get(4), automaton.getDelta()).getSKStrings();
        List<SKString<String>> sk4 = alg.findSKStrings(k, states.get(5), automaton.getDelta()).getSKStrings();
        List<SKString<String>> sk5 = alg.findSKStrings(k, states.get(6), automaton.getDelta()).getSKStrings();
        List<SKString<String>> sk6 = alg.findSKStrings(k, states.get(7), automaton.getDelta()).getSKStrings();
        List<SKString<String>> sk7 = alg.findSKStrings(k, states.get(8), automaton.getDelta()).getSKStrings();
        List<SKString<String>> sk8 = alg.findSKStrings(k, states.get(9), automaton.getDelta()).getSKStrings();
        List<SKString<String>> sk9 = alg.findSKStrings(k, states.get(10), automaton.getDelta()).getSKStrings();
        List<SKString<String>> sk10 = alg.findSKStrings(k, states.get(11), automaton.getDelta()).getSKStrings();
        List<SKString<String>> sk11 = alg.findSKStrings(k, states.get(12), automaton.getDelta()).getSKStrings();
       
        assertEquals(2, sk0.size());
        assertEquals("A", sk0.get(0).getStr().peekFirst().getAcceptSymbol());
        assertEquals("B", sk0.get(0).getStr().peekLast().getAcceptSymbol());
        assertEquals("A", sk0.get(1).getStr().peekFirst().getAcceptSymbol());
        assertEquals("E", sk0.get(1).getStr().peekLast().getAcceptSymbol());
        
        assertEquals(2, sk1.size());
        assertEquals("B", sk1.get(0).getStr().peekFirst().getAcceptSymbol());
        assertEquals("C", sk1.get(0).getStr().peekLast().getAcceptSymbol());
        assertEquals("E", sk1.get(1).getStr().peekFirst().getAcceptSymbol());
        assertEquals("B", sk1.get(1).getStr().peekLast().getAcceptSymbol());
        
        assertEquals(2, sk2.size());
        assertEquals("C", sk2.get(0).getStr().peekFirst().getAcceptSymbol());
        assertEquals("D", sk2.get(0).getStr().peekLast().getAcceptSymbol());
        assertEquals("C", sk2.get(1).getStr().peekFirst().getAcceptSymbol());
        assertEquals("X", sk2.get(1).getStr().peekLast().getAcceptSymbol());
        
        assertEquals(2, sk3.size()); // Lo's error
        assertEquals("D", sk3.get(0).getStr().peekFirst().getAcceptSymbol());
        assertEquals("E", sk3.get(0).getStr().peekLast().getAcceptSymbol());
        assertEquals("X", sk3.get(1).getStr().peekFirst().getAcceptSymbol());
        assertEquals("Y", sk3.get(1).getStr().peekLast().getAcceptSymbol());
        
        assertEquals(1, sk4.size());
        assertEquals("E", sk4.get(0).getStr().peekFirst().getAcceptSymbol());
        
        assertEquals(0, sk5.size());
        
        assertEquals(1, sk6.size());
        assertEquals("Y", sk6.get(0).getStr().peekFirst().getAcceptSymbol());
        
        assertEquals(0, sk7.size());
        
        assertEquals(1, sk8.size());
        assertEquals("B", sk8.get(0).getStr().peekFirst().getAcceptSymbol());
        assertEquals("D", sk8.get(0).getStr().peekLast().getAcceptSymbol());
        
        assertEquals(1, sk9.size());
        assertEquals("D", sk9.get(0).getStr().peekFirst().getAcceptSymbol());
        assertEquals("E", sk9.get(0).getStr().peekLast().getAcceptSymbol());
        
        assertEquals(1, sk10.size());
        assertEquals("E", sk10.get(0).getStr().peekFirst().getAcceptSymbol());
        
        assertEquals(0, sk11.size());
       
    }
    
    @Test
    public void testMergeDavidLoExamples() throws InterruptedException{
        Automaton<String> automaton = this.getDavidLosAutomaton();
        
        SKStrings<String> alg;
        int k=2;
        double s=1;
        
        alg = new SKStrings<>(k,s, "AND");
        
        alg.mergeStates(automaton);
        
        assertEquals(9, automaton.getDelta().size());
    }
    
    private Automaton<String> getDavidLosAutomaton() {
        List<String> trace1;
        trace1 = new LinkedList<>(Arrays.asList("A", "B", "C", "D", "E"));

        List<String> trace2;
        trace2 = new LinkedList<>(Arrays.asList("A", "B", "C", "X", "Y"));

        List<String> trace3;
        trace3 = new LinkedList<>(Arrays.asList("A", "E", "B", "D", "E"));

        Automaton<String> automaton;
        automaton = new Automaton<String>(true);

        automaton.buildPTAOnSymbol(trace1);
        automaton.buildPTAOnSymbol(trace2);
        automaton.buildPTAOnSymbol(trace3);
        return automaton;
    }
}
