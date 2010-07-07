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

import java.util.ArrayList;
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
    assertEquals(expected, tokens);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testBranchIllegal1() {
    final Regexp<Integer> t = Regexp.getToken(Integer.valueOf(0));
    t.branch(0);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testBranchIllegal2() {
    final Regexp<Integer> t = new Regexp<Integer>(null, null, RegexpType.ALTERNATION);
    t.branch(0);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testBranchIllegal3() {
    final Regexp<Integer> t = new Regexp<Integer>(null, null, RegexpType.KLEENE);
    t.branch(0);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testBranchEmpty() {
    final Regexp<Integer> t = new Regexp<Integer>(null, new ArrayList<Regexp<Integer>>(), RegexpType.CONCATENATION);
    t.branch(0);
  }

  /**
   * Start: (0) Expected: (((0) | ))
   */
  @Test
  public void testBranchLengthOne() {
    final List<Regexp<Integer>> children = new ArrayList<Regexp<Integer>>();
    children.add(Regexp.getToken(Integer.valueOf(0)));
    final Regexp<Integer> t = new Regexp<Integer>(null, children, RegexpType.CONCATENATION);
    System.out.print(t.toString() + " -> ");
    t.branch(0);
    System.out.println(t.toString());

    assertTrue(t.getChildren().size() == 1);
    final Regexp<Integer> alt = t.getChild(0);
    assertTrue(alt.isAlternation());
    assertTrue(alt.getChildren().size() == 1);
    assertTrue(alt.getChild(0).isConcatenation());
    assertTrue(alt.getChild(0).getChildren().size() == 1);
    assertTrue(alt.getChild(0).getChild(0).isToken());
  }

  /**
   * Start: (0 1 2) Expected: (((0 1 2) | ))
   */
  @Test
  public void testBranchLengthThree0() {
    final List<Regexp<Integer>> children = new ArrayList<Regexp<Integer>>();
    children.add(Regexp.getToken(Integer.valueOf(0)));
    children.add(Regexp.getToken(Integer.valueOf(1)));
    children.add(Regexp.getToken(Integer.valueOf(2)));
    final Regexp<Integer> t = new Regexp<Integer>(null, children, RegexpType.CONCATENATION);
    System.out.print(t.toString() + " -> ");
    t.branch(0);
    System.out.println(t.toString());

    assertTrue(t.getChildren().size() == 1);
    final Regexp<Integer> alt = t.getChild(0);
    assertTrue(alt.isAlternation());
    assertTrue(alt.getChildren().size() == 1);
    final Regexp<Integer> concat = alt.getChild(0);
    assertTrue(concat.isConcatenation());
    assertTrue(concat.getChildren().size() == 3);

    for (int i = 0; i < 3; i++) {
      assertTrue(concat.getChild(i).getContent().equals(Integer.valueOf(i)));
    }
  }

  /**
   * Start: (0 1 2) Expected: (0 ((1 2) | ))
   */
  @Test
  public void testBranchLengthThree1() {
    final List<Regexp<Integer>> children = new ArrayList<Regexp<Integer>>();
    children.add(Regexp.getToken(Integer.valueOf(0)));
    children.add(Regexp.getToken(Integer.valueOf(1)));
    children.add(Regexp.getToken(Integer.valueOf(2)));
    final Regexp<Integer> t = new Regexp<Integer>(null, children, RegexpType.CONCATENATION);
    System.out.print(t.toString() + " -> ");
    t.branch(1);
    System.out.println(t.toString());

    assertTrue(t.getChildren().size() == 2);
    final Regexp<Integer> alt = t.getChild(1);
    assertTrue(alt.isAlternation());
    assertTrue(alt.getChildren().size() == 1);
    final Regexp<Integer> concat = alt.getChild(0);
    assertTrue(concat.isConcatenation());
    assertTrue(concat.getChildren().size() == 2);

    for (int i = 1; i < 3; i++) {
      assertTrue(concat.getChild(i - 1).getContent().equals(Integer.valueOf(i)));
    }
  }

  /**
   * Start: (0 1 2) Expected: (0 1 ((2) | ))
   */
  @Test
  public void testBranchLengthThree2() {
    final List<Regexp<Integer>> children = new ArrayList<Regexp<Integer>>();
    children.add(Regexp.getToken(Integer.valueOf(0)));
    children.add(Regexp.getToken(Integer.valueOf(1)));
    children.add(Regexp.getToken(Integer.valueOf(2)));
    final Regexp<Integer> t = new Regexp<Integer>(null, children, RegexpType.CONCATENATION);
    System.out.print(t.toString() + " -> ");
    t.branch(2);
    System.out.println(t.toString());

    assertTrue(t.getChildren().size() == 3);
    final Regexp<Integer> alt = t.getChild(2);
    assertTrue(alt.isAlternation());
    assertTrue(alt.getChildren().size() == 1);
    final Regexp<Integer> concat = alt.getChild(0);
    assertTrue(concat.isConcatenation());
    assertTrue(concat.getChildren().size() == 1);

    assertTrue(concat.getChild(0).getContent().equals(Integer.valueOf(2)));
  }

  /**
   * Start: (0 ((10 11) | ) 2) Expected: (0 ((10 11) | ) 2)
   */
  @Test
  public void testBranchAlready() {
    final List<Regexp<Integer>> children = new ArrayList<Regexp<Integer>>();
    children.add(Regexp.getToken(Integer.valueOf(0)));
    final List<Regexp<Integer>> altC = new ArrayList<Regexp<Integer>>();
    altC.add(Regexp.getToken(Integer.valueOf(10)));
    altC.add(Regexp.getToken(Integer.valueOf(11)));
    children.add(new Regexp<Integer>(null, altC, RegexpType.ALTERNATION));
    children.add(Regexp.getToken(Integer.valueOf(2)));
    final Regexp<Integer> t = new Regexp<Integer>(null, children, RegexpType.CONCATENATION);
    System.out.print(t.toString() + " -> ");
    t.branch(1);
    System.out.println(t.toString());

    assertTrue(t.getChildren().size() == 3);
    final Regexp<Integer> alt = t.getChild(1);
    assertTrue(RegexpType.ALTERNATION.equals(alt.getType()));
    assertTrue(alt.getChildren().size() == 2);
  }

}
