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

  /**
   * Test of simplify method, of class KHgrams.
   */
  @Test
  public void testSimplify() throws Exception {
    assertEquals(
            "[1|0]>>1--a|1--2   1--b|1--6   [2|0]>>2--b|1--3   [3|0]>>3--a|1--4   [4|0]+[8|0]>>4--c|2--5   [5|2]+[9|1]   [6|0]>>6--b|1--7   [7|0]>>7--a|1--4   @@@[1|0]   [2|0]<<1--a|1--2   [3|0]<<2--b|1--3   [4|0]+[8|0]<<3--a|1--4   7--a|1--4   [5|2]+[9|1]<<4--c|2--5   [6|0]<<1--b|1--6   [7|0]<<6--b|1--7   ",
            testUniverzal(Arrays.<String>asList(
            "abac", "bbac"), 3, 2));

  }

  /**
   * Test of simplify method, of class KHgrams.
   */
  @Test
  public void testSimplify1() throws Exception {
    assertEquals(
            "[1|0]>>1--a|1--2   1--b|1--6   [2|0]>>2--b|1--3   [3|0]>>3--a|1--4   [4|0]+[8|0]>>4--c|2--5   [5|2]+[9|1]   [6|0]>>6--b|1--7   [7|0]>>7--a|1--4   @@@[1|0]   [2|0]<<1--a|1--2   [3|0]<<2--b|1--3   [4|0]+[8|0]<<3--a|1--4   7--a|1--4   [5|2]+[9|1]<<4--c|2--5   [6|0]<<1--b|1--6   [7|0]<<6--b|1--7   ",
            testUniverzal(Arrays.<String>asList(
            "abac", "bbac"), 3, 2));

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
