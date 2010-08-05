/*
 *  Copyright (C) 2010 vektor
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
package cz.cuni.mff.ksi.jinfer.base.regexp;

/**
 * Enum of possible regular expression types.
 *
 * @author vektor
 */
public enum RegexpType {

  /** An empty letter λ, represantating empty string "". */
  LAMBDA,
  /** A letter of Σ. Eg "a". */
  TOKEN,
  /** A concatenation of regular expressions. Eg "abc". */
  CONCATENATION,
  /** An alternation between regular expressions. Eg "a|b|c". */
  ALTERNATION,
  /** Kleene star operator. Eg "a*". */
  KLEENE;
}
