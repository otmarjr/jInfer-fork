/*
 *  Copyright (C) 2011 reseto
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

package cz.cuni.mff.ksi.jinfer.xsdimportsax;

import cz.cuni.mff.ksi.jinfer.base.utils.IGGUtils;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDAttribute;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDUtility;
import cz.cuni.mff.ksi.jinfer.xsdimportsax.utils.SAXDocumentElement;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author reseto
 */
public final class SAXHelper {

  private SAXHelper() {}

  /**
   * Prepare metadata of <i>attribute</i> tag stored in the parameter.
   * If the parameter has a defined attribute <i>use</i>, add this information to metadata.
   * @param e Instance of the wrapper class containing the <i>attribute</i> tag,
   * from which the metadata is extracted.
   * @return New metadata.
   */
  public static Map<String, Object> getAttributeMetadata(final SAXDocumentElement e) {
    final Map<String, Object> attrMeta = new HashMap<String, Object>();
    if (XSDUtility.REQUIRED.equals(e.getAttributeValue(XSDAttribute.USE))) {
      attrMeta.put(IGGUtils.REQUIRED, Boolean.TRUE);
    } else if (XSDUtility.OPTIONAL.equals(e.getAttributeValue(XSDAttribute.USE))) {
      attrMeta.put(IGGUtils.REQUIRED, Boolean.FALSE);
    }
    return attrMeta;
  }
}
