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
import org.openide.util.NbBundle;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class representing answer of the path, i.e. it contains list of nodes that 
 * satisfies this path.
 * @author sviro
 */
public class PathAnswer {

  private List<Node> nodeAnswers;
  private boolean isValue = false;

  /**
   * Constructor creating the PathAnswer. It gets as parameters {@link NodeList}
   * containing nodes satisfying path and flag describing if the nodes are value
   * nodes (text values of elements or attributes).
   * @param nodeList NodeList of nodes that satisfies path.
   * @param isValue flag representing if the answer point to value nodes.
   */
  public PathAnswer(final NodeList nodeList, final boolean isValue) {
    if (nodeList != null && nodeList.getLength() > 0) {
      final short nodeType = nodeList.item(0).getNodeType();
      final boolean isValueNode = nodeType == Node.TEXT_NODE || nodeType == Node.ATTRIBUTE_NODE;
      if (isValueNode != isValue) {
        throw new RuntimeException("isValue not match actual content of a node list.");
      }

      this.isValue = isValue;
      nodeAnswers = new ArrayList<Node>();
      for (int i = 0; i < nodeList.getLength(); i++) {
        nodeAnswers.add(nodeList.item(i));
      }

    }
  }

  /**
   * Constructor creating the PathAnswer. It gets as parameters {@link List}
   * containing nodes satisfying path and flag describing if the nodes are value
   * nodes (text values of elements or attributes).
   * @param nodeList List of nodes that satisfies path.
   * @param isValue flag representing if the answer point to value nodes.
   */
  public PathAnswer(final List<Node> nodeList, final boolean isValue) {
    if (nodeList != null && !nodeList.isEmpty()) {
      final short nodeType = nodeList.get(0).getNodeType();
      final boolean isValueNode = nodeType == Node.TEXT_NODE || nodeType == Node.ATTRIBUTE_NODE;
      if (isValueNode != isValue) {
        throw new RuntimeException("isValue not match actual content of a node list.");
      }

      this.isValue = isValue;

      nodeAnswers = new ArrayList<Node>();
      nodeAnswers.addAll(nodeList);
    }
  }

  /**
   * Check if the answer is empty, i.e. number of nodes satisfying path is equal to zero.
   * @return true if answer contains zero nodes.
   */
  public boolean isEmpty() {
    return nodeAnswers == null;
  }

  /**
   * Check if answer contains one or zero nodes.
   * @return true if answer contains one or zero nodes.
   */
  public boolean hasMaxOneElement() {
    return isEmpty() || hasOneElement();
  }

  /**
   * Check if answer contains exactly one node.
   * @return true if answer contains exactly one node.
   */
  public boolean hasOneElement() {
    return nodeAnswers != null && nodeAnswers.size() == 1;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof PathAnswer)) {
      return false;
    }

    final PathAnswer pathAnswer = (PathAnswer) obj;
    if (this.isEmpty() && pathAnswer.isEmpty()) {
      return true;
    }

    if (this.isEmpty() || pathAnswer.isEmpty()) {
      return false;
    }

    if (!this.isSameType(pathAnswer)) {
      return false;
    }

    if (this.isValueAnswer()) {
      return this.getValueAnswers().equals(pathAnswer.getValueAnswers());
    }

    return this.getNodeAnswers().equals(pathAnswer.getNodeAnswers());
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (this.nodeAnswers != null ? this.nodeAnswers.hashCode() : 0);
    hash = 67 * hash + (this.isValue ? 1 : 0);
    return hash;
  }

  /**
   * Get list of all nodes that satisfies the path.
   * @return List of nodes that satisfies the path.
   */
  public List<Node> getNodeAnswers() {
    if (isEmpty()) {
      return new ArrayList<Node>();
    }

    return nodeAnswers;
  }

  /**
   * Get list of string values of all nodes that satisfies the path. This method is valid only
   * if this path answer is a value answer, otherwise throws {@link UnsupportedOperationException}
   * @return List of string values of all nodes that satisfies the path.
   */
  public List<String> getValueAnswers() {
    if (isEmpty()) {
      return new ArrayList<String>();
    }

    if (!isValueAnswer()) {
      throw new UnsupportedOperationException(NbBundle.getMessage(PathAnswer.class, "pathAnswer.wrongAnswerType.retreive"));
    }
    final List<String> result = new ArrayList<String>();
    for (Node node : nodeAnswers) {
      result.add(node.getNodeValue());
    }

    return result;
  }

  /**
   * Check if this answer is node answer, i.e. is not a value answer.
   * @return true if this answer is a node answer.
   */
  public boolean isNodeAnswer() {
    return !isValueAnswer();
  }

  /**
   * Check if this answer is a value answer.
   * @return true if this answer is a value answer.
   */
  public boolean isValueAnswer() {
    if (isEmpty()) {
      throw new UnsupportedOperationException("The PathAnswer is empty, cannot recognize correct answer type.");
    }
    return isValue;
  }

  /**
   * Check if provided pathAnswer is the same type as this answer, i.e. if both answers are 
   * value answers or node answers.
   * @param pathAnswer Path answer to be check against.
   * @return true if both answers have the same type.
   */
  private boolean isSameType(final PathAnswer pathAnswer) {
    return (this.isNodeAnswer() && pathAnswer.isNodeAnswer()) || (this.isValueAnswer() && pathAnswer.isValueAnswer());
  }

  /**
   * Get node this answer contains. If answer contains more then one node it throws {@link UnsupportedOperationException}.
   * If this answer is empty return null.
   * @return Node this answer contains.
   */
  public Node getTupleNodeAnswer() {
    if (!hasMaxOneElement()) {
      throw new UnsupportedOperationException("PathAnswer is not an answer for a tuple.");
    }

    if (hasOneElement()) {
      return nodeAnswers.get(0);
    }

    return null;
  }

  /**
   * Get value of tje node this answer contains. This answer must be value type. 
   * If answer contains more then one node it throws {@link UnsupportedOperationException}.
   * If this answer is empty return null.
   * @return Value of the node this answer contains.
   */
  public String getTupleValueAnswer() {
    if (!hasMaxOneElement()) {
      throw new UnsupportedOperationException("PathAnswer is not an answer for a tuple.");
    }

    if (hasOneElement()) {
      return nodeAnswers.get(0).getNodeValue();
    }

    return null;
  }
}