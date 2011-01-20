/*
 *  Copyright (C) 2010 reseto
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

package cz.cuni.mff.ksi.jinfer.xsdimportsax.utils;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author reseto
 */
public class XSDNamedTypeTest {

  @Test
  public void testSeveral() {
    Element cat = Element.getMutable();
    cat.setName("cat");
    Element dog = Element.getMutable();
    dog.setName("dog");
    XSDNamedType nt = new XSDNamedType();
    nt.setContainer(cat);
    assertSame(cat, nt.getContainer());

    ArrayList<Element> rules = new ArrayList<Element>();
    rules.add(cat);
    nt.setRules(rules);
    assertTrue(rules.equals(nt.getRules()));
    assertFalse(nt.isAlreadyCopied());

    rules.add(dog);
    nt.setRules(rules);
    nt.setAlreadyCopied();

    assertTrue(rules.equals(nt.getRules()));
    assertTrue(nt.isAlreadyCopied());
  }

}