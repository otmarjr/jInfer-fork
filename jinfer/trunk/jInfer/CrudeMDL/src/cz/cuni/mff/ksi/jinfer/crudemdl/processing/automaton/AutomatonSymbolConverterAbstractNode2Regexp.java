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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class AutomatonSymbolConverterAbstractNode2Regexp implements AutomatonSymbolConverter<AbstractNode, Regexp<AbstractNode>>{
  private Map<AbstractNode, Regexp<AbstractNode>> conversionMap;

  public AutomatonSymbolConverterAbstractNode2Regexp() {
    this.conversionMap= new HashMap<AbstractNode, Regexp<AbstractNode>>();
  }

  @Override
  public Regexp<AbstractNode> convertSymbol(AbstractNode symbol) {
    if (this.conversionMap.containsKey(symbol)) {
      return this.conversionMap.get(symbol);
    } else {
      final Regexp<AbstractNode> newSymbol= Regexp.<AbstractNode>getToken(symbol);
      this.conversionMap.put(symbol, newSymbol);
      return newSymbol;
    }
  }
}
