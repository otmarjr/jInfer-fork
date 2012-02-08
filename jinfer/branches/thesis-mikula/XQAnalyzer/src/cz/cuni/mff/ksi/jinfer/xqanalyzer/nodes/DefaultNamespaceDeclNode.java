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

/**
 * The node representing a default namespace declaration.
 *
 * @author Jiri Schejbal
 */
public class DefaultNamespaceDeclNode extends PrologChildNode {

  public DefaultNamespaceDeclNode(
          final XQNode parentNode, DefaultNamespaceKind kind, String targetNamespace) {
    super(parentNode);
    assert (kind != null);
    assert (targetNamespace != null);
    addAttribute(AttrNames.ATTR_KIND, kind.toString());
    addAttribute(AttrNames.ATTR_TARGET_NAMESPACE, targetNamespace);
  }

  @Override
  protected String getElementName() {
    return NodeNames.NODE_DEFAULT_NAMESPACE_DECL;
  }
}
