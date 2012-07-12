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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.khgrams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.SymbolToString;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class KHgramsTest {

  public KHgramsTest() {
  }

  private String inputToString(final List<String> pta) {
    return Arrays.deepToString(pta.toArray());
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
            "[1|0]>>1--a|1--2   1--b|1--5   [2|0]>>2--b|1--3   [3|0]+[6|0]>>3--a|2--4   [4|2]+[7|1]   [5|0]>>5--b|1--3   @@@[1|0]   [2|0]<<1--a|1--2   [3|0]+[6|0]<<2--b|1--3   5--b|1--3   [4|2]+[7|1]<<3--a|2--4   [5|0]<<1--b|1--5   ",
            testUniverzal(Arrays.<String>asList(
            "aba", "bba"), 2, 1));
  }

  /**
   * Test of simplify method, of class KHgrams.
   */
  @Test
  public void testSimplify14() throws Exception {
    assertEquals(
            "[1|0]>>1--a|1--2   1--b|1--6   [2|0]>>2--b|1--3   [3|0]>>3--a|1--4   [4|0]+[8|0]>>4--c|2--5   [5|2]+[9|1]   [6|0]>>6--b|1--7   [7|0]>>7--a|1--4   @@@[1|0]   [2|0]<<1--a|1--2   [3|0]<<2--b|1--3   [4|0]+[8|0]<<3--a|1--4   7--a|1--4   [5|2]+[9|1]<<4--c|2--5   [6|0]<<1--b|1--6   [7|0]<<6--b|1--7   ",
            testUniverzal(Arrays.<String>asList(
            "abac", "bbac"), 3, 2));

  }

  /**
   * Test of simplify method, of class KHgrams.
   */
  @Test
  public void testSimplify15() throws Exception {
    assertEquals(
            "[1|0]>>1--h|3--2   1--p|1--7   [2|0]>>2--p|1--3   2--h|1--11   2--e|1--17   [3|0]>>3--e|1--4   [4|0]+[13|0]>>4--e|2--5   [5|0]+[9|0]+[14|0]>>5--e|2--6   5--h|1--10   [6|1]+[15|0]>>6--h|1--10   [7|0]>>7--e|1--8   [8|0]>>8--e|1--5   [10|2]+[16|1]   [11|0]>>11--p|1--12   [12|0]>>12--e|1--4   [17|0]>>17--e|1--18   [18|1]   @@@[1|0]   [2|0]<<1--h|3--2   [3|0]<<2--p|1--3   [4|0]+[13|0]<<3--e|1--4   12--e|1--4   [5|0]+[9|0]+[14|0]<<4--e|2--5   8--e|1--5   [6|1]+[15|0]<<5--e|2--6   [7|0]<<1--p|1--7   [8|0]<<7--e|1--8   [10|2]+[16|1]<<5--h|1--10   6--h|1--10   [11|0]<<2--h|1--11   [12|0]<<11--p|1--12   [17|0]<<2--e|1--17   [18|1]<<17--e|1--18   ",
            testUniverzal(Arrays.<String>asList(
            "hpeee", "peeh", "hhpeeeh", "hee"
            ), 3, 3));

  }

  /**
   * Test of simplify method, of class KHgrams.
   */
  @Test
  public void testSimplify16() throws Exception {
    assertEquals(
            "[1|0]>>1--h|3--2   1--p|1--7   [2|0]>>2--p|1--3   2--h|1--11   2--e|1--17   [3|0]+[12|0]>>3--e|2--4   [4|0]+[13|0]+[8|0]>>4--e|3--6   [6|1]+[5|0]+[9|0]+[14|0]+[15|0]>>6--e|2--6   6--h|2--10   [7|0]>>7--e|1--4   [10|2]+[16|1]   [11|0]>>11--p|1--3   [17|0]>>17--e|1--18   [18|1]   @@@[1|0]   [2|0]<<1--h|3--2   [3|0]+[12|0]<<2--p|1--3   11--p|1--3   [4|0]+[13|0]+[8|0]<<3--e|2--4   7--e|1--4   [6|1]+[5|0]+[9|0]+[14|0]+[15|0]<<4--e|3--6   6--e|2--6   [7|0]<<1--p|1--7   [10|2]+[16|1]<<6--h|2--10   [11|0]<<2--h|1--11   [17|0]<<2--e|1--17   [18|1]<<17--e|1--18   ",
            testUniverzal(Arrays.<String>asList(
            "hpeee", "peeh", "hhpeeeh", "hee"
            ), 3, 2));

  }
  
  /**
   * Test of simplify method, of class KHgrams.
   */
  @Test
  public void testSimplify17() throws Exception {
    assertEquals(
            "[1|0]>>1--h|3--2   1--p|1--3   [2|0]+[11|0]>>2--p|2--3   2--h|1--2   2--e|1--17   [3|0]+[12|0]+[7|0]>>3--e|3--6   [6|1]+[4|0]+[13|0]+[8|0]+[5|0]+[9|0]+[14|0]+[15|0]>>6--e|5--6   6--h|2--10   [10|2]+[16|1]   [17|0]>>17--e|1--18   [18|1]   @@@[1|0]   [2|0]+[11|0]<<1--h|3--2   2--h|1--2   [3|0]+[12|0]+[7|0]<<1--p|1--3   2--p|2--3   [6|1]+[4|0]+[13|0]+[8|0]+[5|0]+[9|0]+[14|0]+[15|0]<<3--e|3--6   6--e|5--6   [10|2]+[16|1]<<6--h|2--10   [17|0]<<2--e|1--17   [18|1]<<17--e|1--18   ",
            testUniverzal(Arrays.<String>asList(
            "hpeee", "peeh", "hhpeeeh", "hee"
            ), 3, 1));

  }  
  
  private String testUniverzal(List<String> pta, final int k, final int h) throws InterruptedException {
    System.out.println("simplify " + inputToString(pta));
    final Automaton<String> automaton = new Automaton<String>(true);
    for (String in : pta) {
      final List<String> seq = new ArrayList<String>(in.length());
      for (int i = 0; i < in.length(); i++) {
        seq.add(in.substring(i, i + 1));
      }
      automaton.buildPTAOnSymbol(seq);
    }
    KHgrams<String> instance = new KHgrams<String>(k, h);
    Automaton<String> result = instance.simplify(automaton, new SymbolToString<String>() {

      @Override
      public String toString(String symbol) {
        return symbol;
      }
    });
    return result.toTestString();
  }
}
