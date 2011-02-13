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
package cz.cuni.mff.ksi.jinfer.xsdimportsax.utils;

/**
 * Wrapper class for tag attributes read by SAX parser.
 * All methods are default getters and setters for the fields specified by constructor parameters.
 * @author reseto
 */
public class SAXAttributeData {

  private String uri, local, qname, type, value;

  public SAXAttributeData(final String uri, final String local, final String qname, final String type, final String value) {
    this.uri    = (uri    != null) ? uri    : "";
    this.local  = (local  != null) ? local  : "";
    this.qname  = (qname  != null) ? qname  : "";
    this.type   = (type   != null) ? type   : "";
    this.value  = (value  != null) ? value  : "";
  }

  public SAXAttributeData() {
    this.uri = "";
    this.local = "";
    this.qname = "";
    this.type = "";
    this.value = "";
  }

  public String getLocal() {
    return local;
  }

  public void setLocal(final String local) {
    this.local = local;
  }

  public String getQName() {
    return qname;
  }

  public void setName(final String qname) {
    this.qname = qname;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(final String uri) {
    this.uri = uri;
  }

  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }
}
