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

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.AutomatonCloner;
import cz.cuni.mff.ksi.jinfer.base.automaton.AutomatonClonerSymbolConverter;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.SymbolToString;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifier;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class KHgrams<T> implements AutomatonSimplifier<T> {
  public KHgrams() {
    // TODO anti safety k,h check for k >= h >= 0
  }
  
  @Override
  public Automaton<T> simplify(Automaton<T> inputAutomaton, SymbolToString<T> symbolToString) throws InterruptedException {
    HelperAutomaton helperAutomaton = new HelperAutomaton(false, 2, 1);
    final Map<T, String> conversionMap= new HashMap<T, String>();
    final Map<String, T> reverseConversionMap = new HashMap<String, T>();
    AutomatonCloner<T, String> cl = new AutomatonCloner<T, String>();
    
    cl.convertAutomaton(inputAutomaton, helperAutomaton, new AutomatonClonerSymbolConverter<T, String>() {
      private char lastC = 'A';
      @Override
      public String convertSymbol(T symbol) {
        if (conversionMap.containsKey(symbol)) {
          return conversionMap.get(symbol);
        }
        String nstr = String.valueOf(lastC);
        conversionMap.put(symbol, nstr);
        reverseConversionMap.put(nstr, symbol);
        lastC+= 1;
        return nstr;
      }
    });
    
    helperAutomaton.kcontextMe();
    
    AutomatonCloner<String, T> lc = new AutomatonCloner<String, T>();
    
    Automaton<T> resultAutomaton= lc.convertAutomaton(helperAutomaton, new AutomatonClonerSymbolConverter<String, T>() {
      @Override
      public T convertSymbol(String symbol) {
        return reverseConversionMap.get(symbol);
      }
    });
    return resultAutomaton;
  }

  @Override
  public Automaton<T> simplify(Automaton<T> inputAutomaton, SymbolToString<T> symbolToString, String elementName) throws InterruptedException {
    return simplify(inputAutomaton, symbolToString);
  }
}

