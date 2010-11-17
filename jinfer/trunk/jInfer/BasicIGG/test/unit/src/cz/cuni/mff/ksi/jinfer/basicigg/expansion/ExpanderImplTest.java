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
package cz.cuni.mff.ksi.jinfer.basicigg.expansion;

import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.TestUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests of the expander.
 * Basically, we want to test:
 * <ul>
 *   <li>Elements with subnodes that are already simple regexp must not be expanded.</li>
 *   <li>Everything else must be expanded.</li>
 * </ul>
 *
 * @author vektor
 */
public class ExpanderImplTest {

  @Test(expected = IllegalArgumentException.class)
  public void testExpandNull() {
    System.out.println("expandNull");
    new ExpanderImpl().expand(null);
  }

  @Test
  public void testExpandNoOp() {
    System.out.println("expandNoOp");
    final List<Element> grammar = new ArrayList<Element>();

    final List<Regexp<AbstractStructuralNode>> okChildren = new ArrayList<Regexp<AbstractStructuralNode>>();
    okChildren.add(Regexp.<AbstractStructuralNode>getToken(
            TestUtils.getElement("e1", Regexp.<AbstractStructuralNode>getLambda())));
    okChildren.add(Regexp.<AbstractStructuralNode>getToken(
            TestUtils.getElement("e2", Regexp.<AbstractStructuralNode>getLambda())));
    okChildren.add(Regexp.<AbstractStructuralNode>getToken(
            TestUtils.getElement("e3", Regexp.<AbstractStructuralNode>getLambda())));

    final Element e1 = TestUtils.getElement("e1", Regexp.getConcatenation(okChildren));
    final Element e2 = TestUtils.getElement("e2", Regexp.getConcatenation(okChildren));

    assertNotSame(e1, e2);

    grammar.add(e1);

    final List<Element> ret1 = new ExpanderImpl().expand(grammar);
    assertEquals(e1, ret1.get(0));

    grammar.add(e2);

    final List<Element> ret2 = new ExpanderImpl().expand(grammar);
    assertEquals(e1, ret2.get(0));
    assertEquals(e2, ret2.get(1));
  }

  @Test
  public void testExpand() {
    System.out.println("expand");
//    List<Element> grammar = null;
//    ExpanderImpl instance = new ExpanderImpl();
//    List expResult = null;
//    List result = instance.expand(grammar);
//    assertEquals(expResult, result);
  }
}
