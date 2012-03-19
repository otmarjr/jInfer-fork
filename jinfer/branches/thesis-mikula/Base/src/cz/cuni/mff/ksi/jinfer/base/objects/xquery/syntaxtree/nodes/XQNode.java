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
package cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is a base class for all XML nodes used for XML representation
 * of an XQuery program.
 *
 * All has to be associated with the XQDocument instance.
 *
 * @author Jiri Schejbal
 */
public abstract class XQNode {

  private XQNode parentNode;
  private Map<String, String> attributes = new HashMap<String, String>();
  private String textContent;

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
  public void addAttribute(String name, String value) {
    attributes.put(name, value);
  }
  
  /** TODO rio What is this for?
   * Adds text content for this node.
   *
   * @param text Text content.
   */
  protected void addTextContent(String text) {
    if (text != null) {
      textContent = text;
    }
  }
  
  public abstract List<XQNode> getSubnodes();
  
  public XQNode getParentNode() {
    return parentNode;
  }
  
  public void setParentNode(final XQNode parentNode) {
    if (this.parentNode != null) {
      System.out.println(this.parentNode);
      System.out.println(parentNode);
      assert(this.parentNode == null);
    }
    this.parentNode = parentNode;
  }
}
