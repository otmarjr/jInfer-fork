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
 *
 * @author sviro
 */
public class PathAnswer {

  private List<Node> nodeAnswers;
  private boolean isValue = false;

  //TODO sviro unit tests
  public PathAnswer(NodeList nodeList) {
    if (nodeList != null && nodeList.getLength() > 0) {
      final short nodeType = nodeList.item(0).getNodeType();
      if (nodeType == Node.TEXT_NODE || nodeType == Node.ATTRIBUTE_NODE) {
        isValue = true;
      }
      nodeAnswers = new ArrayList<Node>();
      for (int i = 0; i < nodeList.getLength(); i++) {
        nodeAnswers.add(nodeList.item(i));
      }

    }
  }

  public PathAnswer(List<Node> nodeList) {
    if (nodeList != null && nodeList.size() > 0) {
      final short nodeType = nodeList.get(0).getNodeType();
      if (nodeType == Node.TEXT_NODE || nodeType == Node.ATTRIBUTE_NODE) {
        isValue = true;
      }
      
      nodeAnswers = new ArrayList<Node>();
      nodeAnswers.addAll(nodeList);
    }
  }

  public boolean isEmpty() {
    return nodeAnswers == null;
  }

  public boolean hasMaxOneElement() {
    return isEmpty() || hasOneElement();
  }

  public boolean hasOneElement() {
    return nodeAnswers != null && nodeAnswers.size() == 1;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof PathAnswer)) {
      return false;
    }

    PathAnswer pathAnswer = (PathAnswer) obj;
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

  public List<Node> getNodeAnswers() {
    if (isEmpty()) {
      return new ArrayList<Node>();
    }

    return nodeAnswers;
  }

  public List<String> getValueAnswers() {
    if (isEmpty()) {
      return new ArrayList<String>();
    }
    
    if (!isValueAnswer()) {
      throw new UnsupportedOperationException(NbBundle.getMessage(PathAnswer.class, "pathAnswer.wrongAnswerType.retreive"));
    }
    List<String> result = new ArrayList<String>();
    for (Node node : nodeAnswers) {
      result.add(node.getNodeValue());
    }

    return result;
  }

  public boolean isNodeAnswer() {
    return !isValueAnswer();
  }

  public boolean isValueAnswer() {
    if (isEmpty()) {
      throw new UnsupportedOperationException("The PathAnswer is empty, cannot recognize correct answer type.");
    }
    return isValue;
  }

  private boolean isSameType(PathAnswer pathAnswer) {
    return (this.isNodeAnswer() && pathAnswer.isNodeAnswer()) || (this.isValueAnswer() && pathAnswer.isValueAnswer());
  }
}
