/*
 * Copyright (C) 2011 sviro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.functionalDependencies;

import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static org.junit.Assert.*;
import org.w3c.dom.Text;

/**
 *
 * @author sviro
 */
public class PathAnswerTest {

  public PathAnswerTest() {
  }

  /**
   * Test of isEmpty method, of class PathAnswer.
   */
  @Test
  public void testIsEmpty() {
    List<Node> list = null;
    NodeList nodeList = null;
    PathAnswer instance = new PathAnswer(list, true);
    boolean expResult = true;
    boolean result = instance.isEmpty();
    assertEquals(expResult, result);

    instance = new PathAnswer(nodeList, true);
    result = instance.isEmpty();
    assertEquals(expResult, result);
  }

  /**
   * Test of isEmpty method, of class PathAnswer.
   */
  @Test
  public void testIsEmpty2() throws ParserConfigurationException {
    List<Node> list = new ArrayList<Node>();
    PathAnswer instance = new PathAnswer(list, true);
    boolean expResult = true;
    boolean result = instance.isEmpty();
    assertEquals(expResult, result);

    DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document document = newDocumentBuilder.newDocument();
    NodeList childNodes = document.getChildNodes();
    instance = new PathAnswer(childNodes, true);
    result = instance.isEmpty();
    assertEquals(expResult, result);
  }

  /**
   * Test of isEmpty method, of class PathAnswer.
   */
  @Test
  public void testIsEmpty3() throws ParserConfigurationException {
    DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document document = newDocumentBuilder.newDocument();
    Element element = document.createElement("test");
    document.appendChild(element);

    List<Node> list = new ArrayList<Node>();
    list.add(element);
    PathAnswer instance = new PathAnswer(list, false);
    boolean expResult = false;
    boolean result = instance.isEmpty();
    assertEquals(expResult, result);

    NodeList childNodes = document.getChildNodes();
    instance = new PathAnswer(childNodes, false);
    result = instance.isEmpty();
    assertEquals(expResult, result);
  }

  /**
   * Test of hasMaxOneElement method, of class PathAnswer.
   */
  @Test
  public void testHasMaxOneElement() throws ParserConfigurationException {
    DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document document = newDocumentBuilder.newDocument();
    Element element = document.createElement("test");
    document.appendChild(element);

    List<Node> list = new ArrayList<Node>();
    list.add(element);
    PathAnswer instance = new PathAnswer(list, false);
    boolean expResult = true;
    boolean result = instance.hasMaxOneElement();
    assertEquals(expResult, result);

    list.clear();
    instance = new PathAnswer(list, true);
    result = instance.hasMaxOneElement();
    assertEquals(expResult, result);


    NodeList childNodes = document.getChildNodes();
    instance = new PathAnswer(childNodes, false);
    result = instance.hasMaxOneElement();
    assertEquals(expResult, result);

    document.removeChild(document.getFirstChild());
    childNodes = document.getChildNodes();
    instance = new PathAnswer(childNodes, true);
    result = instance.hasMaxOneElement();
    assertEquals(expResult, result);
  }

  /**
   * Test of hasOneElement method, of class PathAnswer.
   */
  @Test
  public void testHasOneElement() throws ParserConfigurationException {
    DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document document = newDocumentBuilder.newDocument();
    Element element = document.createElement("test");
    document.appendChild(element);

    List<Node> list = new ArrayList<Node>();
    list.add(element);
    PathAnswer instance = new PathAnswer(list, false);
    boolean expResult = true;
    boolean result = instance.hasOneElement();
    assertEquals(expResult, result);

    NodeList childNodes = document.getChildNodes();
    instance = new PathAnswer(childNodes, false);
    result = instance.hasOneElement();
    assertEquals(expResult, result);
  }

  /**
   * Test of equals method, of class PathAnswer.
   */
  @Test
  public void testEquals() {
    Object obj = null;
    PathAnswer instance = new PathAnswer(new ArrayList<Node>(), false);
    boolean expResult = false;
    boolean result = instance.equals(obj);
    assertEquals(expResult, result);
  }

  /**
   * Test of equals method, of class PathAnswer.
   */
  @Test
  public void testEquals2() {
    Object obj = new PathAnswer(new ArrayList<Node>(), false);
    PathAnswer instance = new PathAnswer(new ArrayList<Node>(), true);
    boolean expResult = true;
    boolean result = instance.equals(obj);
    assertEquals(expResult, result);
  }

  /**
   * Test of equals method, of class PathAnswer.
   */
  @Test
  public void testEquals3() throws ParserConfigurationException {
    DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document document = newDocumentBuilder.newDocument();
    Element element = document.createElement("test");

    List<Node> list = new ArrayList<Node>();
    list.add(element);
    PathAnswer obj = new PathAnswer(list, false);
    PathAnswer instance = new PathAnswer(new ArrayList<Node>(), false);
    boolean expResult = false;
    boolean result = instance.equals(obj);
    assertEquals(expResult, result);

    result = obj.equals(instance);
    assertEquals(expResult, result);
  }

  /**
   * Test of equals method, of class PathAnswer.
   */
  @Test
  public void testEquals4() throws ParserConfigurationException {
    DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document document = newDocumentBuilder.newDocument();
    Element element = document.createElement("test");
    document.appendChild(element);
    Text textNode = document.createTextNode("testing");
    element.appendChild(textNode);

    List<Node> list = new ArrayList<Node>();
    list.add(textNode);
    PathAnswer obj = new PathAnswer(list, true);
    PathAnswer instance = new PathAnswer(document.getChildNodes(), false);
    boolean expResult = false;
    boolean result = instance.equals(obj);
    assertEquals(expResult, result);
  }

  /**
   * Test of equals method, of class PathAnswer.
   */
  @Test
  public void testEquals5() throws ParserConfigurationException {
    DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document document = newDocumentBuilder.newDocument();
    Element root = document.createElement("root");
    document.appendChild(root);

    Element element = document.createElement("test");
    root.appendChild(element);
    Text textNode1 = document.createTextNode("testing");
    element.appendChild(textNode1);
    Element element2 = document.createElement("test2");
    root.appendChild(element2);
    Text textNode2 = document.createTextNode("testing");
    element2.appendChild(textNode2);

    List<Node> list = new ArrayList<Node>();
    list.add(element);
    list.add(element2);
    PathAnswer obj = new PathAnswer(list, false);
    PathAnswer instance = new PathAnswer(root.getChildNodes(), false);
    boolean expResult = true;
    boolean result = instance.equals(obj);
    assertEquals(expResult, result);

    list.clear();
    list.add(textNode1);
    obj = new PathAnswer(list, true);
    instance = new PathAnswer(element2.getChildNodes(), true);

    result = instance.equals(obj);
    assertEquals(expResult, result);
  }

  /**
   * Test of getNodeAnswers method, of class PathAnswer.
   */
  @Test
  public void testGetNodeAnswers() {
    PathAnswer instance = new PathAnswer(new ArrayList<Node>(), false);
    List<Node> valueAnswers = instance.getNodeAnswers();
    List<Node> expected = new ArrayList<Node>();
    assertEquals(expected, valueAnswers);
  }

  /**
   * Test of getNodeAnswers method, of class PathAnswer.
   */
  @Test
  public void testGetNodeAnswers2() throws ParserConfigurationException {
    DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document document = newDocumentBuilder.newDocument();
    Text textNode = document.createTextNode("element");
    List<Node> list = new ArrayList<Node>();
    list.add(textNode);

    PathAnswer instance = new PathAnswer(list, true);
    List<Node> nodeAnswers = instance.getNodeAnswers();
    List<Node> expected = new ArrayList<Node>();
    expected.add(textNode);
    assertEquals(expected, nodeAnswers);
  }

  /**
   * Test of getNodeAnswers method, of class PathAnswer.
   */
  @Test
  public void testGetNodeAnswers3() throws ParserConfigurationException {
    DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document document = newDocumentBuilder.newDocument();
    Element element = document.createElement("element");
    List<Node> list = new ArrayList<Node>();
    list.add(element);

    PathAnswer instance = new PathAnswer(list, false);
    List<Node> result = instance.getNodeAnswers();
    List<Node> exptected = list;

    assertEquals(exptected, result);
  }

  /**
   * Test of getValueAnswers method, of class PathAnswer.
   */
  @Test
  public void testGetValueAnswers() throws ParserConfigurationException {
    PathAnswer instance = new PathAnswer(new ArrayList<Node>(), false);
    List<String> valueAnswers = instance.getValueAnswers();
    List<String> expected = new ArrayList<String>();

    assertEquals(expected, valueAnswers);
  }

  /**
   * Test of getValueAnswers method, of class PathAnswer.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAnswers2() throws ParserConfigurationException {
    DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document document = newDocumentBuilder.newDocument();
    Element element = document.createElement("element");
    List<Node> list = new ArrayList<Node>();
    list.add(element);

    PathAnswer instance = new PathAnswer(list, false);
    List<String> valueAnswers = instance.getValueAnswers();
  }

  /**
   * Test of getValueAnswers method, of class PathAnswer.
   */
  @Test
  public void testGetValueAnswers3() throws ParserConfigurationException {
    DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document document = newDocumentBuilder.newDocument();
    Text textNode = document.createTextNode("test");

    List<Node> list = new ArrayList<Node>();
    list.add(textNode);

    PathAnswer instance = new PathAnswer(list, true);
    List<String> result = instance.getValueAnswers();
    List<String> expected = new ArrayList<String>();
    expected.add("test");

    assertEquals(expected, result);
  }

  /**
   * Test of isNodeAnswer method, of class PathAnswer.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void testIsNodeAnswer() {
    PathAnswer instance = new PathAnswer(new ArrayList<Node>(), false);
    boolean result = instance.isNodeAnswer();
  }

  /**
   * Test of isNodeAnswer method, of class PathAnswer.
   */
  @Test
  public void testIsNodeAnswer2() throws ParserConfigurationException {
    DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document document = newDocumentBuilder.newDocument();
    Element element = document.createElement("test");

    List<Node> list = new ArrayList<Node>();
    list.add(element);

    PathAnswer instance = new PathAnswer(list, false);
    boolean expected = true;
    boolean result = instance.isNodeAnswer();
    assertEquals(expected, result);
  }

  /**
   * Test of isValueAnswer method, of class PathAnswer.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void testIsValueAnswer() {
    PathAnswer instance = new PathAnswer(new ArrayList<Node>(), true);
    boolean result = instance.isValueAnswer();
  }

  /**
   * Test of isValueAnswer method, of class PathAnswer.
   */
  @Test
  public void testIsValueAnswer2() throws ParserConfigurationException {
    DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document document = newDocumentBuilder.newDocument();
    Text textNode = document.createTextNode("test");

    List<Node> list = new ArrayList<Node>();
    list.add(textNode);

    PathAnswer instance = new PathAnswer(list, true);
    boolean expected = true;
    boolean result = instance.isValueAnswer();
    assertEquals(expected, result);
  }
}
