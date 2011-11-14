/*
 *  Copyright (C) 2011 vektor
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
package cz.cuni.mff.ksi.jinfer.base.utils;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author vektor
 */
public class IGGUtilsTest {

  @Test(expected = IllegalArgumentException.class)
  public void testIsSimpleConcatenationNull() {
    System.out.println("isSimpleConcatenationNull");
    IGGUtils.isSimpleConcatenation(null);
  }

  @Test
  public void testIsSimpleConcatenation() {
    System.out.println("isSimpleConcatenation");

    // lambda is not OK
    assertEquals(false, IGGUtils.isSimpleConcatenation(
            Regexp.<AbstractStructuralNode>getLambda()));

    final List<Regexp<AbstractStructuralNode>> abusiveChildren = new ArrayList<Regexp<AbstractStructuralNode>>();
    abusiveChildren.add(Regexp.<AbstractStructuralNode>getToken(
            TestUtils.getElement("e", Regexp.<AbstractStructuralNode>getLambda())));

    // alternation is not OK
    assertEquals(false, IGGUtils.isSimpleConcatenation(
            Regexp.getAlternation(abusiveChildren)));
    // permutation is not OK
    assertEquals(false, IGGUtils.isSimpleConcatenation(
            Regexp.getPermutation(abusiveChildren)));

    // empty concatenation is OK
    assertEquals(true, IGGUtils.isSimpleConcatenation(
            IGGUtilsTest.<AbstractStructuralNode>getEmptyConcat()));

    // repeated concatenation is not OK
    final Regexp<AbstractStructuralNode> concatRepeated = Regexp.getMutable();
    concatRepeated.setType(RegexpType.CONCATENATION);
    concatRepeated.setInterval(RegexpInterval.getBounded(5, 10));
    assertEquals(false, IGGUtils.isSimpleConcatenation(concatRepeated));

    final List<Regexp<AbstractStructuralNode>> okChildren = new ArrayList<Regexp<AbstractStructuralNode>>();
    okChildren.add(Regexp.<AbstractStructuralNode>getToken(
            TestUtils.getElement("e1", Regexp.<AbstractStructuralNode>getLambda())));
    okChildren.add(Regexp.<AbstractStructuralNode>getToken(
            TestUtils.getElement("e2", Regexp.<AbstractStructuralNode>getLambda())));
    okChildren.add(Regexp.<AbstractStructuralNode>getToken(
            TestUtils.getElement("e3", Regexp.<AbstractStructuralNode>getLambda())));

    // concatenation with some token children is OK
    assertEquals(true, IGGUtils.isSimpleConcatenation(
            Regexp.getConcatenation(okChildren)));

    final Regexp<AbstractStructuralNode> repeatedToken = Regexp.getMutable();
    repeatedToken.setType(RegexpType.TOKEN);
    repeatedToken.setInterval(RegexpInterval.getKleeneStar());
    repeatedToken.setContent(TestUtils.getElement("e", Regexp.<AbstractStructuralNode>getLambda()));

    okChildren.add(repeatedToken);

    // concatenation with a repeated token child is not OK
    assertEquals(false, IGGUtils.isSimpleConcatenation(
            Regexp.getConcatenation(okChildren)));

    // let's drop it for a second...
    okChildren.remove(repeatedToken);
    assertEquals(true, IGGUtils.isSimpleConcatenation(
            Regexp.getConcatenation(okChildren)));

    // now something horrible... concatenation containing itself!
    okChildren.add(Regexp.getConcatenation(okChildren));

    // this is not OK
    assertEquals(false, IGGUtils.isSimpleConcatenation(
            Regexp.getConcatenation(okChildren)));
  }

  private static <T> Regexp<T> getEmptyConcat() {
    final Regexp<T> ret = Regexp.getMutable();
    ret.setType(RegexpType.CONCATENATION);
    ret.setInterval(RegexpInterval.getOnce());
    ret.setImmutable();
    return ret;
  }
}
