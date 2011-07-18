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

import cz.cuni.mff.ksi.jinfer.base.interfaces.Pair;
import cz.cuni.mff.ksi.jinfer.base.objects.ImmutablePair;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.ObjectFactory;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.SidePaths;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.TleftSidePaths;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author sviro
 */
public class TupleFactoryTest {

  public TupleFactoryTest() {
  }

  /**
   * Test of getTuplePairs method, of class TupleFactory.
   */
  @Test
  public void testGetTuplePairsNull() throws InterruptedException {
    List<Tuple> tuples = null;
    List expResult = null;
    List result = TupleFactory.getTuplePairs(tuples);
    assertEquals(expResult, result);
  }

  /**
   * Test of getTuplePairs method, of class TupleFactory.
   */
  @Test
  public void testGetTuplePairsEmpty() throws InterruptedException {
    List<Tuple> tuples = new ArrayList<Tuple>();
    List<Pair<Tuple, Tuple>> expResult = new ArrayList<Pair<Tuple, Tuple>>();
    List<Pair<Tuple, Tuple>> result = TupleFactory.getTuplePairs(tuples);
    assertEquals(expResult, result);
  }

  /**
   * Test of getTuplePairs method, of class TupleFactory.
   */
  @Test
  public void testGetTuplePairs() throws InterruptedException {
    List<Tuple> tuples = new ArrayList<Tuple>();
    tuples.add(new Tuple(null, 0));


    List<Pair<Tuple, Tuple>> expResult = new ArrayList<Pair<Tuple, Tuple>>();
    List<Pair<Tuple, Tuple>> result = TupleFactory.getTuplePairs(tuples);
    assertEquals(expResult, result);
  }

  /**
   * Test of getTuplePairs method, of class TupleFactory.
   */
  @Test
  public void testGetTuplePairs2() throws InterruptedException {
    List<Tuple> tuples = new ArrayList<Tuple>();
    Tuple tuple1 = new Tuple(null, 0);

    tuples.add(tuple1);
    tuples.add(tuple1);

    List<Pair<Tuple, Tuple>> expResult = new ArrayList<Pair<Tuple, Tuple>>();
    expResult.add(new ImmutablePair<Tuple, Tuple>(tuple1, tuple1));
    List<Pair<Tuple, Tuple>> result = TupleFactory.getTuplePairs(tuples);
    assertEquals(expResult, result);
  }


  /**
   * Test of getTuplePairs method, of class TupleFactory.
   */
  @Test
  public void testGetTuplePairs3() throws InterruptedException {
    List<Tuple> tuples = new ArrayList<Tuple>();
    Tuple tuple1 = new Tuple(null, 0);
    Tuple tuple2 = new Tuple(null, 1);
    Tuple tuple3 = new Tuple(null, 2);

    tuples.add(tuple1);
    tuples.add(tuple2);
    tuples.add(tuple3);

    List<Pair<Tuple, Tuple>> expResult = new ArrayList<Pair<Tuple, Tuple>>();
    expResult.add(new ImmutablePair<Tuple, Tuple>(tuple1, tuple2));
    expResult.add(new ImmutablePair<Tuple, Tuple>(tuple1, tuple3));
    expResult.add(new ImmutablePair<Tuple, Tuple>(tuple2, tuple3));
    List<Pair<Tuple, Tuple>> result = TupleFactory.getTuplePairs(tuples);
    assertEquals(expResult, result);
  }

  /**
   * Test of getFDSidePathAnswers method, of class TupleFactory.
   */
  @Test(expected=RuntimeException.class)
  public void testGetFDSidePathAnswersNull() throws ParserConfigurationException {
    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document document = builder.newDocument();
    Element element = document.createElement("test");
    document.appendChild(element);

    RXMLTree tree = new RXMLTree(document);
    Tuple tuple = null;
    SidePaths sidePaths = null;
    List<PathAnswer> result = TupleFactory.getFDSidePathAnswers(tree, tuple, sidePaths, false);
  }


  /**
   * Test of getFDSidePathAnswers method, of class TupleFactory.
   */
  @Test(expected=RuntimeException.class)
  public void testGetFDSidePathAnswersNull2() throws ParserConfigurationException {
    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document document = builder.newDocument();
    Element element = document.createElement("test");
    document.appendChild(element);

    RXMLTree tree = new RXMLTree(document);
    Tuple tuple = new Tuple(null, 0);
    SidePaths sidePaths = null;
    List<PathAnswer> result = TupleFactory.getFDSidePathAnswers(tree, tuple, sidePaths, false);
  }


  /**
   * Test of getFDSidePathAnswers method, of class TupleFactory.
   */
  @Test
  public void testGetFDSidePathAnswersEmpty() throws ParserConfigurationException {
    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document document = builder.newDocument();
    Element element = document.createElement("test");
    document.appendChild(element);

    RXMLTree tree = new RXMLTree(document);
    Tuple tuple = new Tuple(null, 0);
    SidePaths sidePaths = new ObjectFactory().createTleftSidePaths();
    List<PathAnswer> result = TupleFactory.getFDSidePathAnswers(tree, tuple, sidePaths, false);
    List<PathAnswer> expected = new ArrayList<PathAnswer>();
    assertEquals(expected, result);
  }

  /**
   * Test of getFDSidePathAnswers method, of class TupleFactory.
   */
  @Test
  public void testGetFDSidePathAnswers() throws ParserConfigurationException {
    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document document = builder.newDocument();
    Element element = document.createElement("test");
    document.appendChild(element);

    RXMLTree tree = new RXMLTree(document);
    Tuple tuple = new Tuple(null, 0);
    tree.getNodesMap().put(element, new NodeAttribute());
    tree.getNodesMap().get(element).addToTuple(tuple);
    TleftSidePaths sidePaths = new ObjectFactory().createTleftSidePaths();
    sidePaths.getPath().add("//bib");
    List<PathAnswer> result = TupleFactory.getFDSidePathAnswers(tree, tuple, sidePaths, false);

    List<PathAnswer> expected = new ArrayList<PathAnswer>();
    List<Node> nodes = new ArrayList<Node>();
    expected.add(new PathAnswer(nodes, false));
    assertEquals(expected, result);
  }
}
