/*
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

/*
 * This code originates from Jiří Schejbal's master thesis. Jiří Schejbal
 * is also the author of the original version of this code.
 * With his approval, we use his code in jInfer and we slightly modify it to
 * suit our cause.
 */
package cz.cuni.mff.ksi.jinfer.xqanalyzer.nodes;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is a base class for all XML nodes used for XML representation
 * of an XQuery program.
 *
 * All has to be associated with the XQDocument instance.
 *
 * @author Jiri Schejbal
 * 
 * @see XQDocument
 */
public abstract class XQNode {

  private final XQNode parentNode;
  private Map<String, String> attributes = new HashMap<String, String>();

  /**
   * Creates the XML node associated with the given XQuery XML document.
   *
   * @param xqDocument The reference to XQuery XML document this node is
   *      associated to.
   */
  public XQNode(final XQNode parentNode) {
    this.parentNode = parentNode;
  }

  /**
   * Gets the name of the element representing the XQuery construction.
   *
   * @return Name of element.
   */
  protected abstract String getElementName();

  /**
   * Add a new attribute to this node.
   *
   * @param name The name of the attribute.
   * @param value The value of the attribute.
   */
  protected void addAttribute(String name, String value) {
    attributes.put(name, value);
  }
  /** TODO co je toto?
   * Adds text content for this node.
   *
   * @param text Text content.
   */
  /*protected void addTextContent(String text) {
  if (text != null) {
  element.setTextContent(text);
  }
  }*/
}
