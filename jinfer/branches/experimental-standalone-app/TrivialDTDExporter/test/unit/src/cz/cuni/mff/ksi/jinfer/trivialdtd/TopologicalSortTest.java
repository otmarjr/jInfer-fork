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
package cz.cuni.mff.ksi.jinfer.trivialdtd;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
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

  public TopologicalSortTest() {
  }

  @Test(expected=NullPointerException.class)
  public void testNull() {
    final TopologicalSort s = new TopologicalSort(null);
    s.sort();
  }

  @Test
  public void testEmpty() {
    final TopologicalSort s = new TopologicalSort(Collections.<Element>emptyList());
    assertEquals(s.sort(), Collections.<Element>emptyList());
  }

  @Test
  public void testOne() {
    final Element e = new Element(null, "test", null,
            new Regexp<AbstractNode>(null, new ArrayList<Regexp<AbstractNode>>(), RegexpType.CONCATENATION));
    final TopologicalSort s = new TopologicalSort(Arrays.asList(e));
    final List<Element> elements = s.sort();
    assertTrue(elements.size() == 1);
    assertEquals(elements.get(0), e);
  }

  @Test
  public void testSort() {
    final Element e1 = new Element(null, "test1", null,
            new Regexp<AbstractNode>(null, new ArrayList<Regexp<AbstractNode>>(), RegexpType.CONCATENATION));
    final Element e2 = new Element(null, "test2", null,
            new Regexp<AbstractNode>(null, new ArrayList<Regexp<AbstractNode>>(), RegexpType.CONCATENATION));
    final Element e3 = new Element(null, "test3", null,
            new Regexp<AbstractNode>(null, new ArrayList<Regexp<AbstractNode>>(), RegexpType.CONCATENATION));
    final Element e4 = new Element(null, "test4", null,
            new Regexp<AbstractNode>(null, new ArrayList<Regexp<AbstractNode>>(), RegexpType.CONCATENATION));
    final Element e5 = new Element(null, "test5", null,
            new Regexp<AbstractNode>(null, new ArrayList<Regexp<AbstractNode>>(), RegexpType.CONCATENATION));
    final Element e6 = new Element(null, "test6", null,
            new Regexp<AbstractNode>(null, new ArrayList<Regexp<AbstractNode>>(), RegexpType.CONCATENATION));
    final Element e7 = new Element(null, "test7", null,
            new Regexp<AbstractNode>(null, new ArrayList<Regexp<AbstractNode>>(), RegexpType.CONCATENATION));
    final Element e8 = new Element(null, "test8", null,
            new Regexp<AbstractNode>(null, new ArrayList<Regexp<AbstractNode>>(), RegexpType.CONCATENATION));

    e1.getSubnodes().getChildren().add(Regexp.<AbstractNode>getToken(e2));
    e1.getSubnodes().getChildren().add(Regexp.<AbstractNode>getToken(e3));
    e2.getSubnodes().getChildren().add(Regexp.<AbstractNode>getToken(e4));
    e2.getSubnodes().getChildren().add(Regexp.<AbstractNode>getToken(e5));
    e3.getSubnodes().getChildren().add(Regexp.<AbstractNode>getToken(e4));
    e3.getSubnodes().getChildren().add(Regexp.<AbstractNode>getToken(e5));
    e4.getSubnodes().getChildren().add(Regexp.<AbstractNode>getToken(e6));
    e5.getSubnodes().getChildren().add(Regexp.<AbstractNode>getToken(e6));
    e7.getSubnodes().getChildren().add(Regexp.<AbstractNode>getToken(e8));
    
    final List<Element> elements = Arrays.asList(e1, e2, e3, e4, e5, e6, e7, e8);

    Collections.shuffle(elements);

    final TopologicalSort s = new TopologicalSort(elements);
    final List<Element> toposorted = s.sort();

    assertTrue(toposorted.size() == 8);
    
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
            new Regexp<AbstractNode>(null, new ArrayList<Regexp<AbstractNode>>(), RegexpType.CONCATENATION));
    final Element e2 = new Element(null, "test2", null,
            new Regexp<AbstractNode>(null, new ArrayList<Regexp<AbstractNode>>(), RegexpType.CONCATENATION));
    final Element e3 = new Element(null, "test3", null,
            new Regexp<AbstractNode>(null, new ArrayList<Regexp<AbstractNode>>(), RegexpType.CONCATENATION));
    final Element e4 = new Element(null, "test4", null,
            new Regexp<AbstractNode>(null, new ArrayList<Regexp<AbstractNode>>(), RegexpType.CONCATENATION));

    e1.getSubnodes().getChildren().add(Regexp.<AbstractNode>getToken(e2));
    e1.getSubnodes().getChildren().add(Regexp.<AbstractNode>getToken(e3));
    e3.getSubnodes().getChildren().add(Regexp.<AbstractNode>getToken(e4));

    final List<Element> elements = Arrays.asList(e1, e2);

    Collections.shuffle(elements);

    final TopologicalSort s = new TopologicalSort(elements);
    final List<Element> toposorted = s.sort();

    assertTrue(toposorted.size() == 2);
    assertTrue(toposorted.indexOf(e1) > toposorted.indexOf(e2));
    assertTrue(toposorted.indexOf(e1) > toposorted.indexOf(e3));
    assertTrue(toposorted.indexOf(e2) > toposorted.indexOf(e4));
  }
}
