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
package cz.cuni.mff.ksi.jinfer.basicigg.xpath;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.TestUtils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author vektor
 */
@SuppressWarnings("PMD.SystemPrintln")
public class XPathProcessorTest {

  private static final char NL = '\n';

  @Test
  public void testProcessEmpty() {
    System.out.println("processEmpty");
    final InputStream s = new ByteArrayInputStream("".getBytes());
    final XPathProcessor instance = new XPathProcessor();
    final List<Element> result = instance.process(s);
    assertEquals(0, result.size());
  }

  @Test
  public void testProcessEmptyComment() {
    System.out.println("processEmptyComment");
    final InputStream s = new ByteArrayInputStream("# this is a comment".getBytes());
    final XPathProcessor instance = new XPathProcessor();
    final List<Element> result = instance.process(s);
    assertEquals(0, result.size());
  }

  private static final String ZOO =
          "zoo" + NL
          + "# this must not show" + NL
          + NL
          + "   " + NL
          + "   " + NL + NL
          + "zoo/lion" + NL
          + "zoo/lion/paw" + NL
          + "zoo/./lion" + NL + NL
          + "giraffe/../zebra" + NL
          + "giraffe/..//paw";

  private static final String[] ZOO_RESULTS = {
    "zoo: ELEMENT\n()",
    "zoo: ELEMENT\n(lion: ELEMENT\n())",
    "lion: ELEMENT\n()",
    "zoo: ELEMENT\n(lion: ELEMENT\n(paw: ELEMENT\n()))",
    "lion: ELEMENT\n(paw: ELEMENT\n())",
    "paw: ELEMENT\n()",
    "zoo: ELEMENT\n(lion: ELEMENT\n())",
    "lion: ELEMENT\n()",
    "giraffe: ELEMENT\n()",
    "zebra: ELEMENT\n()",
    "giraffe: ELEMENT\n()",
    "paw: ELEMENT\n()"};

  @Test
  public void testProcessBasic() {
    System.out.println("processBasic");
    final InputStream s = new ByteArrayInputStream(ZOO.getBytes());
    final XPathProcessor instance = new XPathProcessor();
    final List<Element> result = instance.process(s);
    assertEquals(ZOO_RESULTS.length, result.size());
    for (int i = 0; i < result.size(); i++) {
      assertEquals("Iteration " + i, ZOO_RESULTS[i], result.get(i).toString());
    }
  }

  private static final String CARS =
          "# this must not show" + NL
          + "car//wheel" + NL
          + "car/front//wheel" + NL
          + "# this must not show" + NL;
  
  private static final String[] CARS_RESULTS = {
    "car: ELEMENT\n()",
    "wheel: ELEMENT\n()",
    "car: ELEMENT\n(front: ELEMENT\n())",
    "front: ELEMENT\n()",
    "wheel: ELEMENT\n()"};

  @Test
  public void testProcessDescendant() {
    System.out.println("testProcessDescendant");
    final InputStream s = new ByteArrayInputStream(CARS.getBytes());
    final XPathProcessor instance = new XPathProcessor();
    final List<Element> result = instance.process(s);
    assertEquals(CARS_RESULTS.length, result.size());
    for (int i = 0; i < result.size(); i++) {
      assertEquals("Iteration " + i, CARS_RESULTS[i], result.get(i).toString());
    }
  }

  private static final String PEOPLE =
          "person/@name" + NL
          + "people/person/@name" + NL
          + "person[@name]" + NL
          + "person[@name and @age]" + NL
          + "person[@name = \"john\"]" + NL
          + "person[@name != \"john\"]" + NL
          + "# this must not show" + NL;

  private static final String[] PEOPLE_RESULTS = {
    "person:name{()}",
    "people{(person:name{()})}",
    "person:name{()}",
    "person:name{()}",
    // Explanation: upon finding name, an attribute is added to the element and that element is added to rules.
    // Upon finding age, an attribute is added to the same element and it is added to rules once again.
    // Because we work with references, rules contain two references to the same element with two attributes inside.
    "person:name,age{()}",
    "person:name,age{()}",
    // End of explained part.
    "person:name{()}",
    "person:name{()}"};

  @Test
  public void testProcessAttributes() {
    System.out.println("testProcessAttributes");
    final InputStream s = new ByteArrayInputStream(PEOPLE.getBytes());
    final XPathProcessor instance = new XPathProcessor();
    final List<Element> result = instance.process(s);
    assertEquals(PEOPLE_RESULTS.length, result.size());
    for (int i = 0; i < result.size(); i++) {
      assertEquals("Iteration " + i, PEOPLE_RESULTS[i], TestUtils.elementToStr(result.get(i)));
    }
  }
  
  private static final String PEOPLE2 =
          "person[@name]/address";

  private static final String[] PEOPLE_RESULTS2 = {
    // Explanation: the same as above. First rule is for the relation
    // person -> @name, second for person -> address.
    "person:name{(address{()})}",
    "person:name{(address{()})}",
    // End of explained part.
    "address{()}"
  };

  @Test
  public void testProcessAttributes2() {
    System.out.println("testProcessAttributes2");
    final InputStream s = new ByteArrayInputStream(PEOPLE2.getBytes());
    final XPathProcessor instance = new XPathProcessor();
    final List<Element> result = instance.process(s);
    assertEquals(PEOPLE_RESULTS2.length, result.size());
    for (int i = 0; i < result.size(); i++) {
      assertEquals("Iteration " + i, PEOPLE_RESULTS2[i], TestUtils.elementToStr(result.get(i)));
    }
  }

  private static final String BOOKS =
          "chapter/text()" + NL
          + "chapter//text()" + NL
          + "chapter[text()]" + NL
          + "chapter[text() = \"Lorem ipsum\"]" + NL
          + "chapter[text() = \"Lorem ipsum\"]/b";

  private static final String[] BOOKS_RESULTS = {
    "chapter{(\"null\")}",
    "chapter{()}",
    "chapter{(\"null\")}",
    "chapter{(\"null\",\"Lorem ipsum\")}",
    "chapter{(\"null\",\"Lorem ipsum\")}",
    "chapter{(\"null\",\"Lorem ipsum\",b{()})}",
    "chapter{(\"null\",\"Lorem ipsum\",b{()})}",
    "chapter{(\"null\",\"Lorem ipsum\",b{()})}",
    "b{()}"};

  @Test
  public void testProcessSimpleData() {
    System.out.println("testProcessSimpleData");
    final InputStream s = new ByteArrayInputStream(BOOKS.getBytes());
    final XPathProcessor instance = new XPathProcessor();
    final List<Element> result = instance.process(s);
    assertEquals(BOOKS_RESULTS.length, result.size());
    for (int i = 0; i < result.size(); i++) {
      assertEquals("Iteration " + i, BOOKS_RESULTS[i], TestUtils.elementToStr(result.get(i)));
    }
  }
}
