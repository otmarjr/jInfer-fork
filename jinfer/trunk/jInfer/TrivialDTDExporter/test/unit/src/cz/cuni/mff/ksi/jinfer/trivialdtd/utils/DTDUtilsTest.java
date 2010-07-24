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
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
import cz.cuni.mff.ksi.jinfer.base.objects.SimpleData;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
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
@SuppressWarnings({"PMD.SystemPrintln", "unchecked"})
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
  public void testOmitAttributesNoneOfOne() {
    System.out.println("testOmitAttributesNoneOfOne");
    final Regexp<AbstractNode> element = Regexp.getToken((AbstractNode) new Element(null, "lala", null, null));
    final List<Regexp<AbstractNode>> list = Arrays.asList(element);
    final List<Regexp<AbstractNode>> expResult = new ArrayList<Regexp<AbstractNode>>(Arrays.asList(element));
    final List<Regexp<AbstractNode>> result = DTDUtils.omitAttributes(list);
    assertEquals(expResult, result);
  }

  @Test
  public void testOmitAttributesOneOfOne() {
    System.out.println("testOmitAttributesOneOfOne");
    final Regexp<AbstractNode> att = Regexp.getToken((AbstractNode) new Attribute(null, "lala", null, null, null));
    final List<Regexp<AbstractNode>> list = Arrays.asList(att);
    final List<Regexp<AbstractNode>> result = DTDUtils.omitAttributes(list);
    if (result == null || !BaseUtils.isEmpty(result)) {
      Assert.fail();
    }
  }

  @Test
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
  public void testContainsPCDATANoneOfOne() {
    System.out.println("testContainsPCDATANoneOfOne");
    final Regexp<AbstractNode> att = Regexp.getToken((AbstractNode) new Attribute(null, "lala", null, null, null));
    final List<Regexp<AbstractNode>> list = Arrays.asList(att);
    final boolean result = DTDUtils.containsPCDATA(list);
    assertEquals(false, result);
  }

  @Test
  public void testContainsPCDATAOneOfOne() {
    System.out.println("testContainsPCDATAOneOfOne");
    final Regexp<AbstractNode> sd = Regexp.getToken((AbstractNode) new SimpleData(null, "lala", null, null, null));
    final List<Regexp<AbstractNode>> list = Arrays.asList(sd);
    final boolean result = DTDUtils.containsPCDATA(list);
    assertEquals(true, result);
  }

  @Test
  public void testContainsPCDATAOneOfMore() {
    System.out.println("testContainsPCDATAOneOfMore");
    final Regexp<AbstractNode> sd = Regexp.getToken((AbstractNode) new SimpleData(null, "lala", null, null, null));
    final Regexp<AbstractNode> att = Regexp.getToken((AbstractNode) new Attribute(null, "lala", null, null, null));
    final List<Regexp<AbstractNode>> list = Arrays.asList(att, sd, att);
    final boolean result = DTDUtils.containsPCDATA(list);
    assertEquals(true, result);
  }

  @Test(expected = NullPointerException.class)
  public void testUniquePCDATANull() {
    System.out.println("testUniquePCDATANull");
    DTDUtils.uniquePCDATA(null);
  }

  @Test
  public void testUniquePCDATAEmpty() {
    System.out.println("testUniquePCDATAEmpty");
    final List<AbstractNode> ret = DTDUtils.uniquePCDATA(new ArrayList<AbstractNode>());
    assertEquals(0, ret.size());
  }

  @Test
  public void testUniquePCDATAOne() {
    System.out.println("testUniquePCDATAOne");
    final List<AbstractNode> l = new ArrayList<AbstractNode>();
    l.add(new SimpleData(null, null, null, null, null));
    final List<AbstractNode> ret = DTDUtils.uniquePCDATA(l);
    assertEquals(1, ret.size());
  }

  @Test
  public void testUniquePCDATAOnePlusElement() {
    System.out.println("testUniquePCDATAOnePlusElement");
    final List<AbstractNode> l = new ArrayList<AbstractNode>();
    l.add(new SimpleData(null, null, null, null, null));
    l.add(new Element(null, null, null, null));
    final List<AbstractNode> ret = DTDUtils.uniquePCDATA(l);
    assertEquals(2, ret.size());
  }

  @Test
  public void testUniquePCDATATwo() {
    System.out.println("testUniquePCDATATwo");
    final List<AbstractNode> l = new ArrayList<AbstractNode>();
    l.add(new SimpleData(null, null, null, null, null));
    l.add(new SimpleData(null, null, null, null, null));
    final List<AbstractNode> ret = DTDUtils.uniquePCDATA(l);
    assertEquals(1, ret.size());
  }

  @Test
  public void testUniquePCDATATwoPlusElement() {
    System.out.println("testUniquePCDATATwoPlusElement");
    final List<AbstractNode> l = new ArrayList<AbstractNode>();
    l.add(new SimpleData(null, null, null, null, null));
    l.add(new SimpleData(null, null, null, null, null));
    l.add(new Element(null, null, null, null));
    final List<AbstractNode> ret = DTDUtils.uniquePCDATA(l);
    assertEquals(2, ret.size());
  }

  @Test
  public void testUniquePCDATAComplex() {
    System.out.println("testUniquePCDATAComplex");
    final List<AbstractNode> l = new ArrayList<AbstractNode>();
    l.add(new SimpleData(null, null, null, null, null));
    l.add(new Element(null, null, null, null));
    l.add(new SimpleData(null, null, null, null, null));
    l.add(new Element(null, null, null, null));
    l.add(new SimpleData(null, null, null, null, null));
    l.add(new Element(null, null, null, null));
    l.add(new Element(null, null, null, null));
    final List<AbstractNode> ret = DTDUtils.uniquePCDATA(l);
    assertEquals(5, ret.size());
    assertEquals(NodeType.SIMPLE_DATA, ret.get(0).getType());
    for (int i = 1; i < ret.size(); i++) {
      assertEquals(NodeType.ELEMENT, ret.get(i).getType());
    }
  }
}
