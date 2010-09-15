/*
 *  Copyright (C) 2010 reseto
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

package cz.cuni.mff.ksi.jinfer.xsdimporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author reseto
 */
public class XSDDocumentElement {

  private final String trimmedQName;
  private final Map<String, XSDAttributeData> attrs;

  /* determines if the current XSDDocElem is a direct successor
   of named complex type (so it's a sequence/all/choice or complexContent/ext)
  */
  private String associatedCTypeName;

  public XSDDocumentElement(final String trimmedQName) {
    this.trimmedQName = trimmedQName;
    attrs = new HashMap<String, XSDAttributeData>();
    associatedCTypeName = "";
  }

  public String getAssociatedCTypeName() {
    return associatedCTypeName;
  }
  
  public void setAssociatedCTypeName(String associatedCTypeName) {
    this.associatedCTypeName = associatedCTypeName;
  }

  public Map<String, XSDAttributeData> getAttrs() {
    return attrs;
  }

  public String getName() {
    return trimmedQName;
  }
  
  public boolean isNamedComplexType() {
    XSDAttributeData nameAttr = attrs.get("name");
    return (nameAttr != null && !nameAttr.getQName().equals("") && trimmedQName.equalsIgnoreCase("complextype")) ? true : false;
  }

}
