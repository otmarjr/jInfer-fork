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

import cz.cuni.mff.ksi.jinfer.base.utils.EqualityUtils;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
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
  public void testRepairNull() {
    System.out.println("repair concat null");
    final Element res = null;
    DOMHelper.repairConcatInterval(res);
    assertEquals(null, res);
  }

  @Test
  public void testRepairNormal() {
    System.out.println("repair normal case");
    final Element res = Element.getMutable();
    res.getSubnodes().setType(RegexpType.CONCATENATION);
    res.getSubnodes().setInterval(null);
    DOMHelper.repairConcatInterval(res);
    final RegexpInterval i1 = RegexpInterval.getOnce();
    final RegexpInterval i2 = res.getSubnodes().getInterval();
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameIntervals(i1, i2);
    assertEquals(expResult, result);
  }

  @Test
  public void testRepairNothing() {
    System.out.println("nothing to repair");
    final Element res = Element.getMutable();
    res.getSubnodes().setType(RegexpType.CONCATENATION);
    res.getSubnodes().setInterval(RegexpInterval.getOptional());
    DOMHelper.repairConcatInterval(res);
    final RegexpInterval i1 = RegexpInterval.getOptional();
    final RegexpInterval i2 = res.getSubnodes().getInterval();
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameIntervals(i1, i2);
    assertEquals(expResult, result);
  }

  @Test
  public void testRepairArbitraty() {
    System.out.println("repair arbitraty");
    final int min = 5;
    final int max = 6;
    final Element res = Element.getMutable();
    res.getSubnodes().setType(RegexpType.CONCATENATION);
    res.getSubnodes().setInterval(RegexpInterval.getBounded(min, max));
    DOMHelper.repairConcatInterval(res);
    RegexpInterval i1 = RegexpInterval.getOptional();
    final RegexpInterval i2 = res.getSubnodes().getInterval();
    boolean expResult = false;
    boolean result = EqualityUtils.sameIntervals(i1, i2);
    assertEquals(expResult, result);

    i1 = RegexpInterval.getBounded(min, max);
    expResult = true;
    result = EqualityUtils.sameIntervals(i1, i2);
    assertEquals(expResult, result);
  }

  @Test
  public void testSetLambda() {
    final Element el = Element.getMutable();
    el.getSubnodes().setType(RegexpType.ALTERNATION);
    DOMHelper.setLambda(el);
    assertEquals(RegexpType.LAMBDA, el.getSubnodes().getType());
  }
}