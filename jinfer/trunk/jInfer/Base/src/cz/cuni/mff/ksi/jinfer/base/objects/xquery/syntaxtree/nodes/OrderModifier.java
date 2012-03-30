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

/**
 * This class represents the detail of ordering specification.
 *
 * This includes order direction, empty order mode and collation URI.
 *
 * @author Jiri Schejbal
 */
public class OrderModifier {

  private OrderDirection orderDirection;
  private EmptyOrderMode emptyOrderMode;
  private String collationURI;

  public OrderModifier(
          OrderDirection orderDirection, EmptyOrderMode emptyOrderMode,
          String collationURI) {
    this.orderDirection = orderDirection;
    this.emptyOrderMode = emptyOrderMode;
    this.collationURI = collationURI;
  }

  public OrderDirection getOrderDirection() {
    return orderDirection;
  }

  public EmptyOrderMode getEmptyOrderMode() {
    return emptyOrderMode;
  }

  public String getCollationURI() {
    return collationURI;
  }
}
