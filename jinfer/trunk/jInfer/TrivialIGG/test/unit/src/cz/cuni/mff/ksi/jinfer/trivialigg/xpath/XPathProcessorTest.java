/*
 *  Copyright (C) 2010 vitasek
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
package cz.cuni.mff.ksi.jinfer.trivialigg.xpath;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author vektor
 */
public class XPathProcessorTest {

  private static final char NL = '\n';

  private static final String ZOO =
          "zoo" + NL
          + "zoo/lion" + NL
          + "zoo/lion/paw" + NL
          + "zoo/./lion";

  private static String[] ZOO_RESULTS = {
    "zoo: ELEMENT\n()",
    "zoo: ELEMENT\n(lion: ELEMENT\n(),)",
    "lion: ELEMENT\n()",
    "zoo: ELEMENT\n(lion: ELEMENT\n(paw: ELEMENT\n(),),)",
    "lion: ELEMENT\n(paw: ELEMENT\n(),)",
    "paw: ELEMENT\n()",
    "zoo: ELEMENT\n(lion: ELEMENT\n(),)",
    "lion: ELEMENT\n()"};

  @Test
  public void testProcessBasic() {
    System.out.println("processBasic");
    final InputStream s = new ByteArrayInputStream(ZOO.getBytes());
    final XPathProcessor instance = new XPathProcessor();
    final List<AbstractNode> result = instance.process(s);
    assertEquals(ZOO_RESULTS.length, result.size());
    for (int i = 0; i < result.size(); i++) {
      assertEquals(ZOO_RESULTS[i], result.get(i).toString());
    }
  }

  private static final String CARS =
          "car//wheel" + NL
          + "car/front//wheel" + NL;
  
  private static String[] CARS_RESULTS = {
    "car: ELEMENT\n()",
    "wheel: ELEMENT\n()",
    "car: ELEMENT\n(front: ELEMENT\n(),)",
    "front: ELEMENT\n()",
    "wheel: ELEMENT\n()"};

  @Test
  public void testProcessDescendant() {
    System.out.println("testProcessDescendant");
    final InputStream s = new ByteArrayInputStream(CARS.getBytes());
    final XPathProcessor instance = new XPathProcessor();
    final List<AbstractNode> result = instance.process(s);
    assertEquals(CARS_RESULTS.length, result.size());
    for (int i = 0; i < result.size(); i++) {
      assertEquals(CARS_RESULTS[i], result.get(i).toString());
    }
  }

  private static final String PEOPLE =
          "person/@name" + NL +
          "people/person/@name" + NL +
          "person[@name]" + NL +
          "person[@name and @age]" + NL +
          "person[@name = \"john\"]" + NL +
          "person[@name != \"john\"]" + NL;

  private static String[] PEOPLE_RESULTS = {
    "person: ELEMENT\n(name: ATTRIBUTE\nnull: ,)",
    "people: ELEMENT\n(person: ELEMENT\n(name: ATTRIBUTE\nnull: ,),)",
    "person: ELEMENT\n(name: ATTRIBUTE\nnull: ,)",
    "person: ELEMENT\n(name: ATTRIBUTE\nnull: ,)",
    // Explanation: upon finding name, an attribute is added to the element and that element is added to rules.
    // Upon finding age, an attribute is added to the same element and it is added to rules once again.
    // Because we work with references, rules contain two references to the same element with two attributes inside.
    "person: ELEMENT\n(name: ATTRIBUTE\nnull: ,age: ATTRIBUTE\nnull: ,)",
    "person: ELEMENT\n(name: ATTRIBUTE\nnull: ,age: ATTRIBUTE\nnull: ,)",
    // End of explained part.
    "person: ELEMENT\n(name: ATTRIBUTE\nnull: john ,)",
    "person: ELEMENT\n(name: ATTRIBUTE\nnull: ,)"};

  @Test
  public void testProcessAttributes() {
    System.out.println("testProcessAttributes");
    final InputStream s = new ByteArrayInputStream(PEOPLE.getBytes());
    final XPathProcessor instance = new XPathProcessor();
    final List<AbstractNode> result = instance.process(s);
    assertEquals(PEOPLE_RESULTS.length, result.size());
    for (int i = 0; i < result.size(); i++) {
      assertEquals(PEOPLE_RESULTS[i], result.get(i).toString());
    }
  }
}
