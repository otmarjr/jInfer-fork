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
package cz.cuni.mff.ksi.jinfer.functionalDependencies.modelGenerator;

import cz.cuni.mff.ksi.jinfer.functionalDependencies.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handler responsible for creation of paths defined for XML tree.
 * @author sviro
 */
public class PathsContentHandler extends DefaultHandler {

  private final Set<Path> paths;
  private Stack<String> tags;
  String lastClosedTag;
  private static final Logger LOG = Logger.getLogger(PathsContentHandler.class);
  
  private static Comparator<Path> pathComparator = new Comparator<Path>() {

    @Override
    public int compare(Path o1, Path o2) {
      int path1Size = getPathSize(o1);
      int path2Size = getPathSize(o2);
      return path1Size - path2Size;
    }
    
    private int getPathSize(Path path) {
      return path.getPathValue().replaceAll("[^/]", "").length();
    }
  };

  public PathsContentHandler() {
    paths = new HashSet<Path>();
  }

  private String getCurrentXPath(String attribute, boolean isText) {
    String str = "/";
    for (String tag : tags) {
      str += "/" + tag;
    }
    if (attribute != null) {
      str += "/@" + attribute;
    }
    
    if (isText) {
      str += "/text()";
    }
    return str;
  }

  @Override
  public void startDocument() throws SAXException {
    tags = new Stack<String>();
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    boolean isRepeatElement = false;

    if (lastClosedTag != null) {
      // an element was recently closed ...
      if (lastClosedTag.equals(localName)) {
        // ... and it's the same as the current one
        isRepeatElement = true;
      } else {
        // ... but it's different from the current one, so discard it
        tags.pop();
      }
    }

    // if it's not the same element, add the new element
    if (!isRepeatElement) {
      tags.push(localName);
    }

    paths.add(new Path(getCurrentXPath(null, false)));
    addAttributePaths(attributes);



    lastClosedTag = null;
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    paths.add(new Path(getCurrentXPath(null, true)));
  }
  
  
  
  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    // if two tags are closed in succession (without an intermediate opening tag),
    // then the information about the deeper nested one is discarded
    if (lastClosedTag != null) {
      tags.pop();
    }
    lastClosedTag = localName;
  }

  /**
   * Get list of paths defined fo the tree.
   * @return List of paths defined for the tree.
   */
  List<Path> getPaths() {
    List<Path> result = new ArrayList<Path>(paths);
    Collections.sort(result, pathComparator);
    return result;
  }

  private void addAttributePaths(Attributes attributes) {
    for (int i = 0; i < attributes.getLength(); i++) {
      String attrName = attributes.getLocalName(i);
      paths.add(new Path(getCurrentXPath(attrName, false)));
    }
  }
}
