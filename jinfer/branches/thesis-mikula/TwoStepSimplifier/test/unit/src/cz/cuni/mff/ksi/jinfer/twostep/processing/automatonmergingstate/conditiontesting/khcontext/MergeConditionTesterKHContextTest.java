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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.khcontext;

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite of MergeConditionTesterKHContext.
 *
 * @author anti
 */
@SuppressWarnings("PMD.SystemPrintln")
public class MergeConditionTesterKHContextTest {

  private String inputToString(final List<String> pta) {
    return Arrays.deepToString(pta.toArray());
  }

  /**
   * Test of getMergableStates method, of class MergeConditionTesterKHContext.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBadKHSet() throws InterruptedException {
    System.out.println("k,h bad values set");
    final Automaton<String> automaton = new Automaton<String>(true);
    automaton.buildPTAOnSymbol(Arrays.<String>asList());
    final KHContext<String> instance = new KHContext<String>(3, 4);
    final List<List<List<State<String>>>> result = instance.getMergableStates(automaton);
  }

  /**
   * Test of getMergableStates method, of class MergeConditionTesterKHContext.
   */
  public void testGetMergableStatesEmptyAut() throws InterruptedException {
    System.out.println("getMergableStates empty automaton");
    final Automaton<String> automaton = new Automaton<String>(false);
    final KHContext<String> instance = new KHContext<String>(2, 1);
    final List<List<List<State<String>>>> result = instance.getMergableStates(automaton);
    assertTrue(result.isEmpty());
  }

  private void testGetMergableStatesNoAlt(final List<String> pta, final int k, final int h) throws InterruptedException {
    System.out.println("getMergableStates no alternatives " + inputToString(pta));
    final Automaton<String> automaton = new Automaton<String>(true);
    for (String in : pta) {
      final List<String> seq = new ArrayList<String>(in.length());
      for (int i = 0; i < in.length(); i++) {
        seq.add(in.substring(i, i + 1));
      }
      automaton.buildPTAOnSymbol(seq);
    }
    final KHContext<String> instance = new KHContext<String>(k, h);
    final List<List<List<State<String>>>> result = instance.getMergableStates(automaton);
    assertTrue(result.isEmpty());
  }

  private String testUniverzal(final List<String> pta, final int k, final int h) throws InterruptedException {
    System.out.println("getMergableStates merge all " + inputToString(pta));
    final Automaton<String> automaton = new Automaton<String>(true);
    for (String in : pta) {
      final List<String> seq = new ArrayList<String>(in.length());
      for (int i = 0; i < in.length(); i++) {
        seq.add(in.substring(i, i + 1));
      }
      automaton.buildPTAOnSymbol(seq);
    }
    final KHContext<String> instance = new KHContext<String>(k, h);
    boolean search = true;
    boolean found = false;
    List<List<List<State<String>>>> result = Collections.emptyList();
    result = instance.getMergableStates(automaton);
    if (!result.isEmpty()) {
      for (List<List<State<String>>> alt : result) {
        for (List<State<String>> merg : alt) {
          automaton.mergeStates(merg);
        }
      }
    }

    return automaton.toTestString();
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
            "aa", "ab", "ac", "ad", "aba", "aca", "ada"), 2, 2);
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
  public void testGetMergableStates12() throws InterruptedException {
    assertEquals(
            "[1|0]>>1--a|2--2   [2|0]>>2--a|2--3   [3|2]   @@@[1|0]   [2|0]<<1--a|2--2   [3|2]<<2--a|2--3   ",
            testUniverzal(Arrays.<String>asList(
            "aa", "aa"), 2, 1));
  }

  @Test
  public void testGetMergableStates13() throws InterruptedException {
    assertEquals(
//            "[1|0]>>1--a|1--2   1--b|1--5   [2|0]>>2--b|1--3   [3|0]+[6|0]>>3--a|2--4   [4|2]+[7|1]   [5|0]>>5--b|1--3   @@@[1|0]   [2|0]<<1--a|1--2   [3|0]+[6|0]<<2--b|1--3   5--b|1--3   [4|2]+[7|1]<<3--a|2--4   [5|0]<<1--b|1--5   ",
              "[1|0]>>1--a|1--2   1--b|1--5   [2|0]>>2--b|1--6   [5|0]>>5--b|1--6   [6|0]+[3|0]>>6--a|2--7   [7|2]+[4|1]   @@@[1|0]   [2|0]<<1--a|1--2   [5|0]<<1--b|1--5   [6|0]+[3|0]<<5--b|1--6   2--b|1--6   [7|2]+[4|1]<<6--a|2--7   ",
            testUniverzal(Arrays.<String>asList(
            "aba", "bba"), 2, 1));
  }

  @Test
  public void testGetMergableStates14() throws InterruptedException {
    assertEquals(
//            "[1|0]>>1--a|1--2   1--b|1--6   [2|0]>>2--b|1--3   [3|0]>>3--a|1--4   [4|0]+[8|0]>>4--c|2--5   [5|2]+[9|1]   [6|0]>>6--b|1--7   [7|0]>>7--a|1--4   @@@[1|0]   [2|0]<<1--a|1--2   [3|0]<<2--b|1--3   [4|0]+[8|0]<<3--a|1--4   7--a|1--4   [5|2]+[9|1]<<4--c|2--5   [6|0]<<1--b|1--6   [7|0]<<6--b|1--7   ",
              "[1|0]>>1--a|1--2   1--b|1--6   [2|0]>>2--b|1--3   [3|0]>>3--a|1--8   [6|0]>>6--b|1--7   [7|0]>>7--a|1--8   [8|0]+[4|0]>>8--c|2--9   [9|2]+[5|1]   @@@[1|0]   [2|0]<<1--a|1--2   [3|0]<<2--b|1--3   [6|0]<<1--b|1--6   [7|0]<<6--b|1--7   [8|0]+[4|0]<<7--a|1--8   3--a|1--8   [9|2]+[5|1]<<8--c|2--9   ",
            testUniverzal(Arrays.<String>asList(
            "abac", "bbac"), 3, 2));
  }

  /**
   * Test of simplify method, of class KHgrams.
   */
  @Test
  public void testGetMergableStates15() throws Exception {
    assertEquals(
//            "[1|0]>>1--h|3--2   1--p|1--7   [2|0]>>2--p|1--3   2--h|1--11   2--e|1--17   [3|0]>>3--e|1--4   [4|0]+[13|0]>>4--e|2--5   [5|0]+[9|0]+[14|0]>>5--e|2--6   5--h|1--10   [6|1]+[15|0]>>6--h|1--10   [7|0]>>7--e|1--8   [8|0]>>8--e|1--5   [10|2]+[16|1]   [11|0]>>11--p|1--12   [12|0]>>12--e|1--4   [17|0]>>17--e|1--18   [18|1]   @@@[1|0]   [2|0]<<1--h|3--2   [3|0]<<2--p|1--3   [4|0]+[13|0]<<3--e|1--4   12--e|1--4   [5|0]+[9|0]+[14|0]<<4--e|2--5   8--e|1--5   [6|1]+[15|0]<<5--e|2--6   [7|0]<<1--p|1--7   [8|0]<<7--e|1--8   [10|2]+[16|1]<<5--h|1--10   6--h|1--10   [11|0]<<2--h|1--11   [12|0]<<11--p|1--12   [17|0]<<2--e|1--17   [18|1]<<17--e|1--18   ",
            "[1|0]>>1--h|3--2   1--p|1--7   [2|0]>>2--p|1--3   2--h|1--11   2--e|1--17   [3|0]>>3--e|1--13   [7|0]>>7--e|1--8   [8|0]>>8--e|1--14   [11|0]>>11--p|1--12   [12|0]>>12--e|1--13   [13|0]+[4|0]>>13--e|2--14   [14|0]+[9|0]+[5|0]>>14--e|2--15   14--h|1--16   [15|1]+[6|1]>>15--h|1--16   [16|2]+[10|1]   [17|0]>>17--e|1--18   [18|1]   @@@[1|0]   [2|0]<<1--h|3--2   [3|0]<<2--p|1--3   [7|0]<<1--p|1--7   [8|0]<<7--e|1--8   [11|0]<<2--h|1--11   [12|0]<<11--p|1--12   [13|0]+[4|0]<<12--e|1--13   3--e|1--13   [14|0]+[9|0]+[5|0]<<13--e|2--14   8--e|1--14   [15|1]+[6|1]<<14--e|2--15   [16|2]+[10|1]<<15--h|1--16   14--h|1--16   [17|0]<<2--e|1--17   [18|1]<<17--e|1--18   ",
            testUniverzal(Arrays.<String>asList(
            "hpeee", "peeh", "hhpeeeh", "hee"), 3, 3));

  }

  /**
   * Test of simplify method, of class KHgrams.
   */
  @Test
  public void testGetMergableStates16() throws Exception {
    assertEquals(
           // "",
           // "[1|0]>>1--h|3--2   1--p|1--7   [2|0]>>2--p|1--3   2--h|1--11   2--e|1--17   [3|0]+[12|0]>>3--e|2--4   [4|0]+[13|0]+[8|0]>>4--e|3--5   [5|1]+[9|0]+[14|0]+[6|1]+[15|0]>>5--e|2--5   5--h|2--10   [7|0]>>7--e|1--4   [10|2]+[16|1]   [11|0]>>11--p|1--3   [17|0]>>17--e|1--18   [18|1]   @@@[1|0]   [2|0]<<1--h|3--2   [3|0]+[12|0]<<2--p|1--3   11--p|1--3   [4|0]+[13|0]+[8|0]<<3--e|2--4   7--e|1--4   [5|1]+[9|0]+[14|0]+[6|1]+[15|0]<<4--e|3--5   5--e|2--5   [7|0]<<1--p|1--7   [10|2]+[16|1]<<5--h|2--10   [11|0]<<2--h|1--11   [17|0]<<2--e|1--17   [18|1]<<17--e|1--18   ",
            //"[1|0]>>1--h|3--2   1--p|1--7   [2|0]>>2--p|1--3   2--h|1--11   2--e|1--17   [3|0]+[12|0]>>3--e|2--4   [4|0]+[13|0]+[8|0]>>4--e|3--6   [6|1]+[5|0]+[9|0]+[14|0]+[15|0]>>6--e|2--6   6--h|2--10   [7|0]>>7--e|1--4   [10|2]+[16|1]   [11|0]>>11--p|1--3   [17|0]>>17--e|1--18   [18|1]   @@@[1|0]   [2|0]<<1--h|3--2   [3|0]+[12|0]<<2--p|1--3   11--p|1--3   [4|0]+[13|0]+[8|0]<<3--e|2--4   7--e|1--4   [6|1]+[5|0]+[9|0]+[14|0]+[15|0]<<6--e|2--6   4--e|3--6   [7|0]<<1--p|1--7   [10|2]+[16|1]<<6--h|2--10   [11|0]<<2--h|1--11   [17|0]<<2--e|1--17   [18|1]<<17--e|1--18   ",
            "[1|0]>>1--h|3--2   1--p|1--7   [2|0]>>2--p|1--12   2--h|1--11   2--e|1--17   [7|0]>>7--e|1--13   [11|0]>>11--p|1--12   [12|0]+[3|0]>>12--e|2--13   [13|0]+[4|0]+[8|0]>>13--e|3--15   [15|1]+[14|0]+[9|0]+[5|0]+[6|1]>>15--h|2--16   15--e|2--15   [16|2]+[10|1]   [17|0]>>17--e|1--18   [18|1]   @@@[1|0]   [2|0]<<1--h|3--2   [7|0]<<1--p|1--7   [11|0]<<2--h|1--11   [12|0]+[3|0]<<11--p|1--12   2--p|1--12   [13|0]+[4|0]+[8|0]<<12--e|2--13   7--e|1--13   [15|1]+[14|0]+[9|0]+[5|0]+[6|1]<<15--e|2--15   13--e|3--15   [16|2]+[10|1]<<15--h|2--16   [17|0]<<2--e|1--17   [18|1]<<17--e|1--18   ",
            testUniverzal(Arrays.<String>asList(
            "hpeee", "peeh", "hhpeeeh", "hee"), 3, 2));

  }

  /**
   * Test of simplify method, of class KHgrams.
   */
  @Test
  public void testGetMergableStates17() throws Exception {
    assertEquals(
          //  "[1|0]>>1--h|3--2   1--p|1--3   [2|0]+[11|0]>>2--p|2--3   2--h|1--2   2--e|1--17   [3|0]+[12|0]+[7|0]>>3--e|3--4   [4|1]+[13|0]+[8|0]+[5|0]+[9|0]+[14|0]+[6|1]+[15|0]>>4--e|5--4   4--h|2--10   [10|2]+[16|1]   [17|0]>>17--e|1--18   [18|1]   @@@[1|0]   [2|0]+[11|0]<<1--h|3--2   2--h|1--2   [3|0]+[12|0]+[7|0]<<2--p|2--3   1--p|1--3   [4|1]+[13|0]+[8|0]+[5|0]+[9|0]+[14|0]+[6|1]+[15|0]<<3--e|3--4   4--e|5--4   [10|2]+[16|1]<<4--h|2--10   [17|0]<<2--e|1--17   [18|1]<<17--e|1--18   ",
            //"[1|0]>>1--h|3--2   1--p|1--3   [2|0]+[11|0]>>2--p|2--3   2--h|1--2   2--e|1--17   [3|0]+[12|0]+[7|0]>>3--e|3--6   [6|1]+[4|0]+[13|0]+[8|0]+[5|0]+[9|0]+[14|0]+[15|0]>>6--e|5--6   6--h|2--10   [10|2]+[16|1]   [17|0]>>17--e|1--18   [18|1]   @@@[1|0]   [2|0]+[11|0]<<1--h|3--2   2--h|1--2   [3|0]+[12|0]+[7|0]<<2--p|2--3   1--p|1--3   [6|1]+[4|0]+[13|0]+[8|0]+[5|0]+[9|0]+[14|0]+[15|0]<<6--e|5--6   3--e|3--6   [10|2]+[16|1]<<6--h|2--10   [17|0]<<2--e|1--17   [18|1]<<17--e|1--18   ",  
            "[1|0]>>1--h|3--11   1--p|1--12   [11|0]+[2|0]>>11--p|2--12   11--h|1--11   11--e|1--17   [12|0]+[3|0]+[7|0]>>12--e|3--15   [15|1]+[14|0]+[9|0]+[5|0]+[13|0]+[4|0]+[8|0]+[6|1]>>15--h|2--16   15--e|5--15   [16|2]+[10|1]   [17|0]>>17--e|1--18   [18|1]   @@@[1|0]   [11|0]+[2|0]<<11--h|1--11   1--h|3--11   [12|0]+[3|0]+[7|0]<<11--p|2--12   1--p|1--12   [15|1]+[14|0]+[9|0]+[5|0]+[13|0]+[4|0]+[8|0]+[6|1]<<15--e|5--15   12--e|3--15   [16|2]+[10|1]<<15--h|2--16   [17|0]<<11--e|1--17   [18|1]<<17--e|1--18   ",
            testUniverzal(Arrays.<String>asList(
            "hpeee", "peeh", "hhpeeeh", "hee"), 3, 1));

  }
}
