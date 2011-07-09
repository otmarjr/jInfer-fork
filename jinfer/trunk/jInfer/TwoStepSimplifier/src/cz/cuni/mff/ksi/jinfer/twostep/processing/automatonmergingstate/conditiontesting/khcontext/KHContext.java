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

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTester;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.AutomatonCloner;
import cz.cuni.mff.ksi.jinfer.base.automaton.AutomatonClonerSymbolConverter;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * k,h-context equivalence criterion implementation.
 *
 * It first finds all contexts of two states, then compares each pair. Returns
 * list of alternative merge lists of pairs.
 *
 * Not using k-grams algorithm.
 * 
 * @author anti
 */
public class KHContext<T> implements MergeConditionTester<T> {

  private int k;
  private int h;

  private void checkConstraits() {
    if (!((h >= 0) && (k >= h))) {
      throw new IllegalArgumentException("Parameter k must be greater than h, be grater than zero. k >= h => 0.");
    }
  }

  /**
   * Setting k,h. It has to be k >= h, or exception thrown (cannot merge more than
   * k states on path, when only k of them were examined).
   * @param k
   * @param h
   */
  public KHContext(final int k, final int h) {
    this.k = k;
    this.h = h;
    checkConstraits();
  }

  @Override
  public List<List<List<State<T>>>> getMergableStates(final Automaton<T> automaton) throws InterruptedException {
    KHContextHelperAutomaton helperAutomaton = new KHContextHelperAutomaton(false, this.k, this.h);
    final Map<T, String> conversionMap = new HashMap<T, String>();
    final Map<String, T> reverseConversionMap = new HashMap<String, T>();
    AutomatonCloner<T, String> cl = new AutomatonCloner<T, String>();

    cl.convertAutomaton(automaton, helperAutomaton, new AutomatonClonerSymbolConverter<T, String>() {

      private char lastC = 'A';

      @Override
      public String convertSymbol(T symbol) {
        if (conversionMap.containsKey(symbol)) {
          return conversionMap.get(symbol);
        }
        String nstr = String.valueOf(lastC);
        conversionMap.put(symbol, nstr);
        reverseConversionMap.put(nstr, symbol);
        lastC += 1;
        return nstr;
      }
    });

    helperAutomaton.kcontextMe();
    Map<String, Set<StateHString>> contextMap = helperAutomaton.getContextMap();

    List<List<List<State<T>>>> alts = new LinkedList<List<List<State<T>>>>();
    for (String key : contextMap.keySet()) {
      if (contextMap.get(key).size() >= 2) {
        List<List<State<T>>> mergSeq = new LinkedList<List<State<T>>>();
        Iterator<StateHString> it = contextMap.get(key).iterator();
        Deque<State<String>> first = it.next().getStates();
        int j = 0;
        for (State<String> fS : first) {
          if (j >= this.h) {
            List<State<T>> tmp = new LinkedList<State<T>>();
            tmp.add(cl.getReverseStateConversionMap().get(fS));
            mergSeq.add(tmp);
          }
          j++;
        }
        while (it.hasNext()) {
          StateHString merg = it.next();
          Iterator<State<String>> mergIt = merg.getStates().iterator();
          Iterator<List<State<T>>> mergseqIt = mergSeq.iterator();
          int i = 0;
          while (mergseqIt.hasNext()) {
            if (Thread.interrupted()) {
              throw new InterruptedException();
            }
            if (i >= this.h) {
              mergseqIt.next().add(cl.getReverseStateConversionMap().get(mergIt.next()));
            } else {
              mergIt.next();
            }
            i++;
          }
        }
        if (!mergSeq.isEmpty()) {
          alts.add(mergSeq);
        }
      }
    }
    return alts;

  }
}
