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

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit test of Regexp class.
 * 
 * @author vektor
 */
public class RegexpTest {

  public RegexpTest() {
  }

  @Test
  public void testGetTokensSimple() {
    final Regexp<Integer> r = Regexp.getToken(Integer.valueOf(0));
    assertTrue(r.getTokens().size() == 1);
    assertTrue(r.getTokens().get(0).equals(Integer.valueOf(0)));
  }

  /**
   * Will create a regexp representing "a b (b | c | (d e))* d e" and check
   * whether its tokens are "a b b c d e d e".
   */
  @Test
  public void testGetTokens() {
    @SuppressWarnings("unchecked")
    final List<Regexp<Character>> l4 = Arrays.asList(
            Regexp.<Character>getToken('d'),
            Regexp.<Character>getToken('e')
            );
    final Regexp<Character> concat = new Regexp<Character>(null, l4, RegexpType.CONCATENATION);
    @SuppressWarnings("unchecked")
    final List<Regexp<Character>> l3 = Arrays.asList(
            Regexp.<Character>getToken('b'),
            Regexp.<Character>getToken('c'),
            concat
            );
    @SuppressWarnings("unchecked")
    final List<Regexp<Character>> l2 = Arrays.asList(
            new Regexp<Character>(null, l3, RegexpType.ALTERNATION)
            );
    final Regexp<Character> kleene = new Regexp<Character>(null, l2, RegexpType.KLEENE);
    @SuppressWarnings("unchecked")
    final List<Regexp<Character>> l1 = Arrays.asList(
            Regexp.<Character>getToken('a'),
            Regexp.<Character>getToken('b'),
            kleene,
            Regexp.<Character>getToken('d'),
            Regexp.<Character>getToken('e')
            );
    final Regexp<Character> test = new Regexp<Character>(null, l1, RegexpType.CONCATENATION);
    final List<Character> tokens = test.getTokens();
    final List<Character> expected = Arrays.asList('a', 'b', 'b', 'c', 'd', 'e', 'd', 'e');
    assertEquals(tokens, expected);
  }

}
