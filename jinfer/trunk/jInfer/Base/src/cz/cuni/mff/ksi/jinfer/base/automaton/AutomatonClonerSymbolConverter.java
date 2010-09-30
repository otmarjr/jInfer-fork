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

/**
 * Interface for AutomatonCloner<A, B>
 *
 * Has to implement only one method - convertSymbol, in which it has to return
 * symbol of type B, for each possible symbol of type A.
 *
 * But wait, it has to be .equals() consistent. Once it returns instance X of
 * type B, as a symbol corresponding to input instance Y (of type A), every
 * next call with instance Z (of type A), for which Z.equals(Y), method  must
 * return instance Z', for which:
 * (Z').equals(X)
 *
 * It can be accomplished as a simple memory inside converter between subsequent
 * calls of course (and returning really same references).
 *
 * @author anti
 */
public interface AutomatonClonerSymbolConverter<A, B> {
  B convertSymbol(final A symbol);
}
