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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.greedy;

import java.util.ArrayList;
import java.util.Arrays;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.SymbolToString;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTester;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTesterFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.khcontext.KHContext;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class GreedyTest {

  public GreedyTest() {
  }

  private String inputToString(final List<String> pta) {
    return Arrays.deepToString(pta.toArray());
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
    Greedy<String> instance = new Greedy<String>(new MergeConditionTesterFactory() {

      @Override
      public <T> MergeConditionTester<T> create() {
        return new KHContext<T>(k, h);
      }

      @Override
      public String getName() {
        return "KHContextTestFactory";
      }

      @Override
      public String getDisplayName() {
        throw new UnsupportedOperationException("Not supported yet.");
      }

      @Override
      public String getModuleDescription() {
        throw new UnsupportedOperationException("Not supported yet.");
      }

      @Override
      public List<String> getCapabilities() {
        throw new UnsupportedOperationException("Not supported yet.");
      }

      @Override
      public String getUserModuleDescription() {
        throw new UnsupportedOperationException("Not supported yet.");
      }
    });
    Automaton<String> result = instance.simplify(automaton, new SymbolToString<String>() {

      @Override
      public String toString(String symbol) {
        return symbol;
      }
    });
    return result.toTestString();
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
            // "[1|0]>>1--a|1--2   1--b|1--5   [2|0]>>2--b|1--3   [3|0]+[6|0]>>3--a|2--4   [4|2]+[7|1]   [5|0]>>5--b|1--3   @@@[1|0]   [2|0]<<1--a|1--2   [3|0]+[6|0]<<2--b|1--3   5--b|1--3   [4|2]+[7|1]<<3--a|2--4   [5|0]<<1--b|1--5   ",
            "[1|0]>>1--a|1--2   1--b|1--5   [2|0]>>2--b|1--6   [5|0]>>5--b|1--6   [6|0]+[3|0]>>6--a|2--7   [7|2]+[4|1]   @@@[1|0]   [2|0]<<1--a|1--2   [5|0]<<1--b|1--5   [6|0]+[3|0]<<2--b|1--6   5--b|1--6   [7|2]+[4|1]<<6--a|2--7   ",
            testUniverzal(Arrays.<String>asList(
            "aba", "bba"), 2, 1));
  }

  /**
   * Test of simplify method, of class KHgrams.
   */
  @Test
  public void testSimplify14() throws Exception {
    assertEquals(
            //            "[1|0]>>1--a|1--2   1--b|1--6   [2|0]>>2--b|1--3   [3|0]>>3--a|1--4   [4|0]+[8|0]>>4--c|2--5   [5|2]+[9|1]   [6|0]>>6--b|1--7   [7|0]>>7--a|1--4   @@@[1|0]   [2|0]<<1--a|1--2   [3|0]<<2--b|1--3   [4|0]+[8|0]<<3--a|1--4   7--a|1--4   [5|2]+[9|1]<<4--c|2--5   [6|0]<<1--b|1--6   [7|0]<<6--b|1--7   ",
            "[1|0]>>1--a|1--2   1--b|1--6   [2|0]>>2--b|1--3   [3|0]>>3--a|1--8   [6|0]>>6--b|1--7   [7|0]>>7--a|1--8   [8|0]+[4|0]>>8--c|2--9   [9|2]+[5|1]   @@@[1|0]   [2|0]<<1--a|1--2   [3|0]<<2--b|1--3   [6|0]<<1--b|1--6   [7|0]<<6--b|1--7   [8|0]+[4|0]<<3--a|1--8   7--a|1--8   [9|2]+[5|1]<<8--c|2--9   ",
            testUniverzal(Arrays.<String>asList(
            "abac", "bbac"), 3, 2));

  }

  /**
   * Test of simplify method, of class KHgrams.
   */
  @Test
  public void testSimplify15() throws Exception {
    assertEquals(
            //            "[1|0]>>1--h|3--2   1--p|1--7   [2|0]>>2--p|1--3   2--h|1--11   2--e|1--17   [3|0]>>3--e|1--4   [4|0]+[13|0]>>4--e|2--5   [5|0]+[9|0]+[14|0]>>5--e|2--6   5--h|1--10   [6|1]+[15|0]>>6--h|1--10   [7|0]>>7--e|1--8   [8|0]>>8--e|1--5   [10|2]+[16|1]   [11|0]>>11--p|1--12   [12|0]>>12--e|1--4   [17|0]>>17--e|1--18   [18|1]   @@@[1|0]   [2|0]<<1--h|3--2   [3|0]<<2--p|1--3   [4|0]+[13|0]<<3--e|1--4   12--e|1--4   [5|0]+[9|0]+[14|0]<<4--e|2--5   8--e|1--5   [6|1]+[15|0]<<5--e|2--6   [7|0]<<1--p|1--7   [8|0]<<7--e|1--8   [10|2]+[16|1]<<5--h|1--10   6--h|1--10   [11|0]<<2--h|1--11   [12|0]<<11--p|1--12   [17|0]<<2--e|1--17   [18|1]<<17--e|1--18   ",
            "[1|0]>>1--h|3--2   1--p|1--7   [2|0]>>2--p|1--3   2--h|1--11   2--e|1--17   [3|0]>>3--e|1--13   [7|0]>>7--e|1--8   [8|0]>>8--e|1--14   [11|0]>>11--p|1--12   [12|0]>>12--e|1--13   [13|0]+[4|0]>>13--e|2--14   [14|0]+[9|0]+[5|0]>>14--e|2--15   14--h|1--16   [15|1]+[6|1]>>15--h|1--16   [16|2]+[10|1]   [17|0]>>17--e|1--18   [18|1]   @@@[1|0]   [2|0]<<1--h|3--2   [3|0]<<2--p|1--3   [7|0]<<1--p|1--7   [8|0]<<7--e|1--8   [11|0]<<2--h|1--11   [12|0]<<11--p|1--12   [13|0]+[4|0]<<3--e|1--13   12--e|1--13   [14|0]+[9|0]+[5|0]<<8--e|1--14   13--e|2--14   [15|1]+[6|1]<<14--e|2--15   [16|2]+[10|1]<<14--h|1--16   15--h|1--16   [17|0]<<2--e|1--17   [18|1]<<17--e|1--18   ",
            testUniverzal(Arrays.<String>asList(
            "hpeee", "peeh", "hhpeeeh", "hee"), 3, 3));

  }

  /**
   * Test of simplify method, of class KHgrams.
   */
  @Test
  public void testSimplify16() throws Exception {
    assertEquals(
            //            "[1|0]>>1--h|3--2   1--p|1--7   [2|0]>>2--p|1--3   2--h|1--11   2--e|1--17   [3|0]+[12|0]>>3--e|2--4   [4|0]+[13|0]+[8|0]>>4--e|3--5   [5|1]+[9|0]+[14|0]+[6|1]+[15|0]>>5--e|2--5   5--h|2--10   [7|0]>>7--e|1--4   [10|2]+[16|1]   [11|0]>>11--p|1--3   [17|0]>>17--e|1--18   [18|1]   @@@[1|0]   [2|0]<<1--h|3--2   [3|0]+[12|0]<<2--p|1--3   11--p|1--3   [4|0]+[13|0]+[8|0]<<3--e|2--4   7--e|1--4   [5|1]+[9|0]+[14|0]+[6|1]+[15|0]<<4--e|3--5   5--e|2--5   [7|0]<<1--p|1--7   [10|2]+[16|1]<<5--h|2--10   [11|0]<<2--h|1--11   [17|0]<<2--e|1--17   [18|1]<<17--e|1--18   ",
            "[1|0]>>1--h|3--2   1--p|1--7   [2|0]>>2--p|1--12   2--h|1--11   2--e|1--17   [7|0]>>7--e|1--13   [11|0]>>11--p|1--12   [12|0]+[3|0]>>12--e|2--13   [13|0]+[4|0]+[8|0]>>13--e|3--15   [15|1]+[14|0]+[9|0]+[5|0]+[6|1]>>15--h|2--16   15--e|2--15   [16|2]+[10|1]   [17|0]>>17--e|1--18   [18|1]   @@@[1|0]   [2|0]<<1--h|3--2   [7|0]<<1--p|1--7   [11|0]<<2--h|1--11   [12|0]+[3|0]<<2--p|1--12   11--p|1--12   [13|0]+[4|0]+[8|0]<<7--e|1--13   12--e|2--13   [15|1]+[14|0]+[9|0]+[5|0]+[6|1]<<13--e|3--15   15--e|2--15   [16|2]+[10|1]<<15--h|2--16   [17|0]<<2--e|1--17   [18|1]<<17--e|1--18   ",
            testUniverzal(Arrays.<String>asList(
            "hpeee", "peeh", "hhpeeeh", "hee"), 3, 2));

  }

  /**
   * Test of simplify method, of class KHgrams.
   */
  @Test
  public void testSimplify17() throws Exception {
    assertEquals(
            //            "[1|0]>>1--h|3--2   1--p|1--3   [2|0]+[11|0]>>2--p|2--3   2--h|1--2   2--e|1--17   [3|0]+[12|0]+[7|0]>>3--e|3--4   [4|1]+[13|0]+[8|0]+[5|0]+[9|0]+[14|0]+[6|1]+[15|0]>>4--e|5--4   4--h|2--10   [10|2]+[16|1]   [17|0]>>17--e|1--18   [18|1]   @@@[1|0]   [2|0]+[11|0]<<1--h|3--2   2--h|1--2   [3|0]+[12|0]+[7|0]<<2--p|2--3   1--p|1--3   [4|1]+[13|0]+[8|0]+[5|0]+[9|0]+[14|0]+[6|1]+[15|0]<<3--e|3--4   4--e|5--4   [10|2]+[16|1]<<4--h|2--10   [17|0]<<2--e|1--17   [18|1]<<17--e|1--18   ",
            "[1|0]>>1--h|3--11   1--p|1--12   [11|0]+[2|0]>>11--p|2--12   11--h|1--11   11--e|1--17   [12|0]+[3|0]+[7|0]>>12--e|3--15   [15|1]+[14|0]+[9|0]+[5|0]+[13|0]+[4|0]+[8|0]+[6|1]>>15--h|2--16   15--e|5--15   [16|2]+[10|1]   [17|0]>>17--e|1--18   [18|1]   @@@[1|0]   [11|0]+[2|0]<<1--h|3--11   11--h|1--11   [12|0]+[3|0]+[7|0]<<1--p|1--12   11--p|2--12   [15|1]+[14|0]+[9|0]+[5|0]+[13|0]+[4|0]+[8|0]+[6|1]<<12--e|3--15   15--e|5--15   [16|2]+[10|1]<<15--h|2--16   [17|0]<<11--e|1--17   [18|1]<<17--e|1--18   ",
            testUniverzal(Arrays.<String>asList(
            "hpeee", "peeh", "hhpeeeh", "hee"), 3, 1));

  }
}
