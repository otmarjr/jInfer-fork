/*
 *  Copyright (C) 2011 reseto
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

package cz.cuni.mff.ksi.jinfer.xsdimportdom;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author reseto
 */
@SuppressWarnings("PMD.SystemPrintln")
public class DOMHelperTest {

  @Test
  public void testTrimNull() {
    System.out.println("trim null");
    assertEquals(null, DOMHelper.trimNS(null));
  }

  @Test
  public void testTrimEmpty() {
    System.out.println("trim empty");
    assertEquals("", DOMHelper.trimNS(""));
  }

  @Test
  public void testTrimNormal() {
    System.out.println("trim normal");
    assertEquals("element", DOMHelper.trimNS("xs:element"));
  }

  @Test
  public void testTrimContainer() {
    System.out.println("trim container");
    assertEquals("", DOMHelper.trimNS("::container::order::"));
  }

  @Test
  public void testTrimBad() {
    System.out.println("trim bad");
    assertEquals("x", DOMHelper.trimNS("xsd:element:x"));
  }

  @Test
  public void testSetLambda() {
    final Element el = Element.getMutable();
    el.getSubnodes().setType(RegexpType.ALTERNATION);
    DOMHelper.setLambda(el);
    assertEquals(RegexpType.LAMBDA, el.getSubnodes().getType());
  }
}