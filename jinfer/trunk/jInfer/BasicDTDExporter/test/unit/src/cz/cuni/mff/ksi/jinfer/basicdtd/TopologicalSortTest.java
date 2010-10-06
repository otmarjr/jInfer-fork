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
package cz.cuni.mff.ksi.jinfer.basicdtd;

import cz.cuni.mff.ksi.jinfer.base.objects.Attribute;
import cz.cuni.mff.ksi.jinfer.base.utils.TopologicalSort;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit test of Topological sorting algorithm.
 *
 * @author vektor
 */
public class TopologicalSortTest {

  @Test(expected=NullPointerException.class)
  public void testNull() {
    final TopologicalSort s = new TopologicalSort(null);
    s.sort();
  }

  @Test
  public void testEmpty() {
    final TopologicalSort s = new TopologicalSort(Collections.<Element>emptyList());
    assertEquals(Collections.<Element>emptyList(), s.sort());
  }

  @Test
  public void testOne() {
    final Element e = new Element(null, "test", null,
            Regexp.<AbstractStructuralNode>getMutable(), new ArrayList<Attribute>(0));
    e.getSubnodes().setType(RegexpType.CONCATENATION);
    e.getSubnodes().setInterval(RegexpInterval.getOnce());
    final TopologicalSort s = new TopologicalSort(Arrays.asList(e));
    final List<Element> elements = s.sort();
    assertEquals(1, elements.size());
    assertEquals(e, elements.get(0));
  }

  @Test
  public void testSort() {
    final Element e1 = new Element(null, "test1", null,
            Regexp.<AbstractStructuralNode>getMutable(), new ArrayList<Attribute>(0));
    final Element e2 = new Element(null, "test2", null,
            Regexp.<AbstractStructuralNode>getMutable(), new ArrayList<Attribute>(0));
    final Element e3 = new Element(null, "test3", null,
            Regexp.<AbstractStructuralNode>getMutable(), new ArrayList<Attribute>(0));
    final Element e4 = new Element(null, "test4", null,
            Regexp.<AbstractStructuralNode>getMutable(), new ArrayList<Attribute>(0));
    final Element e5 = new Element(null, "test5", null,
            Regexp.<AbstractStructuralNode>getMutable(), new ArrayList<Attribute>(0));
    final Element e6 = new Element(null, "test6", null,
            Regexp.<AbstractStructuralNode>getMutable(), new ArrayList<Attribute>(0));
    final Element e7 = new Element(null, "test7", null,
            Regexp.<AbstractStructuralNode>getMutable(), new ArrayList<Attribute>(0));
    final Element e8 = new Element(null, "test8", null,
            Regexp.<AbstractStructuralNode>getMutable(), new ArrayList<Attribute>(0));
    e1.getSubnodes().setType(RegexpType.CONCATENATION);
    e1.getSubnodes().setInterval(RegexpInterval.getOnce());
    e2.getSubnodes().setType(RegexpType.CONCATENATION);
    e2.getSubnodes().setInterval(RegexpInterval.getOnce());
    e3.getSubnodes().setType(RegexpType.CONCATENATION);
    e3.getSubnodes().setInterval(RegexpInterval.getOnce());
    e4.getSubnodes().setType(RegexpType.CONCATENATION);
    e4.getSubnodes().setInterval(RegexpInterval.getOnce());
    e5.getSubnodes().setType(RegexpType.CONCATENATION);
    e5.getSubnodes().setInterval(RegexpInterval.getOnce());
    e6.getSubnodes().setType(RegexpType.CONCATENATION);
    e6.getSubnodes().setInterval(RegexpInterval.getOnce());
    e7.getSubnodes().setType(RegexpType.CONCATENATION);
    e7.getSubnodes().setInterval(RegexpInterval.getOnce());
    e8.getSubnodes().setType(RegexpType.CONCATENATION);
    e8.getSubnodes().setInterval(RegexpInterval.getOnce());

    e1.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(e2));
    e1.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(e3));
    e2.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(e4));
    e2.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(e5));
    e3.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(e4));
    e3.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(e5));
    e4.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(e6));
    e5.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(e6));
    e7.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(e8));
    
    final List<Element> elements = Arrays.asList(e1, e2, e3, e4, e5, e6, e7, e8);

    Collections.shuffle(elements);

    final TopologicalSort s = new TopologicalSort(elements);
    final List<Element> toposorted = s.sort();

    assertEquals(8, toposorted.size());

    assertTrue(toposorted.indexOf(e1) > toposorted.indexOf(e2));
    assertTrue(toposorted.indexOf(e1) > toposorted.indexOf(e3));
    assertTrue(toposorted.indexOf(e1) > toposorted.indexOf(e4));
    assertTrue(toposorted.indexOf(e1) > toposorted.indexOf(e6));
    assertTrue(toposorted.indexOf(e3) > toposorted.indexOf(e6));
    assertTrue(toposorted.indexOf(e7) > toposorted.indexOf(e8));
  }

  @Test
  public void testMore() {
    final Element e1 = new Element(null, "test1", null,
            Regexp.<AbstractStructuralNode>getMutable(), new ArrayList<Attribute>(0));
    final Element e2 = new Element(null, "test2", null,
            Regexp.<AbstractStructuralNode>getMutable(), new ArrayList<Attribute>(0));
    final Element e3 = new Element(null, "test3", null,
            Regexp.<AbstractStructuralNode>getMutable(), new ArrayList<Attribute>(0));
    final Element e4 = new Element(null, "test4", null,
            Regexp.<AbstractStructuralNode>getMutable(), new ArrayList<Attribute>(0));

    e1.getSubnodes().setType(RegexpType.CONCATENATION);
    e1.getSubnodes().setInterval(RegexpInterval.getOnce());
    e2.getSubnodes().setType(RegexpType.CONCATENATION);
    e2.getSubnodes().setInterval(RegexpInterval.getOnce());
    e3.getSubnodes().setType(RegexpType.CONCATENATION);
    e3.getSubnodes().setInterval(RegexpInterval.getOnce());
    e4.getSubnodes().setType(RegexpType.CONCATENATION);
    e4.getSubnodes().setInterval(RegexpInterval.getOnce());

    e1.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(e2));
    e1.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(e3));
    e3.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(e4));

    final List<Element> elements = Arrays.asList(e1, e2);

    Collections.shuffle(elements);

    final TopologicalSort s = new TopologicalSort(elements);
    final List<Element> toposorted = s.sort();

    assertEquals(2, toposorted.size());
    assertTrue(toposorted.indexOf(e1) > toposorted.indexOf(e2));
    assertTrue(toposorted.indexOf(e1) > toposorted.indexOf(e3));
    assertTrue(toposorted.indexOf(e2) > toposorted.indexOf(e4));
  }
}
