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
package cz.cuni.mff.ksi.jinfer.trivialdtd.utils;

import cz.cuni.mff.ksi.jinfer.base.objects.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.SimpleData;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author vektor
 */
@SuppressWarnings("PMD.SystemPrintln")
public class DTDUtilsTest {

  @Test
  public void testOmitAttributesEmpty() {
    System.out.println("omitAttributesEmpty");
    final List<Regexp<AbstractNode>> list = Collections.emptyList();
    final List<Regexp<AbstractNode>> expResult = Collections.emptyList();
    final List<Regexp<AbstractNode>> result = DTDUtils.omitAttributes(list);
    assertEquals(expResult, result);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testOmitAttributesNoneOfOne() {
    System.out.println("testOmitAttributesNoneOfOne");
    final Regexp<AbstractNode> element = Regexp.getToken((AbstractNode) new Element(null, "lala", null, null));
    final List<Regexp<AbstractNode>> list = Arrays.asList(element);
    final List<Regexp<AbstractNode>> expResult = new ArrayList<Regexp<AbstractNode>>(Arrays.asList(element));
    final List<Regexp<AbstractNode>> result = DTDUtils.omitAttributes(list);
    assertEquals(expResult, result);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testOmitAttributesOneOfOne() {
    System.out.println("testOmitAttributesOneOfOne");
    final Regexp<AbstractNode> att = Regexp.getToken((AbstractNode) new Attribute(null, "lala", null, null, null));
    final List<Regexp<AbstractNode>> list = Arrays.asList(att);
    final List<Regexp<AbstractNode>> result = DTDUtils.omitAttributes(list);
    if (!result.isEmpty()) {
      Assert.fail();
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testOmitAttributesOneOfMore1() {
    System.out.println("testOmitAttributesOneOfMore1");
    final Regexp<AbstractNode> element = Regexp.getToken((AbstractNode) new Element(null, "lala", null, null));
    final Regexp<AbstractNode> att = Regexp.getToken((AbstractNode) new Attribute(null, "lala", null, null, null));
    final List<Regexp<AbstractNode>> list = Arrays.asList(element, element, att, element);
    final List<Regexp<AbstractNode>> expResult = new ArrayList<Regexp<AbstractNode>>(Arrays.asList(element, element, element));
    final List<Regexp<AbstractNode>> result = DTDUtils.omitAttributes(list);
    assertEquals(expResult, result);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testOmitAttributesOneOfMore2() {
    System.out.println("testOmitAttributesOneOfMore2");
    final Regexp<AbstractNode> element = Regexp.getToken((AbstractNode) new Element(null, "lala", null, null));
    final Regexp<AbstractNode> att = Regexp.getToken((AbstractNode) new Attribute(null, "lala", null, null, null));
    final List<Regexp<AbstractNode>> list = Arrays.asList(element, element, att, element, att);
    final List<Regexp<AbstractNode>> expResult = new ArrayList<Regexp<AbstractNode>>(Arrays.asList(element, element, element));
    final List<Regexp<AbstractNode>> result = DTDUtils.omitAttributes(list);
    assertEquals(expResult, result);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testOmitAttributesOneOfMore3() {
    System.out.println("testOmitAttributesOneOfMore3");
    final Regexp<AbstractNode> element = Regexp.getToken((AbstractNode) new Element(null, "lala", null, null));
    final Regexp<AbstractNode> att = Regexp.getToken((AbstractNode) new Attribute(null, "lala", null, null, null));
    final List<Regexp<AbstractNode>> list = Arrays.asList(att, element, element, element);
    final List<Regexp<AbstractNode>> expResult = new ArrayList<Regexp<AbstractNode>>(Arrays.asList(element, element, element));
    final List<Regexp<AbstractNode>> result = DTDUtils.omitAttributes(list);
    assertEquals(expResult, result);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testOmitAttributesAllOfMore() {
    System.out.println("testOmitAttributesAllOfMore");
    final Regexp<AbstractNode> att = Regexp.getToken((AbstractNode) new Attribute(null, "lala", null, null, null));
    final List<Regexp<AbstractNode>> list = Arrays.asList(att, att, att);
    final List<Regexp<AbstractNode>> expResult = Collections.emptyList();
    final List<Regexp<AbstractNode>> result = DTDUtils.omitAttributes(list);
    assertEquals(expResult, result);
  }

  @Test(expected = NullPointerException.class)
  public void testContainsPCDATAEmpty() {
    System.out.println("containsPCDATAEmpty");
    DTDUtils.containsPCDATA(null);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testContainsPCDATANoneOfOne() {
    System.out.println("testContainsPCDATANoneOfOne");
    final Regexp<AbstractNode> att = Regexp.getToken((AbstractNode) new Attribute(null, "lala", null, null, null));
    final List<Regexp<AbstractNode>> list = Arrays.asList(att);
    final boolean result = DTDUtils.containsPCDATA(list);
    assertEquals(result, false);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testContainsPCDATAOneOfOne() {
    System.out.println("testContainsPCDATAOneOfOne");
    final Regexp<AbstractNode> sd = Regexp.getToken((AbstractNode) new SimpleData(null, "lala", null, null, null));
    final List<Regexp<AbstractNode>> list = Arrays.asList(sd);
    final boolean result = DTDUtils.containsPCDATA(list);
    assertEquals(result, true);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testContainsPCDATAOneOfMore() {
    System.out.println("testContainsPCDATAOneOfMore");
    final Regexp<AbstractNode> sd = Regexp.getToken((AbstractNode) new SimpleData(null, "lala", null, null, null));
    final Regexp<AbstractNode> att = Regexp.getToken((AbstractNode) new Attribute(null, "lala", null, null, null));
    final List<Regexp<AbstractNode>> list = Arrays.asList(att, sd, att);
    final boolean result = DTDUtils.containsPCDATA(list);
    assertEquals(result, true);
  }
}
