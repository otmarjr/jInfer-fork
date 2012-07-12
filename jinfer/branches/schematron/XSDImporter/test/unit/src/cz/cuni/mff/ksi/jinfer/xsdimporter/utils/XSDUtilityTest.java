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

package cz.cuni.mff.ksi.jinfer.xsdimporter.utils;

import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.utils.EqualityUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.IGGUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author reseto
 */
@SuppressWarnings("PMD.SystemPrintln")
public class XSDUtilityTest {


  @Test
  public void testTrimNull() {
    System.out.println("trim null");
    assertEquals(null, XSDUtility.trimNS(null));
  }

  @Test
  public void testTrimEmpty() {
    System.out.println("trim empty");
    assertEquals("", XSDUtility.trimNS(""));
  }

  @Test
  public void testTrimNormal() {
    System.out.println("trim normal");
    assertEquals("element", XSDUtility.trimNS("xs:element"));
  }

  @Test
  public void testTrimContainer() {
    System.out.println("trim container");
    assertEquals("", XSDUtility.trimNS("::container::order::"));
  }

  @Test
  public void testTrimBad() {
    System.out.println("trim bad");
    assertEquals("x", XSDUtility.trimNS("xsd:element:x"));
  }

  @Test
  public void testSetLambda() {
    System.out.println("test set lambda");
    final Element el = Element.getMutable();
    el.getSubnodes().setType(RegexpType.ALTERNATION);
    XSDUtility.setLambda(el);
    assertEquals(RegexpType.LAMBDA, el.getSubnodes().getType());
    assertFalse(el.isMutable());
  }

  @Test
  public void testPrepareMetadata1() {
    System.out.println("test prepare metadata 1");
    final Element el = Element.getMutable();
    el.getSubnodes().setType(RegexpType.CONCATENATION);
    final RegexpInterval originalInterval = RegexpInterval.getOnce();
    el.getMetadata().putAll(XSDUtility.prepareMetadata(originalInterval));
    assertTrue(el.getMetadata().containsKey(IGGUtils.FROM_SCHEMA));
    assertTrue(el.getMetadata().containsKey(XSDAttribute.MINOCCURS.getMetadataName()));
    assertTrue(el.getMetadata().containsKey(XSDAttribute.MAXOCCURS.getMetadataName()));
    final String sMin = (String) el.getMetadata().get(XSDAttribute.MINOCCURS.getMetadataName());
    final String sMax = (String) el.getMetadata().get(XSDAttribute.MAXOCCURS.getMetadataName());
    RegexpInterval retInterval;
    if (!sMax.equals(XSDUtility.UNBOUNDED)) {
      retInterval = RegexpInterval.getBounded(Integer.parseInt(sMin), Integer.parseInt(sMax));
    } else {
      retInterval = RegexpInterval.getUnbounded(Integer.parseInt(sMin));
    }
    assertTrue(EqualityUtils.sameIntervals(originalInterval, retInterval));
  }
}