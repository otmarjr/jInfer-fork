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
package cz.cuni.mff.ksi.jinfer.basicigg.xml;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author vektor
 */
@SuppressWarnings("PMD.SystemPrintln")
public class XMLProcessorTest {

  @Test(expected = RuntimeException.class)
  public void testProcessEmpty() {
    System.out.println("processEmpty");
    final InputStream s = new ByteArrayInputStream("".getBytes());
    new XMLProcessor().process(s);
  }

  @Test(expected = RuntimeException.class)
  public void testProcessEmptyComment() {
    System.out.println("processEmptyComment");
    final InputStream s = new ByteArrayInputStream("<!-- nothing to be seen here -->".getBytes());
    new XMLProcessor().process(s);
  }

  private static final String CARS =
          "<cars/>";

  private static final String[] CARS_RESULTS = {
    "cars: ELEMENT\n()"};

  @Test
  public void testProcessSimple() {
    System.out.println("processSimple");
    final InputStream s = new ByteArrayInputStream(CARS.getBytes());
    final List<Element> result = new XMLProcessor().process(s);
    assertEquals(CARS_RESULTS.length, result.size());
    for (int i = 0; i < result.size(); i++) {
      assertEquals("Iteration " + i, CARS_RESULTS[i], result.get(i).toString());
    }
  }

  private static final String CARS_ATTR =
          "<cars manufacturer=\"audi\"/>";

  private static final String[] CARS_RESULTS_ATTR = {
    "cars: ELEMENT <cars/manufacturer: ATTRIBUTE#null: audi>\n()"};

  @Test
  public void testProcessSimpleAttr() {
    System.out.println("processSimpleAttr");
    final InputStream s = new ByteArrayInputStream(CARS_ATTR.getBytes());
    final List<Element> result = new XMLProcessor().process(s);
    assertEquals(CARS_RESULTS_ATTR.length, result.size());
    for (int i = 0; i < result.size(); i++) {
      assertEquals("Iteration " + i, CARS_RESULTS_ATTR[i], result.get(i).toString());
    }
  }

  private static final String PEOPLE =
          "<people>" +
          "  <person>" +
          "    <name first=\"John\" last=\"Doe\"/>" +
          "  </person>" +
          "  <person>" +
          "    <name first=\"Jane\" last=\"Doe\"/>" +
          "  </person>" +
          "</people>";

  private static final String PEOPLE_COMMENTS =
          "<!-- comment --><people>" +
          "  <person><!---->" +
          "    <name first=\"John\" last=\"Doe\"/><!-- comment -->" +
          "  </person><!-- comment -->" +
          "  <person>" +
          "    <name first=\"Jane\" last=\"Doe\"/>" +
          "  </person>" +
          "</people><!-- comment -->";

  private static final String[] PEOPLE_RESULTS = {
    "people/person/name: ELEMENT <people/person/name/first: ATTRIBUTE#null: John; people/person/name/last: ATTRIBUTE#null: Doe>\n()",
    "people/person: ELEMENT\n(people/person/name: ELEMENT <people/person/name/first: ATTRIBUTE#null: John; people/person/name/last: ATTRIBUTE#null: Doe>\n())",
    "people/person/name: ELEMENT <people/person/name/first: ATTRIBUTE#null: Jane; people/person/name/last: ATTRIBUTE#null: Doe>\n()",
    "people/person: ELEMENT\n(people/person/name: ELEMENT <people/person/name/first: ATTRIBUTE#null: Jane; people/person/name/last: ATTRIBUTE#null: Doe>\n())",
    "people: ELEMENT\n(people/person: ELEMENT\n(people/person/name: ELEMENT <people/person/name/first: ATTRIBUTE#null: John; people/person/name/last: ATTRIBUTE#null: Doe>\n())\n,people/person: ELEMENT\n(people/person/name: ELEMENT <people/person/name/first: ATTRIBUTE#null: Jane; people/person/name/last: ATTRIBUTE#null: Doe>\n()))"};

  @Test
  public void testProcess() {
    System.out.println("process");
    final InputStream s = new ByteArrayInputStream(PEOPLE.getBytes());
    final List<Element> result = new XMLProcessor().process(s);
    assertEquals(PEOPLE_RESULTS.length, result.size());
    for (int i = 0; i < result.size(); i++) {
      assertEquals("Iteration " + i, PEOPLE_RESULTS[i], result.get(i).toString());
    }
    final InputStream sComments = new ByteArrayInputStream(PEOPLE_COMMENTS.getBytes());
    final List<Element> resultComments = new XMLProcessor().process(sComments);
    assertEquals(PEOPLE_RESULTS.length, resultComments.size());
    for (int i = 0; i < resultComments.size(); i++) {
      assertEquals("Iteration " + i, PEOPLE_RESULTS[i], result.get(i).toString());
    }
  }

  private static final String CITIES =
          "<continent name=\"Europe\">" +
          "  <city>" +
          "    Bratislava" +
          "  </city>" +
          "  <city>" +
          "    Prague" +
          "  </city>" +
          "</continent>";

  private static final String[] CITIES_RESULTS = {
    "continent/city: ELEMENT\n(continent/city/Bratislava: SIMPLE_DATA#null: )",
    "continent/city: ELEMENT\n(continent/city/Prague: SIMPLE_DATA#null: )",
    "continent: ELEMENT <continent/name: ATTRIBUTE#null: Europe>\n(continent/city: ELEMENT\n(continent/city/Bratislava: SIMPLE_DATA#null: )\n,continent/city: ELEMENT\n(continent/city/Prague: SIMPLE_DATA#null: ))"};
  @Test
  public void testProcessSimpleData() {
    System.out.println("testProcessSimpleData");
    final InputStream s = new ByteArrayInputStream(CITIES.getBytes());
    final List<Element> result = new XMLProcessor().process(s);
    assertEquals(CITIES_RESULTS.length, result.size());
    for (int i = 0; i < result.size(); i++) {
      assertEquals("Iteration " + i, CITIES_RESULTS[i], result.get(i).toString());
    }
  }

  private static final String HTML =
          "<p>" +
          "  This is a " +
          "  <b>" +
          "  bold" +
          "  </b>" +
          "  text. And some " +
          "  <i>" +
          "  italics" +
          "  </i>" +
          "</p>";

  private static final String[] HTML_RESULTS = {
    "p/b: ELEMENT\n(p/b/bold: SIMPLE_DATA#null: )",
    "p/i: ELEMENT\n(p/i/italics: SIMPLE_DATA#null: )",
    "p: ELEMENT\n(p/This is a: SIMPLE_DATA#null: \n,p/b: ELEMENT\n(p/b/bold: SIMPLE_DATA#null: )\n,p/text. And some: SIMPLE_DATA#null: \n,p/i: ELEMENT\n(p/i/italics: SIMPLE_DATA#null: ))"};

  @Test
  public void testProcessMixed() {
    System.out.println("testProcessMixed");
    final InputStream s = new ByteArrayInputStream(HTML.getBytes());
    final List<Element> result = new XMLProcessor().process(s);
    assertEquals(HTML_RESULTS.length, result.size());
    for (int i = 0; i < result.size(); i++) {
      assertEquals("Iteration " + i, HTML_RESULTS[i], result.get(i).toString());
    }
  }
}
