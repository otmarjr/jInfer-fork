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

import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.FD;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.Logger;
import org.openide.util.Exceptions;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author sviro
 */
public class RXMLTree {

  private static final Logger LOG = Logger.getLogger(RXMLTree.class);
  private final Document document;
  private List<String> paths = null;
  private Map<Node, Set<Tuple>> tuplesMap;
  private List<Tuple> tuples;
  private int tupleID;

  public RXMLTree(final Document document) {
    this.document = document;
    tuplesMap = new HashMap<Node, Set<Tuple>>();
    tupleID = 0;
  }

  public boolean isSatisfyingFD(FD fd) {
    return isTreeSatisfyingFD(fd) || isReliabilitySatisfyFD(fd);
  }

  private boolean isTreeSatisfyingFD(FD fd) {
    if (tuples == null) {
      tuples = TupleFactory.createTuples(this);
    }
    List<Pair<Tuple, Tuple>> tuplePairs = TupleFactory.getTuplePairs(tuples);

    for (Pair<Tuple, Tuple> tuplePair : tuplePairs) {
      Tuple tuple1 = tuplePair.getFirst();
      Tuple tuple2 = tuplePair.getSecond();
      List<PathAnswer> tuple1LeftSideAnswers = TupleFactory.getFDSidePathAnswers(this, tuple1, fd.getLeftSidePaths());
      List<PathAnswer> tuple2LeftSideAnswers = TupleFactory.getFDSidePathAnswers(this, tuple2, fd.getLeftSidePaths());
      List<PathAnswer> tuple1RightSideAnswers = TupleFactory.getFDSidePathAnswers(this, tuple1, fd.getRightSidePaths());
      List<PathAnswer> tuple2RightSideAnswers = TupleFactory.getFDSidePathAnswers(this, tuple2, fd.getRightSidePaths());

      boolean leftSideEqual = areTuplePathAnswersEqual(tuple1LeftSideAnswers, tuple2LeftSideAnswers);
      boolean tuple1PathAnswersNotEmpty = isTuplePathAnswersNotEmpty(tuple1LeftSideAnswers);
      boolean rightSideEqual = areTuplePathAnswersEqual(tuple1RightSideAnswers, tuple2RightSideAnswers);


      if (leftSideEqual && tuple1PathAnswersNotEmpty && !rightSideEqual) {
        return false;
      }
    }

    return true;
  }

  private boolean areTuplePathAnswersEqual(final List<PathAnswer> tuple1Answers, final List<PathAnswer> tuple2Answers) {
    if (tuple1Answers.size() != tuple2Answers.size()) {
      return false;
    }

    for (int i = 0; i < tuple1Answers.size(); i++) {
      PathAnswer tuple1Answer = tuple1Answers.get(i);
      PathAnswer tuple2Answer = tuple2Answers.get(i);

      if (!tuple1Answer.equals(tuple2Answer)) {
        return false;
      }
    }

    return true;
  }

  private boolean isTuplePathAnswersNotEmpty(List<PathAnswer> tuple1LeftSideAnswers) {
    for (PathAnswer pathAnswer : tuple1LeftSideAnswers) {
      if (pathAnswer.isEmpty()) {
        return false;
      }
    }
    return true;
  }

  private boolean isReliabilitySatisfyFD(FD fd) {
    //TODO sviro implement;
    return false;
  }

  public Document getDocument() {
    return document;
  }

  @Override
  public String toString() {
    try {
      TransformerFactory transfac = TransformerFactory.newInstance();
      Transformer trans = transfac.newTransformer();
      trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      trans.setOutputProperty(OutputKeys.INDENT, "yes");

      //create string from xml tree
      StringWriter sw = new StringWriter();
      StreamResult result = new StreamResult(sw);
      DOMSource source = new DOMSource(document);
      trans.transform(source, result);
      return sw.toString();
    } catch (TransformerException ex) {
      Exceptions.printStackTrace(ex);
    }

    return null;
  }

  public List<String> getPaths() {
    return paths;
  }

  public void setPaths(List<String> paths) {
    this.paths = paths;
  }

  public String pathsToString() {
    StringBuilder builder = new StringBuilder();
    for (String path : paths) {
      builder.append(path).append("\n");
    }

    return builder.toString();
  }

  public PathAnswer getPathAnswer(String path) {
    return getPathAnswerForTuple(path, null);
  }

  public PathAnswer getPathAnswerForTuple(final String path, final Tuple tuple) {
    XPathFactory xpathFactory = XPathFactory.newInstance();
    XPath xPath = xpathFactory.newXPath();
    XPathExpression xPathExpression;

    List<Node> result = new ArrayList<Node>();
    try {
      xPathExpression = xPath.compile(path);
      NodeList nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);

      if (tuple != null) {
        for (int i = 0; i < nodeList.getLength(); i++) {
          Node node = nodeList.item(i);
          if (tuplesMap.get(node).contains(tuple)) {
            result.add(node);
          }
        }
//        if (result.size() > 1) {
//          throw new RuntimeException("The path answer for tuple must return max one node");
//        }
        
        return new PathAnswer(result);
      }
      
      return new PathAnswer(nodeList);
    } catch (XPathExpressionException ex) {
      LOG.error("Path " + path + " cannot be compiled or evaluated.", ex);
    }
    
    return null;
  }

  public List<Tuple> getTuples() {
    return tuples;
  }

  public int getNewTupleID() {
    return tupleID++;
  }

  public Map<Node, Set<Tuple>> getTuplesMap() {
    return tuplesMap;
  }
}
