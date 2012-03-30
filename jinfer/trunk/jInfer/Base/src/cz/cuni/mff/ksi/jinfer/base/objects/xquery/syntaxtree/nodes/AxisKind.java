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
 * The enumeration of axis kinds.
 * 
 * @author Jiri Schejbal
 */
public enum AxisKind {

  CHILD(Direction.FORWARD, AttrValues.ATTR_VALUE_CHILD),
  DESCENDANT(Direction.FORWARD, AttrValues.ATTR_VALUE_DESCENDANT),
  ATTRIBUTE(Direction.FORWARD, AttrValues.ATTR_VALUE_ATTRIBUTE),
  SELF(Direction.FORWARD, AttrValues.ATTR_VALUE_SELF),
  DESCENDANT_OR_SELF(Direction.FORWARD,
  AttrValues.ATTR_VALUE_DESCENDANT_OR_SELF),
  FOLLOWING_SIBLING(Direction.FORWARD,
  AttrValues.ATTR_VALUE_FOLLOWING_SIBLING),
  FOLLOWING(Direction.FORWARD, AttrValues.ATTR_VALUE_FOLLOWING),
  PARENT(Direction.REVERSE, AttrValues.ATTR_VALUE_PARENT),
  ANCESTOR(Direction.REVERSE, AttrValues.ATTR_VALUE_ANCESTOR),
  PRECEDING_SIBLING(Direction.REVERSE,
  AttrValues.ATTR_VALUE_PRECEDING_SIBLING),
  PRECEDING(Direction.REVERSE, AttrValues.ATTR_VALUE_PRECEDING),
  ANCESTOR_OR_SELF(Direction.REVERSE,
  AttrValues.ATTR_VALUE_ANCESTOR_OR_SELF);
  private String value;
  private Direction direction;

  private AxisKind(Direction direction, String value) {
    this.direction = direction;
    this.value = value;
  }

  /**
   * Gets the string representing the axis direction.
   *
   * @return Axis direction.
   */
  public String getDirectionValue() {
    return direction.toString();
  }

  /**
   * Gets the axis name.
   *
   * @return Name of axis.
   */
  @Override
  public String toString() {
    return value;
  }

  private enum Direction {

    FORWARD(AttrValues.ATTR_VALUE_FORWARD),
    REVERSE(AttrValues.ATTR_VALUE_REVERSE);
    private String value;

    private Direction(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value;
    }
  }
}
